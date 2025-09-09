package com.hitech.dms.web.dao.territoryManager;

import java.util.List;

import com.hitech.dms.web.model.territoryManager.TerritoryManagerListModel;

public interface TerritoryManagerDao {
	List<TerritoryManagerListModel> fetchTerritoryManagerList(String userCode, int territoryId);
}
