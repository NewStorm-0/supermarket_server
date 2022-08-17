package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.common.JwtUtils;
import com.newstorm.exception.BaseException;
import com.newstorm.pojo.OrderCommodity;
import com.newstorm.pojo.UserOrder;
import com.newstorm.pojo.dto.CheckoutDTO;
import com.newstorm.pojo.dto.OrderDTO;
import com.newstorm.service.OrderCommodityService;
import com.newstorm.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class UserOrderController {

    private HttpServletRequest request;
    UserOrderService userOrderService;
    OrderCommodityService orderCommodityService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setUserOrderService(UserOrderService userOrderService) {
        this.userOrderService = userOrderService;
    }

    @Autowired
    public void setOrderCommodityService(OrderCommodityService orderCommodityService) {
        this.orderCommodityService = orderCommodityService;
    }

    /**
     * 结账产生订单
     *
     * @param checkoutDTO 包含满减券以及商品信息
     * @return 结账信息，包含花费与积分
     */
    @PostMapping("/checkout")
    public JsonResult checkout(@RequestBody CheckoutDTO checkoutDTO) {
        Integer account = checkoutDTO.getAccount();
        OrderDTO orderDTO = checkoutDTO.getOrder();
        return new JsonResult(userOrderService.checkout(account, orderDTO));
    }

    /**
     * 会员查询自己的订单
     *
     * @return 订单信息及订单货品信息
     */
    @GetMapping("/user")
    public JsonResult getUserOrders() {
        Integer account = JwtUtils.getUserAccount(request.getHeader(JwtUtils.AUTH_HEADER_KEY));
        return new JsonResult(getUserOrders(account));
    }

    /**
     * 管理员查询会员订单
     *
     * @param account 会员卡号
     * @return 订单信息及订单货品信息
     */
    @GetMapping("/administrator")
    public JsonResult administratorGetUserOrders(@RequestParam("account") Integer account) {
        checkIdentity();
        return new JsonResult(getUserOrders(account));
    }

    private Map<String, List> getUserOrders(int account) {
        Map<String, Object> map1 = new HashMap<>(1);
        map1.put("user_id", account);
        List<UserOrder> userOrderList = userOrderService.listByMap(map1);
        List<List<OrderCommodity>> orderCommodityList = new ArrayList<>(userOrderList.size());
        Map<String, Object> map2 = new HashMap<>(1);
        for (UserOrder userOrder : userOrderList) {
            map2.put("order_id", userOrder.getId());
            orderCommodityList.add(orderCommodityService.listByMap(map2));
        }
        Map<String, List> returnMap = new HashMap<>(userOrderList.size());
        returnMap.put("UserOrderList", userOrderList);
        returnMap.put("OrderCommodityList", orderCommodityList);
        return returnMap;
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
