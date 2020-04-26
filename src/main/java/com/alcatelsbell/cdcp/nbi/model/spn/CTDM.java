package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_TDM")

public class CTDM extends CdcpObject {
	String rmUID;
	String nativeName;
	String rate;
	String direction;
	String activeState;
	String owner;
	String owneSserviceType;
	String aEnd1TprmUID;
	String aEnd1NermUID;
	String aEnd1PortrmUID;
	String aEnd1CTPID;
	String aEnd2TprmUID;
	String aEnd2NermUID;
	String aEnd2PortrmUID;
	String aEnd2CTPID;
	String zEnd1TprmUID;
	String zEnd1NermUID;
	String zEnd1PortrmUID;
	String zEnd1CTPID;
	String zEnd2TprmUID;
	String zEnd2NermUID;
	String zEnd2PortrmUID;
	String zEnd2CTPID;
	String PW1rmUID;
	String PW2rmUID;
	String PW3rmUID;
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
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getActiveState() {
		return activeState;
	}
	public void setActiveState(String activeState) {
		this.activeState = activeState;
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
	public String getaEnd1TprmUID() {
		return aEnd1TprmUID;
	}
	public void setaEnd1TprmUID(String aEnd1TprmUID) {
		this.aEnd1TprmUID = aEnd1TprmUID;
	}
	public String getaEnd1NermUID() {
		return aEnd1NermUID;
	}
	public void setaEnd1NermUID(String aEnd1NermUID) {
		this.aEnd1NermUID = aEnd1NermUID;
	}
	public String getaEnd1PortrmUID() {
		return aEnd1PortrmUID;
	}
	public void setaEnd1PortrmUID(String aEnd1PortrmUID) {
		this.aEnd1PortrmUID = aEnd1PortrmUID;
	}
	public String getaEnd1CTPID() {
		return aEnd1CTPID;
	}
	public void setaEnd1CTPID(String aEnd1CTPID) {
		this.aEnd1CTPID = aEnd1CTPID;
	}
	public String getaEnd2TprmUID() {
		return aEnd2TprmUID;
	}
	public void setaEnd2TprmUID(String aEnd2TprmUID) {
		this.aEnd2TprmUID = aEnd2TprmUID;
	}
	public String getaEnd2NermUID() {
		return aEnd2NermUID;
	}
	public void setaEnd2NermUID(String aEnd2NermUID) {
		this.aEnd2NermUID = aEnd2NermUID;
	}
	public String getaEnd2PortrmUID() {
		return aEnd2PortrmUID;
	}
	public void setaEnd2PortrmUID(String aEnd2PortrmUID) {
		this.aEnd2PortrmUID = aEnd2PortrmUID;
	}
	public String getaEnd2CTPID() {
		return aEnd2CTPID;
	}
	public void setaEnd2CTPID(String aEnd2CTPID) {
		this.aEnd2CTPID = aEnd2CTPID;
	}
	public String getzEnd1TprmUID() {
		return zEnd1TprmUID;
	}
	public void setzEnd1TprmUID(String zEnd1TprmUID) {
		this.zEnd1TprmUID = zEnd1TprmUID;
	}
	public String getzEnd1NermUID() {
		return zEnd1NermUID;
	}
	public void setzEnd1NermUID(String zEnd1NermUID) {
		this.zEnd1NermUID = zEnd1NermUID;
	}
	public String getzEnd1PortrmUID() {
		return zEnd1PortrmUID;
	}
	public void setzEnd1PortrmUID(String zEnd1PortrmUID) {
		this.zEnd1PortrmUID = zEnd1PortrmUID;
	}
	public String getzEnd1CTPID() {
		return zEnd1CTPID;
	}
	public void setzEnd1CTPID(String zEnd1CTPID) {
		this.zEnd1CTPID = zEnd1CTPID;
	}
	public String getzEnd2TprmUID() {
		return zEnd2TprmUID;
	}
	public void setzEnd2TprmUID(String zEnd2TprmUID) {
		this.zEnd2TprmUID = zEnd2TprmUID;
	}
	public String getzEnd2NermUID() {
		return zEnd2NermUID;
	}
	public void setzEnd2NermUID(String zEnd2NermUID) {
		this.zEnd2NermUID = zEnd2NermUID;
	}
	public String getzEnd2PortrmUID() {
		return zEnd2PortrmUID;
	}
	public void setzEnd2PortrmUID(String zEnd2PortrmUID) {
		this.zEnd2PortrmUID = zEnd2PortrmUID;
	}
	public String getzEnd2CTPID() {
		return zEnd2CTPID;
	}
	public void setzEnd2CTPID(String zEnd2CTPID) {
		this.zEnd2CTPID = zEnd2CTPID;
	}
	public String getPW1rmUID() {
		return PW1rmUID;
	}
	public void setPW1rmUID(String pW1rmUID) {
		PW1rmUID = pW1rmUID;
	}
	public String getPW2rmUID() {
		return PW2rmUID;
	}
	public void setPW2rmUID(String pW2rmUID) {
		PW2rmUID = pW2rmUID;
	}
	public String getPW3rmUID() {
		return PW3rmUID;
	}
	public void setPW3rmUID(String pW3rmUID) {
		PW3rmUID = pW3rmUID;
	}





	
}
