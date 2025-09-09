package com.hitech.dms.web.entity.SparePo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */

@Entity
@Table(name = "PA_PO_DTL", uniqueConstraints = { @UniqueConstraint(columnNames = "po_dtl_id") })
@Data
public class SparePoDetailEntity implements Serializable {

	private static final long serialVersionUID = 4076799122614129051L;
	@Id
	@Column(name = "po_dtl_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger poDtlId;

	@JoinColumn(name = "po_hdr_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private SparePoEntity sparePOHdr;
	
	@Column(name = "part_id", nullable = false)
	private Integer partId;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice;

	@Column(name = "net_amount", nullable = false)
	private BigDecimal netAmount;

	@Column(name = "gst_percent", nullable = false)
	private BigDecimal gstPercent;

	@Column(name = "gst_amount", nullable = false)
	private BigDecimal gstAmount;

//	@Column(name = "delete_flag")
//	@Type(type = "yes_no")
//	private char deleteFlag;

	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount;
	
	
	@Column(name = "MRP",nullable = false)
	private BigDecimal mrpPrice;



//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		SparePoDetailEntity other = (SparePoDetailEntity) obj;
//		return Objects.equals(sparePOHdr, other.sparePOHdr) && Objects.equals(sparePOHdr, other.sparePOHdr)
//				&& Objects.equals(poDtlId, other.poDtlId);
//	}

	
}
