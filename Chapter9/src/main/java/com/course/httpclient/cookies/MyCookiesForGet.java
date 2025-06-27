package com.course.httpclient.cookies;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForGet {
    private String url;
    private ResourceBundle bundle;
    private CookieStore cookieStore;
    @BeforeTest
    public void beforeTest(){
        bundle = ResourceBundle.getBundle("application", Locale.CHINA);
        url = bundle.getString("test.Url");
    }
    //访问一个接口，获取Cookies
    @Test
    public void testGetCookies() throws IOException {
        String result;
        //从resource/application.properties下读取getCookies对应的值，即获取cookies接口的url
        String testUrl = this.url + this.bundle.getString("getCookies");
        //实例化HttpGet对象，并传入URL
        HttpGet get = new HttpGet(testUrl);
        //实例化DefaultHttpClient对象，以便后续发送请求
        DefaultHttpClient client = new DefaultHttpClient();
        //发送请求并获取响应结果
        HttpResponse response = client.execute(get);
        //将响应结果转换为String格式，并设置编码格式为utf-8
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);

        //发送请求后，就可以用client获取Cookies
        this.cookieStore = client.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println("Cookies的值为：" + "key = " + name + "; value = " + value);
        }
    }
    @Test(dependsOnMethods = {"testGetCookies"})
    public void getWithCookies() throws IOException {
        //从resource/application.properties下读取test.get.with.Cookies对应的值，即获取"访问时需要携带cookies接口"的url
        String uri = bundle.getString("test.get.with.Cookies");
        String testUrl = this.url + uri;
        //实例化HttpGet对象，并传入URL
        HttpGet get = new HttpGet(testUrl);
        //实例化DefaultHttpClient对象，以便后续发送请求
        DefaultHttpClient client = new DefaultHttpClient();
        //设置cookie的值，这里是把cookie设置为了成员变量，能够在类中传递
        //并且在上一个方法中获取到了cookie值传入了成员变量cookiestore中
        //使用了testng的dependsOnMethods方法，先获取cookie，再传入
        client.setCookieStore(this.cookieStore);
        HttpResponse response = client.execute(get);
        //判断是否返回成功，用状态码判断
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("状态码为：" + statusCode);
        if(statusCode == 200){
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(result);
        }else
            Assert.assertTrue(false);


    }
}
