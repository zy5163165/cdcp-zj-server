package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_MGB")

public class CMGB extends CdcpObject {
	String rmUID;
	String phyPortrmUID;
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getPhyPortrmUID() {
		return phyPortrmUID;
	}
	public void setPhyPortrmUID(String phyPortrmUID) {
		this.phyPortrmUID = phyPortrmUID;
	}

	
}
