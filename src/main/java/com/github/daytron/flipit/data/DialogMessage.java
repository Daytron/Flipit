/*
 * The MIT License
 *
 * Copyright 2014 Ryan Gilera.
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
 * @author Ryan Gilera
 */
public enum DialogMessage {
    CONFIRMATION_HEAD_TITLE("Confirmation Dialog"),    
    CONFIRMATION_TITLE("Confirm Action"),
    CONFIRM_EXIT("Are you sure you want to exit?"),
    CONFIRM_NEW_GAME("You are about to leave this game.\n"
            + "Are you sure you want a new game?"),
    CONFIRM_RESTART("You are about to leave this game.\n"
            + "Are you sure you want to restart the game?"),
    
    ERROR_HEAD_TITLE("Error Dialog"),
    ERROR_TITLE("Error"),
    ERROR_NO_MAPS_FOUND("No available maps found."
            + "Map files are missing in the maps folder.");
    
    
    
    private String text;

    private DialogMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
    
}
