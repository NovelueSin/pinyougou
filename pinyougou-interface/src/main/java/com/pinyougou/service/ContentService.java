package com.pinyougou.service;

import com.pinyougou.pojo.Content;
import com.pinyougou.pojo.PageResult;

import java.util.List;
import java.io.Serializable;
/**
 * 广告服务
 * ContentService 服务接口
 * @date 2018-09-26 11:12:14
 * @version 1.0
 */
public interface ContentService {

	/** 添加方法 */
	void save(Content content);

	/** 修改方法 */
	void update(Content content);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Content findOne(Serializable id);

	/** 查询全部 */
	List<Content> findAll();

	/** 多条件分页查询 */
	PageResult findByPage(Content content, int page, int rows);

}