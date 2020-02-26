package com.ood.myorange.config;

import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;


/**
 * Created by Chen on 2/21/20.
 *
 * Every new DAO should extends this Interface.
 */
@Configuration
public interface BaseDao<T> extends Mapper<T>,MySqlMapper<T>{

}
