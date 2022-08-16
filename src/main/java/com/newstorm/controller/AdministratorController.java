package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.common.JwtUtils;
import com.newstorm.pojo.Administrator;
import com.newstorm.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {
    AdministratorService administratorService;
    HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setAdministratorService(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }


    @PostMapping("/login")
    public JsonResult login(@RequestBody Administrator administrator) {
        String account = administrator.getAccount();
        String password = administrator.getPassword();
        return new JsonResult(administratorService.login(account, password));
    }

}
