/*** Project's main class that starts the game 
 *   The game was implemented using javafx
 *   @author Somlea Mihai
 *   @since 12.12.2018
 */

import javafx.application.Application;
import javafx.stage.Stage;

/*** Main entry into the program. */
public class Game extends Application{
    public static void main(String[] args){
        Application.launch(args);
    }   

    public void start(Stage primaryStage) throws Exception{
        new Board(primaryStage);
    }
}//end Game class