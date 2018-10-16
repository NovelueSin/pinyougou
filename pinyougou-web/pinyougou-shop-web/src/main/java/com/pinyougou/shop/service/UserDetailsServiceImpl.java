package com.pinyougou.shop.service;


import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService{

    /*注入商家服务接口代理对象*/
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*创建List集合封装角色*/
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        /*添加角色*/
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        /*根据登录名查询商家【根据账号查询密码】*/
        Seller seller = sellerService.findOne(username);
        /*判断商家存在并判断状态*/
        if (seller != null && seller.getStatus().equals("1")) {
           /* 返回用户信息对象*/
            return new User(username, seller.getPassword(), grantedAuthorities);
        }
       /* 如果登录不通过，返回空*/
        return null;
    }

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }
}
