package com.tarena.passport;

import com.tarena.passport.auto.domain.JwtRSAGenerator;
import com.tarena.passport.protocol.LoginInfo;
import com.tarena.passport.protocol.LoginUser;
import com.tarena.passport.protocol.PassportBusinessException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AutoTestStarter.class)
@Slf4j
public class AutoTest {
    @Autowired
    private JwtRSAGenerator<LoginUser> jwtRSAGenerator;

    @Test
    public void encodeToken() throws PassportBusinessException {
        LoginUser loginInfo = new LoginUser();
        loginInfo.setUserId(1l);
        String jwt = jwtRSAGenerator.generateToken(loginInfo);
        System.out.println(jwt);
    }
    @Test
    public void decodeToken() throws PassportBusinessException {
        LoginUser loginInfo = new LoginUser();
        loginInfo.setUserId(1l);
        Long start=new Date().getTime();
        String jwt = jwtRSAGenerator.generateToken(loginInfo);
        Long end=new Date().getTime();
        System.out.println("获取jwt:"+jwt+";用时["+(end-start)+"]毫秒");
        start=new Date().getTime();
        LoginUser token = jwtRSAGenerator.getLoginFromToken(jwt, LoginUser.class);
        end=new Date().getTime();
        System.out.println("获得用户信息id:"+token.getUserId()+";用时["+(end-start)+"]毫秒");
    }


}
