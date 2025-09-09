package com.hitech.dms.web.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Data
@Table(name = "adm_user_cust")
public class UserCustomerEntity implements Serializable
{
    private static final long serialVersionUID = 4101876059474055633L;

    @Id
    @Column(name = "usr_cust_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usrCustId;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_type")
    private String userType;
    
    @Column(name = "user_name")
    private String userName;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean empStatus;
    
    @Column(name = "IsEmailVerified")
	@Type(type = "yes_no")
	private Boolean isEmailVerified;
    
    @Column(name = "MobileNumber")
	private String empMobile;
    
    @Column(name = "BirthDate")
	private Date birthDate;

	@Column(name = "MaritalStatus")
	private BigInteger maritalStatusId;
	private transient String maritalStatus;

	@Column(name = "Gender")
	private BigInteger genderId;
	private transient String gender;
	
	@Column(name = "AnniversaryDate")
	private Date anniversaryDate;
	
	@Column(name = "Qualification1")
	private String qualification1;
	
	@Column(name = "PANNo")
	private String panNumber;
	
	@JsonIgnore
	@Column(name = "CreatedBy",updatable=false)
	private String createdBy;

	@JsonIgnore
	@Column(name = "CreatedDate",updatable=false)
	private Date createdDate;
	
	@JsonIgnore
	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@JsonIgnore
	@Column(name = "ModifiedDate")
	private Date modifiedDate;

//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonIgnore
//    private UserEntity user;
}
