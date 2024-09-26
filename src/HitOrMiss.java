import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;

/**
 * Program Name: HitOrMiss;
 * Student Name: Piyusha Satija;
 * Student ID: 200001855;
 * Date: Mar 22, 2024;
 * Course: CPSC 1181-003;
 * Compiler: IntelliJ IDEA 2023.2.1 OpenJDK
 */

public class HitOrMiss extends Application
{
    private BorderPane root;
    private Pane background;
    private Circle ball, temp;
    private double centreX, xVelocity;
    private int hits = 0, misses = 0, clicks = 0;
    private Text numOfHits, numOfMisses, end;
    private Button pause, reset;
    private BallAnimation animation;
    private PauseTransition pauseAnimation;

    /**
     * The main method launches args for the Application class.
     * @param args
     */

    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * This is the overridden start method of Application.
     * @param primaryStage the primary stage for this application, onto which the application scene can be set.
     * Applications may create other stages, if needed, but they will not be primary stages.
     * In this program, I have created a ball game where if the user clicks on the ball, they win a hit and the speed of the ball
     * increases with each hit.
     * If they miss a hit, the miss count increases. If they have 5 misses, the game ends and the user must click the reset button
     * to play the game again.
     */

    @Override
    public void start(Stage primaryStage)
    {
        root = new BorderPane();
        background = new Pane();
        xVelocity = 2;

        background.setStyle("-fx-background-color: black");
        root.setCenter(background);

        ball = new Circle(-60, 250, 40);
        ball.setFill(Color.WHITE);

        ball.setOnMousePressed(new MousePressHandler());

        animation = new BallAnimation();
        animation.start();

        numOfHits = new Text(10, 30, "Hits: " + hits);
        numOfHits.setFill(Color.WHITE);
        numOfHits.setFont(new Font("Arial", 15));

        numOfMisses = new Text(70, 30, "Misses: " + misses);
        numOfMisses.setFill(Color.WHITE);
        numOfMisses.setFont(new Font("Arial", 15));

        ButtonClickHandler handler = new ButtonClickHandler();
        pause = new Button("Pause");
        pause.setOnAction(handler);
        reset = new Button ("Reset");
        reset.setOnAction(handler);

        HBox buttons = new HBox(15, pause, reset);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);
        buttons.setPadding(new Insets(7));

        root.setBottom(buttons);
        root.getChildren().addAll(ball, numOfHits, numOfMisses);

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hit Or Miss");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * This is the animation class that contains the code for the ball animation.
     * It moves the ball from one end to another and also will stop the animation once the user misses five balls.
     */

    private class BallAnimation extends AnimationTimer
    {
        @Override
        public void handle(long now)
        {
            centreX = ball.getCenterX();

            if (centreX > 600)
            {
                misses++;
                numOfMisses.setText("Misses: " + misses);
                ball.setCenterX(-60);
                ball.setCenterY(new Random().nextInt(100, 350));
                centreX = -60;
            }

            centreX = centreX + xVelocity;
            ball.setCenterX(centreX);

            if (misses == 5)
            {
                animation.stop();
                end = new Text(150, 250, "Game Over!");
                end.setFont(new Font("Arial", 30));
                end.setFill(Color.WHITE);
                root.getChildren().add(end);
            }
        }
    }

    /**
     * This is the mouse press event handler that if the mouse is pressed on the ball, gets called, and increases the speed of the
     * ball and increments the hits counter.
     * If the press is done when the game is paused, nothing happens.
     */

    private class MousePressHandler implements EventHandler<MouseEvent>
    {
        @Override
        public void handle(MouseEvent e)
        {
            if (e.getSource() == ball)
            {
                if (clicks % 2 == 1)
                {
                    e.consume();
                }
                else
                {
                    hits++;
                    numOfHits.setText("Hits: " + hits);
                    celebration(e.getX(), e.getY());

                    if (hits < 5)
                    {
                        xVelocity = xVelocity + 1;
                    }
                    else
                    {
                        xVelocity = xVelocity + 0.5;
                    }

                    ball.setCenterY(new Random().nextInt(100, 350));
                    ball.setCenterX(-60);

                }
            }
        }
    }

    /**
     * This is the button click event handler that gets called if the <code>pause</code> and/or <code>reset</code> buttons are clicked.
     * The <code>pause</code> button pauses the game. User can only reset the game when it's paused but cannot click the ball.
     * The <code>reset</code> button resets the game completely.
     */

    private class ButtonClickHandler implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent e)
        {
            if (e.getSource() == pause)
            {
                if (clicks % 2 == 0)
                {
                    animation.stop();
                }
                else
                {
                    animation.start();
                }
                clicks++;
            }

            if (e.getSource() == reset)
            {
                clicks = 0;
                animation.start();
                ball.setCenterX(-60);
                xVelocity = 2;
                hits = 0;
                numOfHits.setText("Hits: " + hits);
                misses = 0;
                numOfMisses.setText("Misses: " + misses);
                root.getChildren().remove(end);
            }
        }
    }

    /**
     * This is for the bonus part of the assignment. If the user is successful in clicking the ball, this method gets called.
     * A randomly colored ball flashes at the position of the click.
     * @param x is the x-coordinate of the ball.
     * @param y is the y-coordinate of the ball.
     */

    public void celebration(double x, double y)
    {
        Random random = new Random();
        temp = new Circle(x, y, 40);
        temp.setFill(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));

        root.getChildren().add(temp);

        //This code is inspired by some help from Oracle's website on Animation Class.

        pauseAnimation = new PauseTransition(Duration.seconds(0.1));
        pauseAnimation.setOnFinished(e ->
        {
            root.getChildren().remove(temp);
            ball.setFill(Color.WHITE);
        });

        pauseAnimation.play();
    }
}
