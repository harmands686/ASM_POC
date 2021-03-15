package com.vf.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "as_resource")
public class ASMResource implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	
	@NotBlank
	private String app;
	@NotBlank
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