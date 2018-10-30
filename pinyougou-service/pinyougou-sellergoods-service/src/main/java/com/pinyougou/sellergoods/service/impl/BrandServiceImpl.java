package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.pojo.Brand;
import com.pinyougou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * BrandServiceImpl 服务接口实现类
 *
 * @version 1.0
 * @date 2018-09-26 11:12:14
 */

@Service(interfaceName = "com.pinyougou.service.BrandService")
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 添加品牌
     */
    public void save(Brand brand) {
        try {
            brandMapper.insertSelective(brand);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改方法
     */
    public void update(Brand brand) {
        try {
            brandMapper.updateByPrimaryKeySelective(brand);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据主键id删除
     */
    public void delete(Serializable id) {

    }

    /**
     * 批量删除
     */
    public void deleteAll(Serializable[] ids) {
        /*创建示范对象*/
        Example example = new Example(Brand.class);
        /*创建条件对象*/
        Example.Criteria criteria = example.createCriteria();
        /*添加in条件*/
        criteria.andIn("id", Arrays.asList(ids));
        /*根据条件删除*/
        brandMapper.deleteByExample(example);
    }

    /**
     * 根据主键id查询
     */
    public Brand findOne(Serializable id) {
        try {
            return brandMapper.selectByPrimaryKey(id);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 查询全部
     */
    public List<Brand> findAll() {
        try {
            return brandMapper.selectAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 多条件分页查询
     */
    public PageResult findByPage(Brand brand, int page, int rows) {
        try {
            PageInfo<Brand> pageInfo = PageHelper.startPage(page, rows)
                    .doSelectPageInfo(new ISelect() {
                        @Override
                        public void doSelect() {
                            brandMapper.findAll(brand);
                        }
                    });
            return new PageResult(pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Map<String, Object>> findAllByIdAndName() {
        try {
            return brandMapper.findAllByIdAndName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}