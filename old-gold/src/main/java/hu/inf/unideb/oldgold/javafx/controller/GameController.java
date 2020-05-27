package hu.inf.unideb.oldgold.javafx.controller;

import hu.inf.unideb.oldgold.results.GameResult;
import hu.inf.unideb.oldgold.results.GameResultDao;
import hu.inf.unideb.oldgold.state.OldGoldState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
public class GameController {
    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultDao gamerDao;

    private int from;
    private int to;
    private int round;
    private OldGoldState oldGoldState;

    private List<Button> buttons;
    private Border transparent;
    private Border red;
    private Image blueCoin;
    private Image goldCoin;

    @FXML
    private Label firstPlayer;

    @FXML
    private Label error;

    @FXML
    private Label secondPlayer;

    @FXML
    private GridPane gamePane;

    @FXML
    private Pane winnerPopUp;

    @FXML
    private Label winner;

    public void initialize() {
        blueCoin = new Image(getClass().getResource("/images/blue.png").toExternalForm());
        goldCoin = new Image(getClass().getResource("/images/gold.png").toExternalForm());
        transparent = new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        red = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        from = -1;
        to = -1;
        round = 0;
        oldGoldState = new OldGoldState();
        buttons = getAllGameButton();
        setButtonText();
        firstPlayer.setBorder(red);
        secondPlayer.setBorder(transparent);
    }

    /**
     * A játék mezőn található gombokat gyüjti össze egy listába.
     * @return pályán lévő Gombok listája
     */
    private List<Button> getAllGameButton() {
        List<Button> buttons = new ArrayList<>();
        gamePane.getChildren().filtered(node -> node instanceof Button)
                .forEach(node -> buttons.add((Button) node));
        return buttons;
    }


    /**
     * A lépés az első kattintással kiválasztjuk hogy melyik érmét akarjuk át rakni,
     * a második kattintással pedig megadjuk hogy hová szeretnénk rakni,
     * amennyiben a választott érme a legbaloldalabbi automatikus eltűnik a pályáról
     * amenyiben az eltűnő érme az aranyszinű a játék véget ér.
     * @param actionEvent
     */
    public void step(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        setNextPlayer();



        if (from == -1) {
            from = buttons.indexOf(btn);
            btn.setStyle("-fx-background-color: white; -fx-border-color: red; -fx-alignment: center;");
        } else {
            buttons.get(from).setStyle("-fx-background-color: white; -fx-border-color: black; -fx-alignment: center;");
            to = buttons.indexOf(btn);
        }

        if (oldGoldState.isFirstCoin(from)) {
            oldGoldState.step(from, to);
            setButtonText();
            error.setText("");
            buttons.get(from).setStyle("-fx-background-color: white; -fx-border-color: black; -fx-alignment: center;");
            from = -1;
            round++;
            setNextPlayer();

            if (oldGoldState.isGoal()) {
                log.info("game finished");
                finish();
            }
        } else {
            if (from != -1 && to != -1) {
                log.info("move from: {} to: {}", from, to);
                try {
                    oldGoldState.step(from, to);
                    error.setText("");
                    setButtonText();
                    round++;
                    setNextPlayer();
                } catch (UnsupportedOperationException e) {
                    log.error("Invalid move");
                    error.setText("Helytelen lépés");
                }
                from = -1;
                to = -1;
            }
        }
    }

    /**
     * Ój játék kezdete a popUp ablak eltüntetése és újra inicializálás történik.
     * @param actionEvent
     */
    public void newGame(ActionEvent actionEvent) {
        gamePane.setVisible(true);
        winnerPopUp.setVisible(false);
        winnerPopUp.setDisable(true);
        initialize();
    }

    /**
     * Vissza lépés a fő menübe.
     * @param actionEvent
     * @throws IOException
     */
    public void goToMenu(ActionEvent actionEvent) throws IOException {
        fxmlLoader.setLocation(getClass().getResource("/fxml/launch.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     *  képek betöltése a játék rácsba a 0-értékek üres cellák,
     *  az 1-es értékek egy kék érmét a 2-es érték pedig az arany érmét jelentik.
     */
    private void setButtonText() {
        int[] state= oldGoldState.getState();
        for (int i = 0; i < oldGoldState.getState().length; i++) {
            if(state[i]==1){
                buttons.get(i).setGraphic(new ImageView(blueCoin));
            }else if(state[i]==2) {
                buttons.get(i).setGraphic(new ImageView(goldCoin));
            }else{
                buttons.get(i).setGraphic(null);
            }
        }
    }

    /**
     * A soron következő játékos bekarikázása.
     */
    private void setNextPlayer() {
        if (round % 2 == 0) {
            log.info("next player {}", firstPlayer.getText());
            firstPlayer.setBorder(red);
            secondPlayer.setBorder(transparent);
        } else {
            log.info("next player {}", secondPlayer.getText());
            secondPlayer.setBorder(red);
            firstPlayer.setBorder(transparent);
        }
    }

    /**
     * A játék befejezésekor törénő események,
     * A győztes pop up page kirajzolása a győztes jatékos nevének kiíratása,
     *  felhasználóhoz tartozó pontok adatbázisban való tárolása.
     */
    private void finish() {
        gamePane.setVisible(false);
        winnerPopUp.setVisible(true);
        winnerPopUp.setDisable(false);
        String winnerPlayer = winningPlayer();
        GameResult gameResult = gamerDao.findByName(winnerPlayer);
        if (gameResult == null) {
            log.info("user: {} not played before", firstPlayer.getText());
            gameResult = new GameResult();
            gameResult.setNameOfPlayer(winnerPlayer);
            gameResult.setScore(1);
            gamerDao.persist(gameResult);
        } else {
            gamerDao.increaseScoreByGameResult(gameResult);
        }

        winner.setText(winnerPlayer);
    }

    /**
     * A győztes játékos nevét határozza meg.
     *
     * @return győztes játékos neve.
     */
    private String winningPlayer() {
        if (round % 2 == 0) {
            return secondPlayer.getText();
        } else {
            return firstPlayer.getText();
        }
    }

}
