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
import com.github.daytron.flipit.data.PlayerType;
import com.github.daytron.flipit.data.ColorProperty;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 *
 * @author Ryan Gilera ryangilera@gmail.com
 */
final class Graphics {

    private final Canvas canvas;
    private final GraphicsContext gc;

    private final double gridXSpace;
    private final double gridYSpace;

    private final List<Double> rowCell;
    private final List<Double> columnCell;

    private final int TILE_EDGE_EFFECT_THICKNESS;

    private int frameCount;

    private double turnStatusPosX;
    private double turnStatusPosY;

    // Labels positions
    private double scoreLabelHeight;

    private double leftScoreLabelPosX;
    private double leftScoreLabelPosY;
    private double leftScoreLabelPosYForClear;
    private double leftTurnLeftLabelPosX;
    private double leftTurnLeftLabelPosY;
    private double leftTurnLeftLabelPosYForClear;
    private double leftPlayerLabelPosX;
    private double leftPlayerLabelPosY;

    private double rightScoreLabelPosX;
    private double rightScoreLabelPosY;
    private double rightScoreLabelPosYForClear;
    private double rightTurnLeftLabelPosX;
    private double rightTurnLeftLabelPosY;
    private double rightTurnLeftLabelPosYForClear;
    private double rightPlayerLabelPosX;
    private double rightPlayerLabelPosY;

    private PlayerType leftPlayer;
    private PlayerType rightPlayer;

    protected Graphics(Canvas canvas, double gridX, double gridY,
            List<Double> rowCell, List<Double> columnCell,
            int tile_edge) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.gc.setLineWidth(2);

        this.gridXSpace = gridX;
        this.gridYSpace = gridY;
        this.rowCell = rowCell;
        this.columnCell = columnCell;
        this.TILE_EDGE_EFFECT_THICKNESS = tile_edge;

        // Calculate position
        this.turnStatusPosX = this.canvas.getWidth()
                - MapProperty.TURN_STATUS_LABEL_POSX.getValue();
        this.turnStatusPosY = this.canvas.getHeight()
                - MapProperty.TURN_STATUS_LABEL_POSY.getValue();

        // For score labels
        this.scoreLabelHeight = MapProperty.MAP_TOP_PADDING.getValue() * 0.6;
        double topPadding = MapProperty.MAP_TOP_PADDING.getValue() * 0.2;

        // Calculate computer player score label positions
        this.rightScoreLabelPosX = this.canvas.getWidth() - 114;
        this.rightScoreLabelPosY = topPadding + this.scoreLabelHeight;
        this.rightScoreLabelPosYForClear = topPadding;
        this.rightTurnLeftLabelPosX = this.rightScoreLabelPosX - 30;
        this.rightTurnLeftLabelPosY = topPadding + 27;
        this.rightTurnLeftLabelPosYForClear = topPadding;
        this.rightPlayerLabelPosX = this.rightScoreLabelPosX - 49;
        this.rightPlayerLabelPosY = topPadding + 48;

        // Calculate human player score label positions
        this.leftScoreLabelPosX = MapProperty.MAP_PADDING.getValue();
        this.leftScoreLabelPosY = this.rightScoreLabelPosY;
        this.leftScoreLabelPosYForClear = topPadding;
        this.leftTurnLeftLabelPosX = this.leftScoreLabelPosX + 97;
        this.leftTurnLeftLabelPosY = topPadding + 27;
        this.leftTurnLeftLabelPosYForClear = topPadding;
        this.leftPlayerLabelPosX = this.leftTurnLeftLabelPosX + 2;
        this.leftPlayerLabelPosY = topPadding + 48;
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

    protected void flipTile(final ColorProperty playerColor,
            final int count_column, final int count_row) {

        FlipAnimation flipAnimation = new FlipAnimation(frameCount,
                gridXSpace, gridYSpace, rowCell, columnCell,
                TILE_EDGE_EFFECT_THICKNESS, gc, this);
        flipAnimation.flipTile(playerColor, count_column, count_row);
    }

    protected void displayTurnStatus(PlayerType player) {
        // Clear any previous status label
        clearTurnStatus();

        // Get previous line width
        double oldLineWidth = this.gc.getLineWidth();
        // Get a reference to the current font, to be retained later 
        Font oldFont = this.gc.getFont();

        //Set new line width
        this.gc.setLineWidth(0.8);
        // Set a new font
        this.gc.setFont(Font.font("Helvetica",
                MapProperty.TURN_STATUS_LABEL_FONT_SIZE.getValue()));

        String textLabel;
        if (player == PlayerType.HUMAN) {
            textLabel = "Your turn";
        } else {
            textLabel = "Enemy turn";
        }

        // Draw the label text
        this.gc.strokeText(textLabel, this.turnStatusPosX, this.turnStatusPosY);

        // Return previous line width
        this.gc.setLineWidth(oldLineWidth);
        // Return previous font
        this.gc.setFont(oldFont);
    }

    protected void clearTurnStatus() {
        // Clears the previous text
        this.gc.clearRect(this.turnStatusPosX, this.turnStatusPosY
                - MapProperty.TURN_STATUS_LABEL_FONT_SIZE.getValue(),
                MapProperty.TURN_STATUS_LABEL_POSX.getValue(),
                MapProperty.TURN_STATUS_LABEL_POSY.getValue() * 3);
    }

    protected void clearCanvas() {
        this.gc.clearRect(0, 0,
                this.canvas.getWidth(),
                this.canvas.getHeight());
    }

    protected void drawScoreLabel(PlayerType leftPlayer, PlayerType righPlayer) {
        // Set left and right players of their score label position
        this.leftPlayer = leftPlayer;
        this.rightPlayer = righPlayer;

        // Get previous line width
        double oldLineWidth = this.gc.getLineWidth();
        // Get a reference to the current font, to be retained later 
        Font oldFont = this.gc.getFont();
        // Get previous paint fill
        Paint oldFillPaint = this.gc.getFill();
        // Get previous paint stroke
        Paint oldStrokePaint = this.gc.getStroke();

        //Set new line width
        this.gc.setStroke(Color.web(ColorProperty.SCORE_LABEL_BORDER.getColor()));
        this.gc.setLineWidth(2);
        this.gc.setFill(Color.web(ColorProperty.SCORE_LABEL_FILL.getColor()));

        // Set font for score label
        this.gc.setFont(Font.font("Helvetica", 50));

        this.gc.strokeText("000", this.leftScoreLabelPosX,
                this.leftScoreLabelPosY);
        this.gc.fillText("000", this.leftScoreLabelPosX,
                this.leftScoreLabelPosY);

        this.gc.strokeText("000", this.rightScoreLabelPosX,
                this.rightScoreLabelPosY);
        this.gc.fillText("000", this.rightScoreLabelPosX,
                this.rightScoreLabelPosY);

        // Set font and line width for turn left label
        this.gc.setFont(Font.font("Helvetica", 22));
        this.gc.setLineWidth(1);

        this.gc.strokeText("00", this.leftTurnLeftLabelPosX,
                this.leftTurnLeftLabelPosY);
        this.gc.fillText("00", this.leftTurnLeftLabelPosX,
                this.leftTurnLeftLabelPosY);

        this.gc.strokeText("00", this.rightTurnLeftLabelPosX,
                this.rightTurnLeftLabelPosY);
        this.gc.fillText("00", this.rightTurnLeftLabelPosX,
                this.rightTurnLeftLabelPosY);

        String playerLeftLabelString;
        String playerRightLabelString;

        if (this.leftPlayer == PlayerType.HUMAN
                && this.rightPlayer == PlayerType.COMPUTER) {
            playerLeftLabelString = "YOU";
            playerRightLabelString = "COM";
        } else {
            playerLeftLabelString = "COM";
            playerRightLabelString = "YOU";
        }

        this.gc.setFont(Font.font("Helvetica", 20));
        this.gc.strokeText(playerLeftLabelString, this.leftPlayerLabelPosX,
                this.leftPlayerLabelPosY);
        this.gc.fillText(playerLeftLabelString, this.leftPlayerLabelPosX,
                this.leftPlayerLabelPosY);

        this.gc.strokeText(playerRightLabelString, this.rightPlayerLabelPosX,
                this.rightPlayerLabelPosY);
        this.gc.fillText(playerRightLabelString, this.rightPlayerLabelPosX,
                this.rightPlayerLabelPosY);

        // Return previous line width
        this.gc.setLineWidth(oldLineWidth);
        // Return previous font
        this.gc.setFont(oldFont);
        // Return old fill paint
        this.gc.setFill(oldFillPaint);
        // Return old stroke paint
        this.gc.setStroke(oldStrokePaint);
    }

    public Graphics() {
        this.canvas = null;
        this.gc = null;
        this.gridXSpace = 0;
        this.gridYSpace = 0;
        this.rowCell = null;
        this.columnCell = null;
        this.TILE_EDGE_EFFECT_THICKNESS = 0;
    }

    protected void drawScore(int newScore, int turnLeft,
            PlayerType playerToScore) {
        // Get previous line width
        double oldLineWidth = this.gc.getLineWidth();
        // Get a reference to the current font, to be retained later 
        Font oldFont = this.gc.getFont();
        // Get previous paint fill
        Paint oldFillPaint = this.gc.getFill();
        // Get previous paint stroke
        Paint oldStrokePaint = this.gc.getStroke();

        //Set new line width
        this.gc.setStroke(Color.web(ColorProperty.SCORE_LABEL_BORDER.getColor()));
        this.gc.setLineWidth(2);
        this.gc.setFill(Color.web(ColorProperty.SCORE_LABEL_FILL.getColor()));

        // Set font for score label
        this.gc.setFont(Font.font("Helvetica", 50));

        // Reformat new score to appropriate text;
        String scoreLabel;
        if (newScore < 10) {
            scoreLabel = "00" + Integer.toString(newScore);
        } else if (newScore < 100) {
            scoreLabel = "0" + Integer.toString(newScore);
        } else {
            scoreLabel = Integer.toString(newScore);
        }

        String turnLeftLabel;
        if (turnLeft < 10) {
            turnLeftLabel = "0" + Integer.toString(turnLeft);
        } else {
            turnLeftLabel = Integer.toString(turnLeft);
        }

        if (playerToScore == this.leftPlayer) {
            this.clearLeftScoreTurnLeft();

            this.gc.strokeText(scoreLabel, this.leftScoreLabelPosX,
                    this.leftScoreLabelPosY);
            this.gc.fillText(scoreLabel, this.leftScoreLabelPosX,
                    this.leftScoreLabelPosY);

            // Set font and line width for turn left label
            this.gc.setFont(Font.font("Helvetica", 22));
            this.gc.setLineWidth(1);

            this.gc.strokeText(turnLeftLabel, this.leftTurnLeftLabelPosX,
                    this.leftTurnLeftLabelPosY);
            this.gc.fillText(turnLeftLabel, this.leftTurnLeftLabelPosX,
                    this.leftTurnLeftLabelPosY);
        } else {
            this.clearRightScoreTurnLeft();

            this.gc.strokeText(scoreLabel, this.rightScoreLabelPosX,
                    this.rightScoreLabelPosY);
            this.gc.fillText(scoreLabel, this.rightScoreLabelPosX,
                    this.rightScoreLabelPosY);

            // Set font and line width for turn left label
            this.gc.setFont(Font.font("Helvetica", 22));
            this.gc.setLineWidth(1);

            this.gc.strokeText(turnLeftLabel, this.rightTurnLeftLabelPosX,
                    this.rightTurnLeftLabelPosY);
            this.gc.fillText(turnLeftLabel, this.rightTurnLeftLabelPosX,
                    this.rightTurnLeftLabelPosY);
        }

        // Return previous line width
        this.gc.setLineWidth(oldLineWidth);
        // Return previous font
        this.gc.setFont(oldFont);
        // Return old fill paint
        this.gc.setFill(oldFillPaint);
        // Return old stroke paint
        this.gc.setStroke(oldStrokePaint);
    }

    protected void clearLeftScoreTurnLeft() {
        // Clear score
        this.gc.clearRect(this.leftScoreLabelPosX,
                this.leftScoreLabelPosYForClear,
                96, this.scoreLabelHeight + 10);

        // Clear turn left counter
        this.gc.clearRect(this.leftTurnLeftLabelPosX,
                this.leftTurnLeftLabelPosYForClear,
                60, 30);
    }

    protected void clearRightScoreTurnLeft() {
        // Clear score
        this.gc.clearRect(this.rightScoreLabelPosX,
                this.rightScoreLabelPosYForClear,
                96, this.scoreLabelHeight + 10);

        // Clear turn left counter
        this.gc.clearRect(this.rightTurnLeftLabelPosX,
                this.rightTurnLeftLabelPosYForClear,
                60, 30);
    }
}
