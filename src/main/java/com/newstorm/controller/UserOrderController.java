package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.pojo.dto.CheckoutDto;
import com.newstorm.pojo.dto.OrderDto;
import com.newstorm.service.UserOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
@Slf4j
public class UserOrderController {

    private HttpServletRequest request;
    UserOrderService userOrderService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setOrderService(UserOrderService userOrderService) {
        this.userOrderService = userOrderService;
    }

    /**
     * 结账产生订单
     *
     * @param checkoutDto 包含满减券以及商品信息
     * @return 结账信息，包含花费与积分
     */
    @PostMapping("/checkout")
    public JsonResult checkout(@RequestBody CheckoutDto checkoutDto) {
        Integer account = checkoutDto.getAccount();
        OrderDto orderDto = checkoutDto.getOrder();
        return new JsonResult(userOrderService.checkout(account, orderDto));
    }
}
