//import java.awt.*;
import javax.swing.*;

public class MyFrame extends JFrame{
    
    // sudoku panel
    MyPanel panel;

    // default constructor
    MyFrame(){
        setTitle("Sudoku Solver");

        panel = new MyPanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(panel);
        this.pack();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // demonstrate program
    public void demo(){
        System.out.println("Press enter in console to start");
        System.console().readLine();

        // randomize grid
        panel.randGrid(true);

        // begin solving process

        try{
            Thread.sleep(2000);
        }
        catch(InterruptedException ex){
            ex.printStackTrace();
        }

        panel.backTrack(true);
    }
}
