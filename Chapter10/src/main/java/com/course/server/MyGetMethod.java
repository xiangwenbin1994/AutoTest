package com.course.server;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@Tag(name="这是我全部的Get请求",description = "get请求相关api")
public class MyGetMethod {
    /**
     * 给客户端返回cookies信息的接口
     * @param response
     * @return
     */
    @RequestMapping(value = "/getCookies",method = RequestMethod.GET)
    @Operation(summary = "用get请求访问接口",description = "返回cookies的接口")
    public String getCookies(HttpServletResponse response){
        //HttpServletRequest 放请求信息的
        //HttpServletResponse 放响应信息的
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        return "恭喜你获得Cookies信息成功";
    }

    /**
     * 需要携带cookies才能登录的请求
     * @param request
     * @return
     */

    @RequestMapping(value = "/get/with/cookies",method = RequestMethod.GET)
    @Operation(summary = "用get请求访问接口",description = "要求携带cookie才能访问的接口")
    public String getWithCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(Objects.isNull(cookies)){
            return "cookie信息为空，用户未登录";
        }
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            String value = cookie.getValue();
            if(name.equals("login") && value.equals("true")){
                return "已接收到正确的cookies，登录成功";
            }
        }
        return "未接收到正确的cookies，登录失败";
    }

    /**
     * 开发一个需要携带参数才能访问的get请求
     * @param start
     * @param end
     * @return
     */
    @RequestMapping(value = "/get/with/param",method = RequestMethod.GET)
    @Operation(summary = "用get请求访问接口",description = "需要携带参数才能访问的get接口")
    public Map<String,Integer> getWithParam(@RequestParam Integer start, @RequestParam Integer end){
        Map<String,Integer> myGoodsMap = new HashMap<>();
        myGoodsMap.put("小米手机",1999);
        myGoodsMap.put("米老头",2);
        myGoodsMap.put("洗衣机",3999);
        return myGoodsMap;
    }

    //开发一个参数在路径上的接口
    @RequestMapping(value = "/get/with/param/{start}/{end}",method = RequestMethod.GET)
    @Operation(summary = "用get请求访问接口",description = "参数在路径上的接口")
    public Map<String,Integer> getinParam(@PathVariable Integer start, @PathVariable Integer end){
        Map<String,Integer> myGoodsMap = new HashMap<>();
        myGoodsMap.put("大疆无人机",3999);
        myGoodsMap.put("旺旺仙贝",1);
        myGoodsMap.put("肉夹馍",11);
        return myGoodsMap;
    }
}
