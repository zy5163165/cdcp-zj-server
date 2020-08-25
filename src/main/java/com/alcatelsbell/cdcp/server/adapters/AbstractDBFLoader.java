package com.alcatelsbell.cdcp.server.adapters;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.asb.mule.probe.framework.entity.CRD;
import org.asb.mule.probe.framework.entity.CTP;
import org.asb.mule.probe.framework.entity.CTP2;
import org.asb.mule.probe.framework.entity.CrossConnect;
import org.asb.mule.probe.framework.entity.EDS_PTN;
import org.asb.mule.probe.framework.entity.EQH;
import org.asb.mule.probe.framework.entity.Equipment;
import org.asb.mule.probe.framework.entity.EquipmentHolder;
import org.asb.mule.probe.framework.entity.FlowDomainFragment;
import org.asb.mule.probe.framework.entity.HW_MSTPBindingPath;
import org.asb.mule.probe.framework.entity.ManagedElement;
import org.asb.mule.probe.framework.entity.NEL;
import org.asb.mule.probe.framework.entity.PRT;
import org.asb.mule.probe.framework.entity.PTP;
import org.asb.mule.probe.framework.entity.R_FTP_PTP;
import org.asb.mule.probe.framework.entity.R_TrafficTrunk_CC_Section;
import org.asb.mule.probe.framework.entity.SBN;
import org.asb.mule.probe.framework.entity.SNN;
import org.asb.mule.probe.framework.entity.Section;
import org.asb.mule.probe.framework.entity.SubnetworkConnection;
import org.asb.mule.probe.framework.entity.TPL;
import org.asb.mule.probe.framework.entity.TopoNode;
import org.asb.mule.probe.framework.entity.TrafficTrunk;
import org.asb.mule.probe.framework.entity.TrailNtwProtection;
import org.asb.mule.probe.framework.entity.spn.LBS;
import org.asb.mule.probe.framework.entity.spn.PRB;
import org.asb.mule.probe.framework.entity.spn.PSW;
import org.asb.mule.probe.framework.entity.spn.PWT;
import org.asb.mule.probe.framework.entity.spn.TNL;
import org.asb.mule.probe.framework.entity.spn.TPB;
import org.asb.mule.probe.framework.entity.spn.TPI;
import org.asb.mule.probe.framework.service.Constant;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;

import com.alcatelsbell.cdcp.common.Constants;
import com.alcatelsbell.cdcp.nbi.irm.IrmCollectReportUtil;
import com.alcatelsbell.cdcp.nbi.irm.TJ_Differenceslist;
import com.alcatelsbell.cdcp.nbi.irm.TJ_INTERFACE_OTN;
import com.alcatelsbell.cdcp.nbi.irm.TJ_INTERFACE_PTN;
import com.alcatelsbell.cdcp.nbi.irm.TJ_INTERFACE_SDH;
import com.alcatelsbell.cdcp.nbi.model.CCTP;
import com.alcatelsbell.cdcp.nbi.model.CCrossConnect;
import com.alcatelsbell.cdcp.nbi.model.CDevice;
import com.alcatelsbell.cdcp.nbi.model.CEMS;
import com.alcatelsbell.cdcp.nbi.model.CEquipment;
import com.alcatelsbell.cdcp.nbi.model.CEthRoute;
import com.alcatelsbell.cdcp.nbi.model.CEthTrunk;
import com.alcatelsbell.cdcp.nbi.model.CFTP_PTP;
import com.alcatelsbell.cdcp.nbi.model.CIPAddress;
import com.alcatelsbell.cdcp.nbi.model.CIPRoute;
import com.alcatelsbell.cdcp.nbi.model.CMP_CTP;
import com.alcatelsbell.cdcp.nbi.model.CPTP;
import com.alcatelsbell.cdcp.nbi.model.CPW;
import com.alcatelsbell.cdcp.nbi.model.CPWE3;
import com.alcatelsbell.cdcp.nbi.model.CPWE3_PW;
import com.alcatelsbell.cdcp.nbi.model.CPW_Tunnel;
import com.alcatelsbell.cdcp.nbi.model.CPath;
import com.alcatelsbell.cdcp.nbi.model.CProtectionGroup;
import com.alcatelsbell.cdcp.nbi.model.CProtectionGroupTunnel;
import com.alcatelsbell.cdcp.nbi.model.CRack;
import com.alcatelsbell.cdcp.nbi.model.CRoute;
import com.alcatelsbell.cdcp.nbi.model.CSection;
import com.alcatelsbell.cdcp.nbi.model.CShelf;
import com.alcatelsbell.cdcp.nbi.model.CSlot;
import com.alcatelsbell.cdcp.nbi.model.CSubnetwork;
import com.alcatelsbell.cdcp.nbi.model.CSubnetworkDevice;
import com.alcatelsbell.cdcp.nbi.model.CTunnel;
import com.alcatelsbell.cdcp.nbi.model.CTunnel_Section;
import com.alcatelsbell.cdcp.nbi.model.CdcpObject;
import com.alcatelsbell.cdcp.nodefx.EmsJob;
import com.alcatelsbell.cdcp.nodefx.NEWrapper;
import com.alcatelsbell.cdcp.server.CdcpServerUtil;
import com.alcatelsbell.cdcp.server.MigrateManager;
import com.alcatelsbell.cdcp.server.MigrateRunnable;
import com.alcatelsbell.cdcp.server.da.DAEntity;
import com.alcatelsbell.cdcp.server.da.DAUtil;
import com.alcatelsbell.cdcp.util.DSUtil;
import com.alcatelsbell.cdcp.util.DataInserter;
import com.alcatelsbell.cdcp.util.DatabaseUtil;
import com.alcatelsbell.cdcp.util.DicConst;
import com.alcatelsbell.cdcp.util.DicUtil;
import com.alcatelsbell.cdcp.util.MigrateThread;
import com.alcatelsbell.cdcp.util.SqliteDelegation;
import com.alcatelsbell.nms.common.Detect;
import com.alcatelsbell.nms.common.SysUtil;
import com.alcatelsbell.nms.db.components.service.JPASupport;
import com.alcatelsbell.nms.db.components.service.JPASupportFactory;
import com.alcatelsbell.nms.db.components.service.JPASupportSpringImpl;
import com.alcatelsbell.nms.db.components.service.JPAUtil;
import com.alcatelsbell.nms.db.components.service.JpaServerUtil;
import com.alcatelsbell.nms.util.ObjectUtil;
import com.alcatelsbell.nms.util.SysProperty;
import com.alcatelsbell.nms.util.log.LogUtil;
import com.alcatelsbell.nms.valueobject.BObject;
import com.alcatelsbell.nms.valueobject.CdcpDictionary;
import com.alcatelsbell.nms.valueobject.sys.Ems;

/**
 * Author: Ronnie.Chen
 * Date: 13-7-4
 * Time: 下午1:48
 * rongrong.chen@alcatel-sbell.com.cn
 */
public abstract class AbstractDBFLoader {
	protected String fileUrl = null;
	protected Serializable resultObject = null;
	protected String emsdn = null;
	// protected FileLogger logger = new FileLogger("migrate.log");
	protected Long emsid = -1l;
	protected String taskSerial = null;
	protected SqliteDelegation sd = null;
	JPASupport sqliteJPASupport = null;
	protected Map<String, String> shelfTypeMap = null;

	protected static final String FDFRT_POINT_TO_POINT = "FDFRT_POINT_TO_POINT";
	protected static final String FDFRT_POINT_TO_MULTIPOINT = "FDFRT_POINT_TO_MULTIPOINT";
	protected static final String FDFRT_MULTIPOINT = "FDFRT_MULTIPOINT";
	protected static final String ST_SIMPLE = "ST_SIMPLE";

	private String JOB_TYPE = null;
	private String shelfType = null;
    private long startTime = System.currentTimeMillis();
	// test
	protected String emsType = "SDH";

	private HashMap attributes = new HashMap();

	private MigrateRunnable runnable = null;

	protected boolean migrateLogical = true;

	public void setMigrateLogical(boolean migrateLogical) {
		this.migrateLogical = migrateLogical;
	}

	public boolean isMigrateLogical() {
		return migrateLogical;
	}

	public void setAttribute(String key,Object value ) {
		attributes.put(key,value);
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public HashMap getAttributesMap() {
		return attributes;
	}
	private Logger m_logger = null;
	public Logger getLogger() {
		Logger logger = MigrateThread.thread().getLogger();
		return logger;
	}

	public String getTaskSerial() {
		return taskSerial;
	}

	public void setTaskSerial(String taskSerial) {
		this.taskSerial = taskSerial;
	}

	public boolean isTableHasData(Class cls) {
		try {
			long count = sd.findObjectsCount("select count(c.id) from "+cls.getSimpleName()+" c");

		    getLogger().info(cls.getSimpleName()+" size = "+count);
			if (count > 0) return true;
		} catch (Exception e) {
			getLogger().error(e, e);
		}

		return false;

	}

	public void setRunnable(MigrateRunnable runnable) {
		this.runnable = runnable;
	}

	public void execute() throws Exception {
		checkSuspend();
		m_logger = getLogger();
		int beginHour = SysProperty.getInt("cdcp.permitStartHour",-1);         // 17
		int endHour = SysProperty.getInt("cdcp.permitEndHour",-1);           // 8
		if (beginHour > 0 && endHour > 0 ) {
			Calendar calendar = Calendar.getInstance();

			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
			if (dayofweek == Calendar.SATURDAY || dayofweek == Calendar.SUNDAY) {
				System.out.println();
			}
			else if ((beginHour> endHour && hour < beginHour && hour > endHour) || (beginHour < endHour && (hour < beginHour || hour > endHour))) {
				throw new Exception("每天"+beginHour+"点-"+endHour+"点禁止同步，同步终止");
			}
		}



        startTime = System.currentTimeMillis();
        try {
            DatabaseUtil.reset();
            JOB_TYPE = EmsJob.JOB_TYPE_SYNC_EMS;
            if (!new File(fileUrl).exists()) throw new Exception("数据文件不存在，同步终止");
            sqliteJPASupport = JPASupportFactory.createSqliteJPASupport(fileUrl);
            sd = new SqliteDelegation(sqliteJPASupport);

			getLogger().info("Using Loader:"+this.getClass().getName());
                checkDataFile();
               getLogger().info("数据校验文件合格:"+fileUrl);
		   getLogger().info("attrubites = "+attributes);

		//	getLogger().info("migrate logical = "+migrateLogical);

            doExecute();
            afterExecute();
            VDeviceMergeRunnable vdm = new VDeviceMergeRunnable(getLogger());
            vdm.run(emsdn);
        } catch (Exception e) {
            getLogger().error(e, e);


            throw e;
        }   finally {
			try {

				sd.release();
			} catch (Exception e) {

			}

			try {
                if (!SysProperty.getString("cdcp.migrate.deleteDBFile","true").equalsIgnoreCase("false")) {
					if (!fileUrl.endsWith("-mt.db")) {
						getLogger().info("Delete file : " + fileUrl);
						boolean delete = new File(fileUrl).getAbsoluteFile().delete();
						getLogger().info("Delete file : " + fileUrl + " = " + delete);
					}
                }
                int size = MigrateManager.getInstance().getQueue().size();
                long cost = System.currentTimeMillis() - startTime;
                getLogger().info("spend : "+((cost) /  (1000l * 60l))+" minutes");
                getLogger().info(size+" tasks is waiting to be processed !");



            } catch (Throwable e) {
                getLogger().error(e, e);
            }
			m_logger = null;
            MigrateThread.thread().end();

        }
    }

	protected void checkSuspend() {
		if (runnable != null)
			runnable.checkSuspend();
	}

	private HashMap<String,Long> times = new HashMap<String, Long>();
	protected long logTime(String name) {
		Long t = times.get(name);
		if (t == null) {
			times.put(name,System.currentTimeMillis());
		} else {
			long tt = System.currentTimeMillis() - t;
			getLogger().info("[TIME] "+name+" spend "+tt+"ms");
			times.remove(name);
		}
		return System.currentTimeMillis();
	}


	protected void checkDataFile()  throws Exception{
        EDS_PTN eds = (EDS_PTN)JpaServerUtil.getInstance().findOneObject("select c from EDS_PTN c where c.taskSerial = '"+taskSerial+"' order by c.id desc");
        if (eds != null && eds.getFromWhere() == 100) {
            throw new Exception("数据文件校验不合格:"+fileUrl+" ;eds="+eds.getAdditinalInfo());
        }
        checkOneTable(ManagedElement.class, NEL.class);
    }
    protected void checkTable(Class  cls) throws Exception {
        checkTable(cls,0);
    }
    private void checkTable(Class  cls,int lowerLimit) throws Exception {
        Object o = sd.queryOneObject("select count(c.id) from " + cls.getSimpleName() + " c");
        if (o != null ) {
            int count = ((Long) o).intValue();
            if (count == 0 || count <= lowerLimit) {
                throw new Exception(cls.getSimpleName()+" count error : size = "+count+"; lowerlimit = "+lowerLimit);
            }
        }
    }
    
    protected void checkOneTable(Class cls1, Class  cls2) throws Exception {
    	int lowerLimit = 0;
    	Object o1 = sd.queryOneObject("select count(c.id) from " + cls1.getSimpleName() + " c");
    	Object o2 = sd.queryOneObject("select count(c.id) from " + cls2.getSimpleName() + " c");
        if (o1 != null && o2 != null) {
            int count1 = ((Long) o1).intValue();
            int count2 = ((Long) o2).intValue();
            if (count1 <= lowerLimit && count2 <= lowerLimit) {
                throw new Exception(cls1.getSimpleName()+" count error : size = "+count1+"; "+cls2.getSimpleName()+" count error : size = "+count2+"; lowerlimit = "+lowerLimit);
            }
        }
    }

    public void migrateSingleDevice() throws Exception {
		JOB_TYPE = EmsJob.JOB_TYPE_SYNC_DEVICE;
		getLogger().error("1#######################################");
		executeMigrateDevice();
		updateEmsStatus(Constants.CEMS_STATUS_READY);
		try {
			CdcpServerUtil.sendMigrateLogMessage(taskSerial, emsdn, "同步结束", 100);
		} catch (Exception e) {
			getLogger().error(e, e);
		}

	}

	public abstract void doExecute() throws Exception;

	public void executeMigrateDevice() throws Exception {
		try {
			checkEMS(emsdn, null);
		} catch (Exception e) {
			// throw new Exception("EMS not existed");
			getLogger().error(e, e);
			return;
		}
		getLogger().error("2#######################################");
		NEWrapper neWrapper = (NEWrapper) resultObject;
		ManagedElement me = neWrapper.getMe();
		if (me == null) {
			getLogger().error("### me is null !");
			return;
		}
        getLogger().info("Device Dn = "+me.getDn());
        shelfType = me.getProductName();
		List<Equipment> equipments = neWrapper.getEquipments();
		List<EquipmentHolder> equipmentHolders = neWrapper.getEquipmentHolders();
		List<PTP> ptps = neWrapper.getPtps();
		logAction("migrateManagedElement", "同步网元", 1);
		migrateManagedElement(me);
		logAction("migrateEquipmentHolder", "同步槽道", 10);
		migrateEquipmentHolder(me.getDn(), equipmentHolders);
		logAction("migrateEquipment", "同步板卡", 30);
		migrateEquipment(me.getDn(), equipments);
		logAction("migratePTP", "同步端口", 60);
		migratePTP(me.getDn(), ptps);

		logAction("migrateCTP", "同步CTP", 80);
		migrateCTP(me.getDn(), neWrapper.getCtps());
		getLogger().info("release");
	}

	protected void logAction(String name, String desc, int percentage) {

		checkSuspend();

        long total = Runtime.getRuntime().totalMemory() / 1000000l;
        long free = Runtime.getRuntime().freeMemory() / 1000000l;
        long max = Runtime.getRuntime().maxMemory() / 1000000l;
        getLogger().info("Total Memory = " + total + "M;" + " Free Memory = " + free + "M; Max Memory = " + max + "M");
//        getLogger().info("Current Memory Useed : ");
		getLogger().info(percentage + "%  [" + name + "], " + desc);
		try {
			CdcpServerUtil.sendMigrateLogMessage(taskSerial, emsdn, name + "," + (desc == null ? "" : desc), percentage);
		} catch (Exception e) {
			getLogger().error(e, e);
		}
	}

	public void afterExecute() {
		HashMap<Class,Number> statMap = printTableStat();
		processIRMStat(statMap);
		saveDA();
		updateEmsStatus(Constants.CEMS_STATUS_READY);
		try {
			CdcpServerUtil.sendMigrateLogMessage(taskSerial, emsdn, "同步结束", 100);
		} catch (Exception e) {
			getLogger().error(e, e);
		}
	}

	private void processIRMStat(HashMap<Class, Number> map) {
		if(SysProperty.getString("cdcp.da","on").equals("off"))
			return;
		try {
			Ems ems = CdcpServerUtil.findEms(emsdn);
			if (ems != null) {
                if (ems.getProtocalType() != null && ems.getProtocalType() == CdcpDictionary.PROTOCALTYPE.PTN.value) {
                    TJ_INTERFACE_PTN ptn = new TJ_INTERFACE_PTN();
                    ptn.setEmsName(emsdn);
                    ptn.setAcquisitionTime(new Date());
                    ptn.setPtnCardNumber( toInteger(map.get(CEquipment.class)));
                    ptn.setPtnDeviceNumber(toInteger(map.get(CDevice.class)));
                    ptn.setPtnPortNumber(toInteger(map.get(CPTP.class)));
                    ptn.setPtnPWE3Number(toInteger(map.get(CPWE3.class)));
                    ptn.setPtnPWNumber(toInteger(map.get(CPW.class)));
                    ptn.setPtnSectionNumber(toInteger(map.get(CSection.class)));
                    ptn.setPtnShelfNumber(toInteger(map.get(CShelf.class)));
                    ptn.setPtnSubnetworkNumber(toInteger(map.get(CSubnetwork.class)));
                    ptn.setPtnTunnelNumber(toInteger(map.get(CTunnel.class)));

                    IrmCollectReportUtil.insertPtnData(ptn);


                }
				else if (ems.getProtocalType() != null && ems.getProtocalType() == CdcpDictionary.PROTOCALTYPE.SDH.value) {
					TJ_INTERFACE_SDH ptn = new TJ_INTERFACE_SDH();
					ptn.setEmsName(emsdn);
					ptn.setAcquisitionTime(new Date());
					ptn.setSdhCardNumber(toInteger(map.get(CEquipment.class)));
					ptn.setSdhDeviceNumber(toInteger(map.get(CDevice.class)));
					ptn.setSdhPortNumber(toInteger(map.get(CPTP.class)));
					ptn.setSdhEthrouteNumber(toInteger(map.get(CEthRoute.class)));
					ptn.setSdhEthtrunkNumber(toInteger(map.get(CEthTrunk.class)));
					ptn.setSdhSectionNumber(toInteger(map.get(CSection.class)));
					ptn.setSdhShelfNumber(toInteger(map.get(CShelf.class)));
					ptn.setSdhSubnetworkNumber(toInteger(map.get(CSubnetwork.class)));
					ptn.setSdhRouteNumber(toInteger(map.get(CRoute.class)));
					ptn.setChannellNumber(toInteger(map.get(CPath.class)));


					IrmCollectReportUtil.insertSdhData(ptn);


				}
				else if (ems.getProtocalType() != null && (ems.getProtocalType() == CdcpDictionary.PROTOCALTYPE.WDM.value
				|| ems.getProtocalType() == CdcpDictionary.PROTOCALTYPE.OTN.value
				)) {
					TJ_INTERFACE_OTN ptn = new TJ_INTERFACE_OTN();
					ptn.setEmsName(emsdn);
					ptn.setAcquisitionTime(new Date());
					ptn.setOtnCardNumber(toInteger(map.get(CEquipment.class)));
					ptn.setOtnDeviceNumber(toInteger(map.get(CDevice.class)));
					ptn.setOtnPortNumber(toInteger(map.get(CPTP.class)));

					JPASupport jpaSupport = createJPASupport();
					try {
						List list = JPAUtil.getInstance().queryQL(jpaSupport, "select count(c.id) from CSection c where c.type = 'OTS'");

						ptn.setOtnOtsNumber(toInteger((Number)list.get(0)));
						list = JPAUtil.getInstance().queryQL(jpaSupport, "select count(c.id) from CSection c where c.type = 'OMS'");


						ptn.setOtnOmsNumber(toInteger(toInteger((Number) list.get(0))));
					} finally {
						jpaSupport.release();
					}

					ptn.setOtnOchNumber(toInteger(map.get(CPath.class)));



					ptn.setOtnShelfNumber(toInteger(map.get(CShelf.class)));

					ptn.setOtnRouteNumber(toInteger(map.get(CRoute.class)));



					IrmCollectReportUtil.insertOtnData(ptn);




				}
            }
		} catch (Exception e) {
			getLogger().error(e, e);
		}
	}

	private Integer toInteger(Number number) {
		if (number == null) return null;
		return number.intValue();
	}


	private void saveDA() {
		if(SysProperty.getString("cdcp.da","on").equals("off"))
			return;

		HashMap lastData = (HashMap)ObjectUtil.readObject(emsdn);
		int[] codes = getDACodes();
		HashMap map = new HashMap();
		JPASupport jpaSupport = new JPASupportSpringImpl("entityManagerFactoryData");
		for (int code : codes) {
			DAEntity daEntity = DAUtil.getDAEntity(code);
			if (daEntity != null) {
				String ql = daEntity.ql;
				ql = ql.replaceAll(":emsName",emsdn);
				try {
					List names = JPAUtil.getInstance().queryQL(jpaSupport, ql);
					map.put(code,names);

					if (lastData != null) {
						List<String> lastNames = (List<String>) lastData.get(code);
						compare(code,lastNames,names);
					}
				} catch (Exception e) {
					getLogger().error(e,e);
				}

			}
		}


		jpaSupport.release();

		try {
			ObjectUtil.saveObject(emsdn, map);
		} catch (Exception e) {
			getLogger().error(e,e);
		}
	}

	private void compare(int code,List<String> lastNames, List<String> names) {
		if (names == null || names.isEmpty() || lastNames == null || lastNames.isEmpty()) {
			getLogger().error("本次或者上次采集为空 code="+code);
			return;
		}
		List<String> adds = new ArrayList<String>();
		List<String> dels = new ArrayList<String>();
		 HashSet<String> lastSet = new HashSet<String>(lastNames);
		HashSet<String> nameSet = new HashSet<String>(names);
		for (String name : nameSet) {
			if (!lastSet.contains(name)) {
				adds.add(name);

			}   else
				lastSet.remove(name);
		}

		dels = new ArrayList<String>(lastSet);

		getLogger().info("");
		getLogger().info("code="+code);
		Connection connection = null;
		try {
			 connection = IrmCollectReportUtil.getConnection();
			Date date = new Date();
			int add_count = 0;
			int del_count = 0;

			for (String add : adds) {
           //     getLogger().info(" + "+add);
				TJ_Differenceslist df = new TJ_Differenceslist();
                df.setCompartTime(date);
                df.setEmsName(emsdn);
                df.setDifferencesType(1);
                df.setObjectType(code);
                df.setObjectName(add);
                IrmCollectReportUtil.collectionObjectDefferencesList(connection,df);
				add_count++;
            }
			for (String del : dels) {
				del_count ++;
           //     getLogger().info(" - "+del);
				TJ_Differenceslist df = new TJ_Differenceslist();
                df.setCompartTime(date);
                df.setEmsName(emsdn);
                df.setDifferencesType(2);
                df.setObjectType(code);
                df.setObjectName(del);
                IrmCollectReportUtil.collectionObjectDefferencesList(connection,df);
            }
			connection.commit();
			getLogger().info("code="+code+" add="+add_count+" del="+del_count);
		} catch (Exception e) {
			getLogger().error(e, e);
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					getLogger().error(e, e);
				}
		}


	}


	protected int[] getDACodes(){
		Ems ems = null;
		try {
			ems = (Ems) JpaServerUtil.getInstance().findObjectByDN(Ems.class, emsdn);
		} catch (Exception e) {
			getLogger().error(e, e);
		}

		if (ems != null && ems.getProtocalType() != null && ems.getProtocalType() == CdcpDictionary.PROTOCALTYPE.SDH.value)
			return new int[] {10,11,12,13,14,15,16,17,18,19};

		if (ems != null && ems.getProtocalType() != null && ems.getProtocalType() == CdcpDictionary.PROTOCALTYPE.OTN.value)
			return new int[] {21,22,23,24,25,26,27,28};
		return new int[] {1,2,3,4,5,6,7,8,9};
	}

	protected void updateEmsStatus(int status) {
		JPASupport jpaSupport = new JPASupportSpringImpl("entityManagerFactoryData");

		try {
			jpaSupport.begin();
			Map map  = new HashMap();
			Date date = new Date();
			map.put("date", date);

			JPAUtil.getInstance().executeQL(jpaSupport, "update CEMS c set c.status = " + status + ",c.updateDate = :date where c.dn = '" + emsdn + "'",map);
			jpaSupport.end();
		} catch (Exception e) {
			getLogger().error(e, e);
			jpaSupport.rollback();

		} finally {
			jpaSupport.release();
		}
	}

	private BObject saveObject(BObject object) {
		JPASupport jpaSupport = new JPASupportSpringImpl("entityManagerFactoryData");

		try {
			jpaSupport.begin();
			BObject bo = JPAUtil.getInstance().saveObject(jpaSupport, -1, object);
			jpaSupport.end();
			return bo;
		} catch (Exception e) {
			getLogger().error(e, e);
			jpaSupport.rollback();

		} finally {
			jpaSupport.release();
		}
		return null;
	}

	public BObject findObjectByDn(Class cls, String dn) {
		JPASupport jpaSupport = createJPASupport();
		try {

			return JPAUtil.getInstance().findObjectByDn(jpaSupport, -1, cls, dn);

		} catch (Exception e) {
			getLogger().error(e, e);

		} finally {
			jpaSupport.release();
		}
		return null;
	}

    public List findObjects(Class cls, String ql) {
        JPASupport jpaSupport = createJPASupport();
        try {

            return JPAUtil.getInstance().findObjects(jpaSupport, ql);

        } catch (Exception e) {
            getLogger().error(e, e);

        } finally {
            jpaSupport.release();
        }
        return null;
    } 
	// protected Long toSid(Long sourceId) {
	// return sourceId * 10000 + emsid;
	// }
	protected void checkEMS(String emsdn, String vendor) throws Exception {
		if (!EmsJob.JOB_TYPE_SYNC_DEVICE.equals(JOB_TYPE)) {
			List<ManagedElement> query = sd.query("select c from ManagedElement c ", 0, 1);
			if (query != null && query.size() > 0) {
				ManagedElement em = query.get(0);
				emsdn = em.getEmsName();
				this.emsdn = emsdn;
			}
		}

		if (emsdn == null)
			throw new Exception("Unkown emsname = " + emsdn);

		CEMS ems = null;
		try {
			ems = (CEMS) findObjectByDn(CEMS.class, emsdn);
		} catch (Exception e) {
			getLogger().error(e, e);

		}

		if (ems == null) {
			if (vendor == null)
				throw new Exception("Vendor is null");
			ems = new CEMS();
			ems.setDn(emsdn);
			ems.setName(emsdn);
			ems.setSid(DatabaseUtil.getSID(CEMS.class, emsdn));
			ems.setCnName(emsdn);
			ems.setVendor(vendor);
			ems.setStatus(Constants.CEMS_STATUS_MIGRATING);
			saveObject(ems);
		} else {
			int i = 0;
			while (ems.getStatus() != null && (ems.getStatus().intValue() == Constants.CEMS_STATUS_UPPER_LEVEL_LOCK)) {
				getLogger().info(emsdn+"Locked by upper level app ,try after 10 minutes");
				Thread.sleep(10 * 60 * 1000l);
				ems = (CEMS) findObjectByDn(CEMS.class, emsdn);
				if (i++ > 10)
					throw new Exception(emsdn+" Locked by upper level app,and timeout !");
			}
			// ems.setStatus(Constants.CEMS_STATUS_MIGRATING);
			updateEmsStatus(Constants.CEMS_STATUS_MIGRATING);
		}
		emsid = ems.getSid();
		// if (emsid == null) {
		// ems.setSid(DatabaseUtil.nextSID(ems));
		// ems.setStatus(Constants.CEMS_STATUS_MIGRATING);
		// jpaSupport.begin();
		// JPAUtil.getInstance().storeObjectByDn(jpaSupport, -1, ems);
		// jpaSupport.end();
		// emsid = ems.getSid();
		// }
		// jpaSupport.end();
		// jpaSupport.release();
		if (emsid > 10000)
			throw new Exception("Too big emsid : " + emsid);
	}

	protected void removeDuplicateDN(List bos) {
        int count = 0;
		HashMap map = new HashMap();
        String name = null;
		for (int i = 0; i < bos.size(); i++) {
			BObject bObject = (BObject) bos.get(i);
            name = bObject.getClass().getName();
			if (map.get(bObject.getDn()) != null)
			    count++;
			map.put(bObject.getDn(), bObject);
		}
		bos.clear();
		bos.addAll(map.values());
//        if (count > 0)
//        getLogger().error("DuplicateDN "+name+" count = " + count);
    }

	// 将重复的dn后加上 _1/2...
	protected void handleDuplicateDN(List bos) {
		HashMap map = new HashMap();

		HashMap<String,Integer> dnNos = new HashMap<String, Integer>();
		for (int i = 0; i < bos.size(); i++) {
			BObject bObject = (BObject) bos.get(i);
			if (map.get(bObject.getDn()) != null) {
				String olddn = bObject.getDn();
				int no = 1;
				if (dnNos.get(olddn) != null)
					no = dnNos.get(olddn) + 1;
				dnNos.put(olddn,no);
				bObject.setDn(olddn+"__"+no);

			}
			map.put(bObject.getDn(), bObject);
		}
		bos.clear();
		bos.addAll(map.values());
//        if (count > 0)
//        getLogger().error("DuplicateDN "+name+" count = " + count);
	}



	protected void executeTableDelete(final String tableName,final String emsname) throws Exception{
		getLogger().info("executeTableDelete : "+tableName+" ems="+emsname);
		JPASupport ctx = new JPASupportSpringImpl("entityManagerFactoryData");
		try {
			EntityManager entityManager = ctx.getEntityManager();

			HibernateEntityManager hibernateEntityManager = (HibernateEntityManager)entityManager;
			final AtomicBoolean finish = new AtomicBoolean(false);
			while (!finish.get()) {

                entityManager.getTransaction().begin();
                hibernateEntityManager.getSession().doWork(new Work() {
                    @Override
                    public void execute(Connection connection) throws SQLException {
                        String sql = "delete from "+tableName+" where emsname='"+emsname+"' and rownum < 10000";
                        int i = connection.prepareStatement(sql).executeUpdate();
                        if (i == 0)
                            finish.set(true);

                    }
                });
                entityManager.getTransaction().commit();
            }
		} catch (Exception e) {
			throw e;
		} finally {
			ctx.release();
		}


	}
	protected void executeDelete(String ql, Class cls) {
        getLogger().info("Delete QL : "+ql);
		JPASupport jpaSupport = new JPASupportSpringImpl("entityManagerFactoryData");
		try {
			jpaSupport.begin();
			JPAUtil.getInstance().executeQL(jpaSupport, ql);
			jpaSupport.end();
		} catch (Exception e) {
			getLogger().error(e, e);
			jpaSupport.rollback();
		} finally {
			jpaSupport.release();
		}
	}

	protected void executeNativeSql(String sql) {
		getLogger().info("Delete native sql : "+sql);
		JPASupport jpaSupport = new JPASupportSpringImpl("entityManagerFactoryData");
		try {
			jpaSupport.begin();
			EntityManager em = jpaSupport.getEntityManager();
			int re = em.createNativeQuery(sql).executeUpdate();

			jpaSupport.end();
		} catch (Exception e) {
			getLogger().error(e, e);
			jpaSupport.rollback();
		} finally {
			jpaSupport.release();
		}
	}

	protected void migrateSubnetwork() throws Exception {
		if (!isTableHasData(TopoNode.class))
			return;
		executeDelete("delete from CSubnetwork c where c.emsName = '" + emsdn + "'", CSubnetwork.class);
		executeDelete("delete from CSubnetworkDevice c where c.emsName = '" + emsdn + "'", CSubnetworkDevice.class);
		List<TopoNode> topoNodes = sd.queryAll(TopoNode.class);
		DataInserter di = new DataInserter(emsid);
		LinkedList<TopoNode> temp = new LinkedList<TopoNode>();
	//	topoNodes.clear();
		for (TopoNode topoNode : topoNodes) {
			if (topoNode.getParent() == null || topoNode.getParent().isEmpty()) {
				temp.addLast(topoNode);
			}  else {

				temp.addFirst(topoNode);
			}
		}
		for (int i = 0; i < temp.size(); i++) {
			TopoNode topoNode = topoNodes.get(i);
			String name = topoNode.getName();
			String parent = topoNode.getParent();
			if (name.contains("TopoSubnetwork") || name.contains("NeBlock")) {
				CSubnetwork cSubnetwork = new CSubnetwork();
				cSubnetwork.setDn(topoNode.getName());
				cSubnetwork.setName(topoNode.getNativeemsname());
				cSubnetwork.setNativeemsname(topoNode.getNativeemsname());
				cSubnetwork.setSid(DatabaseUtil.nextSID(cSubnetwork));
				cSubnetwork.setEmsName(emsdn);
				if (parent != null && !parent.trim().isEmpty()) {
					cSubnetwork.setParentSubnetworkDn(parent);
					cSubnetwork.setParentSubnetworkId(DatabaseUtil.getSID(CSubnetwork.class, parent));
				}

				di.insert(cSubnetwork);
			} else if (name.contains("ManagedElement")) {
				// HZ-U2000-2-P@720897

				// if (name.contains("=") && name.trim().endsWith(";")) {
				if (true) {
					// String meid = name.substring(name.lastIndexOf("=")+1,name.lastIndexOf(";"));
					// String deviceDn = emsdn + "@" + meid;
					CSubnetworkDevice csd = new CSubnetworkDevice();
					csd.setDn(emsdn+"@"+topoNode.getDn());
					csd.setSid(DatabaseUtil.nextSID(csd));
					csd.setSubnetworkDn(parent);
					if (parent != null)
						csd.setSubnetworkId(DatabaseUtil.getSID(CSubnetwork.class, parent));
					csd.setDeviceDn(name);
					csd.setEmsName(emsdn);
                    if (DatabaseUtil.isSIDExisted(CDevice.class, csd.getDeviceDn())) {
                        csd.setDeviceId(DatabaseUtil.getSID(CDevice.class, csd.getDeviceDn()));
                        if (parent != null)
                            di.insert(csd);
                    }
				}
			}
		}

		di.end();
	}

	protected void migrateManagedElement() throws Exception {
		if (!isTableHasData(ManagedElement.class))
			return;
		List<ManagedElement> meList = sd.queryAll(ManagedElement.class);
		if (meList == null || meList.isEmpty()) {
			getLogger().info("ManagedElement is empty, return");
			return;
		}

		DataInserter di = new DataInserter(emsid);
		executeDelete("delete  from CDevice c where c.emsName = '" + emsdn + "'", CDevice.class);

		if (meList != null && meList.size() > 0) {
			for (ManagedElement me : meList) {
				CDevice device = transDevice(me);
				device.setSid(DatabaseUtil.nextSID(device));
                if(device.getAdditionalInfo().length() > 2000){
                    device.setAdditionalInfo(null);
                }
				di.insert(device);
			}
		}
		di.end();
	}

	protected void migrateManagedElement(ManagedElement me) throws Exception {
		DataInserter di = new DataInserter(emsid);
		executeDelete("delete  from CDevice c where c.dn = '" + me.getDn() + "'", CDevice.class);
		CDevice device = transDevice(me);
		device.setSid(DatabaseUtil.nextSID(device));
		device.setTag3("NEW");
		di.insert(device);
		di.end();
	}

	protected void migrateEquipment() throws Exception {
		if (!isTableHasData(Equipment.class))
			return;
		executeDelete("delete   from CEquipment c where c.emsName = '" + emsdn + "'", CEquipment.class);
		List<Equipment> equipments = sd.queryAll(Equipment.class);
		insertEquipments(equipments);
	}

	protected void migrateEquipment(String deviceDn, List<Equipment> equipments) throws Exception {
		executeDelete("delete  from CEquipment c where c.dn  like '" + deviceDn + "@%'", CEquipment.class);
		getLogger().info("Migrate equipments size = "+(equipments == null ? null : equipments.size()));
		insertEquipments(equipments);
	}

	private void insertEquipments(List<Equipment> equipments) throws Exception {
		DataInserter di = new DataInserter(emsid);
		if (equipments != null && equipments.size() > 0) {
			for (Equipment equipment : equipments) {
				CEquipment cEquipment = transEquipment(equipment);
                if (cEquipment != null)
				    di.insert(cEquipment);
			}
		}
		di.end();
	}

	protected void migrateEquipmentHolder() throws Exception {
		if (!isTableHasData(EquipmentHolder.class))
			return;
		List<EquipmentHolder> equipmentHolders = sd.queryAll(EquipmentHolder.class);
		if (equipmentHolders != null) {
			executeDelete("delete  from CShelf c where c.emsName = '" + emsdn + "'", CShelf.class);
			executeDelete("delete  from CRack c where c.emsName = '" + emsdn + "'", CRack.class);
			executeDelete("delete  from CSlot c where c.emsName = '" + emsdn + "'", CSlot.class);

			insertEquipmentHolders(equipmentHolders);
		}
	}

	protected void migrateEquipmentHolder(String deviceDn, List<EquipmentHolder> equipmentHolders) throws Exception {
//		executeDelete("delete  from CSlot c where c.parentDn = '" + deviceDn + "'", CSlot.class);
//		executeDelete("delete  from CRack c where c.parentDn = '" + deviceDn + "'", CRack.class);
//		executeDelete("delete  from CShelf c where c.parentDn = '" + deviceDn + "'", CShelf.class);
		removeDuplicateDN(equipmentHolders);

		executeDelete("delete  from CSlot c where c.dn like '" + deviceDn + "@%'", CSlot.class);
		executeDelete("delete  from CRack c where c.dn like  '" + deviceDn + "@%'", CRack.class);
		executeDelete("delete  from CShelf c where c.dn like '" + deviceDn + "@%'", CShelf.class);
		getLogger().info("Migrate equipmentHolders size = "+(equipmentHolders == null ? null : equipmentHolders.size()));
		insertEquipmentHolders(equipmentHolders);
	}

	protected void insertEquipmentHolders(List<EquipmentHolder> equipmentHolders) throws Exception {
		if (shelfTypeMap != null) {
			shelfTypeMap.clear();
			shelfTypeMap = null;
		}
		DataInserter di = new DataInserter(emsid);

		// // ////////////////// 将EH分类///////////////////
		List<EquipmentHolder> racks = new ArrayList<EquipmentHolder>();
		List<EquipmentHolder> shelfs = new ArrayList<EquipmentHolder>();
		List<EquipmentHolder> subshelfs = new ArrayList<EquipmentHolder>();
		List<EquipmentHolder> slots = new ArrayList<EquipmentHolder>();
		List<EquipmentHolder> subslots = new ArrayList<EquipmentHolder>();

		for (int i = 0; i < equipmentHolders.size(); i++) {
			EquipmentHolder equipmentHolder = equipmentHolders.get(i);
			// String dn = equipmentHolder.getDn();
			// String rack = CodeTool.extractValue(dn, "rack");
			// String shelf = CodeTool.extractValue(dn, "shelf");
			// String slot = CodeTool.extractValue(dn, "slot");
			// String subSlot = CodeTool.extractValue(dn, "sub_slot");

			// if (rack != null && shelf == null) {
			if (equipmentHolder.getHolderType().equals("rack")) {
				racks.add(equipmentHolder);
				// } else if (rack != null && shelf != null && slot == null) {
			} else if (equipmentHolder.getHolderType().equals("shelf")) {
				shelfs.add(equipmentHolder);
			}else if (equipmentHolder.getHolderType().equals("sub_shelf")) {
				subshelfs.add(equipmentHolder);
			}

			else if (equipmentHolder.getHolderType().equals("slot")) {
				slots.add(equipmentHolder);
			} else if (equipmentHolder.getHolderType().equals("sub_slot")) {
				subslots.add(equipmentHolder);
			}
		}
		// ////////////////// 将EH分类///////////////////

		for (EquipmentHolder equipmentHolder : racks) {
			CdcpObject cEquipmentHolder = transEquipmentHolder(equipmentHolder);
			di.insert(cEquipmentHolder);
		}
		for (EquipmentHolder equipmentHolder : subshelfs) {
			CdcpObject cEquipmentHolder = transEquipmentHolder(equipmentHolder);
			di.insert(cEquipmentHolder);
		}
		for (EquipmentHolder equipmentHolder : shelfs) {
			CdcpObject cEquipmentHolder = transEquipmentHolder(equipmentHolder);
			di.insert(cEquipmentHolder);
		}

		for (EquipmentHolder equipmentHolder : slots) {
			CdcpObject cEquipmentHolder = transEquipmentHolder(equipmentHolder);
			di.insert(cEquipmentHolder);
		}
		for (EquipmentHolder equipmentHolder : subslots) {
			CdcpObject cEquipmentHolder = transEquipmentHolder(equipmentHolder);
			di.insert(cEquipmentHolder);
		}
		di.end();

	}

	protected boolean notEmpty(String str) {
		return str != null && !str.trim().isEmpty();
	}

	protected void migratePTP() throws Exception {
		if (!isTableHasData(PTP.class))
			return;
		executeDelete("delete  from CPTP c where c.emsName = '" + emsdn + "'", CPTP.class);
		executeDelete("delete from CIPAddress c where c.emsName = '" + emsdn + "'", CIPAddress.class);
		List<PTP> ptps = sd.queryAll(PTP.class);
		insertPtps(ptps);
	}

	protected void migrateCTP(String deviceDn, List<CTP> ctps) throws Exception {
		if (ctps == null || ctps.isEmpty()) {
			getLogger().error("ctp is empty !");
			return;
		}
		executeDelete("delete from CCTP c where c.portdn in  (select d.dn from CPTP d where d.dn like '" + deviceDn + "@%') ", CPTP.class);
		insertCtps(ctps);
	}

	protected void migrateCTP() throws Exception {
	//	executeDelete("delete  from CCTP c where c.emsName = '" + emsdn + "'", CCTP.class);

		List<CTP> ctps = sd.queryAll(CTP.class);
		if (isEmptyResult(ctps))
			return;

		executeTableDelete("C_CTP",emsdn);
		insertCtps(ctps);
	}
	
	protected void migrate201PtpForFHSDH() throws Exception {
		if (!isTableHasData(PTP.class)) {
			getLogger().error("ptp data is empty, cannot migrate 201ptp !");
			return;
		}
		List<PTP> ptps = sd.queryAll(PTP.class);
		if (isEmptyResult(ptps)) {
			getLogger().error("ptp is empty, cannot migrate 201ptp !");
			return;
		}
		
		insert201Ptps(ptps);
	}

	protected List insertCtps(List<CTP> ctps) throws Exception {
		DataInserter di = new DataInserter(emsid);
		getLogger().info("migrateCtp size = " + (ctps == null ? null : ctps.size()));
		List<CCTP> cctps = new ArrayList<CCTP>();
		if (ctps != null && ctps.size() > 0) {
			for (CTP ctp : ctps) {
				CCTP cctp = transCTP(ctp);
				if (cctp != null) {
					cctps.add(cctp);
					if (cctp.getPortdn() == null || cctp.getPortdn().trim().isEmpty())
						System.out.println("cctp = " + cctp.getDn());
					if (cctp.getDn().equals("EMS:QUZ-T2000-3-P@ManagedElement:590467@PTP:/rack=1/shelf=1/slot=2/domain=sdh/port=1@CTP:/sts3c_au4-j=2"))
						System.out.println("cctp = " + cctp);
					di.insert(cctp);
				}
			}
		}

		di.end();
        return cctps;
	}

	protected void migratePTP(String deviceDn, List<PTP> ptps) throws Exception {
		executeDelete("delete from CPTP c where c.dn  like '" + deviceDn + "@%'", CPTP.class);
		insertPtps(ptps);
	}

	private void insertPtps(List<PTP> ptps) throws Exception {
		DataInserter di = new DataInserter(emsid);
		getLogger().info("migratePtp size = " + (ptps == null ? null : ptps.size()));
		List<CPTP> cptps = new ArrayList<CPTP>();
		if (ptps != null && ptps.size() > 0) {
			for (PTP ptp : ptps) {
				CPTP cptp = transPTP(ptp);
				if (cptp != null) {
					cptps.add(cptp);
				}
			}
		}

		this.removeDuplicateDN(cptps);
		for (int i = 0; i < cptps.size(); i++) {
			CPTP cptp = cptps.get(i);
			di.insert(cptp);
			if (cptp.getIpAddress() != null && !cptp.getIpAddress().isEmpty()) {
				CIPAddress ip = new CIPAddress();
				ip.setDn(SysUtil.nextDN());
				ip.setSid(DatabaseUtil.nextSID(ip));
				ip.setEmsName(emsdn);
				ip.setEmsid(emsid);
				ip.setIpaddress(cptp.getIpAddress());
				ip.setPtpId(DatabaseUtil.getSID(CPTP.class, cptp.getDn()));
				di.insert(ip);
			}
		}
		di.end();

	}
	
	private List<CCTP> insert201Ptps(List<PTP> ptps) throws Exception {
		DataInserter di = new DataInserter(emsid);
		getLogger().info("migrate201PtpForFHSdh Start...");
		List<CCTP> cctps = new ArrayList<CCTP>();
//		List<PTP> allCtps = sd.queryAll(CTP.class);
//		getLogger().info("allCtps size =  " + allCtps==null?0:allCtps.size());
		if (ptps != null && ptps.size() > 0) {
			for (PTP ptp : ptps) {
				if (ptp.getDn().endsWith("/port=201")) { // 201端口
					getLogger().info("migrate201Ptp：" + ptp.getDn());
					cctps = createCtpBy201Ptp(ptp);
					getLogger().info("delete from CCTP c where c.dn  like '" + ptp.getDn() + "@%'");
					executeDelete("delete from CCTP c where c.dn  like '" + ptp.getDn() + "@%'", CCTP.class);
					if (Detect.notEmpty(cctps)) {
						getLogger().info("Ctp size："+cctps.size());
						di.insert(cctps);
					}
				}
			}
		}
		
		getLogger().info("migrate201PtpForFHSdh End...");
		di.end();
		return cctps;
	}
	
	public List<CCTP> createCtpBy201Ptp(PTP ptp) {
		List<CCTP> cctps = new ArrayList<>();
		
		CCTP ctpVc4 = createVc4(ptp);
		cctps.add(ctpVc4);
		
		for (int k=1;k<=3;k++) {
			for (int l=1;l<=7;l++) {
				for (int m=1;m<=3;m++) {
					CCTP ctpVc12 = createVc12(ctpVc4, k, l, m);
					cctps.add(ctpVc12);
				}
			}
		}
		
		return cctps;
	}
	
	private CCTP createVc4(PTP ptp) {
		CCTP cctp = new CCTP();
		String dn = ptp.getDn() + "@CTP:/sts3c_au4-j=1";
		cctp.setDn(dn);

		cctp.setPortdn(ptp.getDn());
		cctp.setParentCtpdn("");
		cctp.setSid(DatabaseUtil.nextSID(cctp));
		cctp.setCollectTimepoint(ptp.getCreateDate());
		cctp.setEdgePoint(false);
		cctp.setType(ptp.getType());
		cctp.setConnectionState(ptp.getConnectionState());
		cctp.setTpMappingMode(ptp.getTpMappingMode());
		cctp.setDirection(3);
		cctp.setTransmissionParams(ptp.getTransmissionParams());
		cctp.setRate("15");

        cctp.setRateDesc("VC4");

        cctp.setTmRate("155M");
        cctp.setJ("1");
        cctp.setK("-");
        cctp.setL("-");
        cctp.setM("-");

		cctp.setTpProtectionAssociation(ptp.getTpProtectionAssociation());
		cctp.setParentDn(ptp.getDn());
		cctp.setEmsName(emsdn);
		cctp.setEmsid(emsid);
		cctp.setUserLabel("VC4-1");
		cctp.setNativeEMSName("VC4-1");
		cctp.setOwner(ptp.getOwner());
		cctp.setAdditionalInfo(ptp.getAdditionalInfo());
		
		return cctp;
	}
	
	private CCTP createVc12(CCTP ctpVc4, int k, int l, int m) {
		CCTP cctp = new CCTP();
		String dn = ctpVc4.getDn() + "/tu3_vc3-k=" + k + "/vt2_tu12-l=" + l + "-m=" + m;
		int num = (k - 1) * 21 + (l - 1) * 3 + m;
		cctp.setDn(dn);

		cctp.setParentCtpdn(ctpVc4.getDn());
		cctp.setPortdn(ctpVc4.getParentDn());
		cctp.setParentDn(ctpVc4.getParentDn());
		cctp.setSid(DatabaseUtil.nextSID(cctp));
		cctp.setCollectTimepoint(ctpVc4.getCreateDate());
		cctp.setEdgePoint(false);
		cctp.setType(ctpVc4.getType());
		cctp.setConnectionState(ctpVc4.getConnectionState());
		cctp.setTpMappingMode(ctpVc4.getTpMappingMode());
		cctp.setDirection(3);
		cctp.setTransmissionParams(ctpVc4.getTransmissionParams());
		cctp.setRate("11");

        cctp.setRateDesc("VC12");

        cctp.setTmRate("2M");
        cctp.setJ("1");
        cctp.setK(String.valueOf(k));
        cctp.setL(String.valueOf(l));
        cctp.setM(String.valueOf(m));

		cctp.setTpProtectionAssociation(ctpVc4.getTpProtectionAssociation());
		cctp.setEmsName(emsdn);
		cctp.setEmsid(emsid);
		cctp.setUserLabel("VC12-" + num);
		cctp.setNativeEMSName("VC12-" + num);
		cctp.setOwner(ctpVc4.getOwner());
		cctp.setAdditionalInfo(ctpVc4.getAdditionalInfo());
		
		return cctp;
	}

    protected void errorLog(String info) {
        getLogger().error(info);
    }



    protected void putIntoList(HashMap map ,String key,String value) {
        List list = (List) map.get(key);
        if (list == null) {
            list = new ArrayList();
            map.put(key,list);
        }
        list.add(value);
    }

	protected void migrateMPCTP() throws Exception {
		if (!isTableHasData(HW_MSTPBindingPath.class))
			return;
		executeDelete("delete  from CMP_CTP c where c.emsName = '" + emsdn + "'", CMP_CTP.class);
		List<HW_MSTPBindingPath> bps = sd.queryAll(HW_MSTPBindingPath.class);

		DataInserter di = new DataInserter(emsid);
		getLogger().info("migrateMPCTP size = " + (bps == null ? null : bps.size()));
		List<CMP_CTP> cctps = new ArrayList<CMP_CTP>();
		if (bps != null && bps.size() > 0) {
			for (HW_MSTPBindingPath path : bps) {
				String direction = path.getDirection();
				if ("D_BIDIRECTIONAL".equals(direction) || "D_SOURCE".equals(direction)) {
					String[] ctps = path.getAllPathList().split(Constant.listSplitReg);
					for (String ctp : ctps) {
						CMP_CTP cctp = transMP_CTP(path.getParentDn(), ctp);
						if (cctp != null) {
							cctps.add(cctp);
						}
					}
				}
			}
		}
		di.end();
	}

	private CMP_CTP transMP_CTP(String ptp, String ctp) {
		CMP_CTP cftp_ptp = new CMP_CTP();
		cftp_ptp.setDn(ptp + "<>" + ctp);
		cftp_ptp.setEmsName(emsdn);
		cftp_ptp.setCtpDn(ctp);
		cftp_ptp.setCtpId(DatabaseUtil.getSID(CCTP.class, ctp));
		cftp_ptp.setPtpDn(ptp);
		cftp_ptp.setPtpId(DatabaseUtil.getSID(CPTP.class, ptp));
		return cftp_ptp;
	}
	public void migrateNESection(List<Section> sections,String neDn) throws Exception {
		executeDelete("delete  from CSection c where c.emsName = '" + emsdn + "' and ( c.aendTp like '"+neDn+"@%' or c.zendTp like '"+neDn+"@%' )", CSection.class);
		DataInserter di = new DataInserter(emsid);

		if (sections != null && sections.size() > 0) {
			for (Section section : sections) {
				CSection csection = transSection(section);
				csection.setSid(DatabaseUtil.nextSID(csection));
				// csection.setSid(toSid(Long.parseLong(section.getDn().substring(section.getDn().lastIndexOf(" - ") + 3))));
				String aendtp = csection.getAendTp();
				String zendtp = csection.getZendTp();
				if (aendtp.contains("CTP") || zendtp.contains("CTP")) {
					continue;
				}
				csection.setAptpId(DatabaseUtil.getSID(CPTP.class, aendtp));
				csection.setZptpId(DatabaseUtil.getSID(CPTP.class, zendtp));
				di.updateByDn(csection);
				getLogger().info("update Section : "+csection.getDn());
			}
		}
		di.end();
	}
	protected void migrateSection() throws Exception {
		if (!isTableHasData(Section.class))
			return;
		executeDelete("delete  from CSection c where c.emsName = '" + emsdn + "'", CSection.class);
		DataInserter di = new DataInserter(emsid);
		List<Section> sections = sd.queryAll(Section.class);
		if (sections != null && sections.size() > 0) {
			for (Section section : sections) {
				CSection csection = transSection(section);
				csection.setSid(DatabaseUtil.nextSID(csection));
				// csection.setSid(toSid(Long.parseLong(section.getDn().substring(section.getDn().lastIndexOf(" - ") + 3))));
				String aendtp = csection.getAendTp();
				String zendtp = csection.getZendTp();
				if (aendtp.contains("CTP") || zendtp.contains("CTP")) {
					continue;
				}
				csection.setAptpId(DatabaseUtil.getSID(CPTP.class, aendtp));
				csection.setZptpId(DatabaseUtil.getSID(CPTP.class, zendtp));
				di.insert(csection);
			}
		}
		di.end();
	}

	protected void migrateFTPPTP() throws Exception {
		if (!isTableHasData(CFTP_PTP.class))
			return;
		executeDelete("delete from CFTP_PTP c where c.emsName = '" + emsdn + "'", CFTP_PTP.class);
		List<R_FTP_PTP> list = sd.queryAll(R_FTP_PTP.class);
		DataInserter di = new DataInserter(emsid);
		for (int i = 0; i < list.size(); i++) {
			R_FTP_PTP r_ftp_ptp = list.get(i);
			CFTP_PTP cftp_ptp = transFTP_PTP(emsdn, r_ftp_ptp);
			di.insert(cftp_ptp);
		}
		di.end();

	}

	protected void migrateIPRoute() throws Exception {
		if (!isTableHasData(R_TrafficTrunk_CC_Section.class))
			return;
		// //////////////////// migrate route //////////////////////////////
		executeDelete("delete  from CTunnel_Section c where c.emsName = '" + emsdn + "'", CTunnel_Section.class);
		// executeDelete("delete from CRoute c where c.emsName = '" + emsdn + "'", CIPRoute.class);
		DataInserter di = new DataInserter(emsid);
		int from = 0;
		int limit = 10000;
		while (true) {
			List<R_TrafficTrunk_CC_Section> rtcs = sd.query("select c from R_TrafficTrunk_CC_Section c", from, limit);
			if (from == 0 && (rtcs == null || rtcs.isEmpty())) {
				// throw new EMSDataTableEmptyException("Table empty : R_TrafficTrunk_CC_Section");
				return;
			}
			if (rtcs.isEmpty())
				break;
			from = from + rtcs.size();
			getLogger().info("Migrate route " + from);
			long t1 = System.currentTimeMillis();
			for (int i = 0; i < rtcs.size(); i++) {
				R_TrafficTrunk_CC_Section r_trafficTrunk_cc_section = rtcs.get(i);
				String trafficTrunDn = r_trafficTrunk_cc_section.getTrafficTrunDn();
				// 资管没有用到这部分数据
				// CIPRoute cRoute = new CIPRoute();
				// cRoute.setEmsName(emsdn);
				// // cRoute.setDn(SysUtil.nextDN());
				// cRoute.setTunnelDn(trafficTrunDn);
				// cRoute.setTunnelId(DatabaseUtil.getSID(CTunnel.class, trafficTrunDn));
				// cRoute.setEntityType(r_trafficTrunk_cc_section.getType());
				// if (r_trafficTrunk_cc_section.getCcOrSectionDn() != null && !r_trafficTrunk_cc_section.getCcOrSectionDn().isEmpty()) {
				// cRoute.setEntityDn(r_trafficTrunk_cc_section.getCcOrSectionDn());
				// }
				// cRoute.setAptp(r_trafficTrunk_cc_section.getaPtp());
				// cRoute.setZptp(r_trafficTrunk_cc_section.getzPtp());
				//
				// cRoute.setAptpId(DatabaseUtil.getSID(CPTP.class, cRoute.getAptp()));
				// cRoute.setZptpId(DatabaseUtil.getSID(CPTP.class, cRoute.getZptp()));
				// cRoute.setAend(r_trafficTrunk_cc_section.getaEnd());
				// cRoute.setZend(r_trafficTrunk_cc_section.getzEnd());
				// cRoute.setCollectTimepoint(r_trafficTrunk_cc_section.getCreateDate());
				//
				// cRoute.setDn(cRoute.getTunnelDn() + "_" + cRoute.getEntityDn());
				// di.insert(cRoute);

				if (r_trafficTrunk_cc_section.getType().equals("SECTION")) {
					CTunnel_Section ts = new CTunnel_Section();
					ts.setDn(trafficTrunDn + "<>" + r_trafficTrunk_cc_section.getCcOrSectionDn());
					ts.setEmsName(emsdn);
					ts.setTunnelDn(trafficTrunDn);
					ts.setTunnelId(DatabaseUtil.getSID(CTunnel.class, trafficTrunDn));
					ts.setSectionDn(r_trafficTrunk_cc_section.getCcOrSectionDn());
					ts.setSectionId(DatabaseUtil.getSID(CSection.class, r_trafficTrunk_cc_section.getCcOrSectionDn()));
					if (!DatabaseUtil.isSIDExisted(CTunnel_Section.class, ts.getDn())) // section+tunnel 可能有重复
						di.insert(ts);
				}

			}
			rtcs.clear();
		}
		di.end();

	}

	public CEquipment transEquipment(Equipment equipment) {
		CEquipment cEquipment = new CEquipment();
		cEquipment.setCollectTimepoint(equipment.getCreateDate());
		cEquipment.setDn(equipment.getDn());
		if (cEquipment.getDn().contains("@")) {
			String slotdn = cEquipment.getDn().substring(0, cEquipment.getDn().lastIndexOf("@"));
			cEquipment.setSlotDn(slotdn);
			cEquipment.setParentDn(slotdn);
			cEquipment.setSlotId(DatabaseUtil.getSID(CSlot.class, slotdn));
		}
		cEquipment.setAdditionalInfo(equipment.getAdditionalInfo());
		cEquipment.setEmsName(equipment.getEmsName());
		cEquipment.setExpectedEquipmentObjectType(equipment.getExpectedEquipmentObjectType());
		cEquipment.setInstalledEquipmentObjectType(equipment.getInstalledEquipmentObjectType());
		cEquipment.setInstalledPartNumber(equipment.getInstalledPartNumber());
		cEquipment.setInstalledSerialNumber(equipment.getInstalledSerialNumber());
		cEquipment.setInstalledVersion(equipment.getInstalledVersion());
		cEquipment.setNativeEMSName(equipment.getNativeEMSName());
		cEquipment.setOwner(equipment.getOwner());
		// if (equipment.getParentDn() == null)
		// cEquipment.setParentDn(equipment.getParentDn());
		cEquipment.setServiceState(equipment.getServiceState());
		cEquipment.setUserLabel(equipment.getUserLabel());
		
		HashMap<String, String> additionalInfoMap = MigrateUtil.transMapValue(equipment.getAdditionalInfo());
		if (additionalInfoMap != null) {
			if (Detect.notEmpty(additionalInfoMap.get("installedSerialNumber"))) {
				cEquipment.setInstalledSerialNumber2(additionalInfoMap.get("installedSerialNumber"));
			}
			if (Detect.notEmpty(additionalInfoMap.get("installedPartNumber"))) {
				cEquipment.setInstalledPartNumber2(additionalInfoMap.get("installedPartNumber"));
			}
		}
		if (!Detect.notEmpty(cEquipment.getInstalledSerialNumber2())) {
			cEquipment.setInstalledSerialNumber2(equipment.getInstalledSerialNumber());
		}

		return cEquipment; // To change body of created methods use File | Settings | File Templates.
	}

	public CdcpObject transEquipmentHolder(EquipmentHolder equipmentHolder) {
		// CEquipmentHolder cequipmentHolder = new CEquipmentHolder();
		// cequipmentHolder.setDn(equipmentHolder.getDn());
		// cequipmentHolder.setCollectTimepoint(equipmentHolder.getCreateDate());
		// cequipmentHolder.setHolderType(equipmentHolder.getHolderType());
		// cequipmentHolder.setExpectedOrInstalledEquipment(equipmentHolder.getExpectedOrInstalledEquipment());
		// cequipmentHolder.setAcceptableEquipmentTypeList(equipmentHolder.getAcceptableEquipmentTypeList());
		// cequipmentHolder.setHolderState(equipmentHolder.getHolderState());
		// cequipmentHolder.setParentDn(equipmentHolder.getParentDn());
		// cequipmentHolder.setEmsName(equipmentHolder.getEmsName());
		// cequipmentHolder.setUserLabel(equipmentHolder.getUserLabel());
		// cequipmentHolder.setNativeEMSName(equipmentHolder.getNativeEMSName());
		// cequipmentHolder.setOwner(equipmentHolder.getOwner());
		// cequipmentHolder.setAdditionalInfo(equipmentHolder.getAdditionalInfo());

		String dn = equipmentHolder.getDn();
		// String rack = CodeTool.extractValue(dn, "rack");
		// String shelf = CodeTool.extractValue(dn, "shelf");
		// String slot = CodeTool.extractValue(dn, "slot");
		// String subSlot = CodeTool.extractValue(dn, "sub_slot");
		String no = dn.substring(dn.lastIndexOf("=") + 1);

		// if (rack != null && shelf == null) {
		if (equipmentHolder.getHolderType().equals("rack")) {
			CRack cequipmentHolder = new CRack();
			cequipmentHolder.setCdeviceId(DatabaseUtil.getSID(CDevice.class,
					equipmentHolder.getDn().substring(0, equipmentHolder.getDn().indexOf("@EquipmentHolder"))));
			cequipmentHolder.setDn(equipmentHolder.getDn());
			cequipmentHolder.setSid(DatabaseUtil.nextSID(cequipmentHolder));
			// cequipmentHolder.setNo(rack);
			cequipmentHolder.setNo(no);
			cequipmentHolder.setCollectTimepoint(equipmentHolder.getCreateDate());
			cequipmentHolder.setHolderType(equipmentHolder.getHolderType());
			cequipmentHolder.setExpectedOrInstalledEquipment(equipmentHolder.getExpectedOrInstalledEquipment());
			cequipmentHolder.setAcceptableEquipmentTypeList(equipmentHolder.getAcceptableEquipmentTypeList());
			cequipmentHolder.setHolderState(equipmentHolder.getHolderState());
			cequipmentHolder.setParentDn(equipmentHolder.getParentDn());
			cequipmentHolder.setEmsName(equipmentHolder.getEmsName());
			cequipmentHolder.setUserLabel(equipmentHolder.getUserLabel());
			cequipmentHolder.setNativeEMSName(equipmentHolder.getNativeEMSName());
			cequipmentHolder.setOwner(equipmentHolder.getOwner());
			cequipmentHolder.setAdditionalInfo(equipmentHolder.getAdditionalInfo());

			return cequipmentHolder;
			// } else if (rack != null && shelf != null && slot == null) {
		}  else if (equipmentHolder.getHolderType().equals("shelf") || equipmentHolder.getHolderType().equals("sub_shelf")) {
			CShelf cequipmentHolder = new CShelf();
			if (dn.contains("/shelf")) {
				cequipmentHolder.setRackDn(dn.substring(0, dn.indexOf("/shelf")));
				cequipmentHolder.setRackId(DatabaseUtil.getSID(CRack.class, cequipmentHolder.getRackDn()));
			}

			if (equipmentHolder.getHolderType().equals("sub_shelf")) {
				cequipmentHolder.setTag1(dn.substring(0,dn.indexOf("/sub_shelf=")));
			}

			// cequipmentHolder.setNo(shelf);
			cequipmentHolder.setNo(no);
			cequipmentHolder.setDn(equipmentHolder.getDn());
			cequipmentHolder.setSid(DatabaseUtil.nextSID(cequipmentHolder));

			cequipmentHolder.setCollectTimepoint(equipmentHolder.getCreateDate());
			cequipmentHolder.setHolderType(equipmentHolder.getHolderType());
			cequipmentHolder.setExpectedOrInstalledEquipment(equipmentHolder.getExpectedOrInstalledEquipment());
			cequipmentHolder.setAcceptableEquipmentTypeList(equipmentHolder.getAcceptableEquipmentTypeList());
			cequipmentHolder.setHolderState(equipmentHolder.getHolderState());
			cequipmentHolder.setParentDn(equipmentHolder.getParentDn());
			cequipmentHolder.setEmsName(equipmentHolder.getEmsName());
			cequipmentHolder.setUserLabel(equipmentHolder.getUserLabel());
			cequipmentHolder.setNativeEMSName(equipmentHolder.getNativeEMSName());
			cequipmentHolder.setOwner(equipmentHolder.getOwner());
			cequipmentHolder.setAdditionalInfo(equipmentHolder.getAdditionalInfo());
			cequipmentHolder.setShelfType(getShelfType(equipmentHolder.getParentDn(), equipmentHolder.getAdditionalInfo()));
			return cequipmentHolder;
			// } else if (rack != null && shelf != null && slot != null) {
		} else if (equipmentHolder.getHolderType().equals("slot") || equipmentHolder.getHolderType().equals("sub_slot")) {
			CSlot cequipmentHolder = new CSlot();
			if (dn.contains("/slot")) {
				cequipmentHolder.setShelfDn(dn.substring(0, dn.indexOf("/slot")));
				cequipmentHolder.setShelfId(DatabaseUtil.getSID(CShelf.class, cequipmentHolder.getShelfDn()));

			}
			// cequipmentHolder.setNo(slot);
			cequipmentHolder.setNo(no);
			// if (subSlot != null) {
			if (equipmentHolder.getHolderType().equals("sub_slot")) {
				// cequipmentHolder.setParentSlotDn(dn.substring(0, dn.indexOf("/sub_slot")));
				// cequipmentHolder.setParentSlotDn(dn.substring(0, dn.indexOf("/sub")));
				// cequipmentHolder.setParentSlotId(DatabaseUtil.getSID(CSlot.class, cequipmentHolder.getParentSlotDn()));
				// cequipmentHolder.setNo(subSlot);
				String cardDn = dn.substring(0, dn.indexOf("/sub")) + "@Equipment:1";
				cequipmentHolder.setCardDn(cardDn);
			}

			cequipmentHolder.setDn(equipmentHolder.getDn());
			cequipmentHolder.setSid(DatabaseUtil.nextSID(cequipmentHolder));

			cequipmentHolder.setCollectTimepoint(equipmentHolder.getCreateDate());
			cequipmentHolder.setHolderType(equipmentHolder.getHolderType());
			cequipmentHolder.setExpectedOrInstalledEquipment(equipmentHolder.getExpectedOrInstalledEquipment());
			cequipmentHolder.setAcceptableEquipmentTypeList(equipmentHolder.getAcceptableEquipmentTypeList());

			if (cequipmentHolder.getAcceptableEquipmentTypeList() != null && cequipmentHolder.getAcceptableEquipmentTypeList().length() > 1500)
				cequipmentHolder.setAcceptableEquipmentTypeList("");

			cequipmentHolder.setHolderState(equipmentHolder.getHolderState());
			cequipmentHolder.setParentDn(equipmentHolder.getParentDn());
			cequipmentHolder.setEmsName(equipmentHolder.getEmsName());
			cequipmentHolder.setUserLabel(equipmentHolder.getUserLabel());
			cequipmentHolder.setNativeEMSName(equipmentHolder.getNativeEMSName());
			cequipmentHolder.setOwner(equipmentHolder.getOwner());
			cequipmentHolder.setAdditionalInfo(equipmentHolder.getAdditionalInfo());
			return cequipmentHolder;
		}
		return null;
	}

	protected String getShelfType(String neDN, String additionalInfo) {
		if (!EmsJob.JOB_TYPE_SYNC_DEVICE.equals(JOB_TYPE)) {
			if (shelfTypeMap == null) {
				shelfTypeMap = new HashMap<String, String>();
				getLogger().info("shelfTypeMap: ");
				List<ManagedElement> nes = sd.queryAll(ManagedElement.class);
				if (nes != null && nes.size() > 0) {
					for (ManagedElement ne : nes) {
						shelfTypeMap.put(ne.getDn(), ne.getProductName());
					}
				}
			}
			return shelfTypeMap.get(neDN);
		}
		return shelfType;
	}

	public CDevice transDevice(ManagedElement me) {
		CDevice device = new CDevice();
		device.setEmsName(me.getEmsName());
		device.setDn(me.getDn());
		device.setLocation(me.getLocation());
		device.setNativeEmsName(me.getNativeEMSName());
		device.setNeVersion(me.getNeVersion());
		device.setProductName(me.getProductName());
		device.setSupportedRates(me.getSupportedRates());
		device.setUserLabel(me.getUserLabel());
		device.setCollectTimepoint(me.getCreateDate());
		device.setAdditionalInfo(me.getAdditionalInfo());

		String additionalInfo = me.getAdditionalInfo();
		HashMap<String, String> additionalInfoMap = MigrateUtil.transMapValue(additionalInfo);
		if (additionalInfoMap != null) {
			device.setIpAddress(additionalInfoMap.get("LSRID"));
			device.setMaxTransferRate(additionalInfoMap.get("MaxTransferRate"));
		}
		// huawei
		if (additionalInfoMap != null && additionalInfoMap.containsKey("IPAddress")) {
			device.setIpAddress(additionalInfoMap.get("IPAddress"));
		}
		// alu
		if (additionalInfoMap != null && additionalInfoMap.containsKey("ipAddress")) {
			device.setIpAddress(additionalInfoMap.get("ipAddress"));
		}
		// fenghuo
		if (additionalInfoMap != null && additionalInfoMap.containsKey("ObjectIP")) {
			device.setIpAddress(additionalInfoMap.get("ObjectIP"));
		}
		//zte
		if (additionalInfoMap != null && additionalInfoMap.containsKey("RouteId:/ip")) {
			device.setIpAddress(additionalInfoMap.get("RouteId:/ip"));
		}
		if (additionalInfoMap != null && additionalInfoMap.containsKey("RouteId")) {
			String routeId = additionalInfoMap.get("RouteId");
			if (routeId.contains("ip=")) {
				device.setIpAddress(routeId.substring(routeId.indexOf("ip=")+3));
			}
		}
		return device;
	}

	public CPTP transPTP(PTP ptp) {
		CPTP cptp = new CPTP();
		cptp.setDn(ptp.getDn());
		if (cptp.getDn().contains("/domain")) {
			String dn = cptp.getDn();
			String me = dn.substring(0, dn.lastIndexOf("@"));
			String slot = dn.substring(dn.indexOf("/rack"), dn.indexOf("/domain"));

			String carddn = me + "@EquipmentHolder:" + slot + "@Equipment:1";
			cptp.setCardid(DatabaseUtil.getSID(CEquipment.class, carddn));
		}
		cptp.setSid(DatabaseUtil.nextSID(cptp));
		cptp.setCollectTimepoint(ptp.getCreateDate());
		cptp.setEdgePoint(ptp.isEdgePoint());
		cptp.setType(ptp.getType());
		cptp.setConnectionState(ptp.getConnectionState());
		cptp.setTpMappingMode(ptp.getTpMappingMode());
		cptp.setDirection(DicUtil.getPtpDirection(ptp.getDirection()));
		cptp.setTransmissionParams(ptp.getTransmissionParams());
		cptp.setRate(ptp.getRate());
		cptp.setTpProtectionAssociation(ptp.getTpProtectionAssociation());
		// cptp.setParentDn(ptp.getParentDn());
		cptp.setEmsName(ptp.getEmsName());
		cptp.setUserLabel(ptp.getUserLabel());
		cptp.setNativeEMSName(ptp.getNativeEMSName());
		cptp.setOwner(ptp.getOwner());
		cptp.setAdditionalInfo(ptp.getAdditionalInfo());

		String temp = cptp.getDn();
		if (temp.startsWith("EMS:"))
			temp = temp.substring(4);
		if (temp.contains("@PTP"))
			temp = temp.substring(0, temp.indexOf("@PTP"));
		if (temp.contains("@FTP"))
			temp = temp.substring(0, temp.indexOf("@FTP"));
		temp = temp.replaceAll("ManagedElement:", "");

		cptp.setDeviceDn(temp);
		cptp.setParentDn(temp);

		Map<String, String> map = MigrateUtil.transMapValue(ptp.getTransmissionParams());
		cptp.setPortMode(map.get("PortMode"));
		cptp.setPortRate(map.get("PortRate"));
		cptp.setWorkingMode(map.get("WorkingMode"));
		cptp.setMacAddress(map.get("MACAddress"));
		cptp.setIpAddress(map.get("IPAddress"));
		cptp.setIpMask(map.get("IPMask"));

		return cptp; // To change body of created methods use File | Settings | File Templates.
	}

	public CCTP 	transCTP(CTP ctp) {
		CCTP cctp = new CCTP();
		cctp.setDn(ctp.getDn());

		cctp.setPortdn(ctp.getPortdn());
		cctp.setParentCtpdn(ctp.getParentCtpdn());
		cctp.setSid(DatabaseUtil.nextSID(cctp));
		cctp.setCollectTimepoint(ctp.getCreateDate());
		cctp.setEdgePoint(ctp.isEdgePoint());
		cctp.setType(ctp.getType());
		cctp.setConnectionState(ctp.getConnectionState());
		cctp.setTpMappingMode(ctp.getTpMappingMode());
		cctp.setDirection(DicUtil.getPtpDirection(ctp.getDirection()));
		cctp.setTransmissionParams(ctp.getTransmissionParams());
        if (ctp.getTransmissionParams() != null && ctp.getTransmissionParams().contains("@"))
            ctp.setRate(cctp.getTransmissionParams().substring(0,ctp.getTransmissionParams().indexOf("@")));
		cctp.setRate(ctp.getRate());

        cctp.setRateDesc(SDHUtil.rateDesc(ctp.getRate()));

        if (cctp.getRateDesc() == null || cctp.getRateDesc().isEmpty()) {
		//getLogger().error("RateDesc is null : rate=" + ctp.getRate());
		}
        cctp.setTmRate(SDHUtil.getTMRate(ctp.getRate()));
        if (cctp.getTmRate() == null || cctp.getTmRate().isEmpty()) {
		//	getLogger().error("getTmRate is null : rate=" + ctp.getRate());
		}
        SDHUtil.setCTPNumber(cctp);


		cctp.setTpProtectionAssociation(ctp.getTpProtectionAssociation());
		cctp.setParentDn(ctp.getParentDn());
		cctp.setEmsName(emsdn);
		cctp.setUserLabel(ctp.getUserLabel());
		cctp.setNativeEMSName(ctp.getNativeEMSName());
		cctp.setOwner(ctp.getOwner());
		cctp.setAdditionalInfo(ctp.getAdditionalInfo());

		return cctp;
	}

	public CTunnel transTunnel(TrafficTrunk src) {
		CTunnel des = new CTunnel();
		des.setDn(src.getDn());
		des.setCollectTimepoint(src.getCreateDate());
		des.setRerouteAllowed(src.getRerouteAllowed());
		des.setAdministrativeState(src.getAdministrativeState());
		des.setActiveState(src.getActiveState());
		des.setDirection(DicUtil.getConnectionDirection(src.getDirection()));
		des.setTransmissionParams(src.getTransmissionParams());
		des.setNetworkRouted(src.getNetworkRouted());
		des.setAend(src.getaEnd());
		des.setZend(src.getzEnd());
		des.setAptp(src.getaPtp());
		des.setZptp(src.getzPtp());
		des.setAendTrans(src.getaEndTrans());
		des.setZendtrans(src.getzEndtrans());
		des.setParentDn(src.getParentDn());
		des.setEmsName(src.getEmsName());
		des.setUserLabel(src.getUserLabel());
		des.setNativeEMSName(src.getNativeEMSName());
		des.setOwner(src.getOwner());
		des.setAdditionalInfo(src.getAdditionalInfo());
		return des;
	}

	public CPWE3 transFDF(FlowDomainFragment src) {
		CPWE3 des = new CPWE3();
		des.setDn(src.getDn());
		des.setCollectTimepoint(src.getCreateDate());
		des.setFlexible(src.isFlexible());
		des.setNetworkAccessDomain(src.getNetworkAccessDomain());
		des.setAdministrativeState(src.getAdministrativeState());
		des.setFdfrState(src.getFdfrState());
		des.setMultipointServiceAttrParaList(src.getMultipointServiceAttrParaList());
		des.setMultipointServiceAttrMacList(src.getMultipointServiceAttrMacList());
		des.setMultipointServiceAttrAddInfo(src.getMultipointServiceAttrAddInfo());
		des.setDirection(DicUtil.getConnectionDirection(src.getDirection()));
		des.setTransmissionParams(src.getTransmissionParams());
		des.setRate(src.getRate());
		des.setFdfrType(src.getFdfrType());
		des.setZx_aend(src.getaEnd());
		des.setZx_zend(src.getzEnd());
		des.setAptp(src.getaPtp());
		des.setAptpId(DatabaseUtil.getSID(CPTP.class, des.getAptp()));
		des.setZptp(src.getzPtp());
		des.setZptpId(DatabaseUtil.getSID(CPTP.class, des.getZptp()));

		des.setAendTrans(src.getaEndTrans());
		des.setZendtrans(src.getzEndtrans());
		des.setParentDn(src.getParentDn());
		des.setEmsName(src.getEmsName());
		des.setUserLabel(src.getUserLabel());
		des.setNativeEMSName(src.getNativeEMSName());
		des.setOwner(src.getOwner());
		des.setAdditionalInfo(src.getAdditionalInfo());

		return des;
	}

    private HashSet unkownRates = new HashSet();

	public CSection transSection(Section section) {
		CSection csection = new CSection();
		csection.setDn(section.getDn());
		csection.setSid(DatabaseUtil.nextSID(csection));
		csection.setCollectTimepoint(section.getCreateDate());
		csection.setRate(section.getRate());
		String rate = section.getRate();
		if (rate != null) {
			int r = 0;
			try {
				r = Integer.parseInt(rate);
			} catch (NumberFormatException e) {
				LogUtil.error(getClass(), "Unknown rate :" + rate);
			}
			rate = DicUtil.getSpeedByRate(r);
            if (rate == null)  {
                if (!unkownRates.contains(section.getRate())) {
                    LogUtil.error(getClass(), "Unknown rate for speed:" + section.getRate());

                    unkownRates.add(section.getRate());
                }
            }
			// if (r == DicConst.LR_DSR_Gigabit_Ethernet)
			// rate = "1000M";

		}
		csection.setSpeed(rate);
		csection.setDirection(DicUtil.getConnectionDirection(section.getDirection()));
		csection.setAendTp(section.getaEndTP());

		csection.setZendTp(section.getzEndTP());
		DatabaseUtil.getSID(CPTP.class,csection.getAendTp());

		DatabaseUtil.getSID(CPTP.class, csection.getZendTp());
		csection.setParentDn(section.getParentDn());
		csection.setEmsName(section.getEmsName());
		csection.setUserLabel(section.getUserLabel());
		csection.setNativeEMSName(section.getNativeEMSName());
		csection.setOwner(section.getOwner());
		csection.setAdditionalInfo(section.getAdditionalInfo());
		return csection;
	}
	public boolean isEmptyResult(Collection c) {
		return c == null ? true : c.isEmpty();
	}
	protected void migrateCC() throws Exception {
		List<CrossConnect> ccs = sd.queryAll(CrossConnect.class);

		if (isEmptyResult(ccs))
			return;
		executeTableDelete("C_CROSSCONNECT",emsdn);
	//	executeDelete("delete from CCrossConnect c where c.emsName = '" + emsdn + "'", CCrossConnect.class);
		DataInserter di = new DataInserter(emsid);



		if (ccs != null && ccs.size() > 0) {
			for (CrossConnect cc : ccs) {
				CCrossConnect ccc = transCC(cc);
				ccc.setSid(DatabaseUtil.nextSID(CCrossConnect.class));
				di.insert(ccc);
			}
		}
		di.end();
	}

	protected CCrossConnect transCC(CrossConnect src) {
		CCrossConnect des = new CCrossConnect();
		des.setDn(src.getDn());
		des.setCollectTimepoint(src.getCreateDate());
		des.setCcType(src.getCcType());
		des.setDirection(src.getDirection());
		//TODO
		des.setAend(src.getaEndNameList());
		des.setZend(src.getzEndNameList());
		des.setAptp(src.getaEndTP());
		des.setZptp(src.getzEndTP());
		des.setParentDn(src.getParentDn());
		des.setEmsName(emsdn);
		des.setAdditionalInfo(src.getAdditionalInfo());
		return des;
	}

	public CFTP_PTP transFTP_PTP(String emsdn, R_FTP_PTP r_ftp_ptp) {
		// R_FTP_PTP r_ftp_ptp = list.get(i);
		CFTP_PTP cftp_ptp = new CFTP_PTP();
		cftp_ptp.setDn(r_ftp_ptp.getDn());
		cftp_ptp.setEmsName(emsdn);
		cftp_ptp.setFtpDn(r_ftp_ptp.getFtpDn());
		cftp_ptp.setPtpDn(r_ftp_ptp.getPtpDn());
		cftp_ptp.setFtpId(DatabaseUtil.getSID(CPTP.class, cftp_ptp.getFtpDn()));
		cftp_ptp.setPtpId(DatabaseUtil.getSID(CPTP.class, cftp_ptp.getPtpDn()));
		cftp_ptp.setRate(r_ftp_ptp.getRate());
		cftp_ptp.setTransmissionParams(r_ftp_ptp.getTransmissionParams());
		cftp_ptp.setTpMappingMode(r_ftp_ptp.getTpMappingMode());
		return cftp_ptp;
	}

	protected JPASupport createJPASupport() {
		JPASupport jpaSupport = new JPASupportSpringImpl("entityManagerFactoryData");
		return jpaSupport;
	}
	protected Class[] getStatClss() {
	   return new Class[] { CSubnetwork.class, CSubnetworkDevice.class, CDevice.class, CRack.class, CShelf.class, CSlot.class, CEquipment.class,
			   CPTP.class, CCTP.class, CFTP_PTP.class, CIPAddress.class, CCrossConnect.class, CPWE3.class, CPW.class, CPWE3_PW.class, CTunnel.class,
			   CPW_Tunnel.class, CIPRoute.class, CSection.class, CTunnel_Section.class, CProtectionGroup.class, CProtectionGroupTunnel.class };

	}
	protected HashMap printTableStat() {
		JPASupport jpaSupport = createJPASupport();
		HashMap map = new HashMap();
		Class[] clss = getStatClss();
		for (int i = 0; i < clss.length; i++) {
			Class cls = clss[i];
			String ql = "select count(c.id) from " + cls.getName() + " c where c.emsName = '" + emsdn + "'";
			List list = JPAUtil.getInstance().queryQL(jpaSupport, ql);

			map.put(cls,list.get(0));
			MigrateThread.thread().getLogger().info(cls.getName() + " == " + list.get(0));
		}
		jpaSupport.release();
		return map;

	}

	// public void migrateProtectingPWE3Tunnel() throws Exception {
	// JPASupport jpaSupport = createJPASupport();
	// List<CProtectionGroupTunnel> cpgts = null;
	// List<CPWE3_Tunnel> cpwe3_tunnels = null;
	// HashMap<String,ArrayList> groupTunnels = null;
	// try {
	// cpgts = JPAUtil.getInstance().findObjects(jpaSupport, "select c from CProtectionGroupTunnel c where c.emsName = '" + emsdn + "'");
	// groupTunnels = new HashMap<String, ArrayList>();
	// cpwe3_tunnels = JPAUtil.getInstance().findObjects(jpaSupport,"select c from CPWE3_Tunnel c where c.emsName = '"+emsdn+"'");
	// } catch (Exception e) {
	// getLogger().error(e, e);
	// } finally {
	// jpaSupport.release();
	// }
	//
	// HashMap<String,String> tunnelPGroup = new HashMap<String, String>();
	// if (cpgts != null) {
	// for (int i = 0; i < cpgts.size(); i++) {
	// CProtectionGroupTunnel cProtectionGroupTunnel = cpgts.get(i);
	// String protectGroupDn = cProtectionGroupTunnel.getProtectGroupDn();
	// String tunnelDn = cProtectionGroupTunnel.getTunnelDn();
	// if (tunnelDn == null || protectGroupDn == null) continue;;
	// ArrayList arrayList = groupTunnels.get(protectGroupDn);
	// if (arrayList == null) {
	// arrayList = new ArrayList();
	// groupTunnels.put(protectGroupDn,arrayList);
	// }
	// arrayList.add(cProtectionGroupTunnel);
	//
	// tunnelPGroup.put(tunnelDn,protectGroupDn);
	// }
	// }
	//
	//
	// HashSet set = new HashSet();
	// if (cpwe3_tunnels == null) cpwe3_tunnels = new ArrayList<CPWE3_Tunnel>();
	// if (cpwe3_tunnels != null) {
	// for (int i = 0; i < cpwe3_tunnels.size(); i++) {
	// CPWE3_Tunnel cpwe3_tunnel = cpwe3_tunnels.get(i);
	// String tunnelDn = cpwe3_tunnel.getTunnelDn();
	// String pwe3Dn = cpwe3_tunnel.getPwe3Dn();
	// set.add(pwe3Dn+"<>"+tunnelDn);
	// }
	// }
	//
	// DataInserter di = new DataInserter(emsid);
	// try {
	// for (int i = 0; i < cpwe3_tunnels.size(); i++) {
	// CPWE3_Tunnel cpwe3_tunnel = cpwe3_tunnels.get(i);
	// String tunnelDn = cpwe3_tunnel.getTunnelDn();
	// String pwe3Dn = cpwe3_tunnel.getPwe3Dn();
	// String protectGroupDn = tunnelPGroup.get(tunnelDn);
	// if (protectGroupDn != null) {
	// ArrayList<CProtectionGroupTunnel> cProtectionGroupTunnels = groupTunnels.get(protectGroupDn);
	// if (cProtectionGroupTunnels != null) {
	// for (int j = 0; j < cProtectionGroupTunnels.size(); j++) {
	// CProtectionGroupTunnel cProtectionGroupTunnel = cProtectionGroupTunnels.get(j);
	// String newTunnelDn = cProtectionGroupTunnel.getTunnelDn();
	// String key = (pwe3Dn+"<>"+ newTunnelDn);
	// if (!set.contains(key)) {
	// CPWE3_Tunnel cpwe3Tunnel = new CPWE3_Tunnel();
	// cpwe3Tunnel.setDn(SysUtil.nextDN());
	// cpwe3Tunnel.setCollectTimepoint(new Date());
	// cpwe3Tunnel.setPwe3Dn(pwe3Dn);
	// cpwe3Tunnel.setPwe3Id(DatabaseUtil.getSID(CPWE3.class, pwe3Dn));
	// cpwe3Tunnel.setTunnelDn(newTunnelDn);
	// cpwe3Tunnel.setTunnelId(DatabaseUtil.getSID(CTunnel.class, newTunnelDn));
	// cpwe3Tunnel.setStatus(cProtectionGroupTunnel.getStatus());
	// cpwe3Tunnel.setEmsName(emsdn);
	// di.insert(cpwe3Tunnel);
	// System.out.println("insert:"+key);
	// }
	//
	// }
	// }
	// }
	// }
	// } catch (Exception e) {
	// getLogger().error(e, e);
	// } finally {
	// di.end();
	// }
	//
	//
	// }

	public void migrateProtectingPWTunnel() throws Exception {
		HashMap<String, ArrayList<CProtectionGroupTunnel>> groupTunnels = new HashMap<String, ArrayList<CProtectionGroupTunnel>>();
		JPASupport jpaSupport = createJPASupport();
		List<CProtectionGroupTunnel> cpgts = null;
		List<CPW_Tunnel> cpw_tunnels = null;
		try {
			cpgts = JPAUtil.getInstance().findObjects(jpaSupport, "select c from CProtectionGroupTunnel c where c.emsName = '" + emsdn + "'");
			cpw_tunnels = JPAUtil.getInstance().findObjects(jpaSupport, "select c from CPW_Tunnel c where c.emsName = '" + emsdn + "'");
		} catch (Exception e) {
			getLogger().error(e, e);
		} finally {
			jpaSupport.release();
		}

		HashMap<String, String> tunnelPGroup = new HashMap<String, String>();
		if (cpgts != null) {
			for (CProtectionGroupTunnel cProtectionGroupTunnel : cpgts) {
				String protectGroupDn = cProtectionGroupTunnel.getProtectGroupDn();
				String tunnelDn = cProtectionGroupTunnel.getTunnelDn();
				if (tunnelDn == null || protectGroupDn == null)
					continue;
				ArrayList<CProtectionGroupTunnel> arrayList = groupTunnels.get(protectGroupDn);
				if (arrayList == null) {
					arrayList = new ArrayList<CProtectionGroupTunnel>();
					groupTunnels.put(protectGroupDn, arrayList);
				}
				arrayList.add(cProtectionGroupTunnel);

				tunnelPGroup.put(tunnelDn, protectGroupDn);
			}
		}

		if (cpw_tunnels == null)
			cpw_tunnels = new ArrayList<CPW_Tunnel>();

		DataInserter di = new DataInserter(emsid);
		try {
			for (CPW_Tunnel cpw_tunnel : cpw_tunnels) {
				String tunnelDn = cpw_tunnel.getTunnelDn();
				String pwDn = cpw_tunnel.getPwDn();
				String protectGroupDn = tunnelPGroup.get(tunnelDn);
				if (protectGroupDn != null) {
					ArrayList<CProtectionGroupTunnel> cProtectionGroupTunnels = groupTunnels.get(protectGroupDn);
					if (cProtectionGroupTunnels != null) {
						for (CProtectionGroupTunnel cProtectionGroupTunnel : cProtectionGroupTunnels) {
							String newTunnelDn = cProtectionGroupTunnel.getTunnelDn();
							String key = (pwDn + "<>" + newTunnelDn);
							if (!DatabaseUtil.isSIDExisted(CPW_Tunnel.class, key)) {
								CPW_Tunnel cpwe3Tunnel = new CPW_Tunnel();
								cpwe3Tunnel.setDn(key);
								cpwe3Tunnel.setCollectTimepoint(new Date());
								cpwe3Tunnel.setPwDn(pwDn);
								cpwe3Tunnel.setPwId(DatabaseUtil.getSID(CPW.class, pwDn));
								cpwe3Tunnel.setTunnelDn(newTunnelDn);
								cpwe3Tunnel.setTunnelId(DatabaseUtil.getSID(CTunnel.class, newTunnelDn));
								cpwe3Tunnel.setStatus(cProtectionGroupTunnel.getStatus());
								cpwe3Tunnel.setEmsName(emsdn);
								di.insert(cpwe3Tunnel);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			getLogger().error(e, e);
		} finally {
			di.end();
		}

	}

	public void migrateProtectGroup() throws Exception {
		if (!isTableHasData(TrailNtwProtection.class))
			return;
		List<TrailNtwProtection> pgs = sd.queryAll(TrailNtwProtection.class);
		if (pgs == null || pgs.isEmpty()) {
			getLogger().error("pg data is empty ,skipped");
			return;
		}
		executeDelete("delete from CProtectionGroup c where c.emsName = '" + emsdn + "'", CProtectionGroup.class);
		executeDelete("delete from CProtectionGroupTunnel c where c.emsName = '" + emsdn + "'", CProtectionGroupTunnel.class);
		DataInserter di = new DataInserter(emsid);

		// List list = (List) com.alcatelsbell.nms.util.ObjectUtil.readObjectByPath("d:\\work\\ptlist_SNCP");
		// ManagedElementDataTask task = new ManagedElementDataTask();
		// List<ProtectionGroup> pgs = task.transProtectGroup(list);



		List<CProtectionGroup> cpps = new ArrayList<CProtectionGroup>();
		for (int i = 0; i < pgs.size(); i++) {
			TrailNtwProtection protectionGroup = pgs.get(i);
			CProtectionGroup cpg = transProtectionGroup(protectionGroup);
			di.insert(cpg);
			cpps.add(cpg);
		}

		// /////////////////////////插入关联表///////////////////////////////////
		for (int i = 0; i < cpps.size(); i++) {
			CProtectionGroup cProtectionGroup = cpps.get(i);
			String protectingList = cProtectionGroup.getProtectingList();
			String protectedList = cProtectionGroup.getProtectedList();
			String[] protectings = protectingList.split("\\|\\|");
			String[] protecteds = protectedList.split("\\|\\|");
			if (protectings != null && protectings.length > 0) {
				for (int j = 0; j < protectings.length; j++) {
					String protecting = protectings[j];
					if (protecting != null && protecting.length() > 0) {
						CProtectionGroupTunnel cProtectionGroupTunnel = new CProtectionGroupTunnel();
						cProtectionGroupTunnel.setDn(cProtectionGroup.getDn() + "<>" + protecting);
						// cProtectionGroupTunnel.setDn(SysUtil.nextDN());
						// cProtectionGroupTunnel.setSid(DatabaseUtil.nextSID(cProtectionGroupTunnel));
						cProtectionGroupTunnel.setProtectGroupId(cProtectionGroup.getSid());
						cProtectionGroupTunnel.setProtectGroupDn(cProtectionGroup.getDn());
						cProtectionGroupTunnel.setTunnelDn(protecting);
						cProtectionGroupTunnel.setTunnelId(DatabaseUtil.getSID(CTunnel.class, protecting));
						cProtectionGroupTunnel.setEmsName(emsdn);
						cProtectionGroupTunnel.setStatus("PROTECTING");
						// 错误的pg导致DN重复
						if (!DatabaseUtil.isSIDExisted(CProtectionGroupTunnel.class, cProtectionGroupTunnel.getDn())) {
							di.insert(cProtectionGroupTunnel);
						}
					}
				}
			}

			if (protecteds != null && protecteds.length > 0) {
				for (int j = 0; j < protecteds.length; j++) {
					String protectedd = protecteds[j];
					if (protectedd != null && protectedd.length() > 0) {
						CProtectionGroupTunnel cProtectionGroupTunnel = new CProtectionGroupTunnel();
						cProtectionGroupTunnel.setDn(cProtectionGroup.getDn() + "<>" + protectedd);
						// cProtectionGroupTunnel.setDn(SysUtil.nextDN());
						// cProtectionGroupTunnel.setSid(DatabaseUtil.nextSID(cProtectionGroupTunnel));
						cProtectionGroupTunnel.setProtectGroupId(cProtectionGroup.getSid());
						cProtectionGroupTunnel.setProtectGroupDn(cProtectionGroup.getDn());
						cProtectionGroupTunnel.setTunnelId(DatabaseUtil.getSID(CTunnel.class, protectedd));
						cProtectionGroupTunnel.setTunnelDn(protectedd);
						cProtectionGroupTunnel.setEmsName(emsdn);
						cProtectionGroupTunnel.setStatus("PROTECTED");
						// 错误的pg导致DN重复
						if (!DatabaseUtil.isSIDExisted(CProtectionGroupTunnel.class, cProtectionGroupTunnel.getDn())) {
							di.insert(cProtectionGroupTunnel);
						}
					}
				}
			}
		}

		di.end();

	}

	private CProtectionGroup transProtectionGroup(TrailNtwProtection src) {
		CProtectionGroup des = new CProtectionGroup();
		des.setDn(src.getDn());
		des.setSid(DatabaseUtil.nextSID(des));
		des.setParentDn(src.getParentDn());
		des.setEmsName(emsdn);
		des.setUserLabel(src.getUserLabel());
		des.setNativeEMSName(src.getNativeEMSName());
		des.setOwner(src.getOwner());
		des.setProtectionGroupType(src.getProtectionGroupType());
		des.setProtectionSchemeState(src.getProtectionSchemeState());
		des.setReversionMode(src.getReversionMode());
		des.setRate(src.getRate());
		des.setPgpParameters(src.getTnpParameters());
		String protectedList = src.getWorkerTrailList();
		des.setProtectedList(protectedList);
		String protectingList = src.getProtectionTrail();
		des.setProtectingList(protectingList);
		return des;
	}

	protected String ptp2ne(String ptp) {
		if (!ptp.contains(Constant.dnSplit)) return "";
		return ptp.substring(0, ptp.lastIndexOf(Constant.dnSplit));
	}

	protected CPWE3_PW transCPWE3_PW(CPWE3 cpwe3, CPW cpw) {
		CPWE3_PW cpwe3_pw = new CPWE3_PW();
		cpwe3_pw.setDn(cpwe3.getDn() + "<>" + cpw.getDn());
		cpwe3_pw.setCollectTimepoint(cpwe3.getCollectTimepoint());
		cpwe3_pw.setPwe3Dn(cpwe3.getDn());
//		cpwe3_pw.setPwe3Id(DatabaseUtil.getSID(CPWE3.class, cpwe3.getDn()));
		cpwe3_pw.setPwDn(cpw.getDn());
		cpwe3_pw.setPwId(DatabaseUtil.getSID(CPW.class, cpw.getDn()));
		cpwe3_pw.setEmsName(cpwe3.getEmsName());

		return cpwe3_pw;
	}

	protected CPW_Tunnel transCPW_Tunnel(String emsname, String pwdn, String tunneldn) {

		CPW_Tunnel cpw_tunnel = new CPW_Tunnel();
		cpw_tunnel.setDn(pwdn + "<>" + tunneldn);
		cpw_tunnel.setPwDn(pwdn);
		cpw_tunnel.setPwId(DatabaseUtil.getSID(CPW.class, pwdn));
		cpw_tunnel.setTunnelDn(tunneldn);
		cpw_tunnel.setTunnelId(DatabaseUtil.getSID(CTunnel.class, tunneldn));
		cpw_tunnel.setEmsName(emsname);

		return cpw_tunnel;
	}

//	protected void createVC4Channel() throws Exception {
//		executeDelete("delete  from CChannel c where c.emsName = '" + emsdn + "'", CChannel.class);
//		DataInserter di = new DataInserter(emsid);
//		List<Section> sections = sd.queryAll(Section.class);
//		if (sections != null && sections.size() > 0) {
//			for (Section section : sections) {
//				List<CCTP> aCTPs = sd.query("select c from CCTP c where portdn='" + section.getaEndTP() + "'");
//				List<CCTP> zCTPs = sd.query("select c from CCTP c where portdn='" + section.getzEndTP() + "'");
//				for (int i = 0; i < aCTPs.size(); i++) {
//					CCTP aCTP = aCTPs.get(i);
//					if (!isVC4Rate(Integer.valueOf(aCTP.getRate()))) {
//						continue;
//					}
//					for (int j = 0; j < zCTPs.size(); j++) {
//						CCTP zCTP = zCTPs.get(j);
//						if (isVC4Rate(Integer.valueOf(zCTP.getRate()))) {
//							continue;
//						}
//						if (aCTP.getRate().equals(zCTP.getRate()) && aCTP.getValue().equals(zCTP.getValue())) {
//							CChannel cchannel = transChannel(section, aCTP, zCTP);
//							cchannel.setSid(DatabaseUtil.nextSID(cchannel));
//							di.insert(cchannel);
//						}
//					}
//				}
//			}
//			di.end();
//		}
//	}

	protected void createVC3And12Channel() throws Exception {

	}

	protected void createTrail() throws Exception {
		executeDelete("delete  from CTrail c where c.emsName = '" + emsdn + "'", CRoute.class);
		DataInserter di = new DataInserter(emsid);
		List<SubnetworkConnection> sncs = sd.queryAll(SubnetworkConnection.class);
		if (sncs != null && sncs.size() > 0) {
			for (SubnetworkConnection snc : sncs) {
				CRoute ctrail = transTrail(snc);
				ctrail.setSid(DatabaseUtil.nextSID(ctrail));
				di.insert(ctrail);
			}
			di.end();
		}
	}

	protected CRoute transTrail(SubnetworkConnection snc) {
		CRoute trail = new CRoute();
		trail.setAend(snc.getaEnd());
		trail.setAptp(snc.getaPtp());
		trail.setZend(snc.getzEnd());
		trail.setZptp(snc.getzPtp());
		trail.setDirection(DicUtil.getConnectionDirection(snc.getDirection()));
		trail.setDn(snc.getDn());
		trail.setEmsName(snc.getEmsName());
		trail.setRate(snc.getRate());
		trail.setNativeEmsName(snc.getNativeEMSName());
		trail.setSncState(snc.getSncState());
		trail.setSncType(snc.getSncType());

		return trail;
	}

//	protected CChannel transChannel(Section section, CCTP aCTP, CCTP zCTP) {
//		CChannel channel = new CChannel();
//		channel.setAend(aCTP.getDn());
//		channel.setAptp(section.getaEndTP());
//		channel.setZend(zCTP.getDn());
//		channel.setZptp(section.getzEndTP());
//		channel.setDirection(section.getDirection());
//		channel.setDn(aCTP.getDn() + "<>" + zCTP.getDn());
//		channel.setEmsName(emsdn);
//		channel.setRate(aCTP.getRate());
//		channel.setSection(section.getDn());
//		return channel;
//	}

	protected static boolean isVC4Rate(int rate) {
		switch (rate) {
		case DicConst.LR_STS3c_and_AU4_VC4:
		case DicConst.LR_STS12c_and_VC4_4c:
		case DicConst.LR_STS48c_and_VC4_16c:
		case DicConst.LR_STS192c_and_VC4_64c:
			return true;
		}
		return false;
	}

	
    // <-- 以下是新接口入库方法 -->
	
	/**
	 * 同步板卡
	 * @throws Exception
	 */
	protected void migrateCRD() throws Exception {
		if (!isTableHasData(CRD.class))
			return;
		executeDelete("delete   from CEquipment c where c.emsName = '" + emsdn + "'", CEquipment.class);
		List<CRD> equipments = sd.queryAll(CRD.class);
		insertCRDs(equipments);
	}
	private void insertCRDs(List<CRD> equipments) throws Exception {
		DataInserter di = new DataInserter(emsid);
		if (equipments != null && equipments.size() > 0) {
			for (CRD equipment : equipments) {
				CEquipment cEquipment = transNewCRD(equipment);
                if (cEquipment != null)
				    di.insert(cEquipment);
			}
		}
		di.end();
	}
	public CEquipment transNewCRD(CRD equipment) {
		CEquipment cEquipment = new CEquipment();
		cEquipment.setCollectTimepoint(equipment.getCreateDate());
		cEquipment.setDn(equipment.getRmUID());
		
		cEquipment.setSlotDn(equipment.getHolderrmUID());
		cEquipment.setParentDn(equipment.getHolderrmUID());
		cEquipment.setSlotId(DatabaseUtil.getSID(CSlot.class, equipment.getHolderrmUID()));
		
		cEquipment.setAdditionalInfo(null);
		cEquipment.setEmsName(emsdn);
		
		cEquipment.setExpectedEquipmentObjectType(null);
		cEquipment.setInstalledEquipmentObjectType(equipment.getCardType());
		cEquipment.setInstalledPartNumber(null);
		cEquipment.setInstalledSerialNumber(equipment.getSerialNumber());
		cEquipment.setInstalledVersion(null);
		
		cEquipment.setNativeEMSName(equipment.getCardType());
		cEquipment.setOwner(null);
		cEquipment.setServiceState(equipment.getServiceState());
		cEquipment.setUserLabel(equipment.getNativeName());

		return cEquipment; // To change body of created methods use File | Settings | File Templates.
	}

	/**
	 * 同步槽道
	 * @param equipmentHolders
	 * @throws Exception
	 */
	protected void insertEQHs(List<EQH> equipmentHolders) throws Exception {
		if (shelfTypeMap != null) {
			shelfTypeMap.clear();
			shelfTypeMap = null;
		}
		DataInserter di = new DataInserter(emsid);

		// // ////////////////// 将EQH分类///////////////////
		List<EQH> racks = new ArrayList<EQH>();
		List<EQH> shelfs = new ArrayList<EQH>();
		List<EQH> subshelfs = new ArrayList<EQH>();
		List<EQH> slots = new ArrayList<EQH>();
		List<EQH> subslots = new ArrayList<EQH>();

		for (int i = 0; i < equipmentHolders.size(); i++) {
			EQH equipmentHolder = equipmentHolders.get(i);
			if (equipmentHolder.getHolderType().equals("rack")) {
				racks.add(equipmentHolder);
			} else if (equipmentHolder.getHolderType().equals("shelf")) {
				shelfs.add(equipmentHolder);
			}else if (equipmentHolder.getHolderType().equals("sub_shelf")) {
				subshelfs.add(equipmentHolder);
			}

			else if (equipmentHolder.getHolderType().equals("slot")) {
				slots.add(equipmentHolder);
			} else if (equipmentHolder.getHolderType().equals("sub_slot")) {
				subslots.add(equipmentHolder);
			}
		}
		
		// ////////////////// 将EQH分类///////////////////
		for (EQH equipmentHolder : racks) {
			CdcpObject cEquipmentHolder = transNewEQH(equipmentHolder);
			di.insert(cEquipmentHolder);
		}
		for (EQH equipmentHolder : subshelfs) {
			CdcpObject cEquipmentHolder = transNewEQH(equipmentHolder);
			di.insert(cEquipmentHolder);
		}
		for (EQH equipmentHolder : shelfs) {
			CdcpObject cEquipmentHolder = transNewEQH(equipmentHolder);
			di.insert(cEquipmentHolder);
		}

		for (EQH equipmentHolder : slots) {
			CdcpObject cEquipmentHolder = transNewEQH(equipmentHolder);
			di.insert(cEquipmentHolder);
		}
		for (EQH equipmentHolder : subslots) {
			CdcpObject cEquipmentHolder = transNewEQH(equipmentHolder);
			di.insert(cEquipmentHolder);
		}
		
		di.end();
	}
	public CdcpObject transNewEQH(EQH equipmentHolder) {
		if (equipmentHolder.getHolderType().equals("rack")) {
			CRack cequipmentHolder = new CRack();
			cequipmentHolder.setCdeviceId(DatabaseUtil.getSID(CDevice.class, equipmentHolder.getNermUID()));
			cequipmentHolder.setDn(equipmentHolder.getRmUID());
			cequipmentHolder.setSid(DatabaseUtil.nextSID(cequipmentHolder));
			cequipmentHolder.setNo(equipmentHolder.getHolderNumber());
			cequipmentHolder.setCollectTimepoint(equipmentHolder.getCreateDate());
			cequipmentHolder.setHolderType(equipmentHolder.getHolderType());
			cequipmentHolder.setExpectedOrInstalledEquipment(null);
			cequipmentHolder.setAcceptableEquipmentTypeList(null);
			cequipmentHolder.setHolderState(equipmentHolder.getHolderState());
			cequipmentHolder.setParentDn(equipmentHolder.getNermUID());
			cequipmentHolder.setEmsName(emsdn);
			cequipmentHolder.setUserLabel(null);
			cequipmentHolder.setNativeEMSName(equipmentHolder.getNativeName());
			cequipmentHolder.setOwner(null);
			cequipmentHolder.setAdditionalInfo(assembleEQHAdditionalInfo(equipmentHolder));

			return cequipmentHolder;
		}  else if (equipmentHolder.getHolderType().equals("shelf")) {
			CShelf cequipmentHolder = new CShelf();
			cequipmentHolder.setRackDn(equipmentHolder.getParentHolderrmUID());
			cequipmentHolder.setRackId(DatabaseUtil.getSID(CRack.class, equipmentHolder.getParentHolderrmUID()));

			cequipmentHolder.setNo(equipmentHolder.getHolderNumber());
			cequipmentHolder.setDn(equipmentHolder.getRmUID());
			cequipmentHolder.setSid(DatabaseUtil.nextSID(cequipmentHolder));

			cequipmentHolder.setCollectTimepoint(equipmentHolder.getCreateDate());
			cequipmentHolder.setHolderType(equipmentHolder.getHolderType());
			cequipmentHolder.setExpectedOrInstalledEquipment(null);
			cequipmentHolder.setAcceptableEquipmentTypeList(null);
			cequipmentHolder.setHolderState(equipmentHolder.getHolderState());
			cequipmentHolder.setParentDn(equipmentHolder.getNermUID());
			cequipmentHolder.setEmsName(emsdn);
			cequipmentHolder.setUserLabel(null);
			cequipmentHolder.setNativeEMSName(equipmentHolder.getNativeName());
			cequipmentHolder.setOwner(null);
			cequipmentHolder.setAdditionalInfo(assembleEQHAdditionalInfo(equipmentHolder));
			cequipmentHolder.setShelfType(equipmentHolder.getProductName());
			return cequipmentHolder;
		} else if (equipmentHolder.getHolderType().equals("slot") || equipmentHolder.getHolderType().equals("sub_slot")) {
			CSlot cequipmentHolder = new CSlot();
			cequipmentHolder.setShelfDn(equipmentHolder.getParentHolderrmUID());
			cequipmentHolder.setShelfId(DatabaseUtil.getSID(CShelf.class, equipmentHolder.getParentHolderrmUID()));
			
			cequipmentHolder.setNo(equipmentHolder.getHolderNumber());
			cequipmentHolder.setDn(equipmentHolder.getRmUID());
			cequipmentHolder.setSid(DatabaseUtil.nextSID(cequipmentHolder));

			cequipmentHolder.setCollectTimepoint(equipmentHolder.getCreateDate());
			cequipmentHolder.setHolderType(equipmentHolder.getHolderType());
			cequipmentHolder.setExpectedOrInstalledEquipment(null);
			cequipmentHolder.setAcceptableEquipmentTypeList(null);
			if (equipmentHolder.getHolderType().equals("sub_slot")) {
				cequipmentHolder.setParentSlotDn(equipmentHolder.getParentHolderrmUID());
				if (slot_shelfMap != null) {
					String shelfDn = slot_shelfMap.get(equipmentHolder.getParentHolderrmUID());
					if (Detect.notEmpty(shelfDn)) {
						cequipmentHolder.setShelfDn(shelfDn);
						cequipmentHolder.setShelfId(DatabaseUtil.getSID(CShelf.class, shelfDn));
					} else {
						getLogger().error("sub_slot的ParentHolder没有shelf : " + equipmentHolder.getRmUID());
						System.out.println("sub_slot的ParentHolder没有shelf : " + equipmentHolder.getRmUID());
					}
				} else {
					getLogger().error("slot_shelfMap is null!");
					System.out.println("slot_shelfMap is null!");
				}
			}

			if (cequipmentHolder.getAcceptableEquipmentTypeList() != null && cequipmentHolder.getAcceptableEquipmentTypeList().length() > 1500)
				cequipmentHolder.setAcceptableEquipmentTypeList("");

			cequipmentHolder.setHolderState(equipmentHolder.getHolderState());
			cequipmentHolder.setParentDn(equipmentHolder.getNermUID());
			cequipmentHolder.setEmsName(emsdn);
			cequipmentHolder.setUserLabel(null);
			cequipmentHolder.setNativeEMSName(equipmentHolder.getNativeName());
			cequipmentHolder.setOwner(null);
			cequipmentHolder.setAdditionalInfo(assembleEQHAdditionalInfo(equipmentHolder));
			return cequipmentHolder;
		}
		return null;
	}
	protected String assembleEQHAdditionalInfo(EQH eqh) {
		String additionalInfo = "";
		
		
		return additionalInfo;
	}

	/**
	 * 同步端口
	 * @throws Exception
	 */
	protected void migratePRT() throws Exception {
		if (!isTableHasData(PRT.class))
			return;
		executeDelete("delete  from CPTP c where c.emsName = '" + emsdn + "'", CPTP.class);
		executeDelete("delete from CIPAddress c where c.emsName = '" + emsdn + "'", CIPAddress.class);
		List<PRT> ptps = sd.queryAll(PRT.class);
		insertPRTs(ptps);
	}
	private void insertPRTs(List<PRT> ptps) throws Exception {
		DataInserter di = new DataInserter(emsid);
		getLogger().info("migratePtp size = " + (ptps == null ? null : ptps.size()));
		List<CPTP> cptps = new ArrayList<CPTP>();
		if (ptps != null && ptps.size() > 0) {
			for (PRT ptp : ptps) {
				CPTP cptp = transPRT(ptp);
				if (cptp != null) {
					cptps.add(cptp);
				}
			}
		}

		this.removeDuplicateDN(cptps);
		for (int i = 0; i < cptps.size(); i++) {
			CPTP cptp = cptps.get(i);
			di.insert(cptp);
			if (cptp.getIpAddress() != null && !cptp.getIpAddress().isEmpty()) {
				CIPAddress ip = new CIPAddress();
				ip.setDn(SysUtil.nextDN());
				ip.setSid(DatabaseUtil.nextSID(ip));
				ip.setEmsName(emsdn);
				ip.setEmsid(emsid);
				ip.setIpaddress(cptp.getIpAddress());
				ip.setPtpId(DatabaseUtil.getSID(CPTP.class, cptp.getDn()));
				di.insert(ip);
			}
		}
		di.end();
	}
	public CPTP transPRT(PRT ptp) {
		CPTP cptp = new CPTP();
		cptp.setDn(ptp.getRmUID());
		
		cptp.setCardid(DatabaseUtil.getSID(CEquipment.class, ptp.getCardrmUID()));
		cptp.setDeviceDn(ptp.getNermUID());
		cptp.setParentDn(ptp.getCardrmUID());
		
		cptp.setSid(DatabaseUtil.nextSID(cptp));
		cptp.setCollectTimepoint(ptp.getCreateDate());
		cptp.setEdgePoint(true);//未采集
		cptp.setConnectionState(null);//未采集
		cptp.setTpMappingMode(null);//未采集 "D_NA"
		cptp.setTpProtectionAssociation(null);//未采集 "TPPA_NA"
		cptp.setDirection(DicUtil.getPtpDirection(ptp.getDirection()));
		cptp.setRate(ptp.getPortRate());
//		if (!"NA".equals(ptp.getPortRate())) {
//			cptp.setSpeed(ptp.getPortRate());
//		}
		cptp.setSpeed(DicUtil.getRateWithoutNA(ptp.getPortRate()));
		cptp.setEmsName(emsdn);
		cptp.setUserLabel(null);//未采集 "VMU48_OTN_TP"
		cptp.setNativeEMSName(ptp.getNativeName());
		cptp.setOwner(null);
		cptp.setNo(ptp.getPortNo());
		int eoType = 0;
		if ("electrical".equalsIgnoreCase(ptp.getSignalType())) {
			eoType = DicConst.EOTYPE_ELECTRIC;
			cptp.setEoType(eoType);
		}
		if ("optical".equalsIgnoreCase(ptp.getSignalType())) {
			eoType = DicConst.EOTYPE_OPTIC;
			cptp.setEoType(eoType);
		}
		
		String ptpType = "";
		if ("ftp".equalsIgnoreCase(ptp.getPhysicalOrLogical())) {
			ptpType = "LOGICAL";
		} else {
			if ("electrical".equalsIgnoreCase(ptp.getSignalType())) {
				ptpType = "ELECTRICAL";
			} else if ("optical".equalsIgnoreCase(ptp.getSignalType())) {
				ptpType = "OPTICAL";
			}
		}
		cptp.setType(ptpType);// 根据PhysicalOrLogical和SignalType综合计算
		
		String additionalInfo = "BandParity:||ProtectionRole:||SupportedPortType:" + ptp.getSignalType() + "||";
		cptp.setAdditionalInfo(additionalInfo);
		
		cptp.setTransmissionParams(null);//未采集
		cptp.setPortRate(ptp.getPortRate());

//		Map<String, String> map = MigrateUtil.transMapValue(ptp.getTransmissionParams());
//		cptp.setPortMode(map.get("PortMode"));
//		cptp.setPortRate(map.get("PortRate"));
//		cptp.setWorkingMode(map.get("WorkingMode"));
//		cptp.setMacAddress(map.get("MACAddress"));
//		cptp.setIpAddress(map.get("IPAddress"));
//		cptp.setIpMask(map.get("IPMask"));

		return cptp; // To change body of created methods use File | Settings | File Templates.
	}
	
	/**
	 * 同步CTP
	 */
	protected void migrateNewCTP() throws Exception {
		List<CTP2> ctps = sd.queryAll(CTP2.class);
//		sd.execute("create index index1 on ctp2(ctpname, relatedportrmuid);");
		if (isEmptyResult(ctps))
			return;

		executeTableDelete("C_CTP", emsdn);
		insertNewCtps(ctps);
	}
	protected List insertNewCtps(List<CTP2> ctps) throws Exception {
		DataInserter di = new DataInserter(emsid);
		getLogger().info("migrateCtp size = " + (ctps == null ? null : ctps.size()));
		List<CCTP> cctps = new ArrayList<CCTP>();
		if (ctps != null && ctps.size() > 0) {
			for (CTP2 ctp : ctps) {
				CCTP cctp = transNewCTP2(ctp);
				if (cctp != null) {
					cctps.add(cctp);
					di.insert(cctp);
				}
			}
		}

		di.end();
        return cctps;
	}
	public CCTP transNewCTP2(CTP2 ctp) {
		CCTP cctp = new CCTP();
		cctp.setDn(ctp.getRmUID());

		cctp.setPortdn(ctp.getRelatedPortrmUID());
		cctp.setParentCtpdn(null);// 未采集
		cctp.setSid(DatabaseUtil.nextSID(cctp));
		cctp.setCollectTimepoint(ctp.getCreateDate());
		cctp.setEdgePoint(false);// 未采集
		cctp.setType(ctp.getCtpType());
		cctp.setConnectionState(null);// 未采集
		cctp.setTpMappingMode(null);// 未采集
		cctp.setDirection(DicUtil.getCtpDirection(ctp.getDirection()));
		cctp.setTransmissionParams(null);// 未采集
//        if (ctp.getTransmissionParams() != null && ctp.getTransmissionParams().contains("@"))
//            ctp.setRate(cctp.getTransmissionParams().substring(0,ctp.getTransmissionParams().indexOf("@")));
		cctp.setRate(null);// 未采集

        cctp.setRateDesc(null);// 未采集
//        if (cctp.getRateDesc() == null || cctp.getRateDesc().isEmpty()) {
//        	getLogger().error("RateDesc is null : rate=" + ctp.getRate());
//		}
//        cctp.setTmRate(SDHUtil.getTMRate(ctp.getRate()));
//        if (cctp.getTmRate() == null || cctp.getTmRate().isEmpty()) {
//			getLogger().error("getTmRate is null : rate=" + ctp.getRate());
//		}
//        SDHUtil.setCTPNumber(cctp);

		cctp.setTpProtectionAssociation(null);// 未采集
		cctp.setParentDn(ctp.getRelatedPortrmUID());
		cctp.setEmsName(emsdn);
		cctp.setUserLabel(ctp.getNativeName());// 未采集
		cctp.setNativeEMSName(ctp.getCtpName());
		cctp.setOwner(null);
		cctp.setAdditionalInfo(null);// 未采集
		cctp.setFrequencies(ctp.getFrequency());
		
//		setParentCtpDn(cctp);

		return cctp;
	}
//	long i = 0;
	protected void setParentCtpDn(CCTP cctp) {
		String relatedPortrmUID = cctp.getParentDn();
		String ctpName = cctp.getNativeEMSName();
		
		if (StringUtils.contains(ctpName, "/")) {
			String parentCtpName = StringUtils.substring(ctpName,0,ctpName.lastIndexOf("/"));
			String sql = "select c from CTP2 c where c.ctpName = '"+parentCtpName+"' and c.relatedPortrmUID = '"+relatedPortrmUID+"'";
			CTP2 ctp = (CTP2) sd.queryOneObject(sql);
			if (null != ctp) {
				cctp.setParentCtpdn(ctp.getRmUID());
//				System.out.println(cctp.getDn()+"found parendCtp!--" + i);
//				i++;
			} else {
				System.out.println(cctp.getDn()+"not found parendCtp!");
			}
		}
	}
	
	
	/**
	 * SPN兼容当前中间表的同步
	 */
	/**
     * 1. 同步网元
     */
	// TODO:SPN兼容当前中间表的同步
    public void migrateManagedElementForSpn() throws Exception {
		if (!isTableHasData(NEL.class)) {
			getLogger().info("NEL is empty, return...");
			return;
		}
		List<NEL> meList = sd.queryAll(NEL.class);
		if (!Detect.notEmpty(meList)) {
			getLogger().info("NEL is empty, return");
			return;
		}

		DataInserter di = new DataInserter(emsid);
		executeDelete("delete  from CDevice c where c.emsName = '" + emsdn + "'", CDevice.class);

		if (meList != null && meList.size() > 0) {
			for (NEL me : meList) {
				CDevice device = transNELForSpn(me);
				device.setSid(DatabaseUtil.nextSID(device));
                if(device.getAdditionalInfo().length() > 2000){
                    device.setAdditionalInfo(null);
                }
				di.insert(device);
			}
		}
		di.end();
	}
    public CDevice transNELForSpn(NEL me) {
        
        CDevice device = new CDevice();
		device.setEmsName(emsdn);
		device.setEmsid(emsid);
		device.setDn(me.getRmUID());//emsdn+"@"+
		device.setLocation(me.getLocation());
		device.setNativeEmsName(me.getNativeName());
		device.setNeVersion(me.getSoftwareVersion());
		device.setProductName(me.getProductName());
		device.setSupportedRates(null);
		device.setUserLabel("managedElement");
		device.setCollectTimepoint(me.getCreateDate());
		device.setIpAddress(me.getIPAddress());

		String additionalInfo = assembleAdditionalInfoForSpn(me);
		device.setAdditionalInfo(additionalInfo);
		
        return device;
    }
    public String assembleAdditionalInfoForSpn(NEL me) {
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
     * 2. 同步子网、子网网元
     */
    public void migrateSubnetworkForSpn() throws Exception {
		if (!isTableHasData(SBN.class)) {
			getLogger().info("SBN is empty, return...");
			return;
		}
		List<SBN> subNets = sd.queryAll(SBN.class);
		if (!Detect.notEmpty(subNets)) {
			getLogger().info("SBN is empty, return");
			return;
		}
			
		DataInserter di = new DataInserter(emsid);
		executeDelete("delete from CSubnetwork c where c.emsName = '" + emsdn + "'", CSubnetwork.class);
		for (SBN subNet : subNets) {
			CSubnetwork cSubnetwork = new CSubnetwork();
			cSubnetwork.setDn(subNet.getRmUID());//emsdn+"@"+
			cSubnetwork.setName(subNet.getNativeName());
			cSubnetwork.setNativeemsname(subNet.getNativeName());
			cSubnetwork.setxPos(subNet.getxPos());
			cSubnetwork.setyPos(subNet.getyPos());
			cSubnetwork.setSid(DatabaseUtil.nextSID(cSubnetwork));
			cSubnetwork.setEmsName(emsdn);
			cSubnetwork.setEmsid(emsid);
			
			String parent = subNet.getParentSubnetrmUID();
			if (parent != null && !parent.trim().isEmpty()) {
				cSubnetwork.setParentSubnetworkDn(parent);
				cSubnetwork.setParentSubnetworkId(DatabaseUtil.getSID(CSubnetwork.class, parent));
			}

			di.insert(cSubnetwork);
		}
		
		di.end();
	}
    public void migrateSubnetworkDeviceForSpn() throws Exception {
    	if (!isTableHasData(SNN.class)) {
			getLogger().info("SNN is empty, return...");
			return;
		}
    	List<SNN> subNetNes = sd.queryAll(SNN.class);
		if (!Detect.notEmpty(subNetNes)) {
			getLogger().info("SNN is empty, return");
			return;
		}
    	
		DataInserter di = new DataInserter(emsid);
		executeDelete("delete from CSubnetworkDevice c where c.emsName = '" + emsdn + "'", CSubnetworkDevice.class);
		
		for (SNN subNetNe : subNetNes) {
			CSubnetworkDevice csd = new CSubnetworkDevice();
			csd.setDn(subNetNe.getDn());//emsdn+"@"+
			csd.setSid(DatabaseUtil.nextSID(csd));
			String parent = subNetNe.getSubnetrmUID();
			csd.setSubnetworkDn(parent);
			if (parent != null)
				csd.setSubnetworkId(DatabaseUtil.getSID(CSubnetwork.class, parent));
			csd.setDeviceDn(subNetNe.getRmUID());
			csd.setxPos(subNetNe.getxPos());
			csd.setyPos(subNetNe.getyPos());
			csd.setEmsName(emsdn);
			csd.setEmsid(emsid);
			
			di.insert(csd);
		}
    	
		di.end();
    }
    
    /**
     * 3. 同步槽道
     */
    public void migrateEquipmentHolderForSpn() throws Exception {
    	if (!isTableHasData(EQH.class)) {
			getLogger().info("EQH is empty, return...");
			return;
		}
    	List<EQH> equipmentHolders = sd.queryAll(EQH.class);
		if (!Detect.notEmpty(equipmentHolders)) {
			getLogger().info("EQH is empty, return");
			return;
		}
		
		if (equipmentHolders != null) {
			executeDelete("delete  from CShelf c where c.emsName = '" + emsdn + "'", CShelf.class);
			executeDelete("delete  from CRack c where c.emsName = '" + emsdn + "'", CRack.class);
			executeDelete("delete  from CSlot c where c.emsName = '" + emsdn + "'", CSlot.class);

			insertEQHsForSpn(equipmentHolders);
		}
	}
    HashMap<String, String> slot_shelfMap = new HashMap<String, String>();
	public void insertEQHsForSpn(List<EQH> equipmentHolders) throws Exception {
		if (shelfTypeMap != null) {
			shelfTypeMap.clear();
			shelfTypeMap = null;
		}
		DataInserter di = new DataInserter(emsid);

		// // ////////////////// 将EQH分类///////////////////
		List<EQH> racks = new ArrayList<EQH>();
		List<EQH> shelfs = new ArrayList<EQH>();
		List<EQH> subshelfs = new ArrayList<EQH>();
		List<EQH> slots = new ArrayList<EQH>();
		List<EQH> subslots = new ArrayList<EQH>();

		for (int i = 0; i < equipmentHolders.size(); i++) {
			EQH equipmentHolder = equipmentHolders.get(i);
			if (equipmentHolder.getHolderType().equals("rack")) {
				racks.add(equipmentHolder);
			} else if (equipmentHolder.getHolderType().equals("shelf")) {
				shelfs.add(equipmentHolder);
			}else if (equipmentHolder.getHolderType().equals("sub_shelf")) {
				subshelfs.add(equipmentHolder);
			}
			else if (equipmentHolder.getHolderType().equals("slot")) {
				slots.add(equipmentHolder);
				slot_shelfMap.put(equipmentHolder.getRmUID(), equipmentHolder.getParentHolderrmUID());
			} else if (equipmentHolder.getHolderType().equals("sub_slot")) {
				subslots.add(equipmentHolder);
			}
		}
		
		// ////////////////// 将EQH分类///////////////////
		for (EQH equipmentHolder : racks) {
			CdcpObject cEquipmentHolder = transNewEQHForSpn(equipmentHolder);
			di.insert(cEquipmentHolder);
		}
		for (EQH equipmentHolder : subshelfs) {
			CdcpObject cEquipmentHolder = transNewEQHForSpn(equipmentHolder);
			di.insert(cEquipmentHolder);
		}
		for (EQH equipmentHolder : shelfs) {
			CdcpObject cEquipmentHolder = transNewEQHForSpn(equipmentHolder);
			di.insert(cEquipmentHolder);
		}

		for (EQH equipmentHolder : slots) {
			CdcpObject cEquipmentHolder = transNewEQHForSpn(equipmentHolder);
			di.insert(cEquipmentHolder);
		}
		for (EQH equipmentHolder : subslots) {
			CdcpObject cEquipmentHolder = transNewEQHForSpn(equipmentHolder);
			di.insert(cEquipmentHolder);
		}
		
		di.end();
		if (slot_shelfMap != null) {
			slot_shelfMap.clear();
			slot_shelfMap = null;
		}
	}
//    HashMap<String,CRack> rackMap = new HashMap<String, CRack>();
    public CdcpObject transNewEQHForSpn(EQH equipmentHolder) {
        CdcpObject cdcpObject = transNewEQH(equipmentHolder);
        if (cdcpObject instanceof CRack) {
            String add = ((CRack) cdcpObject).getAdditionalInfo();
            HashMap<String, String> map = MigrateUtil.transMapValue(add);
            if (map.get("Sequence") != null)
                ((CRack) cdcpObject).setNo(map.get("Sequence"));

//            rackMap.put(cdcpObject.getDn(),(CRack)cdcpObject);
        }
        if (cdcpObject instanceof CSlot) {
            String additionalInfo = assembleEQHAdditionalInfo(equipmentHolder);
            Map<String, String> map = MigrateUtil.transMapValue(additionalInfo);
            ((CSlot) cdcpObject).setNo(equipmentHolder.getHolderNumber()); // 槽道编号取容器序号
            if (equipmentHolder.getHolderNumber() == null) {
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
        }
        if (cdcpObject instanceof CShelf) {
        	CShelf shelf = (CShelf) cdcpObject;
        	shelf.setShelfType(equipmentHolder.getProductName());
            if (!Detect.notEmpty(shelf.getShelfType()) || "--".equals(shelf.getShelfType())) {
            	shelf.setShelfType(getShelfType(shelf.getParentDn())); // 机框型号从设备拷贝
            }
        }
        return cdcpObject;
    }
	protected String getShelfType(String neDN) {
		if (shelfTypeMap == null) {
			shelfTypeMap = new HashMap<String, String>();
			getLogger().info("shelfTypeMap: ");
			List<NEL> nes = sd.queryAll(NEL.class);
			if (nes != null && nes.size() > 0) {
				for (NEL ne : nes) {
					shelfTypeMap.put(ne.getRmUID(), ne.getProductName());
				}
			}
		}
		return shelfTypeMap.get(neDN);
	}
    
    /**
	 * 4. 同步板卡
	 * @throws Exception
	 */
	public void migrateEquipmentForSpn() throws Exception {
		if (!isTableHasData(CRD.class)) {
			getLogger().info("CRD is empty, return...");
			return;
		}
		executeDelete("delete   from CEquipment c where c.emsName = '" + emsdn + "'", CEquipment.class);
		List<CRD> equipments = sd.queryAll(CRD.class);
		insertCRDsForSpn(equipments);
	}
	private void insertCRDsForSpn(List<CRD> equipments) throws Exception {
		DataInserter di = new DataInserter(emsid);
		if (equipments != null && equipments.size() > 0) {
			for (CRD equipment : equipments) {
				CEquipment cEquipment = transNewCRDForSpn(equipment);
                if (cEquipment != null)
				    di.insert(cEquipment);
			}
		}
		di.end();
	}
	public CEquipment transNewCRDForSpn(CRD equipment) {
		CEquipment cEquipment = new CEquipment();
		cEquipment.setCollectTimepoint(equipment.getCreateDate());
		cEquipment.setDn(equipment.getRmUID());
		
		cEquipment.setSlotDn(equipment.getHolderrmUID());
		cEquipment.setParentDn(equipment.getHolderrmUID());
		cEquipment.setSlotId(DatabaseUtil.getSID(CSlot.class, equipment.getHolderrmUID()));
		
		cEquipment.setAdditionalInfo(null);
		cEquipment.setEmsName(emsdn);
		cEquipment.setEmsid(emsid);
		
		cEquipment.setExpectedEquipmentObjectType(null);
		cEquipment.setInstalledEquipmentObjectType(equipment.getCardType());
		cEquipment.setInstalledPartNumber(null);
		cEquipment.setInstalledSerialNumber(equipment.getSerialNumber());
		cEquipment.setInstalledVersion(null);
		
		cEquipment.setNativeEMSName(equipment.getCardType());
		cEquipment.setOwner(null);
		cEquipment.setServiceState(equipment.getServiceState());
		cEquipment.setUserLabel(equipment.getNativeName());

		return cEquipment; // To change body of created methods use File | Settings | File Templates.
	}
	
	/**
	 * 5. 同步端口
	 * @throws Exception
	 */
	public void migratePtpForSpn() throws Exception {
		if (!isTableHasData(PRT.class)) {
			getLogger().info("PRT is empty, return...");
			return;
		}
		executeDelete("delete  from CPTP c where c.emsName = '" + emsdn + "'", CPTP.class);
		executeDelete("delete from CIPAddress c where c.emsName = '" + emsdn + "'", CIPAddress.class);
		List<PRT> ptps = sd.queryAll(PRT.class);
		insertPRTsForSpn(ptps);
	}
	private void insertPRTsForSpn(List<PRT> ptps) throws Exception {
		DataInserter di = new DataInserter(emsid);
		getLogger().info("migratePtp size = " + (ptps == null ? null : ptps.size()));
		List<CPTP> cptps = new ArrayList<CPTP>();
		if (ptps != null && ptps.size() > 0) {
			for (PRT ptp : ptps) {
				CPTP cptp = transPRTForSpn(ptp);
				if (cptp != null) {
					cptps.add(cptp);
				}
			}
		}

		this.removeDuplicateDN(cptps);
		for (int i = 0; i < cptps.size(); i++) {
			CPTP cptp = cptps.get(i);
			di.insert(cptp);
			if (cptp.getIpAddress() != null && !cptp.getIpAddress().isEmpty()) {
				CIPAddress ip = new CIPAddress();
				ip.setDn(SysUtil.nextDN());
				ip.setSid(DatabaseUtil.nextSID(ip));
				ip.setEmsName(emsdn);
				ip.setEmsid(emsid);
				ip.setIpaddress(cptp.getIpAddress());
				ip.setPtpId(DatabaseUtil.getSID(CPTP.class, cptp.getDn()));
				di.insert(ip);
			}
		}
		di.end();
	}
	public CPTP transPRTForSpn(PRT ptp) {
		CPTP cptp = new CPTP();
		cptp.setDn(ptp.getRmUID());
		
		cptp.setCardid(DatabaseUtil.getSID(CEquipment.class, ptp.getCardrmUID()));
		cptp.setDeviceDn(ptp.getNermUID());
		cptp.setParentDn(ptp.getCardrmUID());
		
		cptp.setSid(DatabaseUtil.nextSID(cptp));
		cptp.setCollectTimepoint(ptp.getCreateDate());
		cptp.setEdgePoint(true);//未采集
		cptp.setConnectionState(null);//未采集
		cptp.setTpMappingMode(null);//未采集 "D_NA"
		cptp.setTpProtectionAssociation(null);//未采集 "TPPA_NA"
		cptp.setDirection(DicUtil.getPtpDirection(ptp.getDirection()));
		cptp.setRate(ptp.getPortRate());
//		if (!"NA".equals(ptp.getPortRate())) {
//			cptp.setSpeed(ptp.getPortRate());
//		}
		cptp.setSpeed(DicUtil.getRateWithoutNA(ptp.getPortRate()));
		cptp.setEmsName(emsdn);
		cptp.setUserLabel(null);//未采集 "VMU48_OTN_TP"
		cptp.setNativeEMSName(ptp.getNativeName());
		cptp.setOwner(null);
		cptp.setNo(ptp.getPortNo());
		int eoType = 0;
		if ("electrical".equalsIgnoreCase(ptp.getSignalType())) {
			eoType = DicConst.EOTYPE_ELECTRIC;
			cptp.setEoType(eoType);
		}
		if ("optical".equalsIgnoreCase(ptp.getSignalType())) {
			eoType = DicConst.EOTYPE_OPTIC;
			cptp.setEoType(eoType);
		}
		
		String ptpType = "";
		if ("ftp".equalsIgnoreCase(ptp.getPhysicalOrLogical())) {
			ptpType = "LOGICAL";
		} else {
			if ("electrical".equalsIgnoreCase(ptp.getSignalType())) {
				ptpType = "ELECTRICAL";
			} else if ("optical".equalsIgnoreCase(ptp.getSignalType())) {
				ptpType = "OPTICAL";
			}
		}
		cptp.setType(ptpType);// 根据PhysicalOrLogical和SignalType综合计算
		
		String additionalInfo = "BandParity:||ProtectionRole:||SupportedPortType:" + ptp.getSignalType() + "||";
		cptp.setAdditionalInfo(additionalInfo);
		
		cptp.setTransmissionParams(null);//未采集
		cptp.setPortRate(ptp.getPortRate());
		
		if (!Detect.notEmpty(cptp.getParentDn()) || "--".equals(cptp.getParentDn())) {
			cptp.setParentDn(ptp.getNermUID()); // 端口无板卡直接属于设备时，parentdn取设备dn
		}
		
		// 端口的接口改造，新增15个字段的入库
		cptp.setPhysicalOrLogical(ptp.getPhysicalOrLogical());
		cptp.setSignalType(ptp.getSignalType());
		cptp.setPortType(ptp.getPortType());
		cptp.setPortSubType(ptp.getPortSubType());
		cptp.setRole(ptp.getRole());
		cptp.setIsOverlay(ptp.getIsOverlay());
		cptp.setvNetrmUID(ptp.getvNetrmUID());
		cptp.setMtnGrouprmUID(ptp.getMtnGrouprmUID());
		cptp.setInLaserUpthreshold(ptp.getInLaserUpthreshold());
		cptp.setInLaserDownthreshold(ptp.getInLaserDownthreshold());
		cptp.setOutLaserUpthreshold(ptp.getOutLaserUpthreshold());
		cptp.setOutLaserDownthreshold(ptp.getOutLaserDownthreshold());
		cptp.setIpAddress(ptp.getIPAddress());
		cptp.setIpMask(ptp.getIPMask());
		cptp.setIPV6(ptp.getIPV6());

		return cptp;
	}
	// c_ftp_ptp端口绑定
	public void migrateFtpPtpForSpn() throws Exception {
		if (!isTableHasData(PRB.class)) {
			getLogger().info("PRB is empty, return...");
			return;
		}
		executeDelete("delete from CFTP_PTP c where c.emsName = '" + emsdn + "'", CFTP_PTP.class);
		List<PRB> list = sd.queryAll(PRB.class);
		DataInserter di = new DataInserter(emsid);
		for (int i = 0; i < list.size(); i++) {
			PRB prb = list.get(i);
			CFTP_PTP cftp_ptp = transFtpPtpForSpn(emsdn, prb);
			di.insert(cftp_ptp);
		}
		di.end();
	}
	public CFTP_PTP transFtpPtpForSpn(String emsdn, PRB prb) {
		String ftpDn = prb.getRmUID();
		String ptpDn = prb.getPhyPortrmUID();
		CFTP_PTP cftp_ptp = new CFTP_PTP();
		cftp_ptp.setDn(ftpDn + "<>" + ptpDn);
		cftp_ptp.setEmsName(emsdn);
		cftp_ptp.setFtpDn(ftpDn);
		cftp_ptp.setPtpDn(ptpDn);
		cftp_ptp.setFtpId(DatabaseUtil.getSID(CPTP.class, ftpDn));
		cftp_ptp.setPtpId(DatabaseUtil.getSID(CPTP.class, ptpDn));
//		cftp_ptp.setRate(prb.getRate());
		return cftp_ptp;
	}
	
	/**
     * 6. 同步段
     */
	HashMap<String, List<String>> ptp_sectionMap = new HashMap<String, List<String>>();
    public void migrateSectionForSpn() throws Exception {
    	if (!isTableHasData(TPL.class)){
			getLogger().info("TPL is empty, return...");
			return;
		}
        executeDelete("delete  from CSection c where c.emsName = '" + emsdn + "'", CSection.class);
        DataInserter di = new DataInserter(emsid);
        List<TPL> sections = sd.queryAll(TPL.class);
        if (sections != null && sections.size() > 0) {
            for (TPL section : sections) {
                CSection csection = transNewSectionForSpn(section);
//                String azPtp = section.getaEndPortrmUID()+"-"+section.getzEndPortrmUID();
                DSUtil.putIntoValueList(ptp_sectionMap, section.getaEndPortrmUID(), section.getRmUID());
                DSUtil.putIntoValueList(ptp_sectionMap, section.getzEndPortrmUID(), section.getRmUID());
                di.insert(csection);
            }
        }
        di.end();
    }
    public CSection transNewSectionForSpn(TPL section) {
        CSection csection = new CSection();
        csection.setDn(section.getRmUID());
        csection.setSid(DatabaseUtil.nextSID(csection));
        csection.setCollectTimepoint(section.getCreateDate());
        
        csection.setDirection(DicUtil.getConnectionDirection(section.getDirection()));
        csection.setParentDn(null);// 未采集
        csection.setEmsName(emsdn);
        csection.setEmsid(emsid);
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
     * 7. 同步伪线
     */
    public void migratePwForSpn() throws Exception {
    	if (!isTableHasData(PSW.class)){
			getLogger().info("PSW is empty, return...");
			return;
		}
    	executeDelete("delete from CPW c where c.emsName = '" + emsdn + "'", CPW.class);
        DataInserter di = new DataInserter(emsid);
        List<PSW> pws = sd.queryAll(PSW.class);
        if (pws != null && pws.size() > 0) {
            for (PSW pw : pws) {
            	CPW cpw = transPwForSpn(pw);
                di.insert(cpw);
            }
        }
        di.end();
    }
    public CPW transPwForSpn(PSW pw) {
    	CPW cpw = new CPW();
    	cpw.setDn(pw.getRmUID());
    	cpw.setSid(DatabaseUtil.nextSID(cpw));
    	cpw.setCollectTimepoint(pw.getCreateDate());
    	cpw.setEmsName(emsdn);
    	cpw.setEmsid(emsid);
    	cpw.setNativeEMSName(pw.getNativeName());
    	
    	cpw.setDirection(DicUtil.getConnectionDirection(pw.getDirection()));
    	cpw.setActiveState(pw.getActiveState());
    	cpw.setAend(pw.getaEndTprmUID());
    	cpw.setZend(pw.getzEndTprmUID());
    	if (!"--".equals(pw.getaEndPortrmUID())) {
    		cpw.setAptp(pw.getaEndPortrmUID());
    	}
    	if (!"--".equals(pw.getzEndPortrmUID())) {
    		cpw.setZptp(pw.getzEndPortrmUID());
    	}
		
        if (cpw.getAptp() != null)
		    cpw.setAptpId(DatabaseUtil.getSID(CPTP.class, cpw.getAptp()));
        else {
            getLogger().error("PW:APTP is null: "+pw.getRmUID());
        }
        if (cpw.getZptp() != null)
		    cpw.setZptpId(DatabaseUtil.getSID(CPTP.class, cpw.getZptp()));
        else {
            getLogger().error("PW:ZPTP is null: "+pw.getRmUID());
        }
        
    	String cir = pw.getaEndIngressCIR();
		if (cir != null && !cir.equals("0"))
			cpw.setCir(cir + "M");
		String pir = pw.getaEndIngressPIR();
		if (pir != null && !pir.equals("10000"))
			cpw.setPir(pir + "M");
    	
    	
    	return cpw;
    }
    
	
    /**
     * 8. 同步隧道
     */
    public void migrateTunnelForSpn() throws Exception {
    	if (!isTableHasData(TNL.class)){
			getLogger().info("TNL is empty, return...");
			return;
		}
    	executeDelete("delete from CTunnel c where c.emsName = '" + emsdn + "'", CTunnel.class);
        DataInserter di = new DataInserter(emsid);
        List<TNL> tunnels = sd.queryAll(TNL.class);
        if (tunnels != null && tunnels.size() > 0) {
            for (TNL tunnel : tunnels) {
            	CTunnel ctunnel = transTunnelForSpn(tunnel);
                di.insert(ctunnel);
            }
        }
        di.end();
    }
    public CTunnel transTunnelForSpn(TNL tunnel) {
    	CTunnel ctunnel = new CTunnel();
    	ctunnel.setDn(tunnel.getRmUID());
    	ctunnel.setSid(DatabaseUtil.nextSID(ctunnel));
    	ctunnel.setCollectTimepoint(tunnel.getCreateDate());
    	ctunnel.setEmsName(emsdn);
    	ctunnel.setEmsid(emsid);
    	ctunnel.setNativeEMSName(tunnel.getNativeName());
    	
    	ctunnel.setDirection(DicUtil.getConnectionDirection(tunnel.getDirection()));
    	ctunnel.setActiveState(tunnel.getActiveState());
    	ctunnel.setAend(tunnel.getaEndTprmUID());
    	ctunnel.setZend(tunnel.getzEndTprmUID());
    	ctunnel.setAptp(tunnel.getaEndPortrmUID());
		ctunnel.setZptp(tunnel.getzEndPortrmUID());
        if (ctunnel.getAptp() != null)
		    ctunnel.setAptpId(DatabaseUtil.getSID(CPTP.class, ctunnel.getAptp()));
        else {
            getLogger().error("PW:APTP is null: "+tunnel.getRmUID());
        }
        if (ctunnel.getZptp() != null)
		    ctunnel.setZptpId(DatabaseUtil.getSID(CPTP.class, ctunnel.getZptp()));
        else {
            getLogger().error("PW:ZPTP is null: "+tunnel.getRmUID());
        }
        
        ctunnel.setAingressLabel(tunnel.getaEndRevInLabel());
        ctunnel.setAegressLabel(tunnel.getaEndOutLabel());
        ctunnel.setZingressLabel(tunnel.getzEndInLabel());
        ctunnel.setZegressLabel(tunnel.getzEndRevOutLabel());
        ctunnel.setCir(tunnel.getCIR());
        ctunnel.setPir(tunnel.getPIR());
    	
    	
    	return ctunnel;
    }
    // c_pw_tunnel
    HashMap<String, List<String>> tunnel_pwMap = new HashMap<String, List<String>>();
    public void migratePwTunnelForSpn() throws Exception {
    	if (!isTableHasData(PWT.class)){
			getLogger().info("PWT is empty, return...");
			return;
		}
    	executeDelete("delete from CPW_Tunnel c where c.emsName = '" + emsdn + "'", CPW_Tunnel.class);
        DataInserter di = new DataInserter(emsid);
        List<PWT> pwts = sd.queryAll(PWT.class);
        List<CPW_Tunnel> cpw_tunnels = new ArrayList<CPW_Tunnel>();
        if (pwts != null && pwts.size() > 0) {
            for (PWT pwt : pwts) {
            	CPW_Tunnel cpw_tunnel = transCPW_TunnelForSpn(pwt.getRmUID(), pwt.getTunnelrmUID(), pwt);
            	cpw_tunnels.add(cpw_tunnel);
            	DSUtil.putIntoValueList(tunnel_pwMap, pwt.getTunnelrmUID(), pwt.getRmUID());
            }
        }
        removeDuplicateDN(cpw_tunnels);
        di.insert(cpw_tunnels);
        di.end();
    }
    protected CPW_Tunnel transCPW_TunnelForSpn(String pwRmuid, String tunnelRmuid, PWT pwt) {
		CPW_Tunnel cpw_tunnel = new CPW_Tunnel();
		cpw_tunnel.setDn(pwRmuid + "<>" + tunnelRmuid);
		cpw_tunnel.setSid(DatabaseUtil.nextSID(cpw_tunnel));
		if (pwt != null) {
			cpw_tunnel.setCollectTimepoint(pwt.getCreateDate());
		} else {
			cpw_tunnel.setCollectTimepoint(new Date());
		}
		cpw_tunnel.setEmsName(emsdn);
		cpw_tunnel.setEmsid(emsid);
		
		cpw_tunnel.setPwDn(pwRmuid);
		cpw_tunnel.setPwId(DatabaseUtil.getSID(CPW.class, pwRmuid));
		cpw_tunnel.setTunnelDn(tunnelRmuid);
		cpw_tunnel.setTunnelId(DatabaseUtil.getSID(CTunnel.class, tunnelRmuid));

		return cpw_tunnel;
	}
    // c_protectiongroup
    public void migrateProtectiongroupForSpn() throws Exception {
    	if (!isTableHasData(TPI.class)){
			getLogger().info("TPI is empty, return...");
			return;
		}
    	executeDelete("delete from CProtectionGroup c where c.emsName = '" + emsdn + "'", CProtectionGroup.class);
        DataInserter di = new DataInserter(emsid);
        List<TPI> tpis = sd.queryAll(TPI.class);
        List<CProtectionGroup> cpgs = new ArrayList<CProtectionGroup>();
        if (tpis != null && tpis.size() > 0) {
            for (TPI tpi : tpis) {
            	CProtectionGroup cpg = transProtectionGroupForSpn(tpi);
            	cpgs.add(cpg);
            }
        }
        removeDuplicateDN(cpgs);
        di.insert(cpgs);
        di.end();
    }
    private CProtectionGroup transProtectionGroupForSpn(TPI tpi) {
		CProtectionGroup cpg = new CProtectionGroup();
		cpg.setDn(tpi.getRmUID());
		cpg.setSid(DatabaseUtil.nextSID(cpg));
		cpg.setEmsName(emsdn);
		
		cpg.setNativeEMSName(tpi.getNativeName());
		cpg.setProtectionGroupType(tpi.getType());
		cpg.setReversionMode(tpi.getReversionMode());
		return cpg;
	}
    // c_protectiongroup_tunnel
    HashMap<String, List<String>> tpi_tunnelMap = new HashMap<String, List<String>>();
    public void migrateProtectiongroupTunnelForSpn() throws Exception {
    	if (!isTableHasData(TPB.class)){
			getLogger().info("TPB is empty, return...");
			return;
		}
		executeDelete("delete from CProtectionGroupTunnel c where c.emsName = '" + emsdn + "'", CProtectionGroupTunnel.class);
        DataInserter di = new DataInserter(emsid);
        List<TPB> tpbs = sd.queryAll(TPB.class);
        if (tpbs != null && tpbs.size() > 0) {
            for (TPB tpb : tpbs) {
            	CProtectionGroupTunnel cProtectionGroupTunnel = new CProtectionGroupTunnel();
				cProtectionGroupTunnel.setDn(tpb.getRmUID() + "<>" + tpb.getTunnelrmUID());
				// cProtectionGroupTunnel.setSid(DatabaseUtil.nextSID(cProtectionGroupTunnel));
				cProtectionGroupTunnel.setProtectGroupId(DatabaseUtil.getSID(CProtectionGroup.class, tpb.getRmUID()));
				cProtectionGroupTunnel.setProtectGroupDn(tpb.getRmUID());
				cProtectionGroupTunnel.setTunnelDn(tpb.getTunnelrmUID());
				cProtectionGroupTunnel.setTunnelId(DatabaseUtil.getSID(CTunnel.class, tpb.getTunnelrmUID()));
				cProtectionGroupTunnel.setEmsName(emsdn);
				if ("Master".equalsIgnoreCase(tpb.getRole())) {
					cProtectionGroupTunnel.setStatus("PROTECTED");
				} else if ("Backup".equalsIgnoreCase(tpb.getRole())) {
					cProtectionGroupTunnel.setStatus("PROTECTING");
				} else {
					cProtectionGroupTunnel.setStatus(tpb.getRole());
				}
				
				DSUtil.putIntoValueList(tpi_tunnelMap, tpb.getRmUID(), tpb.getTunnelrmUID());
				
				// 错误的pg导致DN重复
				if (!DatabaseUtil.isSIDExisted(CProtectionGroupTunnel.class, cProtectionGroupTunnel.getDn())) {
					di.insert(cProtectionGroupTunnel);
				}
            }
        }
        
        if (Detect.notEmpty(tpi_tunnelMap) && Detect.notEmpty(tunnel_pwMap)) {
        	Set<String> tpis = tpi_tunnelMap.keySet();
			for (String tpi : tpis) {
				List<String> tunnels = tpi_tunnelMap.get(tpi);
				if (Detect.notEmpty(tunnels) && tunnels.size() > 1) {
					if (Detect.notEmpty(tunnel_pwMap.get(tunnels.get(0))) && !Detect.notEmpty(tunnel_pwMap.get(tunnels.get(1)))) {
						for (String pwDn : tunnel_pwMap.get(tunnels.get(0))) {
							CPW_Tunnel cpw_tunnel = transCPW_TunnelForSpn(pwDn, tunnels.get(1), null);
							// DN重复
							if (!DatabaseUtil.isSIDExisted(CPW_Tunnel.class, cpw_tunnel.getDn())) {
								di.insert(cpw_tunnel);
							}
						}
					} else if (Detect.notEmpty(tunnel_pwMap.get(tunnels.get(1))) && !Detect.notEmpty(tunnel_pwMap.get(tunnels.get(0)))) {
						for (String pwDn : tunnel_pwMap.get(tunnels.get(1))) {
							CPW_Tunnel cpw_tunnel = transCPW_TunnelForSpn(pwDn, tunnels.get(0), null);
							// DN重复
							if (!DatabaseUtil.isSIDExisted(CPW_Tunnel.class, cpw_tunnel.getDn())) {
								di.insert(cpw_tunnel);
							}
						}
					} else {
						getLogger().error("both tunnel have pw : " + tunnels.get(0));
					}
				}
			}
        } else {
        	getLogger().error("tpi_tunnelMap or tunnel_pwMap is null");
        }
        
        di.end();
        if (tpi_tunnelMap != null) {
        	tpi_tunnelMap.clear();
            tpi_tunnelMap = null;
        }
        if (tunnel_pwMap != null) {
        	tunnel_pwMap.clear();
            tunnel_pwMap = null;
        }
    }
    // c_tunnel_section
    public void migrateTunnelSectionForSpn() throws Exception {
    	if (!isTableHasData(LBS.class)){
			getLogger().info("LBS is empty, return...");
			return;
		}
    	executeDelete("delete  from CTunnel_Section c where c.emsName = '" + emsdn + "'", CTunnel_Section.class);
    	DataInserter di = new DataInserter(emsid);
    	List<LBS> lbss = sd.queryAll(LBS.class);
        if (lbss != null && lbss.size() > 0) {
            for (LBS lbs : lbss) {
            	List<String> aendTPL = ptp_sectionMap.get(lbs.getaEndPortrmUID());
            	List<String> zendTPL = ptp_sectionMap.get(lbs.getzEndPortrmUID());
            	
            	if (Detect.notEmpty(aendTPL)) {
            		if (!Detect.onlyOne(aendTPL)) {
                		getLogger().error("aendTPL.size>1-" + lbs.getaEndPortrmUID());
                		System.out.println("aendTPL.size>1-" + lbs.getaEndPortrmUID());
                	}
            		for (String sectionDn : aendTPL) {
                		CTunnel_Section cts = transTunnelSectionForSpn(lbs.getTunnelrmUID(), sectionDn);
        				if (!DatabaseUtil.isSIDExisted(CTunnel_Section.class, cts.getDn())) // section+tunnel 可能有重复
        					di.insert(cts);
            		}
            	}
            	if (Detect.notEmpty(zendTPL)) {
            		if (!Detect.onlyOne(zendTPL)) {
                		getLogger().error("zendTPL.size>1-" + lbs.getzEndPortrmUID());
                		System.out.println("zendTPL.size>1-" + lbs.getzEndPortrmUID());
                	}
            		for (String sectionDn : zendTPL) {
                		CTunnel_Section cts = transTunnelSectionForSpn(lbs.getTunnelrmUID(), sectionDn);
        				if (!DatabaseUtil.isSIDExisted(CTunnel_Section.class, cts.getDn())) // section+tunnel 可能有重复
        					di.insert(cts);
            		}
            	}
            }
        }
        di.end();
        if (ptp_sectionMap != null) {
        	ptp_sectionMap.clear();
        	ptp_sectionMap = null;
		}
    }
    private CTunnel_Section transTunnelSectionForSpn(String tunnelDn, String sectionDn) {
    	CTunnel_Section ts = new CTunnel_Section();
		ts.setDn(tunnelDn + "<>" + sectionDn);
		ts.setEmsName(emsdn);
		ts.setTunnelDn(tunnelDn);
		ts.setTunnelId(DatabaseUtil.getSID(CTunnel.class, tunnelDn));
		ts.setSectionDn(sectionDn);
		ts.setSectionId(DatabaseUtil.getSID(CSection.class, sectionDn));
		
		return ts;
    }
    
	
}
