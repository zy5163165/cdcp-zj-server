package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_TNL")

public class CTNL extends CdcpObject {
	String rmUID;
	String nativeName;
	String direction;
	String activeState;
	String aEndTprmUID;
	String aEndNermUID;
	String aEndPortrmUID;
	String aEndOutLabel;
	String aEndRevInLabel;
	String zEndTprmUID;
	String zEndNermUID;
	String zEndPortrmUID;
	String zEndInLabel;
	String zEndRevOutLabel;
	String CIR;
	String RevCIR;
	String PIR;
	String RevPIR;
	String isOverlay;
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
	public String getaEndTprmUID() {
		return aEndTprmUID;
	}
	public void setaEndTprmUID(String aEndTprmUID) {
		this.aEndTprmUID = aEndTprmUID;
	}
	public String getaEndNermUID() {
		return aEndNermUID;
	}
	public void setaEndNermUID(String aEndNermUID) {
		this.aEndNermUID = aEndNermUID;
	}
	public String getaEndPortrmUID() {
		return aEndPortrmUID;
	}
	public void setaEndPortrmUID(String aEndPortrmUID) {
		this.aEndPortrmUID = aEndPortrmUID;
	}
	public String getaEndOutLabel() {
		return aEndOutLabel;
	}
	public void setaEndOutLabel(String aEndOutLabel) {
		this.aEndOutLabel = aEndOutLabel;
	}
	public String getaEndRevInLabel() {
		return aEndRevInLabel;
	}
	public void setaEndRevInLabel(String aEndRevInLabel) {
		this.aEndRevInLabel = aEndRevInLabel;
	}
	public String getzEndTprmUID() {
		return zEndTprmUID;
	}
	public void setzEndTprmUID(String zEndTprmUID) {
		this.zEndTprmUID = zEndTprmUID;
	}
	public String getzEndNermUID() {
		return zEndNermUID;
	}
	public void setzEndNermUID(String zEndNermUID) {
		this.zEndNermUID = zEndNermUID;
	}
	public String getzEndPortrmUID() {
		return zEndPortrmUID;
	}
	public void setzEndPortrmUID(String zEndPortrmUID) {
		this.zEndPortrmUID = zEndPortrmUID;
	}
	public String getzEndInLabel() {
		return zEndInLabel;
	}
	public void setzEndInLabel(String zEndInLabel) {
		this.zEndInLabel = zEndInLabel;
	}
	public String getzEndRevOutLabel() {
		return zEndRevOutLabel;
	}
	public void setzEndRevOutLabel(String zEndRevOutLabel) {
		this.zEndRevOutLabel = zEndRevOutLabel;
	}
	public String getCIR() {
		return CIR;
	}
	public void setCIR(String cIR) {
		CIR = cIR;
	}
	public String getRevCIR() {
		return RevCIR;
	}
	public void setRevCIR(String revCIR) {
		RevCIR = revCIR;
	}
	public String getPIR() {
		return PIR;
	}
	public void setPIR(String pIR) {
		PIR = pIR;
	}
	public String getRevPIR() {
		return RevPIR;
	}
	public void setRevPIR(String revPIR) {
		RevPIR = revPIR;
	}
	public String getIsOverlay() {
		return isOverlay;
	}
	public void setIsOverlay(String isOverlay) {
		this.isOverlay = isOverlay;
	}

	
}
