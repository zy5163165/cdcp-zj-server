package com.alcatelsbell.cdcp.server.adapters.fenghuootnm2000;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.asb.mule.probe.framework.entity.CRD;
import org.asb.mule.probe.framework.entity.CTP2;
import org.asb.mule.probe.framework.entity.EQH;
import org.asb.mule.probe.framework.entity.NEL;
import org.asb.mule.probe.framework.entity.SBN;
import org.asb.mule.probe.framework.entity.SIF;
import org.asb.mule.probe.framework.entity.SNL;
import org.asb.mule.probe.framework.entity.SNN;
import org.asb.mule.probe.framework.entity.SNT;
import org.asb.mule.probe.framework.entity.TPL;
import org.asb.mule.probe.framework.util.FileLogger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alcatelsbell.cdcp.common.Constants;
import com.alcatelsbell.cdcp.nbi.model.CCTP;
import com.alcatelsbell.cdcp.nbi.model.CChannel;
import com.alcatelsbell.cdcp.nbi.model.CCrossConnect;
import com.alcatelsbell.cdcp.nbi.model.CDevice;
import com.alcatelsbell.cdcp.nbi.model.CEquipment;
import com.alcatelsbell.cdcp.nbi.model.CEthRoute;
import com.alcatelsbell.cdcp.nbi.model.CEthRoute_ETHTrunk;
import com.alcatelsbell.cdcp.nbi.model.CEthRoute_StaticRoute;
import com.alcatelsbell.cdcp.nbi.model.CEthTrunk;
import com.alcatelsbell.cdcp.nbi.model.CEthTrunk_SDHRoute;
import com.alcatelsbell.cdcp.nbi.model.CMP_CTP;
import com.alcatelsbell.cdcp.nbi.model.COMS_CC;
import com.alcatelsbell.cdcp.nbi.model.COMS_Section;
import com.alcatelsbell.cdcp.nbi.model.CPTP;
import com.alcatelsbell.cdcp.nbi.model.CPath;
import com.alcatelsbell.cdcp.nbi.model.CPath_CC;
import com.alcatelsbell.cdcp.nbi.model.CPath_Channel;
import com.alcatelsbell.cdcp.nbi.model.CPath_Section;
import com.alcatelsbell.cdcp.nbi.model.CRack;
import com.alcatelsbell.cdcp.nbi.model.CRoute;
import com.alcatelsbell.cdcp.nbi.model.CRoute_CC;
import com.alcatelsbell.cdcp.nbi.model.CRoute_Channel;
import com.alcatelsbell.cdcp.nbi.model.CRoute_Section;
import com.alcatelsbell.cdcp.nbi.model.CSection;
import com.alcatelsbell.cdcp.nbi.model.CShelf;
import com.alcatelsbell.cdcp.nbi.model.CSlot;
import com.alcatelsbell.cdcp.nbi.model.CStaticRoute;
import com.alcatelsbell.cdcp.nbi.model.CSubnetwork;
import com.alcatelsbell.cdcp.nbi.model.CSubnetworkDevice;
import com.alcatelsbell.cdcp.nbi.model.CTransmissionSystem;
import com.alcatelsbell.cdcp.nbi.model.CTransmissionSystem_Channel;
import com.alcatelsbell.cdcp.nbi.model.CVirtualBridge;
import com.alcatelsbell.cdcp.nbi.model.CdcpObject;
import com.alcatelsbell.cdcp.server.adapters.AbstractDBFLoader;
import com.alcatelsbell.cdcp.server.adapters.MigrateUtil;
import com.alcatelsbell.cdcp.util.DNUtil;
import com.alcatelsbell.cdcp.util.DSUtil;
import com.alcatelsbell.cdcp.util.DataInserter;
import com.alcatelsbell.cdcp.util.DatabaseUtil;
import com.alcatelsbell.cdcp.util.DicConst;
import com.alcatelsbell.cdcp.util.DicUtil;
import com.alcatelsbell.cdcp.util.MigrateThread;
import com.alcatelsbell.nms.common.Detect;
import com.alcatelsbell.nms.common.SysUtil;
import com.alcatelsbell.nms.db.components.service.JPASupportSpringImpl;
import com.alcatelsbell.nms.valueobject.BObject;

/**
 * 烽火OMC新网管入库(OTN)
 * Author: Zong Yu
 * Date: 18-7-11
 * Time: 下午4:43
 */
public class FHOTNM2000OTNnewMigrator extends AbstractDBFLoader{

    protected HashMap<String,String> deviceOMSRate = new HashMap<String, String>();
    HashMap<String,List<CCTP>> ctp_ctpMap = new HashMap<String, List<CCTP>>(); // ctp和下面的子ctp
    
    // sif/snl/tpl的actp-zctp
    HashMap<String,List<SIF>> azctp_sifMap = new HashMap<String, List<SIF>>();
    HashMap<String,List<SNL>> azctp_snlMap = new HashMap<String, List<SNL>>();
    HashMap<String,List<TPL>> azptp_tplMap = new HashMap<String, List<TPL>>();
    
    // oms/och/dsr的rmuid，用于遍历
    HashMap<String,List<SNL>> rmuid_omsMap = new HashMap<String, List<SNL>>();
    HashMap<String,List<SNL>> rmuid_ochMap = new HashMap<String, List<SNL>>();
    HashMap<String,List<SNL>> rmuid_dsrMap = new HashMap<String, List<SNL>>();
    
    // och/dsr下所有交叉的两端ctp
    HashMap<String,List<String>> ochUid_ccCtpMap = new HashMap<String, List<String>>();
    HashMap<String,List<String>> dsrUid_ccCtpMap = new HashMap<String, List<String>>();
    
    // oms下的交叉/ots(section)
    HashMap<String,List<COMS_CC>> omsUid_omsccMap = new HashMap<String, List<COMS_CC>>();
    HashMap<String,List<COMS_Section>> omsUid_omssectionMap = new HashMap<String, List<COMS_Section>>();
    
    // och下的交叉/ots(section)/波道
    HashMap<String,List<CPath_CC>> ochUid_pathccMap = new HashMap<String, List<CPath_CC>>();
    HashMap<String,List<CPath_Section>> ochUid_pathsectionMap = new HashMap<String, List<CPath_Section>>();
    HashMap<String,List<CPath_Channel>> ochUid_pathchannelMap = new HashMap<String, List<CPath_Channel>>();
    
    // oms下的波道、och下的子波道
    HashMap<String,List<CChannel>> oms_waveMap = new HashMap<String, List<CChannel>>();
    HashMap<String,List<CChannel>> och_subwaveMap = new HashMap<String, List<CChannel>>();
    
    List<String> dsrOchNoSubwave = new ArrayList<String>(); // dsr下的och，没有子波道
    
    HashMap<String,List<String>> dsr_snlMap = new HashMap<String, List<String>>(); // analysisRoutingForDSR方法存放已查到的snl
    HashMap<String,List<String>> dsr_snlOchMap = new HashMap<String, List<String>>(); // analysisRoutingForDSROch方法存放已查到的snl

    public FHOTNM2000OTNnewMigrator(String fileUrl, String emsdn) {
        this.fileUrl = fileUrl;
        this.emsdn = emsdn;
        MigrateThread.thread().initLog(emsdn + "." + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log");

    }

    private static FileLogger logger = new FileLogger("FH-OTN-Device.log");
    public FHOTNM2000OTNnewMigrator(Serializable object, String emsdn) {
        this.emsdn = emsdn;
        this.resultObject = object;
        MigrateThread.thread().initLog(logger);
    }
    
    private void testTime() {
    	
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(System.currentTimeMillis()));
        long t1 = System.currentTimeMillis();
        
        // 具体方法start
        System.out.println("7.同步CTP");
        try {
			migrateNewCTP();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // 具体方法end
        
        long t2 = System.currentTimeMillis();
		long t = (t2 - t1) / (3600000l);
		String unit = "Hours";
		if (t == 0) {
			t = (t2 - t1) / (60000l);
			unit = "Minutes";
			if (t == 0) {
				t = (t2 - t1) / (1000l);
				unit = "Seconds";
			}
		}
		System.out.println("================== "+t+unit+" [同步] =====================");
		System.out.println(df.format(System.currentTimeMillis()));
    }

    @Override
    public void doExecute() throws Exception {
        checkEMS(emsdn, "烽火");
        
//        testTime();

        logAction(emsdn + " migrateManagedElement", "同步网元", 5);
        System.out.println("1.同步网元");
        migrateManagedElement();
        
        logAction("migrateSubnet", "同步子网", 10);
        System.out.println("2.同步子网");
        migrateSubnetwork();
        migrateSubnetworkDevice();

        logAction("migrateEquipmentHolder", "同步槽道", 15);
        System.out.println("3.同步槽道");
        migrateEquipmentHolder();

        logAction("migrateCRD", "同步板卡", 20);
        System.out.println("4.同步板卡");
        migrateCRD();
        
        logAction("migratePRT", "同步端口", 25);
        System.out.println("5.同步端口");
        migratePRT();

        logAction("migrateSection", "同步段", 30);
        System.out.println("6.同步段");
        migrateSection();

        // 逻辑数据
        logAction("migrateNewCTP", "同步CTP", 40);
        System.out.println("7.同步CTP");
        migrateNewCTP();

        logAction("migrateRouteCC", "同步交叉", 50);
        System.out.println("8.同步交叉");
       	migrateRouteCC();
       	
       	logAction("migrateSNL", "同步子网连接", 60);
       	System.out.println("9.同步子网连接");
       	migrateSNL();
       	
       	logAction("migrateSNT", "同步路由", 70);
       	System.out.println("10.同步路由");
       	migrateSNT();
       	
       	
       	if (Detect.notEmpty(dsrOchNoSubwave)) {
       		for (String dsrOch : dsrOchNoSubwave) {
       			getLogger().info(dsrOch);
       			System.out.println(dsrOch);
       		}
       	}
       	
       	System.out.println("11. end");
//        migrateOMS();

        
        sd.release();
    }

    protected Class[] getStatClss() {
        return new Class[]{CCrossConnect.class, CChannel.class, CPath.class, CRoute.class, CPath_Channel.class,
                CPath_CC.class, CRoute_Channel.class,
                CRoute_CC.class, CSubnetwork.class, CSubnetworkDevice.class, CVirtualBridge.class,
                CMP_CTP.class, CEthTrunk.class, CStaticRoute.class, CEthRoute.class, CEthTrunk_SDHRoute.class,
                CEthRoute_StaticRoute.class, CEthRoute_ETHTrunk.class, CSection.class, CCTP.class, CDevice.class, CPTP.class, CTransmissionSystem.class, CTransmissionSystem_Channel.class};
    }


    // <-- 以下是新接口入库方法 -->
    /**
     * 同步网元
     */
    protected void migrateManagedElement() throws Exception {
		if (!isTableHasData(NEL.class))
			return;
		List<NEL> meList = sd.queryAll(NEL.class);
		if (meList == null || meList.isEmpty()) {
			getLogger().info("NEL is empty, return");
			return;
		}

		DataInserter di = new DataInserter(emsid);
		executeDelete("delete  from CDevice c where c.emsName = '" + emsdn + "'", CDevice.class);

		if (meList != null && meList.size() > 0) {
			for (NEL me : meList) {
				CDevice device = transNEL(me);
				device.setSid(DatabaseUtil.nextSID(device));
                if(device.getAdditionalInfo().length() > 2000){
                    device.setAdditionalInfo(null);
                }
				di.insert(device);
			}
		}
		di.end();
	}
    public CDevice transNEL(NEL me) {
        
        CDevice device = new CDevice();
		device.setEmsName(emsdn);
		device.setDn(me.getRmUID());//emsdn+"@"+
		device.setLocation(me.getLocation());
		device.setNativeEmsName(me.getNativeName());
		device.setNeVersion(me.getSoftwareVersion());
		device.setProductName(me.getProductName());
		device.setSupportedRates(null);
		device.setUserLabel("managedElement");
		device.setCollectTimepoint(me.getCreateDate());
		device.setIpAddress(me.getIPAddress());

		String additionalInfo = assembleAdditionalInfo(me);
		device.setAdditionalInfo(additionalInfo);
		
		// additionalInfo解析信息，当前未采集
//		HashMap<String, String> additionalInfoMap = MigrateUtil.transMapValue(additionalInfo);
//		if (additionalInfoMap != null) {
//			device.setIpAddress(additionalInfoMap.get("LSRID"));
//			device.setMaxTransferRate(additionalInfoMap.get("MaxTransferRate"));
//		}
//		if (additionalInfoMap != null && additionalInfoMap.containsKey("RouteId")) {
//			String routeId = additionalInfoMap.get("RouteId");
//			if (routeId.contains("ip=")) {
//				device.setIpAddress(routeId.substring(routeId.indexOf("ip=")+3));
//			}
//		}
        
		// SupportedRates，当前未采集
//        String supportedRates = me.getSupportedRates();
//        if (supportedRates != null && (supportedRates.contains("334") || supportedRates.contains("339")))
//            deviceOMSRate.put(me.getDn(),"100G");
//        else
//            deviceOMSRate.put(me.getDn(),"40G");

        return device;
    }
    public String assembleAdditionalInfo(NEL me) {
    	String assembleAdditionalInfo = "";
    	
    	assembleAdditionalInfo = "xSite:";
    	assembleAdditionalInfo = assembleAdditionalInfo + "||ySite:";
    	assembleAdditionalInfo = assembleAdditionalInfo + "||State:" + me.getState();
    	assembleAdditionalInfo = assembleAdditionalInfo + "||EntityClass:";
    	assembleAdditionalInfo = assembleAdditionalInfo + "||NeType:";
    	assembleAdditionalInfo = assembleAdditionalInfo + "||MaxCapacity:";
    	assembleAdditionalInfo = assembleAdditionalInfo + "||ObjectIP:" + me.getIPAddress();
    	assembleAdditionalInfo = assembleAdditionalInfo + "||ObjectIPMask:";
    	assembleAdditionalInfo = assembleAdditionalInfo + "||isGatewayME:";
    	assembleAdditionalInfo = assembleAdditionalInfo + "||installedSerialNumber:";
    	assembleAdditionalInfo = assembleAdditionalInfo + "||installedPartNumber:";
    	assembleAdditionalInfo = assembleAdditionalInfo + "||";
    	
    	return assembleAdditionalInfo;
    }
    
    /**
     * 同步子网、子网网元
     */
    protected void migrateSubnetwork() throws Exception {
		if (!isTableHasData(SBN.class))
			return;
		DataInserter di = new DataInserter(emsid);
		
		executeDelete("delete from CSubnetwork c where c.emsName = '" + emsdn + "'", CSubnetwork.class);
		List<SBN> subNets = sd.queryAll(SBN.class);
		for (SBN subNet : subNets) {
			CSubnetwork cSubnetwork = new CSubnetwork();
			cSubnetwork.setDn(subNet.getRmUID());//emsdn+"@"+
			cSubnetwork.setName(subNet.getNativeName());
			cSubnetwork.setNativeemsname(subNet.getNativeName());
			cSubnetwork.setSid(DatabaseUtil.nextSID(cSubnetwork));
			cSubnetwork.setEmsName(emsdn);
			
			String parent = subNet.getParentSubnetrmUID();
			if (parent != null && !parent.trim().isEmpty()) {
				cSubnetwork.setParentSubnetworkDn(parent);
				cSubnetwork.setParentSubnetworkId(DatabaseUtil.getSID(CSubnetwork.class, parent));
			}

			di.insert(cSubnetwork);
		}
		
		di.end();
	}
    protected void migrateSubnetworkDevice() throws Exception {
    	if (!isTableHasData(SNN.class))
			return;
		DataInserter di = new DataInserter(emsid);
		
		executeDelete("delete from CSubnetworkDevice c where c.emsName = '" + emsdn + "'", CSubnetworkDevice.class);
		List<SNN> subNetNes = sd.queryAll(SNN.class);
		for (SNN subNetNe : subNetNes) {
			CSubnetworkDevice csd = new CSubnetworkDevice();
			csd.setDn(subNetNe.getDn());//emsdn+"@"+
			csd.setSid(DatabaseUtil.nextSID(csd));
			String parent = subNetNe.getSubnetrmUID();
			csd.setSubnetworkDn(parent);
			if (parent != null)
				csd.setSubnetworkId(DatabaseUtil.getSID(CSubnetwork.class, parent));
			csd.setDeviceDn(subNetNe.getRmUID());
			csd.setEmsName(emsdn);
			
			di.insert(csd);
		}
    	
		di.end();
    }
    
    /**
     * 同步槽道
     */
    protected void migrateEquipmentHolder() throws Exception {
		if (!isTableHasData(EQH.class))
			return;
		List<EQH> equipmentHolders = sd.queryAll(EQH.class);
		if (equipmentHolders != null) {
			executeDelete("delete  from CShelf c where c.emsName = '" + emsdn + "'", CShelf.class);
			executeDelete("delete  from CRack c where c.emsName = '" + emsdn + "'", CRack.class);
			executeDelete("delete  from CSlot c where c.emsName = '" + emsdn + "'", CSlot.class);

			insertEQHs(equipmentHolders);
		}
	}
    HashMap<String,CRack> rackMap = new HashMap<String, CRack>();
    public CdcpObject transNewEQH(EQH equipmentHolder) {
        CdcpObject cdcpObject = super.transNewEQH(equipmentHolder);
        if (cdcpObject instanceof CRack) {
            String add = ((CRack) cdcpObject).getAdditionalInfo();
            HashMap<String, String> map = MigrateUtil.transMapValue(add);
            if (map.get("Sequence") != null)
                ((CRack) cdcpObject).setNo(map.get("Sequence"));

            rackMap.put(cdcpObject.getDn(),(CRack)cdcpObject);
        }
        if (cdcpObject instanceof CSlot) {
            String additionalInfo = assembleEQHAdditionalInfo(equipmentHolder);
            Map<String, String> map = MigrateUtil.transMapValue(additionalInfo);
            String nativeEMSName = ((CSlot) cdcpObject).getNativeEMSName();
            if (nativeEMSName.contains("0X")) {
                String no = nativeEMSName.substring(nativeEMSName.lastIndexOf("0X") + 2);
                ((CSlot) cdcpObject).setNo(no);
            } else if (nativeEMSName.contains("SLOT_")){
                String no = nativeEMSName.substring(nativeEMSName.lastIndexOf("SLOT_") + 5);
                ((CSlot) cdcpObject).setNo(no);

            } else {
                ((CSlot) cdcpObject).setNo(nativeEMSName);
            }
        }
        if (cdcpObject instanceof CShelf) {
            ((CShelf) cdcpObject).setShelfType(equipmentHolder.getProductName());
        }
        return cdcpObject;
    }
    
    /**
     * 同步板卡
     */
    public CEquipment transNewCRD(CRD equipment) {
        CEquipment cEquipment = super.transNewCRD(equipment);
        return cEquipment;
    }
    
    /**
     * 同步CTP
     */
    @Override
    protected List insertNewCtps(List<CTP2> ctps) throws Exception {
        DataInserter di = new DataInserter(emsid);
        getLogger().info("migrateCtp size = " + (ctps == null ? null : ctps.size()));
        List<CCTP> cctps = new ArrayList<CCTP>();
        HashMap<String,List<CCTP>> portCtps = new HashMap<String, List<CCTP>>();
        if (ctps != null && ctps.size() > 0) {
            for (CTP2 ctp : ctps) {
                CCTP cctp = transNewCTP2(ctp);
                if (cctp != null) {
                    cctps.add(cctp);
                    DSUtil.putIntoValueList(portCtps,ctp.getRelatedPortrmUID(),cctp);

                }
            }
        }

        // 建立CTP层级关系
        for (String portDn : portCtps.keySet()) {
            List<CCTP> cs = portCtps.get(portDn);
            for (CCTP c : cs) {
            	String ctpName = c.getNativeEMSName();
            	if (StringUtils.contains(ctpName, "/")) {
            		String parentCtpName = StringUtils.substring(ctpName,0,ctpName.lastIndexOf("/"));
            		for (CCTP c2 : cs) {
            			if (parentCtpName.equals(c2.getNativeEMSName())) {
            				c.setParentCtpdn(c2.getDn());
            				DSUtil.putIntoValueList(ctp_ctpMap, c.getParentCtpdn(), c);
            				break;
            			}
            		}
            	}
            }
        }
        portCtps.clear();
        di.insert(cctps);
        di.end();
        return cctps;
    }
    @Override
    public CCTP transNewCTP2(CTP2 ctp) {
        CCTP cctp = super.transNewCTP2(ctp);
        String tmRate = transCtpType2Tmrate(cctp.getType());
        cctp.setTmRate(tmRate);
        cctp.setRateDesc(cctp.getType());

        return cctp;
    }
    protected String transCtpType2Tmrate(String ctpType) {
        if ("ODU0".equalsIgnoreCase(ctpType)) {
        	return "1.25G";
        }
        if ("ODU1".equalsIgnoreCase(ctpType)) {
        	return "2.5G";
        }
        if ("ODU2".equalsIgnoreCase(ctpType)) {
        	return "10G";
        }
        if ("ODU2e".equalsIgnoreCase(ctpType)) {
        	return "10G";
        }
        if ("ODU3".equalsIgnoreCase(ctpType)) {
        	return "40G";
        }
        if ("ODU4".equalsIgnoreCase(ctpType)) {
        	return "100G";
        }

        return null;
    }
    
    /**
     * 同步段
     */
    @Override
    protected void migrateSection() throws Exception {
        executeDelete("delete  from CSection c where c.emsName = '" + emsdn + "'", CSection.class);
        DataInserter di = new DataInserter(emsid);
        List<TPL> sections = sd.queryAll(TPL.class);
        if (sections != null && sections.size() > 0) {
            for (TPL section : sections) {
                CSection csection = transNewSection(section);
                String azPtp = section.getaEndPortrmUID()+"-"+section.getzEndPortrmUID();
                DSUtil.putIntoValueList(azptp_tplMap, azPtp, section);
                
                di.insert(csection);
            }
        }
        di.end();
    }
    public CSection transNewSection(TPL section) {
        CSection csection = new CSection();
        csection.setDn(section.getRmUID());
        csection.setSid(DatabaseUtil.nextSID(csection));
        csection.setCollectTimepoint(section.getCreateDate());
        
//        String rate = section.getRate();
//        if (rate != null) {
//            int r = 0;
//            try {
//                r = Integer.parseInt(rate);
//            } catch (NumberFormatException e) {
//                LogUtil.error(getClass(), "Unknown rate :" + rate);
//            }
//            rate = DicUtil.getSpeedByRate(r);
//        }
//        csection.setSpeed(rate);
        
        csection.setDirection(DicUtil.getConnectionDirection(section.getDirection()));
        csection.setParentDn(null);// 未采集
        csection.setEmsName(emsdn);
        csection.setUserLabel(null);// 未采集
        csection.setNativeEMSName(section.getNativeName());
        csection.setOwner(null);// 未采集
        csection.setAdditionalInfo(null);// 未采集

        csection.setAendTp((section.getaEndPortrmUID()));
        csection.setZendTp((section.getzEndPortrmUID()));
        csection.setType("OTS");
        csection.setSpeed(transCtpType2Tmrate(section.getCtpType()));//getDefaultTmRate(csection.getAendTp())
        csection.setRate(section.getCtpType());// 根据CtpType转换
        
        return csection;
    }
    
    /**
     * 同步交叉
     */
    protected void migrateRouteCC() throws Exception {
        executeDelete("delete from CCrossConnect c where c.emsName = '" + emsdn + "'", CCrossConnect.class);
        DataInserter di = new DataInserter(emsid);
        List<CCrossConnect> newCCs = new ArrayList<CCrossConnect>();
        try {
            List<SIF> ccList = sd.queryAll(SIF.class);
            if (ccList == null || ccList.isEmpty()){
            	return;
            }
        	for (SIF cc : ccList){
        		CCrossConnect des = new CCrossConnect();
        		
        		des.setDn(cc.getRmUID());
        		des.setCollectTimepoint(cc.getCreateDate());
        		
                des.setAptp(cc.getaEndPortrmUID1());
                des.setZptp(cc.getzEndPortrmUID1());
                des.setAend(cc.getaEndCtprmUID1());
                des.setZend(cc.getzEndCtprmUID1());
                
                des.setParentDn(cc.getNermUID());
                des.setEmsName(emsdn);
                des.setDirection(cc.getDirection());
                
                newCCs.add(des);
        		
                String azctp = cc.getaEndCtprmUID1()+"-"+cc.getzEndCtprmUID1();
                DSUtil.putIntoValueList(azctp_sifMap, azctp, cc);
        	}

            removeDuplicateDN(newCCs);
            di.insert(newCCs);

        } catch (Exception e) {
        	e.printStackTrace();
            getLogger().error(e, e);
        } finally {
            di.end();
        }
    }
    
    /**
     * 同步子网连接SNL
     * 划分波道和子波道
     */
	public void migrateSNL() throws Exception {

		executeDelete("delete  from CRoute c where c.emsName = '" + emsdn + "'", CRoute.class);
		executeDelete("delete  from CPath c where c.emsName = '" + emsdn + "'", CPath.class);
		executeDelete("delete  from CChannel c where c.emsName = '" + emsdn + "'", CChannel.class);

		DataInserter di = new DataInserter(emsid);
		List<CSection> cOmss = new ArrayList<CSection>();
		List<CPath> cPaths = new ArrayList<CPath>();
		List<CRoute> cRoutes = new ArrayList<CRoute>();
		List<CChannel> cChannels = new ArrayList<CChannel>();
		
		try {
			List<SNL> subNetLinks = sd.queryAll(SNL.class);
			if (subNetLinks == null || subNetLinks.isEmpty()) {
				return;
			}
			for (SNL snl : subNetLinks) {
				String layer = snl.getLayer(); // OTS、ODU、OTU等，暂时忽略
				if ("OMS".equalsIgnoreCase(layer)) {
					DSUtil.putIntoValueList(rmuid_omsMap, snl.getRmUID(), snl);
					CSection oms = transOmsSection(snl);
					cOmss.add(oms);
					
					//波道划分，以OMS下的OCH ctp来划分，两端ctp名称一致就可以划分(其实就是oms两端ctp的子ctp一一对应)
					List<CCTP> aendChildCtps = ctp_ctpMap.get(snl.getaEndCtprmUID1());
					List<CCTP> zendChildCtps = ctp_ctpMap.get(snl.getzEndCtprmUID1());
					if (Detect.notEmpty(aendChildCtps) && Detect.notEmpty(zendChildCtps)) {
						for (CCTP aendChildCtp : aendChildCtps) {
							for (CCTP zendChildCtp : zendChildCtps) {
								if (StringUtils.equalsIgnoreCase(aendChildCtp.getNativeEMSName(), zendChildCtp.getNativeEMSName())) {
									CChannel channel = createCChannel(oms, aendChildCtp, zendChildCtp);
									cChannels.add(channel);
									
									DSUtil.putIntoValueList(oms_waveMap, oms.getDn(), channel);
									break;
								}
							}
						}
					}
				}
				if ("OCH".equalsIgnoreCase(layer)) {
					DSUtil.putIntoValueList(rmuid_ochMap, snl.getRmUID(), snl);
					CPath och = transOchPath(snl);
					cPaths.add(och);

					// 子波道划分，以och的末级子ctp来划分，一个子ctp，如果它还有子ctp，那么它就不划分子波道了
					List<CCTP> aendChildCtps = ctp_ctpMap.get(snl.getaEndCtprmUID1());
					List<CCTP> zendChildCtps = ctp_ctpMap.get(snl.getzEndCtprmUID1());
					List<CChannel> newChannels = getSubWaves(aendChildCtps, zendChildCtps, och);
					if (Detect.notEmpty(newChannels)) {
						cChannels.addAll(newChannels);
						for (CChannel channel : newChannels) {
							DSUtil.putIntoValueList(och_subwaveMap, och.getDn(), channel);
						}
					}
				}
				if ("DSR".equalsIgnoreCase(layer)) {
					DSUtil.putIntoValueList(rmuid_dsrMap, snl.getRmUID(), snl);
					CRoute dsr = transDsrRoute(snl);
					cRoutes.add(dsr);
				}

				String azctp = snl.getaEndCtprmUID1()+"-"+snl.getzEndCtprmUID1();
                DSUtil.putIntoValueList(azctp_snlMap, azctp, snl);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error(e, e);
		}
		
		try {
			
			removeDuplicateDN(cOmss);
			di.insert(cOmss);
			removeDuplicateDN(cPaths);
			di.insert(cPaths);
			removeDuplicateDN(cRoutes);
			di.insert(cRoutes);
			removeDuplicateDN(cChannels);
			di.insert(cChannels);
			
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error(e, e);
		} finally {
            di.end();
        }

	}
	protected CSection transOmsSection(SNL section) {
        CSection csection = new CSection();
        csection.setDn(section.getRmUID());
        csection.setSid(DatabaseUtil.nextSID(csection));
        csection.setCollectTimepoint(section.getCreateDate());
        
        csection.setDirection(DicUtil.getConnectionDirection(section.getDirection()));
        csection.setParentDn(null);// 未采集
        csection.setEmsName(emsdn);
        csection.setUserLabel(null);// 未采集
        csection.setNativeEMSName(section.getNativeName());
        csection.setOwner(null);// 未采集
        csection.setAdditionalInfo(null);// 未采集

        csection.setAendTp((section.getaEndPortrmUID1()));
        csection.setZendTp((section.getzEndPortrmUID1()));
        csection.setType("OMS");
        
        csection.setSpeed(DicUtil.getRateWithoutNA(section.getRate()));
        csection.setRate(section.getRate());
        
        return csection;
    }
	protected CPath transOchPath(SNL path) {
		CPath cpath = new CPath();
        cpath.setDn(path.getRmUID());
        cpath.setSid(DatabaseUtil.nextSID(cpath));
        
        cpath.setDirection(DicUtil.getConnectionDirection(path.getDirection()));
        cpath.setEmsName(emsdn);
        cpath.setName(path.getNativeName());
        cpath.setCategory("OCH");// 取值范围HOP、OCH
        
        cpath.setAend(path.getaEndCtprmUID1());
        cpath.setZend(path.getzEndCtprmUID1());
        cpath.setAptp(path.getaEndPortrmUID1());
        cpath.setZptp(path.getzEndPortrmUID1());
        
        cpath.setRate(path.getRate());
        cpath.setTmRate(DicUtil.getRateWithoutNA(path.getRate()));
        cpath.setRateDesc(path.getRate());
        
        return cpath;
    }
	protected CRoute transDsrRoute(SNL route) {
		CRoute croute = new CRoute();
        croute.setDn(route.getRmUID());
        croute.setSid(DatabaseUtil.nextSID(croute));
        
        croute.setDirection(DicUtil.getConnectionDirection(route.getDirection()));
        croute.setEmsName(emsdn);
        croute.setName(route.getNativeName());
        croute.setSncState(route.getActiveState());
        croute.setCategory("DSR");// 取值范围SDHROUTE、DSR
        
        croute.setAend(route.getaEndCtprmUID1());
        croute.setZend(route.getzEndCtprmUID1());
        croute.setAptp(route.getaEndPortrmUID1());
        croute.setZptp(route.getzEndPortrmUID1());
        
        croute.setRate(route.getRate());
        croute.setTmRate(DicUtil.getRateWithoutNA(route.getRate()));
        croute.setRateDesc(route.getRate());
        
        return croute;
    }
	
	/**
	 * 递归创建子ctp集合下，末级子ctp之间的子波道channel
	 */
	protected List<CChannel> getSubWaves(List<CCTP> aendChildCtps, List<CCTP> zendChildCtps, CPath och) {
		List<CChannel> cChannels = new ArrayList<CChannel>();
		if (Detect.notEmpty(aendChildCtps) && Detect.notEmpty(zendChildCtps)) {
			for (CCTP aendChildCtp : aendChildCtps) {
				List<CCTP> aendGrandChildCtps = ctp_ctpMap.get(aendChildCtp.getDn());
				boolean hasChild = Detect.notEmpty(aendGrandChildCtps);// 1.has, 0.hasn't
				for (CCTP zendChildCtp : zendChildCtps) {
					if (StringUtils.equalsIgnoreCase(aendChildCtp.getNativeEMSName(), zendChildCtp.getNativeEMSName())) {
						List<CCTP> zendGrandChildCtps = ctp_ctpMap.get(zendChildCtp.getDn());
						// 若A/Z端有一端没有找到子ctp，则建立子波道
						if (!Detect.notEmpty(aendGrandChildCtps) || !Detect.notEmpty(zendGrandChildCtps)) {
							CChannel channel = createCChannel(och, aendChildCtp, zendChildCtp);
							cChannels.add(channel);
						} else {
							List<CChannel> newChannels = getSubWaves(aendGrandChildCtps, zendGrandChildCtps, och);
							if (Detect.notEmpty(newChannels)) {
								cChannels.addAll(newChannels);
							}
						}
						
//						if (!hasChild) {// A端找不到子ctp
//							if (!Detect.notEmpty(zendGrandChildCtps)) {// Z端也找不到子ctp
//								CChannel channel = createCChannel(och, aendChildCtp, zendChildCtp);
//								cChannels.add(channel);
//							}
//						}
//
//						if (hasChild) {
//							List<CChannel> newChannels = getSubWaves(aendGrandChildCtps, zendGrandChildCtps, och);
//							if (Detect.notEmpty(newChannels)) {
//								cChannels.addAll(newChannels);
//							}
//						}
						
						break;
					}
				}
			}
		}

		return cChannels;
	}
    
    /**
     * 创建波道或子波道
     */
	protected CChannel createCChannel(BObject parent,CCTP acctp, CCTP zcctp) {
        String aSideCtp = acctp.getDn();
        String zSideCtp = zcctp.getDn();
        CChannel cChannel = new CChannel();
        cChannel.setDn(aSideCtp + "<>" + zSideCtp+"@CHANNEL:1");
        cChannel.setSid(DatabaseUtil.nextSID(CChannel.class));
        cChannel.setAend(aSideCtp);
        cChannel.setZend(zSideCtp); 
        cChannel.setSectionOrHigherOrderDn(parent.getDn());
        if (parent instanceof CSection) {
        	cChannel.setCategory("波道");
        	cChannel.setName("OCH-"+DNUtil.extractNewOCHno(acctp.getNativeEMSName()));
        }
        if (parent instanceof CPath) {
        	cChannel.setCategory("子波道");
        	cChannel.setName(((CPath) parent).getName());
        }
            
        cChannel.setNo("OCH-"+DNUtil.extractNewOCHno(acctp.getNativeEMSName()));
        cChannel.setRate(acctp.getRate());
        cChannel.setTmRate(acctp.getTmRate());
        cChannel.setRateDesc(acctp.getRateDesc());
        cChannel.setFrequencies(acctp.getFrequencies());

        cChannel.setWaveLen(FHDwdmUtil.getWaveLength( (acctp.getFrequencies())));

        cChannel.setDirection(DicConst.CONNECTION_DIRECTION_CD_BI);
        cChannel.setAptp(acctp.getParentDn());
        cChannel.setZptp(zcctp.getParentDn());

//        CPTP aptp = ptpMap.get(acctp.getParentDn());
//        CPTP zptp = ptpMap.get(zcctp.getParentDn());
//        if (aptp != null && zptp != null)
//            cChannel.setTag3(aptp.getTag3()+"-"+zptp.getTag3());
        cChannel.setEmsName(emsdn);
        return cChannel;
    }
	
	/**
     * 同步光波道SNT(路由分析)
     */
	public void migrateSNT() throws Exception {
		executeDelete("delete  from COMS_CC c where c.emsName = '" + emsdn + "'", COMS_CC.class);
		executeDelete("delete  from COMS_Section c where c.emsName = '" + emsdn + "'", COMS_Section.class);
      
		executeDelete("delete  from CPath_CC c where c.emsName = '" + emsdn + "'", CPath_CC.class);
		executeDelete("delete  from CPath_Section c where c.emsName = '" + emsdn + "'", CPath_Section.class);
		executeDelete("delete  from CPath_Channel c where c.emsName = '" + emsdn + "'", CPath_Channel.class);
      
		executeDelete("delete  from CRoute_CC c where c.emsName = '" + emsdn + "'", CRoute_CC.class);
		executeDelete("delete  from CRoute_Section c where c.emsName = '" + emsdn + "'", CRoute_Section.class);
		executeDelete("delete  from CRoute_Channel c where c.emsName = '" + emsdn + "'", CRoute_Channel.class);
		
		DataInserter di = new DataInserter(emsid);
		List<COMS_CC> oms_ccs = new ArrayList<COMS_CC>();
		List<COMS_Section> oms_sections = new ArrayList<COMS_Section>();
		
		List<CPath_CC> path_ccs = new ArrayList<CPath_CC>();
		List<CPath_Section> path_sections = new ArrayList<CPath_Section>();
		List<CPath_Channel> path_channels = new ArrayList<CPath_Channel>();
        
		List<CRoute_CC> route_ccs = new ArrayList<CRoute_CC>();
		List<CRoute_Section> route_sections = new ArrayList<CRoute_Section>();
		List<CRoute_Channel> route_channels = new ArrayList<CRoute_Channel>();
		
		HashMap<String,List<SNT>> rmuid_sntMap = new HashMap<String, List<SNT>>();
		
		try {
			List<SNT> subNetTrails = sd.queryAll(SNT.class);
			if (subNetTrails == null || subNetTrails.isEmpty()) {
				return;
			}
			for (SNT snl : subNetTrails) {
				DSUtil.putIntoValueList(rmuid_sntMap, snl.getRmUID(), snl);
			}
			// OMS的路由分析(CC/OTS)
			for (String omsUid : rmuid_omsMap.keySet()) {
				List<SNT> snts = rmuid_sntMap.get(omsUid);
				if (Detect.notEmpty(snts)) {
					for (SNT snt : snts) {
						String type = snt.getType();
						if ("SIF".equalsIgnoreCase(type)) { // 交叉CC
							getCCRouting(snt, rmuid_omsMap.get(omsUid).get(0), oms_ccs, path_ccs, route_ccs);
						}
						
						if ("SNL".equalsIgnoreCase(type)) { // OTS
							getOTSRouting(snt, rmuid_omsMap.get(omsUid).get(0), oms_sections, path_sections, route_sections);
						}
					}
				}
			}
			
			// OCH的路由分析(CC/OTS/TPL)
			for (String ochUid : rmuid_ochMap.keySet()) {
				List<SNT> snts = rmuid_sntMap.get(ochUid);
				if (Detect.notEmpty(snts)) {
					for (SNT snt : snts) {
						String type = snt.getType();
						if ("SIF".equalsIgnoreCase(type)) { // 交叉CC
							getCCRouting(snt, rmuid_ochMap.get(ochUid).get(0), oms_ccs, path_ccs, route_ccs);
						}
						
						if ("SNL".equalsIgnoreCase(type)) { // OTS
							getOTSRouting(snt, rmuid_ochMap.get(ochUid).get(0), oms_sections, path_sections, route_sections);
						}
						
						if ("TPL".equalsIgnoreCase(type)) { // TPL
							// type为tpl时，直接根据两端端口取tpl关联och，并建立C_Path_Section
							String azPtp = snt.getaEndPortrmUID()+"-"+snt.getzEndPortrmUID();
							List<TPL> sections = azptp_tplMap.get(azPtp);
							if (!Detect.notEmpty(sections)) {
								azPtp = snt.getzEndPortrmUID()+"-"+snt.getaEndPortrmUID();
								sections = azptp_tplMap.get(azPtp);
							}
							//创建path_sections
							if (Detect.notEmpty(sections)) {
								CPath_Section cPath_section = createPathSection(ochUid, sections.get(0).getRmUID(), emsdn);
								path_sections.add(cPath_section);
								
							}
						}
					}
				}
			}
			
			// OCH的路由分析(波道)
			for (String ochUid : rmuid_ochMap.keySet()) {
				List<SNT> snts = rmuid_sntMap.get(ochUid);
				if (Detect.notEmpty(snts)) {
					for (SNT snt : snts) {
						String type = snt.getType();
						if ("SNL".equalsIgnoreCase(type)) { // 波道
							String azCtp = snt.getaEndCtprmUID() + "-" + snt.getzEndCtprmUID();
							List<SNL> snls = azctp_snlMap.get(azCtp);
							if (!Detect.notEmpty(snls)) {
								azCtp = snt.getzEndCtprmUID() + "-" + snt.getaEndCtprmUID();
								snls = azctp_snlMap.get(azCtp);
							}
							if (Detect.notEmpty(snls)) {
								for (SNL snl : snls) {
									if ("OMS".equalsIgnoreCase(snl.getLayer())) { // 波道
										// 取OMS下的波道
										List<CChannel> channels = oms_waveMap.get(snl.getRmUID());
										if (Detect.notEmpty(channels)) { 
											// 有波道，遍历波道，如果存在波道的某一端ctp在sif的ctp集合中出现了，那么就选取这个波道
											List<String> ctpList = ochUid_ccCtpMap.get(ochUid);//och下sif的ctp集合
											for (CChannel channel : channels) {
												String aendCtp = channel.getAend();
												String zendCtp = channel.getZend();
												if (ctpList.contains(aendCtp) || ctpList.contains(zendCtp)) {
													//创建path_channels
													CPath_Channel cpath_channel = new CPath_Channel();
											        cpath_channel.setDn(SysUtil.nextDN());
											        cpath_channel.setChannelDn(channel.getDn());
											        cpath_channel.setChannelId(channel.getSid());
											        cpath_channel.setPathDn(ochUid);
											        cpath_channel.setPathId(DatabaseUtil.getSID(CPath.class,ochUid));
											        cpath_channel.setEmsName(emsdn);
											        path_channels.add(cpath_channel);
											        
											        DSUtil.putIntoValueList(ochUid_pathchannelMap, ochUid, cpath_channel);
											        //break;
												}
											}
										} else { 
											// 无波道，直接取oms下面的ots+交叉，加入到och的路由里面
											String omsUid = snl.getRmUID();
											List<COMS_CC> och_omsccs = omsUid_omsccMap.get(omsUid);
											if (Detect.notEmpty(och_omsccs)) {
												for (COMS_CC oms_cc : och_omsccs) {
													// 创建path_ccs
													CPath_CC cPath_cc = createPathCC(ochUid, oms_cc.getCcdn(), emsdn);
													path_ccs.add(cPath_cc);
												}
											}
											
											List<COMS_Section> och_omssections = omsUid_omssectionMap.get(omsUid);
											if (Detect.notEmpty(och_omssections)) {
												for (COMS_Section oms_section : och_omssections) {
													// 创建path_sections
													CPath_Section cPath_section = createPathSection(ochUid, oms_section.getSectiondn(), emsdn);
													path_sections.add(cPath_section);
												}
											}
											
//											List<SNT> omsSnts = rmuid_sntMap.get(omsUid);
//											if (Detect.notEmpty(omsSnts)) {
//												getOmsRouteForOch(omsSnts, ochUid, path_ccs, path_sections);
//											}
										}

										break;
									}
								}
							}
						}
					}
				}
			}
			
			// DSR的路由分析
			for (String dsrUid : rmuid_dsrMap.keySet()) {
				analysisRoutingForDSR(dsrUid, rmuid_dsrMap.get(dsrUid).get(0), rmuid_sntMap, route_ccs, route_sections, route_channels);
				analysisRoutingForDSROch(dsrUid, rmuid_dsrMap.get(dsrUid).get(0), rmuid_sntMap, route_ccs, route_sections, route_channels);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error(e, e);
		}
		
		try {
			
			di.insertWithDupCheck(oms_ccs);
            di.insertWithDupCheck(oms_sections);
			
			di.insertWithDupCheck(path_ccs);
            di.insertWithDupCheck(path_sections);
            di.insertWithDupCheck(path_channels);
			
			di.insertWithDupCheck(route_ccs);
            di.insertWithDupCheck(route_sections);
            di.insertWithDupCheck(route_channels);
			
//			removeDuplicateDN(cPaths);
//			di.insert(cPaths);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error(e, e);
		} finally {
            di.end();
        }
		
	}
	/**
	 * 分析SNT中type为“SIF”的数据（交叉） 
	 */
	protected void getCCRouting(SNT snt, SNL snl, List<COMS_CC> oms_ccs, List<CPath_CC> path_ccs, List<CRoute_CC> route_ccs) {
		String azCtp = snt.getaEndCtprmUID()+"-"+snt.getzEndCtprmUID();
		List<SIF> sifs = azctp_sifMap.get(azCtp);
		if (!Detect.notEmpty(sifs)) {
			azCtp = snt.getzEndCtprmUID()+"-"+snt.getaEndCtprmUID();
			sifs = azctp_sifMap.get(azCtp);
		}
		if (Detect.notEmpty(sifs)) {
			SIF sif = sifs.get(0);
			String rmuid = snl.getRmUID();
			if ("OMS".equalsIgnoreCase(snl.getLayer())) {
				// 创建oms_ccs
				COMS_CC coms_cc = new COMS_CC();
	            coms_cc.setDn(SysUtil.nextDN());
	            coms_cc.setOmsdn(rmuid);
	            coms_cc.setCcdn(sif.getRmUID());
	            coms_cc.setEmsName(emsdn);
	            oms_ccs.add(coms_cc);
	            
	            DSUtil.putIntoValueList(omsUid_omsccMap, rmuid, coms_cc);
			}
			if ("OCH".equalsIgnoreCase(snl.getLayer())) {
				// 创建path_ccs
				CPath_CC cPath_cc = createPathCC(rmuid, sif.getRmUID(), emsdn);
		        path_ccs.add(cPath_cc);
		        
		        DSUtil.putIntoValueList(ochUid_ccCtpMap, rmuid, sif.getaEndCtprmUID1());
		        DSUtil.putIntoValueList(ochUid_ccCtpMap, rmuid, sif.getzEndCtprmUID1());
			}
			if ("DSR".equalsIgnoreCase(snl.getLayer())) {
				// 创建route_ccs
				CRoute_CC cRoute_cc = createRouteCC(rmuid, sif.getRmUID(), emsdn);
				route_ccs.add(cRoute_cc);

				DSUtil.putIntoValueList(dsrUid_ccCtpMap, rmuid, sif.getaEndCtprmUID1());
				DSUtil.putIntoValueList(dsrUid_ccCtpMap, rmuid, sif.getzEndCtprmUID1());
			}
		}
	}
	/**
	 * 分析SNT中type为“SNL”的数据（OTS） 
	 */
	protected void getOTSRouting(SNT snt, SNL snl, List<COMS_Section> oms_sections, List<CPath_Section> path_sections, List<CRoute_Section> route_sections) {
		String azCtp = snt.getaEndCtprmUID()+"-"+snt.getzEndCtprmUID();
		List<SNL> matchSnls = azctp_snlMap.get(azCtp);
		if (!Detect.notEmpty(matchSnls)) {
			azCtp = snt.getzEndCtprmUID()+"-"+snt.getaEndCtprmUID();
			matchSnls = azctp_snlMap.get(azCtp);
		}
		if (Detect.notEmpty(matchSnls)) {
			for (SNL matchSnl : matchSnls) {
				if ("OTS".equalsIgnoreCase(matchSnl.getLayer())) {
					// 在snl里面查询到ots后，要根据ots两端端口，取tpl里面的section
					String azPtp = matchSnl.getaEndPortrmUID1()+"-"+matchSnl.getzEndPortrmUID1();
					List<TPL> sections = azptp_tplMap.get(azPtp);
					if (!Detect.notEmpty(sections)) {
						azPtp = matchSnl.getzEndPortrmUID1()+"-"+matchSnl.getaEndPortrmUID1();
						sections = azptp_tplMap.get(azPtp);
					}
					if (Detect.notEmpty(sections)) {
						String rmuid = snl.getRmUID();
						if ("OMS".equalsIgnoreCase(snl.getLayer())) {
							// 创建oms_sections
							COMS_Section coms_section = new COMS_Section();
	                        coms_section.setDn(SysUtil.nextDN());
	                        coms_section.setOmsdn(rmuid);
	                        coms_section.setSectiondn(sections.get(0).getRmUID());
	                        coms_section.setEmsName(emsdn);
	                        oms_sections.add(coms_section);
	                        
	                        DSUtil.putIntoValueList(omsUid_omssectionMap, rmuid, coms_section);
						}
						if ("OCH".equalsIgnoreCase(snl.getLayer())) {
							// 创建path_sections
							CPath_Section cPath_section = createPathSection(rmuid, sections.get(0).getRmUID(), emsdn);
							path_sections.add(cPath_section);
						}
						if ("DSR".equalsIgnoreCase(snl.getLayer())) {
							// 创建route_sections
							CRoute_Section cRoute_section = createRouteSection(rmuid, sections.get(0).getRmUID(), emsdn);
							route_sections.add(cRoute_section);
						}
						
						break;
					}
				}
			}
		}
	}
	/**
	 * 为OCH对OMS路由进行分析(OCH下的OMS路由没有波道时)
	 */
	protected void getOmsRouteForOch(List<SNT> snts, String ochUid, List<CPath_CC> path_ccs, List<CPath_Section> path_sections) {

		for (SNT snt : snts) {
			String type = snt.getType();
			if ("SIF".equalsIgnoreCase(type)) { // 交叉CC
				getCCRouting(snt, rmuid_ochMap.get(ochUid).get(0), new ArrayList<COMS_CC>(), path_ccs, new ArrayList<CRoute_CC>());
			}

			if ("SNL".equalsIgnoreCase(type)) { // OTS
				getOTSRouting(snt, rmuid_ochMap.get(ochUid).get(0), new ArrayList<COMS_Section>(), path_sections, new ArrayList<CRoute_Section>());
			}
		}

	}
	/**
	 * DSR的路由分析
	 */
	protected void analysisRoutingForDSR(String rmUid, SNL dsr, HashMap<String, List<SNT>> rmuid_sntMap, 
			List<CRoute_CC> route_ccs, List<CRoute_Section> route_sections, List<CRoute_Channel> route_channels) {
		List<SNT> snts = rmuid_sntMap.get(rmUid);
		if (Detect.notEmpty(snts)) {
			for (SNT snt : snts) {
				String type = snt.getType();
				if ("SIF".equalsIgnoreCase(type)) { // 交叉CC
					getCCRouting(snt, dsr, new ArrayList<COMS_CC>(), new ArrayList<CPath_CC>(), route_ccs);
				}

				if ("SNL".equalsIgnoreCase(type)) {
					// getOTSRouting(snt, rmuid_dsrMap.get(rmuid).get(0), new ArrayList<COMS_Section>(), new ArrayList<CPath_Section>(), route_sections);// OTS
					String azCtp = snt.getaEndCtprmUID() + "-" + snt.getzEndCtprmUID();
					List<SNL> matchSnls = azctp_snlMap.get(azCtp);
					if (!Detect.notEmpty(matchSnls)) {
						azCtp = snt.getzEndCtprmUID() + "-" + snt.getaEndCtprmUID();
						matchSnls = azctp_snlMap.get(azCtp);
					}
					if (Detect.notEmpty(matchSnls)) {
						for (SNL matchSnl : matchSnls) {
							
							// 不处理已经出现过的snl
							List<String> snlDns = dsr_snlMap.get(dsr.getRmUID());
							if (snlDns==null) {
								snlDns = new ArrayList<String>();
							}
							if (snlDns.contains(matchSnl.getRmUID())) {
								continue;
							}
							DSUtil.putIntoValueList(dsr_snlMap, dsr.getRmUID(), matchSnl.getRmUID());
							
							if ("DSR".equalsIgnoreCase(matchSnl.getLayer())) {
								// 忽略DSR
							}

							if ("OTS".equalsIgnoreCase(matchSnl.getLayer())) {
								// 在snl里面查询到ots后，要根据ots两端端口，取tpl里面的section
								String azPtp = matchSnl.getaEndPortrmUID1() + "-" + matchSnl.getzEndPortrmUID1();
								List<TPL> sections = azptp_tplMap.get(azPtp);
								if (!Detect.notEmpty(sections)) {
									azPtp = matchSnl.getzEndPortrmUID1() + "-" + matchSnl.getaEndPortrmUID1();
									sections = azptp_tplMap.get(azPtp);
								}
								if (Detect.notEmpty(sections)) {
									// 创建route_sections
									CRoute_Section cRoute_section = createRouteSection(dsr.getRmUID(), sections.get(0).getRmUID(), emsdn);
									route_sections.add(cRoute_section);

									break;
								}
							}

							if ("OMS".equalsIgnoreCase(matchSnl.getLayer())) {
								// 理论上不会有oms，但为了完整，如果是oms，就把oms的路由：ots+交叉，当成dsr的ots+交叉
								List<COMS_CC> oms_ccs = omsUid_omsccMap.get(matchSnl.getRmUID());
								if (Detect.notEmpty(oms_ccs)) {
									for (COMS_CC oms_cc : oms_ccs) {
										// 创建route_ccs
										CRoute_CC cRoute_cc = createRouteCC(dsr.getRmUID(), oms_cc.getCcdn(), emsdn);
										route_ccs.add(cRoute_cc);
										
//										DSUtil.putIntoValueList(dsrUid_ccCtpMap, rmuid, sif.getaEndCtprmUID1());
//										DSUtil.putIntoValueList(dsrUid_ccCtpMap, rmuid, sif.getzEndCtprmUID1());
									}
								}
								
								List<COMS_Section> oms_sections = omsUid_omssectionMap.get(matchSnl.getRmUID());
								if (Detect.notEmpty(oms_sections)) {
									for (COMS_Section oms_section : oms_sections) {
										// 创建route_sections
										CRoute_Section cRoute_section = createRouteSection(dsr.getRmUID(), oms_section.getSectiondn(), emsdn);
										route_sections.add(cRoute_section);
									}
								}
								
								break;
							}

							if ("ODU".equalsIgnoreCase(matchSnl.getLayer()) || "OTU".equalsIgnoreCase(matchSnl.getLayer())) {
								// 递归
								analysisRoutingForDSR(matchSnl.getRmUID(), dsr, rmuid_sntMap, route_ccs, route_sections, route_channels);
								
								break;
							}
							
							if ("OCH".equalsIgnoreCase(matchSnl.getLayer())) {
								// DSR的交叉分析完成之后再开始
							}
						}
					}
				}
			}
		}
	}
	/**
	 * DSR的路由分析-子波道
	 */
	protected void analysisRoutingForDSROch(String rmUid, SNL dsr, HashMap<String, List<SNT>> rmuid_sntMap, 
			List<CRoute_CC> route_ccs, List<CRoute_Section> route_sections, List<CRoute_Channel> route_channels) {
		List<SNT> snts = rmuid_sntMap.get(rmUid);
		if (Detect.notEmpty(snts)) {
			for (SNT snt : snts) {
				String type = snt.getType();
				if ("SNL".equalsIgnoreCase(type)) {
					String azCtp = snt.getaEndCtprmUID() + "-" + snt.getzEndCtprmUID();
					List<SNL> matchSnls = azctp_snlMap.get(azCtp);
					if (!Detect.notEmpty(matchSnls)) {
						azCtp = snt.getzEndCtprmUID() + "-" + snt.getaEndCtprmUID();
						matchSnls = azctp_snlMap.get(azCtp);
					}
					if (Detect.notEmpty(matchSnls)) {
						for (SNL matchSnl : matchSnls) {
							
							// 不处理已经出现过的snl
							List<String> snlDns = dsr_snlOchMap.get(dsr.getRmUID());
							if (snlDns==null) {
								snlDns = new ArrayList<String>();
							}
							if (snlDns.contains(matchSnl.getRmUID())) {
								continue;
							}
							DSUtil.putIntoValueList(dsr_snlOchMap, dsr.getRmUID(), matchSnl.getRmUID());
							
							if ("ODU".equalsIgnoreCase(matchSnl.getLayer()) || "OTU".equalsIgnoreCase(matchSnl.getLayer())) {
								// 递归
								analysisRoutingForDSROch(matchSnl.getRmUID(), dsr, rmuid_sntMap, route_ccs, route_sections, route_channels);
								
								break;
							}
							
							if ("OCH".equalsIgnoreCase(matchSnl.getLayer())) {
								// DSR的交叉分析完成之后再开始
								// 取OCH下的子波道
								List<CChannel> channels = och_subwaveMap.get(matchSnl.getRmUID());
								if (Detect.notEmpty(channels)) { 
									// 有子波道，遍历子波道，如果存在子波道的某一端ctp在sif的ctp集合中出现了，那么就选取这个子波道
									List<String> ctpList = dsrUid_ccCtpMap.get(dsr.getRmUID());//dsr下sif的ctp集合
									for (CChannel channel : channels) {
										String aendCtp = channel.getAend();
										String zendCtp = channel.getZend();
										if (ctpList.contains(aendCtp) || ctpList.contains(zendCtp)) {
											//创建route_channels
											CRoute_Channel croute_channel = createRouteChannel(dsr.getRmUID(), channel.getDn(), emsdn);
									        route_channels.add(croute_channel);
									        
									        //break;
										}
									}
								} else {
									String dsrOch = dsr.getRmUID()+"-无子波道-"+matchSnl.getRmUID();
									if (!dsrOchNoSubwave.contains(dsrOch)) {
										dsrOchNoSubwave.add(dsrOch);
									}
//									System.out.println(dsr.getRmUID()+"-无子波道-"+matchSnl.getRmUID());
									
//									// 无子波道，直接取och下面的ots+交叉+波道，加入到dsr的路由里面
//									String ochUid = matchSnl.getRmUID();
//									List<CPath_CC> och_pathccs = ochUid_pathccMap.get(ochUid);
//									if (Detect.notEmpty(och_pathccs)) {
//										for (CPath_CC path_cc : och_pathccs) {
//											// 创建route_ccs
//											CRoute_CC cRoute_cc = createRouteCC(dsrUid, path_cc.getCcDn(), emsdn);
//											route_ccs.add(cRoute_cc);
//										}
//									}
//									
//									List<CPath_Section> och_pathsections = ochUid_pathsectionMap.get(ochUid);
//									if (Detect.notEmpty(och_pathsections)) {
//										for (CPath_Section path_section : och_pathsections) {
//											// 创建route_sections
//											CRoute_Section cRoute_section = createRouteSection(dsrUid, path_section.getSectionDn(), emsdn);
//											route_sections.add(cRoute_section);
//										}
//									}
//									
//									List<CPath_Channel> och_pathchannels = ochUid_pathchannelMap.get(ochUid);
//									if (Detect.notEmpty(och_pathchannels)) {
//										for (CPath_Channel path_channel : och_pathchannels) {
//											// 创建route_channels
//											CRoute_Channel cRoute_channel = createRouteChannel(dsrUid, path_channel.getChannelDn(), emsdn);
//											route_channels.add(cRoute_channel);
//										}
//									}
								}

								break;
							}
						}
					}
				}
			}
		}
	}
	
	protected CPath_Section createPathSection(String pathDn, String sectionDn, String emsDn) {
		// 创建path_sections
		CPath_Section cPath_section = new CPath_Section();
		cPath_section.setDn(SysUtil.nextDN());
		cPath_section.setSectionDn(sectionDn);
		cPath_section.setSectionId(DatabaseUtil.getSID(CSection.class,sectionDn));
		cPath_section.setPathDn(pathDn);
		cPath_section.setPathId(DatabaseUtil.getSID(CPath.class,pathDn));
		cPath_section.setEmsName(emsDn);
		
		DSUtil.putIntoValueList(ochUid_pathsectionMap, pathDn, cPath_section);
		
		return cPath_section;
	}
	protected CPath_CC createPathCC(String pathDn, String ccDn, String emsDn) {
		// 创建path_ccs
		CPath_CC cPath_cc = new CPath_CC();
        cPath_cc.setDn(SysUtil.nextDN());
        cPath_cc.setCcDn(ccDn);
        cPath_cc.setCcId(DatabaseUtil.getSID(CCrossConnect.class,ccDn));
        cPath_cc.setPathDn(pathDn);
        cPath_cc.setPathId(DatabaseUtil.getSID(CPath.class,pathDn));
        cPath_cc.setEmsName(emsDn);
        
        DSUtil.putIntoValueList(ochUid_pathccMap, pathDn, cPath_cc);
		
		return cPath_cc;
	}
	
	protected CRoute_Section createRouteSection(String routeDn, String sectionDn, String emsDn) {
		// 创建route_sections
		CRoute_Section cRoute_section = new CRoute_Section();
		cRoute_section.setDn(SysUtil.nextDN());
		cRoute_section.setSectionDn(sectionDn);
		cRoute_section.setSectionId(DatabaseUtil.getSID(CSection.class, sectionDn));
		cRoute_section.setRouteDn(routeDn);
		cRoute_section.setRouteId(DatabaseUtil.getSID(CRoute.class, routeDn));
		cRoute_section.setEmsName(emsDn);
		
		return cRoute_section;
	}
	protected CRoute_CC createRouteCC(String routeDn, String ccDn, String emsDn) {
		// 创建route_ccs
		CRoute_CC cRoute_cc = new CRoute_CC();
		cRoute_cc.setDn(SysUtil.nextDN());
		cRoute_cc.setCcDn(ccDn);
		cRoute_cc.setCcId(DatabaseUtil.getSID(CCrossConnect.class,ccDn));
		cRoute_cc.setRouteDn(routeDn);
		cRoute_cc.setRouteId(DatabaseUtil.getSID(CRoute.class, routeDn));
		cRoute_cc.setEmsName(emsDn);
		
		return cRoute_cc;
	}
	protected CRoute_Channel createRouteChannel(String routeDn, String channelDn, String emsDn) {
		//创建route_channels
		CRoute_Channel croute_channel = new CRoute_Channel();
        croute_channel.setDn(SysUtil.nextDN());
        croute_channel.setChannelDn(channelDn);
        croute_channel.setChannelId(DatabaseUtil.getSID(CChannel.class, channelDn));
        croute_channel.setRouteDn(routeDn);
        croute_channel.setRouteId(DatabaseUtil.getSID(CRoute.class,routeDn));
        croute_channel.setEmsName(emsdn);
        
        return croute_channel;
	}
	
	
	/**
	 * 工具方法 
	 */
	// 从map中取两端相同的数据
//	protected  containsCtps(List<CCTP> ctps,String ctpdn) {
//		
//	}
	
	
    
    // <-- 以上是新接口入库方法 -->


    public static void main(String[] args) throws Exception {
//        List allObjects = JpaClient.getInstance("cdcp.datajpa").findAllObjects(CDevice.class);
        String fileName=  "D:\\20180725.db";
        String emsdn = "ZJ-FH-1-OTN";
        if (args != null && args.length > 0)
            fileName = args[0];
        if (args != null && args.length > 1)
            emsdn = args[1];
        String[] locations = { "appserver-spring.xml" };
        ApplicationContext ctx = new ClassPathXmlApplicationContext(locations);
        JPASupportSpringImpl context = new JPASupportSpringImpl("entityManagerFactoryData");
        try
        {
            context.begin();
            String[] preLoadSqls = Constants.PRE_LOAD_SQLS;
            for (String sql : preLoadSqls) {

              //  DBUtil.getInstance().executeNonSelectingSQL(context,sql);
            }
            context.end();
        } catch (Exception ex) {
            context.rollback();
            throw ex;
        } finally {
            context.release();
        }

        FHOTNM2000OTNnewMigrator loader = new FHOTNM2000OTNnewMigrator (fileName, emsdn){
            public void afterExecute() {
                printTableStat();
            }
        };
        loader.execute();
    }


}
