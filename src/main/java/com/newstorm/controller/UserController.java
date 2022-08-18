package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.common.JwtUtils;
import com.newstorm.exception.BaseException;
import com.newstorm.pojo.User;
import com.newstorm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author NewStorm
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    //request 用来获取用户的 token ，以便获取用户的其余信息
    private HttpServletRequest request;
    UserService userService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 会员登录
     *
     * @param parameters 结构为 { "account": Integer, "password": String }
     * @return user 表中所有属性，其中 password 为 null
     */
    @Operation(summary = "会员登录")
    @Parameter(description = "结构为 { \"account\": Integer, \"password\": String }")
    @ApiResponse(description = "user 表中所有属性，其中 password 为 null",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(anyOf = {User.class})))
    @PostMapping("/login")
    public JsonResult login(@RequestBody Map<String, Object> parameters) {
        try {
            parameters.forEach((k, v) -> log.info("Key: " + k + " Value: " + v));
            Map<String, Object> map = userService.login(
                    Integer.valueOf((String) parameters.get("account")),
                    (String) parameters.get("password"));
            return new JsonResult(map);
        } catch (NumberFormatException e) {
            throw new BaseException("输入信息格式错误");
        }

    }

    /**
     * 会员注册
     *
     * @param user 为注册的会员信息
     * @return 会员卡号
     */
    @Operation(summary = "会员注册")
    @Parameter(description = "注册的会员信息")
    @ApiResponse(description = "会员卡号")
    @PostMapping("/register")
    public JsonResult register(@RequestBody User user) {
        return new JsonResult(userService.register(user));
    }

    /**
     * 会员修改密码
     *
     * @param parameters 结构为{ "oldPassword": String, "newPassword": String }
     * @return 是否修改成功
     */
    @Operation(summary = "会员修改密码")
    @Parameter(description = "结构为{ \"oldPassword\": String, \"newPassword\": String }")
    @ApiResponse(description = "是否修改成功")
    @PostMapping("/change_password")
    public JsonResult changePassword(@RequestBody Map<String, String> parameters) {
        Integer account = JwtUtils.getUserAccount(request.getHeader(JwtUtils.AUTH_HEADER_KEY));
        return userService.changePassword(
                account,
                parameters.get("oldPassword"),
                parameters.get("newPassword")
        ) ? JsonResult.success() : new JsonResult("原密码输入错误");
    }

    /**
     * 会员余额充值
     *
     * @param amount 结构为 { "amount": Double }
     * @return 是否充值成功
     */
    @Operation(summary = "会员余额充值")
    @Parameter(description = "结构为 { \"amount\": Double }")
    @ApiResponse(description = "是否充值成功")
    @PostMapping("/recharge")
    public JsonResult recharge(@RequestBody Map<String, Double> amount) {
        Integer account = JwtUtils.getUserAccount(request.getHeader(JwtUtils.AUTH_HEADER_KEY));
        return userService.recharge(account, amount.get("amount")) ?
                JsonResult.success() : new JsonResult("充值失败");
    }

    /**
     * 会员积分兑换满减券
     *
     * @param couponType 兑换的满减券种类
     * @param number     兑换的满减券数量
     * @return 是否兑换成功
     */
    @Operation(summary = "会员积分兑换满减券")
    @Parameter(name = "couponType", description = "兑换的满减券种类")
    @Parameter(name = "number", description = "兑换的满减券数量")
    @ApiResponse(description = "是否兑换成功")
    @GetMapping("/redeem")
    public JsonResult redeem(@RequestParam("couponType") Integer couponType,
                             @RequestParam("number") Integer number) {
        Integer account = JwtUtils.getUserAccount(request.getHeader(JwtUtils.AUTH_HEADER_KEY));
        return userService.redeem(account, couponType, number) ?
                JsonResult.success() : new JsonResult("兑换失败：积分不足");
    }
}
