package com.nomad.socialspring.location;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  private static final String SEP = File.pathSeparator;


  @EventListener(ApplicationReadyEvent.class)
  void createLocations() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    File generatedJsonFile = new File("." + SEP + "online" + SEP + "loc_generated.json");
    File locationsJsonFile = new File("." + SEP + "online" + SEP + "loc_json.json");

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


      for (JsonElement jsonElement : gson.fromJson(locationsJson, JsonArray.class)) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String countryName = jsonObject.get("name").getAsString();
        JsonArray locationsJsonArray = jsonObject.get("locations").getAsJsonArray();
        countryLocations.put(countryName, new ArrayList<>(locationsJsonArray.size()));
        for (JsonElement locationElement : locationsJsonArray) {
          String locationName = locationElement.getAsString();
          countryLocations.get(countryName).add(locationName);
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
