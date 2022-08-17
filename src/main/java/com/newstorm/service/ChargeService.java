package com.newstorm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newstorm.pojo.Charge;

import java.time.LocalDate;
import java.util.List;

public interface ChargeService extends IService<Charge> {
    /**
     * 查询一段日期内的充值记录
     *
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @return 符号条件的充值记录
     */
    List<Charge> listBetweenDate(LocalDate startDate, LocalDate endDate);
}
