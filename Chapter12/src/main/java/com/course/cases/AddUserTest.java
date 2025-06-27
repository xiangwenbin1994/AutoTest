package com.course.cases;

import com.course.model.AddUserCase;
import com.course.model.InterfaceName;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.Test;

import java.io.IOException;

public class addUserCase {

    @Test(dependsOnMethods = "loginTrue",description = "添加用户接口")
    public void addUser() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        AddUserCase addUserCase = session.selectOne("addUserCase");
        System.out.println(addUserCase.toString());
        System.out.println(ConfigFile.getUrl(InterfaceName.ADDUSER));
    }
}
