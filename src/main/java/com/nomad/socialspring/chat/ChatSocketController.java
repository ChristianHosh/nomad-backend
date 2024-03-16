package com.nomad.socialspring.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

  private final ChatService chatService;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/messages")
  public void chat(ChatMessageRequest request) {
    ChatMessageResponse messageResponse = chatService.chat(request);

    messagingTemplate.convertAndSend("/channel/chat/" + messageResponse.getChatChannelUUID(), messageResponse);
  }
}
