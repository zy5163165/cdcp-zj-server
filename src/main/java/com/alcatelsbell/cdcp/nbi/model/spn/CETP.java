package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_ETP")

public class CETP extends CdcpObject {
	String rmUID;

	public String getRmUID() {
		return rmUID;
	}

	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	
}
