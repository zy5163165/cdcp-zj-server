package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_PWP")

public class CPWP extends CdcpObject {
	String rmUID;
	String carriePwrmUID;
	String number;
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getCarriePwrmUID() {
		return carriePwrmUID;
	}
	public void setCarriePwrmUID(String carriePwrmUID) {
		this.carriePwrmUID = carriePwrmUID;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	
}
