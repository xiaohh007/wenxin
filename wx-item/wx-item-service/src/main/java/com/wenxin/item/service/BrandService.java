package com.wenxin.item.service;

import com.wenxin.common.pojo.PageResult;
import com.wenxin.item.item.pojo.Brand;

import java.util.List;

public interface BrandService {
    PageResult<Brand> queryBrandPage(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    void addBrand(Brand brand, List<Long> cids);

    void updateBrand(Brand brand, List<Long> cids);

    void deleteBrandById(Long bid);
}
