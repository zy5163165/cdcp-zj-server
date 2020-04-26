package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_BRD")

public class CBRD extends CdcpObject {
	String rmUID;
	String nativeName;
	String aEndrmUID;
	String zEndrmUID;
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
	public String getaEndrmUID() {
		return aEndrmUID;
	}
	public void setaEndrmUID(String aEndrmUID) {
		this.aEndrmUID = aEndrmUID;
	}
	public String getzEndrmUID() {
		return zEndrmUID;
	}
	public void setzEndrmUID(String zEndrmUID) {
		this.zEndrmUID = zEndrmUID;
	}

	
}
