package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_EPG")

public class CEPG extends CdcpObject {
	
	String rmUID;
	String nermUID;
	String nativeName;
	String reversionMode;
	String type;
	
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getNermUID() {
		return nermUID;
	}
	public void setNermUID(String nermUID) {
		this.nermUID = nermUID;
	}
	public String getNativeName() {
		return nativeName;
	}
	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}
	public String getReversionMode() {
		return reversionMode;
	}
	public void setReversionMode(String reversionMode) {
		this.reversionMode = reversionMode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
