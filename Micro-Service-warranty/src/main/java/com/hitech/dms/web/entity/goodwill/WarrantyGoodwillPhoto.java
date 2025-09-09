package com.hitech.dms.web.entity.goodwill;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name="wa_goodwill_photo")
public class WarrantyGoodwillPhoto {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "warranty_goodwill_id", referencedColumnName = "id")
    private WarrantyGoodwill warrantyGoodwill;

    @NotNull
    @Column(name = "file_name" ,length = 300)
    private String fileName;

    @NotNull
    @Column(name = "file_type", length=50)
    private String fileType;

    @Override
    public String toString() {
        return "WarrantyGoodwillPhoto{" +
                "id=" + id +
                ", warrantyGoodwill=" + warrantyGoodwill +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
