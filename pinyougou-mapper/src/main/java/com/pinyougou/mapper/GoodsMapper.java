package com.pinyougou.mapper;

import com.pinyougou.pojo.Goods;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * GoodsMapper 数据访问接口
 * @date 2018-09-26 10:28:00
 * @version 1.0
 */
public interface GoodsMapper extends Mapper<Goods>{


    List<Map<String,Object>> findAll(Goods goods);
}