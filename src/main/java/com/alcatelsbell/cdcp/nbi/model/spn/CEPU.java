package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_EPU")

public class CEPU extends CdcpObject {
	
	String rmUID;
	String grouprmUID;
	String cardrmUID;
	String role;
	
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getGrouprmUID() {
		return grouprmUID;
	}
	public void setGrouprmUID(String grouprmUID) {
		this.grouprmUID = grouprmUID;
	}
	public String getCardrmUID() {
		return cardrmUID;
	}
	public void setCardrmUID(String cardrmUID) {
		this.cardrmUID = cardrmUID;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
	
}
