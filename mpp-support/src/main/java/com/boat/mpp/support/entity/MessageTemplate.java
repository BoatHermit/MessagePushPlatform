package com.boat.mpp.support.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "message_template")
public class MessageTemplate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "audit_status", nullable = false)
    private Byte auditStatus;

    @Column(name = "flow_id", length = 50)
    private String flowId;

    @Column(name = "msg_status", nullable = false)
    private Byte msgStatus;

    @Column(name = "cron_task_id")
    private Long cronTaskId;

    @Column(name = "cron_crowd_path", length = 500)
    private String cronCrowdPath;

    @Column(name = "expect_push_time", length = 100)
    private String expectPushTime;

    @Column(name = "id_type", nullable = false)
    private Byte idType;

    @Column(name = "send_channel", nullable = false)
    private Byte sendChannel;

    @Column(name = "template_type", nullable = false)
    private Byte templateType;

    @Column(name = "msg_type", nullable = false)
    private Byte msgType;

    @Column(name = "shield_type", nullable = false)
    private Byte shieldType;

    @Column(name = "msg_content", nullable = false, length = 600)
    private String msgContent;

    @Column(name = "send_account", nullable = false)
    private Byte sendAccount;

    @Column(name = "creator", nullable = false, length = 45)
    private String creator;

    @Column(name = "updator", nullable = false, length = 45)
    private String updator;

    @Column(name = "auditor", nullable = false, length = 45)
    private String auditor;

    @Column(name = "team", nullable = false, length = 45)
    private String team;

    @Column(name = "proposer", nullable = false, length = 45)
    private String proposer;

    @Column(name = "is_deleted", nullable = false)
    private Byte isDeleted;

    @Column(name = "created", nullable = false)
    private Integer created;

    @Column(name = "updated", nullable = false)
    private Integer updated;

}