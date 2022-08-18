package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.common.JwtUtils;
import com.newstorm.exception.BaseException;
import com.newstorm.pojo.Redeem;
import com.newstorm.service.RedeemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    @Operation(summary = "会员获取满减券兑换记录")
    @ApiResponse(description = "该会员的满减券兑换记录",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(anyOf = {Redeem.class})))
    @GetMapping("/user")
    public JsonResult getRedeemRecords() {
        Integer account = JwtUtils.getUserAccount(request.getHeader(JwtUtils.AUTH_HEADER_KEY));
        return new JsonResult(redeemService.getUserRedeem(account));
    }

    /**
     * 管理员获取会员满减券兑换记录
     *
     * @param account 会员卡号
     * @return 该会员的满减券兑换记录
     */
    @Operation(summary = "管理员获取会员满减券兑换记录")
    @Parameter(description = "会员卡号")
    @ApiResponse(description = "该会员的满减券兑换记录",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(anyOf = {Redeem.class})))
    @GetMapping("/administrator")
    public JsonResult adminGetRedeemRecords(@RequestParam("account") Integer account) {
        checkIdentity();
        return new JsonResult(redeemService.getUserRedeem(account));
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
