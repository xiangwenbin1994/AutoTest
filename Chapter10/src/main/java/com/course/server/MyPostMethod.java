package com.course.server;

import com.bean.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Tag(name="这是我全部的Post请求",description = "Post请求相关api")
public class MyPostMethod {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Operation(summary = "用post请求访问接口",description = "能够返回cookie的post请求")
    public String postWithParam(HttpServletResponse response, @RequestParam(value = "userName",required = true) String userName,
                                @RequestParam(value = "password",required = true) String password){
        if(userName.equals("zhangsan") && password.equals("123456")){
            Cookie cookie = new Cookie("login","true");
            response.addCookie(cookie);
            return "恭喜你，登录成功";
        }
        return "用户名密码不正确，登录失败";
    }

    /**
     * 携带Cookie访问，返回用户列表的接口
     * @param request
     * @param user
     * @return
     */
    @RequestMapping(value = "/getUserList",method = RequestMethod.POST)
    @Operation(summary = "用post请求访问接口",description = "携带Cookie访问，返回用户列表的接口")
    public String getUserList(HttpServletRequest request, @RequestBody User user){
        //获取请求中的cookies
        Cookie[] cookies = request.getCookies();
        if(Objects.isNull(cookies)){
            return "你必须携带cookies才能访问这个接口";
        }
        for (Cookie cookie : cookies) {
            if(user.getUserName().equals("zhangsan") && user.getPassword().equals("123456")
                    && cookie.getName().equals("login")
                    && cookie.getValue().equals("true")){
                User u = new User();
                u.setName("lisi");
                u.setAge("18");
                u.setSex("man");
                return u.toString();
            }
        }
        return "登录失败，用户信息不正确";
    }
}
