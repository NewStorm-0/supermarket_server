package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.common.JwtUtils;
import com.newstorm.service.RedeemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/redeem")
public class RedeemController {
    private HttpServletRequest request;
    RedeemService redeemService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setRedeemService(RedeemService redeemService) {
        this.redeemService = redeemService;
    }

    /**
     * 会员获取满减券兑换记录
     *
     * @return 该会员的满减券兑换记录
     */
    @GetMapping("/user")
    public JsonResult getRedeemRecords() {
        Integer account = JwtUtils.getUserAccount(request.getHeader(JwtUtils.AUTH_HEADER_KEY));
        return new JsonResult(redeemService.getUserRedeem(account));
    }
}
