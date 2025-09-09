package com.hitech.dms.web.entity.registratoncertificate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RegistrationCertificateEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private String chassisNo;
    private String engineNo;
    private String profitCenter;
    private String model;
    private String itemNo;
    private String itemDesc;
    private String customerName;
    private String customerMobileNo;
    private String registratonNo;
    private String vinNo;
}
