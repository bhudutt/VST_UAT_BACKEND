package com.hitech.dms.web.model.tm.create.response;


import lombok.Data;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@JsonInclude(Include.NON_NULL)
public class TmListModel {

	private BigInteger tmID;
	private String tmName;
}
