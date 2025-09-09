package com.hitech.dms.web.dao.machineItem.search;

import java.util.List;

import com.hitech.dms.web.model.machineItem.request.ItemDTLRequestModel;
import com.hitech.dms.web.model.machineItem.request.ItemListRequestModel;
import com.hitech.dms.web.model.machineItem.response.ItemDTLResponseModel;
import com.hitech.dms.web.model.machineItem.response.ItemListResponseModel;

public interface MachineItemDao {
	public List<ItemListResponseModel> fetchMachineItemList(String userCode, ItemListRequestModel requestModel);

	public ItemDTLResponseModel fetchMachineItemDTL(String userCode, ItemDTLRequestModel requestModel);
}
