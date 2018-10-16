package com.pinyougou.shop.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Goods;
import com.pinyougou.service.GoodsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference(timeout = 10000)
    private GoodsService goodsService;

    @RequestMapping("/save")
    public boolean save(@RequestBody Goods goods) {
        try {
            /*获取登录用户名*/
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(sellerId);
            /*设置商家id*/
            goods.setSellerId(sellerId);
            goodsService.save(goods);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
