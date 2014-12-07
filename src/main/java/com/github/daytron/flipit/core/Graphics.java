/*
 * The MIT License
 *
 * Copyright 2014 Your Organisation.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.daytron.flipit.core;

import com.github.daytron.flipit.data.TileColor;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Ryan Gilera ryangilera@gmail.com
 */
final class Graphics {

    private final GraphicsContext gc;

    private final double gridXSpace;
    private final double gridYSpace;

    private final List<Double> rowCell;
    private final List<Double> columnCell;

    private final int TILE_EDGE_EFFECT_THICKNESS;

    private int frameCount;

    protected Graphics(GraphicsContext gc, double gridX, double gridY,
            List<Double> rowCell, List<Double> columnCell,
            int tile_edge) {
        this.gc = gc;
        this.gc.setLineWidth(2);

        this.gridXSpace = gridX;
        this.gridYSpace = gridY;
        this.rowCell = rowCell;
        this.columnCell = columnCell;
        this.TILE_EDGE_EFFECT_THICKNESS = tile_edge;
    }

    /**
     * Method for painting the tile
     *
     * @param light_edge_color A string hex color code
     * @param main_color A string hex color code
     * @param shadow_edge_color A string hex color code
     * @param count_column int value of the column (x) position of the tile
     * (with 0 as index)
     * @param count_row int value of the row (y) position of the tile (with 0 as
     * index)
     */
    protected void paintTile(String light_edge_color,
            String main_color, String shadow_edge_color,
            int count_column, int count_row) {
        // coloring the light top and left edges respectively
        gc.setFill(Color.web(light_edge_color));
        gc.fillRect(columnCell.get(count_column), rowCell.get(count_row),
                gridXSpace, TILE_EDGE_EFFECT_THICKNESS);
        gc.fillRect(columnCell.get(count_column), rowCell.get(count_row),
                TILE_EDGE_EFFECT_THICKNESS, gridYSpace);

        // coloring main tile body
        gc.setFill(Color.web(main_color));
        gc.fillRect(columnCell.get(count_column) + TILE_EDGE_EFFECT_THICKNESS,
                rowCell.get(count_row) + TILE_EDGE_EFFECT_THICKNESS,
                gridXSpace - TILE_EDGE_EFFECT_THICKNESS,
                gridYSpace - TILE_EDGE_EFFECT_THICKNESS);

        // coloring tile's shadow for bottom and right edges respectively
        gc.setFill(Color.web(shadow_edge_color));
        gc.fillRect(columnCell.get(count_column) + TILE_EDGE_EFFECT_THICKNESS,
                rowCell.get(count_row) + gridYSpace - TILE_EDGE_EFFECT_THICKNESS,
                gridXSpace - TILE_EDGE_EFFECT_THICKNESS,
                TILE_EDGE_EFFECT_THICKNESS);
        gc.fillRect(columnCell.get(count_column)
                + gridXSpace - TILE_EDGE_EFFECT_THICKNESS,
                rowCell.get(count_row) + TILE_EDGE_EFFECT_THICKNESS,
                TILE_EDGE_EFFECT_THICKNESS,
                gridYSpace - TILE_EDGE_EFFECT_THICKNESS);
    }

    protected void flipTile(final TileColor playerColor,
            final int count_column, final int count_row) {

        FlipAnimation flipAnimation = new FlipAnimation(frameCount,
                gridXSpace, gridYSpace, rowCell, columnCell,
                TILE_EDGE_EFFECT_THICKNESS, gc, this);
        flipAnimation.flipTile(playerColor, count_column, count_row);
    }
}
