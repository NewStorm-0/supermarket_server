package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.common.JwtUtils;
import com.newstorm.exception.BaseException;
import com.newstorm.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/charge")
public class ChargeController {
    private HttpServletRequest request;
    ChargeService chargeService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setChargeService(ChargeService chargeService) {
        this.chargeService = chargeService;
    }

    /**
     * 会员获取充值记录
     *
     * @return 该会员的充值记录
     */
    @GetMapping("/user")
    public JsonResult getChargeRecords() {
        Integer account = JwtUtils.getUserAccount(request.getHeader(JwtUtils.AUTH_HEADER_KEY));
        Map<String, Object> condition = new HashMap<>(1);
        condition.put("user_id", account);
        return new JsonResult(chargeService.listByMap(condition));
    }

    /**
     * 管理员获取单个会员充值记录
     *
     * @return 该会员的充值记录
     */
    @GetMapping("/administrator")
    public JsonResult administratorGetChargeRecords(@RequestParam("account") Integer account) {
        checkIdentity();
        Map<String, Object> condition = new HashMap<>(1);
        condition.put("user_id", account);
        return new JsonResult(chargeService.listByMap(condition));
    }

    /**
     * 检查当前登录身份是否是管理员，若不是管理员，则抛出异常
     */
    private void checkIdentity() {
        if (JwtUtils.notAdministrator(request.getHeader(JwtUtils.AUTH_HEADER_KEY))) {
            throw new BaseException("您不具备管理员权限");
        }
    }
}
