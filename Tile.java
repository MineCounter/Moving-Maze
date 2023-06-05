import java.util.*;
import java.io.*;

/**
 * This class is used in the {@link Board} class for tile manipulation
 * @author Alok More
 */
public class Tile {
    
    /**
     * Checks if a tile is open to a compass side.
     * @param tileEncoding String value of the tile to check
     * @param dir Char value of the compass direction to check in
     * @return {@code true} if direction is open, otherwise {@code false}
     */
    public static boolean isTileOpen(String tileEncoding, char dir)
    {

        switch(dir)
        {
            case 'n':
            {
                if(tileEncoding.charAt(0) == '1')
                {
                    return true;
                }else return false;
            }
            case 'e':
            {
                if(tileEncoding.charAt(1) == '1')
                {
                    return true;
                }else return false;
            }
            case 's':
            {
                if(tileEncoding.charAt(2) == '1')
                {
                    return true;
                }else return false;
            }
            case 'w':
            {
                if(tileEncoding.charAt(3) == '1')
                {
                    return true;
                }else return false;
            }
            default:
                return false;
        }
    }

    /**
     * Rotates a tile clockwise.
     * @param encoding String value of the tile to check
     * @return The string value of the new rotated tile
     * @see Tile#rotateAntiClockwise(String)
     * @see Board#floatingPhase(String, int)
     */
    public static String rotateClockwise(String encoding)
    {

        char first = encoding.charAt(3);
        String three = encoding.substring(0, 3);
        String relic = encoding.substring(4, 6);

        

        return first+three+relic;
    }

    /**
     * Rotates a tile anticlockwise
     * @param encoding String value of the tile to check
     * @return The string value of the new rotated tile.
     * @see Tile#rotateClockwise(String)
     * @see Board#floatingPhase(String, int)
     */
    public static String rotateAntiClockwise(String encoding)
    {

        char first = encoding.charAt(0);
        String three = encoding.substring(1, 4);
        String relic = encoding.substring(4, 6);
        
        return three+first+relic;
    }

    /**
     * Creates the floating tile given its encoding.
     * @param encoding String value of the tile to convert
     * @return A 2D char array of the tile
     * @see Board#printBoard()
     */
    public static char[][] createFloating(String encoding)
    {

        char[][] tileBig = new char[5][9];
        String open = "";
        
        for(int j = 0; j < 5; j++)
        {
            for(int i = 0; i < 9; i++)
            {    
                    tileBig[j][i] = ' ';
            }
        }


        if(encoding.charAt(0) == '1')
        {
                tileBig[1][4] = Board.PATH_NS;
                open+= "n";
        }

        if(encoding.charAt(1) == '1')
        {
            for(int i = 5; i < 8; i++)
            {
                tileBig[2][i] = Board.PATH_EW;
            }
            open+= "e";
        }

        if(encoding.charAt(2) == '1')
        {
            tileBig[3][4] = Board.PATH_NS;
            open+= "s";
        } 
        
        if(encoding.charAt(3) == '1')
        {
            for(int i = 1; i < 4; i++)
            {
                tileBig[2][i] = Board.PATH_EW;
            }
            open+= "w";
        }

        switch (open) 
        {

            case "ne":
                tileBig[2][4] = Board.PATH_NE;
                break;

            case "ns":
                tileBig[2][4] = Board.PATH_NS;
                break;

            case "nw":
                tileBig[2][4] = Board.PATH_NW;
                break;

            case "nes":
                tileBig[2][4] = Board.PATH_NES;
                break;

            case "new":
                tileBig[2][4] = Board.PATH_NEW;
                break;

            case "nsw":
                tileBig[2][4] = Board.PATH_NSW;
                break;

            case "es":
                tileBig[2][4] = Board.PATH_ES;
                break;

            case "ew":
                tileBig[2][4] = Board.PATH_EW;
                break;

            case "esw":

                tileBig[2][4] = Board.PATH_ESW;
                break;

            case "sw":
                tileBig[2][4] = Board.PATH_SW;
                break;

            case "nesw":
                tileBig[2][4] = Board.PATH_NESW;
                break;

            default:
                tileBig[2][4] = '0';
                break;

        }

        tileBig[0][0]= Board.BORDER_TOPLEFT;
        tileBig[0][8] = Board.BORDER_TOPRIGHT;
        tileBig[4][0] = Board.BORDER_BOTTOMLEFT;
        tileBig[4][8] = Board.BORDER_BOTTOMRIGHT;

        for(int i = 1; i < 8; i++)
        {
            tileBig[0][i] = Board.BORDER_HORI;
            tileBig[4][i] = Board.BORDER_HORI;
        }

        for(int i = 1; i < 4; i++)
        {
            tileBig[i][0] = Board.BORDER_VERT;
            tileBig[i][8] = Board.BORDER_VERT;
        }

        
        //print tileBig
        return tileBig;
    }

    /**
     * Creates a tile given its encoding.
     * @param encoding String value of the tile to convert
     * @return A 2D char array of the tile.
     * @see Board#terminalBoard()
     */
    public static char[][] createTile(String encoding)
    {

        char[][] tile = new char[7][3];
        for(int j = 0; j < 7; j++)
        {
            for(int i = 0; i < 3; i++)
            {
                tile[j][i] = ' ';
            }
        }

        String open = "";

            if(encoding.charAt(0) == '1')
            {
                tile[3][0] = Board.PATH_NS;
                open += "n";
            }
                
            if(encoding.charAt(1) == '1')
            {
                for(int i = 4; i < 7; i++)
                {
                    tile[i][1] = Board.PATH_EW;
                }
                open += "e";
            }
            if(encoding.charAt(2) == '1')
            {
                tile[3][2] = Board.PATH_NS;
                open += "s";
            }
            if(encoding.charAt(3) == '1')
            {
                for(int i = 0; i < 3; i++)
                {
                    tile[i][1] = Board.PATH_EW;
                }
                open += "w";
            }
            
            switch (open) 
            {

            case "ne":
                tile[3][1] = Board.PATH_NE;
                break;

            case "ns":
                tile[3][1] = Board.PATH_NS;
                break;

            case "nw":
                tile[3][1] = Board.PATH_NW;
                break;

            case "nes":
                tile[3][1] = Board.PATH_NES;
                break;

            case "new":
                tile[3][1] = Board.PATH_NEW;
                break;

            case "nsw":
                tile[3][1] = Board.PATH_NSW;
                break;

            case "es":
                tile[3][1] = Board.PATH_ES;
                break;

            case "ew":
                tile[3][1] = Board.PATH_EW;
                break;

            case "esw":

                tile[3][1] = Board.PATH_ESW;
                break;

            case "sw":
                tile[3][1] = Board.PATH_SW;
                break;

            case "nesw":
                tile[3][1] = Board.PATH_NESW;
                break;

            default:
                tile[3][1] = '0';
                break;

        }

          
        return tile;
    }
    
    /**
     * Checks if the input is an even tile
     * @param positionRequested String value of the compass diection and number to slide into 
     * @return {@code true} if the slide is valid, otherwise {@code false}
     * @see Tile#SlideTest2(String, int, int)
     * @see Board#floatingPhase(String, int)
     */
    public static boolean slideTest(String positionRequested)
    {

        char cardinal = positionRequested.charAt(0);
        int number = Character.getNumericValue(positionRequested.charAt(1));

        switch (cardinal) 
        {
            case 'n':
            {
                if ((number % 2 == 0))
                {
                    return true;
                }
            }
                break;
            case 'e':
            {
                if ((number % 2 == 0))
                {
                    return true;
                }
            }
                break;
            case 's':
            {
                if ((number % 2 == 0))
                {
                    return true;
                }
            }
                break;
            case 'w':
            {
                if ((number % 2 == 0))
                {
                    return true;
                }
            }
                break;
            default:
                return false;
                
        }
        return false;
    }

    /**
     * Checks to make sure the input is in the bounds of the game board
     * @param input String value of the compass diection and number to slide into 
     * @param row The int value of the number of rows in the board
     * @param col The int value of the number f columns in the game board
     * @return {@code true} if the input is valid, otherwise {@code false}
     * @see Tile#slideTest(String)
     * @see Board#floatingPhase(String, int)
     */
    public static boolean SlideTest2(String input, int row, int col)
    {
        char cardinal = input.charAt(0);
        int number = Character.getNumericValue(input.charAt(1));

        switch (cardinal) 
        {
            case 'n':
            {
                if ((number > row))
                {
                    return false;
                }
            }
                break;
            case 'e':
            {
                if ((number > col))
                {
                    return false;
                }
            }
                break;
            case 's':
            {
                if ((number > row))
                {
                    return false;
                }
            }
                break;
            case 'w':
            {
                if ((number > col))
                {
                    return false;
                }
            }
                break;
            default:
                return true;
                
        }

        return true;
    }

    /**
     * Slides the tile onto the board and sets the floating tile to the one slid off.
     * @param pos String value of the compass diection and number to slide into 
     * @param row The int value of the number of rows in the board
     * @param col The int value of the number of columns in the board
     * @param tiles String array of the tile encodings
     * @param fT String value of the floating tile
     * @return A string value of the new floating tile
     * @see Board#floatingPhase(String, int)
     */
    public static String slideTile(String pos, int row, int col, String[][] tiles, String fT)
    {
        int num = Character.getNumericValue(pos.charAt(1));
        String temp = "";

        if (pos.charAt(0) == 'n'){
            temp = tiles[num-1][col-1];
            for(int i = col-1; i > 0; i--){
                
                tiles[num-1][i] = tiles[num-1][i-1];
            }
            tiles[num-1][0] = fT;
            Board.setTileEncodings(tiles);
            //MovingMaze.updateColorVal(Player, 2, colors[Player][2]-1);
        }

        if (pos.charAt(0) == 'w'){
            temp = tiles[row-1][num-1];
            for(int i = row-1; i > 0; i--){
                
                tiles[i][num-1] = tiles[i-1][num-1];

               
            }tiles[0][num-1] = fT;
            //bObj.setFloatingTile(temp);
            Board.setTileEncodings(tiles);
            //MovingMaze.updateColorVal(Player, 3, colors[Player][3]-1);
        }

        if (pos.charAt(0) == 's'){
            temp = tiles[num-1][0];
            for(int i = 0; i < col-1; i++){
                
                tiles[num-1][i] = tiles[num-1][i+1];
                
            }
            tiles[num-1][col-1] = fT;
            //bObj.setFloatingTile(temp);
            Board.setTileEncodings(tiles);
            //MovingMaze.updateColorVal(Player, 2, colors[Player][2]-1);
        }

        if (pos.charAt(0) == 'e'){
            temp = tiles[0][num-1];
            for(int i = 0; i < row-1; i++){
                
                tiles[i][num-1] = tiles[i+1][num-1];
                
            }tiles[row-1][num-1] = fT;
            //bObj.setFloatingTile(temp);
            Board.setTileEncodings(tiles);
            //MovingMaze.updateColorVal(Player, 3, colors[Player][3]+1);
        }
        
        
        return temp;
        
    }
}
