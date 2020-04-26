package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_L3T")

public class CL3T extends CdcpObject {
	String rmUID;
	String tunnelrmUID;
	String isTunnelGroup;
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getTunnelrmUID() {
		return tunnelrmUID;
	}
	public void setTunnelrmUID(String tunnelrmUID) {
		this.tunnelrmUID = tunnelrmUID;
	}
	public String getIsTunnelGroup() {
		return isTunnelGroup;
	}
	public void setIsTunnelGroup(String isTunnelGroup) {
		this.isTunnelGroup = isTunnelGroup;
	}

	
}
