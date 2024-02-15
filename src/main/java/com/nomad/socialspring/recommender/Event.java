package com.nomad.socialspring.recommender;

import lombok.Getter;

@Getter
public enum Event {
  VIEW(5),
  LIKE(1),
  COMMENT(3),
  LIKE_COMMENT(1),
  JOIN(5);
  
  private final int strength;
  Event(int strength) {
    this.strength = strength;
  }
}
