package hu.inf.unideb.oldgold.javafx;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.AbstractModule;
import hu.inf.unideb.oldgold.results.GameResultDao;
import hu.inf.unideb.util.guice.PersistenceModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.List;

@Slf4j
public class OldGoldApplication extends Application {

    private GuiceContext context = new GuiceContext(this, () -> List.of(
            new AbstractModule() {
                @Override
                protected void configure() {
                     install(new PersistenceModule("old-gold"));
                     bind(GameResultDao.class);
                 }
            }
    ));

    @Inject
    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("Starting application...");
        context.init();
        fxmlLoader.setLocation(getClass().getResource("/fxml/launch.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Old gold game");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
