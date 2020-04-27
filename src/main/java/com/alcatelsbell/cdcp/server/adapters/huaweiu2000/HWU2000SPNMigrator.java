package com.alcatelsbell.cdcp.server.adapters.huaweiu2000;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.asb.mule.probe.framework.entity.CRD;
import org.asb.mule.probe.framework.entity.EPG;
import org.asb.mule.probe.framework.entity.EPU;
import org.asb.mule.probe.framework.entity.EQH;
import org.asb.mule.probe.framework.entity.NEL;
import org.asb.mule.probe.framework.entity.OMC;
import org.asb.mule.probe.framework.entity.PGU;
import org.asb.mule.probe.framework.entity.PRT;
import org.asb.mule.probe.framework.entity.PTG;
import org.asb.mule.probe.framework.entity.SBN;
import org.asb.mule.probe.framework.entity.SNN;
import org.asb.mule.probe.framework.entity.TPL;
import org.asb.mule.probe.framework.entity.spn.BRD;
import org.asb.mule.probe.framework.entity.spn.ESI;
import org.asb.mule.probe.framework.entity.spn.ESP;
import org.asb.mule.probe.framework.entity.spn.ETH;
import org.asb.mule.probe.framework.entity.spn.ETP;
import org.asb.mule.probe.framework.entity.spn.IGL;
import org.asb.mule.probe.framework.entity.spn.IGT;
import org.asb.mule.probe.framework.entity.spn.L3I;
import org.asb.mule.probe.framework.entity.spn.L3P;
import org.asb.mule.probe.framework.entity.spn.L3T;
import org.asb.mule.probe.framework.entity.spn.LBS;
import org.asb.mule.probe.framework.entity.spn.MCB;
import org.asb.mule.probe.framework.entity.spn.MCL;
import org.asb.mule.probe.framework.entity.spn.MCP;
import org.asb.mule.probe.framework.entity.spn.MCS;
import org.asb.mule.probe.framework.entity.spn.MGB;
import org.asb.mule.probe.framework.entity.spn.MGP;
import org.asb.mule.probe.framework.entity.spn.MPI;
import org.asb.mule.probe.framework.entity.spn.MTL;
import org.asb.mule.probe.framework.entity.spn.MTR;
import org.asb.mule.probe.framework.entity.spn.NWS;
import org.asb.mule.probe.framework.entity.spn.PRB;
import org.asb.mule.probe.framework.entity.spn.PSW;
import org.asb.mule.probe.framework.entity.spn.PWP;
import org.asb.mule.probe.framework.entity.spn.PWT;
import org.asb.mule.probe.framework.entity.spn.SRR;
import org.asb.mule.probe.framework.entity.spn.SRT;
import org.asb.mule.probe.framework.entity.spn.STT;
import org.asb.mule.probe.framework.entity.spn.TDM;
import org.asb.mule.probe.framework.entity.spn.TNL;
import org.asb.mule.probe.framework.entity.spn.TPB;
import org.asb.mule.probe.framework.entity.spn.TPI;
import org.asb.mule.probe.framework.util.FileLogger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.alcatelsbell.cdcp.common.Constants;
import com.alcatelsbell.cdcp.nbi.model.CdcpObject;
import com.alcatelsbell.cdcp.nbi.model.spn.CBRD;
import com.alcatelsbell.cdcp.nbi.model.spn.CCRD;
import com.alcatelsbell.cdcp.nbi.model.spn.CEPG;
import com.alcatelsbell.cdcp.nbi.model.spn.CEPU;
import com.alcatelsbell.cdcp.nbi.model.spn.CEQH;
import com.alcatelsbell.cdcp.nbi.model.spn.CESI;
import com.alcatelsbell.cdcp.nbi.model.spn.CESP;
import com.alcatelsbell.cdcp.nbi.model.spn.CETH;
import com.alcatelsbell.cdcp.nbi.model.spn.CETP;
import com.alcatelsbell.cdcp.nbi.model.spn.CIGL;
import com.alcatelsbell.cdcp.nbi.model.spn.CIGT;
import com.alcatelsbell.cdcp.nbi.model.spn.CL3I;
import com.alcatelsbell.cdcp.nbi.model.spn.CL3P;
import com.alcatelsbell.cdcp.nbi.model.spn.CL3T;
import com.alcatelsbell.cdcp.nbi.model.spn.CLBS;
import com.alcatelsbell.cdcp.nbi.model.spn.CMCB;
import com.alcatelsbell.cdcp.nbi.model.spn.CMCL;
import com.alcatelsbell.cdcp.nbi.model.spn.CMCP;
import com.alcatelsbell.cdcp.nbi.model.spn.CMCS;
import com.alcatelsbell.cdcp.nbi.model.spn.CMGB;
import com.alcatelsbell.cdcp.nbi.model.spn.CMGP;
import com.alcatelsbell.cdcp.nbi.model.spn.CMPI;
import com.alcatelsbell.cdcp.nbi.model.spn.CMTL;
import com.alcatelsbell.cdcp.nbi.model.spn.CMTR;
import com.alcatelsbell.cdcp.nbi.model.spn.CNEL;
import com.alcatelsbell.cdcp.nbi.model.spn.CNWS;
import com.alcatelsbell.cdcp.nbi.model.spn.COMC;
import com.alcatelsbell.cdcp.nbi.model.spn.CPGU;
import com.alcatelsbell.cdcp.nbi.model.spn.CPRB;
import com.alcatelsbell.cdcp.nbi.model.spn.CPRT;
import com.alcatelsbell.cdcp.nbi.model.spn.CPSW;
import com.alcatelsbell.cdcp.nbi.model.spn.CPTG;
import com.alcatelsbell.cdcp.nbi.model.spn.CPWP;
import com.alcatelsbell.cdcp.nbi.model.spn.CPWT;
import com.alcatelsbell.cdcp.nbi.model.spn.CSBN;
import com.alcatelsbell.cdcp.nbi.model.spn.CSNN;
import com.alcatelsbell.cdcp.nbi.model.spn.CSRR;
import com.alcatelsbell.cdcp.nbi.model.spn.CSRT;
import com.alcatelsbell.cdcp.nbi.model.spn.CSTT;
import com.alcatelsbell.cdcp.nbi.model.spn.CTDM;
import com.alcatelsbell.cdcp.nbi.model.spn.CTNL;
import com.alcatelsbell.cdcp.nbi.model.spn.CTPB;
import com.alcatelsbell.cdcp.nbi.model.spn.CTPI;
import com.alcatelsbell.cdcp.nbi.model.spn.CTPL;
import com.alcatelsbell.cdcp.server.adapters.AbstractDBFLoader;
import com.alcatelsbell.cdcp.util.DataInserter;
import com.alcatelsbell.cdcp.util.DatabaseUtil;
import com.alcatelsbell.cdcp.util.MigrateThread;
import com.alcatelsbell.nms.common.Detect;
import com.alcatelsbell.nms.cronjob.Assertion;
import com.alcatelsbell.nms.db.components.service.JPASupportSpringImpl;
import com.alcatelsbell.nms.valueobject.BObject;

/**
 * 华为OMC新网管入库(SPN)
 * Author: Zong Yu
 * Date: 2020-04
 * Time: 下午4:43
 */
public class HWU2000SPNMigrator extends AbstractDBFLoader{

    public HWU2000SPNMigrator(String fileUrl, String emsdn) {
        this.fileUrl = fileUrl;
        this.emsdn = emsdn;
        MigrateThread.thread().initLog(emsdn + "." + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log");

    }

    private static FileLogger logger = new FileLogger("HW-SPN-Device.log");
    public HWU2000SPNMigrator(Serializable object, String emsdn) {
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
    	checkEMS(emsdn, "华为");
    	getLogger().info("华为SPN入库-HWU2000SPNMigrator");
//        testTime();
    	logAction(emsdn + " SpnMigrateStart", "SPN同步开始", 0);
        
    	// 不做处理直接入库的对象，都放到BOMap和COMap里
    	HashMap<String,Object> boMap = getBOMap();
    	int i = 1;
    	for (String key : boMap.keySet()) {
    		logAction(emsdn + " migrate" + key, "同步" + key, i);
    		batchMigrate(key, boMap);
    		i++;
    	}
    	i = 0;
    	
    	logAction(emsdn + " SpnMigrateEnd", "SPN同步结束", 100);
       	System.out.println("11. end");
        
        sd.release();
    }

    protected Class[] getStatClss() {
		return new Class[] { COMC.class, CNEL.class, CEQH.class, CCRD.class, CPRT.class, CPRB.class, CTNL.class,
				CLBS.class, CTPI.class, CTPB.class, CMPI.class, CMTR.class, CMTL.class, CPSW.class, CPWP.class,
				CPWT.class, CETH.class, CESP.class, CESI.class, CTDM.class, CETP.class, CBRD.class, CL3I.class,
				CL3P.class, CL3T.class, CTPL.class, CSBN.class, CSNN.class, CEPG.class, CEPU.class, CPTG.class,
				CPGU.class, CSTT.class, CSRT.class, CSRR.class, CIGT.class, CIGL.class, CNWS.class, CMGP.class,
				CMGB.class, CMCL.class, CMCS.class, CMCP.class, CMCB.class
		};
    }


    // <-- 以下是新接口入库方法 -->
    /**
     * 批量入库，根据db文件里的对象的字段，直接入库到中间表的对应表里。
     * @param key
     * @param boMap
     * @throws Exception
     */
    public void batchMigrate(String key, HashMap<String,Object> boMap) throws Exception {
		if (!isTableHasData(boMap.get(key).getClass()))
			return;
		List<? extends BObject> boList = sd.queryAll(boMap.get(key).getClass());
		if (boList == null || boList.isEmpty()) {
			getLogger().info(key + " is empty, return");
			return;
		}

		HashMap<String,Object> coMap = getCOMap();
    	
		// 删除原有数据
		executeDelete("delete  from C" + key + " c where c.emsName = '" + emsdn + "'", coMap.get(key).getClass());
		// 获取新数据
		List<? extends CdcpObject> cdcpList = null;
		try {
			cdcpList = getCdcpObject(boMap.get(key).getClass(),coMap.get(key).getClass(),boList);
		} catch (Exception e) {
			getLogger().error("获取新数据出错:" + e.getMessage());
			e.printStackTrace();
		}
		
		// 插入新数据
		if (Detect.notEmpty(cdcpList)) {
			DataInserter di = new DataInserter(emsid);
			di.insert(cdcpList);
			di.end();
		} else {
			getLogger().error("cdcpList is null...");
		}
	}
    
    public List<? extends CdcpObject> getCdcpObject(Class<?> boClazz, Class<?> coClazz, List<? extends BObject> boList) {
    	List<CdcpObject> cdcpList = new ArrayList<CdcpObject>();
    	// 获取实体类的所有属性，返回Field数组
        Field[] boFields = boClazz.getDeclaredFields();
        Assertion.noNullElements(boFields, "BO对象为空-" + boClazz.getName());
        Field[] coFields = coClazz.getDeclaredFields();
        Assertion.noNullElements(coFields, "CO对象为空-" + coClazz.getName());
        String[] coFieldsString = getStrings(coFields);
        
    	for (BObject bo : boList) {
    		try {
    			//创建CDCP对象
				CdcpObject co = (CdcpObject) coClazz.newInstance();
                for (Field boField : boFields) {
                	String fieldName = boField.getName();
                	if ("number".equalsIgnoreCase(fieldName)) { // BO中的number，转换到中间表变成numbers
                		try {
                			// 往CO对象里set值
                			Method boM = bo.getClass().getMethod("get" + "Number");
                			if (null != boM.invoke(bo)) {
                				String value = boM.invoke(bo).toString();
    							Method coM = co.getClass().getMethod("set" + "Numbers", String.class);
    							coM.invoke(co, value);
                			}
                			continue;
						} catch (Exception e) {
							getLogger().error("getset出错:" + e.getMessage());
							e.printStackTrace();
							break;
						}
                	}
                	if (ArrayUtils.contains(coFieldsString, fieldName)) { // 判断CO中是否有该属性
                		String upperPropertyname = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                		char[] chars = fieldName.toCharArray();
                		if (Character.isUpperCase(chars[1])) {
                			upperPropertyname = fieldName;
                		}
                		
                		try {
                			// 往CO对象里set值
                			Method boM = bo.getClass().getMethod("get" + upperPropertyname);
                			if (null != boM.invoke(bo)) {
                				String value = boM.invoke(bo).toString();
    							Method coM = co.getClass().getMethod("set" + upperPropertyname, String.class);
    							coM.invoke(co, value);
    							if ("RmUID".equalsIgnoreCase(fieldName)) {
    								co.setDn(value);
    							}
                			}
						} catch (Exception e) {
							getLogger().error("getset出错:" + e.getMessage());
							e.printStackTrace();
							break;
						}
                		
                		
                	} else {
                		getLogger().error("BO中存在CO没有的字段:" + boField.getName());
                		break;
                	}
                	
                }
                co.setEmsName(emsdn);
                co.setSid(DatabaseUtil.nextSID(co.getClass()));
                cdcpList.add(co);
				
			} catch (Exception e) {
				getLogger().error("创建CDCP对象出错:" + e.getMessage());
				e.printStackTrace();
				break;
			}
    		
    	}
    	
    	return cdcpList;
    }
    
    public String[] getStrings(Field[] fields) {
    	String[] strings = new String[]{};
    	for (Field field : fields) {
    		strings = StringUtils.addStringToArray(strings, field.getName());
    	}
    	
    	return strings;
    }
    
    public HashMap<String,Object> getBOMap() {
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		OMC omc = new OMC();
		NEL nel = new NEL();
		EQH eqh = new EQH();
		CRD crd = new CRD();
		PRT prt = new PRT();
		PRB prb = new PRB();
		TNL tnl = new TNL();
		LBS lbs = new LBS();
		TPI tpi = new TPI();
		TPB tpb = new TPB();
		MPI mpi = new MPI();
		MTR mtr = new MTR();
		MTL mtl = new MTL();
		PSW psw = new PSW();
		PWP pwp = new PWP();
		PWT pwt = new PWT();
		ETH eth = new ETH();
		ESP esp = new ESP();
		ESI esi = new ESI();
		TDM tdm = new TDM();
		ETP etp = new ETP();
		BRD brd = new BRD();
		L3I l3i = new L3I();
		L3P l3p = new L3P();
		L3T l3t = new L3T();
		TPL tpl = new TPL();
		SBN sbn = new SBN();
		SNN snn = new SNN();
		EPG epg = new EPG();
		EPU epu = new EPU();
		PTG ptg = new PTG();
		PGU pgu = new PGU();
		STT stt = new STT();
		SRT srt = new SRT();
		SRR srr = new SRR();
		IGT igt = new IGT();
		IGL igl = new IGL();
		NWS nws = new NWS();
		MGP mgp = new MGP();
		MGB mgb = new MGB();
		MCL mcl = new MCL();
		MCS mcs = new MCS();
		MCP mcp = new MCP();
		MCB mcb = new MCB();
		
		map.put("OMC", omc);
		map.put("NEL", nel);
		map.put("EQH", eqh);
		map.put("CRD", crd);
		map.put("PRT", prt);
		map.put("PRB", prb);
		map.put("TNL", tnl);
		map.put("LBS", lbs);
		map.put("TPI", tpi);
		map.put("TPB", tpb);
		map.put("MPI", mpi);
		map.put("MTR", mtr);
		map.put("MTL", mtl);
		map.put("PSW", psw);
		map.put("PWP", pwp);
		map.put("PWT", pwt);
		map.put("ETH", eth);
		map.put("ESP", esp);
		map.put("ESI", esi);
		map.put("TDM", tdm);
		map.put("ETP", etp);
		map.put("BRD", brd);
		map.put("L3I", l3i);
		map.put("L3P", l3p);
		map.put("L3T", l3t);
		map.put("TPL", tpl);
		map.put("SBN", sbn);
		map.put("SNN", snn);
		map.put("EPG", epg);
		map.put("EPU", epu);
		map.put("PTG", ptg);
		map.put("PGU", pgu);
		map.put("STT", stt);
		map.put("SRT", srt);
		map.put("SRR", srr);
		map.put("IGT", igt);
		map.put("IGL", igl);
		map.put("NWS", nws);
		map.put("MGP", mgp);
		map.put("MGB", mgb);
		map.put("MCL", mcl);
		map.put("MCS", mcs);
		map.put("MCP", mcp);
		map.put("MCB", mcb);
		
		return map;
	}
    public HashMap<String,Object> getCOMap() {
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		COMC omc = new COMC();
		CNEL nel = new CNEL();
		CEQH eqh = new CEQH();
		CCRD crd = new CCRD();
		CPRT prt = new CPRT();
		CPRB prb = new CPRB();
		CTNL tnl = new CTNL();
		CLBS lbs = new CLBS();
		CTPI tpi = new CTPI();
		CTPB tpb = new CTPB();
		CMPI mpi = new CMPI();
		CMTR mtr = new CMTR();
		CMTL mtl = new CMTL();
		CPSW psw = new CPSW();
		CPWP pwp = new CPWP();
		CPWT pwt = new CPWT();
		CETH eth = new CETH();
		CESP esp = new CESP();
		CESI esi = new CESI();
		CTDM tdm = new CTDM();
		CETP etp = new CETP();
		CBRD brd = new CBRD();
		CL3I l3i = new CL3I();
		CL3P l3p = new CL3P();
		CL3T l3t = new CL3T();
		CTPL tpl = new CTPL();
		CSBN sbn = new CSBN();
		CSNN snn = new CSNN();
		CEPG epg = new CEPG();
		CEPU epu = new CEPU();
		CPTG ptg = new CPTG();
		CPGU pgu = new CPGU();
		CSTT stt = new CSTT();
		CSRT srt = new CSRT();
		CSRR srr = new CSRR();
		CIGT igt = new CIGT();
		CIGL igl = new CIGL();
		CNWS nws = new CNWS();
		CMGP mgp = new CMGP();
		CMGB mgb = new CMGB();
		CMCL mcl = new CMCL();
		CMCS mcs = new CMCS();
		CMCP mcp = new CMCP();
		CMCB mcb = new CMCB();
		
		map.put("OMC", omc);
		map.put("NEL", nel);
		map.put("EQH", eqh);
		map.put("CRD", crd);
		map.put("PRT", prt);
		map.put("PRB", prb);
		map.put("TNL", tnl);
		map.put("LBS", lbs);
		map.put("TPI", tpi);
		map.put("TPB", tpb);
		map.put("MPI", mpi);
		map.put("MTR", mtr);
		map.put("MTL", mtl);
		map.put("PSW", psw);
		map.put("PWP", pwp);
		map.put("PWT", pwt);
		map.put("ETH", eth);
		map.put("ESP", esp);
		map.put("ESI", esi);
		map.put("TDM", tdm);
		map.put("ETP", etp);
		map.put("BRD", brd);
		map.put("L3I", l3i);
		map.put("L3P", l3p);
		map.put("L3T", l3t);
		map.put("TPL", tpl);
		map.put("SBN", sbn);
		map.put("SNN", snn);
		map.put("EPG", epg);
		map.put("EPU", epu);
		map.put("PTG", ptg);
		map.put("PGU", pgu);
		map.put("STT", stt);
		map.put("SRT", srt);
		map.put("SRR", srr);
		map.put("IGT", igt);
		map.put("IGL", igl);
		map.put("NWS", nws);
		map.put("MGP", mgp);
		map.put("MGB", mgb);
		map.put("MCL", mcl);
		map.put("MCS", mcs);
		map.put("MCP", mcp);
		map.put("MCB", mcb);
		
		return map;
	}
    
    
    
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
		executeDelete("delete  from CNEL c where c.emsName = '" + emsdn + "'", CNEL.class);

		if (meList != null && meList.size() > 0) {
			for (NEL me : meList) {
				CNEL device = transNEL(me);
				di.insert(device);
			}
		}
		di.end();
	}
    
    public CNEL transNEL(NEL me) {
    	CNEL device = new CNEL();
		device.setEmsName(emsdn);
		device.setDn(me.getRmUID());
		device.setSid(DatabaseUtil.nextSID(device));
		
		device.setRmUID(me.getRmUID());
		device.setLocation(me.getLocation());
		device.setProductName(me.getProductName());
		device.setIPAddress(me.getIPAddress());
		
        return device;
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
//    	ZTE_SPN_Migrator loader1 = new ZTE_SPN_Migrator ("D:\\123.db","ZJ-FH-1-OTN");
//    	HashMap<String,Object> boMap = new HashMap<String,Object>();
//    	boMap.put("NEL", new NEL());
//    	HashMap<String,Object> coMap = new HashMap<String,Object>();
//		coMap.put("NEL", new CNEL());
//		List<BObject> boList = new ArrayList<BObject>();
//		boList.add(bo);
//    	// 获取新数据
//		List<CdcpObject> cdcpList = null;
//		try {
//			cdcpList = loader1.getCdcpObject(boMap.get("NEL").getClass(), coMap.get("NEL").getClass(), boList);
//		} catch (Exception e) {
//			loader1.getLogger().error("获取新数据出错:" + e.getMessage());
//			e.printStackTrace();
//		}
//		loader1.batchMigrate("");
    	
        String fileName=  "D:\\123-mt.db";
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

        HWU2000SPNMigrator loader = new HWU2000SPNMigrator (fileName, emsdn){
            public void afterExecute() {
                printTableStat();
            }
        };
        loader.execute();
    }


}
