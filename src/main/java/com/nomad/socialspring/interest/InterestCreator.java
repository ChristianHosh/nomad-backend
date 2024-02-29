package com.nomad.socialspring.interest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InterestCreator {

  private final InterestRepository interestRepository;

  private final List<String> interestNames = List.of(
          "Hiking",
          "Camping",
          "Volunteering",
          "Partying",
          "Nature Retreat",
          "History",
          "Cultural",
          "Religious",
          "Kid-Friendly",
          "Backpacking",
          "Fine-Dining",
          "Meditation",
          "Photography",
          "Culinary",
          "Kayaking",
          "Safari",
          "Scuba-Diving",
          "Water Activities",
          "Theme Parks",
          "Stargazing"
  );

  @EventListener(ApplicationReadyEvent.class)
  void createInterests() {
    List<Interest> interests = new LinkedList<>();

    for (String name : interestNames) {
      interests.add(new Interest(name));
    }

    for (Interest interest : interests) {
      try {
        interestRepository.save(interest);
      } catch (Exception e) {
        log.info("Could not save interest [%s] [%s]".formatted(interest, interest.getName()));
      }
    }
    log.info("Done saving interests");
  }


}
