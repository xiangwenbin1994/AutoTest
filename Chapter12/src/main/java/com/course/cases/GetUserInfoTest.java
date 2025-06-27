package com.course.cases;

import com.course.config.TestConfig;
import com.course.model.GetUserInfoCase;
import com.course.model.InterfaceName;
import com.course.model.User;
import com.course.utils.ConfigFile;
import com.course.utils.DatabaseUtil;
import com.course.utils.JsonAssertUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.ValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetUserInfoTest {
    @Test(dependsOnGroups = "loginTrue",description = "获取userId为1的用户信息")
    public void getUserInfo() throws Exception {
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserInfoCase getUserInfoCase = session.selectOne("getUserInfoCase", 1);
        System.out.println(getUserInfoCase.toString());
        System.out.println(ConfigFile.getUrl(InterfaceName.GETUSERINFO));
        //发请求，获取响应，并取得响应体
        JSONObject resultJson = getJsonResult(getUserInfoCase);
        System.out.println("实际获得的响应体为" + resultJson);
        //获取数据库中的预期结果
        User user = session.selectOne(getUserInfoCase.getExpected(), getUserInfoCase);
        JSONObject expectedJson = new JSONObject(user.toString());
        System.out.println("user自己转换的json为" + expectedJson);
        //断言数据库中的预期结果和发送请求得到的响应体是否一致
        //自定义比较规则，忽略掉json字符串中value的""
        JsonAssertUtils.assertEqualsIgnoreQuotes(expectedJson.toString(),resultJson.toString(),"password");

    }

    private JSONObject getJsonResult(GetUserInfoCase getUserInfoCase) throws IOException {
        //建立post请求,url用TestConfig在loginTest的@beforeTest处加载的url
        HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param = new JSONObject();
        param.put("id", getUserInfoCase.getUserId());
        //设置请求头
        post.setHeader("Content-Type", "application/json");
        //设置请求体
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);
        //设置Cookie
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);
        //发送请求并接收响应
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        result = result.replace("[","").replace("]","");
        System.out.println("result的值为：");
        System.out.println(result);
        JSONObject resultJson = new JSONObject(result);
        return  resultJson;
    }

}
