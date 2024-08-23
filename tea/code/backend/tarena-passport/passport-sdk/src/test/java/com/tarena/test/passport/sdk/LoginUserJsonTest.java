package com.tarena.test.passport.sdk;

import com.alibaba.fastjson.JSON;
import com.tarena.passport.protocol.LoginUser;
import java.util.Date;

public class LoginUserJsonTest {
    public static void main(String[] args) {
        LoginUser user=new LoginUser();
        user.setUserId(1l);
        user.setUserName("wang");
        user.setActivate(true);
        user.setNickName("wang");
        user.setAvatar("http://11.11.1.1/images.png");
        user.setExpireAt(new Date().getTime());
        user.setDays(5);
        user.setDeviceId("1");
        String o = JSON.toJSONString(user);
        System.out.println(o);
    }
}
