package menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Pawel Kierski on 10/06/2019
 * Menu class to handle menu options
 */
public class Menu implements Initializable {

    /**
     * music
     */
    private static MediaPlayer music = new MediaPlayer(new Media(new File("music.mp3").toURI().toString()));;

    /**
     * choice box with difficulty options
     */
    @FXML private ChoiceBox<String> difficultChoiceBox;
    /**
     * chosen difficulty option
     */
    private static int difficultyLvl;

    /**
     * Main initialize metod
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle){

        setDifficultChoiceBox();
        music.setCycleCount(MediaPlayer.INDEFINITE);
        music.setVolume(0.05);
        musicOn();
    }

    /**
     *This changes scene to gameplay scene
     * @param event
     * @throws IOException
     */
    public void playButton(ActionEvent event) throws IOException {

        switch(difficultChoiceBox.getValue()) {

            case "Latwy":
                difficultyLvl = 2;
                break;
            case "Sredni":
                difficultyLvl = 3;
                break;
            case "Trudny":
                difficultyLvl = 4;
                break;
            case "Expert":
                difficultyLvl = 6;
                break;
        }

        SceneSwitcher.switchScene("/fxmls/gameplayFX.fxml");
    }

    /**
     * This turn off application
     */
    public void exitButton(){
        System.exit(0);
    }

    /**
     * This inits difficulty choice box
     */
    private void setDifficultChoiceBox(){

        difficultChoiceBox.getItems().addAll("Latwy", "Sredni", "Trudny", "Expert");
        difficultChoiceBox.setValue("Sredni");
    }

    /**
     * This checks current status of difficulty choice box
     * @return difficulty
     */
    public static int getDifficultyLvl() {
        return difficultyLvl;
    }

    /**
     * This plays music
     */
    public static void musicOn(){
        music.play();
    }

    /**
     * This stops music
     */
    public static void musicOff(){
        music.stop();
    }

}