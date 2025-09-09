package com.hitech.dms.web.entity.pdi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="SV_MT_CHECKLIST_CHECKPOINT")
public class ServiceMtCheckPoint {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkpointId;

    @Size(max = 1)
    @NotBlank(message = "active status cannot blank")
    private String activeStatus;

    @NotBlank(message = "checkpoints cannot blank")
    private String checkpointDesc;

    private String defaultTick;

    @NotBlank(message = "fieldType cannot blank")
    @Size(max = 20)
    private String fieldType;

    @Size(max =100)
    private String specification;

    @ManyToOne
    @JoinColumn(name = "aggregate_id")
    private ServiceMtAggregate serviceMrcAggregate;


}
