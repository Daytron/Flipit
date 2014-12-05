/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit;

import com.github.daytron.flipit.core.Map;
import com.github.daytron.flipit.data.PlayerType;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ryan
 */
public class GamePreloader {

    // pre game variables
    private final List<String> listOfMapPath;
    private final List<String> listOfMapNames;
    private final List<Map> listOfMapObjects;
    private final List<String> listOfMapPreviewImage;
    
    // new game variables
    private Map mapSelected;
    private int playerSelected;
    private String mapPreviewImageSelected;
    private PlayerType player1;
    private PlayerType player2;
    private String player1ColorSelected;
    private String player2ColorSelected;

    public GamePreloader() {
        this.listOfMapNames = new ArrayList<>();
        this.listOfMapPath = new ArrayList<>();
        this.listOfMapObjects = new ArrayList<>();
        this.listOfMapPreviewImage = new ArrayList<>();
    }

    
    
    public void init() {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/maps");
        File[] files = file.listFiles();

        if (files != null) {
            for (File aFile : files) {
                String ext = aFile.getPath().substring(aFile.getPath().lastIndexOf(".") + 1);

                if (aFile.isFile() && "json".equals(ext)) {
                    listOfMapPath.add(aFile.getPath());
                }
                
                if (aFile.isFile() && "png".equals(ext)) {
                    listOfMapPreviewImage.add(aFile.getPath());
                }
            }
        }

        for (String jsonFilePath : listOfMapPath) {
            extractMapNames(jsonFilePath);
        }
        
    }

    public void setPlayer1(PlayerType player1) {
        this.player1 = player1;
    }

    public void setPlayer2(PlayerType player2) {
        this.player2 = player2;
    }

    public PlayerType getPlayer1() {
        return player1;
    }

    public PlayerType getPlayer2() {
        return player2;
    }

    public void setPlayer1ColorSelected(String player1ColorSelected) {
        this.player1ColorSelected = player1ColorSelected;
    }

    public String getPlayer1ColorSelected() {
        return player1ColorSelected;
    }

    public void setPlayer2ColorSelected(String player2ColorSelected) {
        this.player2ColorSelected = player2ColorSelected;
    }

    public String getPlayer2ColorSelected() {
        return player2ColorSelected;
    }
    
    

    public void setMapSelected(String mapSelected) {
        for (Map map: this.listOfMapObjects) {
            if (map.getName().contains(mapSelected)) {
                this.mapSelected = map;
                
                this.mapPreviewImageSelected = "file:src/main/resources/maps/" + map.getMapID() + ".png";
            }
        }
    
    }

    public String getMapPreviewImageSelected() {
        return mapPreviewImageSelected;
    }
    
    

    public Map getMapSelected() {
        return this.mapSelected;
    }

    // TEMP ONLY - MODIFY LATER
    public int getPlayerSelected() {
        return 1;
    }
    
    
    
    public List<String> getMapNames() {
        return listOfMapNames;
    }
    
    public List<Map> getMapObjects() {
        return listOfMapObjects;
    }

    public List<String> getListOfMapPreviewImage() {
        return listOfMapPreviewImage;
    }
    
    
    
    public boolean isMapEmpty() {
        return this.listOfMapPath.isEmpty();
    }

    private void extractMapNames(String file_location) {
        Gson gson = new Gson();
        
        try {
            BufferedReader br;

            br = new BufferedReader(
                    new FileReader(file_location));

            // Convert the Json file back to object
            Map aMap = gson.fromJson(br, Map.class);
            
            this.listOfMapObjects.add(aMap);
            this.listOfMapNames.add(aMap.getName());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GamePreloader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
}