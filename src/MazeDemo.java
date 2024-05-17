import javax.swing.*;

public class MazeDemo {
    public static void main(String[] args) {
        // Display a welcome message
        JOptionPane.showMessageDialog(null, "Hello, welcome to Daniel's maze!");

        // Prompt the user for the grid size
        String input = JOptionPane.showInputDialog(null, "Enter the size of the grid:", "Grid Size", JOptionPane.QUESTION_MESSAGE);

        // Use an array to store gridSize so it can be effectively final
        final int[] gridSize = new int[1];
        try {
            gridSize[0] = Integer.parseInt(input);
            if (gridSize[0] <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Using default size of 5.", "Error", JOptionPane.ERROR_MESSAGE);
            gridSize[0] = 5;
        }

        // Create and display the game panel
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maze Grid");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800); // Adjusted to be more flexible for larger grids

            GamePanel gamePanel = new GamePanel(gridSize[0]);

            // Create the menu bar
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("Options");

            JMenuItem resetItem = new JMenuItem("Reset");
            resetItem.addActionListener(e -> gamePanel.resetGrid());
            menu.add(resetItem);

            JMenuItem speedItem = new JMenuItem("Set Speed");
            speedItem.addActionListener(e -> {
                String speedInput = JOptionPane.showInputDialog(frame, "Enter the speed in milliseconds:", "Set Speed", JOptionPane.QUESTION_MESSAGE);
                try {
                    int speed = Integer.parseInt(speedInput);
                    if (speed < 0) {
                        throw new NumberFormatException();
                    }
                    gamePanel.setSpeed(speed);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Using default speed of 100ms.", "Error", JOptionPane.ERROR_MESSAGE);
                    gamePanel.setSpeed(100);
                }
            });
            menu.add(speedItem);

            menuBar.add(menu);
            frame.setJMenuBar(menuBar);

            frame.add(gamePanel);
            frame.setVisible(true);
        });
    }
}
