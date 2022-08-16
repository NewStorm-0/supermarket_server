package com.newstorm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.newstorm.common.JsonResult;
import com.newstorm.mapper.RedeemMapper;
import com.newstorm.pojo.Redeem;
import com.newstorm.service.RedeemService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RedeemServiceImpl extends ServiceImpl<RedeemMapper, Redeem> implements RedeemService {
    @Override
    public List<Redeem> getUserRedeem(int account) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("user_id", account);
        return listByMap(map);
    }
}
