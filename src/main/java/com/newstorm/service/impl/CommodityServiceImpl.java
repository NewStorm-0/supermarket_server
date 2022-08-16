package com.newstorm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.mapper.CommodityMapper;
import com.newstorm.pojo.Commodity;
import com.newstorm.pojo.dto.CommodityDTO;
import com.newstorm.service.CommodityService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {
    @Override
    public double calculatePrice(List<CommodityDTO> commodityDTOList) {
        double price = 0.00D;
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        for (CommodityDTO commodityDTO : commodityDTOList) {
            queryWrapper.eq("id", commodityDTO.getCommodityId())
                    .select("price");
            price += (Integer) getMap(queryWrapper).get("price") * commodityDTO.getNumber();
            queryWrapper.clear();
        }
        return price;
    }

    @Override
    public Map<Integer, Double> getPriceBatch(List<Integer> commodityIdList) {
        Map<Integer, Double> idPriceMap = new HashMap<>(commodityIdList.size());
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        for (Integer id : commodityIdList) {
            queryWrapper.eq("id", id)
                    .select("price");
            idPriceMap.put(id, (Double) getMap(queryWrapper).get("price"));
            queryWrapper.clear();
        }
        return idPriceMap;
    }

    @Override
    public double getPrice(int commodityId) {
        QueryWrapper<Commodity> commodityQueryWrapper = new QueryWrapper<>();
        commodityQueryWrapper.eq("id", commodityId)
                .select("price");
        return (double) getMap(commodityQueryWrapper).get("price");
    }
}
