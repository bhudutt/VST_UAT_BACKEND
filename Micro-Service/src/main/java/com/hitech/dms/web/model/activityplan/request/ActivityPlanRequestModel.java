package com.hitech.dms.web.model.activityplan.request;

import java.util.List;

import com.hitech.dms.web.entity.activityplan.ActivityPlanDTLEntity;
import com.hitech.dms.web.entity.activityplan.ActivityPlanHDREntity;

import lombok.Data;
@Data
public class ActivityPlanRequestModel {

	private ActivityPlanHDREntity activityPlanHDREntity;
	private List<ActivityPlanDTLEntity> activityPlanDTLEntity;
}
