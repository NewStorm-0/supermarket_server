package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.service.MembershipLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/membership_level")
public class MembershipLevelController {

    MembershipLevelService membershipLevelService;

    @Autowired
    public void setMembershipLevelService(MembershipLevelService membershipLevelService) {
        this.membershipLevelService = membershipLevelService;
    }

    @GetMapping("/all")
    public JsonResult getAllLevels() {
        return new JsonResult(membershipLevelService.list());
    }
}
