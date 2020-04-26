package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_SRT")

public class CSRT extends CdcpObject {
	String rmUID;
	String srTunnelTrailrmUID;
	String nativeName;
	String direction;
	String state;
	String aEndNermUID;
	String aEndIP;
	String zEndNermUID;
	String zEndIP;
	String CIR;
	String latency;
	String signalType;
	String role;
	String routingPinningType;
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getSrTunnelTrailrmUID() {
		return srTunnelTrailrmUID;
	}
	public void setSrTunnelTrailrmUID(String srTunnelTrailrmUID) {
		this.srTunnelTrailrmUID = srTunnelTrailrmUID;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	public String getCIR() {
		return CIR;
	}
	public void setCIR(String cIR) {
		CIR = cIR;
	}
	public String getLatency() {
		return latency;
	}
	public void setLatency(String latency) {
		this.latency = latency;
	}
	public String getSignalType() {
		return signalType;
	}
	public void setSignalType(String signalType) {
		this.signalType = signalType;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getRoutingPinningType() {
		return routingPinningType;
	}
	public void setRoutingPinningType(String routingPinningType) {
		this.routingPinningType = routingPinningType;
	}

	
}
