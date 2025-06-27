package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.InterfaceName;
import com.course.model.UpdateUserInfoCase;
import com.course.model.User;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class UpdateUserInfoTest {
    @Test(dependsOnGroups = "loginTrue",description = "更新userId为1的用户信息")
    public void updateUserInfo() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase", 1);
        System.out.println(updateUserInfoCase.toString());
        ConfigFile.getUrl(InterfaceName.UPDATEUSERINFO);
        //发请求，获取响应信息
        int result = getResult(updateUserInfoCase);
        System.out.println("result的值为：");
        System.out.println(result);
        //从数据库中读取信息
        //重新建立一个session，避免update之后session的影响
        SqlSession session2 = DatabaseUtil.getSqlSession();
        User user = session2.selectOne(updateUserInfoCase.getExpected(),updateUserInfoCase);
        //对比从数据库中得到的预期结果与响应的实际结果，进行断言
        Assert.assertNotNull(user);
        Assert.assertNotNull(result);


    }

    @Test(dependsOnGroups = "loginTrue",description = "删除用户信息")
    public void deleteUser() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateUserInfoCase updateUserInfoCase = session.selectOne("updateUserInfoCase", 2);
        System.out.println(updateUserInfoCase.toString());
        System.out.println(ConfigFile.getUrl(InterfaceName.UPDATEUSERINFO));
        //发请求，获取响应信息
        int result = getResult(updateUserInfoCase);
        //重新获取一个session，来避免之前的session的影响
        SqlSession session2 = DatabaseUtil.getSqlSession();
        //从数据库中读取信息
        User user = session2.selectOne(updateUserInfoCase.getExpected(),updateUserInfoCase);
        //对比从数据库中得到的预期结果与响应的实际结果，进行断言
        Assert.assertNotNull(user);
        Assert.assertNotNull(result);
    }

    private int getResult(UpdateUserInfoCase updateUserInfoCase) throws IOException {
        //建立post请求，并从LoginTest中的beforeTest处获得更新用户接口的url
        HttpPost post = new HttpPost(TestConfig.updateUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id",updateUserInfoCase.getUserId());
        param.put("userName",updateUserInfoCase.getUserName());
        param.put("sex",updateUserInfoCase.getSex());
        param.put("age",updateUserInfoCase.getAge());
        param.put("permission",updateUserInfoCase.getPermission());
        param.put("isDelete",updateUserInfoCase.getIsDelete());
        //设置请求头
        post.setHeader("Content-Type","application/json");
        //设置请求体
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //请出HttpClient并设置Cookies
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);
        //执行httpClient并获取响应
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        //获取响应体信息
        int result = Integer.parseInt(EntityUtils.toString(response.getEntity(),"utf-8"));
        return result;

    }
}
