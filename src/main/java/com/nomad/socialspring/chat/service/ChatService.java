package com.nomad.socialspring.chat.service;

import com.nomad.socialspring.chat.dto.*;
import com.nomad.socialspring.chat.mapper.ChatChannelMapper;
import com.nomad.socialspring.chat.mapper.ChatMessageMapper;
import com.nomad.socialspring.chat.model.ChatChannel;
import com.nomad.socialspring.chat.model.ChatChannelUser;
import com.nomad.socialspring.chat.model.ChatMessage;
import com.nomad.socialspring.chat.repo.ChatChannelFacade;
import com.nomad.socialspring.chat.repo.ChatChannelUserFacade;
import com.nomad.socialspring.chat.repo.ChatMessageFacade;
import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.security.facade.AuthenticationFacade;
import com.nomad.socialspring.security.jwt.JwtUtils;
import com.nomad.socialspring.user.dto.UserResponse;
import com.nomad.socialspring.user.mapper.UserMapper;
import com.nomad.socialspring.user.model.User;
import com.nomad.socialspring.user.repo.UserFacade;
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
    private final AuthenticationFacade authenticationFacade;

    public ChatMessageResponse chat(@NotNull ChatMessageRequest request) {
        if (!jwtUtils.validateJwtToken(request.jwtToken()))
            throw BxException.unauthorized("invalid token");

        User sender = userFacade.findByUsername(jwtUtils.getUsernameFromJwtToken(request.jwtToken()));
        ChatChannel chatChannel = chatChannelFacade.findById(request.chatChannelId());

        ChatMessage chatMessage = chatMessageFacade.newChatMessageFrom(request, sender, chatChannel);
        chatChannelUserFacade.setNewMessageOn(chatChannel);

        return ChatMessageMapper.entityToResponse(chatMessage);
    }

    public ResponseEntity<?> updateMessageReadStatus(@NotNull ChatMessageReadRequest request) {
        User user = authenticationFacade.getAuthenticatedUser();
        ChatChannel chatChannel = chatChannelFacade.findById(request.chatChannelId());
        ChatChannelUser chatChannelUser = chatChannelUserFacade.findById(chatChannel, user);

        chatChannelUserFacade.setReadMessage(chatChannelUser);

        return ResponseEntity.ok().build();
    }

    public ChatChannelResponse createNewChannel(@NotNull ChatChannelRequest request) {
        User user = authenticationFacade.getAuthenticatedUser();
        List<User> userList = userFacade.findByUsernameList(request.usernames());
        userList.add(user);

        ChatChannel chatChannel = chatChannelFacade.newChannel(request.name(), userList);

        return ChatChannelMapper.entityToResponse(chatChannel, user);
    }

    public ChatChannelResponse addNewUsersToChannel(String channelId, @NotNull ChatChannelUsersRequest request) {
        List<User> userList = userFacade.findByUsernameList(request.usernames());
        userList.add(authenticationFacade.getAuthenticatedUser());

        ChatChannel chatChannel = chatChannelFacade.addNewUsers(channelId, userList);

        return ChatChannelMapper.entityToResponse(chatChannel);
    }


    public ChatChannelResponse removeUsersFromChannel(String channelId, @NotNull ChatChannelUsersRequest request) {
        List<User> userList = userFacade.findByUsernameList(request.usernames());

        ChatChannel chatChannel = chatChannelFacade.removeUsers(channelId, userList);

        return ChatChannelMapper.entityToResponse(chatChannel);
    }

    public Page<ChatMessageResponse> getChannelMessages(String channelId, int page, int size) {
        ChatChannel chatChannel = chatChannelFacade.findById(channelId);
        Page<ChatMessage> chatMessagePage = chatMessageFacade.getMessagesByChatChannel(chatChannel, page, size);

        return chatMessagePage.map(ChatMessageMapper::entityToResponse);
    }

    public Page<UserResponse> getChannelUsers(String channelId, int page, int size) {
        ChatChannel chatChannel = chatChannelFacade.findById(channelId);
        Page<User> userPage = userFacade.getUsersByChatChannel(chatChannel, page, size);

        return userPage.map(UserMapper::entityToResponse);
    }

}
