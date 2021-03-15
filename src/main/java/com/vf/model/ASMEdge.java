package com.vf.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "asm_edge")
public class ASMEdge implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String app;
	
	@Id
	private String fromRes;

	@Id
	private String relationship;
	
	@Id
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