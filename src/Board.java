import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.animation.KeyValue;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class Board{
    
    private static final String TITLE = "Tic-Tac-Toe";
    private Stage primaryStage, informationStage;
    private EventHandler<WindowEvent> confirmCloseEventHandler;
    private boolean playable = false;
    private boolean turnX = true;
    private Text message;
    private Player first, second;
    private List<TileCombo> combos = new ArrayList<>();
    private Tile[][] board = new Tile[3][3];
    private BorderPane root;
    private Line line;

    /*** Constructor for the Board class
     * @param primaryStage : The stage of the game app
     */
    public Board(Stage primaryStage){
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setTitle(TITLE);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        //the stage that contains the greetings message and information regarding the game
        informationStage = new Stage();
        informationStage.setTitle("Welcome");
        informationStage.setScene(new Scene(infoContent()));
        informationStage.setResizable(false);
        informationStage.initModality(Modality.APPLICATION_MODAL);
        informationStage.show();
    
        //making the close event handler for the pop up 
        confirmCloseEventHandler = new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert closeConfirmation = new Alert(AlertType.CONFIRMATION);
                closeConfirmation.setHeaderText("Confirm Exit");
                closeConfirmation.setContentText("Are you sure you want to exit the game?");
                closeConfirmation.initModality(Modality.APPLICATION_MODAL);
                closeConfirmation.initOwner(primaryStage);

                Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                        ButtonType.OK);
                exitButton.setText("Exit");
        
                Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
                if (!ButtonType.OK.equals(closeResponse.get())) {
                    event.consume();
                }
            }//end handle method implementation
        };

        primaryStage.setOnCloseRequest(confirmCloseEventHandler);
        this.primaryStage = primaryStage;
    }

    /*** Method that creates the content of the scene
     * @return The pane where all the content will be placed
     */
    private Parent createContent(){
        Pane pane = new Pane();
        HBox topPane = new HBox();
        root = new BorderPane();

        message  = new Text();
        message.setText("Hello!");
        message.setFont(Font.font(25));
        message.setStroke(Color.BLACK);
        topPane.setPrefSize(600, 20);
        topPane.getChildren().add(message);
        topPane.setAlignment(Pos.BOTTOM_LEFT);

        for(int x = 0; x < 3; ++x){
            for(int y = 0; y < 3; ++y){
                Tile tile  = new Tile();
                tile.setTranslateX(x * 202);
                tile.setTranslateY(y * 202 + 20);
                pane.getChildren().add(tile);

                board[x][y] = tile;
            }
        }

        //adding the vertical combos
        for(int x = 0; x < 3; x++){
            combos.add(new TileCombo(board[x][0], board[x][1], board[x][2]));
        }

        //adding the horizontal combos
        for(int y = 0; y < 3; y++){
            combos.add(new TileCombo(board[0][y], board[1][y], board[2][y]));
        }

        //adding the diagnol combos
        combos.add(new TileCombo(board[0][0], board[1][1], board[2][2]));
        combos.add(new TileCombo(board[2][0], board[1][1], board[0][2]));

        root.setPrefSize(600, 620);
        root.setTop(topPane);
        root.setCenter(pane);

        return root;
    }//end createContent method

    /*** Method that generates the content for the information stage at the beggining of the game */
    private Parent infoContent(){
        BorderPane base = new BorderPane();
        base.setPrefSize(400, 400);
        
        Text infoText = new Text();
        infoText.setText("Hi! This is a simple Tic-Tac-Toe game that can be played with a friend "
            + "or alone against an Ai. For instruction you should press the Instruction button. If you want to start "
            + "playing the game then you should press the Start button. If you want to exit simply press the Exit button.");
        infoText.setFont(Font.font(18));
        infoText.setWrappingWidth(400);

        Text title = new Text();
        title.setText("Tic-Tac-Toe!");
        title.setFont(Font.font(32));

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #4b4c4f;");
        hBox.setAlignment(Pos.CENTER);

        Button instructButton = new Button();
        instructButton.setText("Instructions");
        instructButton.setPrefSize(100, 50);
        instructButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				//TODO: add the implementation for the insctuction button event
			}
        });

        Button startButton = new Button();
        startButton.setText("Start");
        startButton.setPrefSize(100, 50);
        startButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                informationStage.close();
                startPrompt();
            }
        });

        Button closeButton  = new Button();
        closeButton.setText("Exit");
        closeButton.setPrefSize(100, 50);
        closeButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
                informationStage.fireEvent(new WindowEvent(informationStage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        });

        hBox.getChildren().addAll(instructButton, startButton, closeButton);

        base.setTop(title);
        base.setCenter(infoText);
        base.setBottom(hBox);

        base.setAlignment(title, Pos.CENTER_LEFT);
        return base;   
    }//end infoContent method

    /*** Method that generates the start prompt for the game after the user presses the Start button
     *   It has options regarding in which way does the user want to play: 
     *   1. with a friend
     *   2. against the Ai
     *   And asks if he wants to go first or second(X Player or O Player)
     */
    private void startPrompt(){
        Alert options = new Alert(Alert.AlertType.CONFIRMATION);
        options.setTitle("Game options");
        options.setHeaderText(null);
        options.setContentText("How do you want to play the game?");
        options.initModality(Modality.APPLICATION_MODAL);
        options.initOwner(primaryStage);

        ButtonType friendButton = new ButtonType("With a friend");
        ButtonType soloButton = new ButtonType("Against the AI");
        
        options.getButtonTypes().setAll(friendButton, soloButton);

        Optional<ButtonType> choices = options.showAndWait();
        if(choices.get() == friendButton){
            first = new Player(true);
            second = new Player(false);
            playable = true;
        }else{
            first = new Player(true);
            second = null;
            playable = true;
        }
    }//end startPrompt method

    /*** Method that checks the state of the game */
    private void gameState(){
        for(TileCombo combo : combos){
            if(combo.isComplete()){
                playable = false;
                message.setText("Game over!");
                playWinAnimation(combo);
                break;
            }
        }
    }//end gameState method

    /*** Method that plays an animation when one of the players wins */
    private void playWinAnimation(TileCombo combo){
        line  = new Line();
        line.setStartX(combo.tiles[0].getCenterX());
        line.setStartY(combo.tiles[0].getCenterY());
        line.setEndX(combo.tiles[0].getCenterX());
        line.setEndY(combo.tiles[0].getCenterY());

        root.getChildren().add(line);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2),
            new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
            new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
        timeline.play(); 
        playAgainPrompt();
        
     }//end playWinAnimation metod

     /*** Method that creates a promp that will appear at the end of the game to see if the player wants to play again */
     private void playAgainPrompt(){
        Alert playAgain = new Alert(Alert.AlertType.CONFIRMATION);
        playAgain.setHeaderText(null);
        playAgain.setContentText("Play again?");

        Button yesButton = (Button) playAgain.getDialogPane().lookupButton(ButtonType.OK);
        yesButton.setText("Yes");

        Optional<ButtonType> choice = playAgain.showAndWait();
        if(ButtonType.OK.equals(choice.get())){
            for(int x  = 0; x < 3; x++){
                for(int y = 0; y < 3; y++){
                    board[x][y].setText(null);
                }
            }
            root.getChildren().remove(line);
            playable = true;
            turnX = true;
            message.setText("Good luck!");
        }
    }//end playAgainPrompt method

    /*** Private class that handles the tiles of the game board */
    private class Tile extends StackPane{
        private Text text = new Text();

        /*** Constructor for the Tile class that initializes a tile with a rectangle border */
        public Tile(){
            Rectangle border = new Rectangle(202, 202);//since we have a (600,600) pane then we can fit 3 rows and 3 columns (had to change the value a bit becuase of the padding)
            border.setFill(Color.rgb(225, 225, 225));
            border.setStroke(Color.BLACK);
            setAlignment(Pos.CENTER);
            text.setFont(Font.font(60));
            text.setText(null);

            getChildren().addAll(border, text);//adding the children to the tile

            setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    if(!playable){
                        return;
                    }

                    if(event.getButton() == MouseButton.PRIMARY){
                        if(turnX){
                            drawX();
                            gameState();
                        }else{
                            drawO();
                            gameState();
                        }
                        
                    }
                }
            });
        }

        private void drawX(){
            if(!text.getText().equals("X") && !text.getText().equals("O")){
                text.setText("X");
                turnX = false;
                message.setText("Good luck!");
            }else{
                message.setText("Can't place there!");
                
            }
        }

        private void drawO(){
            if(!text.getText().equals("X") && !text.getText().equals("O")){
                text.setText("O");
                turnX = true;
                message.setText("Good luck!");
            }else{
                message.setText("Can't place there!");
            }
        }

        public String getValue(){
            return text.getText();
        }

        public double getCenterX(){
            return getTranslateX() + 101;//we get the start of the tile, and then add half of it's size so we get the center
        }

        public double getCenterY(){
            return getTranslateY() + 136;
        }

        public void setText(String pattern){
            text.setText(pattern);
        }
    }//end inner Tile class

    /*** Inner class that checks if the player has managed to get the tiles combo needed to win or not*/
    private class TileCombo{
        private Tile[] tiles;
        /*** Constructor for the TileCombo class
         * @param tiles The array of tiles that will create the combo
         */
        public TileCombo(Tile... tiles){
            this.tiles = tiles;
        }
        
        /*** Method that checks if the combo of tiles is complete
         */
        public boolean isComplete(){
            if(tiles[0].getValue().isEmpty()){
                return false;
            }

            return tiles[0].getValue().equals(tiles[1].getValue())
                    && tiles[0].getValue().equals(tiles[2].getValue());
        }
    }//end inner TileCombo class
}//end Board class
