package com.chatup.controllers.services.interfaces;

import com.chatup.models.entities.Chat;
import com.chatup.models.entities.ChatMessage;
import com.chatup.models.entities.User;

import java.util.List;

public interface UserChatServices {
    int sendChatMessage(ChatMessage message);

    Chat getChat(int chatID);

    int createChat(Chat chat);
    List<User> getSingleChatUsers(int singleChatId);
}
