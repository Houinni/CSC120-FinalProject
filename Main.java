import javax.swing.SwingUtilities;

/**
 * The Main class serves as the entry point for the Christmas Tree Light-On
 * Challenge game.
 * It initializes the graphical user interface (GUI)
 */
public class Main {
    /**
     * The main method launches the game by creating an instance of the
     * LightPyramidGUI class.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LightPyramidGUI());
    }
}
