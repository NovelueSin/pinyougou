package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.PageResult;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.service.TypeTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference(timeout = 10000)
    private TypeTemplateService typeTemplateService;

    /** 分页查询类型模板 */
    @GetMapping("/findByPage")
    public PageResult findByPage(TypeTemplate typeTemplate,
                                 Integer page, Integer rows){
        // GET请求
        if (typeTemplate != null && StringUtils.isNoneBlank(typeTemplate.getName())){
            try{
                typeTemplate.setName(new String(typeTemplate
                        .getName().getBytes("ISO8859-1"), "UTF-8"));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return typeTemplateService.findByPage(typeTemplate, page, rows);
    }


}
