package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_PWP")

public class CPWP extends CdcpObject {
	String rmUID;
	String carriePwrmUID;
	String numbers;
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
	public String getNumbers() {
		return numbers;
	}
	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	
}
