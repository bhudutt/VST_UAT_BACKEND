package com.hitech.dms.web.entity.pcr;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
@Table(name = "SV_WA_PCR_PHOTOS")
public class WarrantyPcrPhotos implements Serializable{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "warranty_pcr_id", referencedColumnName = "id")
    private ServiceWarrantyPcr serviceWarrantyPcr;

    @NotNull
    @Column(name = "file_name", length = 300)
    private String fileName;
    
    @Column(name = "fileContentType", length = 300)
    private String fileContentType;
    
//    private List<String> fileNames;
    //added by mahesh.kumar on 28-02-2025
    @Transient
    private String filePath;
    
    

    @Override
    public String toString() {
        return "ServiceMrcPhotos{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                '}';
    }

}
