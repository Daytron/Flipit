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
package com.github.daytron.flipit;

import com.github.daytron.flipit.core.Map;
import com.github.daytron.flipit.data.PlayerType;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
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
    private String mapPreviewImageSelected;
    private PlayerType player1;
    private PlayerType player2;
    private String player1ColorSelected;
    private String player2ColorSelected;
    private boolean isRunningInJar;

    public GamePreloader() {
        this.listOfMapNames = new ArrayList<>();
        this.listOfMapPath = new ArrayList<>();
        this.listOfMapObjects = new ArrayList<>();
        this.listOfMapPreviewImage = new ArrayList<>();
    }

    public void init() throws URISyntaxException {
        this.isRunningInJar = false;
        File file = new File(System.getProperty("user.dir") + 
                "/src/main/resources/maps");
        
        if (!file.exists()) {
            this.isRunningInJar = true;
            file = new File("./maps");
        }

        File[] files = file.listFiles();

        if (files != null) {
            for (File aFile : files) {
                String ext = aFile.getPath()
                        .substring(aFile.getPath().lastIndexOf(".") + 1);

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
        for (Map map : this.listOfMapObjects) {
            if (map.getName().contains(mapSelected)) {
                this.mapSelected = map;

                if (isRunningInJar) {
                    this.mapPreviewImageSelected = 
                            "file:./maps/" + map.getMapID() + ".png";
                } else {
                this.mapPreviewImageSelected = 
                        "file:src/main/resources/maps/" + map.getMapID() + ".png";
                }
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
