package com.pinyougou.pojo;

import java.io.Serializable;

/**
 * 品牌实体类
 * @author LEE.SIU.WAH
 * @email lixiaohua7@163.com
 * @date 2017年12月1日 下午4:59:52
 * @version 1.0
 */
public class Brand implements Serializable{

	private Long id;
    private String name;
    private String firstChar;
    /** setter and getter method */
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    public String getFirstChar() {
        return firstChar;
    }
    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar == null ? null : firstChar.trim();
    }
}