package com.newstorm;


import com.newstorm.pojo.*;
import com.newstorm.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SupermarketServerApplicationTests {
    @Autowired
    UserCouponService userCouponService;
    @Autowired
    RedeemService redeemService;
    @Autowired
    CouponService couponService;

    @Test
    public void contextLoads() {
        List<UserCoupon> userCouponList = userCouponService.list().subList(7, 60);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<LocalDateTime> localDateTimeList =
                createRandomDate(LocalDateTime.now().minusDays(3),
                        LocalDateTime.now().plusDays(5), userCouponList.size());
        int index = 0;
        for (UserCoupon userCoupon : userCouponList) {
            Redeem redeem = new Redeem();
            Map<String, Object> map = new HashMap<>(1);
            map.put("type", userCoupon.getCouponType());
            redeem.setValue(couponService.listByMap(map).get(0).getCost());
            redeem.setNumber(userCoupon.getQuantity());
            redeem.setUserId(userCoupon.getUserId());
            redeem.setCouponType(userCoupon.getCouponType());
            redeem.setTime(localDateTimeList.get(index++));
            redeemService.save(redeem);
        }
    }

    public static LocalDateTime createRandomDate(LocalDateTime startTime, LocalDateTime endTime) {
        //将两个时间转为时间戳
        long start = startTime.toEpochSecond(ZoneOffset.of("+8"));
        long end = endTime.toEpochSecond(ZoneOffset.of("+8"));
        //获取两个时间的随机数
        long difference = (long) (Math.random() * (end - start));
        //生成时间
        return LocalDateTime.ofEpochSecond(start + difference, 0, ZoneOffset.ofHours(8));
    }


    public static List<LocalDateTime> createRandomDate(LocalDateTime startTime, LocalDateTime endTime, Integer number) {
        //将两个时间转为时间戳
        long start = startTime.toEpochSecond(ZoneOffset.of("+8"));
        long end = endTime.toEpochSecond(ZoneOffset.of("+8"));
        long difference;
        List<LocalDateTime> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            //获取两个时间的随机数
            difference = (long) (Math.random() * (end - start));
            //生成时间
            list.add(LocalDateTime.ofEpochSecond(start + difference, 0, ZoneOffset.ofHours(8)));
        }
        return list;
    }
}
