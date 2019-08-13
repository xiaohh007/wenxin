package com.wenxin.item.serviceImpl;

import com.wenxin.item.mapper.CategoryMapper;
import com.wenxin.item.item.pojo.Category;
import com.wenxin.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    @Transactional
    public List<Category> queryByParentId(Long pid) {
        Category category = new Category();

        category.setParentId(pid);

        return this.categoryMapper.select(category);
    }

    @Override
    @Transactional
    public void saveCategoryChild(Category category) {
        /**
         * 将本节点插入到数据库中
         * 将此category的父节点的isParent设为true
         */
        //1.首先置id为null
        category.setId(null);
        //2.保存
        this.categoryMapper.insert(category);
        //3.修改父节点
        Category parent = new Category();
        parent.setId(category.getParentId());
        parent.setIsParent(true);
        this.categoryMapper.updateByPrimaryKeySelective(parent);
    }

    /**
     * 删除分类节点
     * @param id
     */
    @Override
    @Transactional
    public void deleteCategoryChild(Long id) {
        //根据id查询
        Category category = this.categoryMapper.selectByPrimaryKey(id);
        if (category.getIsParent()){
            //1.查找所有叶子节点
            List<Category> list = new ArrayList<>();
            queryAllLeafNode(category,list);

            //2.查找所有的子节点
            List<Category> list2 = new ArrayList<>();
            queryAllNode(category,list2);

            //3.删除数据库中的数据,使用list2
            for (Category c : list2) {this.categoryMapper.delete(c);}

            //4.维护中间表
            for (Category c : list) {
                this.categoryMapper.deleteByCategoryIdInCategoryBrand(c.getId());
            }

        }else{
            //1.查询此节点的父亲节点的孩子个数-----查询还有几个兄弟
            Example example = new Example(Category.class);
            example.createCriteria().andEqualTo("parentId",category.getParentId());
            List<Category> list = this.categoryMapper.selectByExample(example);
            if (list.size() != 1) {
                //有兄弟  直接删除自己
                this.categoryMapper.deleteByPrimaryKey(category.getId());
                //删除中间表
                this.categoryMapper.deleteByCategoryIdInCategoryBrand(category.getId());
            }else{
                //没有兄弟节点了,删除自身节点
                this.categoryMapper.deleteByPrimaryKey(category.getId());
                //更新分类表
                Category parent = new Category();
                parent.setId(category.getParentId());
                parent.setIsParent(false);
                this.categoryMapper.updateByPrimaryKeySelective(parent);
                //维护中间表
                this.categoryMapper.deleteByCategoryIdInCategoryBrand(category.getId());
            }
        }
    }

    /**
     * 修改分类节点
     * @param category
     */
    @Override
    @Transactional
    public void updateCategoryChild(Category category) {

        this.categoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    @Transactional
    public List<Category> queryLast() {
        List<Category> list = this.categoryMapper.queryLast();
        return list;
    }

    /**
     * 查询该节点下所有子节点
     * @param category
     * @param node
     */
    private void queryAllNode(Category category, List<Category> node) {
        node.add(category);
        Example example = new Example(Category.class);
        example.createCriteria().andEqualTo("parentId",category.getId());
        List<Category> list = this.categoryMapper.selectByExample(example);
        for (Category category1 : list) {
            queryAllLeafNode(category1,node);
        }

    }

    /**
     * 查询该节点下包含的 所有叶子节点,用于维护tb_category_brand中间表
     * @param category
     * @param leafNode
     */
    private void queryAllLeafNode(Category category, List<Category> leafNode) {
        if (!category.getIsParent()){
            leafNode.add(category);
        }
        Example example = new Example(Category.class);
        example.createCriteria().andEqualTo("parentId",category.getId());
        List<Category> list = this.categoryMapper.selectByExample(example);
        for (Category category1 : list) {
            queryAllLeafNode(category1,leafNode);
        }
    }
}
