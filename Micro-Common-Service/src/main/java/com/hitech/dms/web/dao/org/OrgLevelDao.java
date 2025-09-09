package com.hitech.dms.web.dao.org;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.org.request.OrgLeveDealerRequestModel;
import com.hitech.dms.web.model.org.request.OrgLevelByDeptRequestModel;
import com.hitech.dms.web.model.org.request.OrgLevelDealerBranchRequestModel;
import com.hitech.dms.web.model.org.request.OrgLevelHierForParentRequestModel;
import com.hitech.dms.web.model.org.response.OrgLeveDealerResponseModel;
import com.hitech.dms.web.model.org.response.OrgLevelByDeptResponseModel;
import com.hitech.dms.web.model.org.response.OrgLevelDealerBranchResponseModel;
import com.hitech.dms.web.model.org.response.OrgLevelHierForParentResponseModel;

public interface OrgLevelDao {
	public List<OrgLevelByDeptResponseModel> fetchOrgLevelListByDept(String userCode, String deptCode, Integer pcId);

	public List<OrgLevelByDeptResponseModel> fetchOrgLevelListByDept(String userCode,
			OrgLevelByDeptRequestModel orgLevelByDeptRequestModel);

	public List<OrgLevelHierForParentResponseModel> fetchOrgLevelHierForParent(String userCode, Integer levelID,
			Long orgHierID, String includeInactive, String isFor, BigInteger dealerId, Integer departmentId,
			Integer pcId);

	public List<OrgLevelHierForParentResponseModel> fetchOrgLevelHierForParent(String userCode,
			OrgLevelHierForParentRequestModel orgLevelHierForParentRequestModel);

	public List<OrgLeveDealerResponseModel> fetchOrgLevelDealerList(String userCode,
			OrgLeveDealerRequestModel orgLeveDealerRequestModel);

	public List<OrgLevelDealerBranchResponseModel> fetchOrgLevelDealerBranchList(String userCode,
			OrgLevelDealerBranchRequestModel orgLeveDealerBranchRequestModel);

	public List<OrgLevelByDeptResponseModel> getPcEnquiry(String userCode);
}
