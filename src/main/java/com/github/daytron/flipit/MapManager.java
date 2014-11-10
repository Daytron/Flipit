/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 *
 * @author ryan
 */
public class MapManager {
    private final Canvas canvas;
    private final List<Double> rowCell;
    private final List<Double> columnCell;

    public MapManager(Canvas canvas) {
        this.canvas = canvas;
        this.rowCell = new ArrayList<>();
        this.columnCell = new ArrayList<>();
    }
    
    
    public void generateMap(GraphicsContext gc) {
        double x = this.canvas.getWidth();
        double y = this.canvas.getHeight();
        
        double preferredHeight = ((int)y / 20) * 20.0;
        double preferredWidth = ((int)x / 30) * 30.0;
        
        // Padding space for width and height
        double halfPaddingWidth =  (x - preferredWidth) / 2;
        double halfPaddingHeight = (y - preferredHeight) / 2;
        
        // space between each cell
        double gridXSpace = preferredWidth / 30;
        double gridYSpace  = preferredHeight / 20;
        
        // generate rows
        for(double yi = halfPaddingHeight; yi <= (y - halfPaddingHeight); yi = yi + gridYSpace) {
            gc.strokeLine(halfPaddingWidth, yi, x - halfPaddingWidth, yi);
            this.rowCell.add(yi);
        }
        
        // generate columns
        for(double xi = halfPaddingWidth; xi <= (x - halfPaddingWidth); xi = xi + gridXSpace) {
            gc.strokeLine(xi, halfPaddingHeight, xi, y - halfPaddingHeight);
            this.columnCell.add(xi);
        }
        
        gc.setFill(Color.BLUE);
        gc.fillRect(this.columnCell.get(3), this.rowCell.get(3), gridXSpace,gridYSpace);
        
    }

    public List<Double> getColumnCell() {
        return columnCell;
    }

    public List<Double> getRowCell() {
        return rowCell;
    }
    
}
