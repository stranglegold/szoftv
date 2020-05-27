package hu.inf.unideb.oldgold.javafx.controller;

import hu.inf.unideb.oldgold.results.GameResult;
import hu.inf.unideb.oldgold.results.GameResultDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.IOException;

public class RankListController {
    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultDao gamerDao;

    @FXML
    private TableColumn user;

    @FXML
     private TableColumn score;

    @FXML
    private TableView<GameResult> ranklist;

    public void initialize(){
        ObservableList<GameResult> obslist = FXCollections.observableList(gamerDao.findAllOrderByScoreAsc());
        user.setCellValueFactory(new PropertyValueFactory<>("nameOfPlayer"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        ranklist.setItems(obslist);
    }

    public void back(ActionEvent actionEvent) throws IOException {
        fxmlLoader.setLocation(getClass().getResource("/fxml/launch.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
