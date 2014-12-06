/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.utility;

/**
 *
 * @author ryan
 */
public final class GlobalSettings {

    private GlobalSettings() {
    }
    
    
    public static final String PLAYER_COLOR_BLUE = "#0073CF";
    public static final String PLAYER_COLOR_BLUE_LIGHT_EDGE = "#4FB1FF";
    public static final String PLAYER_COLOR_BLUE_SHADOW_EDGE = "#004881";
    
    public static final String PLAYER_COLOR_RED = "#CF0000";
    public static final String PLAYER_COLOR_RED_LIGHT_EDGE = "#FF4F4F";
    public static final String PLAYER_COLOR_RED_SHADOW_EDGE = "#810000";
    
    public static final String TILE_POSSIBLE_MOVE_HIGHLIGHT_LiGHT_EDGE_COLOR = "#EDFFF7";
    public static final String TILE_POSSIBLE_MOVE_HIGHLIGHT_MAIN_COLOR = "#ABFFD8";
    public static final String TILE_POSSIBLE_MOVE_HIGHLIGHT_SHADOW_EDGE_COLOR = "#8BD6B3";
    
    public static final String TILE_POSSIBLE_ATTACK_HIGHLIGHT_LiGHT_EDGE_COLOR = "#FFEDED";
    public static final String TILE_POSSIBLE_ATTACK_HIGHLIGHT_MAIN_COLOR = "#FFABAB";
    public static final String TILE_POSSIBLE_ATTACK_HIGHLIGHT_SHADOW_EDGE_COLOR = "#D68B8B";
    
    public final static String TILE_NEUTRAL_LIGHT_EDGE_COLOR = "#FFFFFF";
    public final static String TILE_NEUTRAL_MAIN_COLOR = "#C6C6C6";
    public final static String TILE_NEUTRAL_SHADOW_EDGE_COLOR = "#000000";

    public final static String TILE_BOULDER_LIGHT_EDGE_COLOR = "#FFFFFF";
    public final static String TILE_BOULDER_MAIN_COLOR = "#4F4F4F";
    public final static String TILE_BOULDER_SHADOW_EDGE_COLOR = "#000000"; 
    
    public static final String PLAYER_OPTION_HUMAN = "Human";
    public static final String PLAYER_OPTION_COMPUTER = "Computer";
    
    // Attacking Tile direction
    public static final int DIRECTION_TOP_LEFT = 1;
    public static final int DIRECTION_TOP_RIGHT = 2;
    public static final int DIRECTION_LOWER_LEFT = 3;
    public static final int DIRECTION_LOWER_RIGHT = 4;
    
    // SCORING
    public static final int SCORE_ONE_TILE_OCCUPY = 1;
    public static final int SCORE_CHECKMATE = 10;
    
    
    // AI
    public static final int COMPUTER_EASY = 1;
    public static final int COMPUTER_NORMAL = 2;
    public static final int COMPUTER_HARD = 3;
}
