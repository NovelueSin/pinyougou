package com.pinyougou.mapper;

import com.pinyougou.pojo.TypeTemplate;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * TypeTemplateMapper 数据访问接口
 * @date 2018-09-26 10:28:00
 * @version 1.0
 */
public interface TypeTemplateMapper extends Mapper<TypeTemplate>{

    List<TypeTemplate> findAll(TypeTemplate typeTemplate);
}