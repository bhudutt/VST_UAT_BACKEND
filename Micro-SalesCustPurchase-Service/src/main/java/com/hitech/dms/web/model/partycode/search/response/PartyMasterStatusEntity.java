package com.hitech.dms.web.model.partycode.search.response;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ADM_BP_PARTY_BRANCH")
@Data
public class PartyMasterStatusEntity {
	
	
	@Id
    @Column(name = "party_branch_id")
    private BigInteger id;

    @Column(name = "IsActive")
    private boolean isActive;

}
