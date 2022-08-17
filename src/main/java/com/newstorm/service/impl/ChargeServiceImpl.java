package com.newstorm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.mapper.ChargeMapper;
import com.newstorm.pojo.Charge;
import com.newstorm.service.ChargeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChargeServiceImpl extends ServiceImpl<ChargeMapper, Charge> implements ChargeService {
    @Override
    public List<Charge> listBetweenDate(LocalDate startDate, LocalDate endDate) {
        QueryWrapper<Charge> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("time", startDate, endDate);
        return list(queryWrapper);
    }
}
