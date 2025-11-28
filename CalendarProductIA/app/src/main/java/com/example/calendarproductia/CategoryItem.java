package com.example.calendarproductia;

public class CategoryItem {
    public String CategoryName;
    public int CategoryImage;

    public CategoryItem(String categoryName, int categoryImage){
        CategoryName= categoryName;
        CategoryImage = categoryImage;

    }

    public String getCategoryName(){
        return CategoryName;
    }
    public int getCategoryImage(){
        return CategoryImage;
    }
}
