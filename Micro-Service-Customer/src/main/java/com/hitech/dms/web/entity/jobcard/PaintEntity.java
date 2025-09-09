/**
 * 
 */
package com.hitech.dms.web.entity.jobcard;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Entity
@Table(name="SV_PAINT_SCRATCHED_RO_Mapping")
@Data
public class PaintEntity implements Serializable {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private BigInteger ID;

	    @Column(name = "RO_ID")
	    private BigInteger roId;

	    @Column(name = "PAINT_SCRATCHED_ID")
	    private BigInteger paintId;

	    @Column(name = "DIESEL")
	    private String diesel;

	    @Column(name = "FINAL_ACTION_TAKEN")
	    private String finalAction;

	    @Column(name = "ADVICE_TO_CUST")
	    private String suggestion;

	    @Column(name = "Createdby")
	    private String createdBy;

	    @Column(name = "CreatedDate")
	    private Date createdDate;



}
