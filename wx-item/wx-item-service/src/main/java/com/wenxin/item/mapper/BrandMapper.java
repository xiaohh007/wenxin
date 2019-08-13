package com.wenxin.item.mapper;

import com.wenxin.item.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand (category_id,brand_id) values(#{cid},#{bid})")
    void insertCategoryBrand(@Param("cid") Long cid, @Param("bid")Long bid);


    @Delete("delete from  tb_category_brand where brand_id =(#{bid})")
    void deleteCategoryBrand(Long bid);
}
