import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.sound.sampled.AudioFormat.Encoding;
import javax.swing.*;

/**
 * This class is used to create the game board object.
 * @author Alok More
 * 
 */
public class Board extends JFrame implements ActionListener, MouseListener
{

    private File file;
    private String mode;
    private static int k = 0;
    private int row = 0;
    private int col = 0;
    private static String floatingTile = "";
    private int boardHeight;
    private int boardWidth;
    private char[][] board;
    private static String[][] tileEncodings;
    private String oldInput;
    private static Font standard = new Font("Raleway", Font.BOLD, 25);
    /**
     * This 2D array is use to store the different player attributes.
     * <p>
     * The array can be read as [color][attribute] with the following attributes:
     * <ul>
     * <li>[0] = Row position relative to the entire game board</li>
     * <li>[1] = Column position relative to the entire game board</li>
     * <li>[2] = Row position of the tile</li>
     * <li>[3] = Column position of the tile</li>
     * <li>[4] = Number of relic collected </li>
     * </ul>
     * @see Relics
     */
    public static int[][] colors = {{3, 2, 0, 0, 0}, {0, 2, 0, 0, 0}, {3, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};
    public static char[] cardinalDirections = {'n', 'e', 's', 'w'};
    private static boolean[][] visited;
    private static JPanel p = new JPanel();
    private static JPanel floatingPanel;
    private static JButton floatingTileB;
    private JPanel back;
    private JButton[][] b;
    private String[][] encoding;
    private Integer[] gameState = {0,0};
    private JPanel playerDis;
    private JPanel score;
    Relics rObj = new Relics();

    /**
     * Corridor char for East-West
     */
    public static final char PATH_EW = '═';
    /**
     * Corridor char for North-South
     */
    public static final char PATH_NS = '║';
    /**
     * Corridor char for East-South
     */
    public static final char PATH_ES = '╔';
    /**
     * Corridor char for South-West
     */
    public static final char PATH_SW = '╗';
    /**
     * Corridor char for East-North
     */
    public static final char PATH_NE = '╚';
    /**
     * Corridor char for North-West
     */
    public static final char PATH_NW = '╝';
    /**
     * Corridor char for North-East-South
     */
    public static final char PATH_NES = '╠';
    /**
     * Corridor char for North-South-West
     */
    public static final char PATH_NSW = '╣';
    /**
     * Corridor char for East-South-West
     */
    public static final char PATH_ESW = '╦';
    /**
     * Corridor char for North-East-West
     */
    public static final char PATH_NEW = '╩';
    /**
     * Corridor char for North-East-South-West
     */
    public static final char PATH_NESW = '╬';
    
    /**
     * Horizonatal Border
     */
    public static final char BORDER_HORI = '─';
    /**
     * Vertical Border
     */
    public static final char BORDER_VERT = '│';
    /**
     * Top Left Corner
     */
    public static final char BORDER_TOPLEFT = '┌';
    /**
     * Top Right Corner
     */
    public static final char BORDER_TOPRIGHT = '┐';
    /**
     * Bottom Left Corner
     */
    public static final char BORDER_BOTTOMLEFT = '└';
    /**
     * Bottom Right Corner
     */
    public static final char BORDER_BOTTOMRIGHT = '┘';
    /**
     * Left Edge
     */
    public static final char BORDER_LEFTEDGE = '├';
    /**
     * Right Edge
     */
    public static final char BORDER_RIGHTEDGE = '┤';
    /**
     * Top Edge
     */
    public static final char BORDER_TOPEDGE = '┬';
    /**
     * Bottom Edge
     */
    public static final char BORDER_BOTTOMEDGE = '┴';
    /**
     * Intersection
     */
    public static final char BORDER_MIDDLE = '┼';

    /**
     * This constructor is used to create the game board object.
     * @param file The file object that contains the game board.
     * @param mode The text or gui mode of the game.
     */
    Board(File file, String mode) 
    {
        // TODO
        this.file = file;
        this.mode = mode;

    }

    /**
     * This method is used to print the starting message of the game.
     * @param k The number of relics to be collected.
     */
    public static void gameStartMessage(int k)
    {
        StdOut.println("--------------------------------------------------");
        StdOut.println("Moving Maze");
        StdOut.println("Relic goal: " + k);
        StdOut.println("--------------------------------------------------");
    }

    /**
     * This method:
     * <p>
     * <ul>
     * <li>Reads the file.</li> 
     * <li>Stores the number of rows in {@link row}.</li>
     * <li>Stores the number of columns in {@link col}.</li>
     * <li>Stores the number of relics to be collected in {@link k}.</li>
     * <li>Stores the floating tile in {@link floatingTile}.</li>
     * <li>Stores the tile encodings in a 2D array {@link tileEncodings}.</li>
     * </ul>
     * <p>
     * It also calls the {@link gameStartMessage(int)} method to print the starting message of the game.
     * @exception IOException If the file cannot be read.
     * @see Board#gameStartMessage(int)
     * @see Board#terminalBoard()
     * @see Board#setRow(int)
     * @see Board#setCol(int)
     * @see Board#setK(int)
     * @see Board#setFloatingTile(String)
     * @see Board#setTileEncodings(String[][])
     * @see Board#setBoardHeight(int)
     * @see Board#setBoardWidth(int)
     * @see Board#createGameBoard(int, int)
     * @see Board#updateColorVal(int, int, int)
     */
    public void initialiseBoard()
    {

        try
        {
            Scanner input = new Scanner(file);
            setRow(input.nextInt());
            setCol(input.nextInt());
            setK(input.nextInt());
            rObj.setK(k);
            setFloatingTile(input.next());
            String[][] tileEncodings1 = new String[row][col];
            for (int i = 0; i < col; i++)
            {
                for (int j = 0; j < row; j++)
                {
                    tileEncodings1[j][i] = input.next();
                }
            }

            setTileEncodings(tileEncodings1);
            input.close();
        } catch (Exception e)
        {
            System.err.println("Error reading game board file.");
            System.exit(1);
        }

        setBoardHeight(4*col+3);
        setBoardWidth(8*row+3);
        createGameBoard(boardHeight, boardWidth);

        updateColorVal(1, 3, row-1);
        updateColorVal(2, 2, col-1);
        updateColorVal(3, 3, row-1);
        updateColorVal(3, 2, col-1);

        updateColorVal(1, 0, boardWidth-4);
        updateColorVal(2, 1, boardHeight-3);
        updateColorVal(3, 1, boardHeight-3);
        updateColorVal(3, 0, boardWidth-4);

        gameStartMessage(k);
        terminalBoard();

    }

    /**
     * This method is used to create the game board in the terminal.
     * <p></p>
     * <ul>
     * <li> It creates the numbers around the board to indicate tiles </li>
     * <li> Creates borders around the board and each tile </li>
     * <li> Creates the pathways in the tiles </li>
     * <li> Prints the players on the board </li>
     * <li> Checks for relics and displays the active relic using {@link Relics#getRelicColor(String)}, {@link Relics#getRelicNumber(String)} and setting it's location using {@link Relics#setRelicLocation(char, int[])}. </li>
     * </ul>
     * @see Tile#createTile(String)
     * @see Board#colors
     * @see Relics#getRelicColor(String)
     * @see Relics#getRelicNumber(String)
     * @see Relics#setRelicLocation(char, int[])
     * @see printBoard()
     * 
     */
    public void terminalBoard(){

        for(int i = 0; i < row; i++)
        {
            board[0][8*i+5] = (char)(i+1+'0');
        }

        for(int i = 0; i < col; i++)
        {
            board[4*i+3][0] = (char)(i+1+'0');
        }
        
        for(int i = 0; i < row; i++)
        {
            board[boardHeight-1][8*i+5] = (char)(i+1+'0');
        }

        for(int i = 0; i < col; i++)
        {
            board[4*i+3][boardWidth-1] = (char)(i+1+'0');
        }

        for(int i = 1; i < boardWidth-1; i++)
        {
            for(int j = 1; j < boardHeight-1; j++)
            {
                if ((j-1)%4 == 0)
                {
                    board[j][i] = Board.BORDER_HORI;
                }
                
            }
            
        }

        for(int i = 1; i < boardWidth-1; i++)
        {
            for(int j = 2; j < boardHeight-2; j++)
            {
                if((i-1)%8 == 0)
                {
                    board[j][i] = Board.BORDER_VERT;
                }
            }
        }

        for (int i = 1; i < boardWidth-1; i++) 
        {
            for (int j = 1; j < boardHeight-1; j++) 
            {
                if((i-1)%8 == 0 && (j-1)%4 == 0)
                {
                    board[j][i] = Board.BORDER_MIDDLE;
                }
                if(j ==1 && (i-1)%8 == 0)
                {
                    board[j][i] = Board.BORDER_TOPEDGE;
                }
                if(j == (boardHeight - 2) && (i-1)%8 == 0)
                {
                    board[j][i] = Board.BORDER_BOTTOMEDGE;
                }
                if(i ==1 && (j-1)%4 == 0)
                {
                    board[j][i] = Board.BORDER_LEFTEDGE;
                }
                if(i == (boardWidth - 2) && (j-1)%4 == 0)
                {
                    board[j][i] = Board.BORDER_RIGHTEDGE;
                }
                if (i == 1 && j == 1)
                {
                    board[j][i] = Board.BORDER_TOPLEFT;
                }
                if (j == 1 && i == boardWidth-2)
                {
                    board[j][i] = Board.BORDER_TOPRIGHT;
                }

                if (j == boardHeight-2 && i == 1)
                {
                    board[j][i] = Board.BORDER_BOTTOMLEFT;
                }

                if (j == boardHeight-2 && i == boardWidth-2)
                {
                    board[j][i] = Board.BORDER_BOTTOMRIGHT;
                }

            }
        }



        for(int x = 0; x < 7; x++)
                {
                    for(int y = 0; y < 3; y++)
                    {
                        for(int j = 0; j < col; j++)
                        {
                            for(int i = 0; i < row; i++)
                            {
                                char[][] temp = Tile.createTile(tileEncodings[i][j]);
                                if(k != 0)
                                { 
                                    if (rObj.getRelicColor(tileEncodings[i][j])=='g')
                                    {
                                        if (colors[0][4]+1 == rObj.getRelicNumber(tileEncodings[i][j]))
                                        {
                                            temp[3][1] = 'g';
                                            int[] location = {i, j};
                                            rObj.setRelicLocation('g', location);
                                            
                                        }
                                    }

                                    if (rObj.getRelicColor(tileEncodings[i][j])=='y')
                                    {
                                        if (colors[1][4]+1 == rObj.getRelicNumber(tileEncodings[i][j]))
                                        {
                                            temp[3][1] = 'y';
                                            int[] location = {i, j};
                                            rObj.setRelicLocation('y', location);
                                        }
                                    }

                                    if (rObj.getRelicColor(tileEncodings[i][j])=='r')
                                    {
                                        if (colors[2][4]+1 == rObj.getRelicNumber(tileEncodings[i][j]))
                                        {
                                            temp[3][1] = 'r';
                                            int[] location = {i, j};
                                            rObj.setRelicLocation('r', location);
                                        }
                                    }

                                    if (rObj.getRelicColor(tileEncodings[i][j])=='b')
                                    {
                                        if (colors[3][4]+1 == rObj.getRelicNumber(tileEncodings[i][j]))
                                        {
                                            temp[3][1] = 'b';
                                            int[] location = {i, j};
                                            rObj.setRelicLocation('b', location);
                                        }
                                    }
                                }
                                
                                board[4*j+2+y][8*i+2+x] = temp[x][y];
                            }
                        }
                    }
                }

        board[colors[0][1]][colors[0][0]]= 'G';
        board[colors[1][1]][colors[1][0]]= 'Y';
        board[colors[2][1]][colors[2][0]]= 'R';
        board[colors[3][1]][colors[3][0]]= 'B';
        printBoard();
    }
    
    /**
     * This method prints the board to the terminal as well as creating the floating tile, displaying its relic and printing it.
     * 
     * @see Tile#createFloating(String)
     * @see Relics#getRelicColor(String)
     * @see Relics#getRelicNumber(String)
     * 
     */
    public void printBoard(){
        StdOut.println();
        for (int i = 0; i < boardHeight; i++)
        {
            for (int j = 0; j < boardWidth; j++)
            {
                StdOut.print(board[i][j]);
            }
            StdOut.println();
        }

        StdOut.println();
        char[][] ftEncoding = Tile.createFloating(floatingTile);
        
        Relics rObj = new Relics();

        if (rObj.getRelicColor(floatingTile)=='g')
        {
            if (colors[0][4]+1 == rObj.getRelicNumber(floatingTile))
            {
                ftEncoding[2][4] = 'g';
            }
        }

        if (rObj.getRelicColor(floatingTile)=='y')
        {
            if (colors[1][4]+1 == rObj.getRelicNumber(floatingTile))
            {
                ftEncoding[2][4] = 'y';
            }
        }

        if (rObj.getRelicColor(floatingTile)=='r')
        {
            if (colors[2][4]+1 == rObj.getRelicNumber(floatingTile))
            {
                ftEncoding[2][4] = 'r';
            }
        }

        if (rObj.getRelicColor(floatingTile)=='b')
        {
            if (colors[3][4]+1 == rObj.getRelicNumber(floatingTile))
            {
                ftEncoding[2][4] = 'b';
            }
        }

        for(int j = 0; j < 5; j++)
        {
            for(int i = 0; i < 9; i++)
            {
                StdOut.print(ftEncoding[j][i]);
            }
            StdOut.println();
        }

        StdOut.println();
        
    }

    /**
     * Set the row of the gameboard object.
     * @param row The int row value to set to
     * @see Board#getRow()
     */
    public void setRow(int row)
    {
        this.row = row;
    }
    /**
     * Returns the row of the gameboard object.
     * @return The int value of {@link Board#row}
     * @see Board#setRow(int)
     */
    public int getRow()
    {
        return row;
    }

    /**
     * Returns the column of the gameboard object.
     * @return The int value of {@link Board#col}
     * @see Board#setCol(int)
     */
    public int getCol()
    {
        return col;
    }

    /**
     * Set the column of the gameboard object.
     * @param col The int column value to set to
     * @see Board#getCol()
     */
    public void setCol(int col)
    {
        this.col = col;
    }

    /**
     * Set the relic goal for the game in the gameboard object.
     * @param k The int relic goal value to set to
     * @see Board#getK()
     */
    public static void setK(int k)
    {
        Board.k = k;
    }
    /**
     * Returns the relic goal of the game.
     * @return The int value of {@link Board#k}
     * @see Board#setK(int)
     */
    public static int getK() {
        return k;
    }

    /**
     * Set the active floating tile encoding 
     * @param floatingTile The new floating tile encoding
     * @see Board#getFloatingTile()
     */
    public void setFloatingTile(String floatingTile)
    {
        this.floatingTile = floatingTile;
    }

    /**
     * Set the dimension of Height when initially making the board.
     * @param boardHeight The int value to set to.
     * @see Board#initialiseBoard()
     */
    public void setBoardHeight(int boardHeight)
    {
        this.boardHeight = boardHeight;
    }

    /**
     * Set the dimension of Width when initally making the board
     * @param boardWidth The int value to set to.
     * @see Board#initialiseBoard()
     */
    public void setBoardWidth(int boardWidth)
    {
        this.boardWidth = boardWidth;
    }

    /**
     * Set the encodings of the tiles making up the board
     * @param s A 2D String array making up the {@link Board#row} by {@link Board#col} grid
     * @see Board#initialiseBoard()
     */
    public static void setTileEncodings(String[][] s)
    {
        tileEncodings = s;
    }

    /**
     * Takes in {@link Board#boardHeight} and {@link Board#boardWidth} to create the gamboard and initialise it with spaces.
     * @param boardHeight The int value of board's Height.
     * @param boardWidth The int value of the board's Width.
     * @see Board#initialiseBoard()
     */
    public void createGameBoard(int boardHeight, int boardWidth)
    {
        board = new char[boardHeight][boardWidth];
        for (int i = 0; i < boardWidth; i++)
        {
            for (int j = 0; j < boardHeight; j++)
            {
                board[j][i] = ' ';
            }
        }
    }

    /**
     * Returns the active floating tile encoding
     * @return The string encoding {@link Board#floatingTile}
     */
    public String getFloatingTile()
    {
        return floatingTile;
    }

    /**
     * Returns the game board.
     * @return A 2D char array {@link Board#board}
     */
    public char[][] getBoard()
    {
        return board;
    }

    /**
     * Returns the tile encodings of all the tiles making up the game board.
     * @return A 2D String array {@link Board#tileEncodings}
     */
    public String[][] getTileEncodings()
    {
        return tileEncodings;
    }

    /**
     * Sets the old input, for the next move to check if a player is not sliding a tile from the opposite side of the last direction slid from.
     * @param s The string value to set to.
     * @see Board#floatingPhase(String, int)
     */
    public void setOldInput(String s)
    {
        this.oldInput = s;
    }
    /**
     * Returns the old input in {@link Board#floatingPhase(String, int)}, to check if a player is not sliding a tile from the opposite side of the last direction slid from.
     * @return The string value of {@link Board#oldInput}
     * @see Board#floatingPhase(String, int)
     */
    public String getOldInput()
    {
        return oldInput;
    }

    /**
     * Updates {@link Board#colors}. 
     * @param color The Player to update.
     * @param pos The attribute to update.
     * @param value The new int value to update to.
     * @see Board#getColorVal(int, int)
     */
    public static void updateColorVal(int color, int pos, int value){
        colors[color][pos] = value;
    }

    /**
     * Returns the value of an atrribute from a specific player.
     * @param color The Player whose attribute is needed.
     * @param pos The specific attribute needed.
     * @return An int value of the requested attribute from a player.
     * @see Board#updateColorVal(int, int, int)
     */
    public static int getColorVal(int color, int pos){
        return colors[color][pos];
    }

    /**
     * This function takes in a compass direction and returns the opposite direction.
     * @param dir The compass direction
     * @return the String value of the opposite direction of 
     */
    public static String cardinalOpposite(char dir)
    {

        switch(dir){
            case 'n': return "s";
            case 'e': return "w";
            case 's': return "n";
            case 'w': return "e";
            default: return "0";
        }

    }

    /**
     * Gets a specific tile encoding given the x,y co-ordinates of the tile
     * @param x The specific column of the tile
     * @param y The specific row of the tile.
     * @return A string from {@link Board#tileEncodings} 
     */
    public String getEncoding(int x, int y)
    {
        if (x < 0 || x > col-1 || y < 0 || y > row-1)
            return "0000xx";
        else return tileEncodings[y][x];
    }
    
    /**
     * Prints the scoreboard of number of relics collected
     * @see Board#colors
     * @see Board#k
     */
    public static void scoreboard(){
        StdOut.println("Relics collected /"+Board.k+":");
        StdOut.println("- Green  "+colors[0][4]);
        StdOut.println("- Yellow "+colors[1][4]);
        StdOut.println("- Red    "+colors[2][4]);
        StdOut.println("- Blue   "+colors[3][4]);
    }

    /**
     * Takes in a compass direction abbreviation and returns it's full form.
     * @param in The direction abbreviation
     * @return A string of the full form of a compass direction.
     * @see Board#movingPhase(String, int)
     */
    public static String cardinalString(String in)
    {
        switch(in){
            case "n": return "north";
            case "e": return "east";
            case "s": return "south";
            case "w": return "west";
            default: return "";
        }
    }

    /**
     * The first phase of a player's turn. It allows the player to rotate the {@link Board#floatingTile} and slide it in on the board.
     * The phase is only over if a tile is slid.
     * <p>
     * Calls upon multiple smaller functions to operate, check under See Also.
     * @param currentPlayer A string of the current player's color.
     * @param color An int value of the current player's color.
     * @return {@code true} if still in floating phase, {@code false} otherwise.
     * @see Board#setFloatingTile(String)
     * @see Tile#rotateClockwise(String)
     * @see Tile#rotateAntiClockwise(String)
     * @see Board#getFloatingTile()
     * @see Tile#slideTest(String)
     * @see Tile#SlideTest2(String, int, int)
     * @see Board#cardinalOpposite(char)
     * @see Tile#slideTile(String, int, int, String[][], String)
     * @see Board#setFloatingTile(String)
     * @see Board#setOldInput(String)
     * @see Board#slidePlayer(String, int, int, int)
     * @see Board#terminalBoard()
     */
    public boolean floatingPhase(String currentPlayer, int color)
    {

        boolean loop = true;
        boolean relicCollected = false;
        String newInput;
        
        StdOut.println("["+currentPlayer+"] Rotate and slide the floating tile:");
        StdOut.print("> ");
        String input = StdIn.readString();

        switch (input)
        {
            case "r":
            {
                setFloatingTile(Tile.rotateClockwise(getFloatingTile()));
                StdOut.println("Rotating right.");
                terminalBoard();
                break;
            }

            case "l":
            {
                setFloatingTile(Tile.rotateAntiClockwise(getFloatingTile()));
                StdOut.println("Rotating left.");
                terminalBoard();
                break;
            }
            
            case "quit":
            {
                StdOut.println("Game has been quit.");
                scoreboard();
                System.exit(0);
            }

            default:
            {
                if (input.length()!=2)
                {
                    StdOut.println("Invalid input.");
                    break;
                }

                if (!Tile.slideTest(input))
                {
                    StdOut.println("Cannot slide into odd positions.");
                    break;
                }
                else
                {
                    if (Tile.SlideTest2(input, row, col))
                    {
                        newInput = cardinalOpposite(input.charAt(0))+input.charAt(1)+"";
                        if (Objects.equals(oldInput, newInput))
                        {
                            StdOut.println("Cannot slide into last exit point.");
                            break;
                        }
                        else
                        {
                            String temp = Tile.slideTile(input, row, col, tileEncodings, floatingTile);
                            setFloatingTile(temp);
                            setOldInput(input);
                            StdOut.println("Inserting at "+input+".");
                            for (int i = 0; i < 4; i++)
                            {
                                slidePlayer(input, i, row, col);
                            }
                    
                            terminalBoard();
                            loop = false;
                            break;
                        }
                    }
                    else
                    {
                        StdOut.println("Invalid input.");
                        break;
                    }
                }
            }

        }

        return loop;
    }

    /**
     * The second phase of a player's turn. Allows the player to navigate the board and collect relics.
     * The phase is only over if the player types done or collects a relic.
     * <p>
     * Calls upon multiple smaller functions to operate, check under See Also.
     * @param currentPlayer A string of the current player's color.
     * @param color An int value of the current player's color.
     * @return {@code true} if still in phase, {@code false} otherwise.
     * @see Board#scoreboard()
     * @see Board#terminalBoard()
     * @see Tile#isTileOpen(String, char)
     * @see Board#getEncoding(int, int)
     * @see Relics#getRelicLocation(int)
     * @see Board#cardinalString(String)
     * @see Board#endGame()
     */
    public boolean movingPhase(String currentPlayer, int color)
    {   
        boolean loop = true;
        boolean currentPath = false;
        boolean nextPath = false;
        boolean relicCollected = false;
        String path = "";
        int x = 0;
        int y = 0;
        int[] playerPos = new int[2];

        StdOut.println("["+currentPlayer+"] Move your adventurer:");
        StdOut.print("> ");
        String input = StdIn.readString();

        switch (input)
        {
            case "quit":
            {
                StdOut.println("Game has been quit.");
                scoreboard();
                System.exit(0);
            }

            case "done":
            {
                StdOut.println("End of "+currentPlayer+"'s turn.");
                scoreboard();
                terminalBoard();
                loop = false;
                break;
            }

            case "n":
            {
                currentPath = Tile.isTileOpen(getEncoding(colors[color][2], colors[color][3]), input.charAt(0));
                x = (colors[color][2])-1;
                y = colors[color][3];
                path = getEncoding(x, y);
                nextPath = Tile.isTileOpen(path, 's');
                break;
            }

            case "s":
            {
                currentPath = Tile.isTileOpen(getEncoding(colors[color][2], colors[color][3]), input.charAt(0));
                x = (colors[color][2])+1;
                y = colors[color][3];
                path = getEncoding(x, y);
                nextPath = Tile.isTileOpen(path, 'n');
                break;
            }

            case "e":
            {
                currentPath = Tile.isTileOpen(getEncoding(colors[color][2], colors[color][3]), input.charAt(0));
                x = colors[color][2];
                y = (colors[color][3])+1;
                path = getEncoding(x, y);
                nextPath = Tile.isTileOpen(path, 'w');
                break;
            }

            case "w":
            {
                currentPath = Tile.isTileOpen(getEncoding(colors[color][2], colors[color][3]), input.charAt(0));
                x = colors[color][2];
                y = (colors[color][3])-1;
                path = getEncoding(x, y);
                nextPath = Tile.isTileOpen(path, 'e');
                break;
            }

            default:
            {
                if (input.length()==3 && input.charAt(1)==',')
                {
                    currentPath = pathFinding(color, input);
                    if(currentPath)
                        nextPath = true;
                    
                }
                else
                {
                    StdOut.println("Invalid input.");
                    break;
                }
            }
        }

        if (currentPath && nextPath)
        {
            switch (input)
            {
                case "n":
                {
                    colors[color][1] = colors[color][1] -4;
                    colors[color][2] = colors[color][2] -1;

                    if(k != 0)
                    {
                        playerPos = rObj.getRelicLocation(color);
                        if (colors[color][3] == playerPos[0] && colors[color][2] == playerPos[1])
                        {
                            colors[color][4] += 1;
                            loop = false;
                            relicCollected = true;
                        }
                    }

                    StdOut.println("Moving "+cardinalString(input)+".");
                    break;                    
                }

                case "s":
                {
                    colors[color][1] = colors[color][1] +4;
                    colors[color][2] = colors[color][2] +1;
                    
                    if(k != 0)
                    {
                        playerPos = rObj.getRelicLocation(color);
                        if (colors[color][3] == playerPos[0] && colors[color][2] == playerPos[1])
                        {
                            colors[color][4] += 1;
                            loop = false;
                            relicCollected = true;
                        }
                    }

                    StdOut.println("Moving "+cardinalString(input)+".");
                    break;
                }

                case "e":
                {
                    colors[color][0] = colors[color][0] +8;
                    colors[color][3] = colors[color][3] +1;
                    
                    if(k != 0)
                    {
                        playerPos = rObj.getRelicLocation(color);
                        if (colors[color][3] == playerPos[0] && colors[color][2] == playerPos[1])
                        {
                            colors[color][4] += 1;
                            loop = false;
                            relicCollected = true;
                        }
                    }

                    StdOut.println("Moving "+cardinalString(input)+".");
                    break;
                }

                case "w":
                {
                    colors[color][0] = colors[color][0] -8;
                    colors[color][3] = colors[color][3] -1;
                    
                    if(k != 0)
                    {
                        playerPos = rObj.getRelicLocation(color);
                        if (colors[color][3] == playerPos[0] && colors[color][2] == playerPos[1])
                        {
                            colors[color][4] += 1;
                            loop = false;
                            relicCollected = true;
                        }
                    }

                    StdOut.println("Moving "+cardinalString(input)+".");
                    break;
                }

                default:
                {
                    if(k != 0)
                    {
                        playerPos = rObj.getRelicLocation(color);
                        if (colors[color][3] == playerPos[0] && colors[color][2] == playerPos[1])
                        {
                            colors[color][4] += 1;
                            loop = false;
                            relicCollected = true;
                        }
                    }

                    StdOut.println("Moving to "+input+".");
                }
            }
            if(relicCollected)
            {
                    //StdOut.println("Moving "+cardinalString(input)+".");
                    terminalBoard();
                    StdOut.println(currentPlayer+ " has collected a relic.");
                    if (colors[color][4] == k)
                    {
                    StdOut.println(currentPlayer+ " has all their relics.");
                    }
                    StdOut.println("End of "+currentPlayer+"'s turn.");
                    scoreboard();
                    terminalBoard();
            }
            else
            {
                terminalBoard();
            }
            
        }
        else
        {
            if ((x < 0 || x > col-1 || y < 0 || y > row-1)&&(currentPath))
            {
                StdOut.println("Cannot move "+cardinalString(input)+": off the board.");
            }
            else
            {
                if (!input.equals("done")&& !input.equals("quit"))
                {
                    if(input.length()!=3)
                        StdOut.println("Cannot move "+cardinalString(input)+": no path.");
                    else
                        StdOut.println("Cannot move to "+input+": no path.");
                }    
            }
        }

        if(k != 0)
            endGame();
        return loop;
    }

    private void slidePlayer(String input, int color, int row, int col)
    {

        char cardinal = input.charAt(0);
        int number = (Character.getNumericValue(input.charAt(1)))-1;

        switch (cardinal)
        {
            case 'n':
            {
                if ((colors[color][3] == number) && (colors[color][2] == col-1))
                {
                    colors[color][2] = 0;
                    for(int i = col-1; i > 0; i--)
                    {
                        colors[color][1] = colors[color][1] - 4;
                    }
                }
                else
                {
                    if (colors[color][3] == number)
                    {
                        colors[color][2] = colors[color][2] + 1;
                        colors[color][1] = colors[color][1] + 4;
                    }
                }
                break;
            }

            case 's':
            {
                if ((colors[color][3] == number) && (colors[color][2] == 0))
                {
                    colors[color][2] = col-1;
                    for(int i = 0; i < col-1; i++)
                    {
                        colors[color][1] = colors[color][1] + 4;
                    }
                }
                else
                {
                    if (colors[color][3] == number)
                    {
                        colors[color][2] = colors[color][2] - 1;
                        colors[color][1] = colors[color][1] - 4;
                    }
                }
                break;
            }

            case 'e':
            {
                if ((colors[color][2] == number) && (colors[color][3] == 0))
                {
                    colors[color][3] = row-1;
                    for(int i = 0; i < row-1; i++)
                    {
                        colors[color][0] = colors[color][0] + 8;
                    }
                }
                else
                {
                    if (colors[color][2] == number)
                    {
                        colors[color][3] = colors[color][3] - 1;
                        colors[color][0] = colors[color][0] - 8;
                    }
                }
                break;
            }

            case 'w':
            {
                if ((colors[color][2] == number) && (colors[color][3] == row - 1))
                {
                    colors[color][3] = 0;
                    for(int i = row-1; i > 0; i--)
                    {
                        colors[color][0] = colors[color][0] - 8;
                    }
                }
                else
                {
                    if (colors[color][2] == number)
                    {
                        colors[color][3] = colors[color][3] + 1;
                        colors[color][0] = colors[color][0] + 8;
                    }
                }
                break;
            }
        }
    }

    private void endGame()
    {
        //check if a player is in their starting position and has all their relics
        if (colors[0][3] == 0 && colors[0][2] == 0 && colors[0][4] == k)
        {
            StdOut.println("Green has won.");
            scoreboard();
            System.exit(0);
        }
        if (colors[1][3] == (row-1) && colors[1][2] == 0 && colors[1][4] == k)
        {
            StdOut.println("Yellow has won.");
            scoreboard();
            System.exit(0);
        }
        if (colors[2][3] == 0 && colors[2][2] == (col-1) && colors[2][4] == k)
        {
            StdOut.println("Red has won.");
            scoreboard();
            System.exit(0);
        }
        if (colors[3][3] == (row-1) && colors[3][2] == (col-1) && colors[3][4] == k)
        {
            StdOut.println("Blue has won.");
            scoreboard();
            System.exit(0);
        }

    }

    private boolean pathFinding(int player, String input)
    {
        boolean path = false;

        int xP = colors[player][2];
        int yP = colors[player][3];


        int yF = Character.getNumericValue(input.charAt(0))-1;
        int xF = Character.getNumericValue(input.charAt(2))-1;

        boolean[][] visited = new boolean[row][col];
        //initialise to false except the current position of the player
        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < col; j++)
            {
                visited[i][j] = false;
            }
        }
        visited[yP][xP] = true;

        // //print xP, yP, xF, yF
        // StdOut.println("xP: "+xP+" yP: "+yP+" xF: "+xF+" yF: "+yF);
        // //print row and col
        // StdOut.println("row: "+row+" col: "+col);
        path = movePath(xP, yP, xF, yF, visited);
       
            

        if(path)
        {
            if (player == 0)
                {
                    colors[player][2] = xF;
                    colors[player][3] = yF;
                    colors[player][0] = 8*yF +3;
                    colors[player][1] = 4*xF + 2;
                }
            
            if (player == 1)
                {
                    colors[player][2] = xF;
                    colors[player][3] = yF;
                    colors[player][0] = 8*yF +7;
                    colors[player][1] = 4*xF + 2;
                }

            if (player == 2)
                {
                    colors[player][2] = xF;
                    colors[player][3] = yF;
                    colors[player][0] = 8*yF +3;
                    colors[player][1] = 4*xF + 4;
                }

            if (player == 3)
                {
                    colors[player][2] = xF;
                    colors[player][3] = yF;
                    colors[player][0] = 8*yF +7;
                    colors[player][1] = 4*xF + 4;
                }
        }

        return path;
    }
    
    private boolean movePath(int xP, int yP, int xF, int yF, boolean[][] visited)
    {
        //create a queue
        Queue<Integer[]> queue = new LinkedList<Integer[]>();
        
        //create a new array to store the current position
        Integer[] current = new Integer[2];
        current[0] = xP;
        current[1] = yP;

        //add the current position to the queue
        queue.add(current);
        
        //while the queue is not empty
        while(!queue.isEmpty())
        {
            //remove the first element from the queue
            Integer[] currentPos = queue.remove();
            int x = currentPos[0];
            int y = currentPos[1];
            
            //check if the current position is the final position
            if(x == xF && y == yF)
            {
                return true;
            }
            
            //check if the current position is not the final position
            else
            {
                //check if the current position is not the final position
                if(x < col-1 && !visited[y][x+1])
                {
                    if(Tile.isTileOpen(tileEncodings[y][x], 's') && Tile.isTileOpen(tileEncodings[y][x+1], 'n'))
                    {
                        //create a new array to store the current position
                        Integer[] next = new Integer[2];
                        next[0] = x+1;
                        next[1] = y;
                        //add the current position to the queue
                        queue.add(next);
                        visited[y][x+1] = true;
                    }
                }
                if(x > 0 && !visited[y][x-1])
                {
                    if(Tile.isTileOpen(tileEncodings[y][x], 'n') && Tile.isTileOpen(tileEncodings[y][x-1], 's'))
                    {    //create a new array to store the current position
                        Integer[] next = new Integer[2];
                        next[0] = x-1;
                        next[1] = y;
                        //add the current position to the queue
                        queue.add(next);
                        visited[y][x-1] = true;
                    }
                }
                if(y < row-1 && !visited[y+1][x])
                {
                    if(Tile.isTileOpen(tileEncodings[y][x], 'e') && Tile.isTileOpen(tileEncodings[y+1][x], 'w'))
                    {
                        //create a new array to store the current position
                        Integer[] next = new Integer[2];
                        next[0] = x;
                        next[1] = y+1;
                        //add the current position to the queue
                        queue.add(next);
                        visited[y+1][x] = true;
                    }
                }
                if(y > 0 && !visited[y-1][x])
                {
                    if(Tile.isTileOpen(tileEncodings[y][x], 'w') && Tile.isTileOpen(tileEncodings[y-1][x], 'e'))
                    {
                        //create a new array to store the current position
                        Integer[] next = new Integer[2];
                        next[0] = x;
                        next[1] = y-1;
                        //add the current position to the queue
                        queue.add(next);
                        visited[y-1][x] = true;
                    }
                }
            }
        }

        return false;

        
    }

    /**
     * A Graphical User Inteface of the game.
     * @param row the number of rows of the board
     * @param col the number of columns of the board
     * @param player the starting player color
     */
    public void gui(int row, int col, String player)
    {    
        encoding = tileEncodings;
        JFrame f = new JFrame("Moving Maze");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1280, 920);
        f.setResizable(false);
        back = new JPanel();
        //set background to #A60A3D
        back.setBackground(new Color(166, 10, 61));
        back.setBounds(0, 0, 1280, 920);
        back.setLayout(null);
        //add listener to the back panel for middle mouse click
        back.addMouseListener(new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent e) 
            {
                if (SwingUtilities.isMiddleMouseButton(e)) 
                {
                    setPlayer(gameState[1]++);
                    setPhase(gameState[0]);
                    back.repaint();
                }
            }
        });
        //create a "Moving Maze" label with raleway in #b79962
        JLabel title = new JLabel("Moving Maze");
        //center the label
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Raleway", Font.BOLD, 45));
        title.setForeground(new Color(183, 153, 98));
        title.setBounds(900, 0, 400, 100);
        back.add(title);


        //create a "Scoreboard" label with raleway in #b79962
        JLabel scoreboard = new JLabel("Score:");
        scoreboard.setFont(new Font("Raleway", Font.BOLD, 40));
        scoreboard.setForeground(new Color(183, 153, 98));
        scoreboard.setBounds(1035, 120, 400, 100);
        back.add(scoreboard);

        score = new JPanel();
        score = updateScore(back);

        

        back.add(createFloating());
        back.add(leftFloating(1));
        back.add(rightFloating(1));
        back.add(topFloating(1));
        back.add(bottomFloating(1));

        
        p.setBounds(120, 60, 760, 760);
        p.setLayout(new GridLayout(col, row));
        
        createBoard(p, encoding);
        //make first 2 buttons and last two buttons #D22730
        back.add(p);
        createRelic(encoding);
        updateFloating();
        updatePlayers();
        p.repaint();
        back.repaint();
        //create a panel at the top right for the floating tile
        

        back.add(floatingPanel);
        playerDis = playerPanel("Green");
        back.add(playerDis);
       

        f.add(back);
        f.setVisible(true);
    }
    
    private JPanel leftFloating(int state)
    {
        JPanel panel1 = new JPanel();
        panel1.setBounds(60, 60, 60, 760);
        panel1.setLayout(new GridLayout(col, 1));
        //make panel1 transparent
        panel1.setOpaque(false);
        JButton[] buttons1 = new JButton[col];
        if (state==1)
       {
            for (int i = 0; i < col; i++)
            {
                buttons1[i]= new JButton();
                buttons1[i].setContentAreaFilled(false);
                buttons1[i].setBorderPainted(false);
                buttons1[i].setLayout(null);
                panel1.add(buttons1[i]);
                ImageIcon insertIcon = new ImageIcon("png/insert.png");
                //create an image
                Image insertImage = insertIcon.getImage();
                //create a scaled image
                Image insertScaledImage = insertImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                //create a new image icon
                ImageIcon insertScaledIcon = new ImageIcon(insertScaledImage);
                //set the image icon to the button
                buttons1[i].setIcon(insertScaledIcon);

                if(i==0 || i%2==0)
                buttons1[i].setVisible(false);
                else
                {
                    buttons1[i].setActionCommand("w"+(i+1));
                    buttons1[i].addActionListener(this);
                }
            }

            return panel1;
       }
       else
       {
            return panel1;
       }
    }
    
    private JPanel rightFloating(int state)
    {

        JPanel panel2 = new JPanel();
        panel2.setBounds(880, 60, 60, 760);
        panel2.setLayout(new GridLayout(col, 1));
        panel2.setOpaque(false);
        JButton[] buttons2 = new JButton[col];
        if (state == 1)
        {
            for (int i = 0; i < col; i++)
            {
                buttons2[i]= new JButton();
                buttons2[i].setContentAreaFilled(false);
                buttons2[i].setBorderPainted(false);
                buttons2[i].setLayout(null);
                panel2.add(buttons2[i]);
                ImageIcon insertIcon = new ImageIcon("png/insert.png");
                //create an image
                Image insertImage = insertIcon.getImage();
                //create a scaled image
                Image insertScaledImage = insertImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                //create a new image icon
                ImageIcon insertScaledIcon = new ImageIcon(insertScaledImage);
                //set the image icon to the button
                buttons2[i].setIcon(insertScaledIcon);

                if(i==0 || i%2==0)
                buttons2[i].setVisible(false);
                else
                {
                    buttons2[i].setActionCommand("e"+(i+1));
                    buttons2[i].addActionListener(this);
                }
            }
            return panel2;
        }
        else
        {   
            
            return panel2;
        }
    }

    private JPanel topFloating(int state)
    {
        JPanel panel3 = new JPanel();
        panel3.setBounds(120, 0, 760, 60);
        panel3.setLayout(new GridLayout(1, row));
        panel3.setOpaque(false);
        JButton[] buttons3 = new JButton[row];
        if(state==1)
        {
            
            for (int i = 0; i < row; i++)
            {
                buttons3[i]= new JButton();
                buttons3[i].setContentAreaFilled(false);
                buttons3[i].setBorderPainted(false);
                buttons3[i].setLayout(null);
                panel3.add(buttons3[i]);
                ImageIcon insertIcon = new ImageIcon("png/insert.png");
                //create an image
                Image insertImage = insertIcon.getImage();
                //create a scaled image
                Image insertScaledImage = insertImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                //create a new image icon
                ImageIcon insertScaledIcon = new ImageIcon(insertScaledImage);
                //set the image icon to the button
                buttons3[i].setIcon(insertScaledIcon);

                if(i==0 || i%2==0)
                buttons3[i].setVisible(false);
                else
                {
                    buttons3[i].setActionCommand("n"+(i+1));
                    buttons3[i].addActionListener(this);
                }
            }
            return panel3;
        }
        else
        {
            
            return panel3;
        }
    }

    private JPanel bottomFloating(int state)
    {
        JPanel panel4 = new JPanel();
        panel4.setBounds(120, 820, 760, 60);
        panel4.setLayout(new GridLayout(1, row));
        panel4.setOpaque(false);
        JButton[] buttons4 = new JButton[row];
        if(state==1)
        {
            
            for (int i = 0; i < row; i++)
            {
                buttons4[i]= new JButton();
                buttons4[i].setContentAreaFilled(false);
                buttons4[i].setBorderPainted(false);
                buttons4[i].setLayout(null);
                panel4.add(buttons4[i]);
                ImageIcon insertIcon = new ImageIcon("png/insert.png");
                //create an image
                Image insertImage = insertIcon.getImage();
                //create a scaled image
                Image insertScaledImage = insertImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                //create a new image icon
                ImageIcon insertScaledIcon = new ImageIcon(insertScaledImage);
                //set the image icon to the button
                buttons4[i].setIcon(insertScaledIcon);

                if(i==0 || i%2==0)
                buttons4[i].setVisible(false);
                else
                {
                    buttons4[i].setActionCommand("s"+(i+1));
                    buttons4[i].addActionListener(this);
                }
            }
            return panel4;
        }
        else
        {   
            
            return panel4;
        }
    }

    private JPanel playerPanel(String player)
    {
         //create a panel below the floating tile
         JPanel belowFloating = new JPanel();
         belowFloating.setBounds(950, 650, 300, 150);
         //set background to transparent
         belowFloating.setOpaque(false);
         //set layout to grid layout
         belowFloating.setLayout(new GridLayout(2, 1));
         //create a label for the current active player
         JLabel activePlayer = new JLabel("Active Player: "+ player);
        //set the font
        activePlayer.setFont(new Font("Raleway", Font.BOLD, 25));
        //set the foreground color to #b99762
        activePlayer.setForeground(new Color(0xb99762));
        //set the alignment
        activePlayer.setHorizontalAlignment(JLabel.CENTER);
        //add the label to the panel
        belowFloating.add(activePlayer);

        return belowFloating;
    }

    /**
     *  This action listener is used to handle the events of the buttons
     * @param e the event
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        String input = e.getActionCommand();
        if(gameState[0]==0 && Tile.slideTest(input))
        {
            back.add(createFloating());
            back.add(leftFloating(1));
            back.add(rightFloating(1));
            back.add(topFloating(1));
            back.add(bottomFloating(1));

            String newInput = cardinalOpposite(input.charAt(0))+input.charAt(1)+"";
            if (Objects.equals(oldInput, newInput))
            {
                StdOut.println("Cannot slide into last exit point.");
            }
            else
            {
                String temp = Tile.slideTile(input, getRow(), getCol(), encoding, getFloatingTile());
                slidePlayer(input, gameState[1], row, col);
                setFloatingTile(temp);
                updateBoard(encoding);
                updateFloating();
                updatePlayers();
                p.repaint();
                floatingTileB.setIcon(scaling(new ImageIcon("png/"+getFloatingTile().substring(0, 4)+".png"), 90, 90));        
                floatingPanel.repaint();
                setOldInput(input);
                setPhase(gameState[0]);
            }
        }
        else if(gameState[0]==1 && input.length()==3)
        {
            back.remove(createFloating());
            back.remove(leftFloating(1));
            back.remove(rightFloating(1));
            back.remove(topFloating(1));
            back.remove(bottomFloating(1));

            boolean relicCollected = false;

            if(pathFinding(gameState[1], input))
            {   
                if(k != 0)
                    {
                        int[] playerPos = rObj.getRelicLocation(gameState[1]);
                        if (colors[gameState[1]][3] == playerPos[0] && colors[gameState[1]][2] == playerPos[1])
                        {
                            colors[gameState[1]][4] += 1;
                            relicCollected = true;
                        }
                    }
                if(relicCollected)
                {
                    endGame();
                }
                updateBoard(encoding);
                updateFloating();
                updatePlayers();
                back.remove(score);
                score = updateScore(back);
                score.repaint();
                back.remove(playerDis);
                playerDis = playerPanel(setPlayer(gameState[1]));
                back.add(playerDis);
                p.repaint();
                back.repaint();
                setPhase(gameState[0]);

            }
        }
        
    }

    private String setPlayer(int current)
    {
        if(current == 0)
        {
            gameState[1] = 1;
            return "Yellow";
        }
        else if(current == 1)
        {
            gameState[1] = 2;
            return "Red";
        }
        else if(current == 2)
        {
            gameState[1] = 3;
            return "Blue";
        }
        else if(current == 3)
        {
            gameState[1] = 0;
            return "Green";
        }

        return "Green";
    }

    private void setPhase(int current)
    {
        if (current==0)
            gameState[0]=1;
        else if (current==1)
            gameState[0]=0;
    }
    
    private ImageIcon scaling(ImageIcon base, int x, int y)
    {
        //create an image
        Image insertImage = base.getImage();
        //create a scaled image
        Image insertScaledImage = insertImage.getScaledInstance(x, y, Image.SCALE_SMOOTH);
        //create a new image icon
        return new ImageIcon(insertScaledImage);
    }

    private JPanel updateScore(JPanel back)
    {
        //create a "Green: " label with raleway in green
        JLabel green = new JLabel("Green: "+getColorVal(0, 4));
        green.setFont(new Font("Raleway", Font.BOLD, 30));
        green.setForeground(new Color(0, 255, 0));
        green.setBounds(1035, 190, 400, 100);
        back.add(green);

        //create a "Yellow: " label with raleway in yellow
        JLabel yellow = new JLabel("Yellow: "+getColorVal(1, 4));
        yellow.setFont(new Font("Raleway", Font.BOLD, 30));
        yellow.setForeground(new Color(255, 255, 0));
        yellow.setBounds(1035, 240, 400, 100);
        back.add(yellow);

        //create a "Red: " label with raleway in red
        JLabel red = new JLabel("Red: "+getColorVal(2, 4));
        red.setFont(new Font("Raleway", Font.BOLD, 30));
        red.setForeground(new Color(255, 0, 0));
        red.setBounds(1035, 290, 400, 100);
        back.add(red);

        //create a "Blue: " label with raleway in blue
        JLabel blue = new JLabel("Blue: "+getColorVal(3, 4));
        blue.setFont(new Font("Raleway", Font.BOLD, 30));
        blue.setForeground(new Color(0, 0, 255));
        blue.setBounds(1035, 340, 400, 100);
        back.add(blue);

        return back;
    }

    private void createBoard(JPanel p, String[][] encodings)
    {
        
        ImageIcon insertIcon = new ImageIcon("png/insert.png");
        b = new JButton[row][col];
        //make buttons on every even position
        for(int j = 0; j < col; j++)
        {
            for(int i = 0; i < row; i++)
            {
                
                String input = encodings[i][j];
                input = input.substring(0, 4);
                
                //switch statement for tileEncodings
                switch (input)
                {
                    case "1100":
                    {
                        insertIcon = new ImageIcon("png/1100.png");
                        break;
                    }
                    case "0011":
                    {
                        insertIcon = new ImageIcon("png/0011.png");
                        break;
                    }
                    case "1110":
                    {
                        insertIcon = new ImageIcon("png/1110.png");
                        break;
                    }
                    case "0111":
                    {
                        insertIcon = new ImageIcon("png/0111.png");
                        break;
                    }
                    case "1010":
                    {
                        insertIcon = new ImageIcon("png/1010.png");
                        break;
                    }
                    case "0101":
                    {
                        insertIcon = new ImageIcon("png/0101.png");
                        break;
                    }
                    case "1111":
                    {
                        insertIcon = new ImageIcon("png/1111.png");
                        break;
                    }
                    case "1101":
                    {
                        insertIcon = new ImageIcon("png/1101.png");
                        break;
                    }
                    case "0110":
                    {
                        insertIcon = new ImageIcon("png/0110.png");
                        break;
                    }
                    case "1011":
                    {
                        insertIcon = new ImageIcon("png/1011.png");
                        break;
                    }
                    case "1001":
                    {
                        insertIcon = new ImageIcon("png/1001.png");
                        break;
                    }
                    default:
                    {
                        insertIcon = new ImageIcon("png/insert.png");
                        break;
                    }

                }
                
                //create an image
                Image insertImage = insertIcon.getImage();
                //create a scaled image
                Image insertScaledImage = insertImage.getScaledInstance((250/Math.max(row, col)*col), (250/Math.max(row, col)*row), Image.SCALE_SMOOTH);
                //create a new image icon
                ImageIcon insertScaledIcon = new ImageIcon(insertScaledImage);
                //set the image icon to the button

                JLabel relic = new JLabel(scaling(new ImageIcon("png/center.png"), 10, 10));
                if(row==col)
                    relic.setBounds(250/col, 250/row, 50, 50);
                else if(row>col)
                    relic.setBounds(250/col, 250/row, 50, 50);
                else
                    relic.setBounds(500/col, 150/row, 50, 50);
                relic.setHorizontalAlignment(JLabel.CENTER);
                relic.setVerticalAlignment(JLabel.CENTER);

                JLabel player = new JLabel(scaling(new ImageIcon("png/center.png"), 10, 10));
                if(row==col)
                    player.setBounds(250/col, 250/row, 50, 50);
                else if(row>col)
                    player.setBounds(150/col, 500/row, 50, 50);
                else
                    player.setBounds(500/col, 150/row, 50, 50);

                b[i][j] = new JButton(insertScaledIcon);
                b[i][j].setLayout(null);
                b[i][j].setContentAreaFilled(false);
                b[i][j].setActionCommand((i+1)+","+(j+1));
                b[i][j].addActionListener(this);
                b[i][j].add(relic);
                b[i][j].add(player);
                p.add(b[i][j]);
            }
        }
    }

    private void createRelic(String[][] encodings)
    {
        for(int i=0;i<row;i++)
        {
            for(int j=0;j<col;j++)
            {
                String relicCheck = encodings[i][j].substring(4, 6);
                // create a loop to check tileEncodings for relics and current relic number of the player and add the appropriate relic to the center of the button
                if(!relicCheck.equals("xx"))
                {
                    switch (relicCheck.charAt(0))
                    {
                        case 'g':
                        {
                            if((Character.getNumericValue(relicCheck.charAt(1))-1) == getColorVal(0, 4))
                            {
                                ((JLabel) b[i][j].getComponent(0)).setIcon(scaling(new ImageIcon("png/green.png"), 250, 250));
                                int[] location = {i,j};
                                rObj.setRelicLocation('g', location);
                            }
                            break;
                        }
                        case 'y':
                        {
                            if((Character.getNumericValue(relicCheck.charAt(1))-1) == getColorVal(1, 4))
                            {
                                ((JLabel) b[i][j].getComponent(0)).setIcon(scaling(new ImageIcon("png/yellow.png"), 250, 250));
                                int[] location = {i,j};
                                rObj.setRelicLocation('y', location);
                            }
                            break;
                        }
                        case 'r':
                        {
                            if((Character.getNumericValue(relicCheck.charAt(1))-1) == getColorVal(2, 4))
                            {
                                ((JLabel) b[i][j].getComponent(0)).setIcon(scaling(new ImageIcon("png/red.png"), 250, 250));
                                int[] location = {i,j};
                                rObj.setRelicLocation('r', location);
                            }
                            break;
                        }
                        case 'b':
                        {
                            if((Character.getNumericValue(relicCheck.charAt(1))-1) == getColorVal(3, 4))
                            {
                                ((JLabel) b[i][j].getComponent(0)).setIcon(scaling(new ImageIcon("png/blue.png"), 250, 250));
                                int[] location = {i,j};
                                rObj.setRelicLocation('b', location);
                            }
                            break;
                        }
                        default:
                        {
                            ((JLabel) b[i][j].getComponent(0)).setIcon(scaling(new ImageIcon("png/center.png"), 250, 250));
                            break;
                        }
                    }
                }
                else
                {
                    ((JLabel) b[i][j].getComponent(0)).setIcon(scaling(new ImageIcon("png/center.png"), 250, 250));
                }
            }
        }
        // 
    }

    private void updateBoard(String[][] encodings)
    {
     
        ImageIcon insertIcon = new ImageIcon("png/insert.png");

        for(int j = 0; j < col; j++)
        {
            for(int i = 0; i < row; i++)
            {
                
                String input = encodings[i][j];
                input = input.substring(0, 4);
                
                //switch statement for tileEncodings
                switch (input)
                {
                    case "1100":
                    {
                        insertIcon = new ImageIcon("png/1100.png");
                        break;
                    }
                    case "0011":
                    {
                        insertIcon = new ImageIcon("png/0011.png");
                        break;
                    }
                    case "1110":
                    {
                        insertIcon = new ImageIcon("png/1110.png");
                        break;
                    }
                    case "0111":
                    {
                        insertIcon = new ImageIcon("png/0111.png");
                        break;
                    }
                    case "1010":
                    {
                        insertIcon = new ImageIcon("png/1010.png");
                        break;
                    }
                    case "0101":
                    {
                        insertIcon = new ImageIcon("png/0101.png");
                        break;
                    }
                    case "1111":
                    {
                        insertIcon = new ImageIcon("png/1111.png");
                        break;
                    }
                    case "1101":
                    {
                        insertIcon = new ImageIcon("png/1101.png");
                        break;
                    }
                    case "0110":
                    {
                        insertIcon = new ImageIcon("png/0110.png");
                        break;
                    }
                    case "1011":
                    {
                        insertIcon = new ImageIcon("png/1011.png");
                        break;
                    }
                    case "1001":
                    {
                        insertIcon = new ImageIcon("png/1001.png");
                        break;
                    }
                    default:
                    {
                        insertIcon = new ImageIcon("png/insert.png");
                        break;
                    }

                }
                
                //create an image
                Image insertImage = insertIcon.getImage();
                //create a scaled image
                Image insertScaledImage = insertImage.getScaledInstance((250/Math.max(row, col)*col), (250/Math.max(row, col)*row), Image.SCALE_SMOOTH);
                //create a new image icon
                ImageIcon insertScaledIcon = new ImageIcon(insertScaledImage);
                //set the image icon to the button

                b[i][j].setIcon(insertScaledIcon);
                JLabel relic = new JLabel(scaling(new ImageIcon("png/center.png"), 10, 10));
                if(row==col)
                    relic.setBounds(250/col, 250/row, 50, 50);
                else if(row>col)
                    relic.setBounds(250/col, 250/row, 50, 50);
                else
                    relic.setBounds(500/col, 150/row, 50, 50);
                relic.setHorizontalAlignment(JLabel.CENTER);
                relic.setVerticalAlignment(JLabel.CENTER);
                createRelic(encodings);
    
            }
        }
    }

    private void updatePlayers()
    {
        for(int i=0;i<row;i++)
        {
            for(int j=0;j<col;j++)
            {
                for(int color=0; color<4; color++)
                {
                    if(getColorVal(color, 3)==i && getColorVal(color, 2)==j)
                    {
                        if(color==0)
                        {
                            ((JLabel) b[i][j].getComponent(1)).setIcon(scaling(new ImageIcon("png/gP.png"), 50, 50));
                        }
                        else if(color==1)
                        {
                            ((JLabel) b[i][j].getComponent(1)).setIcon(scaling(new ImageIcon("png/yP.png"), 50, 50));
                        }
                        else if(color==2)
                        {
                            ((JLabel) b[i][j].getComponent(1)).setIcon(scaling(new ImageIcon("png/rP.png"), 50, 50));
                        }
                        else if(color==3)
                        {
                            ((JLabel) b[i][j].getComponent(1)).setIcon(scaling(new ImageIcon("png/bP.png"), 50, 50));
                        }
                        else
                        {
                            ((JLabel) b[i][j].getComponent(1)).setIcon(scaling(new ImageIcon("png/center.png"), 50, 50));
                        }
                    }
                }
            }
        }
    }

    private JPanel createFloating()
    {
        floatingPanel = new JPanel();
        floatingPanel.setBounds(950, 500, 300, 150);
        //set background to transparent
        floatingPanel.setOpaque(false);
        //set layout to grid layout
        floatingPanel.setLayout(new GridLayout(1, 3));

        floatingTileB = new JButton(scaling(new ImageIcon("png/"+floatingTile.substring(0, 4)+".png"), 90, 90));
        JLabel relic = new JLabel(scaling(new ImageIcon("png/center.png"), 10, 10));
        floatingTileB.add(relic);
        updateFloating();
        //set the button to be transparent
        floatingTileB.setBorderPainted(false);
        floatingTileB.setLayout(null);
        floatingTileB.setContentAreaFilled(false);

        JButton rotateLeft = new JButton();
        rotateLeft.setContentAreaFilled(false);
        rotateLeft.setBorderPainted(false);
        rotateLeft.setLayout(null);
        rotateLeft.setActionCommand("ac");
        floatingPanel.add(rotateLeft);
        rotateLeft.setIcon(scaling(new ImageIcon("png/rotateLeft.png"), 50, 50));
        //add action listener to rotate left
        rotateLeft.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                //rotate the floating tile
                setFloatingTile(Tile.rotateAntiClockwise(floatingTile));
                //set the image icon to the button
                floatingTileB.setIcon(scaling(new ImageIcon("png/"+getFloatingTile().substring(0, 4)+".png"), 90, 90));
                //repaint the panel
                
                floatingPanel.repaint();
            }
        });

        floatingPanel.add(floatingTileB);
        JButton rotateRight = new JButton();
        rotateRight.setContentAreaFilled(false);
        rotateRight.setLayout(null);
        rotateRight.setBorderPainted(false);
        //set action command
        rotateRight.setActionCommand("c");
        floatingPanel.add(rotateRight);
        //set the image icon to the button
        rotateRight.setIcon(scaling(new ImageIcon("png/rotateRight.png"), 50, 50));
        //add actionlistener for rotate right
        rotateRight.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //rotate the floating tile
                setFloatingTile(Tile.rotateClockwise(floatingTile));
                //set the image icon to the button
                floatingTileB.setIcon(scaling(new ImageIcon("png/"+getFloatingTile().substring(0, 4)+".png"), 90, 90));
                //repaint the panel
                
                floatingPanel.repaint();
            }
        });
        
        return floatingPanel;
    }

    private void updateFloating()
    {
        String relicCheck = floatingTile.substring(4, 6);
                // create a loop to check tileEncodings for relics and current relic number of the player and add the appropriate relic to the center of the button
        if(!relicCheck.equals("xx"))
        {
            switch (relicCheck.charAt(0))
            {
                case 'g':
                {
                    if((Character.getNumericValue(relicCheck.charAt(1))-1) == getColorVal(0, 4))
                    {
                        ((JLabel) floatingTileB.getComponent(0)).setIcon(scaling(new ImageIcon("png/green.png"), 25, 25));
                    }
                    break;
                }
                case 'y':
                {
                    if((Character.getNumericValue(relicCheck.charAt(1))-1) == getColorVal(1, 4))
                    {
                        ((JLabel) floatingTileB.getComponent(0)).setIcon(scaling(new ImageIcon("png/yellow.png"), 25, 25));
                    }
                    break;
                }
                case 'r':
                {
                    if((Character.getNumericValue(relicCheck.charAt(1))-1) == getColorVal(2, 4))
                    {
                        ((JLabel) floatingTileB.getComponent(0)).setIcon(scaling(new ImageIcon("png/red.png"), 25, 25));
                    }
                    break;
                }
                case 'b':
                {
                    if((Character.getNumericValue(relicCheck.charAt(1))-1) == getColorVal(3, 4))
                    {
                        ((JLabel) floatingTileB.getComponent(0)).setIcon(scaling(new ImageIcon("png/blue.png"), 25, 25));
                    }
                    break;
                }
                default:
                {
                    ((JLabel) floatingTileB.getComponent(0)).setIcon(scaling(new ImageIcon("png/center.png"), 10, 10));
                    break;
                }
            }
        }
        else
        {
            ((JLabel) floatingTileB.getComponent(0)).setIcon(scaling(new ImageIcon("png/center.png"), 10, 10));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
}
