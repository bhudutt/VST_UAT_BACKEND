package com.hitech.dms.web.model.admin.role.create.resquest;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class RoleFunctionRequestModel {
	
    private AdminRoleMaster roleMaster;
    private List<BigInteger> functionalityMasters;
    private Long assignedBy;

}
