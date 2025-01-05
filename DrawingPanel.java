package drawing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DrawingPanel extends JPanel {

    private JComboBox<String> shapeComboBox;
    private JButton colorButton;
    private JCheckBox dottedLineCheckBox;
    private JCheckBox filledCheckBox;
    private JButton clearButton;
    private JButton undoButton;
    private Color selectedColor;
    private String selectedShape;
    private boolean isDottedLine;
    private boolean isFilled;

    private Point startPoint;  // Starting point of the shape
    private Rectangle currentRectangle;  // Current rectangle being drawn
    private ArrayList<Rectangle> rectangles;  // Array list to store all rectangles

    private ArrayList<Line> freeBrushLines;  // List to store all free brush lines
    private ArrayList<Circle> circles;  // List to store all circles

    public DrawingPanel() {
        // Set the layout for the panel
        setLayout(new BorderLayout());

        // Create a JLabel for the ComboBox
        JLabel shapeLabel = new JLabel("Shape");
        add(shapeLabel, BorderLayout.NORTH);

        // Create the ComboBox with shape options
        String[] shapes = { "Rectangle", "Circle", "Free Brush" };
        shapeComboBox = new JComboBox<>(shapes);

        // Set the default selected shape
        selectedShape = "Rectangle";  // Default shape

        // Add ItemListener to detect changes
        shapeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    selectedShape = (String) e.getItem();
                    repaint();  // Repaint the panel when a new shape is selected
                }
            }
        });

        // Create the color button
        colorButton = new JButton("Color");
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the color chooser dialog when the button is clicked
                selectedColor = JColorChooser.showDialog(DrawingPanel.this, "Choose Color", selectedColor);
                if (selectedColor == null) {
                    selectedColor = Color.BLACK;  // Default to black if no color is chosen
                }
            }
        });

        // Create the dotted line checkbox
        dottedLineCheckBox = new JCheckBox("Dotted Line");
        dottedLineCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                isDottedLine = dottedLineCheckBox.isSelected();
                repaint();  // Repaint the panel when the checkbox state changes
            }
        });

        // Create the filled checkbox
        filledCheckBox = new JCheckBox("Filled");
        filledCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                isFilled = filledCheckBox.isSelected();
                repaint();  // Repaint the panel when the checkbox state changes
            }
        });

        // Create the Clear All button
        clearButton = new JButton("Clear All");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear all shapes (rectangles, circles, free brush lines)
                rectangles.clear();
                circles.clear();
                freeBrushLines.clear();
                repaint();  // Repaint to remove all shapes
            }
        });

        // Create the Undo button
        undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Undo the last drawn shape (rectangle, circle, or free brush line)
                if (!rectangles.isEmpty()) {
                    rectangles.remove(rectangles.size() - 1);  // Undo the last rectangle
                } else if (!circles.isEmpty()) {
                    circles.remove(circles.size() - 1);  // Undo the last circle
                } else if (!freeBrushLines.isEmpty()) {
                    freeBrushLines.remove(freeBrushLines.size() - 1);  // Undo the last free brush line
                }
                repaint();  // Repaint to remove the last shape
            }
        });

        // Create a JPanel for the ComboBox, Color Button, and Checkboxes
        JPanel controlPanel = new JPanel();
        controlPanel.add(shapeLabel);
        controlPanel.add(shapeComboBox);
        controlPanel.add(colorButton);
        controlPanel.add(dottedLineCheckBox);
        controlPanel.add(filledCheckBox);
        controlPanel.add(clearButton);
        controlPanel.add(undoButton);

        add(controlPanel, BorderLayout.NORTH);

        // Default color is black
        selectedColor = Color.BLACK;
        isDottedLine = false;
        isFilled = false;  // Default to not filled

        // Initialize the array list to store all rectangles, free brush lines, and circles
        rectangles = new ArrayList<>();
        freeBrushLines = new ArrayList<>();
        circles = new ArrayList<>();

        // Add MouseListener and MouseMotionListener for drawing
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedShape.equals("Rectangle")) {
                    startPoint = e.getPoint();  // Record the starting point of the rectangle
                    currentRectangle = new Rectangle(startPoint);  // Create a new rectangle object
                } else if (selectedShape.equals("Circle")) {
                    startPoint = e.getPoint();  // Record the starting point of the circle
                } else if (selectedShape.equals("Free Brush")) {
                    // Start a new line for the free brush
                    freeBrushLines.add(new Line(e.getPoint(), e.getPoint()));  // Start with the initial point
                    repaint();  // Repaint to show the initial point
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedShape.equals("Rectangle") && currentRectangle != null) {
                    rectangles.add(currentRectangle);  // Save the drawn rectangle to the list
                    currentRectangle = null;  // Reset the current rectangle after release
                    repaint();  // Repaint to show the saved rectangle
                } else if (selectedShape.equals("Circle")) {
                    int radius = (int) startPoint.distance(e.getPoint());  // Calculate the radius
                    Circle circle = new Circle(startPoint.x - radius, startPoint.y - radius, radius);  // Create the circle
                    circles.add(circle);  // Save the circle to the list
                    repaint();  // Repaint to show the saved circle
                }
            }
        });

        // Handle mouse dragging to update rectangle size or draw free brush
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedShape.equals("Rectangle") && startPoint != null) {
                    int x = Math.min(startPoint.x, e.getX());  // Ensure x is the smaller value
                    int y = Math.min(startPoint.y, e.getY());  // Ensure y is the smaller value
                    int width = Math.abs(startPoint.x - e.getX());  // Calculate width
                    int height = Math.abs(startPoint.y - e.getY());  // Calculate height

                    // Update the current rectangle's position and size
                    currentRectangle.setBounds(x, y, width, height);
                    repaint();  // Repaint to show the rectangle being drawn
                } else if (selectedShape.equals("Free Brush")) {
                    // Update the last line with the new point
                    freeBrushLines.get(freeBrushLines.size() - 1).addPoint(e.getPoint());
                    repaint();  // Repaint to draw the new line segment
                } else if (selectedShape.equals("Circle") && startPoint != null) {
                    // Calculate the radius while dragging
                    int radius = (int) startPoint.distance(e.getPoint());  // Calculate the radius dynamically
                    repaint();  // Repaint to show the current circle while dragging
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set the current selected color for drawing
        g.setColor(selectedColor);

        // If the dotted line is selected, set the graphics to a dashed pattern
        if (isDottedLine) {
            float[] dashPattern = { 2f, 2f };  // Dash pattern
            ((Graphics2D) g).setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dashPattern, 0f));
        } else {
            ((Graphics2D) g).setStroke(new BasicStroke(1f));  // Solid line
        }

        // Draw all saved rectangles
        for (Rectangle rect : rectangles) {
            if (isFilled) {
                g.fillRect(rect.x, rect.y, rect.width, rect.height);  // Fill the rectangle if "Filled" is checked
            } else {
                g.drawRect(rect.x, rect.y, rect.width, rect.height);  // Otherwise, draw the rectangle outline
            }
        }

        // Draw all saved circles
        for (Circle circle : circles) {
            if (isFilled) {
                g.fillOval(circle.x, circle.y, circle.diameter, circle.diameter);  // Fill the circle if "Filled" is checked
            } else {
                g.drawOval(circle.x, circle.y, circle.diameter, circle.diameter);  // Otherwise, draw the circle outline
            }
        }

        // If a rectangle is being drawn (currentRectangle is not null)
        if (currentRectangle != null) {
            if (isFilled) {
                g.fillRect(currentRectangle.x, currentRectangle.y, currentRectangle.width, currentRectangle.height);  // Fill the current rectangle
            } else {
                g.drawRect(currentRectangle.x, currentRectangle.y, currentRectangle.width, currentRectangle.height);  // Draw the outline of the current rectangle
            }
        }

        // If "Free Brush" is selected, draw the free brush lines
        for (Line line : freeBrushLines) {
            for (int i = 1; i < line.getPoints().size(); i++) {
                Point p1 = line.getPoints().get(i - 1);
                Point p2 = line.getPoints().get(i);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);  // Draw a line between consecutive points
            }
        }

        // If a circle is being drawn (based on mouse dragging)
        if (selectedShape.equals("Circle") && startPoint != null) {
            int radius = (int) startPoint.distance(getMousePosition());  // Calculate the radius
            g.setColor(selectedColor);  // Ensure the color is correct
            if (isFilled) {
                g.fillOval(startPoint.x - radius, startPoint.y - radius, radius * 2, radius * 2);  // Draw filled circle
            } else {
                g.drawOval(startPoint.x - radius, startPoint.y - radius, radius * 2, radius * 2);  // Draw outline
            }
        }
    }

    // Inner class to represent a line with multiple points (used for Free Brush)
    private static class Line {
        private ArrayList<Point> points;

        public Line(Point start, Point end) {
            points = new ArrayList<>();
            points.add(start);
            points.add(end);
        }

        public void addPoint(Point point) {
            points.add(point);
        }

        public ArrayList<Point> getPoints() {
            return points;
        }
    }

    // Inner class to represe nt a circle
    private static class Circle {
        int x, y, diameter;

        public Circle(int x, int y, int radius) {
            this.x = x;
            this.y = y;
            this.diameter = radius * 2;  // Diameter is twice the radius
        }
    }
}
