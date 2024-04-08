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
          "Hiking",   // 1
          "Camping",              // 2
          "Volunteering",         // 3
          "Partying",             // 4
          "Nature Retreat",       // 5
          "History",              // 6
          "Cultural",             // 7
          "Religious",            // 8
          "Kid-Friendly",         // 9
          "Backpacking",          // 10
          "Fine-Dining",          // 11
          "Meditation",           // 12
          "Photography",          // 13
          "Culinary",             // 14
          "Kayaking",             // 15
          "Safari",               // 16
          "Scuba-Diving",         // 17
          "Water Activities",     // 18
          "Theme Parks",          // 19
          "Stargazing",           // 20
          "General"             // 21
  );

  @EventListener(ApplicationReadyEvent.class)
  void createInterests() {
    if (interestRepository.count() == interestNames.size()) {
      log.info("no changes to interests");
      return;
    }

    List<Interest> interests = new LinkedList<>();

    for (String name : interestNames) {
      interests.add(new Interest(name));
    }

    List<String> failedInterests = new LinkedList<>();
    for (Interest interest : interests) {
      try {
        interestRepository.save(interest);
      } catch (Exception ignored) {
        failedInterests.add(interest.getName());
      }
    }
    log.warn("Could not save the following interests [%s]".formatted(failedInterests));
    log.info("Done saving interests");
  }


}
