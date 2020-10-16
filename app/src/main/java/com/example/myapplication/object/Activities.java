package com.example.myapplication.object;

public class Activities {
    private int id;
    private String activitiesName;
    private int baseCalorie;
    private String image;

    public Activities() {

    }

    public Activities(int id, String activitiesName, int baseCalorie, String image) {
        this.id = id;
        this.activitiesName = activitiesName;
        this.baseCalorie = baseCalorie;
        this.image = image;
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
     * @return the baseCalorie
     */
    public int getBaseCalorie() {
        return baseCalorie;
    }

    /**
     * @param baseCalorie the baseCalorie to set
     */
    public void setBaseCalorie(int baseCalorie) {
        this.baseCalorie = baseCalorie;
    }
    
     /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }
}
