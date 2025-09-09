/**
 * 
 */
package com.hitech.dms.web.entity.machinepo;

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
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_PO_DTL", uniqueConstraints = { @UniqueConstraint(columnNames = "Po_DTL_id") })
@Data
public class MachinePODtlEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4076799122614129051L;
	@Id
	@Column(name = "po_dtl_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger poDtlId;

	@JoinColumn(name = "po_hdr_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private MachinePOHdrEntity machinePOHdr;

	@Column(name = "machine_item_id", nullable = false)
	private BigInteger machineItemId;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice;

	@Column(name = "mrp_value", nullable = false)
	private BigDecimal mrpPrice;

	@Column(name = "discount_amount")
	private BigDecimal discountAmount;

	@Column(name = "net_amount", nullable = false)
	private BigDecimal netAmount;

	@Column(name = "gst_percent", nullable = false)
	private BigDecimal gstPercent;

	@Column(name = "gst_amount", nullable = false)
	private BigDecimal gstAmount;

	@Column(name = "delete_flag")
	@Type(type = "yes_no")
	private Boolean deleteFlag;

	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount;

	@Column(name = "invoice_qty")
	private int invoiceQty;

	@Column(name = "pending_qty")
	private int pendingQty;
	
	@Column(name = "prod_div_id")
	private Integer productDivisionId;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MachinePODtlEntity other = (MachinePODtlEntity) obj;
		return Objects.equals(machineItemId, other.machineItemId) && Objects.equals(machinePOHdr, other.machinePOHdr)
				&& Objects.equals(poDtlId, other.poDtlId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(machineItemId, machinePOHdr, poDtlId);
	}
}
