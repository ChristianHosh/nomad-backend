package com.nomad.socialspring.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationCreator {

  private final LocationRepository locationRepository;

  @EventListener(ApplicationReadyEvent.class)
  void createLocations() {
    File generatedJsonFile = new File("./online/loc_generated.json");
    File locationsJsonFile = new File("./online/loc_json.json");

    if (!locationsJsonFile.exists()) {
      log.error("Could not find loc_json.json file, will not create locations");
      return;
    }

    HashMap<String, List<String>> countryLocations = new HashMap<>();
    try {
      String locationsJson = Files.readString(locationsJsonFile.toPath());
      String generatedJson = null;
      try {
        generatedJson = Files.readString(generatedJsonFile.toPath());
      } catch (IOException e) {
        log.warn("Could not find generated.json file");
      }

      if (Objects.equals(locationsJson, generatedJson)) {
        log.info("no changes to loc_generated.json file");
        return;
      }


      JSONArray countriesArray = new JSONArray(locationsJson);
      for (Object obj : countriesArray) {
        if (obj instanceof JSONObject jsonObject) {
          String countryName = jsonObject.getString("name");
          JSONArray locationsArray = jsonObject.getJSONArray("locations");
          countryLocations.put(countryName, new ArrayList<>(locationsArray.length()));
          for (Object locObj : locationsArray) {
            if (locObj instanceof String locationName) {
              countryLocations.get(countryName).add(locationName);
            }
          }
        }
      }
      try {
        Files.write(generatedJsonFile.toPath(), locationsJson.getBytes());
      } catch (IOException e) {
        log.error("Could not write generated.json file", e);
      }
    } catch (IOException e) {
      log.error("Could not read generated.json file", e);
      return;
    }


    for (Map.Entry<String, List<String>> entry : countryLocations.entrySet()) {
      String countryName = entry.getKey();
      List<String> locationNames = entry.getValue();
      List<Location> locations = new ArrayList<>();

      Location country = new Location(countryName, null);
      locations.add(country);
      for (String locationName : locationNames) {
        Location location = new Location(locationName, country);
        locations.add(location);
      }

      for (Location location : locations) {
        try {
          locationRepository.save(location);
        } catch (Exception e) {
          log.error("Could not save location [%s]".formatted(location));
        }
      }
    }
    log.info("done saving locations");
  }
}
