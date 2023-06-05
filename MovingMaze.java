/** Stellenbosch University CS144 Project 2022
 * MovingMaze.java
 * Name and surname: Alok More
 * Student number: 25876864
 */

import java.util.*;
import java.io.*;
/**
 * This class is used to create a moving maze game.
 * @author Alok More
 * @see Board
 * @see Tile
 * @see Relics
 */
public class MovingMaze {

    // ==========================================================
    // Main function
    // ==========================================================

    /**
     * This is the main function of the program. It contains the main game loop.
     * <p>
     * The main function is responsible for the following:
     * <ul>
     * <li>Reading the arguemnts</li>
     * <li>Instantiating the Board Object</li>
     * <li>Passing the arguements to board object</li>
     * <li>Running the game loop</li>
     * </ul>
     * 
     * @param args The game board file name and the number of relics to be collected
     * 
     * @see Board#initialiseBoard()
     * @see Board#floatingPhase(String, int)
     * @see Board#movingPhase(String, int)
     */
    public static void main(String[] args) {
        // args[0] will contain the filename of the game board file to be loaded.
        // args[1] will contain either "text" or "gui".

        File gameBoardFile = new File(args[0]);
        String mode = args[1];

        if (!gameBoardFile.exists()) {
            System.out.println("The game board file does not exist.");
            System.exit(1);
        }

        if (!mode.equals("text") && !mode.equals("gui")) {
            System.out.println("Unknown visual mode.");
            System.exit(1);
        }

        Board board = new Board(gameBoardFile, mode);
        //Tile tObj = new Tile(board.getBoard(), board.getTileEncodings(), board.getFloatingTile());

        board.initialiseBoard();

        boolean gameState = true;
        String currentPlayer = "Green";
        int player = 0;
        

        if(mode.equals("text")) 
        {    
            while(gameState)
            {   
                
                while(board.floatingPhase(currentPlayer, player)){};
                while(board.movingPhase(currentPlayer, player)){};


                if(player == 0)
                    {
                        currentPlayer = "Yellow";
                        player = 1;
                    }else if(player == 1)
                    {
                        currentPlayer = "Red";
                        player = 2;

                    }else if(player == 2)
                    {
                        currentPlayer = "Blue";
                        player = 3;
                    }else if(player == 3)
                    {
                        currentPlayer = "Green";
                        player = 0;
                    }
            }
        }

        if(mode.equals("gui")) 
        {
            board.gui(board.getRow(), board.getCol(), currentPlayer);
        }

    }

    // ==========================================================
    // Test API functions
    // ==========================================================

    // Populate these with high-level code that references your codebase.

    // ----------------------------------------------------------
    // First hand-in

    /**
     * This function is used to if a tile is open in the indicated direction.
     * @param tileEncoding The tile encoding of the tile to be checked
     * @param dir The direction to be checked
     * @return {@code true} if the tile is open in the indicated direction, {@code false} otherwise
     * @see Tile#isTileOpen(String, char)
     */
    public static boolean isTileOpenToSide(String tileEncoding, char dir) {
        return Tile.isTileOpen(tileEncoding, dir);
    }
    /**
     * Take the tile and rotate it once clockwise.
     * @param tileEncoding The tile encoding of the tile to be rotated
     * @return  a boolean array with length 4 – each element in the array indicates whether the rotated tile is open to a specific side.
     * @see Tile#rotateClockwise(String)
     */
    public static boolean[] rotateTileClockwise(String tileEncoding) {

        boolean[] output = new boolean[4];
        String open = Tile.rotateClockwise(tileEncoding);
        for (int i = 0; i < 4; i++) 
        {
            if (open.charAt(i) == '1') 
            {
                output[i] = true;
            } else output[i] = false;
        }

        return output;
    }

    /**
     * Take the tile and rotate it once anticlockwise.
     * @param tileEncoding The tile encoding of the tile to be rotated
     * @return  a boolean array with length 4 – each element in the array indicates whether the rotated tile is open to a specific side.
     * @see Tile#rotateAntiClockwise(String)
     */
    public static boolean[] rotateTileCounterclockwise(String tileEncoding) {
        
        boolean[] output = new boolean[4];
        String open = Tile.rotateAntiClockwise(tileEncoding);
        for (int i = 0; i < 4; i++) 
        {
            if (open.charAt(i) == '1') 
            {
                output[i] = true;
            } else output[i] = false;
        }

        return output;
    }

    /**
     * Take the floating tile and insert it into the maze.
     * @param mazeTileEncodings The 2D array of strings containing the maze tile encodings
     * @param floatingTileEncoding The floating tile encoding
     * @param slidingIndicator The position where the floating tile is to be inserted 
     * @return a boolean array of length 4.
     * <p>
     * Each element in the array indicates whether the new floating tile is open to a specific side.
     * @see MovingMaze#apiSlideTile(String[][], String, String, int, int)
     */
    public static boolean[] slideTileIntoMaze1(String[][] mazeTileEncodings, String floatingTileEncoding, String slidingIndicator) {
        
        int row = mazeTileEncodings.length;
        int col = mazeTileEncodings[0].length;

        return apiSlideTile(mazeTileEncodings, floatingTileEncoding, slidingIndicator, row, col);
    }

    /**
     * Checks if a player can move from one tile to the next. 
     * @param curTileEncoding The tile encoding of the current tile of the player
     * @param newTileEncoding The tile encoding of the new tile the player is trying to move to
     * @param dir The direction the player is trying to move to
     * @return {@code true} if the tile {@code newTileEncoding} can be stepped to from the tile {@code currentTileEncoding} in the direction {@code dir}, {@code false} otherwise
     * @see Tile#isTileOpen(String, char)
     */
    public static boolean canMoveInDirection(String curTileEncoding, String newTileEncoding, char dir) {
        
        boolean current = false;
        boolean newTile = false;

        if (Tile.isTileOpen(curTileEncoding, dir))
        {
            current = true;
        }
        if (Tile.isTileOpen(newTileEncoding, cardinalOpposite(dir)))
        {
            newTile = true;
        }
        if(current && newTile)
        {
            return true;
        }

        return false; 
    }
    /**
     * Checks if a player can move from a starting position of 0,0 to an end position.
     * @param mazeTileEncodings The 2D array of strings containing the maze tile encodings
     * @param steps The steps the player is trying to take, populated with the four cardinal directions
     * @return {@code true} if the player can move along the path, otherwise {@code false}
     * @see MovingMaze#apiCanMove(String[][], char[])
     */
    public static boolean canMoveAlongPath(String[][] mazeTileEncodings, char[] steps) {
        return apiCanMove(mazeTileEncodings, steps);
    }

    // ----------------------------------------------------------
    // Second hand-in

    /**
     * Checks if there is a relic on the given tileEncoding
     * @param tileEncoding The tile encoding of the tile to be checked
     * @return {@code true} if the tile contains a relic, {@code false} otherwise
     * @see Relics#tileHasRelic(String)
     */
    public static boolean tileHasRelic(String tileEncoding) {
        Relics rObj = new Relics();
        return rObj.tileHasRelic(tileEncoding);
    }
     
    /**
     * Take the floating tile and insert it into the maze.
     * @param mazeTileEncodings The 2D array of strings containing the maze tile encodings
     * @param floatingTileEncoding The floating tile encoding
     * @param slidingIndicator The position where the floating tile is to be inserted
     * @return The character of the relic on the new floating tile, if there is no relic, return {@code 'x'}
     * @see MovingMaze#apiSlideTile2(String[][], String, String, int, int)
     */
    public static char slideTileIntoMaze2(String[][] mazeTileEncodings, String floatingTileEncoding, String slidingIndicator) {
        
        int row = mazeTileEncodings.length;
        int col = mazeTileEncodings[0].length;

        String ftEncoding =apiSlideTile2(mazeTileEncodings, floatingTileEncoding, slidingIndicator, row, col);

        return ftEncoding.charAt(4);
    }

    //-----------------------------------------------------------
    // Self-made functions

    /**
     * This function takes in a compass direction and returns the opposite direction.
     * @param dir The compass direction
     * @return the opposite direction
     */
    public static char cardinalOpposite(char dir)
    {

        switch(dir){
            case 'n': return 's';
            case 'e': return 'w';
            case 's': return 'n';
            case 'w': return 'e';
            default: return '0';
        }

    }

    /**
     * This function slides a tile into the maze and returns the a boolean array of the new floating tile's open sides.
     * @param mazeTileEncodings The 2D array of strings containing the maze tile encodings
     * @param floatingTileEncoding The floating tile encoding
     * @param slidingIndicator The position where the floating tile is to be inserted
     * @param row The number of rows in the maze
     * @param col The number of columns in the maze
     * @return a boolean array of length 4. Each element in the array indicates whether the new floating tile is open to a specific side.
     * @see MovingMaze#slideTileIntoMaze1(String[][], String, String) 
     */
    public static boolean[] apiSlideTile(String[][] mazeTileEncodings, String floatingTileEncoding, String slidingIndicator, int row, int col)
    {

        boolean[] result = new boolean[4];
        String newFTE = " ";

        char cardinal = slidingIndicator.charAt(0);
        char number = slidingIndicator.charAt(1);
        int num = Character.getNumericValue(number);

        if(cardinal == 'n'){
            newFTE = mazeTileEncodings[row-1][num-1];
        }else if(cardinal == 'e'){
            newFTE = mazeTileEncodings[num-1][0];
        }else if(cardinal == 's'){
            newFTE = mazeTileEncodings[0][num-1];
        }else if(cardinal == 'w'){
            newFTE = mazeTileEncodings[num-1][col-1];
        }

        char cardN = 'n';
        char cardE = 'e';
        char cardS = 's';
        char cardW = 'w';

        result[0] = isTileOpenToSide(newFTE, cardN);
        result[1] = isTileOpenToSide(newFTE, cardE);
        result[2] = isTileOpenToSide(newFTE, cardS);
        result[3] = isTileOpenToSide(newFTE, cardW);

        return result;
    }

    /**
     * This function slides a tile into the maze and returns the new floating tile's encoding.
     * @param mazeTileEncodings The 2D array of strings containing the maze tile encodings
     * @param floatingTileEncoding The floating tile encoding
     * @param slidingIndicator The position where the floating tile is to be inserted
     * @param row The number of rows in the maze
     * @param col The number of columns in the maze
     * @return The new floating tile's encoding.
     * @see MovingMaze#slideTileIntoMaze2(String[][], String, String)
     */
    public static String apiSlideTile2(String[][] mazeTileEncodings, String floatingTileEncoding, String slidingIndicator, int row, int col)
    {

        String newFTE = " ";

        char cardinal = slidingIndicator.charAt(0);
        char number = slidingIndicator.charAt(1);
        int num = Character.getNumericValue(number);

        if(cardinal == 'n'){
            newFTE = mazeTileEncodings[row-1][num-1];
        }else if(cardinal == 'e'){
            newFTE = mazeTileEncodings[num-1][0];
        }else if(cardinal == 's'){
            newFTE = mazeTileEncodings[0][num-1];
        }else if(cardinal == 'w'){
            newFTE = mazeTileEncodings[num-1][col-1];
        }

        return newFTE;
    }

    /**
     * This function checks if you can move along a path.
     * @param mazeTileEncodings The 2D array of strings containing the maze tile encodings
     * @param steps The path to be checked
     * @return {@code true} if you can move along the path, {@code false} otherwise
     * @see MovingMaze#canMoveAlongPath(String[][], char[])
     */
    public static boolean apiCanMove(String[][] mazeTileEncodings, char[] steps)
    {
        boolean result = true;

        int row = 0;
        int col = 0;
        
        boolean[] stepz = new boolean[steps.length];
        for (int i = 0; i < steps.length; i++)
        {   
            char input = steps[i];
            //if(steps[i] == 'n')
            switch (input)
            {
                case 'n':
                {
                    if (row == 0){
                        return false;
                    }
                     if (row - 1 < 0)
                        {
                            return false;
                        }
                        
                            stepz[i] = canMoveInDirection(mazeTileEncodings[row][col], mazeTileEncodings[row-1][col], steps[i]);
                            row -= row;
                        
                    
                }
                case 'e':
                {
                    if (col == mazeTileEncodings[0].length-1)
                    {
                        return false;
                    }
                      
                        if (col + 1 >= mazeTileEncodings[0].length)
                        {return false;
                        }
                        
                            stepz[i] = canMoveInDirection(mazeTileEncodings[row][col], mazeTileEncodings[row][col+1], steps[i]);
                            col += col;
                        
                    
                }
                case 's':
                {
                    if (row == mazeTileEncodings.length-1)
                    {
                        return false;
                    } 
                    
                        if (row + 1 >= mazeTileEncodings.length)
                        {
                            return false;
                        }
                        
                            stepz[i] = canMoveInDirection(mazeTileEncodings[row][col], mazeTileEncodings[row+1][col], steps[i]);
                            row += row;
                        
                    
                }
            
            case 'w':
                {
                    if (col == 0)
                    {
                        return false;
                    }
                    
                        if (col - 1 < 0)
                        {
                            return false;
                        }
                        
                            stepz[i] = canMoveInDirection(mazeTileEncodings[row][col], mazeTileEncodings[row][col-1], steps[i]);
                            col -= col;
                        
                    
                }
            }

        }
        return true;
    }

}
