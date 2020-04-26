package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_ETH")

public class CETH extends CdcpObject {
	String rmUID;
	String nativeName;
	String serviceType;
	String direction;
	String owner;
	String owneSserviceType;
	String activeState;
	String CIR;
	String PIR;
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
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getOwneSserviceType() {
		return owneSserviceType;
	}
	public void setOwneSserviceType(String owneSserviceType) {
		this.owneSserviceType = owneSserviceType;
	}
	public String getActiveState() {
		return activeState;
	}
	public void setActiveState(String activeState) {
		this.activeState = activeState;
	}
	public String getCIR() {
		return CIR;
	}
	public void setCIR(String cIR) {
		CIR = cIR;
	}
	public String getPIR() {
		return PIR;
	}
	public void setPIR(String pIR) {
		PIR = pIR;
	}

	
}
