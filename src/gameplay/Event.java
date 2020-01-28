package gameplay;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Pawel Kierski on 10/06/2019
 * Event class implements random event
 */
public class Event extends Circle {

    /**
     * lexicon of possible event types
     */
    private static final int[] possibleEvents = {1, 2, 3, 4, 5, 6};
    /**
     * event type
     */
    private int eventNumber;
    /**
     * Timer used for animate event movement
     */
    Timer fallDownTimer;

    /**
     * This is a Constructor to initialize event object and start animation
     * @param horizontalPos horizontal start position
     * @param verticalPos vertical start position
     */
    Event(double horizontalPos, double verticalPos) {

        setVisible(false);
        this.eventNumber = new Random().nextInt(6) + 1;
        setCenterX(horizontalPos);
        setCenterY(verticalPos);
        setRadius(20);

        switch (this.eventNumber) {
            case 1:
                setFill(new ImagePattern(new Image("file:event1.png"), 0, 0, 1, 1, true));
                break;
            //mniejsza paletka
            case 2:
                setFill(new ImagePattern(new Image("file:event2.png"), 0, 0, 1, 1, true));
                break;

            case 3:
                setFill(new ImagePattern(new Image("file:rabbit.png"), 0, 0, 1, 1, true));
                break;
            //szybsza pilka
            case 4:
                setFill(new ImagePattern(new Image("file:turtle.png"), 0, 0, 1, 1, true));
                break;
            //wolniejsza pilka
            case 5:
                setFill(new ImagePattern(new Image("file:event5.png"), 0, 0, 1, 1, true));
                break;
            case 6:
                //super ball
                setFill(new ImagePattern(new Image("file:event6.png"), 0, 0, 1, 1, true));
                break;
        }

        TimerTask fallDownTimerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        fallDown();
                    }
                });
            }
        };

        fallDownTimer = new Timer();
        fallDownTimer.schedule(fallDownTimerTask, 10, 30);

        Gameplay.eventArrayList.add(this);
        setVisible(false);


    }

    /**
     * This contains instructions for obj to fall down
     */
    public void fallDown() {

        Runnable r = () -> setCenterY(getCenterY() + 2);
        Platform.runLater(r);

        if (getCenterY() > 700) {
            setVisible(false);
            fallDownTimer.cancel();
            Gameplay.eventArrayList.remove(this);

        } else if (this.intersects(Gameplay.bat.getBoundsInParent())) {
            implementEvent();
            setVisible(false);
            fallDownTimer.cancel();
            Gameplay.eventArrayList.remove(this);
        }
    }

    /**
     * This implements event on different objects or on the whole game
     */
    private void implementEvent() {

        switch (this.eventNumber) {
            case 1:
                if (Gameplay.bat.getWidth() < 250) {
                    Gameplay.bat.setWidth(Gameplay.bat.getWidth() * 1.5);
                }
                break;

            case 2:

                Gameplay.bat.setWidth(Gameplay.bat.getWidth() * 0.75);
                break;

            case 3:

                Gameplay.ballsArray.forEach(ball -> ball.setSpeedVector(1.2));
                break;
            //szybsza pilka
            case 4:
                Gameplay.ballsArray.forEach(ball -> ball.setSpeedVector(0.8));
                break;
            //wolniejsza pilka
            case 5:
                Gameplay.addBall = true;
                break;
            case 6:
                Ball.superBall = true;

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                            Ball.superBall = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Thread turnOffEffect = new Thread(r);
                turnOffEffect.start();

                break;
        }

    }
}
