package cn.itcast.travel.dao.impl;

import cn.itcast.travel.domain.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> findAll();

    Category findAll(Category category);
}
