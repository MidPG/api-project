package com.wth.wthinterface;

import com.wth.wthclientsdk.client.TApiClient;
import com.wth.wthclientsdk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class WthInterfaceApplicationTests {

    @Resource
    TApiClient tApiClient;

    @Test
    public void test1() {
        User user = new User();
        // 中文乱码
        user.setUsername("hjdjakljskal");
        String userNameByPost = tApiClient.getUserNameByPost(user);
        log.info(userNameByPost);
    }

}
