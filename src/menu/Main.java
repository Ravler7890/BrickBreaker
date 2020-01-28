package menu;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Pawel Kierski on 10/06/2019.
 * Main class used for initialize stage
 */
public class Main extends Application {

    /**
     * This adjust stage, fxfile and controller
     * @param
     * @throws
     */

    public static Stage copyStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        copyStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxmls/menuFX.fxml"));
        primaryStage.setTitle("Brick Breaker");
        primaryStage.setScene(new Scene(root, 1024, 720));
        primaryStage.show();

        System.out.println(primaryStage.getHeight());
        System.out.println(primaryStage.getMaxWidth());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
