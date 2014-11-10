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

/**
 *
 * @author ryan
 */
public class MapManager {
    private final int numberOfRows;
    private final int numberOfColumns;
    private final Canvas canvas;
    private final List<Double> rowCell;
    private final List<Double> columnCell;
    private double gridXSpace;
    private double gridYSpace;
    
    private final static int TILE_EDGE_EFFECT_THICKNESS = 1;

    public MapManager(Canvas canvas, int rows, int columns) {
        this.canvas = canvas;
        this.rowCell = new ArrayList<>();
        this.columnCell = new ArrayList<>();
        this.numberOfRows = rows;
        this.numberOfColumns = columns;
    }
    
    
    public void generateMap(GraphicsContext gc) {
        double x = this.canvas.getWidth();
        double y = this.canvas.getHeight();
        
        double preferredHeight = ((int)y / this.numberOfRows) * (double)this.numberOfRows;
        double preferredWidth = ((int)x / this.numberOfColumns) * (double)this.numberOfColumns;
        
        // Padding space for width and height
        double halfPaddingWidth =  (x - preferredWidth) / 2;
        double halfPaddingHeight = (y - preferredHeight) / 2;
        
        // space between each cell
        this.gridXSpace = preferredWidth / this.numberOfColumns;
        this.gridYSpace  = preferredHeight / this.numberOfRows;
        
        gc.setLineWidth(2);
        
        // generate rows
        for(double yi = halfPaddingHeight; yi <= (y - halfPaddingHeight); yi = yi + this.gridYSpace) {
            //gc.strokeLine(halfPaddingWidth, yi, x - halfPaddingWidth, yi);
            this.rowCell.add(yi);
        }
        
        // generate columns
        for(double xi = halfPaddingWidth; xi <= (x - halfPaddingWidth); xi = xi + this.gridXSpace) {
            //gc.strokeLine(xi, halfPaddingHeight, xi, y - halfPaddingHeight);
            this.columnCell.add(xi);
        }
        
        // Fill grid tiles with neutral color
        for(int count_row = 0; count_row < this.numberOfRows; count_row++) {
            for(int count_column = 0; count_column < this.numberOfColumns; count_column++) {
                
                // coloring (for nuetral tiles) the light top and left edges respectively
                gc.setFill(Color.web("#FFFFFF"));
                gc.fillRect(this.columnCell.get(count_column), this.rowCell.get(count_row), this.gridXSpace, TILE_EDGE_EFFECT_THICKNESS);
                gc.fillRect(this.columnCell.get(count_column), this.rowCell.get(count_row), TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace);
                
                // coloring main tile body
                gc.setFill(Color.web("#C6C6C6"));
                gc.fillRect(this.columnCell.get(count_column) + TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(count_row) + TILE_EDGE_EFFECT_THICKNESS, this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS);
                
                // coloring tile's shadow for bottom and right edges respectively
                gc.setFill(Color.web("#000000"));
                gc.fillRect(this.columnCell.get(count_column) + TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(count_row) + this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS, this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, TILE_EDGE_EFFECT_THICKNESS);
                
                gc.fillRect(this.columnCell.get(count_column) + this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(count_row) + TILE_EDGE_EFFECT_THICKNESS, TILE_EDGE_EFFECT_THICKNESS , this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS);
            }
        }
        
        // top tile bright
        gc.setFill(Color.web("#0E94FF"));
        gc.fillRect(this.columnCell.get(3), this.rowCell.get(3), this.gridXSpace, TILE_EDGE_EFFECT_THICKNESS);
        
        // left tile bright
        gc.setFill(Color.web("#0E94FF"));
        gc.fillRect(this.columnCell.get(3), this.rowCell.get(3), TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace);
        
        // main tile color
        gc.setFill(Color.web("#0073CF"));
        gc.fillRect(this.columnCell.get(3) + TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(3) + TILE_EDGE_EFFECT_THICKNESS, this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS);
        
        // bottom shadow
        gc.setFill(Color.web("#004881"));
        gc.fillRect(this.columnCell.get(3) + TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(3) + this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS, this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS , TILE_EDGE_EFFECT_THICKNESS);
        // right shadow
        gc.setFill(Color.web("#004881"));
        gc.fillRect(this.columnCell.get(3) + this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(3) + TILE_EDGE_EFFECT_THICKNESS, TILE_EDGE_EFFECT_THICKNESS , this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS);
        
        //############################################
        
        // top tile bright
        gc.setFill(Color.web("#0E94FF"));
        gc.fillRect(this.columnCell.get(2), this.rowCell.get(3), this.gridXSpace, TILE_EDGE_EFFECT_THICKNESS);
        
        // left tile bright
        gc.fillRect(this.columnCell.get(2), this.rowCell.get(3), TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace);
        
        // main tile color
        gc.setFill(Color.web("#0073CF"));
        gc.fillRect(this.columnCell.get(2) + TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(3) + TILE_EDGE_EFFECT_THICKNESS, this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS);
        
        // bottom shadow
        gc.setFill(Color.web("#004881"));
        gc.fillRect(this.columnCell.get(2) + TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(3) + this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS, this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS , TILE_EDGE_EFFECT_THICKNESS);
        // right shadow
        gc.fillRect(this.columnCell.get(2) + this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(3) + TILE_EDGE_EFFECT_THICKNESS, TILE_EDGE_EFFECT_THICKNESS , this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS);
    }

    public List<Double> getColumnCell() {
        return this.columnCell;
    }

    public List<Double> getRowCell() {
        return this.rowCell;
    }
    
}
