package com.hitech.dms.web.dao.admin.role.create;

import com.hitech.dms.web.entity.admin.org.OrgHierarchy;

public interface OrgHirarchy {
	public OrgHierarchy createHierarchy(OrgHierarchy hierarchy);
	public void deleteHierarchy(Long id,String modifiedBy);
	public void deleteHierarchyWithChildren(Long id,String modifiedBy);
}
