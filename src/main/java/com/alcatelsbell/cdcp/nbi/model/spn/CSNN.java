package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_SNN")

public class CSNN extends CdcpObject {
	
	String rmUID;
	String subnetrmUID;
	String xPos;
	String yPos;
	
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getSubnetrmUID() {
		return subnetrmUID;
	}
	public void setSubnetrmUID(String subnetrmUID) {
		this.subnetrmUID = subnetrmUID;
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
