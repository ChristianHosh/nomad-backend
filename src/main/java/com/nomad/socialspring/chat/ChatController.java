package com.nomad.socialspring.chat;

import com.nomad.socialspring.user.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;

  @PutMapping("/read")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> updateMessageReadStatus(
          @RequestBody @Valid ChatMessageReadRequest request
  ) {
    return chatService.updateMessageReadStatus(request);
  }

  @PostMapping("/channels")
  @ResponseStatus(HttpStatus.OK)
  public ChatChannelResponse createNewChannel(
          @RequestBody @Valid ChatChannelRequest request
  ) {
    return chatService.createNewChannel(request);
  }

  @GetMapping("/channels")
  @ResponseStatus(HttpStatus.OK)
  public Page<ChatChannelResponse> getAllChannels(
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "50") int size
  ) {
    return chatService.getAllChannels(page, size);
  }

  @GetMapping("/channels/{id}/users")
  @ResponseStatus(HttpStatus.OK)
  public Page<UserResponse> getChannelUsers(
          @PathVariable(name = "id") String channelId,
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "50") int size
  ) {
    return chatService.getChannelUsers(channelId, page, size);
  }

  @PutMapping("/channels/{id}/users")
  @ResponseStatus(HttpStatus.OK)
  public ChatChannelResponse addNewUsersToChannel(
          @PathVariable(name = "id") String channelId,
          @RequestBody @Valid ChatChannelUsersRequest request
  ) {
    return chatService.addNewUsersToChannel(channelId, request);
  }

  @DeleteMapping("/channels/{id}/users")
  @ResponseStatus(HttpStatus.OK)
  public ChatChannelResponse removeUsersFromChannel(
          @PathVariable(name = "id") String channelId,
          @RequestBody @Valid ChatChannelUsersRequest request
  ) {
    return chatService.removeUsersFromChannel(channelId, request);
  }


  @GetMapping("/channels/{id}/messages")
  @ResponseStatus(HttpStatus.OK)
  public Page<ChatMessageResponse> getChannelMessages(
          @PathVariable(name = "id") String channelId,
          @RequestParam(name = "page", defaultValue = "0") int page,
          @RequestParam(name = "size", defaultValue = "50") int size
  ) {
    return chatService.getChannelMessages(channelId, page, size);
  }
}
