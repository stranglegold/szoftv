package hu.inf.unideb.oldgold.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
public class LaunchController {

    @Inject
    private FXMLLoader fxmlLoader;

    @FXML
    private TextField firstPlayerName;

    @FXML
    private TextField secondPlayerName;


    @FXML
    private Label errorLabel;

    public void startAction(ActionEvent actionEvent) throws IOException {
        if (firstPlayerName.getText().isEmpty() ||secondPlayerName.getText().isEmpty()) {
            errorLabel.setText("Meg kell adni a neveket!");
        } else {
            if(firstPlayerName.getText().equals(secondPlayerName.getText())){
                errorLabel.setText("A nevek nem egyezhetnek");
                return;
            }
            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().getFirstPlayer().setText(firstPlayerName.getText());
            fxmlLoader.<GameController>getController().getSecondPlayer().setText(secondPlayerName.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void initialize(){
    }

    public void goToRankList(ActionEvent actionEvent) throws IOException {
        fxmlLoader.setLocation(getClass().getResource("/fxml/ranklist.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
