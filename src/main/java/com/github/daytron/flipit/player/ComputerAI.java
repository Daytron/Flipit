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
package com.github.daytron.flipit.player;

import com.github.daytron.flipit.data.Difficulty;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ryan
 */
public class ComputerAI {

    private final Difficulty computerType;
    private List<Integer[]> possibleTileMoves;
    private List<Integer[]> possibleTileAttacks;

    private int[] chosenTilePlay;

    public ComputerAI(Difficulty type) {
        this.computerType = type;
    }

    /**
     *
     * @param possibleMoves
     * @param possibleAttacks
     * @return
     */
    public int[] play(List<Integer[]> possibleMoves, List<Integer[]> possibleAttacks) {
        this.chosenTilePlay = new int[2];

        this.possibleTileMoves = new ArrayList<>(possibleMoves);
        this.possibleTileAttacks = new ArrayList<>(possibleAttacks);

        switch (this.computerType) {
            case EASY:
                this.easyPlay();
                break;

            case NORMAL:
                this.normalPlay();
                break;

            case HARD:
                this.hardPlay();
                break;

        }

        return chosenTilePlay;
    }

    private void easyPlay() {
        Random randomGenerator = new Random();

        // 0 for move, 1 for attack
        if (randomGenerator.nextInt(2) == 1) {
            if (this.possibleTileAttacks.isEmpty()) {
                // occupy
                if (!this.possibleTileMoves.isEmpty()) {
                    Integer[] temp = this.possibleTileMoves
                        .get(randomGenerator.nextInt(
                            this.possibleTileMoves.size())).clone();

                    this.chosenTilePlay[0] = temp[0];
                    this.chosenTilePlay[1] = temp[1];
                }

            } else {
                // attack
                Integer[] temp = this.possibleTileAttacks
                        .get(randomGenerator.nextInt(
                            this.possibleTileAttacks.size())).clone();

                this.chosenTilePlay[0] = temp[0];
                this.chosenTilePlay[1] = temp[1];
            }
        } else {
            if (this.possibleTileMoves.isEmpty()) {
                // attack
                if (!this.possibleTileAttacks.isEmpty()) {
                    Integer[] temp = this.possibleTileAttacks
                        .get(randomGenerator.nextInt(
                            this.possibleTileAttacks.size())).clone();

                    this.chosenTilePlay[0] = temp[0];
                    this.chosenTilePlay[1] = temp[1];
                }
            } else {
                // occupy
                Integer[] temp = this.possibleTileMoves
                        .get(randomGenerator.nextInt(
                            this.possibleTileMoves.size())).clone();

                this.chosenTilePlay[0] = temp[0];
                this.chosenTilePlay[1] = temp[1];
            }
        }
    }

    private void normalPlay() {

    }

    private void hardPlay() {

    }
}
