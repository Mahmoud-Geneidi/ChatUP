package com.chatup.controllers.FXMLcontrollers;

import com.chatup.controllers.services.implementations.ChatServicesImpl;
import com.chatup.controllers.services.implementations.CurrentChat;
import com.chatup.controllers.services.implementations.CurrentUserImp;
import com.chatup.controllers.services.implementations.ListCoordinatorImpl;
import com.chatup.models.entities.Card;
import com.chatup.models.entities.ChatMessage;
import com.chatup.models.entities.GroupMessage;
import com.chatup.models.enums.CardType;
import com.chatup.models.enums.ChatType;
import com.chatup.network.ServerConnection;
import com.chatup.network.implementations.ClientImpl;
import com.chatup.utils.SwitchScenes;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatScreenController implements Initializable {

    @FXML
    private MFXButton user_chats_btn;
    @FXML
    private MFXButton friends_btn;
    @FXML
    private MFXButton group_btn;
    @FXML
    private MFXButton settings_btn;
    @FXML
    private MFXButton extract_menu_id;
    @FXML
    private TextField txt_ld_search;
    @FXML
    private Circle user_image_side_bar;
    @FXML
    private ListView cardsListView;


    @FXML
    private Button sendButton;

    @FXML
    private TextField messageText;




    @FXML
    private ScrollPane scrollPane;
   // public static ObservableList<Card> currentList;
    private double lastX = 0.0d;
    private double lastY = 0.0d;
    private double lastWidth = 0.0d;
    private double lastHeight = 0.0d;

    private static void prepareListView(ListView cardsListView, ScrollPane scrollPane) {
        cardsListView.setCellFactory(new Callback<ListView<Card>, ListCell<Card>>() {
            public ListCell<Card> call(ListView<Card> param) {
                final Tooltip tooltip = new Tooltip();
                final ListCell<Card> cell = new ListCell<Card>() {
                    @Override
                    public void updateItem(Card item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            if (item != null) {
                                Image userImage = new Image(new ByteArrayInputStream(item.getCardImg()), 30, 30, false, true);
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/card.fxml"));
                                cardController cardController = new cardController(userImage, item.getCardContent(), item.getCardName());
                                loader.setController(cardController);
                                try {
                                    setGraphic(loader.load());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                tooltip.setText(item.getCardContent());
                                setTooltip(tooltip);
                            }
                        } else {
                            setText(null);
                            setGraphic(null);
                        }

                    }
                };
                cell.setStyle("-fx-background-color: #F4F4F4;");
                return cell;
            }
        });
        cardsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Card>() {
            public void changed(ObservableValue<? extends Card> observable, Card oldValue, Card newValue) {
                System.out.println(oldValue);
                System.out.println(newValue);
                if (observable != null && observable.getValue() != null) {
                    System.out.println(newValue.getCardID());
                    if (newValue.getCardType() == CardType.CHAT) {
                        VBox box = ListCoordinatorImpl.getListCoordinator().getSingleChatVbox(newValue.getCardID());
                        scrollPane.setContent(box);
                        CurrentChat.setCurrentChatSingle(newValue.getCardID());
                    } else if (newValue.getCardType() == CardType.GROUP) {
                        VBox box = ListCoordinatorImpl.getListCoordinator().getGroupChatVbox(newValue.getCardID());
                        scrollPane.setContent(box);
                        CurrentChat.setCurrentChatGroup(newValue.getCardID());
                    }
                }
            }
        });
    }

    @FXML
    void sendMessage(ActionEvent event) {
        if(CurrentChat.getCurrentChat() != null){
            int id = CurrentChat.getCurrentChat().getCurrentChatID();
            if(CurrentChat.getCurrentChat().getCurrentChatType() == ChatType.SINGLE){
                ChatMessage message = new ChatMessage(id,CurrentUserImp.getCurrentUser().getId(),
                        messageText.getText(), LocalDateTime.now(),0);
                ListCoordinatorImpl.getListCoordinator().getSingleChatVbox(id).getChildren().add(ChatServicesImpl.getChatService().sendChatMessage(message));
            }else if(CurrentChat.getCurrentChat().getCurrentChatType() == ChatType.GROUP){
                GroupMessage message = new GroupMessage(CurrentUserImp.getCurrentUser().getId(),messageText.getText(),LocalDateTime.now(),
                        id,0);
                ListCoordinatorImpl.getListCoordinator().getGroupChatVbox(id).getChildren().add(ChatServicesImpl.getChatService().sendGroupMessage(message));
            }
            messageText.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepareListView(cardsListView, scrollPane);
        cardsListView.setItems(ListCoordinatorImpl.getListCoordinator().getUserChats());
    }

    @FXML
    void setChats(ActionEvent event) {
        cardsListView.setItems(ListCoordinatorImpl.getListCoordinator().getUserChats());
    }

    @FXML
    void setFriends(ActionEvent event) {

        cardsListView.setItems(ListCoordinatorImpl.getListCoordinator().getUserOnlineFriends());
    }

    @FXML
    void setGroups(ActionEvent event) {

        //currentList.clear();
        //currentList.addAll(ListCoordinatorImpl.getListCoordinator().getUserGroups());
        cardsListView.setItems(ListCoordinatorImpl.getListCoordinator().getUserGroups());
    }

    @FXML
    void signOut(ActionEvent event) {
        try {
            ServerConnection.getServer().logout(CurrentUserImp.getCurrentUser().getId(), ClientImpl.getClient());
            System.out.println("logout successfully");
            SwitchScenes.getInstance().switchToSignInSecond(event);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("couldn't logout");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("couldn't switch");
        }
    }

    @FXML
    void userNotifications(ActionEvent event) {

    }

    @FXML
    void userSettings(ActionEvent event) {

    }

    @FXML
    void closeDecoratedButtonHandler(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                ServerConnection.getServer().logout(CurrentUserImp.getCurrentUser().getId(), ClientImpl.getClient());
                System.out.println("logout successfully");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }

    }

    @FXML
    void maximizeDecoratedButtonHandler(MouseEvent event) {
        Stage stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
        }
    }

    @FXML
    void minimizeDecoratedButtonHandler(MouseEvent event) {
        Stage stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    void getAllFriendRequests(ActionEvent event) {
        FXMLLoader friendRequestFXML;
        try {
            friendRequestFXML =new FXMLLoader(Objects.requireNonNull(ChatScreenController.class.getResource("/views/FriendRequests.fxml")));
            FriendRequestsController friendRequestsController = new FriendRequestsController();
            friendRequestFXML.setController(friendRequestsController);
            Scene scene =new Scene(friendRequestFXML.load(),550, 550);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            //((Node)(event.getSource())).getScene().getRoot().setDisable(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void sendInvitation(ActionEvent event) {
        FXMLLoader friendRequestFXML;
        try {
            friendRequestFXML =new FXMLLoader(Objects.requireNonNull(ChatScreenController.class.getResource("/views/AddFriend.fxml")));
            AddFriendRequestController addFriendRequestController = new AddFriendRequestController();
            friendRequestFXML.setController(addFriendRequestController);
            Scene scene =new Scene(friendRequestFXML.load(),550, 550);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            //((Node)(event.getSource())).getScene().getRoot().setDisable(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
