import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class GamePanel extends JPanel {

    private final int gridSize;
    private final Color[][] gridColors;
    private boolean startSet = false;
    private boolean endSet = false;
    private int speed = 100; // default speed
    private boolean isDragging = false;
    private boolean isErasing = false;

    public GamePanel(int gridSize) {
        this.gridSize = gridSize;
        gridColors = new Color[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridColors[i][j] = Color.WHITE;
            }
        }

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int cellWidth = getWidth() / gridSize;
                int cellHeight = getHeight() / gridSize;
                int row = e.getY() / cellHeight;
                int col = e.getX() / cellWidth;

                if (SwingUtilities.isLeftMouseButton(e)) {
                    isDragging = true;
                    gridColors[row][col] = Color.BLACK;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    isErasing = true;
                    gridColors[row][col] = Color.WHITE;
                } else if (SwingUtilities.isMiddleMouseButton(e) && gridColors[row][col] == Color.WHITE) {
                    if (!startSet) {
                        gridColors[row][col] = Color.GREEN;
                        startSet = true;
                    } else if (!endSet) {
                        gridColors[row][col] = Color.RED;
                        endSet = true;
                        solveMaze();
                    }
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
                isErasing = false;
            }
        };

        MouseMotionAdapter mouseMotionHandler = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int cellWidth = getWidth() / gridSize;
                int cellHeight = getHeight() / gridSize;
                int row = e.getY() / cellHeight;
                int col = e.getX() / cellWidth;

                if (isDragging) {
                    gridColors[row][col] = Color.BLACK;
                } else if (isErasing) {
                    gridColors[row][col] = Color.WHITE;
                }
                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseMotionHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellWidth = getWidth() / gridSize;
        int cellHeight = getHeight() / gridSize;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                g.setColor(gridColors[i][j]);
                g.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                g.setColor(Color.BLACK);
                g.drawRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
            }
        }
    }

    private void solveMaze() {
        Maze maze = new Maze(gridColors, speed, this);
        maze.solve();
    }

    public void resetGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridColors[i][j] = Color.WHITE;
            }
        }
        startSet = false;
        endSet = false;
        repaint();
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
