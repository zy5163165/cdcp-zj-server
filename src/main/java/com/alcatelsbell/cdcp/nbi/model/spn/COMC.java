package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_OMC")

public class COMC extends CdcpObject {
	
	String rmUID;
	String nativeName;
	String commuAdress;
	String omcVersion;
	String interfaceVersion;
	String equipmentDomain;
	String vendor;
	
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
	public String getCommuAdress() {
		return commuAdress;
	}
	public void setCommuAdress(String commuAdress) {
		this.commuAdress = commuAdress;
	}
	public String getOmcVersion() {
		return omcVersion;
	}
	public void setOmcVersion(String omcVersion) {
		this.omcVersion = omcVersion;
	}
	public String getInterfaceVersion() {
		return interfaceVersion;
	}
	public void setInterfaceVersion(String interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}
	public String getEquipmentDomain() {
		return equipmentDomain;
	}
	public void setEquipmentDomain(String equipmentDomain) {
		this.equipmentDomain = equipmentDomain;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	
	
	
}
