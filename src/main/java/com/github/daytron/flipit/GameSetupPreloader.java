/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ryan
 */
public class GameSetupPreloader {

    private final List<String> listOfMapPath;
    private final List<String> listOfMapNames;
    private final List<Map> listOfMapObjects;

    public GameSetupPreloader() {
        this.listOfMapNames = new ArrayList<>();
        this.listOfMapPath = new ArrayList<>();
        this.listOfMapObjects = new ArrayList<>();
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
            }
        }

        for (String jsonFilePath : listOfMapPath) {
            extractMapNames(jsonFilePath);
        }
        
    }
    
    public List<String> getMapNames() {
        return listOfMapNames;
    }
    
    public List<Map> getMapObjects() {
        return listOfMapObjects;
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
            Logger.getLogger(GameSetupPreloader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
}
