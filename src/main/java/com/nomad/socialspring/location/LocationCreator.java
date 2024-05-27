package com.nomad.socialspring.location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationCreator {

  private final LocationRepository locationRepository;


  @EventListener(ApplicationReadyEvent.class)
  void createLocations() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    File generatedJsonFile = new File("./online/loc_generated.json");
    File locationsJsonFile = new File("./online/loc_new.json");

    if (!locationsJsonFile.exists()) {
      log.error("Could not find loc_new.json file, will not create locations");
      return;
    }

    try {
      String locationsJson = Files.readString(locationsJsonFile.toPath());
      String generatedJson = readGeneratedJson(generatedJsonFile.toPath());

      if (Objects.equals(locationsJson, generatedJson)) {
        log.info("no changes to loc_generated.json file");
      }

      List<Location> locationsToSave = new LinkedList<>();
      for (ParentJson parentJson : gson.fromJson(locationsJson, new TypeToken<List<ParentJson>>() {
      })) {
        Location country = locationRepository.findById(parentJson.id()).orElseGet(Location::new);
        country.setName(parentJson.name());
        country.setImageUrl(parentJson.imageUrl());
        country.setAbout(parentJson.about());
        country.setBelongsTo(null);
        country.setLocations(new HashSet<>());

        locationsToSave.add(country);
        for (ChildJson childJson : parentJson.locations()) {
          Location location = locationRepository.findById(childJson.id()).orElseGet(Location::new);
          location.setName(childJson.name());
          location.setImageUrl(childJson.imageUrl());
          location.setAbout(childJson.about());
          location.setBelongsTo(country);
          country.getLocations().add(location);
          locationsToSave.add(location);
        }
      }

      if (!savedAll(locationsToSave))
        return;

      saveGeneratedJson(generatedJsonFile.toPath(), locationsJson.getBytes());
    } catch (IOException e) {
      log.error("Could not read generated.json file", e);
      return;
    }

    log.info("done saving locations");
  }

  private String readGeneratedJson(Path path) {
    try {
      return Files.readString(path);
    } catch (IOException e) {
      log.warn("Could not find generated.json file");
      return null;
    }
  }

  private void saveGeneratedJson(Path path, byte[] bytes) {
    try {
      Files.write(path, bytes);
    } catch (IOException e) {
      log.error("Could not write generated.json file", e);
    }
  }

  private boolean savedAll(List<Location> locationsToSave) {
    try {
      locationRepository.saveAll(locationsToSave);
      return true;
    } catch (Exception e) {
      log.error("Could not save locations", e);
      return false;
    }
  }

  public record ParentJson(Long id, String name, String imageUrl, String about, List<ChildJson> locations) {
  }

  public record ChildJson(Long id, String name, String imageUrl, String about) {
  }
}
