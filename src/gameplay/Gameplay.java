package gameplay;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import menu.Menu;
import menu.SceneSwitcher;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Pawel Kierski 10/06/2019
 * Gameplay class is a controller for gameplayFX
 * Gameplay class is also a storage for all game parameters
 * Gameplay class initialize game and generate level
 */
public class Gameplay implements Initializable {

    /**
     * Win scene
     */
    static Rectangle winGif;
    /**
     * main pane which contains all movable objects
     */
    static Pane mainPane;

    /**
     * Final mainPane's horizontal size
     */
    final static double paneXsize = 1024;
    /**
     * Final mainPane's vertical size
     */
    final static double paneYsize = 720;
    /**
     * actual amount of points collected in game
     */
    static int points;
    /**
     * saved amount of points collected in game (lasts after "cleanup")
     */
    static int savedPoints;
    /**
     * label to visualize points amount on scene
     */
    private static Label pointsLabel;
    /**
     * flag to check if new ball is needed
     * start value = true
     */
    static boolean addBall = true;
    /**
     * flag says if game was won
     */
    private static boolean gameWon = false;
    /**
     * flag says if game was lost
     */
    public static boolean gameOver = false;
    /**
     * a reference stores bat obj
     */
    static Bat bat;
    /**
     * list of all balls existing on scene
     */
    static ArrayList<Ball> ballsArray = new ArrayList<>();
    /**
     * list of all brick existing on game's scene
     */
    static  ArrayList<Brick> bricksArray = new ArrayList<>();
    /**
     * list of all events existing on game's scene
     */
    static ArrayList<Event> eventArrayList = new ArrayList<>();
    /**
     * timer to refresh game status and check flags
     */
    static Timer lvlTimer;

    /**
     * helps flag
     */
    static boolean appear;

    /**
     * starts anchor pane with non-dynamic obj
     */
        @FXML
    AnchorPane anchor;

    /**
     * This initialize scene
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        pointsLabel = new Label("Points: " + points);
        pointsLabel.setScaleX(2);
        pointsLabel.setScaleY(2);
        pointsLabel.setLayoutX(900);
        pointsLabel.setLayoutY(40);
        mainPane = new AnchorPane();
        mainPane.setLayoutY(20);
        mainPane.setPrefHeight(720);
        mainPane.setPrefWidth(1024);
        anchor.getChildren().add(mainPane);
        Image img = new Image("file:background.png");
        mainPane.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        gameInit();

    }
    /**
     * This resets whole game
     */
    public void gameReset(){

        cleanUp();
        gameInit();
    }

    /**
     * This inits game
     */
    public static void gameInit(){

        appear = true;
        addBall = true;
        gameWon = false;
        mainPane.getChildren().remove(winGif);
        batInit();
        addBall();
        lvlInit();
        mainPane.getChildren().add(pointsLabel);
    }

    /**
     * This inits bat used in game
     */
    private static void batInit(){

        bat = new Bat(150,25);
        mainPane.getChildren().add(bat);
    }

    /**
     * This adds ball to game
     */
    public static void addBall(){

        if(addBall) {
            ballsArray.add(new Ball(10));
            mainPane.getChildren().add(ballsArray.get(ballsArray.size()-1));
            mainPane.getChildren().add(ballsArray.get(ballsArray.size()-1).getHitBox());
            addBall = false;
        }


    }

    /**
     * This create level and control current situation
     */
    private static void lvlInit(){

        int layoutX = 16;

        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                Runnable r = ()-> {
                    bricksArray.forEach(brick -> {brick.setY(brick.getY() + 0.1); brick.refreshPoints();
                    if(brick.getY() >= bat.getY())
                    gameOver = true;});
                    for (Event e: eventArrayList
                    ) {
                        if(!e.isVisible()) {
                            mainPane.getChildren().add(e);
                            e.setVisible(true);
                        }
                    }
                    pointsLabel.setText("Points: " + points);
                    addBall();
                    try {
                        gameOver();
                        checkWin();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                };
                Platform.runLater(r);
            }
        };


        for(int i = 0; i < Menu.getDifficultyLvl(); i++){
            for(int j = 0; j < layoutX; j++){
                bricksArray.add(new Brick(paneXsize - Brick.getWidthStatic() * (double) layoutX - 35 + (Brick.getWidthStatic() + 0.5) * (double)j,
                        paneYsize/6 + (Brick.getHeightStatic() + 0.5) * i, i%4 + 1));
            }
        }

        bricksArray.forEach(brick -> brick.addPoints());

        lvlTimer = new Timer();
        lvlTimer.schedule(timerTask,0,50);

    }

    /**
     * This checks and perform game over procedure
     * @throws IOException
     */
    public static void gameOver() throws IOException{

        if(gameOver) {
            savedPoints = points;
            cleanUp();
            ApearWindow.won = false;
            Parent parent = FXMLLoader.load(Menu.class.getResource("/fxmls/apearWindowFX.fxml"));
            Scene parentScene = new Scene(parent);
            Stage window = new Stage();
            window.setScene(parentScene);
            window.show();
            gameOver = false;
            gameWon = true;
        }
    }

    /**
     * This cleanUp when it is needed
     */
    public static void cleanUp(){

        mainPane.getChildren().clear();
        points = 0;
        eventArrayList.forEach(event -> event.fallDownTimer.cancel());
        ballsArray.forEach(ball -> ball.movementTimer.cancel());
        ballsArray.removeAll(ballsArray);
        eventArrayList.removeAll(eventArrayList);
        bricksArray.removeAll(bricksArray);
        lvlTimer.cancel();
        bat.batTimer.cancel();

    }

    /**
     * This change scene to menu's scene
     * @param event event handler
     * @throws IOException
     */
    public void backToMenu(ActionEvent event) throws IOException {

        appear = false;
        lvlTimer.cancel();
        cleanUp();
        SceneSwitcher.switchScene("/fxmls/menuFX.fxml");
    }

    /**
     * This checks and perform win procedure
     * @throws IOException
     */
    private static void checkWin() throws IOException{

        if(bricksArray.isEmpty() && !gameWon){
            gameWon = true;
            savedPoints = points;
            cleanUp();
            ApearWindow.won = true;

            winGif = new Rectangle();
            winGif.setHeight(paneYsize);
            winGif.setWidth(paneXsize);
            winGif.setY(mainPane.getLayoutX());
            winGif.setFill(new ImagePattern(new Image("file:giphy.gif"), 0, 0, 1, 1, true));
            mainPane.getChildren().add(winGif);

            if(appear) {
                Parent parent = FXMLLoader.load(Menu.class.getResource("/fxmls/apearWindowFX.fxml"));
                Scene parentScene = new Scene(parent);
                Stage window = new Stage();
                window.setScene(parentScene);
                window.show();
            }

        }
    }
}


