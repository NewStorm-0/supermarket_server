package com.newstorm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newstorm.pojo.Redeem;

import java.util.List;

public interface RedeemService extends IService<Redeem> {
    /**
     * 根据会员卡号获取会员兑换满减券记录
     * @param account 会员卡号
     * @return redeem 兑换记录 List
     */
    List<Redeem> getUserRedeem(int account);
}
