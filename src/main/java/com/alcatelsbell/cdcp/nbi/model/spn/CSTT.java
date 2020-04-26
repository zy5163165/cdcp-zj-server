package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_STT")

public class CSTT extends CdcpObject {
	String rmUID;
	String nativeName;
	String activeState;
	String aEndNermUID;
	String aEndIP;
	String zEndNermUID;
	String zEndIP;
	String apsEnable;
	String reversionMode;
	String protectionType;
	String vNetrmUID;
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
	public String getActiveState() {
		return activeState;
	}
	public void setActiveState(String activeState) {
		this.activeState = activeState;
	}
	public String getaEndNermUID() {
		return aEndNermUID;
	}
	public void setaEndNermUID(String aEndNermUID) {
		this.aEndNermUID = aEndNermUID;
	}
	public String getaEndIP() {
		return aEndIP;
	}
	public void setaEndIP(String aEndIP) {
		this.aEndIP = aEndIP;
	}
	public String getzEndNermUID() {
		return zEndNermUID;
	}
	public void setzEndNermUID(String zEndNermUID) {
		this.zEndNermUID = zEndNermUID;
	}
	public String getzEndIP() {
		return zEndIP;
	}
	public void setzEndIP(String zEndIP) {
		this.zEndIP = zEndIP;
	}
	public String getApsEnable() {
		return apsEnable;
	}
	public void setApsEnable(String apsEnable) {
		this.apsEnable = apsEnable;
	}
	public String getReversionMode() {
		return reversionMode;
	}
	public void setReversionMode(String reversionMode) {
		this.reversionMode = reversionMode;
	}
	public String getProtectionType() {
		return protectionType;
	}
	public void setProtectionType(String protectionType) {
		this.protectionType = protectionType;
	}
	public String getvNetrmUID() {
		return vNetrmUID;
	}
	public void setvNetrmUID(String vNetrmUID) {
		this.vNetrmUID = vNetrmUID;
	}

	
}
