package com.hitech.dms.web.entity.mobile.attendance;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@Entity
@Table(name = "attendance_clock")
@Data
public class AttendanceClock {
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
    private Integer id;
    
	@Column(name="clockInAttendenceType")
	private Integer clockInAttendenceType;
	
	@Column(name="clockOutAttendenceType")
	private Integer clockOutAttendenceType;
	
	@Column(name="clockinDateTime")
	private LocalDateTime clockInDateTime;
	
	@Column(name="clockOutDateTime")
	private LocalDateTime clockOutDateTime;
	
	@Column(name="clockinLatitute")
	private String clockinLatitute;
	
	@Column(name="clockInLongitute")
	private String clockInLongitute;
	
	@Column(name="clockOutLatitute")
	private String clockOutLatitute;
	
	@Column(name="clockOutLongitute")
	private String clockOutLongitute;
	
	@Column(name="clockInLocation")
	private String clockInLocation;
	
	@Column(name="clockOutLocation")
	private String clockOutLocation;
	
	@Column(name="photo_url")
	private String photoUrl;
	
	@Column(name="clockIsActive")
	private char clockIsActive;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="CreatedBy")
	private Integer createdBy;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@Column(name="ModifiedBy")
	private Integer modifiedBy;

}
