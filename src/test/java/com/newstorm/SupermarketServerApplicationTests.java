package com.newstorm;

import com.newstorm.common.HmacUtils;
import com.newstorm.common.JsonResult;
import com.newstorm.common.JwtUtils;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@SpringBootTest
class SupermarketServerApplicationTests {

    @Test
    public void contextLoads() {


    }

}
