/**
 * 
 */
package com.hitech.dms.web.entity.quotation;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Table(name = "SA_QUOTATION_DTL")
@Entity
@Data
public class VehQuoDTLEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Quotation_Dtl_id")
	private BigInteger quoDTLId;

	@ManyToOne
	@JoinColumn(name = "Quotation_id")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private VehQuoHDREntity vehQuoHDR;
	
	@Column(name = "enquiry_id")
	private BigInteger enquiryHDRId;
	
	@Column(name = "machine_item_id")
	private BigInteger machineItemId;
	
	@Column(name = "qty")
	private Integer qty;
	
	@Column(name = "unit_rate")
	private BigDecimal unitRate;
	
	@Column(name="basic_value")
	private BigDecimal basicValue;
	
	@Column(name="gross_discount")
	private BigDecimal grossAmount;
	
	@Column(name="amount_after_discount")
	private BigDecimal amountAfterDiscount;
	
	@Column(name="igst_per")
	private float igstPer; 
	@Column(name="igst_amount")
	private BigDecimal igstAmnt;
	@Column(name="sgst_per")
	private float sgstPer; 
	@Column(name="sgst_amount")
	private BigDecimal sgstAmnt;
	@Column(name="cgst_per")
	private float cgstPer; 
	@Column(name="cgst_amount")
	private BigDecimal cgstAmnt;
	
	@Column(name="Total_gst_amount")
	private BigDecimal totalGstAmnt;
	
	@Column(name="Total_Item_Amount")
	private BigDecimal totalItemAmnt;
}
