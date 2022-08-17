package com.newstorm;


import com.newstorm.pojo.Charge;
import com.newstorm.pojo.UserOrder;
import com.newstorm.service.ChargeService;
import com.newstorm.service.UserOrderService;
import com.newstorm.service.impl.UserOrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class SupermarketServerApplicationTests {
    @Autowired
    UserOrderService userOrderService;

    @Test
    public void contextLoads() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.withDayOfMonth(1);
        List<UserOrder> chargeList = userOrderService.listBetweenDate(startDate, endDate);
        for (UserOrder charge : chargeList) {
            System.out.println(charge);
        }
    }

}
