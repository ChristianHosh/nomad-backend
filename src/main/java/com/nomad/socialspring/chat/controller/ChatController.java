package com.nomad.socialspring.chat.controller;

import com.nomad.socialspring.chat.dto.*;
import com.nomad.socialspring.chat.service.ChatService;
import com.nomad.socialspring.user.dto.UserResponse;
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
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/messages")
    public void chat(ChatMessageRequest request) {
        ChatMessageResponse messageResponse = chatService.chat(request);

        messagingTemplate.convertAndSend("/channel/chat/" + messageResponse.chatChannelId(), messageResponse);
    }

    @PutMapping("/api/chat/read")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateMessageReadStatus(
            @RequestBody @Valid ChatMessageReadRequest request
    ) {
        return chatService.updateMessageReadStatus(request);
    }

    @PostMapping("/api/chat/channel")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ChatChannelResponse createNewChannel(
            @RequestBody @Valid ChatChannelRequest request
    ) {
        return chatService.createNewChannel(request);
    }

    @GetMapping("/api/chat/channel/{id}/users")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Page<UserResponse> getChannelUsers(
            @PathVariable(name = "id") String channelId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size
    ) {
        return chatService.getChannelUsers(channelId, page, size);
    }
    @PutMapping("/api/chat/channel/{id}/users")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ChatChannelResponse addNewUsersToChannel(
            @PathVariable(name = "id") String channelId,
            @RequestBody @Valid ChatChannelUsersRequest request
    ) {
        return chatService.addNewUsersToChannel(channelId, request);
    }

    @DeleteMapping("/api/chat/channel/{id}/users")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ChatChannelResponse removeUsersFromChannel(
            @PathVariable(name = "id") String channelId,
            @RequestBody @Valid ChatChannelUsersRequest request
    ) {
        return chatService.removeUsersFromChannel(channelId, request);
    }


    @GetMapping("/api/chat/channel/{id}/messages")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Page<ChatMessageResponse> getChannelMessages(
            @PathVariable(name = "id") String channelId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size
    ) {
        return chatService.getChannelMessages(channelId, page, size);
    }
}
