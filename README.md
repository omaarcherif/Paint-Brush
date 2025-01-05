# Paint-Brush
Drawing Application

This project provides a simple drawing application implemented in Java. It allows users to draw various shapes, such as rectangles, circles, and freehand brush lines, with additional customization options. The application is divided into two main classes: DrawingApp and DrawingPanel.

Features

General Features

Draw rectangles, circles, and freehand brush lines.

Choose colors for drawing.

Option to use dotted lines.

Toggle filled shapes.

Undo the last drawn shape.

Clear all shapes.

File Descriptions

DrawingApp.java

This is the main entry point of the application. It:

Initializes a JFrame window titled DrawingApp.

Adds the DrawingPanel to the frame.

Sets the default window size and visibility.

DrawingPanel.java

This is the primary component for the drawing functionality. It:

Provides tools for selecting shapes (rectangle, circle, free brush).

Allows customization, such as choosing colors, dotted lines, and filled shapes.

Implements undo and clear all features.

Handles user input through mouse interactions to create and update shapes.

How to Run

Compile both Java files:

javac DrawingApp.java DrawingPanel.java

Run the application:

java drawing.DrawingApp

Requirements

Java Development Kit (JDK) 8 or later.

Example Usage

Select a shape from the dropdown menu (Rectangle, Circle, or Free Brush).

Use the color picker to choose a drawing color.

Toggle the options for dotted lines or filled shapes.

Draw on the panel using mouse interactions.

Press and drag to create rectangles or circles.

Drag freely for the Free Brush tool.

Use Undo to remove the last drawn shape or Clear All to reset the canvas.

-Future Improvements

. Add support for saving and loading drawings.

. Include additional shapes and drawing tools.

. Implement multi-touch or gesture-based drawing.

License

This project is open-source and available under the MIT License.

