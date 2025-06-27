package com.course.cases;

import com.course.model.InterfaceName;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.Test;

import java.io.IOException;

public class GetUserInfoCase {
    @Test(dependsOnMethods = "loginTrue",description = "获取userId为1的用户信息")
    public void getUserInfo() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserInfoCase getUserInfoCase = session.selectOne("getUserInfoCase",1);
        System.out.println(getUserInfoCase.toString());
        System.out.println(ConfigFile.getUrl(InterfaceName.GETUSERINFO));

    }
}
