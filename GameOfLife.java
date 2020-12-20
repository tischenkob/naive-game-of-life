import java.io.Console;
import java.io.IOException;

public class GameOfLife {
    class Coord2D {
        public int x;
        public int y;

        Coord2D(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    Console console = System.console();
    char[][] grid = { 
        { 'o', 'x', 'o', 'o', 'o' }, 
        { 'o', 'o', 'x', 'o', 'o' }, 
        { 'x', 'x', 'x', 'o', 'o' },
        { 'o', 'o', 'o', 'o', 'o' }, 
        { 'o', 'o', 'o', 'o', 'o' }, 
        { 'o', 'o', 'o', 'o', 'o' } 
    };

    Coord2D[] divs = { 
        new Coord2D(-1, -1), 
        new Coord2D(0, -1), 
        new Coord2D(1, -1), 
        new Coord2D(-1, 0),
            new Coord2D(1, 0), 
            new Coord2D(-1, 1), 
            new Coord2D(0, 1), 
            new Coord2D(1, 1) };

    public void game() throws IOException, InterruptedException {
        while (true) {
            clear();
            show(grid);
            simulate();
            Thread.sleep(750);
        }
    }

    public void simulate() {
        char[][] oldGrid = copyGrid(grid);
        for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < grid[rowIndex].length; columnIndex++) {
                char[] neighbours = getNeighbours(oldGrid, new Coord2D(columnIndex, rowIndex));
                char cell = oldGrid[rowIndex][columnIndex];
                grid[rowIndex][columnIndex] = processNeighbours(neighbours, cell);
            }
        }
    }

    public char[][] copyGrid(char[][] grid) {
        char[][] copy = new char[grid.length][grid[0].length];
        for (int rowIndex = 0; rowIndex < grid.length; rowIndex++)
            copy[rowIndex] = grid[rowIndex].clone();
        return copy;
    }

    public char processNeighbours(char[] cells, char cell) {
        long livingCounter = 0l;
        for (char c: cells) if (c == 'x') livingCounter++;
        
        if (cell == 'x') {
            if (livingCounter < 2 || livingCounter > 3)
                return 'o';
            return 'x';
        }
        if (livingCounter == 3)
            return 'x';
        return 'o';
    }

    public char[] getNeighbours(char[][] grid, Coord2D coordinate) {
        char[] neighbours = new char[8];
        int index = 0;
        for (Coord2D div: divs) {
            neighbours[index++] = getCell(grid, new Coord2D(coordinate.x + div.x, coordinate.y + div.y));
        }
        return neighbours;
    }

    public char getCell(char[][] grid, Coord2D c) {
        try {
            return grid[c.y][c.x];
        } catch (ArrayIndexOutOfBoundsException exception) {
            return 'o';
        }
    }

    public void show(char[][] grid) {
        for (char[] row : grid) {
            for (char cell : row) {
                console.printf("%s ", cell);
            }
            console.printf("\n");
        }
    }
    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String... args) {
        GameOfLife game = new GameOfLife();
        try {
            game.game();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}