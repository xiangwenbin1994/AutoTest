package com.course.cases;

import com.course.model.InterfaceName;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.Test;

import java.io.IOException;

public class GetUserListCase {
    @Test(dependsOnMethods = "loginTrue",description = "获取性别为男的用户信息")
    public void getUserList() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserListCase getUserListCase = session.selectOne("getUserListCase", 1);
        System.out.println(getUserListCase.toString());
        ConfigFile.getUrl(InterfaceName.GETUSERLIST);
    }
}
