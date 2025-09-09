package com.hitech.dms.web.dao.spare.inventorytransfer;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.common.GeneratedNumberModel;
import com.hitech.dms.web.model.spare.inventory.response.SpareGrnInventoryResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.inventory.request.SpareInventoryRequest;
import com.hitech.dms.web.model.spare.inventory.request.SpareInventorySearchRequest;

public interface InventoryTransferForClaimDao {

	SpareGrnInventoryResponse fetchGrnDetails(int grnHdrId, String pageName);

	HashMap<BigInteger, String> searchGrnNumber(String searchType, String searchText, String page, String userCode);

	List<PartNumberDetailResponse> fetchGrnPartDetails(int grnHdrId, String page, String claimType);

	SpareGrnResponse createSpareInventory(String userCode, SpareInventoryRequest spareInventoryRequest);

	ApiResponse<List<SpareGrnInventoryResponse>> fetchGrnList(SpareInventorySearchRequest spareSearchInventoryRequest, 
			String userCode);

}
