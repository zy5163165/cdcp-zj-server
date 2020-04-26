package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_ESI")

public class CESI extends CdcpObject {
	String rmUID;
	String pwrmUID;
	String aEndNermUID;
	String zEndNermUID;
	String role;
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getPwrmUID() {
		return pwrmUID;
	}
	public void setPwrmUID(String pwrmUID) {
		this.pwrmUID = pwrmUID;
	}
	public String getaEndNermUID() {
		return aEndNermUID;
	}
	public void setaEndNermUID(String aEndNermUID) {
		this.aEndNermUID = aEndNermUID;
	}
	public String getzEndNermUID() {
		return zEndNermUID;
	}
	public void setzEndNermUID(String zEndNermUID) {
		this.zEndNermUID = zEndNermUID;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	
}
