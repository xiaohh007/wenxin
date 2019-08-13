package com.wenxin.item.controller;

import com.wenxin.common.pojo.PageResult;
import com.wenxin.item.item.pojo.Brand;
import com.wenxin.item.service.BrandService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;


    /**
     * 品牌分页查询
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key){
        PageResult<Brand> result = this.brandService.queryBrandPage(page,rows,sortBy,desc,key);
        if (result != null && result.getItems() != null && result.getItems().size() != 0) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Void> addBrand(@RequestBody Brand brand, @Param("cid")List<Long> cids){
      this.brandService.addBrand(brand,cids);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateBrand(@RequestBody Brand brand, @Param("cid")List<Long> cids){
        this.brandService.updateBrand(brand,cids);
        return  ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") Long bid){
        this.brandService.deleteBrandById(bid);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
