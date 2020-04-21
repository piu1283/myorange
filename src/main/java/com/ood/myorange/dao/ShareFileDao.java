package com.ood.myorange.dao;

import com.ood.myorange.config.BaseDao;
import com.ood.myorange.constant.enumeration.ShareType;
import com.ood.myorange.pojo.ShareFile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;


/**
 * Created by Linkun on 3/19/2020
 */
public interface ShareFileDao extends BaseDao<ShareFile> {

    @Select("SELECT * FROM `t_share` WHERE shareKey=#{shareKey}")
    ShareFile SelectShareFileByShareKey(String shareKey);

    @Select("SELECT * FROM `t_share` WHERE fileId=#{fileId}")
    ShareFile SelectShareFileByFileId(Integer fileId);

    @Select("SELECT * FROM `t_share` WHERE id=#{shareId} AND CURRENT_TIMESTAMP < shareDeadline")
    ShareFile SelectShareFileIfDeadlineNotExpired(Integer shareId);

    @Select("SELECT * FROM `t_share`")
    List<ShareFile> SelectAllShareFileInfo();

    @Insert( "INSERT into `t_share` (userID,fileId,shareType,sharePass,shareDeadline,shareKey,shareUrl) VALUES(#{userId},#{fileId},#{shareType},#{sharePass},#{deadline},#{shareKey},#{shareUrl})" )
    void insertShareFile(Integer userId, Integer fileId, ShareType shareType,String sharePass, Timestamp deadline, String shareKey, String shareUrl);

    @Update("UPDATE `t_share` SET shareDeadline = #{deadline}, downloadLimitation = #{limitDownloadTimes}, shareType=#{shareType} WHERE id = #{shareId}")
    void updateShareFileById(int shareId, Timestamp deadline, int limitDownloadTimes, ShareType shareType);
}