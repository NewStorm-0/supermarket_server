package com.newstorm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newstorm.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
