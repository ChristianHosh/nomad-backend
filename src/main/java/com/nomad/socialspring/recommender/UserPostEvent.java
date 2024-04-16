package com.nomad.socialspring.recommender;

import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;

public record UserPostEvent(User user, Post post, Event event, boolean increment) {

  public UserPostEvent(User user, Post post, Event event) {
    this(user, post, event, true);
  }

  @Override
  public String toString() {
    return "UserPostEvent{" +
            "user=" + user +
            ", post=" + post +
            ", event=" + event +
            ", increment=" + increment +
            '}';
  }
}
