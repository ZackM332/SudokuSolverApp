import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class MyPanel extends JPanel {

    // dimentions of the window
    int width = 900;
    int height = 900;

    // x and y offset for grid
    int xOff = 50;
    int yOff = 80;

    // approximate rendered size of sudoku grid in pixels
    int boxSize = 800;

    // status text info
    int xText = 50;
    int yText = 60;
    int textSize = 50;
    String text = "";

    // time interval between screen frame updates (not counting calculation time)
    long pause = 50;

    // sudoku grid
    int sud[][] ={{0,0,0, 0,0,0, 0,0,0},
                  {0,0,0, 0,0,0, 0,0,0},
                  {0,0,0, 0,0,0, 0,0,0},

                  {0,0,0, 0,0,0, 0,0,0},
                  {0,0,0, 0,0,0, 0,0,0},
                  {0,0,0, 0,0,0, 0,0,0},

                  {0,0,0, 0,0,0, 0,0,0},
                  {0,0,0, 0,0,0, 0,0,0},
                  {0,0,0, 0,0,0, 0,0,0}};
    
    int[] changed = {-1, -1};

    // size of a sudoku grid
    //static int N = 9;

    MyPanel(){
        // dimentions of panel
        this.setPreferredSize(new Dimension(width, height));
    }

    MyPanel(int[][] g){
        // dimentions of panel
        this.setPreferredSize(new Dimension(width, height));
        setGrid(g);
    }

    // called on object creation and repaint()
    public void paint(Graphics g){

        Graphics2D g2D = (Graphics2D) g;

        // clear screen
        g2D.setPaint(Color.white);
        g2D.fillRect(0, 0, width, height);

        // dimentions of sudoku box

        int third = boxSize / 3;
        int sub = third / 3;

        // Since we are dividing ints and need everything to fit nicely,
        // we need to recalculate ints used for divisions
        third = sub * 3;
        boxSize = 3 * third;

        // set red box
        if (changed[0] != -1){
            g2D.setPaint(Color.red);
            g2D.fill3DRect(xOff + sub * changed[1], yOff+ sub * changed[0], 
                sub, sub, 
                getFocusTraversalKeysEnabled());
        }

        g2D.setPaint(Color.black);

        // bold strokes
        g2D.setStroke(new BasicStroke(5));

        // Horizontal lines, increasing Y each increment
        g2D.drawLine(xOff, yOff, xOff+boxSize, yOff);
        g2D.drawLine(xOff, yOff + third, xOff+boxSize, yOff + third);
        g2D.drawLine(xOff, yOff + third * 2, xOff+boxSize, yOff + third * 2);
        g2D.drawLine(xOff, yOff+boxSize, xOff+boxSize, yOff+boxSize);
        
        // Vertical lines, increasing X each increment
        g2D.drawLine(xOff, yOff, xOff, yOff+boxSize);
        g2D.drawLine(xOff + third, yOff, xOff + third, yOff+boxSize);
        g2D.drawLine(xOff + third * 2, yOff, xOff + third * 2, yOff+boxSize);
        g2D.drawLine(xOff+boxSize, yOff, xOff+boxSize, yOff+boxSize);

        // internal lines
        g2D.setStroke(new BasicStroke(3));

        for (int i = 0; i < 3; i++){
            // Horizontal
            g2D.drawLine(xOff, yOff + third * i + sub, 
                xOff+boxSize, yOff + third * i + sub);
            g2D.drawLine(xOff, yOff + third * i + sub * 2, 
                xOff+boxSize, yOff + third * i + sub * 2);
            
            // Vertical
            g2D.drawLine(xOff + third * i + sub, yOff, 
                xOff + third * i + sub, yOff+boxSize);
            g2D.drawLine(xOff + third * i + sub * 2, yOff, 
                xOff + third * i + sub * 2, yOff+boxSize);
        }

        // numbers
        g2D.setFont(new Font("Times New Roman", Font.PLAIN, (boxSize - xOff) / 9));
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                if (sud[j][i] != 0)
                    g2D.drawString(String.valueOf(sud[j][i]), 
                        xOff + i * sub + sub / 4, 
                        yOff + j * sub + sub - sub / 6);
            }
        }

        // status text
        g2D.setFont(new Font("Times New Roman", Font.PLAIN, textSize));
        g2D.drawString(text, xText, yText);
    }
    

    // ------------------------------- Grid Management Methods --------------------------------

    // clear grid by seting all values to 0
    public void clearGrid(){
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                sud[i][j] = 0;
            }
        }
    }

    // set grid
    public void setGrid(int[][] g){
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                sud[i][j] = g[i][j];
            }
        }
    }

    // randomize the sudoku grid
    // shows process if show is true
    public void randGrid(boolean show){

        text = "Generating Random Grid...";

        // clear the grid
        clearGrid();

        Random rand = new Random();

        // add initial numbers
        for (int i = 0; i < 3; i++){
            // set up numbers to be added
            ArrayList<Integer> nums = new ArrayList<Integer>();
            for (int j = 1; j <= 9; j++){
                    nums.add(j);
            }

            // add each number
            for (int j = 0; j < 9; j++){
                int selected = rand.nextInt(9 - j);
                sud[i*3 + j/3][i*3 + j%3] = nums.get(selected);
                nums.remove(selected);

                if (show){
                    changed[0] = i*3 + j/3;
                    changed[1] = i*3 + j%3;
                    paintGrid();
                }
            }
        }

        // solve the rest of the grid
        //backTrack(true);
        randTrack(show);

        // create empty spaces
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                if (rand.nextInt(2) == 0){
                    sud[i][j] = 0;

                    if (show){
                        changed[0] = i;
                        changed[1] = j;
                        paintGrid();
                    }
                }
            }
        }

        text = "Done";

        changed[0] = -1;
        changed[1] = -1;
        paintGrid();
    }


    // print grid to console
    public void printGrid(int[][] grid){
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                System.out.print(grid[i][j]);
            }
            System.out.println("");
        }
    }

    public void paintGrid(){
        // paint grid
        repaint();
    
        // sleep to make the process visible
        try{
            Thread.sleep(pause);
        }
        catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }


    // ---------------------------------- Sudoku Solving Methods ---------------------------------

    // returns true if placing num at Y: row, X: col in sudoku puzzle grid is legal
    // false if otherwise
    public boolean isSafe(int[][] grid, int row, int col, int num){

        // check the rows and columbs
        for (int i = 0; i < 9; i++){
            if (grid[row][i] == num || grid[i][col] == num)
                return false;
        }

        // calculate 3 by 3 box of row and col
        int bRow = row - row % 3;
        int bCol = col - col % 3;

        // check box
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (grid[i + bRow][j + bCol] == num)
                    return false;
            }
        }

        return true;
    }
    
    // BackTrack Sudoku solving algorithm
    // paint grid if show is true
    public boolean backTrack(int[][] grid, int row, int col, boolean show){

        // check if we reached the end
        if (row == 8 && col == 9)
            return true;

        // advance to next row if end of columbs
        if (col >= 9){
            col = 0;
            row++;
        }

        // check if non-zero
        // if not zero, move to next col
        if (grid[row][col] != 0)
            return backTrack(grid, row, col + 1, show);

        // check 1 through 9
        for (int i = 1; i < 10; i++){
            // if number is legal
            if (isSafe(grid, row, col, i)){
                grid[row][col] = i;

                if (show){

                    changed[0] = row;
                    changed[1] = col;
                    
                    // paint grid
                    paintGrid();
                }

                // check next number
                if (backTrack(grid, row, col + 1, show))
                    return true;
            }
            // reassign to zero if last number failed
            grid[row][col] = 0;
        }
        

        return false;
    }

    // backTrack with less parameter options for ease of coding
    public boolean backTrack(int[][] grid, boolean show){
        text = "Solving Grid with Backtracking Algorithm...";

        boolean b = backTrack(grid, 0, 0, show);

        text = "Done";

        changed[0] = -1;
        changed[1] = -1;
        paintGrid();

        return b;
    }

    public boolean backTrack(boolean show){
        return backTrack(sud, show);
    }


    // Randomized Back Track
    // Instead of going through numbers in numberic order, pick next number randomly from remaining numbers.
    // Used for random sudoku grid generation.
    public boolean randTrack(int[][] grid, int row, int col, Random rand, boolean show){

        // check if we reached the end
        if (row == 8 && col == 9)
            return true;

        // advance to next row if end of columbs
        if (col >= 9){
            col = 0;
            row++;
        }

        // check if non-zero
        // if not zero, move to next col
        if (grid[row][col] != 0)
            return randTrack(grid, row, col + 1, rand, show);

        // set up numbers to be added
        ArrayList<Integer> nums = new ArrayList<Integer>();
        for (int j = 1; j <= 9; j++){
                nums.add(j);
        }


        // check 1 through 9
        for (int i = 0; i < 9; i++){

            int selected = rand.nextInt(9 - i);
            int next = nums.get(selected);
            nums.remove(selected);

            // if number is legal
            if (isSafe(grid, row, col, next)){

                grid[row][col] = next;

                if (show){

                    changed[0] = row;
                    changed[1] = col;
                    
                    // paint grid
                    paintGrid();
                }

                // check next number
                if (randTrack(grid, row, col + 1, rand, show))
                    return true;
            }
            // reassign to zero if last number failed
            grid[row][col] = 0;
        }
        

        return false;
    }

    public boolean randTrack(boolean show){
        Random rand = new Random();
        return randTrack(sud, 0, 0, rand, show);
    }
}
