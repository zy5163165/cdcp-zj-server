package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_PRB")

public class CPRB extends CdcpObject {
	
	String rmUID;
	String nermUID;
	String phyPortrmUID;
	String phyPortParentCardrmUID;
	String logPortParentCardrmUID;
	
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
	public String getPhyPortrmUID() {
		return phyPortrmUID;
	}
	public void setPhyPortrmUID(String phyPortrmUID) {
		this.phyPortrmUID = phyPortrmUID;
	}
	public String getPhyPortParentCardrmUID() {
		return phyPortParentCardrmUID;
	}
	public void setPhyPortParentCardrmUID(String phyPortParentCardrmUID) {
		this.phyPortParentCardrmUID = phyPortParentCardrmUID;
	}
	public String getLogPortParentCardrmUID() {
		return logPortParentCardrmUID;
	}
	public void setLogPortParentCardrmUID(String logPortParentCardrmUID) {
		this.logPortParentCardrmUID = logPortParentCardrmUID;
	}
	
	
	

	
}
