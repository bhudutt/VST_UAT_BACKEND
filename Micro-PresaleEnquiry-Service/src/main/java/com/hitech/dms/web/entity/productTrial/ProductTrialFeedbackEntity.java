package com.hitech.dms.web.entity.productTrial;


import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */

@Entity
@Table(name = "SA_PRODUCT_TRIAL_DTL")
@Data
public class ProductTrialFeedbackEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4313842389060347474L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(name = "Product_Trial_Attribute_ID")
	private BigInteger productTrialAttributeId;
	
	@JoinColumn(name="Product_Trial_ID")
    @ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private ProductTrialHdrEntity productTrialHdr;
	
	@Column(name = "Attribute_Id")
	private BigInteger trialAttributeId;
	
	@Column(name = "Rating")
	private int rating;
	
	@Column(name = "Remarks")
	private String feedbackRemarks;

}
