package com.wenxin.item.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenxin.common.pojo.PageResult;
import com.wenxin.item.item.pojo.Brand;
import com.wenxin.item.mapper.BrandMapper;
import com.wenxin.item.service.BrandService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Transactional
    @Override
    public PageResult<Brand> queryBrandPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //1.分页查询
        PageHelper.startPage(page, rows);
        //2.过滤查询
        Example example = new Example(Brand.class);

        //2.1排序查询
        if (StringUtils.isNoneBlank(sortBy)) {
            //排序
            String orderBy = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderBy);
        }
        //2.2过滤查询
        if (StringUtils.isNoneBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        List<Brand> list = this.brandMapper.selectByExample(example);
        //3创建pageInfo
        PageInfo<Brand> pageInfo = new PageInfo<>(list);
        //返回分页结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void addBrand(Brand brand, List<Long> cids) {
        //新增品牌表
        this.brandMapper.insert(brand);

        //新增品牌和分类中间表
        for (Long cid : cids) {
            this.brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    @Transactional
    @Override
    public void updateBrand(Brand brand, List<Long> cids) {
        //修改品牌表
        this.brandMapper.updateByPrimaryKey(brand);
        //先删除中间表的数据
        this.brandMapper.deleteCategoryBrand(brand.getId());
        //修改中间表
        for (Long cid : cids) {
            //重新保存中间表的数据
            this.brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    @Transactional
    @Override
    public void deleteBrandById(Long bid) {
        //删除品牌表的数据
        this.brandMapper.deleteByPrimaryKey(bid);
        //维护中间表的数据
        this.brandMapper.deleteCategoryBrand(bid);
    }

}
