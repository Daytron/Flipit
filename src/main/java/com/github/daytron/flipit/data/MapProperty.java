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
public enum MapProperty {
    MAP_PADDING(20),
    MAP_TOP_PADDING(80),
    TILE_EDGE_WIDTH_AT_30_DEG_FOR_EACH_PLAYER_COLOR_AREA(3),
    TILE_EDGE_WIDTH_AT_60_DEG_FOR_EACH_PLAYER_COLOR_AREA(5),
    TILE_EDGE_WIDTH(6),
    TILE_EDGE_HALF_WIDTH(3),
    TILE_EDGE_EFFECT_SMALL(1),
    TILE_EDGE_EFFECT_LARGE(2),
    NUMBER_OF_TILE_ANIMATION_FRAMES(6),
    FLIP_FRAME_DURATION(50),
    COM_PLAY_DELAY(400);

    private final int value;

    private MapProperty(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
