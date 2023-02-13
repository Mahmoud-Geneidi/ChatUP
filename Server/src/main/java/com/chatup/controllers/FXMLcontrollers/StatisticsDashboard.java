package com.chatup.controllers.FXMLcontrollers;

import com.chatup.utils.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class StatisticsDashboard implements Initializable {

    @FXML
    private Button announcementButton;

    @FXML
    private ImageView closeXButton;

    @FXML
    private Button signoutButton;

    @FXML
    private Button statisticsButton;

    @FXML
    void announcementButtonHandler(ActionEvent event) {
        StageManager.getInstance().switchToAnnouncement();
    }

    @FXML
    void closeButtonHandler(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Server is running!!!", ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    void overviewButtonHandler(ActionEvent event) {
        StageManager.getInstance().switchToOverview();
    }

    @FXML
    void signoutButtonHandler(ActionEvent event) {

    }

    @FXML
    void statisticsButtonHandler(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signoutButton.setDisable(true);
    }
}
