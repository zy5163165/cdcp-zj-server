package com.alcatelsbell.cdcp.server.adapters;

import com.alcatelsbell.cdcp.nbi.model.CChannel;
import com.alcatelsbell.cdcp.nbi.model.CCrossConnect;
import com.alcatelsbell.cdcp.nbi.model.CPTP;
import com.alcatelsbell.cdcp.nbi.model.CSection;
import com.alcatelsbell.cdcp.server.adapters.fenghuootnm2000.FHDic;
import com.alcatelsbell.cdcp.server.adapters.huaweiu2000.HwDwdmUtil;
import com.alcatelsbell.cdcp.server.adapters.huaweiu2000.U2000MigratorUtil;
import com.alcatelsbell.cdcp.util.DSUtil;
import com.alcatelsbell.cdcp.util.DicConst;
import com.alcatelsbell.cdcp.util.SqliteDelegation;
import com.alcatelsbell.nms.db.components.service.JPASupportFactory;
import org.apache.log4j.Logger;
import org.asb.mule.probe.framework.entity.CrossConnect;
import org.asb.mule.probe.framework.entity.PTP;
import org.asb.mule.probe.framework.entity.Section;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Author: Ronnie.Chen
 * Date: 14-7-17
 * Time: 上午10:34
 * rongrong.chen@alcatel-sbell.com.cn
 */
public class PathFindAlgorithmForFH {
    private Logger logger = null;
//    private List<CSection> cSections = null;

    private HashMap<String,List<CCrossConnect>> ptpCCMap = null;
    private HashMap<String,List<CSection>> ptpSectionMap = null;
    private HashMap<String,CPTP> ptpMap = null;
    List<String> processedCC = new ArrayList<String>();
    List<String> processedSection = new ArrayList<String>();

    private String startPtp;
    public List<String> endPtp = new ArrayList<String>();
    private List<String> startCtp = new ArrayList<String>();
    private String endCtp;

//    private String oeType; // 奇偶波类型，取值 ODD/EVEN

    private List<CChannel> channelList = new ArrayList<CChannel>();

    public List<FindStack> findStacks = new ArrayList<FindStack>();


//    public void find() {
//
//        for (CSection cSection : cSections) {
//            String aptpDn = cSection.getAendTp();
//            String zptpDn = cSection.getZendTp();
//            CPTP aptp = ptpMap.get(aptpDn);
//            CPTP zptp = ptpMap.get(zptpDn);
//
//
//            if (isTerminalTP(aptp)) {
//
//            }
//        }
//    }

    private HashSet<String> processedPtp = new HashSet<String>();
    public PathFindAlgorithmForFH(Logger logger, HashMap<String, List<CCrossConnect>> ptpCCMap,
                             HashMap<String, List<CSection>> ptpSectionMap, HashMap<String, CPTP> ptpMap) {
        this.logger = logger;
        this.ptpCCMap = ptpCCMap;
        this.ptpSectionMap = ptpSectionMap;
        this.ptpMap = ptpMap;
    }

    public void findSingleDirection(CSection cSection,String startPTP) {
        processedSection.add(cSection.getDn());
        processedPtp.add(startPTP);
        findNextCC(startPTP, new FindStack(cSection), getDirection(startPTP),null);
    }
    private String getCCKey(CCrossConnect cc) {
        return  cc.getAend() +"_"+cc.getZend();
    }
    private void findNextCC(String ptp,FindStack stackKey,String direction,CCrossConnect lastCC) {
        List<CCrossConnect> ccs = ptpCCMap.get(ptp);
        if (ccs != null && ccs.size() > 0) {
            for (int i = 0; i < ccs.size(); i++) {
                CCrossConnect cc = ccs.get(i);
                if (lastCC != null) {
                    if (lastCC.getAend().equals(cc.getZend()) && lastCC.getZend().equals(cc.getAend()))
                        continue;
                }
                 if (!processedCC.contains(getCCKey(cc))) {
                     processedCC.add(getCCKey(cc));
                     String ccAnotherSidePtp = anotherSidePtp(cc, ptp);

                     processedPtp.add(ccAnotherSidePtp);

                     String n_direction = getDirection(ccAnotherSidePtp);
                     if (n_direction != null && direction != null && !n_direction.equals(direction))
                         continue;
                     if (n_direction != null)
                         direction = n_direction;

                     if (isTerminalTP(ccAnotherSidePtp)) {
                         log("!!!找到终结点:" + ccAnotherSidePtp);
                         log("stack="+stackKey+"["+n_direction+"]");
                         log("===================================================");
                         endPtp.add(ccAnotherSidePtp);
                         endCtp = anotherSideCtp(cc,ptp);
                         findStacks.add(new FindStack(cc,stackKey));
                         return;
                     }
                     else {
                         try {
                             findNextSection(ccAnotherSidePtp,new FindStack(cc,stackKey), direction);
                         } catch (NextRouteNotFoundException e) {
                             findNextCC(ccAnotherSidePtp,new FindStack(cc,stackKey),direction,cc);
                         }
                     }
                 }
            }
        }





    }

    private void findNextSection(String ptp,FindStack stackKey,String direction) throws NextRouteNotFoundException {
        List<CSection> css = ptpSectionMap.get(ptp);
        if (css != null && css.size() > 0) {
            for (int i = 0; i < css.size(); i++) {
                CSection cs  = css.get(i);
                if (!processedSection.contains(cs.getDn())) {
                    processedSection.add(cs.getDn());
                    String sectionAnothersidePtp = anotherSidePtp(cs, ptp);
                    processedPtp.add(sectionAnothersidePtp);

                    String n_direction = getDirection(sectionAnothersidePtp);
                    if (n_direction != null && direction != null && !isMatch(direction, n_direction))
                        continue;
                    if (n_direction != null) 
                    	direction = n_direction;
                    if (isTerminalTP(sectionAnothersidePtp)) {
                        log("!!!找到终结点:" + sectionAnothersidePtp);
                        log("stack="+stackKey);
                        log("===================================================");
                        endPtp.add(sectionAnothersidePtp);
                        findStacks.add(new FindStack(cs,stackKey));
                        return;
                    }
                    else {
                        findNextCC(sectionAnothersidePtp,new FindStack(cs,stackKey),direction,null);
                    }
                }
            }
        } else {
            throw new NextRouteNotFoundException(ptp);
         //   logger.error("无法找到端口关联的段：EMS:HZ-U2000-3-P@ManagedElement:4063286@PTP:/rack=1/shelf=3145803/slot=13/domain=wdm/port=5");
        }
    }

    private boolean isMatch (String direction, String n_direction) {
    	if (direction == "D_SOURCE") {
    		return n_direction == "D_SINK";
    	}
    	if (direction == "D_SINK") {
    		return n_direction == "D_SOURCE";
    	}
    	if (direction == "D_BIDIRECTIONAL") {
    		return n_direction == "D_BIDIRECTIONAL";
    	}
    	
    	return false;
    }

    private void log(String info) {
      //  System.out.println(info);
    }

    private String anotherSidePtp(CCrossConnect cc,String ptp) {

       String another =  cc.getAptp().equals(ptp) ? cc.getZptp() : cc.getAptp();
  //      log("  ---CC:" + cc.getId() + "---  " + another);
        return another;
    }

    private String anotherSideCtp(CCrossConnect cc,String ptp) {

        String antoher = cc.getAptp().equals(ptp) ? cc.getZend() : cc.getAend();
 //       log("  ---CC:" + cc.getId() + "---  " + antoher);
        return antoher;
    }

    private String anotherSidePtp(CSection cc,String ptp) {

        String another = cc.getAendTp().equals(ptp) ? cc.getZendTp() : cc.getAendTp();
  //      log("  ---SECTION" + cc.getId() + "---  " + another);
        return another;
    }


    /**
     * 检查当前计算到的路由奇偶波，返回false表示当前路由已经和起始点的奇偶波不一致，需要剔除。
     * @param ptp
     * @return
     */
    protected boolean checkOEPort(CPTP ptp) {
//        String nativeEMSName = ptp.getNativeEMSName();
//        if (nativeEMSName != null) {
//            if (nativeEMSName.contains("RE/TE")) {
//                if (oeType == null)
//                    oeType = "EVEN";
//                else {
//                    return oeType.equals("EVEN");
//                }
//            } else if (nativeEMSName.contains("RO/TO")) {
//                if (oeType == null)
//                    oeType = "ODD";
//                else
//                    return oeType.equals("ODD");
//
//            }
//        }
        return true;
    }

    protected String getDirection(CPTP ptp) {
        if (ptp == null) return null;
        int direction = ptp.getDirection();
        if (direction == DicConst.PTP_DIRECTION_SOURCE) {
            return "D_SOURCE";
        }
        if (direction == DicConst.PTP_DIRECTION_SINK) {
            return "D_SINK";
        }
        if (direction == DicConst.PTP_DIRECTION_BIDIRECTIONAL) {
            return "D_BIDIRECTIONAL";
        }
        return null;
    }

    protected boolean checkOEPort(String ptp) {
        CPTP cptp = ptpMap.get(ptp);
        return checkOEPort(cptp);
    }

    protected String getDirection(String ptp) {
        CPTP cptp = ptpMap.get(ptp);
        if (cptp == null) {
            logger.error("Failed to find ptp :"+ptp);
        }
        return getDirection(cptp);
    }


    protected boolean isTerminalTP(CPTP ptp) {
        if (ptp == null) return false;

        return isOMSRate(ptp.getRate());
    }
    
    public static boolean isOMSRate(String rate) {
        if (rate == null) return false;
        return rate.equals(FHDic.LR_Optical_Transmission_Section.value+"")
                || rate.startsWith(FHDic.LR_Optical_Transmission_Section.value + "||")
                || rate.endsWith("||" + FHDic.LR_Optical_Transmission_Section.value)
                || rate.contains("||" + FHDic.LR_Optical_Transmission_Section.value +"||")
                || rate.equals(FHDic.LR_PHYSICAL_OPTICAL.value + "")
                || rate.startsWith(FHDic.LR_PHYSICAL_OPTICAL.value + "||")
                || rate.endsWith("||" + FHDic.LR_PHYSICAL_OPTICAL.value)
                || rate.contains("||" + FHDic.LR_PHYSICAL_OPTICAL.value +"||");
    }

    protected boolean isTerminalTP(String ptp) {
        CPTP cptp = ptpMap.get(ptp);
        return isTerminalTP(cptp);
    }

    class NextRouteNotFoundException extends Exception {
        public NextRouteNotFoundException(String ptp) {
            super("next route not found : "+ptp);
        }
    }

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(PathFindAlgorithmForFH.class);
        String[] locations = { "appserver-spring-sdh.xml" };
        ApplicationContext ctx = new ClassPathXmlApplicationContext(locations);

        SqliteDelegation sd = new SqliteDelegation(JPASupportFactory.createSqliteJPASupport
                ("d:\\cdcpdb\\WDM_HW_HZ\\2014-07-16-160324-HZ-U2000-3-P-DayMigration.db"));
        List<PTP> ptps = sd.queryAll(PTP.class);
        List<CrossConnect> ccs = sd.queryAll(CrossConnect.class);
        List<Section> sections = sd.queryAll(Section.class);


        HashMap<String,CPTP>  ptpMap = new HashMap<String, CPTP>();
        HashMap<String,List<CCrossConnect>> ptpCCMap = new HashMap<String, List<CCrossConnect>>();
        HashMap<String,List<CSection>> ptpSectionMap = new HashMap<String, List<CSection>>();


        for (PTP ptp : ptps) {
            ptpMap.put(ptp.getDn(), U2000MigratorUtil.transPTP(ptp));
        }

        for (CrossConnect cc : ccs) {
            if (cc.getaEndTP().equals(cc.getzEndTP()))
                System.out.println("cc = " + cc);
            List<CCrossConnect> nccs = U2000MigratorUtil.transCCS(cc, null);
            for (CCrossConnect ncc : nccs) {
                DSUtil.putIntoValueList(ptpCCMap,ncc.getAptp(),ncc);
            }
//            List<String> aends = DNUtil.merge(cc.getaEndNameList().split(Constant.listSplitReg));
//            List<String> zends = DNUtil.merge(cc.getzEndNameList().split(Constant.listSplitReg));
//            for (String aend : aends) {
//                for (String zend : zends) {
//                    CCrossConnect ncc = U2000MigratorUtil.transCCS(cc, aend, zend);
//                    ncc.setId(cc.getId());
//                    DSUtil.putIntoValueList(ptpCCMap,ncc.getAptp(),ncc);
////                    DSUtil.putIntoValueList(ptpCCMap,ncc.getZptp(),ncc);
//                }
//            }
        }
        List<CSection> cSections = new ArrayList<CSection>();
        for (Section section : sections) {
            CSection cSection = U2000MigratorUtil.transSection(section);
            cSection.setId(section.getId());
            cSections.add(cSection);
            DSUtil.putIntoValueList(ptpSectionMap,cSection.getAendTp(),cSection);
//            DSUtil.putIntoValueList(ptpSectionMap,cSection.getZendTp(),cSection);
        }



        for (CSection cSection : cSections) {
            String aendTp = cSection.getAendTp();
            if (aendTp.equals("EMS:HZ-U2000-3-P@ManagedElement:4063255@PTP:/rack=1/shelf=3145753/slot=1/domain=wdm/port=1"))
                System.out.println("aendTp = " + aendTp);
            CPTP aptp = ptpMap.get(aendTp);
            if (HwDwdmUtil.isOMSRate(aptp.getRate())) {
                PathFindAlgorithmForFH pathFindAlgorithm = new PathFindAlgorithmForFH(logger, ptpCCMap, ptpSectionMap, ptpMap);
                System.out.println("startPtp="+aptp.getDn());
                pathFindAlgorithm.findSingleDirection(cSection,cSection.getZendTp());
                System.out.println("startPtp=" + aptp.getDn());
                System.out.println("endPtp=" + pathFindAlgorithm.endPtp+" size="+pathFindAlgorithm.endPtp.size());
            }
        }

    }

    public class FindStack {
        public FindStack(Object route) {
            ccAndSections.add(route);
        }

        public FindStack(Object route,FindStack parent) {
            ccAndSections.addAll(parent.ccAndSections);
            ccAndSections.add(route);
        }
        public List ccAndSections = new ArrayList();
    }


}

