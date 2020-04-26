package com.alcatelsbell.cdcp.nbi.model.spn;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.alcatelsbell.cdcp.nbi.model.CdcpObject;

@Entity
@Table(name = "C_PRT")

public class CPRT extends CdcpObject {
	
	String rmUID;
	String nermUID;
	String cardrmUID;
	String holderrmUID;
	String portNo;
	String nativeName;
	String physicalOrLogical;
	String signalType;
	String portRate;
	String direction;
	String waveNum;
	String portType;
	String portSubType;
	String role;
	String isOverlay;
	String vNetrmUID;
	String MtnGrouprmUID;
	String inLaserUpthreshold;
	String inLaserDownthreshold;
	String outLaserUpthreshold;
	String outLaserDownthreshold;
	String IPAddress;
	String IPMask;
	String IPV6;
	
	
	public String getIPAddress() {
		return IPAddress;
	}
	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
	public String getIPMask() {
		return IPMask;
	}
	public void setIPMask(String iPMask) {
		IPMask = iPMask;
	}
	public String getIPV6() {
		return IPV6;
	}
	public void setIPV6(String iPV6) {
		IPV6 = iPV6;
	}
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
	public String getCardrmUID() {
		return cardrmUID;
	}
	public void setCardrmUID(String cardrmUID) {
		this.cardrmUID = cardrmUID;
	}
	public String getHolderrmUID() {
		return holderrmUID;
	}
	public void setHolderrmUID(String holderrmUID) {
		this.holderrmUID = holderrmUID;
	}
	public String getPortNo() {
		return portNo;
	}
	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}
	public String getNativeName() {
		return nativeName;
	}
	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}
	public String getPhysicalOrLogical() {
		return physicalOrLogical;
	}
	public void setPhysicalOrLogical(String physicalOrLogical) {
		this.physicalOrLogical = physicalOrLogical;
	}
	public String getSignalType() {
		return signalType;
	}
	public void setSignalType(String signalType) {
		this.signalType = signalType;
	}
	public String getPortRate() {
		return portRate;
	}
	public void setPortRate(String portRate) {
		this.portRate = portRate;
	}
	public String getDirection() {
		return direction;
	}
	public String getPortSubType() {
		return portSubType;
	}
	public void setPortSubType(String portSubType) {
		this.portSubType = portSubType;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getIsOverlay() {
		return isOverlay;
	}
	public void setIsOverlay(String isOverlay) {
		this.isOverlay = isOverlay;
	}
	public String getvNetrmUID() {
		return vNetrmUID;
	}
	public void setvNetrmUID(String vNetrmUID) {
		this.vNetrmUID = vNetrmUID;
	}
	public String getMtnGrouprmUID() {
		return MtnGrouprmUID;
	}
	public void setMtnGrouprmUID(String mtnGrouprmUID) {
		MtnGrouprmUID = mtnGrouprmUID;
	}
	public String getInLaserUpthreshold() {
		return inLaserUpthreshold;
	}
	public void setInLaserUpthreshold(String inLaserUpthreshold) {
		this.inLaserUpthreshold = inLaserUpthreshold;
	}
	public String getInLaserDownthreshold() {
		return inLaserDownthreshold;
	}
	public void setInLaserDownthreshold(String inLaserDownthreshold) {
		this.inLaserDownthreshold = inLaserDownthreshold;
	}
	public String getOutLaserUpthreshold() {
		return outLaserUpthreshold;
	}
	public void setOutLaserUpthreshold(String outLaserUpthreshold) {
		this.outLaserUpthreshold = outLaserUpthreshold;
	}
	public String getOutLaserDownthreshold() {
		return outLaserDownthreshold;
	}
	public void setOutLaserDownthreshold(String outLaserDownthreshold) {
		this.outLaserDownthreshold = outLaserDownthreshold;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getWaveNum() {
		return waveNum;
	}
	public void setWaveNum(String waveNum) {
		this.waveNum = waveNum;
	}
	public String getPortType() {
		return portType;
	}
	public void setPortType(String portType) {
		this.portType = portType;
	}
	
	
}
