package com.chatup.controllers.services.implementations;

import com.chatup.controllers.reposotories.implementations.ChatRepoImpl;
import com.chatup.controllers.reposotories.implementations.UserRepoImpl;
import com.chatup.controllers.services.interfaces.UserGroupsService;
import com.chatup.controllers.services.interfaces.UserServices;
import com.chatup.models.entities.Chat;
import com.chatup.models.entities.ChatMessage;
import com.chatup.models.entities.User;

import java.util.*;
import java.util.stream.Collectors;

public class UserServicesImpl implements UserServices {

    private static UserServices userServices;

    private UserServicesImpl(){}

    public static UserServices getUserServices(){
        if(userServices == null)
            userServices = new UserServicesImpl();
        return userServices;
    }

    @Override
    public User getUserInfo(int userId) {
        User user= UserRepoImpl.getUserRepo().getUser(userId);
        user.setPassword(null);
        return user;
    }

    @Override
    public User getUserInfo(String phone) {
        User user= UserRepoImpl.getUserRepo().getUser(phone);
        user.setPassword(null);
        return user;
    }

    @Override
    public Map<Chat,ChatMessage> getUserchats(int userId) {
        Map <Chat,ChatMessage> userMessages = new HashMap<>();
        ChatMessage chatMessage;
        for ( Chat chat:  ChatRepoImpl.getInstance().getAllUserChats(userId) ) {
            System.out.println( chat.getId());
            chatMessage= ChatRepoImpl.getInstance().getLastMessage(chat.getId());
            userMessages.put(chat, chatMessage);
            System.out.println( chatMessage.getContent());
        }
        Map<Chat,ChatMessage> resultSet = userMessages.entrySet()
                .stream()
                .sorted(Comparator.comparing(e -> e.getValue().getMessageDateTime()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> left,
                        LinkedHashMap::new));

         return resultSet;
    }

    @Override
    public List<ChatMessage> getChatMsg(int chatId) {
        return ChatRepoImpl.getInstance().getSingleChatMessages(chatId);
    }


}
