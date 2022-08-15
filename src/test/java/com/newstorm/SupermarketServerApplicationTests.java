package com.newstorm;

import com.newstorm.common.HmacUtils;
import com.newstorm.mapper.UserMapper;
import com.newstorm.pojo.MembershipLevel;
import com.newstorm.pojo.User;
import com.newstorm.service.MembershipLevelService;
import com.newstorm.service.UserService;
import com.newstorm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class SupermarketServerApplicationTests {



    @Test
    void contextLoads() {

    }

}
