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
package com.github.daytron.flipit.core;

import java.util.List;

public class Map {

    private String mapID;
    private String name;
    private int[] size;
    private int numOfPlayers;

    private int[] listOfPlayer1StartPosition;
    private int[] listOfPlayer2StartPosition;
    private List<Integer[]> listOfBoulders;

    public void setMapID(String mapID) {
        this.mapID = mapID;
    }

    public String getMapID() {
        return mapID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSize(int[] size) {
        this.size = size;
    }

    public int[] getSize() {
        return this.size;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setListOfBoulders(List<Integer[]> listOfBoulders) {
        this.listOfBoulders = listOfBoulders;
    }

    public List<Integer[]> getListOfBoulders() {
        return listOfBoulders;
    }

    public void setListOfPlayer1StartPosition(int[] listOfPlayer1StartPosition) {
        this.listOfPlayer1StartPosition = listOfPlayer1StartPosition;
    }

    public int[] getListOfPlayer1StartPosition() {
        return listOfPlayer1StartPosition;
    }

    public void setListOfPlayer2StartPosition(int[] listOfPlayer2StartPosition) {
        this.listOfPlayer2StartPosition = listOfPlayer2StartPosition;
    }

    public int[] getListOfPlayer2StartPosition() {
        return listOfPlayer2StartPosition;
    }

}
