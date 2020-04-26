package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_CRD")

public class CCRD extends CdcpObject {
	
	String rmUID;
	String nermUID;
	String holderrmUID;
	String nativeName;
	String cardType;
	String cardSubType;
	String softwareVersion;
	String hardwareVersion;
	String serialNumber;
	String serviceState;
	String role;
	
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
	public String getHolderrmUID() {
		return holderrmUID;
	}
	public void setHolderrmUID(String holderrmUID) {
		this.holderrmUID = holderrmUID;
	}
	public String getNativeName() {
		return nativeName;
	}
	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardSubType() {
		return cardSubType;
	}
	public void setCardSubType(String cardSubType) {
		this.cardSubType = cardSubType;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public String getHardwareVersion() {
		return hardwareVersion;
	}
	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getServiceState() {
		return serviceState;
	}
	public void setServiceState(String serviceState) {
		this.serviceState = serviceState;
	}
	
	

}
