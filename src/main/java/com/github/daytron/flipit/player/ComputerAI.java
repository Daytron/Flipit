/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
