package com.pinyougou.mapper;

import tk.mybatis.mapper.common.Mapper;

import com.pinyougou.pojo.ItemCat;

import java.util.List;

/**
 * ItemCatMapper 数据访问接口
 * @date 2018-09-26 10:28:00
 * @version 1.0
 */
public interface ItemCatMapper extends Mapper<ItemCat>{


    List<ItemCat> findAllByParentId(ItemCat itemCat);
}