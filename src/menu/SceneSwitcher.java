package menu;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Created by Pawel Kierski on 10/06/2019
 */
public abstract class SceneSwitcher {

    public static void switchScene(String directory) throws IOException {

        Parent parent = FXMLLoader.load(Menu.class.getResource(directory));
        Scene parentScene = new Scene(parent);
        Stage window = Main.copyStage;
        window.setScene(parentScene);
        window.show();

    }
}