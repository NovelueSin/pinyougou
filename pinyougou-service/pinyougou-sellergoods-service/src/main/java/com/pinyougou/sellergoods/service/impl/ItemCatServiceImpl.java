package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.ItemCatMapper;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Service(interfaceName = "com.pinyougou.service.ItemCatService")
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public void save(ItemCat itemCat) {
        itemCatMapper.insertSelective(itemCat);
    }

    @Override
    public void update(ItemCat itemCat) {
        itemCatMapper.updateByPrimaryKeySelective(itemCat);
    }

    @Override
    public void delete(Serializable id) {

    }

    /**
     * 批量删除数据，只能删除1级数据
     * @param ids
     */
    @Override
    public void deleteAll(Serializable[] ids) {
        Example example = new Example(ItemCat.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        itemCatMapper.deleteByExample(example);
    }

    @Override
    public ItemCat findOne(Serializable id) {
        return null;
    }

    @Override
    public List<ItemCat> findAll() {
        return null;
    }

    /*根据parentId分页查询*/
    @Override
    public PageResult findByPage(ItemCat itemCat, int page, int rows) {
        try {
            PageInfo<ItemCat> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(
                    new ISelect() {
                        @Override
                        public void doSelect() {
                            itemCatMapper.findAllByParentId(itemCat);
                        }
                    }
            );
            return new PageResult(pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ItemCat> findItemCatByParentId(Long parentId) {
        try {
            /*创建ItemCat封装查询条件*/
            ItemCat itemCat = new ItemCat();
            itemCat.setParentId(parentId);
            return itemCatMapper.select(itemCat);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
