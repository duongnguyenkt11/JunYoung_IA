package com.example.myapplication.object;

import com.example.myapplication.utils.StringUtil;

public class Food {

  private int id;
  private String foodName;
  private int portionSize;
  private int calories;
  private int checkBookmark;
  private String image;
  private String totalKcal;

  public Food(){}

  public Food(int id, String foodName, int portionSize, int calories, int checkBookmark, String image) {
    this.id = id;
    this.foodName = foodName;
    this.portionSize = portionSize;
    this.calories = calories;
    this.checkBookmark = checkBookmark;
    this.image = image;
  }

  public String getTotalKcal() {
    if (StringUtil.isNullOrEmpty(totalKcal)){
      return "0";
    }
    return totalKcal;
  }

  public void setTotalKcal(String totalKcal) {
    this.totalKcal = totalKcal;
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
     * @return the checkBookmark
     */
    public int getCheckBookmark() {
        return checkBookmark;
    }

    /**
     * @param checkBookmark the checkBookmark to set
     */
    public void setCheckBookmark(int checkBookmark) {
        this.checkBookmark = checkBookmark;
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
