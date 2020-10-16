/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.myapplication.object;

import java.sql.Date;


public class History {
    private int id;
    private String activitiesName;
    private String username;
    private String calories;
    private String minute;
    private String trainningDate;
    private int turn;

    public History () {
    }
    
    public History (int id, String activitiesName, String username, String calories, String minute, String trainningDate, int turn) {
        this.id = id;
        this.activitiesName = activitiesName;
        this.username = username;
        this.calories = calories;
        this.minute = minute;
        this.trainningDate = trainningDate;
        this.turn = turn;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the activitiesName
     */
    public String getActivitiesName() {
        return activitiesName;
    }

    /**
     * @param activitiesName the activitiesName to set
     */
    public void setActivitiesName(String activitiesName) {
        this.activitiesName = activitiesName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the minute
     */
    public String getMinute() {
        return minute;
    }

    /**
     * @param minute the minute to set
     */
    public void setMinute(String minute) {
        this.minute = minute;
    }
    
     /**
     * @return the calories
     */
    public String getCalories() {
        return calories;
    }

    /**
     * @param calories the calories to set
     */
    public void setCalories(String calories) {
        this.calories = calories;
    }

    /**
     * @return the trainningDate
     */
    public String getTrainningDate() {
        return trainningDate;
    }

    /**
     * @param trainningDate the trainningDate to set
     */
    public void setTrainningDate(String trainningDate) {
        this.trainningDate = trainningDate;
    }
    
         /**
     * @return the turn
     */
    public int getTurn() {
        return turn;
    }

    /**
     * @param turn the turn to set
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }
}
