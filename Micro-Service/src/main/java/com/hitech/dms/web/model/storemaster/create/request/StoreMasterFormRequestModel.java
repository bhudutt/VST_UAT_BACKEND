package com.hitech.dms.web.model.storemaster.create.request;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class StoreMasterFormRequestModel implements Serializable {

	private List<StoreMasterModel> storeMasterList;
	private Integer page;
	private Integer size;
	
}
