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

import com.github.daytron.flipit.data.TileProperty;
import com.github.daytron.flipit.data.TileColor;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author Ryan Gilera ryangilera@gmail.com
 */
final class FlipAnimation {

    private int frameCount;

    private final double gridXSpace;
    private final double gridYSpace;

    private final List<Double> rowCell;
    private final List<Double> columnCell;

    private final int TILE_EDGE_EFFECT_THICKNESS;
    private final GraphicsContext gc;
    private final Graphics graphics;

    protected FlipAnimation(int frameCount, double gridXSpace, double gridYSpace,
            List<Double> rowCell, List<Double> columnCell,
            int TILE_EDGE_EFFECT_THICKNESS, GraphicsContext gc,
            Graphics graphics) {
        this.frameCount = frameCount;
        this.gridXSpace = gridXSpace;
        this.gridYSpace = gridYSpace;
        this.rowCell = rowCell;
        this.columnCell = columnCell;
        this.TILE_EDGE_EFFECT_THICKNESS = TILE_EDGE_EFFECT_THICKNESS;
        this.gc = gc;

        this.graphics = graphics;
    }

    protected void flipTile(final TileColor playerColor,
            final int count_column, final int count_row) {
        // Reset frameCount
        this.frameCount = 4;

        final Timeline timeline = new Timeline();

        timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO,
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        frameCount = displayFrame(frameCount, playerColor,
                                count_column, count_row);

                        if (frameCount == 0) {
                            timeline.stop();
                        }
                    }
                }), new KeyFrame(Duration.millis(TileProperty.FLIP_FRAME_DURATION.getValue())));

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }

    private int displayFrame(int frame, TileColor playerColor,
            int count_column, int count_row) {
        String light_edge_color;
        String light_edge_color_at_45_deg;
        String main_color;
        String shadow_edge_color;

        String enemy_light_edge_color;
        String enemy_light_edge_color_at_45_deg;
        String enemy_main_color;
        String enemy_shadow_edge_color;

        int tile_edge_width_at_45 = 
                TileProperty.TILE_EDGE_WIDTH_AT_45_DEG_FOR_EACH_PLAYER_COLOR_AREA
                .getValue();

        // Detect and set the colors
        if (playerColor == TileColor.PLAYER_BLUE) {
            light_edge_color = TileColor.PLAYER_BLUE_LIGHT_EDGE.getColor();
            light_edge_color_at_45_deg
                    = TileColor.PLAYER_BLUE_LIGHT_EDGE_AT_45_DEG.getColor();
            main_color = TileColor.PLAYER_BLUE.getColor();
            shadow_edge_color = TileColor.PLAYER_BLUE_SHADOW_EDGE.getColor();

            enemy_light_edge_color = TileColor.PLAYER_RED_LIGHT_EDGE.getColor();
            enemy_light_edge_color_at_45_deg
                    = TileColor.PLAYER_RED_LIGHT_EDGE_AT_45_DEG.getColor();
            enemy_main_color = TileColor.PLAYER_RED.getColor();
            enemy_shadow_edge_color = TileColor.PLAYER_RED_SHADOW_EDGE.getColor();
        } else if (playerColor == TileColor.PLAYER_RED) {
            light_edge_color = TileColor.PLAYER_RED_LIGHT_EDGE.getColor();
            light_edge_color_at_45_deg
                    = TileColor.PLAYER_RED_LIGHT_EDGE_AT_45_DEG.getColor();
            main_color = TileColor.PLAYER_RED.getColor();
            shadow_edge_color = TileColor.PLAYER_RED_SHADOW_EDGE.getColor();

            enemy_light_edge_color = TileColor.PLAYER_BLUE_LIGHT_EDGE.getColor();
            enemy_light_edge_color_at_45_deg
                    = TileColor.PLAYER_BLUE_LIGHT_EDGE_AT_45_DEG.getColor();
            enemy_main_color = TileColor.PLAYER_BLUE.getColor();
            enemy_shadow_edge_color = TileColor.PLAYER_BLUE_SHADOW_EDGE.getColor();
        } else {
            return 0;
        }

        // Declare and initialize draw origin corner
        double topLeftCornerXForEnemyColorEdge;
        double topLeftCornerYForEnemyColorEdge = rowCell.get(count_row);

        double topLeftCornerXForHumanColorEdge;
        double topLeftCornerYForHumanColorEdge = rowCell.get(count_row);

        switch (frame) {
            case 4:
                /**
                 * A frame where the left tile edge is 45 degrees above the
                 * ground.
                 */

                // Clear tile first to remove previous paint
                resetTileColor(count_column, count_row);

                // Calculate the origin corner to draw for computer color edge
                topLeftCornerXForEnemyColorEdge
                        = columnCell.get(count_column) + (gridXSpace / 4);

                // Calculate the origin corner to draw for human color edge
                topLeftCornerXForHumanColorEdge
                        = topLeftCornerXForEnemyColorEdge + tile_edge_width_at_45;

                // Draw the left side of the edge (computer)
                gc.setFill(Color.web(light_edge_color_at_45_deg));
                gc.fillRect(topLeftCornerXForEnemyColorEdge,
                        topLeftCornerYForEnemyColorEdge,
                        tile_edge_width_at_45, gridYSpace);

                // Draw the right side of the edge (human)
                gc.setFill(Color.web(enemy_light_edge_color_at_45_deg));
                gc.fillRect(topLeftCornerXForHumanColorEdge,
                        topLeftCornerYForHumanColorEdge,
                        tile_edge_width_at_45, gridYSpace);

                // Draw the top edge (human)
                gc.setFill(Color.web(enemy_light_edge_color));
                gc.fillRect(topLeftCornerXForHumanColorEdge + tile_edge_width_at_45,
                        topLeftCornerYForHumanColorEdge,
                        (gridXSpace / 2) - (tile_edge_width_at_45 * 2),
                        TILE_EDGE_EFFECT_THICKNESS);

                // Draw the body of the tile (computer)
                gc.setFill(Color.web(enemy_main_color));
                gc.fillRect(topLeftCornerXForHumanColorEdge + tile_edge_width_at_45,
                        topLeftCornerYForHumanColorEdge + TILE_EDGE_EFFECT_THICKNESS,
                        (gridXSpace / 2) - (tile_edge_width_at_45 * 2),
                        gridYSpace - TILE_EDGE_EFFECT_THICKNESS);

                // Draw the shadow edge of the tile (human)
                gc.setFill(Color.web(enemy_shadow_edge_color));
                gc.fillRect(topLeftCornerXForHumanColorEdge + tile_edge_width_at_45,
                        topLeftCornerYForHumanColorEdge
                        + (gridYSpace - TILE_EDGE_EFFECT_THICKNESS),
                        (gridXSpace / 2) - (tile_edge_width_at_45 * 2),
                        TILE_EDGE_EFFECT_THICKNESS);

                break;

            case 3:
                /**
                 * A frame where the left tile edge is now perpendicular in
                 * respect with the ground (90 degrees), only showing the width
                 * of the left edge. The actual width of the edge is twice the
                 * tile edge effect width, where each half belongs to a player
                 * color edge.
                 */

                // Clear tile first to remove previous paint
                resetTileColor(count_column, count_row);

                // Calculate the origin corner to draw for computer color edge
                topLeftCornerXForEnemyColorEdge = columnCell.get(count_column)
                        + ((gridXSpace / 2) - TILE_EDGE_EFFECT_THICKNESS);

                // Calculate the origin corner to draw for human color edge
                topLeftCornerXForHumanColorEdge
                        = topLeftCornerXForEnemyColorEdge
                        + TILE_EDGE_EFFECT_THICKNESS;

                // Draw the left side of the edge (computer)
                gc.setFill(Color.web(main_color));
                gc.fillRect(topLeftCornerXForEnemyColorEdge,
                        topLeftCornerYForEnemyColorEdge,
                        TILE_EDGE_EFFECT_THICKNESS, gridYSpace);

                // Draw the right side of the edge (human)
                gc.setFill(Color.web(enemy_main_color));
                gc.fillRect(topLeftCornerXForHumanColorEdge,
                        topLeftCornerYForHumanColorEdge,
                        TILE_EDGE_EFFECT_THICKNESS, gridYSpace);

                break;

            case 2:

                // Clear tile first to remove previous paint
                resetTileColor(count_column, count_row);

                // Calculate the origin corner to draw for computer color edge
                topLeftCornerXForEnemyColorEdge
                        = columnCell.get(count_column)
                        + ((gridXSpace * 0.75) - (tile_edge_width_at_45 * 2));

                // Calculate the origin corner to draw for human color edge
                topLeftCornerXForHumanColorEdge
                        = topLeftCornerXForEnemyColorEdge + tile_edge_width_at_45;

                // Draw the body of the tile (human)
                gc.setFill(Color.web(main_color));
                gc.fillRect(columnCell.get(count_column) + (gridXSpace / 4),
                        topLeftCornerYForHumanColorEdge + TILE_EDGE_EFFECT_THICKNESS,
                        (gridXSpace / 2) - (tile_edge_width_at_45 * 2),
                        gridYSpace - TILE_EDGE_EFFECT_THICKNESS);

                // Draw the top edge (human)
                gc.setFill(Color.web(light_edge_color));
                gc.fillRect(columnCell.get(count_column) + (gridXSpace / 4),
                        topLeftCornerYForHumanColorEdge,
                        (gridXSpace / 2) - (tile_edge_width_at_45 * 2),
                        TILE_EDGE_EFFECT_THICKNESS);

                // Draw the shadow edge of the tile (human)
                gc.setFill(Color.web(shadow_edge_color));
                gc.fillRect(columnCell.get(count_column) + (gridXSpace / 4),
                        topLeftCornerYForHumanColorEdge
                        + (gridYSpace - TILE_EDGE_EFFECT_THICKNESS),
                        (gridXSpace / 2) - (tile_edge_width_at_45 * 2),
                        TILE_EDGE_EFFECT_THICKNESS);

                // Draw the left side of the edge (computer)
                gc.setFill(Color.web(light_edge_color_at_45_deg));
                gc.fillRect(topLeftCornerXForEnemyColorEdge,
                        topLeftCornerYForEnemyColorEdge,
                        tile_edge_width_at_45, gridYSpace);

                // Draw the right side of the edge (human)
                gc.setFill(Color.web(enemy_light_edge_color_at_45_deg));
                gc.fillRect(topLeftCornerXForHumanColorEdge,
                        topLeftCornerYForHumanColorEdge,
                        tile_edge_width_at_45, gridYSpace);

                break;

            case 1:
                /**
                 * The final frame in which only shows the enemy side of the
                 * tile
                 */

                // Clear tile first to remove previous paint
                resetTileColor(count_column, count_row);

                graphics.paintTile(light_edge_color,
                        main_color,
                        shadow_edge_color,
                        count_column, count_row);
                break;
        }

        frame -= 1;
        return frame;
    }

    private void resetTileColor(int count_column, int count_row) {
        gc.setFill(Color.web(TileColor.TILE_WHITE.getColor()));
        gc.fillRect(columnCell.get(count_column), rowCell.get(count_row),
                gridXSpace, gridYSpace);
    }

}
