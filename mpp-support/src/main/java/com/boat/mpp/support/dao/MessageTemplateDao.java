package com.boat.mpp.support.dao;

import com.boat.mpp.support.domain.MessageTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MessageTemplateDao extends JpaRepository<MessageTemplate, Long>
        , JpaSpecificationExecutor<MessageTemplate> {

    /**
     * 查询 列表（分页)
     * @param isDeleted  0：未删除 1：删除
     * @param pageable 分页对象
     * @return 短信模板
     */
    List<MessageTemplate> findAllByIsDeletedEqualsOrderByUpdatedDesc(Integer isDeleted, Pageable pageable);


    /**
     * 统计未删除的条数
     * @param isDeleted 0：未删除 1：删除
     * @return 未删除的条数
     */
    Long countByIsDeletedEquals(Integer isDeleted);
}
