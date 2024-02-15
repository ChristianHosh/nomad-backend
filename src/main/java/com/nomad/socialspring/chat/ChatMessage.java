package com.nomad.socialspring.chat;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_CHAT_MESSAGE")
public class ChatMessage extends BaseEntity {

  @Column(name = "CONTENT", nullable = false)
  @Size(max = 255)
  private String content;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, optional = false)
  @JoinColumn(name = "CHAT_CHANNEL_ID", nullable = false)
  private ChatChannel chatChannel;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "SENDER_ID", unique = true)
  private User sender;

}