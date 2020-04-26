package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_EQH")

public class CEQH extends CdcpObject {
	
	String rmUID;
	String nermUID;
	String nativeName;
	String holderNumber;
	String holderState;
	String holderType;
	String parentHolderrmUID;
	String productName;
	
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
	public String getNativeName() {
		return nativeName;
	}
	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}
	public String getHolderNumber() {
		return holderNumber;
	}
	public void setHolderNumber(String holderNumber) {
		this.holderNumber = holderNumber;
	}
	public String getHolderState() {
		return holderState;
	}
	public void setHolderState(String holderState) {
		this.holderState = holderState;
	}
	public String getHolderType() {
		return holderType;
	}
	public void setHolderType(String holderType) {
		this.holderType = holderType;
	}
	public String getParentHolderrmUID() {
		return parentHolderrmUID;
	}
	public void setParentHolderrmUID(String parentHolderrmUID) {
		this.parentHolderrmUID = parentHolderrmUID;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	

}
