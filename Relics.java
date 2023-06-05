/**
 * This class is used to create a Relic object for their management.
 * @author Alok More
 */
public class Relics {
    
    private int k;
    private int[] greenLocation;
    private int[] blueLocation;
    private int[] redLocation;
    private int[] yellowLocation;
    
    /**
     * Sets the relic goal for the game.
     * @see Board#getK()
     */
    public Relics() {
        k = Board.getK();
    }

    /**
     * Returns the relic goal of the game.
     * @return An int value
     * @see Relics#setK(int)
     */
    public int getK() {
        return k;
    }

    /**
     * Set the value for the relic goal of the game.
     * @param goal The int value of the relic goal
     * @see Relics#getK()
     */
    public void setK(int goal) {
        k = goal;
    }

    /**
     * Checks to see if a tile contains a relic.
     * @param tileEncoding String value of the tile to check
     * @return {@code true} if it contains a relic, otherwise return {@code false}
     */
    public boolean tileHasRelic(String tileEncoding) {
        if (!(tileEncoding.charAt(4) == 'x')) {
            return true;
        }
        return false; // TODO
    }

    /**
     * Get the color of the relic on a tile after {@link Relics#tileHasRelic(String)} returns {@code true}.
     * @param tileEncoding String value of the tile to check
     * @return A char value indicating the color.
     */
    public char getRelicColor(String tileEncoding) {
        return tileEncoding.charAt(4);
    }

    /**
     * Get the number of the relic after {@link Relics#getRelicColor(String)} returns {@code true}.
     * @param tileEncoding String value of the tile to check
     * @return An int value of the relic number
     */
    public int getRelicNumber(String tileEncoding) {
        return Character.getNumericValue(tileEncoding.charAt(5));
    }

    /**
     * Set the tile coordinates of a relic depending on the relic's color.
     * @param color The int value indicating the player color
     * @param location a 1D int array of the tile coordintes containing the active relic
     * @see Relics#getRelicLocation(int)
     */
    public void setRelicLocation(char color, int[] location) {
        if (color == 'g') {
            greenLocation = location;
        } else if (color == 'b') {
            blueLocation = location;
        } else if (color == 'r') {
            redLocation = location;
        } else if (color == 'y') {
            yellowLocation = location;
        }
    }

    /**
     * Get the tile coordinates of a relic for a given players color.
     * @param color The int value indicating the player color
     * @return A 1D int array of the tile coordinates containing the active relic for that color
     * @see Relics#setRelicLocation(char, int[])
     */
    public int[] getRelicLocation(int color) {

        if (color == 0) {
            return greenLocation;
        } else if (color == 3) {
            return blueLocation;
        } else if (color == 2) {
            return redLocation;
        } else if (color == 1) {
            return yellowLocation;
        }
        
        return new int[0];
    }
}
