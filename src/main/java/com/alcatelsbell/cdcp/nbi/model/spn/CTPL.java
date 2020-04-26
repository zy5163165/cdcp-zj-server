package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_TPL")

public class CTPL extends CdcpObject {
	String rmUID;
	String nativeName;
	String aEndNermUID;
	String zEndNermUID;
	String aEndPortrmUID;
	String zEndPortrmUID;
	String direction;
	String aEndCtprmUID;
	String zEndCtprmUID;
	String ctpType;
	String rate;
	String reality;



	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getReality() {
		return reality;
	}

	public void setReality(String reality) {
		this.reality = reality;
	}

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

	public String getaEndNermUID() {
		return aEndNermUID;
	}

	public void setaEndNermUID(String aEndNermUID) {
		this.aEndNermUID = aEndNermUID;
	}

	public String getzEndNermUID() {
		return zEndNermUID;
	}

	public void setzEndNermUID(String zEndNermUID) {
		this.zEndNermUID = zEndNermUID;
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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getaEndCtprmUID() {
		return aEndCtprmUID;
	}

	public void setaEndCtprmUID(String aEndCtprmUID) {
		this.aEndCtprmUID = aEndCtprmUID;
	}

	public String getzEndCtprmUID() {
		return zEndCtprmUID;
	}

	public void setzEndCtprmUID(String zEndCtprmUID) {
		this.zEndCtprmUID = zEndCtprmUID;
	}

	public String getCtpType() {
		return ctpType;
	}

	public void setCtpType(String ctpType) {
		this.ctpType = ctpType;
	}
}
