package com.vf.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "as_resource")
public class ASMResource {
	
	@Id
	private String app;
	@Id
	private String uniqueId;

	@NotBlank
	private String entitytypes;
	
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
	public String getEntitytypes() {
		return entitytypes;
	}
	public void setEntitytypes(String entitytypes) {
		this.entitytypes = entitytypes;
	}

		
}