public class Player{
    private final boolean isFirst;

    /*** Constructor for the Player class
     * @param state If true then the player is the first(the X Player) else he is second(the O Player)
     */
    public Player(boolean state){
        isFirst = state;
    }

    /*** Getter for the state of the player
     * @return A boolean stating if the player is first or not
     */
    public boolean getState(){
        return isFirst;
    }
    
}//end Player class