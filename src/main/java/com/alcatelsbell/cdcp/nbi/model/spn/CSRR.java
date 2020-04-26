package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_SRR")

public class CSRR extends CdcpObject {
	String rmUID;
	String srTunnelrmUID;
	String routingNo;
	String aEndPortrmUID;
	String zEndPortrmUID;
	String nermUID;
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getSrTunnelrmUID() {
		return srTunnelrmUID;
	}
	public void setSrTunnelrmUID(String srTunnelrmUID) {
		this.srTunnelrmUID = srTunnelrmUID;
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
	public String getzEndPortrmUID() {
		return zEndPortrmUID;
	}
	public void setzEndPortrmUID(String zEndPortrmUID) {
		this.zEndPortrmUID = zEndPortrmUID;
	}
	public String getNermUID() {
		return nermUID;
	}
	public void setNermUID(String nermUID) {
		this.nermUID = nermUID;
	}

	
}
