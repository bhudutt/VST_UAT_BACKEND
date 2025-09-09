package com.hitech.dms.web.dao.installation.create;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.entity.installation.InstallationEntity;
import com.hitech.dms.web.entity.pdi.PdiEntity;
import com.hitech.dms.web.model.Installation.create.response.InstallationCreateResponseModel;
import com.hitech.dms.web.model.pdi.create.response.PdiCreateResponseModel;

public interface InstallationCreateDao {

	public InstallationCreateResponseModel createInstallation(String userCode,
			InstallationEntity requestModel,List<MultipartFile> files);
}
