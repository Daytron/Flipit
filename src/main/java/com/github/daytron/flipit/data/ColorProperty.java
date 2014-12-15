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
package com.github.daytron.flipit.data;

/**
 *
 * @author Ryan Gilera ryangilera@gmail.com
 */
public enum ColorProperty {
    // http://www.hexcolortool.com
    // HSL (207, 100, 41)
    PLAYER_BLUE("#0073CF"),
    // HSL (207, 100, 49)
    PLAYER_BLUE_LIGHT_EDGE_AT_30_DEG("#0089FA"),
    // HSL (207, 100, 57)
    PLAYER_BLUE_LIGHT_EDGE_AT_60_DEG("#249CFF"),
    // HSL (207, 100, 65)
    PLAYER_BLUE_LIGHT_EDGE("#4FB1FF"),
    // HSL (207, 100, 25)
    PLAYER_BLUE_SHADOW_EDGE("#004881"),
    
    // HSL (0, 100, 41)
    PLAYER_RED("#CF0000"),
    // HSL (0, 100, 49)
    PLAYER_RED_LIGHT_EDGE_AT_30_DEG("#FA0000"),
    // HSL (0, 100, 57)
    PLAYER_RED_LIGHT_EDGE_AT_60_DEG("#FF2424"),
    // HSL (0, 100, 65)
    PLAYER_RED_LIGHT_EDGE("#FF4D4D"),
    // HSL (0, 100, 25)
    PLAYER_RED_SHADOW_EDGE("#800000"),
    
    TILE_POSSIBLE_MOVE_HIGHLIGHT_LiGHT_EDGE("#EDFFF7"),
    TILE_POSSIBLE_MOVE_HIGHLIGHT_MAIN("#ABFFD8"),
    TILE_POSSIBLE_MOVE_HIGHLIGHT_SHADOW_EDGE("#8BD6B3"),
    
    TILE_POSSIBLE_ATTACK_HIGHLIGHT_LiGHT_EDGE("#FFEDED"),
    TILE_POSSIBLE_ATTACK_HIGHLIGHT_MAIN("#FFABAB"),
    TILE_POSSIBLE_ATTACK_HIGHLIGHT_SHADOW_EDGE("#D68B8B"),
    
    TILE_NEUTRAL_LIGHT_EDGE("#FFFFFF"),
    TILE_NEUTRAL_MAIN("#C6C6C6"),
    TILE_NEUTRAL_SHADOW_EDGE("#000000"),

    TILE_BOULDER_LIGHT_EDGE("#FFFFFF"),
    TILE_BOULDER_MAIN("#4F4F4F"),
    TILE_BOULDER_SHADOW_EDGE("#000000"),
    
    TILE_WHITE("#FFFFFF"),
    
    // SCORE
    SCORE_LABEL_BORDER("#000000"),
    SCORE_LABEL_FILL("#5C5C5C")
    ;
    
    private final String color;

    private ColorProperty(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
