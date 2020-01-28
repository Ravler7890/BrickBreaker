package gameplay;

import javafx.application.Platform;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Pawel Kierski on 15/06.2019
 * Ball class uses for generate fully functional animated ball
 */

public class Ball extends Circle {

    /**
     * says if ball is on move
     */
    private boolean stable;
    /**
     * is am angle of ball bounce
     */
    private double alpha;
    /**
     * resultant movement vector
     */
    private static double speedVector = 3;
    /**
     * horizontal ball speed
     */
    private double horizontalSpeed;
    /**
     * vertical ball speed
     */
    private double verticalSpeed;
    /**
     * ball's hit box
     */
    private Rectangle hitBox;
    /**
     * says if ball is super ball (event)
     */
    static boolean superBall = false;
    /**
     * power of ball's hit
     */
    private int power;
    /**
     * Timer used for operating movement function
     */
    Timer movementTimer;

    /**
     * This is a constructor for initialize ball obj
     *
     * @param ballRadius initial ball radius
     */

    boolean hitLeft = false;
    boolean hitRight = false;

    Ball(double ballRadius) {

        setRadius(ballRadius);

        setFill(new ImagePattern(new Image("file:ball.png"), 0, 0, 1, 1, true));
        alpha = 0;
        stable = true;

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                move();
            }
        };

        movementTimer = new Timer();
        movementTimer.schedule(timerTask, 0, 5);

        hitBox = new Rectangle();
        hitBox.setWidth(2 * getRadius());
        hitBox.setHeight(2 * getRadius());
        hitBox.setVisible(false);

    }

    /**
     * This is a main animation and functional ball's metod
     */


    private void move() {

        int alphaFlag;
        double alphaFactor;


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                isSuper();
            }
        });

        alphaFactor = 45 / Gameplay.bat.getWidth();
        Gameplay.mainPane.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown()) {
                stable = false;
            }
        });

        if (stable) {
            Runnable r = () -> {
                setCenterX(Gameplay.bat.getX() + Gameplay.bat.getWidth() / 2);
                setCenterY(Gameplay.bat.getY() - getRadius());
            };
            Platform.runLater(r);

        } else {

            if (getCenterY() > Gameplay.paneYsize) {
                if (Gameplay.ballsArray.size() == 1) {
                    Gameplay.gameOver = true;
                }
                Gameplay.ballsArray.remove(this);
                movementTimer.cancel();
                setVisible(false);

            } else if (getBoundsInParent().intersects(Gameplay.bat.getBoundsInParent())) {
                hitRight = false;
                hitLeft = false;
                double offset = getCenterX() - Gameplay.bat.getX() - Gameplay.bat.getWidth() / 2;
                if (horizontalSpeed < 0) {
                    offset *= -1;
                }

                alpha = alpha + (offset * alphaFactor);

                if (alpha < 0) {
                    alpha *= -1;
                    alphaFlag = -1;
                } else
                    alphaFlag = 1;

                if (alpha > 70)
                    alpha = 70;


                if (horizontalSpeed > 0)
                    horizontalSpeed = speedVector * Math.sin(Math.toRadians(alpha)) * alphaFlag;
                else
                    horizontalSpeed = -speedVector * Math.sin(Math.toRadians(alpha)) * alphaFlag;
                verticalSpeed = -speedVector * Math.cos(Math.toRadians(alpha));
            }
            if (getCenterX() - getRadius() <= Gameplay.mainPane.getLayoutX() || getCenterX() + getRadius() >= Gameplay.paneXsize + Gameplay.mainPane.getLayoutX()) {
                if(horizontalSpeed < 0 && !hitLeft) {
                horizontalSpeed *= -1;
                hitLeft = true;
                hitRight = false;
                }
                else if(horizontalSpeed > 0 && !hitRight){
                    horizontalSpeed *= -1;
                    hitRight = true;
                    hitLeft = false;
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setCenterX(getCenterX() + horizontalSpeed * 2);
                    }
                });
            }
            if (getCenterY() <= Gameplay.mainPane.getLayoutY()) {
                hitRight = false;
                hitLeft = false;
                verticalSpeed *= -1;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setCenterY(getCenterY() + verticalSpeed * 2);
                    }
                });
            }

            checkCollision();

            Runnable r2 = () -> {
                setCenterX(getCenterX() + horizontalSpeed);
                setCenterY(getCenterY() + verticalSpeed);
            };
            Platform.runLater(r2);
        }

    }

    /**
     * This checks collision between ball's hitbox and bricks
     */
    private void checkCollision() {

        hitBox.setX(getCenterX() - getRadius());
        hitBox.setY(getCenterY() - getRadius());

        for (int i = 0; i < Gameplay.bricksArray.size(); i++) {
            if (hitBox.getBoundsInLocal().intersects(Gameplay.bricksArray.get(i).getBoundsInParent()) && Gameplay.bricksArray.get(i).getLives() != 0) {

                hitRight = false;
                hitLeft = false;

                if (getCenterY() > Gameplay.bricksArray.get(i).getY() && getCenterY() < Gameplay.bricksArray.get(i).getY() + Gameplay.bricksArray.get(i).getHeight())
                    horizontalSpeed *= -1;
                else
                    verticalSpeed *= -1;

                Gameplay.bricksArray.get(i).hit(power);
                setCenterX(getCenterX() + horizontalSpeed * 4);
                setCenterY(getCenterY() + verticalSpeed * 4);
                break;
            }
        }
    }

    /**
     * This change resultant speed vector by a factor
     * @param speedFactor factor of resultant speed vector change
     */
    public void setSpeedVector(double speedFactor) {
        this.speedVector *= speedFactor;
        horizontalSpeed *= speedFactor;
        verticalSpeed *= speedFactor;
    }

    /**
     * get ball's hitbox
     * @return ball's hitbox
     */
    public Rectangle getHitBox() {
        return hitBox;
    }

    /**
     * This checks if ball is super (event)
     */
    private void isSuper() {

        if (superBall) {
            setFill(new ImagePattern(new Image("file:superball.png"), 0, 0, 1, 1, true));
            power = 2;
        } else {
            power = 1;
            setFill(new ImagePattern(new Image("file:ball.png"), 0, 0, 1, 1, true));
        }

    }
}


