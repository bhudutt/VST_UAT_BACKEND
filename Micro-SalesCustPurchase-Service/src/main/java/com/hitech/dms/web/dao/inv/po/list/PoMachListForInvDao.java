package com.hitech.dms.web.dao.inv.po.list;

import java.util.List;

import com.hitech.dms.web.model.inv.po.list.request.PoMachListForInvRequestModel;
import com.hitech.dms.web.model.inv.po.list.response.PoMachListForInvResponseModel;

public interface PoMachListForInvDao {
	public List<PoMachListForInvResponseModel> fetchPoMachineDtlList(String userCode,
			PoMachListForInvRequestModel requestModel);
}
