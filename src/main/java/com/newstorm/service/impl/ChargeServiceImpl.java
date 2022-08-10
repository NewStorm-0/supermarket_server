package com.newstorm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.mapper.ChargeMapper;
import com.newstorm.pojo.Charge;
import com.newstorm.service.ChargeService;
import org.springframework.stereotype.Service;

@Service
public class ChargeServiceImpl extends ServiceImpl<ChargeMapper, Charge> implements ChargeService {
}
