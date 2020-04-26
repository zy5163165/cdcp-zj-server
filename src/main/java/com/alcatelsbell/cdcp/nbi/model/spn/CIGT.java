package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_IGT")

public class CIGT extends CdcpObject {
	String rmUID;
	String vNetrmUID;
	String nativeName;
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getvNetrmUID() {
		return vNetrmUID;
	}
	public void setvNetrmUID(String vNetrmUID) {
		this.vNetrmUID = vNetrmUID;
	}
	public String getNativeName() {
		return nativeName;
	}
	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}

	
}
