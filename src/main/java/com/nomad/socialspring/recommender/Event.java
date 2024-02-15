package com.nomad.socialspring.recommender;

import lombok.Getter;

@Getter
public enum Event {
  VIEW(10),
  LIKE(20),
  COMMENT(30),
  LIKE_COMMENT(12.5),
  FOLLOW(50),
  RECENCY(0.5);
  
  private final double factor;
  Event(double factor) {
    this.factor = factor;
  }
}
