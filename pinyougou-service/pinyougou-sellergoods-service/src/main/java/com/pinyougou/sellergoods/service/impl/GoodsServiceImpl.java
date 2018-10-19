package com.pinyougou.sellergoods.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.Item;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "com.pinyougou.service.GoodsService")
@Transactional
public class GoodsServiceImpl implements GoodsService {

    /*注入数访问层代理对象*/
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public void save(Goods goods) {
        try {
            //设置为审核状态
            goods.setAuditStatus("0");
            /*添加spu商品表*/
            goodsMapper.insertSelective(goods);
            /*为商品描述对象设置主键id*/
            goods.getGoodsDesc().setGoodsId(goods.getId());
            goodsDescMapper.insertSelective(goods.getGoodsDesc());

            /*判断是否启用规格*/
            if ("1".equals(goods.getIsEnableSpec())) {
                /*迭代所有的sku具体商品集合，往sku表插入数据*/
                for (Item item : goods.getItems()) {
                    /*定义sku商品的标题*/
                    StringBuilder title = new StringBuilder();
                    title.append(goods.getGoodsName());
                    /*把规格选项JSON字符串转化为Map集合*/
                    Map<String, Object> spec = JSON.parseObject(item.getSpec());
                    for (Object value : spec.values()) {
                        /*拼接规格选项到sku商品标题*/
                        title.append(" " + value);
                    }
                    /*设置sku商品的标题*/
                    item.setTitle(title.toString());
                    /*设置sku商品的其他属性*/
                    setItemInfo(item, goods);

                    itemMapper.insertSelective(item);
                }
            } else {
                /** 创建SKU具体商品对象 */
                Item item = new Item();
                /** 设置SKU商品的标题 */
                item.setTitle(goods.getGoodsName());
                /** 设置SKU商品的价格 */
                item.setPrice(goods.getPrice());
                /** 设置SKU商品库存数据 */
                item.setNum(9999);
                /** 设置SKU商品启用状态 */
                item.setStatus("1");
                /** 设置是否默认*/
                item.setIsDefault("1");
                /** 设置规格选项 */
                item.setSpec("{}");
                /** 设置SKU商品其它属性 */
                setItemInfo(item, goods);
                itemMapper.insertSelective(item);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置sku商品信息
     *
     * @param item
     * @param goods
     */
    private void setItemInfo(Item item, Goods goods) {
        /*设置sku商品图片地址*/
        List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (imageList != null && imageList.size() > 0) {
            /*取第一张图片*/
            item.setImage((String) imageList.get(0).get("url"));
        }
        /*设置sku商品的分类 3级分类*/
        item.setCategoryid(goods.getCategory3Id());
        /*设置sku商品创建时间*/
        item.setCreateTime(new Date());
        /*设置sku商品修改时间*/
        item.setUpdateTime(item.getCreateTime());
        /*设置spu商品编号*/
        item.setGoodsId(goods.getId());
        /*设置商家编号*/
        item.setSellerId(goods.getSellerId());
        /*设置商品分类名称*/
        item.setCategory(itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName());
        /*设置品牌名称*/
        item.setBrand(brandMapper.selectByPrimaryKey(goods.getBrandId()).getName());
        /*设置商家店铺名称*/
        item.setSeller(sellerMapper.selectByPrimaryKey(goods.getSellerId()).getNickName());
    }


    @Override
    public void update(Goods goods) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public Goods findOne(Serializable id) {
        return null;
    }

    @Override
    public List<Goods> findAll() {
        return null;
    }

    @Override
    public PageResult findByPage(Goods goods, int page, int rows) {
        try {
            /*开始分页*/
            PageInfo<Map<String, Object>> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    goodsMapper.findAll(goods);
                }
            });
            /*循环查询到的商品*/
            for (Map<String, Object> map : pageInfo.getList()) {
                ItemCat itemCat1 = itemCatMapper.selectByPrimaryKey(map.get("category1Id"));
                map.put("category1Name",itemCat1 != null ? itemCat1.getName(): "");
                ItemCat itemCat2 = itemCatMapper.selectByPrimaryKey(map.get("category2Id"));
                map.put("category2Name",itemCat2 != null ? itemCat2.getName(): "");
                ItemCat itemCat3 = itemCatMapper.selectByPrimaryKey(map.get("category3Id"));
                map.put("category3Name",itemCat3 != null ? itemCat3.getName(): "");
            }

            return new PageResult(pageInfo.getTotal(), pageInfo.getList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
