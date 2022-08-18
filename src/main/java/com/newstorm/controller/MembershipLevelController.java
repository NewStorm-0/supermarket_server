package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.pojo.MembershipLevel;
import com.newstorm.service.MembershipLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "获取所有会员等级信息")
    @ApiResponse(description = "所有会员等级信息",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(anyOf = {MembershipLevel.class})))
    @GetMapping("/all")
    public JsonResult getAllLevels() {
        return new JsonResult(membershipLevelService.list());
    }
}
