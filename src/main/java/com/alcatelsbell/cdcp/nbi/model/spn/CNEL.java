package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_NEL")

public class CNEL extends CdcpObject {
	
	String rmUID;
	String nativeName;
	String location;
	String productName;
	String vendor;
	String reality;
	String IPAddress;
	String hardwareVersion;
	String softwareVersion;
	String state;
	String neType;
	String onermUID;
	String controlPlaneIP;
	String maxCapacity;
	
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getReality() {
		return reality;
	}
	public void setReality(String reality) {
		this.reality = reality;
	}
	public String getIPAddress() {
		return IPAddress;
	}
	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
	public String getHardwareVersion() {
		return hardwareVersion;
	}
	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getNeType() {
		return neType;
	}
	public void setNeType(String neType) {
		this.neType = neType;
	}
	public String getOnermUID() {
		return onermUID;
	}
	public void setOnermUID(String onermUID) {
		this.onermUID = onermUID;
	}
	public String getControlPlaneIP() {
		return controlPlaneIP;
	}
	public void setControlPlaneIP(String controlPlaneIP) {
		this.controlPlaneIP = controlPlaneIP;
	}
	public String getMaxCapacity() {
		return maxCapacity;
	}
	public void setMaxCapacity(String maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	
	
	
}
