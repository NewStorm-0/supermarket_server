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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
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
    @Operation(summary = "结账生成订单",
            description = "根据会员卡号和商品信息、满减券生成订单",
            parameters = {
                    @Parameter(name = "checkoutDTO",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(anyOf = {CheckoutDTO.class})))
            },
            responses = {
                    @ApiResponse(description = "返回积分与结算价格")
            })
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
    @Operation(summary = "会员查询自己的订单")
    @ApiResponse(description = "订单信息及订单货品信息")
    @GetMapping("/user")
    public JsonResult getOrdersFromUser() {
        Integer account = JwtUtils.getUserAccount(request.getHeader(JwtUtils.AUTH_HEADER_KEY));
        return new JsonResult(getOrdersFromUser(account));
    }

    /**
     * 管理员查询会员订单
     *
     * @param account 会员卡号
     * @return 订单信息及订单货品信息
     */
    @Operation(summary = "管理员查询会员订单")
    @Parameter(description = "会员卡号")
    @ApiResponse(description = "订单信息及订单货品信息")
    @GetMapping("/administrator")
    public JsonResult administratorGetUserOrders(@RequestParam("account") Integer account) {
        checkIdentity();
        return new JsonResult(getOrdersFromUser(account));
    }


    /**
     * 管理员获取一段日期内的订单记录（不包含商品）
     *
     * @param startDate 起始日期
     * @param endDate   截止日期
     * @return 符合条件的订单记录
     */
    @Operation(summary = "管理员获取一段日期内的订单记录（不包含商品）")
    @Parameter(name = "startDate", description = "起始日期")
    @Parameter(name = "endDate", description = "截止日期")
    @ApiResponse(description = "符合条件的订单记录",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(anyOf = UserOrder.class)))
    @GetMapping("/administrator/between")
    public JsonResult administratorGetUserOrdersBetweenDate(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        checkIdentity();
        return new JsonResult(userOrderService.listBetweenDate(startDate, endDate));
    }

    /**
     * 注意！！！不是接口！！！
     * 获取一个会员的订单记录（包含商品）
     *
     * @param account 会员卡号
     * @return Map<String, List>
     */
    private Map<String, List> getOrdersFromUser(int account) {
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
