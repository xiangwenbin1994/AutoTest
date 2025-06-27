package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.AddUserCase;
import com.course.model.InterfaceName;
import com.course.model.User;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class AddUserTest {

    @Test(dependsOnGroups = "loginTrue",description = "添加用户接口")
    public void addUser() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        AddUserCase addUserCase = session.selectOne("addUserCase",1);
        System.out.println("addUserCase：" + addUserCase.toString());
        System.out.println(ConfigFile.getUrl(InterfaceName.ADDUSER));
        session.close();
        //发请求，获得服务器响应
        String result = getResult(addUserCase);
        System.out.println("服务器获得的响应为" + result);
        //到数据库中进行比对，取出数据库中的内容
        //使用原来的session取不出来数据，不知道为什么
        SqlSession session2 = DatabaseUtil.getSqlSession();
        User user = session2.selectOne("addUser", addUserCase);
        System.out.println("user的内容为：");
        System.out.println(user.toString());
        //断言是否通过
        Assert.assertEquals(addUserCase.getExpected(),result);
    }

    private String getResult(AddUserCase addUserCase) throws IOException {
        //建立post请求,url用TestConfig在loginTest的@beforeTest处加载的url
        HttpPost httpPost = new HttpPost(TestConfig.addUserUrl);
        JSONObject param = new JSONObject();
        param.put("userName",addUserCase.getUserName());
        param.put("password",addUserCase.getPassword());
        param.put("sex",addUserCase.getSex());
        param.put("age",addUserCase.getAge());
        param.put("permission",addUserCase.getPermission());
        param.put("isDelete",addUserCase.getIsDelete());

        //设置头信息
        httpPost.setHeader("Content-Type","application/json");
        //设置Post的消息体信息
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        httpPost.setEntity(entity);
        //设置Cookies
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);
        //发送请求，接收响应
        HttpResponse response = TestConfig.defaultHttpClient.execute(httpPost);
        //响应信息体转换为字符串，进行接收
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        return result;


    }
}
