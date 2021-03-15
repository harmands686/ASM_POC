package com.vf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "as_resource")
public class ASMResource{
	
	
	@Id
	@Column(name = "ID")
	private Integer id;
	
	@NotBlank
	@Column(name = "APP")
	private String app;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@NotBlank
	@Column(name = "UNIQUEID")
	private String uniqueId;

	@NotBlank
	@Column(name = "ENTITYTYPES")
	private String entityTypes;
	
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getEntityTypes() {
		return entityTypes;
	}
	public void setEntityTypes(String entityTypes) {
		this.entityTypes = entityTypes;
	}

		
}