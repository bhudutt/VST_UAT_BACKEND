package com.hitech.dms.web.entity.pdi;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="SV_MT_CHECKLIST_AGGREGATE")
public class ServiceMtAggregate {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aggregateId;

    @NotBlank(message = "aggregate can't be null")
    private String aggregate;

    @NotNull
    @NotBlank(message = "active status can't be blank")
    private String activeStatus;

    @OneToMany(mappedBy = "serviceMrcAggregate",cascade = CascadeType.ALL)
    private List<ServiceMtCheckPoint> serviceMtMrcCheckPoint;

}
