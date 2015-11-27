package me.argha.sustproject.models;

/**
 * Author: ARGHA K ROY
 * Date: 11/28/2015.
 */
public class Item {

    private String itemId;
    private String name;
    private String description;
    private String main_category;
    private String sub_category;
    private String quantity;
    private String unit;
    private String range_min;
    private String range_max;
    private String expire_date;
    private String rating;
    private String photo;

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMain_category() {
        return main_category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getRange_min() {
        return range_min;
    }

    public String getRange_max() {
        return range_max;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMain_category(String main_category) {
        this.main_category = main_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setRange_min(String range_min) {
        this.range_min = range_min;
    }

    public void setRange_max(String range_max) {
        this.range_max = range_max;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
