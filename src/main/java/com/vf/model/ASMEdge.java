package com.vf.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "asm_edge")
public class ASMEdge {
	
	@NotBlank
	private String app;
	
	@NotBlank
	private String fromRes;

	@NotBlank
	private String relationship;
	
	@NotBlank
	private String toRes;

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getFromRes() {
		return fromRes;
	}

	public void setFromRes(String fromRes) {
		this.fromRes = fromRes;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getToRes() {
		return toRes;
	}

	public void setToRes(String toRes) {
		this.toRes = toRes;
	}
		
}