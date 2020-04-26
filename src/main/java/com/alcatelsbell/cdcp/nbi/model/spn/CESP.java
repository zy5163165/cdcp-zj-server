package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_ESP")

public class CESP extends CdcpObject {
	String rmUID;
	String servicermUID;
	String nermUID;
	String portrmUID;
	String CVID;
	String SVID;
	String ingressCIR;
	String ingressPIR;
	String egressCIR;
	String egressPIR;
	public String getRmUID() {
		return rmUID;
	}
	public void setRmUID(String rmUID) {
		this.rmUID = rmUID;
	}
	public String getServicermUID() {
		return servicermUID;
	}
	public void setServicermUID(String servicermUID) {
		this.servicermUID = servicermUID;
	}
	public String getNermUID() {
		return nermUID;
	}
	public void setNermUID(String nermUID) {
		this.nermUID = nermUID;
	}
	public String getPortrmUID() {
		return portrmUID;
	}
	public void setPortrmUID(String portrmUID) {
		this.portrmUID = portrmUID;
	}
	public String getCVID() {
		return CVID;
	}
	public void setCVID(String cVID) {
		CVID = cVID;
	}
	public String getSVID() {
		return SVID;
	}
	public void setSVID(String sVID) {
		SVID = sVID;
	}
	public String getIngressCIR() {
		return ingressCIR;
	}
	public void setIngressCIR(String ingressCIR) {
		this.ingressCIR = ingressCIR;
	}
	public String getIngressPIR() {
		return ingressPIR;
	}
	public void setIngressPIR(String ingressPIR) {
		this.ingressPIR = ingressPIR;
	}
	public String getEgressCIR() {
		return egressCIR;
	}
	public void setEgressCIR(String egressCIR) {
		this.egressCIR = egressCIR;
	}
	public String getEgressPIR() {
		return egressPIR;
	}
	public void setEgressPIR(String egressPIR) {
		this.egressPIR = egressPIR;
	}

	
}
