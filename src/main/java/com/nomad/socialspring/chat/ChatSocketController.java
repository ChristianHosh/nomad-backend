package com.nomad.socialspring.chat;

import com.nomad.socialspring.user.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

  private final ChatService chatService;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/messages")
  public void chat(ChatMessageRequest request) {
    ChatMessageResponse messageResponse = chatService.chat(request);

    messagingTemplate.convertAndSend("/channel/chat/" + messageResponse.chatChannelId(), messageResponse);
  }
}
