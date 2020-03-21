package com.ood.myorange.dao;

import com.ood.myorange.config.BaseDao;
import com.ood.myorange.pojo.StorageConfig;
import com.ood.myorange.config.storage.StorageConfiguration;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Guancheng Lai on 3/19/20.
 */
public interface StorageConfigDao extends BaseDao<StorageConfig> {
    @Select( "SELECT * FROM `t_source` WHERE id=#{id}" )
    StorageConfig getDataSourceById(int id);

    @Insert( "INSERT into `t_source`(id,name,type,config,current_use) VALUES(#{id},#{name},#{storageType},#{storageConfig)" )
    void insertSource(int id, String name, StorageConfiguration.StorageType storageType, StorageConfig storageConfig);

    @Update( "UPDATE `t_source` SET name = #{name}, type = #{storageType}, config = #{storageConfig}, current_use = #{use}" )
    void updateSourceById(String name, StorageConfiguration.StorageType storageType, StorageConfig storageConfig, boolean use);

    @Delete( "DELETE FORM `t_source` WHERE id = #{id}")
    void deleteSourceById(int id);
}
