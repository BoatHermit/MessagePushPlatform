package com.boat.mpp.support.dao;

import com.boat.mpp.support.domain.SmsRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 短信记录DAO
 */
public interface SmsRecordDao extends JpaRepository<SmsRecord, Long> {

    /**
     * 通过日期和手机号找到发送记录
     *
     * @param phone 手机号
     * @param sendDate 发送日期
     * @return sms记录
     */
    List<SmsRecord> findByPhoneAndSendDate(Long phone, Integer sendDate);
}
