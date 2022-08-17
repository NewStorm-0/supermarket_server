package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.common.JwtUtils;
import com.newstorm.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user_coupon")
public class UserCouponController {
    private HttpServletRequest request;
    UserCouponService userCouponService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setUserCouponService(UserCouponService userCouponService) {
        this.userCouponService = userCouponService;
    }

    /**
     * 会员获取持有的满减券信息
     *
     * @return 持有的满减券信息
     */
    @GetMapping("/user")
    public JsonResult getUserCoupons() {
        Integer account = JwtUtils.getUserAccount(request.getHeader(JwtUtils.AUTH_HEADER_KEY));
        Map<String, Object> condition = new HashMap<>(1);
        condition.put("user_id", account);
        return new JsonResult(userCouponService.listByMap(condition));
    }
}