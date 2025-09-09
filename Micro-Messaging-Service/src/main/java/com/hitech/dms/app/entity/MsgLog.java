package com.hitech.dms.app.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.hitech.dms.app.common.Constant;
import com.hitech.dms.app.util.JodaTimeUtil;
import com.hitech.dms.app.util.JsonUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "ADM_MSG_LOG")
public class MsgLog implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4051883821974572633L;
	@Id
	@Column(name = "msg_id")
	private String msgId;
	@Column(name = "msg")
    private String msg;
	@Column(name = "exchange")
    private String exchange;
	@Column(name = "routing_key")
    private String routingKey;
//	@Column(name = "TemplateName")
//    private String templateName;
	@Column(name = "status")
    private Integer status;
	@Column(name = "try_count")
    private Integer tryCount;
	@Column(name = "next_try_time")
    private Date nextTryTime;
	@Column(name = "create_time")
    private Date createTime;
	@Column(name = "update_time")
    private Date updateTime;

    public MsgLog(String msgId, Object msg, String exchange, String routingKey) {
        this.msgId = msgId;
        this.msg = JsonUtil.objToStr(msg);
        this.exchange = exchange;
        this.routingKey = routingKey;

        this.status = Constant.MsgLogStatus.DELIVERING;
        this.tryCount = 0;

        Date date = new Date();
        this.createTime = date;
        this.updateTime = date;
        this.nextTryTime = (JodaTimeUtil.plusMinutes(date, 1));
    }
}
