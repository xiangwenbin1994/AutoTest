package com.course.utils;

import com.course.model.InterfaceName;

import java.util.Locale;
import java.util.ResourceBundle;

public class ConfigFile {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("application", Locale.CHINA);
    public static String getUrl(InterfaceName name){
        String address = bundle.getString("test.url");
        String uri = "";
        //最终的测试地址
        String testUrl;
        if(name.equals(InterfaceName.LOGIN)){
            uri = bundle.getString("login.uri");
        }
        if(name.equals(InterfaceName.UPDATEUSERINFO)){
            uri = bundle.getString("updateUserInfo.uri");
        }
        if(name.equals(InterfaceName.GETUSERLIST)){
            uri = bundle.getString("getUserList.uri");
        }
        if(name.equals(InterfaceName.GETUSERINFO)){
            uri = bundle.getString("getUserInfo.uri");
        }
        if(name.equals(InterfaceName.ADDUSER)){
            uri = bundle.getString("addUser.uri");
        }

        testUrl = address + uri;
        return testUrl;
    }
}
