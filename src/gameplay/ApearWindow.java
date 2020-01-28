package gameplay;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import menu.Main;
import menu.Menu;
import menu.SceneSwitcher;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Pawel Kierski on 18/06/2019.
 * ApearWindow class uses for open windows in some cases
 */
public class ApearWindow implements Initializable {

    /**
     * label which contains information weather game is won or lose
     */
    @FXML
    Label label;
    /**
     * label which contains points amount
     */
    @FXML
    Label label2;
    /**
     * button used to close window and restart the game
     */
    @FXML
    Button closeButton;

    /**
     * flag says if game is won or lose
     */
    static public boolean won;
    static public boolean appear;

    /**
     * main initialize metod
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {


        if(won){
            label.setText("Gratulacje! Wygrałeś!");
            label2.setText("Twoje punkty: " + Gameplay.savedPoints);
        }
        else{
            label.setText("Przegrałeś! Nowa gra?");
            label2.setText("Twoje punkty: " + Gameplay.savedPoints);
        }
    }

    /**
     * This closes appeared window
     */
    public void closeButtonAction(ActionEvent event) throws IOException {

        Stage stage = (Stage)closeButton.getScene().getWindow();
        stage.close();

        Parent parent = FXMLLoader.load(Menu.class.getResource("/fxmls/menuFX.fxml"));
        Scene parentScene = new Scene(parent);
        Stage window = Main.copyStage;
        window.setScene(parentScene);
        window.show();

    }
}
