package com.newstorm.controller;

import com.newstorm.common.JsonResult;
import com.newstorm.common.JwtUtils;
import com.newstorm.exception.BaseException;
import com.newstorm.pojo.Commodity;
import com.newstorm.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/commodity")
public class CommodityController {
    private HttpServletRequest request;
    CommodityService commodityService;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setCommodityService(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    /**
     * 查询单个商品信息
     *
     * @param commodityId 商品id
     * @return 商品信息
     */
    @GetMapping("/query")
    public JsonResult queryCommodity(@RequestParam("commodityId") Integer commodityId) {
        return new JsonResult(commodityService.getById(commodityId));
    }

    /**
     * 查询所有商品信息
     *
     * @return 所有商品信息
     */
    @GetMapping("/all")
    public JsonResult allCommodity() {
        return new JsonResult(commodityService.list());
    }

    /**
     * 管理员修改商品信息
     *
     * @param commodity 商品实体
     * @return 是否修改成功
     */
    @PostMapping("/change")
    public JsonResult changeCommodity(@RequestBody Commodity commodity) {
        checkIdentity();
        return commodityService.updateById(commodity) ? JsonResult.success()
                : new JsonResult("更新失败");
    }

    /**
     * 管理员删除商品
     *
     * @param commodityId 商品id
     * @return 是否删除成功
     */
    @GetMapping("/delete")
    public JsonResult deleteCommodity(@RequestParam("commodityId") Integer commodityId) {
        checkIdentity();
        return commodityService.removeById(commodityId) ? JsonResult.success()
                : new JsonResult("删除失败");
    }

    /**
     * 管理员新增商品
     *
     * @param commodity 商品实体
     * @return 是否新增成功
     */
    @PostMapping("/add")
    public JsonResult addCommodity(@RequestBody Commodity commodity) {
        checkIdentity();
        commodity.setId(null);
        return commodityService.save(commodity) ? JsonResult.success()
                : new JsonResult("添加失败");
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
