package com.hitech.dms.web.entity.enquiry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "user_column_preferences")
@Data
public class ColumnEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // id can be null when inserting

	@Column(name = "columnNmae")
    private String name;
	@Column(name = "columnVisiblilty")
    private String visibleVal;
	@Column(name = "columnOrder")
    private Integer order;
	@Column(name = "usercode")
	private String usercode;
	@Column(name = "functionlity")
	private String functionlity;
	@Transient
	private Boolean visible;
}
