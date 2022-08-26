package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.common.JwtUtils;
import com.newstorm.exception.BaseException;
import com.newstorm.pojo.Coupon;
import com.newstorm.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/coupon")
public class CouponController {
    private HttpServletRequest request;
    CouponService couponService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setCouponService(CouponService couponService) {
        this.couponService = couponService;
    }

    /**
     * 获取所有满减券信息
     *
     * @return 所有满减券信息
     */
    @Operation(summary = "获取所有满减券信息")
    @ApiResponse(description = "所有满减券信息",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(anyOf = {Coupon.class})))
    @GetMapping("/all")
    public JsonResult getCoupons() {
        return new JsonResult(couponService.list());
    }

    /**
     * 管理员增加满减券
     *
     * @param coupon 满减券实体
     * @return 是否添加成功
     */
    @Operation(summary = "管理员增加满减券")
    @Parameter(description = "满减券实体",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(anyOf = {Coupon.class})))
    @ApiResponse(description = "是否添加成功")
    @PostMapping("/add")
    public JsonResult addCoupon(@RequestBody Coupon coupon) {
        checkIdentity();
        coupon.setType(null);
        return couponService.save(coupon) ? new JsonResult(coupon.getType())
                : new JsonResult("添加失败");
    }

    /**
     * 管理员删除满减券
     *
     * @param couponId 满减券id
     * @return 是否删除成功
     */
    @Operation(summary = "管理员删除满减券")
    @Parameter(description = "满减券id")
    @ApiResponse(description = "是否删除成功")
    @GetMapping("/delete")
    public JsonResult deleteCoupon(@RequestParam("couponId") Integer couponId) {
        checkIdentity();
        return couponService.removeById(couponId) ? JsonResult.success()
                : new JsonResult("删除失败");
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
