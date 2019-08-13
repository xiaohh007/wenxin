package com.wenxin.item.service;


import com.wenxin.item.item.pojo.Category;

import java.util.List;


public interface CategoryService {


    List<Category> queryByParentId(Long pid);

    void saveCategoryChild(Category category);

    void deleteCategoryChild(Long id);

    void updateCategoryChild(Category category);

    List<Category> queryLast();

}
