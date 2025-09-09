package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import javax.persistence.Column;

import lombok.Data;

@Data
public class OldChassisModelItemListRequestModel {
    @Column(name="machine_item_id")
	private BigInteger machineItemID;
    @Column(name="item_no")
	private String itemNo;
    @Column(name="item_description")
	private String itemDescription;
	
}
