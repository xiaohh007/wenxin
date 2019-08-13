package com.wenxin.item.mapper;


import com.wenxin.item.item.pojo.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {


    @Delete("delete from tb_category_brand where Category_id = #{cid}")
    void deleteByCategoryIdInCategoryBrand(@Param("cid") Long id);

    @Select("select * from tb_category where id = (select MAx(id) from tb_category)")
    List<Category> queryLast();

}
