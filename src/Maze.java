import java.awt.*;
import javax.swing.SwingWorker;

public class Maze {

    private final Color[][] gridColors;
    private final int gridSize;
    private final Point start;
    private final Point end;
    private final Color START_COLOR = Color.GREEN;
    private final Color END_COLOR = Color.RED;
    private final Color SEARCH_COLOR = Color.BLUE;
    private final int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private boolean pathFound = false;
    private int speed = 100; // default speed
    private final GamePanel gamePanel;

    public Maze(Color[][] gridColors, int speed, GamePanel gamePanel) {
        this.gridColors = gridColors;
        this.gridSize = gridColors.length;
        this.start = findPoint(START_COLOR);
        this.end = findPoint(END_COLOR);
        this.speed = speed;
        this.gamePanel = gamePanel;
    }

    public void solve() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                boolean[][] visited = new boolean[gridSize][gridSize];
                dfs(start.x, start.y, visited);
                return null;
            }

            @Override
            protected void done() {
                gamePanel.repaint();
            }
        }.execute();
    }

    private void dfs(int x, int y, boolean[][] visited) {
        if (pathFound || !isValid(x, y) || visited[x][y] || gridColors[x][y].equals(Color.BLACK)) {
            return;
        }

        visited[x][y] = true;

        if (gridColors[x][y].equals(END_COLOR)) {
            pathFound = true;
            return;
        }

        gridColors[x][y] = SEARCH_COLOR;
        gamePanel.repaint(); // Repaint the panel to show changes
        try {
            Thread.sleep(speed); // Adjust the speed of visualization
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            dfs(newX, newY, visited);
        }

        // Only mark the cell as searched if the path is not found.
        if (!pathFound) {
            gridColors[x][y] = Color.WHITE;
            gamePanel.repaint();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Point findPoint(Color color) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (gridColors[i][j].equals(color)) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < gridSize && y >= 0 && y < gridSize;
    }
}
