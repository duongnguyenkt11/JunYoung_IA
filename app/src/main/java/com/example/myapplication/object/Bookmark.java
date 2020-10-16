package com.example.myapplication.object;

public class Bookmark {

    private int id;
    private int foodId;
    private String username;
    private String foodName;
    private int portionSize;
    private int calories;
    private int quantity;

    public Bookmark() {

    }

    public Bookmark(int id, int foodId, String username, String foodName, int portionSize, int calories, int quantity) {
        this.id = id;
        this.foodId = foodId;
        this.username = username;
        this.foodName = foodName;
        this.portionSize = portionSize;
        this.calories = calories;
        this.quantity = quantity;
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
     * @return the foodId
     */
    public int getFoodId() {
        return foodId;
    }

    /**
     * @param foodId the foodId to set
     */
    public void setFoodId(int foodId) {
        this.foodId = foodId;
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
     * @return the foodName
     */
    public String getFoodName() {
        return foodName;
    }

    /**
     * @param foodName the foodName to set
     */
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    /**
     * @return the portionSize
     */
    public int getPortionSize() {
        return portionSize;
    }

    /**
     * @param portionSize the portionSize to set
     */
    public void setPortionSize(int portionSize) {
        this.portionSize = portionSize;
    }

    /**
     * @return the calories
     */
    public int getCalories() {
        return calories;
    }

    /**
     * @param calories the calories to set
     */
    public void setCalories(int calories) {
        this.calories = calories;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

  
}
