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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {
    private HttpServletRequest request;
    AdministratorService administratorService;
    MembershipLevelService membershipLevelService;

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

    @PostMapping("/login")
    public JsonResult login(@RequestBody Administrator administrator) {
        String account = administrator.getAccount();
        String password = administrator.getPassword();
        return new JsonResult(administratorService.login(account, password));
    }

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

    private void checkIdentity() {
        if (!JwtUtils.isAdministrator(request.getHeader(JwtUtils.AUTH_HEADER_KEY))) {
            throw new BaseException("您不具备管理员权限");
        }
    }

    private String getAccount() {
        return JwtUtils.getAdministratorAccount(request.getHeader(JwtUtils.AUTH_HEADER_KEY));
    }

    private void checkPassword(String password) {
        if (!administratorService.checkAdministratorPassword(getAccount(), password)) {
            throw new BaseException("管理员密码验证错误");
        }
    }
}
