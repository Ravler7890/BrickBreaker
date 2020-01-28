package gameplay;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Random;

/**
 * Created by Pawel Kierski 12/06/2019
 * Brick class uses for init brick on level's scene
 */

public class Brick extends Rectangle {

    /**
     * Final height of brick
     */
    private static final double heightStatic = 30;
    /**
     * final width of brick
     */
    private static final double widthStatic = 60;
    /**
     * It's about current number of brick's lives
     */
    private int currLives;
    /**
     * It's about start number of brick's lives
     */
    private int startLives;
    /**
     * Text obj which contains string with points
     */
    private Text points;

    /**
     * This is a constructor to initialize brick obj
     * @param horizontalPos a horizontal position of brick
     * @param verticalPos a vertical position of brick
     * @param startLives a start lives of brick
     */
    Brick(double horizontalPos, double verticalPos, int startLives){

        setX(horizontalPos);
        setY(verticalPos);
        setHeight(heightStatic);
        setWidth(widthStatic);
        this.startLives = startLives;
        this.currLives = startLives;
        brickInit();
        pointsInit();

    }

    /**
     * Inits points text fields
     */
    public void pointsInit(){

        points = new Text();
        points.setText("" + 100*startLives);
        points.setFill(Color.LIME);
        points.setX(getX()+20);
        points.setY(getY()+20);
        points.setScaleY(3);
        points.setScaleX(3);
        points.setVisible(false);

        Gameplay.mainPane.getChildren().add(this);
    }

    /**
     * This gets current brick lives
     * @return current brick's lives
     */
    public int getLives(){
        return currLives;
    }

    /**
     * This inform brick obj that it was hit by another obj
     * @param howStrong an information how strong brick was hit
     */
    public void hit(int howStrong){

        if(currLives >= 2) {
            currLives -= howStrong;
        }
        else {
            currLives--;
        }
        brickInit();
    }

    /**
     * This inits bricks aspect and also appoints event constructor randomly
     * when brick's current lives is zero
     */
    private void brickInit(){

        switch (currLives){
            case 1:
                setFill(new ImagePattern(new Image("file:1.png"), 0, 0, 1, 1, true));
                break;
            case 2:
                setFill(new ImagePattern(new Image("file:2.png"), 0, 0, 1, 1, true));
                break;
            case 3:
                setFill(new ImagePattern(new Image("file:3.png"), 0, 0, 1, 1, true));
                break;
            case 4:
                setFill(new ImagePattern(new Image("file:4.png"), 0, 0, 1, 1, true));
                break;
            case 0:
                setVisible(false);
                setDisable(true);
                Gameplay.bricksArray.remove(this);
                Gameplay.points += 100*startLives;
                if(new Random().nextInt(10) <= 2){
                    Event event = new Event(getX(), getY());
                }
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        fadeOutPoints();
                    }
                });;

        }
    }

    /**
     * This gets final bricks height
     * @return brick height
     */
    public static double getHeightStatic() {
        return heightStatic;
    }

    /**
     * This gets final brick width
     * @return bricks width
     */
    public static double getWidthStatic() {
        return widthStatic;
    }

    /**
     * This changes actual position of points text fields
     */
    public void refreshPoints(){
        points.setY(getY()+18);
    }

    /**
     * This provides an animation after brick is crushed
     * and releases points text field
     */
    public void fadeOutPoints(){

        points.setVisible(true);
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), points);
        ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(2), points);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), points);
        translateTransition.setToY(-100);
        scaleIn.setToX(5);
        scaleIn.setToY(5);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        translateTransition.play();
        scaleIn.play();
        fadeOut.play();


    }

    /**
     * This adds points text field to scene pane
     */
    public void addPoints(){
        Gameplay.mainPane.getChildren().add(points);
    }
}
