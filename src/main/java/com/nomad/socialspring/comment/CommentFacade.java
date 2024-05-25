package com.nomad.socialspring.comment;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.post.Post;
import com.nomad.socialspring.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentFacade {

  private final CommentRepository repository;

  public void formatComment(Comment comment, Set<User> mentionedUsers) {
    Set<String> usernames = mentionedUsers.stream()
        .map(User::getUsername)
        .collect(Collectors.toSet());

    var content = comment.getContent();
    var builder = new StringBuilder();
    builder.append("<span>");
    int index = 0;
    while (index < content.length()) {
      int start = content.indexOf("@", index);
      if (start == -1) {
        builder.append(content.substring(index));
        break;
      }

      builder.append(content, index, start);

      int end = content.indexOf(" ", start);
      if (end == -1) {
        builder.append(content.substring(start));
        break;
      }

      String username = content.substring(start + 1, end);
      if (usernames.contains(username)) {
        builder.append("<a class='hover:underline font-semibold' href='/@")
            .append(username).append("'>")
            .append("@").append(username)
            .append("</a>");
      } else {
        builder.append("@").append(username);
      }
      builder.append(" ");

      index = end + 1;
    }

    comment.setContent(builder.append("</span>").toString());
  }


  public Comment save(CommentRequest commentRequest, User user, Post post) {
    return save(CommentMapper.requestToEntity(commentRequest, user, post));
  }

  public Comment save(Comment comment) {
    return repository.save(comment);
  }

  public Page<Comment> findAllByPost(Post post, int page, int size) {
    return repository.findByPost(post, PageRequest.of(page, size));
  }

  public Comment findById(Long id) {
    return repository.findById(id)
        .orElseThrow(BxException.xNotFound(Comment.class, id));
  }

  public void delete(Comment comment) {
    repository.delete(comment);
  }
}
