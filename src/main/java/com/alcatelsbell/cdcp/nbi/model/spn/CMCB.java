package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_MCB")

public class CMCB extends CdcpObject {
	String rmUID;
	String MtnChannelrmUID;
	String role;
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getMtnChannelrmUID() {
		return MtnChannelrmUID;
	}
	public void setMtnChannelrmUID(String mtnChannelrmUID) {
		MtnChannelrmUID = mtnChannelrmUID;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	
}
