package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_LBS")

public class CLBS extends CdcpObject {
	String rmUID;
	String tunnelrmUID;
	String direction;
	String routingGroup;
	String routingNo;
	String aEndPortrmUID;
	String aEndTprmUID;
	String aEndOutLabel;
	String aEndRevInLabel;
	String zEndPortrmUID;
	String aUVID;
	String zEndTprmUID;
	String zEndInLabel;
	String zEndRevOutLabel;
	String nermUID;
	String zUVID;
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getTunnelrmUID() {
		return tunnelrmUID;
	}
	public void setTunnelrmUID(String tunnelrmUID) {
		this.tunnelrmUID = tunnelrmUID;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getRoutingGroup() {
		return routingGroup;
	}
	public void setRoutingGroup(String routingGroup) {
		this.routingGroup = routingGroup;
	}
	public String getRoutingNo() {
		return routingNo;
	}
	public void setRoutingNo(String routingNo) {
		this.routingNo = routingNo;
	}
	public String getaEndPortrmUID() {
		return aEndPortrmUID;
	}
	public void setaEndPortrmUID(String aEndPortrmUID) {
		this.aEndPortrmUID = aEndPortrmUID;
	}
	public String getaEndTprmUID() {
		return aEndTprmUID;
	}
	public void setaEndTprmUID(String aEndTprmUID) {
		this.aEndTprmUID = aEndTprmUID;
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
	public String getzEndPortrmUID() {
		return zEndPortrmUID;
	}
	public void setzEndPortrmUID(String zEndPortrmUID) {
		this.zEndPortrmUID = zEndPortrmUID;
	}
	public String getaUVID() {
		return aUVID;
	}
	public void setaUVID(String aUVID) {
		this.aUVID = aUVID;
	}
	public String getzEndTprmUID() {
		return zEndTprmUID;
	}
	public void setzEndTprmUID(String zEndTprmUID) {
		this.zEndTprmUID = zEndTprmUID;
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
	public String getNermUID() {
		return nermUID;
	}
	public void setNermUID(String nermUID) {
		this.nermUID = nermUID;
	}
	public String getzUVID() {
		return zUVID;
	}
	public void setzUVID(String zUVID) {
		this.zUVID = zUVID;
	}

	
}
