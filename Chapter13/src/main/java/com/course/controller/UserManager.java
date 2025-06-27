package com.course.controller;

import com.course.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Log4j2
@RestController
@RequestMapping(value = "v1")
@Tag(name = "v1",description = "用户管理系统第一个版本")
public class UserManager {
    @Autowired
    private SqlSessionTemplate template;
    @Operation(method = "POST",description = "登录接口")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public boolean login(HttpServletResponse response, @RequestBody User user){
        int i = template.selectOne("login", user);
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        log.info("查询到的结果是" + i + "，用户是");
        if(i == 1){
            log.info("登录的用户是：" + user.getUserName());
            return true;
        }
        return false;
    }
    @Operation(method="POST",description = "添加用户接口，首先要判断用户是否登录")
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public boolean addUser(HttpServletRequest request,@RequestBody User user) {
        Boolean b = verifyCookies(request);
        int result = 0;
        if (b != null) {
            result = template.insert("InsertUserInfo", user);
            if (result > 0) {
                log.info("添加用户的数量是" + result);
                return true;
            }
        }
        return false;
    }
    @Operation(method = "POST",description = "获取用户信息接口")
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public List<User> getUserInfo(HttpServletRequest request,@RequestBody User user){
        Boolean b = verifyCookies(request);
        if(b){
            List<User> users = template.selectList("getUserInfo", user);
            log.info("getUserInfo获取到的用户数量是" + users.size());
            return users;
        }else {
            return null;
        }
    }
    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    @Operation(method = "POST",description = "更新用户信息接口")
    public int updateUserInfo(HttpServletRequest request, @RequestBody User user){
        Boolean b = verifyCookies(request);
        int result = 0;
        if(b){
            result = template.update("updateUserInfo",user);
        }
        log.info("更新用户的条目数为" + result);
        return result;
    }

    private Boolean verifyCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(Objects.isNull(cookies)){
            log.info("Cookie信息为空");
            return false;
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("login") && cookie.getValue().equals("true")){
                log.info("Cookie信息验证通过");
                return true;
            }
        }
        return false;
    }
}
