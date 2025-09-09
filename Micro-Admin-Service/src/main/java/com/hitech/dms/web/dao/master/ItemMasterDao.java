package com.hitech.dms.web.dao.master;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import com.hitech.dms.web.model.master.response.ItemMasterModel;

public interface ItemMasterDao {

	List<ItemMasterModel> fetchItemMasterDetails(String itemNo);

	HashMap<BigInteger, String> fetchItemMasterList(String userCode, Integer dealerId, Integer branchId,
			String searchText);

}
