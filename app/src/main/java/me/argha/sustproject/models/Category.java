package me.argha.sustproject.models;

import java.util.ArrayList;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public class Category {

    private String enName;
    private String bnName;
    private ArrayList<Category> subCategory;

    public String getEnName() {
        return enName;
    }

    public String getBnName() {
        return bnName;
    }

    public ArrayList<Category> getSubCategory() {
        return subCategory;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public void setBnName(String bnName) {
        this.bnName = bnName;
    }

    public void setSubCategory(ArrayList<Category> subCategory) {
        this.subCategory = subCategory;
    }

    @Override
    public String toString() {
        return getBnName();
    }
}
