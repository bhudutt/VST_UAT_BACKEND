package com.hitech.dms.web.controller.pdi.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PdiViewDto {

	private PdiViewHeaderResponse pdiHeaderData;

    private List<Map<String,Object>> pdiCheckpointList;
}
