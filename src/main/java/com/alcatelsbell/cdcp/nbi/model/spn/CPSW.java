package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_PSW")

public class CPSW extends CdcpObject {
	String rmUID;
	String direction;
	String aEndTprmUID;
	String aEndNermUID;
	String zEndTprmUID;
	String zEndNermUID;
	String nativeName;
	String aEndPortrmUID;
	String zEndPortrmUID;
	String activeState;
	String aEndIngressCIR;
	String aEndIngressPIR;
	String aEndEgressCIR;
	String aEndEgressPIR;
	String zEndIngressCIR;
	String zEndIngressPIR;
	String zEndEgressCIR;
	String zEndEgressPIR;
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
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
	public String getNativeName() {
		return nativeName;
	}
	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}
	public String getaEndPortrmUID() {
		return aEndPortrmUID;
	}
	public void setaEndPortrmUID(String aEndPortrmUID) {
		this.aEndPortrmUID = aEndPortrmUID;
	}
	public String getzEndPortrmUID() {
		return zEndPortrmUID;
	}
	public void setzEndPortrmUID(String zEndPortrmUID) {
		this.zEndPortrmUID = zEndPortrmUID;
	}
	public String getActiveState() {
		return activeState;
	}
	public void setActiveState(String activeState) {
		this.activeState = activeState;
	}
	public String getaEndIngressCIR() {
		return aEndIngressCIR;
	}
	public void setaEndIngressCIR(String aEndIngressCIR) {
		this.aEndIngressCIR = aEndIngressCIR;
	}
	public String getaEndIngressPIR() {
		return aEndIngressPIR;
	}
	public void setaEndIngressPIR(String aEndIngressPIR) {
		this.aEndIngressPIR = aEndIngressPIR;
	}
	public String getaEndEgressCIR() {
		return aEndEgressCIR;
	}
	public void setaEndEgressCIR(String aEndEgressCIR) {
		this.aEndEgressCIR = aEndEgressCIR;
	}
	public String getaEndEgressPIR() {
		return aEndEgressPIR;
	}
	public void setaEndEgressPIR(String aEndEgressPIR) {
		this.aEndEgressPIR = aEndEgressPIR;
	}
	public String getzEndIngressCIR() {
		return zEndIngressCIR;
	}
	public void setzEndIngressCIR(String zEndIngressCIR) {
		this.zEndIngressCIR = zEndIngressCIR;
	}
	public String getzEndIngressPIR() {
		return zEndIngressPIR;
	}
	public void setzEndIngressPIR(String zEndIngressPIR) {
		this.zEndIngressPIR = zEndIngressPIR;
	}
	public String getzEndEgressCIR() {
		return zEndEgressCIR;
	}
	public void setzEndEgressCIR(String zEndEgressCIR) {
		this.zEndEgressCIR = zEndEgressCIR;
	}
	public String getzEndEgressPIR() {
		return zEndEgressPIR;
	}
	public void setzEndEgressPIR(String zEndEgressPIR) {
		this.zEndEgressPIR = zEndEgressPIR;
	}

	
}
