package com.hitech.dms.web.entity.pdi;

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
public class PdiAggregate {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Size(max = 1)
    @NotBlank(message = "active status cannot blank")
    private String active_status;

    @Size(max = 100)
    @NotBlank(message = "aggregate type cannot blank")
    private String aggregate;
}
