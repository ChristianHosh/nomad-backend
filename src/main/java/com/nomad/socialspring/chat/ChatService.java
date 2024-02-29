package com.nomad.socialspring.chat;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.security.JwtUtils;
import com.nomad.socialspring.user.User;
import com.nomad.socialspring.user.UserFacade;
import com.nomad.socialspring.user.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final JwtUtils jwtUtils;
  private final UserFacade userFacade;
  private final ChatChannelFacade chatChannelFacade;
  private final ChatMessageFacade chatMessageFacade;
  private final ChatChannelUserFacade chatChannelUserFacade;

  @Transactional
  public ChatMessageResponse chat(@NotNull ChatMessageRequest request) {
    jwtUtils.validateJwtToken(request.jwtToken());

    User sender = userFacade.findByUsername(jwtUtils.getUsernameFromJwtToken(request.jwtToken()));
    ChatChannel chatChannel = chatChannelFacade.findByUUID(request.chatChannelUUID());

    ChatMessage chatMessage = chatMessageFacade.newChatMessageFrom(request, sender, chatChannel);
    chatChannelUserFacade.setNewMessageOn(chatChannel);

    return ChatMessageMapper.entityToResponse(chatMessage);
  }

  @Transactional
  public ResponseEntity<?> updateMessageReadStatus(@NotNull ChatMessageReadRequest request) {
    User user = userFacade.getCurrentUser();
    ChatChannel chatChannel = chatChannelFacade.findByUUID(request.chatChannelUUID());
    ChatChannelUser chatChannelUser = chatChannelUserFacade.findById(chatChannel, user);

    chatChannelUserFacade.setReadMessage(chatChannelUser);

    return ResponseEntity.ok().build();
  }

  @Transactional
  public ChatChannelResponse createNewChannel(@NotNull ChatChannelRequest request) {
    User user = userFacade.getCurrentUser();
    List<User> userList = userFacade.findByUsernameList(request.usernames());
    userList.add(user);

    ChatChannel chatChannel = chatChannelFacade.newChannel(request.name(), userList);

    return ChatChannelMapper.entityToResponse(chatChannel, user);
  }

  @Transactional
  public ChatChannelResponse addNewUsersToChannel(String channelId, @NotNull ChatChannelUsersRequest request) {
    ChatChannel chatChannel = chatChannelFacade.findByUUID(channelId);
    User authenticatedUser = userFacade.getCurrentUser();

    // if current user is not in {chatChannel} throw unauthorized
    if (!chatChannel.containsUser(authenticatedUser))
      throw BxException.unauthorized(BxException.X_CURRENT_USER_NOT_IN_CHAT);

    List<User> userList = userFacade.findByUsernameList(request.usernames());

    chatChannel = chatChannelFacade.addNewUsers(chatChannel, userList);

    return ChatChannelMapper.entityToResponse(chatChannel);
  }

  @Transactional
  public ChatChannelResponse removeUsersFromChannel(String channelId, @NotNull ChatChannelUsersRequest request) {
    ChatChannel chatChannel = chatChannelFacade.findByUUID(channelId);

    // if current user is not in {chatChannel} throw unauthorized
    if (!chatChannel.containsUser(userFacade.getCurrentUser()))
      throw BxException.unauthorized(BxException.X_CURRENT_USER_NOT_IN_CHAT);

    List<User> userList = userFacade.findByUsernameList(request.usernames());

    chatChannel = chatChannelFacade.removeUsers(chatChannel, userList);

    return ChatChannelMapper.entityToResponse(chatChannel);
  }

  @Transactional
  public Page<ChatMessageResponse> getChannelMessages(String channelId, int page, int size) {
    ChatChannel chatChannel = chatChannelFacade.findByUUID(channelId);

    // if current user is not in {chatChannel} throw unauthorized
    if (!chatChannel.containsUser(userFacade.getCurrentUser()))
      throw BxException.unauthorized(BxException.X_CURRENT_USER_NOT_IN_CHAT);

    Page<ChatMessage> chatMessagePage = chatMessageFacade.getMessagesByChatChannel(chatChannel, page, size);

    return chatMessagePage.map(ChatMessageMapper::entityToResponse);
  }

  @Transactional
  public Page<UserResponse> getChannelUsers(String channelId, int page, int size) {
    ChatChannel chatChannel = chatChannelFacade.findByUUID(channelId);

    // if current user is not in {chatChannel} throw unauthorized
    if (!chatChannel.containsUser(userFacade.getCurrentUser()))
      throw BxException.unauthorized(BxException.X_CURRENT_USER_NOT_IN_CHAT);

    Page<User> userPage = userFacade.getUsersByChatChannel(chatChannel, page, size);

    return userPage.map(User::toResponse);
  }

  public Page<ChatChannelResponse> getAllChannels(int page, int size) {
    User user = userFacade.getCurrentUser();

    Page<ChatChannel> chatChannelPage = chatChannelFacade.getChannelThatContainUser(user, page, size);

    return chatChannelPage.map(c -> ChatChannelMapper.entityToResponse(c, user));
  }
}
