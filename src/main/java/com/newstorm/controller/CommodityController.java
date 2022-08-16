package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commodity")
public class CommodityController {
    CommodityService commodityService;

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @GetMapping("/query")
    public JsonResult queryCommodity(@RequestParam("commodityId") Integer commodityId) {
        return new JsonResult(commodityService.getById(commodityId));
    }
}
