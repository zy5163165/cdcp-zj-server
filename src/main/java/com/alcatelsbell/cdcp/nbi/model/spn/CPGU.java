package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_PGU")

public class CPGU extends CdcpObject {
	
	String rmUID;
	String grouprmUID;
	String portrmUID;
	String role;
	
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getGrouprmUID() {
		return grouprmUID;
	}
	public void setGrouprmUID(String grouprmUID) {
		this.grouprmUID = grouprmUID;
	}
	public String getPortrmUID() {
		return portrmUID;
	}
	public void setPortrmUID(String portrmUID) {
		this.portrmUID = portrmUID;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
	
}
