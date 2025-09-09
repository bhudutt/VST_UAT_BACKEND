package com.hitech.dms.web.entity.spare.inventorymanagement.bintobintransfer;

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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

/**
 * @author suraj.gaur
 */
@Getter
@Setter
@Entity
@Table(name = "SP_BIN_TO_BIN_TRANSFER_DTL")
public class BinToBinTransferDtl {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@ManyToOne
    @JsonBackReference
    @JoinColumn(name = "btb_hdr_id", referencedColumnName = "id")
    private BinToBinTransferHdr binTransferHdr;
	
	@Column(name = "part_branch_id")
	private BigInteger partBranchId;
	
	@Column(name = "from_store_master_id")
	private BigInteger fromStoreMasterId;
	
	@Column(name = "from_bin_loc_id")
	private BigInteger fromBinLocId;
	
	@Column(name = "to_store_master_id")
	private BigInteger toStoreMasterId;
	
	@Column(name = "to_bin_loc_id")
	private BigInteger toBinLocId;
	
	@Column(name = "transfer_qty")
	private BigDecimal transferQty;
	
	@Transient
	private String toBinName;

	@Transient
	private BigInteger stockStoreId;

}
