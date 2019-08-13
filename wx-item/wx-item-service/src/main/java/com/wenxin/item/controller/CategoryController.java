package com.wenxin.item.controller;


import com.wenxin.item.item.pojo.Category;
import com.wenxin.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam("pid") Long pid) {

        List<Category> categories = this.categoryService.queryByParentId(pid);

        if (pid == -1) {
            List<Category> list= this.categoryService.queryLast();
            return ResponseEntity.ok(list);
        } else {
            if (categories != null && 0 != categories.size()) {
                return ResponseEntity.ok(categories);
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();//204
        }
    }

    @PostMapping
    public ResponseEntity<Void> addCategoryChild(@RequestBody Category category) {
        this.categoryService.saveCategoryChild(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("cid/{cid}")
    public ResponseEntity<Void> deleteCategoryChild(@PathVariable("cid") Long id) {
        this.categoryService.deleteCategoryChild(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateCategoryChild(@RequestBody Category category) {
        this.categoryService.updateCategoryChild(category);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


}
