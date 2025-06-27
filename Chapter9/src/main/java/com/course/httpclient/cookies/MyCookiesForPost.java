package com.course.httpclient.cookies;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForPost {
    private String url;
    private ResourceBundle bundle;
    private CookieStore cookieStore;
    @BeforeTest
    public void beforeTest(){
        bundle = ResourceBundle.getBundle("application", Locale.CHINA);
        url = bundle.getString("test.Url");
    }
    @Test
    public void testGetCookies() throws IOException {
        String result;
        String testUrl = this.url + this.bundle.getString("getCookies");
        HttpGet get = new HttpGet(testUrl);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);

        //获取Cookies
        this.cookieStore = client.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println("Cookies的值为：" + "key = " + name + "; value = " + value);
        }
    }
    @Test(dependsOnMethods = {"testGetCookies"})
    public void postWithCookies() throws IOException {
        //创建result存放结果
        String result;
        //获取Url
        String uri = bundle.getString("test.post.with.Cookies");
        String testUrl = this.url + uri;
        //实例化post类
        HttpPost post = new HttpPost(testUrl);
        //设置headers
        post.setHeader("Content-Type","application/json");
        //设置请求参数json格式
        JSONObject param = new JSONObject();
        param.put("name","huhansan");
        param.put("age","18");
        //将json格式的信息添加到请求中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //利用client发送请求并接收数据
        DefaultHttpClient client = new DefaultHttpClient();
        //设置cookie信息
        client.setCookieStore(this.cookieStore);
        //执行post请求
        HttpResponse response = client.execute(post);
        result = EntityUtils.toString(response.getEntity());
        System.out.println("返回值为：");
        System.out.println(result);
        //处理结果，看返回的json字符串里的数值是否符合预期
        //将返回的string类型转换为json字符串
        JSONObject resultJson = new JSONObject(result);
        //取出每一个返回的key对应的value看是否符合预期
        String success = (String)resultJson.get("huhansan");
        Assert.assertEquals("success",success);
        String status = (String) resultJson.get("status");
        Assert.assertEquals("1",status);


    }
}
