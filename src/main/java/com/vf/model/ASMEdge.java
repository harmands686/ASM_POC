package com.vf.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "asm_edge")
public class ASMEdge{
	
	
	@Id
	@Column(name = "ID")
	private Integer id;
	
	@NotBlank
	@Column(name = "APP")
	private String app;
	
	@NotBlank
	@Column(name = "FROMRES")
	private String fromRes;

	@NotBlank
	@Column(name = "RELATIONSHIP")
	private String relationShip;
	
	@NotBlank
	@Column(name = "TORES")
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

	public String getRelationShip() {
		return relationShip;
	}

	public void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToRes() {
		return toRes;
	}

	public void setToRes(String toRes) {
		this.toRes = toRes;
	}
		
}