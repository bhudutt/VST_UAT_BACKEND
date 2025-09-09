package com.hitech.dms.web.entity.pcr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;

@Entity
@Data
@Table(name = "SV_WA_MT_FAILURE_TYPE")
public class FailureTypeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "falure_type")
	private String failureType;
	
	@Column(name = "active_status")
	private String activeStatus;
	
	@Column(name = "IsForWithinWarranty")
	private Boolean IsForWithinWarranty;
	
//	@OneToMany(mappedBy = "failureTypeEntity",cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<ServiceWarrantyPcr> serviceWarrantyPcr;

}
