/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit;

/**
 *
 * @author ryan
 */
import java.util.List;

public class Map {

    String name;
    private List<Integer[]> listOfCoordinates;

    //getter and setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer[]> getListOfCoordinates() {
        return listOfCoordinates;
    }

    public void setListOfCoordinates(List<Integer[]> listOfPos) {
        this.listOfCoordinates = listOfPos;
    }

}
