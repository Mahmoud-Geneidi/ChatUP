package com.chatup.network.interfaces;

import com.chatup.models.entities.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface Server extends Remote {

    public int signup(User user) throws RemoteException;
    public User login(String phone, String password, Client client) throws RemoteException;
    public void logout(int id, Client client) throws RemoteException;
    public Map<Chat,ChatMessage> getUserChats(int userID) throws RemoteException;
    public Map<GroupChat,GroupMessage> getUserGroups(int userID) throws RemoteException;
    public List<ChatMessage> getChatMessages(int chatID) throws RemoteException;
    public List<GroupMessage> getGroupMessages(int groupID) throws RemoteException;
    public List<User> getUserFriends(int userID) throws RemoteException;
    public List<User> getUserFriendRequests(int userID) throws RemoteException;
    public User getUser(int userID) throws RemoteException;
    public User getUser(String phoneNumber) throws RemoteException;
    public Boolean sendFriendRequest( List<FriendRequest> addRequests )throws RemoteException;
    public  Boolean updateFriendsRequestStatus (FriendRequest friendRequests)throws RemoteException;

    public int sendChatMessage(ChatMessage message) throws RemoteException;

    public int sendGroupChatMessage(GroupMessage message) throws RemoteException;
    public int createGroupChat(GroupChat groupChat , List<User> userList) throws RemoteException;

    public void addUsersToGroup(int groupChatId,List<User> userList) throws RemoteException;

    public int createChat(Chat chat) throws RemoteException;
    public Chat getChat(int chatID) throws RemoteException;
    public List<User> getGroupMembers(int groupId) throws RemoteException;
}
