package com.ood.myorange.dao;

import com.ood.myorange.config.BaseDao;
import com.ood.myorange.config.storage.StorageType;
import com.ood.myorange.pojo.StorageConfig;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Guancheng Lai on 3/19/2020
 */
public interface StorageConfigDao extends BaseDao<StorageConfig> {
    @Select( "SELECT * FROM `t_source` WHERE id=#{id}" )
    StorageConfig SelectSourceById(int id);

    @Select( "SELECT * FROM `t_source` WHERE type=#{type}" )
    StorageConfig SelectSourceByType(StorageType type);

    @Insert( "INSERT into `t_source` (name,type,config) VALUES(#{name},#{type},#{config})" )
    void insertSource(String name, StorageType type, String config);

    @Update( "UPDATE `t_source` SET name = #{name}, config = #{config}, current_use = #{use} WHERE id = #{id}")
    void updateSourceById(int id, String name, String config, boolean use);

    @Update( "UPDATE `t_source` SET name = #{name}, config = #{config}, current_use = #{use} WHERE type = #{type}")
    void updateSourceByType(StorageType type, String name, String config, boolean use);

    @Delete( "DELETE FORM `t_source` WHERE id = #{id}")
    void deleteSourceById(int id);

    @Select( "SELECT COUNT(*) FROM `t_source` WHERE type=#{type}" )
    int countSourceByType(StorageType type);
}
