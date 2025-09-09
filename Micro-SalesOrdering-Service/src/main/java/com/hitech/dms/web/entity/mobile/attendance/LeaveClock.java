package com.hitech.dms.web.entity.mobile.attendance;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="Leave_Clock")
@Data
public class LeaveClock {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private LocalDateTime leaveStartDate;
	
	private LocalDateTime leaveEndDate;
	
	private String leavetype;
	
	private String employeeRemark;
	
	private String approvalStatus;
	
	private Integer approvedBy;
	
	private String  approverRemark;
	
	private char leaveStatus;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="CreatedBy")
	private Integer createdBy;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@Column(name="ModifiedBy")
	private Integer modifiedBy;

}
