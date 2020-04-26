package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_NWS")

public class CNWS extends CdcpObject {
	String rmUID;
	String nativeName;
	String type;
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getNativeName() {
		return nativeName;
	}
	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	
}
