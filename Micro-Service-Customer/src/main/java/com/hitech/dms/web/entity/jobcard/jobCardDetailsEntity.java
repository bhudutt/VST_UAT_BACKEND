/**
 * 
 */
package com.hitech.dms.web.entity.jobcard;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Entity
@Table(name = "SV_RO_DTL")
@Data
public class jobCardDetailsEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger Id;

	@Column(name = "RO_ID")
	private BigInteger roId;

	@Column(name = "PDI_ID")
	private BigInteger inwordPdiId;

	@Column(name = "FRONT_RHPSI")
	private String frontRhPsi;

	@Column(name = "FRONT_LHPSI")
	private String frontLhPsi;

	@Column(name = "REAR_RHPSI")
	private String rearRhPsi;

	@Column(name = "REAR_LHPSI")
	private String rearLhPsi;

	@Column(name = "SERVICE_ACTIVITY_ID")
	private Integer serviceActivityId;

	@Column(name = "SERVCIE_ACTIVITY_PLAN_ID")
	private Integer serviceActivity;

	@Column(name = "Labour_id")
	private Long labour_id;

	@Column(name = "Invenotry_id")
	private Long invenotry_id;

	@Column(name = "PaintScratched")
	private String paintScratched;

	@Column(name = "HourMeterPhotoGraph")
	private String hourMeterPhotoGraph;

	@Column(name = "ChassisNoPhotoGraph")
	private String chassisNoPhotoGraph;

	@Column(name = "UploadPhotoGraph")
	private String uploadPhotoGraph;

	@Column(name = "UploadPhotoGraph1")
	private String uploadPhotoGraph1;
	@Column(name = "pdiFile")
	private String pdiFile;
	@Column(name = "pdiComment")
	private String pdiComments;
	@Column(name = "installationFile")
	private String installationFile;
	@Column(name = "installationComment")
	private String installationComment;

}
