package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.common.JwtUtils;
import com.newstorm.exception.BaseException;
import com.newstorm.service.OrderCommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order/commodity")
public class OrderCommodityController {
    private HttpServletRequest request;
    OrderCommodityService orderCommodityService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setOrderCommodityService(OrderCommodityService orderCommodityService) {
        this.orderCommodityService = orderCommodityService;
    }

    /**
     * 管理员获取订单对应的商品信息
     *
     * @param orderId 订单号
     * @return 商品信息
     */
    @GetMapping("/administrator")
    public JsonResult getUserOrderCommodities(@RequestParam("orderId") Integer orderId) {
        checkIdentity();
        Map<String, Object> condition = new HashMap<>(1);
        condition.put("order_id", orderId);
        return new JsonResult(orderCommodityService.listByMap(condition));
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
