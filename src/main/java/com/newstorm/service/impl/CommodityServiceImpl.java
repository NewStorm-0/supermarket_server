package com.newstorm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.mapper.CommodityMapper;
import com.newstorm.pojo.Commodity;
import com.newstorm.service.CommodityService;
import org.springframework.stereotype.Service;

@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {
}
