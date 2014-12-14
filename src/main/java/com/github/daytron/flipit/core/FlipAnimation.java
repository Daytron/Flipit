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

import com.github.daytron.flipit.data.MapProperty;
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
        this.frameCount = MapProperty.NUMBER_OF_TILE_ANIMATION_FRAMES.getValue();

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
                }), new KeyFrame(Duration.millis(MapProperty.FLIP_FRAME_DURATION.getValue())));

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }

    private int displayFrame(int frame, TileColor playerColor,
            int count_column, int count_row) {
        String light_edge_color;
        String light_edge_color_at_30_deg;
        String light_edge_color_at_60_deg;
        String main_color;
        String shadow_edge_color;

        String enemy_light_edge_color;
        String enemy_light_edge_color_at_30_deg;
        String enemy_light_edge_color_at_60_deg;
        String enemy_main_color;
        String enemy_shadow_edge_color;

        double tile_edge_width_at_30_half
                = MapProperty.TILE_EDGE_WIDTH_AT_30_DEG_FOR_EACH_PLAYER_COLOR_AREA
                .getValue() / 2;
        double tile_edge_width_at_30
                = MapProperty.TILE_EDGE_WIDTH_AT_30_DEG_FOR_EACH_PLAYER_COLOR_AREA
                .getValue();

        double tile_edge_width_at_60_half
                = MapProperty.TILE_EDGE_WIDTH_AT_60_DEG_FOR_EACH_PLAYER_COLOR_AREA
                .getValue() / 2;
        double tile_edge_width_at_60
                = MapProperty.TILE_EDGE_WIDTH_AT_60_DEG_FOR_EACH_PLAYER_COLOR_AREA
                .getValue();

        // Detect and set the colors
        if (playerColor == TileColor.PLAYER_BLUE) {
            light_edge_color = TileColor.PLAYER_BLUE_LIGHT_EDGE.getColor();
            light_edge_color_at_30_deg
                    = TileColor.PLAYER_BLUE_LIGHT_EDGE_AT_30_DEG.getColor();
            light_edge_color_at_60_deg
                    = TileColor.PLAYER_BLUE_LIGHT_EDGE_AT_60_DEG.getColor();
            main_color = TileColor.PLAYER_BLUE.getColor();
            shadow_edge_color = TileColor.PLAYER_BLUE_SHADOW_EDGE.getColor();

            enemy_light_edge_color = TileColor.PLAYER_RED_LIGHT_EDGE.getColor();
            enemy_light_edge_color_at_30_deg
                    = TileColor.PLAYER_RED_LIGHT_EDGE_AT_30_DEG.getColor();
            enemy_light_edge_color_at_60_deg
                    = TileColor.PLAYER_RED_LIGHT_EDGE_AT_60_DEG.getColor();
            enemy_main_color = TileColor.PLAYER_RED.getColor();
            enemy_shadow_edge_color = TileColor.PLAYER_RED_SHADOW_EDGE.getColor();
        } else if (playerColor == TileColor.PLAYER_RED) {
            light_edge_color = TileColor.PLAYER_RED_LIGHT_EDGE.getColor();
            light_edge_color_at_30_deg
                    = TileColor.PLAYER_RED_LIGHT_EDGE_AT_30_DEG.getColor();
            light_edge_color_at_60_deg
                    = TileColor.PLAYER_RED_LIGHT_EDGE_AT_60_DEG.getColor();
            main_color = TileColor.PLAYER_RED.getColor();
            shadow_edge_color = TileColor.PLAYER_RED_SHADOW_EDGE.getColor();

            enemy_light_edge_color = TileColor.PLAYER_BLUE_LIGHT_EDGE.getColor();
            enemy_light_edge_color_at_30_deg
                    = TileColor.PLAYER_BLUE_LIGHT_EDGE_AT_30_DEG.getColor();
            enemy_light_edge_color_at_60_deg
                    = TileColor.PLAYER_BLUE_LIGHT_EDGE_AT_60_DEG.getColor();
            enemy_main_color = TileColor.PLAYER_BLUE.getColor();
            enemy_shadow_edge_color = TileColor.PLAYER_BLUE_SHADOW_EDGE.getColor();
        } else {
            return 0;
        }

        // Declare and initialize draw origin corner
        double topLeftCornerXForOtherPlayerColorEdge;
        double topLeftCornerYForOtherPlayerColorEdge = rowCell.get(count_row);

        double topLeftCornerXForCurrentPlayerColorEdge;
        double topLeftCornerYForCurrentPlayerColorEdge = rowCell.get(count_row);

        // Calculate the x padding space for 30 degrees tile turn
        double adjacentLengthAt30 = (gridXSpace / 2)
                * Math.cos(Math.toRadians(30));
        double paddingAt30Deg = (gridXSpace / 2) - adjacentLengthAt30;

        // Calculate the x padding space for 60 degrees tile turn
        double adjacentLengthAt60 = (gridXSpace / 2)
                * Math.cos(Math.toRadians(60));
        double paddingAt60Deg = (gridXSpace / 2) - adjacentLengthAt60;

        switch (frame) {
            case 6:
                /**
                 * A frame where the left tile edge is 30 degrees above the
                 * ground.
                 */

                // Clear tile first to remove previous paint
                resetTileColor(count_column, count_row);

                // Calculate the origin corner to draw for computer color edge
                topLeftCornerXForOtherPlayerColorEdge
                        = columnCell.get(count_column) + paddingAt30Deg;

                // Calculate the origin corner to draw for human color edge
                topLeftCornerXForCurrentPlayerColorEdge
                        = topLeftCornerXForOtherPlayerColorEdge
                        + tile_edge_width_at_30_half;

                // Draw the left side of the edge (computer)
                gc.setFill(Color.web(light_edge_color_at_30_deg));
                gc.fillRect(topLeftCornerXForOtherPlayerColorEdge,
                        topLeftCornerYForOtherPlayerColorEdge,
                        tile_edge_width_at_30_half, gridYSpace);

                // Draw the right side of the edge (human)
                gc.setFill(Color.web(enemy_light_edge_color_at_30_deg));
                gc.fillRect(topLeftCornerXForCurrentPlayerColorEdge,
                        topLeftCornerYForCurrentPlayerColorEdge,
                        tile_edge_width_at_30_half, gridYSpace);

                // Draw the top edge (human)
                gc.setFill(Color.web(enemy_light_edge_color));
                gc.fillRect(topLeftCornerXForCurrentPlayerColorEdge
                        + tile_edge_width_at_30_half,
                        topLeftCornerYForCurrentPlayerColorEdge,
                        (adjacentLengthAt30 * 2) - tile_edge_width_at_30,
                        TILE_EDGE_EFFECT_THICKNESS);

                // Draw the body of the tile (computer)
                gc.setFill(Color.web(enemy_main_color));
                gc.fillRect(topLeftCornerXForCurrentPlayerColorEdge
                        + tile_edge_width_at_30_half,
                        topLeftCornerYForCurrentPlayerColorEdge
                        + TILE_EDGE_EFFECT_THICKNESS,
                        (adjacentLengthAt30 * 2) - tile_edge_width_at_30,
                        gridYSpace - TILE_EDGE_EFFECT_THICKNESS);

                // Draw the shadow edge of the tile (human)
                gc.setFill(Color.web(enemy_shadow_edge_color));
                gc.fillRect(topLeftCornerXForCurrentPlayerColorEdge
                        + tile_edge_width_at_30_half,
                        topLeftCornerYForCurrentPlayerColorEdge
                        + (gridYSpace - TILE_EDGE_EFFECT_THICKNESS),
                        (adjacentLengthAt30 * 2) - tile_edge_width_at_30,
                        TILE_EDGE_EFFECT_THICKNESS);

                break;

            case 5:
                /**
                 * A frame where the left tile edge is 60 degrees above the
                 * ground.
                 */

                // Clear tile first to remove previous paint
                resetTileColor(count_column, count_row);

                // Calculate the origin corner to draw for computer color edge
                topLeftCornerXForOtherPlayerColorEdge
                        = columnCell.get(count_column) + paddingAt60Deg;

                // Calculate the origin corner to draw for human color edge
                topLeftCornerXForCurrentPlayerColorEdge
                        = topLeftCornerXForOtherPlayerColorEdge
                        + tile_edge_width_at_60_half;

                // Draw the left side of the edge (computer)
                gc.setFill(Color.web(light_edge_color_at_60_deg));
                gc.fillRect(topLeftCornerXForOtherPlayerColorEdge,
                        topLeftCornerYForOtherPlayerColorEdge,
                        tile_edge_width_at_60_half, gridYSpace);

                // Draw the right side of the edge (human)
                gc.setFill(Color.web(enemy_light_edge_color_at_60_deg));
                gc.fillRect(topLeftCornerXForCurrentPlayerColorEdge,
                        topLeftCornerYForCurrentPlayerColorEdge,
                        tile_edge_width_at_60_half, gridYSpace);

                // Draw the top edge (human)
                gc.setFill(Color.web(enemy_light_edge_color));
                gc.fillRect(topLeftCornerXForCurrentPlayerColorEdge
                        + tile_edge_width_at_60_half,
                        topLeftCornerYForCurrentPlayerColorEdge,
                        (adjacentLengthAt60 * 2) - tile_edge_width_at_60,
                        TILE_EDGE_EFFECT_THICKNESS);

                // Draw the body of the tile (computer)
                gc.setFill(Color.web(enemy_main_color));
                gc.fillRect(topLeftCornerXForCurrentPlayerColorEdge
                        + tile_edge_width_at_60_half,
                        topLeftCornerYForCurrentPlayerColorEdge
                        + TILE_EDGE_EFFECT_THICKNESS,
                        (adjacentLengthAt60 * 2) - tile_edge_width_at_60,
                        gridYSpace - TILE_EDGE_EFFECT_THICKNESS);

                // Draw the shadow edge of the tile (human)
                gc.setFill(Color.web(enemy_shadow_edge_color));
                gc.fillRect(topLeftCornerXForCurrentPlayerColorEdge
                        + tile_edge_width_at_60_half,
                        topLeftCornerYForCurrentPlayerColorEdge
                        + (gridYSpace - TILE_EDGE_EFFECT_THICKNESS),
                        (adjacentLengthAt60 * 2) - tile_edge_width_at_60,
                        TILE_EDGE_EFFECT_THICKNESS);

                break;

            case 4:
                /**
                 * A frame where the left tile edge is now perpendicular in
                 * respect with the ground (90 degrees), only showing the width
                 * of the left edge. The actual width of the edge is 6, where
                 * each half belongs to a player color edge.
                 */

                // Clear tile first to remove previous paint
                resetTileColor(count_column, count_row);

                // Calculate the origin corner to draw for computer color edge
                topLeftCornerXForOtherPlayerColorEdge = columnCell.get(count_column)
                        + ((gridXSpace / 2)
                        - MapProperty.TILE_EDGE_HALF_WIDTH.getValue());

                // Calculate the origin corner to draw for human color edge
                topLeftCornerXForCurrentPlayerColorEdge
                        = topLeftCornerXForOtherPlayerColorEdge
                        + MapProperty.TILE_EDGE_HALF_WIDTH.getValue();

                // Draw the left side of the edge (computer)
                gc.setFill(Color.web(main_color));
                gc.fillRect(topLeftCornerXForOtherPlayerColorEdge,
                        topLeftCornerYForOtherPlayerColorEdge,
                        MapProperty.TILE_EDGE_HALF_WIDTH.getValue(),
                        gridYSpace);

                // Draw the right side of the edge (human)
                gc.setFill(Color.web(enemy_main_color));
                gc.fillRect(topLeftCornerXForCurrentPlayerColorEdge,
                        topLeftCornerYForCurrentPlayerColorEdge,
                        MapProperty.TILE_EDGE_HALF_WIDTH.getValue(),
                        gridYSpace);

                break;

            case 3:
                /**
                 * A frame where the right tile edge is 60 degrees above the
                 * ground.
                 */

                // Clear tile first to remove previous paint
                resetTileColor(count_column, count_row);

                // Calculate the origin corner to draw for computer color edge
                topLeftCornerXForOtherPlayerColorEdge
                        = columnCell.get(count_column)
                        + (gridXSpace - paddingAt60Deg
                        - (tile_edge_width_at_60_half * 2));

                // Calculate the origin corner to draw for human color edge
                topLeftCornerXForCurrentPlayerColorEdge
                        = topLeftCornerXForOtherPlayerColorEdge
                        + tile_edge_width_at_60_half;

                // Draw the body of the tile (human)
                gc.setFill(Color.web(main_color));
                gc.fillRect(columnCell.get(count_column) + paddingAt60Deg,
                        topLeftCornerYForCurrentPlayerColorEdge
                        + TILE_EDGE_EFFECT_THICKNESS,
                        adjacentLengthAt60 * 2,
                        gridYSpace - TILE_EDGE_EFFECT_THICKNESS);

                // Draw the top edge (human)
                gc.setFill(Color.web(light_edge_color));
                gc.fillRect(columnCell.get(count_column) + paddingAt60Deg,
                        topLeftCornerYForCurrentPlayerColorEdge,
                        adjacentLengthAt60 * 2,
                        TILE_EDGE_EFFECT_THICKNESS);

                // Draw the shadow edge of the tile (human)
                gc.setFill(Color.web(shadow_edge_color));
                gc.fillRect(columnCell.get(count_column) + paddingAt60Deg,
                        topLeftCornerYForCurrentPlayerColorEdge
                        + (gridYSpace - TILE_EDGE_EFFECT_THICKNESS),
                        adjacentLengthAt60 * 2,
                        TILE_EDGE_EFFECT_THICKNESS);

                // Draw the left side of the edge (computer)
                gc.setFill(Color.web(light_edge_color_at_60_deg));
                gc.fillRect(topLeftCornerXForOtherPlayerColorEdge,
                        topLeftCornerYForOtherPlayerColorEdge,
                        tile_edge_width_at_60_half, gridYSpace);

                // Draw the right side of the edge (human)
                gc.setFill(Color.web(enemy_light_edge_color_at_60_deg));
                gc.fillRect(topLeftCornerXForCurrentPlayerColorEdge,
                        topLeftCornerYForCurrentPlayerColorEdge,
                        tile_edge_width_at_60_half, gridYSpace);

                break;

            case 2:
                /**
                 * A frame where the right tile edge is 30 degrees above the
                 * ground.
                 */

                // Clear tile first to remove previous paint
                resetTileColor(count_column, count_row);

                // Calculate the origin corner to draw for computer color edge
                topLeftCornerXForOtherPlayerColorEdge
                        = columnCell.get(count_column)
                        + (gridXSpace - paddingAt30Deg
                        - (tile_edge_width_at_30_half * 2));

                // Calculate the origin corner to draw for human color edge
                topLeftCornerXForCurrentPlayerColorEdge
                        = topLeftCornerXForOtherPlayerColorEdge
                        + tile_edge_width_at_30_half;

                // Draw the body of the tile (human)
                gc.setFill(Color.web(main_color));
                gc.fillRect(columnCell.get(count_column) + paddingAt30Deg,
                        topLeftCornerYForCurrentPlayerColorEdge
                        + TILE_EDGE_EFFECT_THICKNESS,
                        adjacentLengthAt30 * 2,
                        gridYSpace - TILE_EDGE_EFFECT_THICKNESS);

                // Draw the top edge (human)
                gc.setFill(Color.web(light_edge_color));
                gc.fillRect(columnCell.get(count_column) + paddingAt30Deg,
                        topLeftCornerYForCurrentPlayerColorEdge,
                        adjacentLengthAt30 * 2,
                        TILE_EDGE_EFFECT_THICKNESS);

                // Draw the shadow edge of the tile (human)
                gc.setFill(Color.web(shadow_edge_color));
                gc.fillRect(columnCell.get(count_column) + paddingAt30Deg,
                        topLeftCornerYForCurrentPlayerColorEdge
                        + (gridYSpace - TILE_EDGE_EFFECT_THICKNESS),
                        adjacentLengthAt30 * 2,
                        TILE_EDGE_EFFECT_THICKNESS);

                // Draw the left side of the edge (computer)
                gc.setFill(Color.web(light_edge_color_at_30_deg));
                gc.fillRect(topLeftCornerXForOtherPlayerColorEdge,
                        topLeftCornerYForOtherPlayerColorEdge,
                        tile_edge_width_at_30_half, gridYSpace);

                // Draw the right side of the edge (human)
                gc.setFill(Color.web(enemy_light_edge_color_at_30_deg));
                gc.fillRect(topLeftCornerXForCurrentPlayerColorEdge,
                        topLeftCornerYForCurrentPlayerColorEdge,
                        tile_edge_width_at_30_half, gridYSpace);

                break;

            case 1:
                /**
                 * The final frame in which only shows the current player side 
                 * of the tile, thus completing the flip animation.
                 */

                // Clear tile first to remove previous paint
                resetTileColor(count_column, count_row);

                graphics.paintTile(light_edge_color,
                        main_color,
                        shadow_edge_color,
                        count_column, count_row);
                break;
        }

        // switches to the next frame
        frame -= 1;
        
        return frame;
    }

    private void resetTileColor(int count_column, int count_row) {
        gc.clearRect(columnCell.get(count_column), rowCell.get(count_row),
                gridXSpace, gridYSpace);
    }

}
