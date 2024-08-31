package com.boat.mpp.support.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "sms_record")
public class SmsRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "message_template_id", nullable = false)
    private Long messageTemplateId;

    @Column(name = "phone", nullable = false)
    private Long phone;

    @Column(name = "supplier_id", nullable = false)
    private Byte supplierId;

    @Column(name = "supplier_name", nullable = false, length = 40)
    private String supplierName;

    @Column(name = "msg_content", nullable = false, length = 600)
    private String msgContent;

    @Column(name = "series_id", nullable = false, length = 100)
    private String seriesId;

    @Column(name = "charging_num", nullable = false)
    private Byte chargingNum;

    @Column(name = "report_content", nullable = false, length = 50)
    private String reportContent;

    @Column(name = "status", nullable = false)
    private Byte status;

    @Column(name = "send_date", nullable = false)
    private Integer sendDate;

    @Column(name = "created", nullable = false)
    private Integer created;

    @Column(name = "updated", nullable = false)
    private Integer updated;

}