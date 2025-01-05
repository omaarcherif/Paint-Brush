package drawing;
import javax.swing.*;

public class DrawingApp {
    public static void main(String[] args) {
        // Create a JFrame to hold the DrawingPanel
        JFrame frame = new JFrame("DrawingApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        
        // Create the DrawingPanel and add it to the frame
        DrawingPanel drawingPanel = new DrawingPanel();
        frame.add(drawingPanel);
        
        // Show the window
        frame.setVisible(true);
    }
}
