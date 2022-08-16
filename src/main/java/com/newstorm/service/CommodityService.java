package com.newstorm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newstorm.pojo.Commodity;
import com.newstorm.pojo.dto.CommodityDto;

import java.util.List;
import java.util.Map;

public interface CommodityService extends IService<Commodity> {
    double calculatePrice(List<CommodityDto> commodityDtoList);

    /**
     * 批量获取商品单价
     * @param commodityIdList 商品 id 组成的 List
     * @return id 为 key， price 为 value 组成的 Map
     */
    Map<Integer, Double> getPriceBatch(List<Integer> commodityIdList);

    /**
     *
     * @param commodityId 商品 id
     * @return 商品单价
     */
    double getPrice(int commodityId);
}
