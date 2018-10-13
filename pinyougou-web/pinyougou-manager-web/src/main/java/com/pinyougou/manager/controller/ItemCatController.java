package com.pinyougou.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.service.ItemCatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference(timeout = 10000)
    private ItemCatService itemCatService;

    /*根据父级id查找商品分类*/
    @GetMapping("/findItemCatByParentId")
    public List<ItemCat> findItemCatByParentId(Long parentId) {
        return itemCatService.findItemCatByParentId(parentId);
    }

    /*分页查询商品分类*/
    @GetMapping("/findByPage")
    public PageResult findByPage(ItemCat itemCat, Integer page, Integer rows) {

        /*Get请求中文转码*/
        /*因为使用的是parentId查询商品,所以不用转码了*/
        return itemCatService.findByPage(itemCat, page, rows);
    }


    /*保存商品分类*/
    @PostMapping("/save")
    public boolean save(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.save(itemCat);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改商品分类
     *
     * @param itemCat
     * @return
     */
    @PostMapping("/update")
    public boolean update(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.update(itemCat);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @GetMapping("/delete")
    public boolean delete(Long[] ids) {
        try {
            itemCatService.deleteAll(ids);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
