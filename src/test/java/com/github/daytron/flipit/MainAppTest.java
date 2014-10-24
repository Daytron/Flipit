/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit;

import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author ryan
 */
public class MainAppTest {
    
    public MainAppTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class MainApp.
     */
    @Test
    @Ignore
    public void testGetInstance() {
        System.out.println("getInstance");
        MainApp instance = new MainApp();
        MainApp expResult = null;
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of start method, of class MainApp.
     */
    @Test
    @Ignore
    public void testStart() throws Exception {
        System.out.println("start");
        Stage primary_stage = null;
        MainApp instance = new MainApp();
        instance.start(primary_stage);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class MainApp.
     */
    @Test
    @Ignore
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        MainApp.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of viewNewGameSetup method, of class MainApp.
     */
    @Test
    @Ignore
    public void testViewNewGameSetup() {
        System.out.println("clickNewGame");
        MainApp instance = new MainApp();
        instance.viewNewGameSetup();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of viewMainGame method, of class MainApp.
     */
    @Test
    @Ignore
    public void testViewMainGame() {
        System.out.println("clickStartGame");
        MainApp instance = new MainApp();
        instance.viewMainGame();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
