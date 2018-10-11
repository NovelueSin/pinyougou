package com.pinyougou.mapper;

import com.pinyougou.pojo.Specification;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * SpecificationMapper 数据访问接口
 * @date 2018-09-26 10:28:00
 * @version 1.0
 */
public interface SpecificationMapper extends Mapper<Specification>{

    /*多条件规格查询*/
    List<Specification> findAll(Specification specification);

    @Select("select id,spec_name as text from tb_specification order by id ASC ")
    List<Map<String,Object>> findAllByIdAndName();
}