package com.course.controller;

import com.course.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Tag(name = "v1",description = "这是我的第一个版本的demo")
@RestController
@RequestMapping("v1")
public class Demo {
    //首先获取一个执行sql语句的对象
    @Autowired
    private SqlSessionTemplate template;

    /**
     * 查询对象总数
     * @return
     */
    @RequestMapping(value = "/getUserCount",method = RequestMethod.GET)
    @Operation(summary = "可以获取到用户数")
    public int getUserList(){
        return template.selectOne("getUserCount");
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    @Operation(summary = "可以新增用户")
    public int addUser(@RequestBody User user){
        return template.insert("addUser",user);
    }

    /**
     * 可以更新用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    @Operation(summary = "可以更新用户信息")
    public int updateUser(@RequestBody User user){
       return template.update("updateUser",user);
    }

    /**
     * 可以删除用户信息
     * @param id
     * @return
     */
    @Operation(summary = "可以删除用户信息")
    @RequestMapping(value = "/deleteUser",method = RequestMethod.GET)
    public int deleteUser(@RequestParam String id){
        return template.delete("deleteUser",id);
    }

}
