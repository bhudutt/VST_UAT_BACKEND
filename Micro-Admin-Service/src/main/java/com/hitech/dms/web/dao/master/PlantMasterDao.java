package com.hitech.dms.web.dao.master;


import java.util.List;

import com.hitech.dms.web.model.master.request.PoPlantMasterRequest;
import com.hitech.dms.web.model.master.response.PlantMasterModel;

public interface PlantMasterDao {

	PlantMasterModel createPlantCode(PoPlantMasterRequest poPlantMasterRequest, String userCode);

	List<PoPlantMasterRequest> fetchPlantMasterList();

}
