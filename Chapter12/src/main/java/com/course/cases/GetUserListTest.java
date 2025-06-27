package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserInfoCase;
import com.course.model.GetUserListCase;
import com.course.model.InterfaceName;
import com.course.model.User;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import com.course.utils.JsonAssertUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetUserListTest {
    @Test(dependsOnGroups = "loginTrue",description = "获取性别为男的用户信息")
    public void getUserList() throws Exception {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserListCase getUserListCase = session.selectOne("getUserListCase", 1);
        System.out.println(getUserListCase.toString());
        ConfigFile.getUrl(InterfaceName.GETUSERLIST);
        JSONArray resultJson = getJsonArrayResult(getUserListCase);
        //根据响应结果，和数据库中存储的用例信息的expected字段进行断言
        SqlSession session2 = DatabaseUtil.getSqlSession();
        List<User> userList = session2.selectList(getUserListCase.getExpected(),getUserListCase);
        for (User user : userList) {
            System.out.println("获取的user：" + user.toString());
        }
        //先断言json的长度是否一样，如果不同，就不用继续比了，直接断言失败
        JSONArray userListJson = new JSONArray(userList);
        Assert.assertEquals(userListJson.length(),resultJson.length());
        //如果断言成功，则需要进一步比对Json中的数据是否一致
        for (int i = 0; i < resultJson.length(); i++) {
            JSONObject expected = (JSONObject) resultJson.get(i);
            JSONObject actual = (JSONObject) userListJson.get(i);
            JsonAssertUtils.assertEqualsIgnoreQuotes(expected.toString(),actual.toString());
        }
    }
    private JSONArray getJsonArrayResult(GetUserListCase getUserListCase) throws IOException {
        ////建立post请求,url用TestConfig在loginTest的@beforeTest处加载的url
        HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param = new JSONObject();
        //设置请求参数
        param.put("userName", getUserListCase.getUserName());
        param.put("age", getUserListCase.getAge());
        param.put("sex", getUserListCase.getSex());
        //设置请求头
        post.setHeader("Content-Type","application/json");
        //把参数放进请求体中
        StringEntity entity = new StringEntity(param.toString());
        post.setEntity(entity);
        //设置Cookies
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);
        //发送请求，获取响应结果
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        JSONArray resultJsonArray = new JSONArray(result);
        return resultJsonArray;
    }
}
