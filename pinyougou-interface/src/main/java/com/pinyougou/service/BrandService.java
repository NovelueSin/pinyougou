package com.pinyougou.service;

import com.pinyougou.pojo.Brand;

import java.util.List;

/**
 * 服务接口
 */
public interface BrandService {

    /**
     * 查询所有品牌
     * @return
     */
    List<Brand> findAll();
}
