package gameplay;

import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Pawel Kierski on 15/06/19
 * Bat class uses for init and operate bat
 */
public class Bat extends Rectangle {

    /**
     * Timer uses for redo main functionality
     */
    Timer batTimer;

    /**
     * This is a constructor to initialize bat obj
     * @param width a width of a bat
     * @param height a height of a bat
     */
    Bat(double width, double height) {

        setWidth(width);
        setHeight(height);
        setY(Gameplay.paneYsize - Gameplay.paneYsize/13);
        setX(Gameplay.paneXsize/2 - getWidth()/2);
        setFill(new ImagePattern(new Image("file:bat.png"), 0, 0, 1, 1, true));

        Reflection reflection = new Reflection();
        reflection.setFraction(0.7);

        this.setEffect(reflection);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                move();
            }
        };

        batTimer = new Timer();
        batTimer.schedule(timerTask, 0, 20);

    }
    /**
     * This refresh current bat position according to current mouse cursor position
     */
    private void move() {

        Gameplay.mainPane.setOnMouseMoved(mouseEvent -> {
            if(getX() >= 0 && getX() + getWidth() <= Gameplay.paneXsize)
                setX(mouseEvent.getSceneX() - getWidth() / 2);
            if(getX() < 0)
                setX(0);
            if(getX() + getWidth() > Gameplay.paneXsize)
                setX(Gameplay.paneXsize - getWidth());
            if(mouseEvent.getSceneY() <= Gameplay.paneYsize - Gameplay.paneYsize/13 && mouseEvent.getSceneY() >= Gameplay.paneYsize - Gameplay.paneYsize/4)
                setY(mouseEvent.getSceneY() - getHeight()/2);

        });

    }
}
