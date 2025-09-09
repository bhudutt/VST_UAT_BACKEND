package com.hitech.dms.web.dao.admin.village.add;

import java.util.List;

import javax.validation.Valid;

import com.hitech.dms.web.model.admin.village.request.VillageRequest;
import com.hitech.dms.web.model.admin.village.response.VillageResponse;

public interface VillageDao {

	VillageResponse addVillage(String userCode,  List<VillageRequest> requestModel);

}
