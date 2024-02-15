package com.nomad.socialspring.recommender;

import lombok.Getter;

@Getter
public enum Event {
  FAVORITE(10),
  VIEW(2),
  JOIN(5),
  COMMENT(3),
  LIKE(2),
  LIKE_COMMENT(1);

  private final int strength;

  Event(int strength) {
    this.strength = strength;
  }
}
