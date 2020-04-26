package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_SBN")

public class CSBN extends CdcpObject {
	
	String rmUID;
	String nativeName;
	String parentSubnetrmUID;
	String xPos;
	String yPos;

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
	public String getParentSubnetrmUID() {
		return parentSubnetrmUID;
	}
	public void setParentSubnetrmUID(String parentSubnetrmUID) {
		this.parentSubnetrmUID = parentSubnetrmUID;
	}
	public String getxPos() {
		return xPos;
	}
	public void setxPos(String xPos) {
		this.xPos = xPos;
	}
	public String getyPos() {
		return yPos;
	}
	public void setyPos(String yPos) {
		this.yPos = yPos;
	}
	
}
