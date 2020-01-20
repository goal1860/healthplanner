package com.app.dietplan.Item;

import java.io.Serializable;

public class SubCategoryList implements Serializable {

    private String id,cat_id,diet_title,diet_info,diet_image;

    public SubCategoryList(String id, String cat_id, String diet_title, String diet_info, String diet_image) {
        this.id = id;
        this.cat_id = cat_id;
        this.diet_title = diet_title;
        this.diet_info = diet_info;
        this.diet_image = diet_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getDiet_title() {
        return diet_title;
    }

    public void setDiet_title(String diet_title) {
        this.diet_title = diet_title;
    }

    public String getDiet_info() {
        return diet_info;
    }

    public void setDiet_info(String diet_info) {
        this.diet_info = diet_info;
    }

    public String getDiet_image() {
        return diet_image;
    }

    public void setDiet_image(String diet_image) {
        this.diet_image = diet_image;
    }

}
