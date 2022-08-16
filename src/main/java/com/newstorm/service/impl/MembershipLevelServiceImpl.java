package com.newstorm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.mapper.MembershipLevelMapper;
import com.newstorm.pojo.MembershipLevel;
import com.newstorm.service.MembershipLevelService;
import org.springframework.stereotype.Service;

@Service
public class MembershipLevelServiceImpl extends ServiceImpl<MembershipLevelMapper, MembershipLevel> implements MembershipLevelService {
    @Override
    public Double getDiscount(Integer level) {
        QueryWrapper<MembershipLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", level).select("discount");
        return (Double) getMap(queryWrapper).get("discount");
    }
}
