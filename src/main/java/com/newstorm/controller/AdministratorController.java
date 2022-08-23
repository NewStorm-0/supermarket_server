package com.newstorm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newstorm.common.JsonResult;
import com.newstorm.common.JwtUtils;
import com.newstorm.exception.BaseException;
import com.newstorm.pojo.Administrator;
import com.newstorm.pojo.MembershipLevel;
import com.newstorm.pojo.User;
import com.newstorm.service.AdministratorService;
import com.newstorm.service.MembershipLevelService;
import com.newstorm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {
    private HttpServletRequest request;
    AdministratorService administratorService;
    MembershipLevelService membershipLevelService;
    UserService userService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setAdministratorService(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @Autowired
    public void setMembershipLevelService(MembershipLevelService membershipLevelService) {
        this.membershipLevelService = membershipLevelService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 管理员登录
     *
     * @param administrator 管理员实体
     * @return token
     */
    @Operation(summary = "管理员登录")
    @Parameter(description = "管理员实体",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(anyOf = {Administrator.class})))
    @ApiResponse(description = "token")
    @PostMapping("/login")
    public JsonResult login(@RequestBody Administrator administrator) {
        String account = administrator.getAccount();
        String password = administrator.getPassword();
        Map<String, String> map = new HashMap<>(1);
        map.put("token", administratorService.login(account, password));
        return new JsonResult(map);
    }

    /**
     * 修改会员信息
     *
     * @param map 会员实体以及管理员密码
     * @return 是否修改成功
     */
    @Operation(summary = "修改会员信息")
    @Parameter(description = "会员实体以及管理员密码",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(allOf = {User.class, String.class})))
    @ApiResponse(description = "是否修改成功")
    @PostMapping("/change/user")
    public JsonResult changeUser(@RequestBody Map<String, Object> map) {
        checkIdentity();
        checkPassword((String) map.get("password"));
        try {
            LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>)
                    map.get("user");
            ObjectMapper objectMapper = new ObjectMapper();
            String userJson = objectMapper.writeValueAsString(linkedHashMap);
            User user = objectMapper.readValue(userJson, User.class);
            return administratorService.changeUser(user) ? JsonResult.success()
                    : new JsonResult("修改失败");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BaseException("数据格式错误");
        }
    }

    /**
     * 修改会员等级
     *
     * @param map 会员等级实体及管理员密码
     * @return 是否修改成功
     */
    @Operation(summary = "修改会员等级")
    @Parameter(description = "会员等级实体及管理员密码",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(allOf = {MembershipLevel.class, String.class})))
    @ApiResponse(description = "是否修改成功")
    @PostMapping("/change/membership_level")
    public JsonResult changeMembershipLevel(@RequestBody Map<String, Object> map) {
        checkIdentity();
        checkPassword((String) map.get("password"));
        try {
            LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>)
                    map.get("membershipLevel");
            ObjectMapper objectMapper = new ObjectMapper();
            String membershipLevelJson = objectMapper.writeValueAsString(linkedHashMap);
            MembershipLevel membershipLevel = objectMapper
                    .readValue(membershipLevelJson, MembershipLevel.class);
            return membershipLevelService.updateById(membershipLevel) ?
                    JsonResult.success() : new JsonResult("更新失败");
        } catch (JsonProcessingException e) {
            throw new BaseException("数据格式错误");
        }
    }

    /**
     * 管理员获取所有会员信息
     *
     * @return 所有会员实体
     */
    @Operation(summary = "管理员获取所有会员信息")
    @ApiResponse(description = "所有会员实体",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(anyOf = {User.class})))
    @GetMapping("/get/users")
    public JsonResult getUsers() {
        checkIdentity();
        List<User> userList = userService.list();
        for (User user : userList) {
            user.setPassword(null);
        }
        return new JsonResult(userList);
    }

    /**
     * 检查当前登录身份是否是管理员，若不是管理员，则抛出异常
     */
    private void checkIdentity() {
        if (JwtUtils.notAdministrator(request.getHeader(JwtUtils.AUTH_HEADER_KEY))) {
            throw new BaseException("您不具备管理员权限");
        }
    }

    /**
     * 获取当前管理员账号
     *
     * @return 管理员账号
     */
    private String getAccount() {
        return JwtUtils.getAdministratorAccount(request.getHeader(JwtUtils.AUTH_HEADER_KEY));
    }

    /**
     * 检查管理员密码是否正确，若错误，则抛出异常
     *
     * @param password 管理员密码
     */
    private void checkPassword(String password) {
        if (!administratorService.checkAdministratorPassword(getAccount(), password)) {
            throw new BaseException("管理员密码验证错误");
        }
    }
}
