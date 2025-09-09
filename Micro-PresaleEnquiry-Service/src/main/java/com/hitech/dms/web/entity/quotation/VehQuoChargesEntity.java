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
@Table(name = "SA_QUOTATION_CHARGES")
@Entity
@Data
public class VehQuoChargesEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "quotation_charges_id")
	private BigInteger quotationChrgId;

	@ManyToOne
	@JoinColumn(name = "Quotation_id")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private VehQuoHDREntity vehQuoHDR;

	@Column(name = "charge_id")
	private BigInteger chargeId;

	@Column(name = "charge_amount")
	private BigDecimal chargeAmount;
}
