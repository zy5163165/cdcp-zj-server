package com.alcatelsbell.cdcp.server.adapters.zte;

import java.util.List;

import com.alcatelsbell.cdcp.util.DicConst;
import com.alcatelsbell.cdcp.util.DicUtil;

/**
 * Author: Ronnie.Chen
 * Date: 2014/12/8
 * Time: 13:42
 * rongrong.chen@alcatel-sbell.com.cn
 */
public class ZTEDicUtil {
    public static Integer getEOType(String layerRates) {
        // if (layerRates == null)
        // return DicConst.EOTYPE_UNKNOWN;
        try {
            List<Integer> list = DicUtil.convertLayerRateList(layerRates);
            for (int rate : list) {
                if (rate == DicConst.LR_PHYSICAL_ELECTRICAL
                        ||rate == 4166 ||  rate == 4165
                        ||rate == 4208       ||  rate == 5  ||  rate == 6  ||  rate == 7  ||  rate == 8

                        ) {
                    return DicConst.EOTYPE_ELECTRIC;
                } else if (rate == DicConst.LR_PHYSICAL_OPTICAL
                        || rate == DicConst.LR_Optical_Channel
                        || rate == DicConst.LR_Optical_Multiplex_Section
                        || rate == DicConst.LR_Optical_Transmission_Section
                        || rate == 4192 || rate == 1535 || rate == 1533 || rate == 1500
                        || rate == 1
                        || rate == 4177
                        || rate == 4178
                        ) {
                    return DicConst.EOTYPE_OPTIC;
                }
            }

            // for (int i = 0; i < list.length; i++) {
            // if (list[i] == DicConst.LR_PHYSICAL_ELECTRICAL || list[i] == DicConst.LR_DSR_Fast_Ethernet || list[i] == DicConst.LR_Ethernet
            // // || list[i] == DicConst.LR_DS0_64K || list[i] == DicConst.LR_DS0_64K
            // //
            // // || list[i] == DicConst.LR_DS0_64K
            // // || list[i] == DicConst.LR_DSR_2M
            // // // || list[i] == DicConst.LR_128K
            // // // || list[i] == DicConst.LAYERRATE_LR_E0
            // // || list[i] == DicConst.LR_E1_2M || list[i] == DicConst.LR_E2_8M || list[i] == DicConst.LR_E3_34M || list[i] == DicConst.LR_E4_140M
            // // || list[i] == DicConst.LR_E5_565M
            // ) {
            // return DicConst.EOTYPE_ELECTRIC;
            // }
            // if (list[i] == DicConst.LR_PHYSICAL_OPTICAL
            // // || list[i] == DicConst.LR_DSR_Gigabit_Ethernet
            // // || list[i] == DicConst.LR_Section_OC48_STS48_and_RS_STM16 || list[i] == DicConst.LR_Section_OC3_STS3_and_RS_STM1
            // // || list[i] == DicConst.LR_Section_OC48_STS48_and_RS_STM16 || list[i] == DicConst.LR_Section_OC48_STS48_and_RS_STM16
            // // || list[i] == DicConst.LR_Section_OC48_STS48_and_RS_STM16 || list[i] == DicConst.LR_DSR_10Gigabit_Ethernet
            // // || list[i] == DicConst.LR_OCH_Data_Unit_1 || list[i] == DicConst.LR_DSR_Gigabit_Ethernet || list[i] == DicConst.LR_DSR_Gigabit_Ethernet
            // )
            //
            // //
            // // //|| list[i] == SysConst.LAYERRATE_LR_RS_STM_1
            // // //|| list[i] == SysConst.LAYERRATE_LR_RS_STM_4
            // // || list[i] == DicConst.LAYERRATE_LR_RS_STM_16
            // // || list[i] == DicConst.LAYERRATE_LR_RS_STM_64
            // // // || list[i] == SysConst.LAYERRATE_LR_MS_STM_1
            // // // || list[i] == SysConst.LAYERRATE_LR_MS_STM_4
            // // || list[i] == DicConst.LAYERRATE_LR_MS_STM_16
            // // || list[i] == DicConst.LAYERRATE_LR_MS_STM_64)
            // {
            // return DicConst.EOTYPE_OPTIC;
            // }
            // }
            // System.out.println(
            // "Neight 46 nor 47 in list, can't identify the eotype");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if (!unkownLRs.contains(layerRates)) {
        // LogUtil.error(DicUtil.class, "!!!!!!!!!!!!!!!!!! Unknow layrate : " + layerRates);
        // unkownLRs.add(layerRates);
        // }
        return DicConst.EOTYPE_UNKNOWN;
    }


    public static String getSpeed(String layerRates) {
        List<Integer> list = DicUtil.convertLayerRateList(layerRates);
        for (int rate : list) {
            if (rate == DicConst.LR_PHYSICAL_OPTICAL || rate == DicConst.LR_OPTICAL_SECTION|| rate == DicConst.LR_PHYSICAL_OPTICAL )
                continue;
            String speedByRate = getSpeedByRate(rate);
            if (speedByRate != null)
                return speedByRate;
        }
        return "40G";
    }

    public static String getPtpType(String layerRates) {
        String type = "OPTICAL";
        try {
            List<Integer> list = DicUtil.convertLayerRateList(layerRates);
            if (
                    list.contains(4165)
                    || list.contains(4166)) {
                return "LOGICAL";
            }
            if (list.contains(DicConst.LR_E1_2M) || list.contains(DicConst.LR_DSR_2M)) {
                type = "E1";
            } else if (list.contains(DicConst.LR_DSR_Fast_Ethernet)) {
                type = "FE";
            } else if (list.contains(DicConst.LR_DSR_Gigabit_Ethernet) || list.contains(DicConst.LR_DSR_10Gigabit_Ethernet_LAN)
                    || list.contains(DicConst.LR_DSR_10Gigabit_Ethernet)) {
                type = "GE";
            } else if (list.contains(DicConst.LR_Ethernet)) {
                type = "ETH";
            } else if (list.contains(DicConst.LR_PHYSICAL_ELECTRICAL)

                    || list.contains(4208)



                    ) {
                type = "ELECTRICAL";
            } else if (list.contains(DicConst.LR_PHYSICAL_OPTICAL) || list.contains(DicConst.LR_DSR_OC3_STM1) || list.contains(DicConst.LR_DSR_OC12_STM4)
                    || list.contains(DicConst.LR_DSR_OC48_and_STM16)
                    || list.contains(DicConst.LR_OCH_Data_Unit_1)
                    || list.contains(DicConst.LR_OCH_Data_Unit_2)
                    || list.contains(DicConst.LR_OCH_Data_Unit_3)
                    || list.contains(DicConst.LR_OCH_Transport_Unit_1)
                    || list.contains(DicConst.LR_OCH_Transport_Unit_2)
                    || list.contains(DicConst.LR_OCH_Transport_Unit_3)
                    || list.contains(4177)
                    || list.contains(4178)

                    ) {
                type = "OPTICAL";
            } else if (list.contains(DicConst.LR_ATM_NI)) {
                type = "IMA";
            } else if (list.contains(DicConst.LR_LAG_Fragment)) {
                type = "LAG";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }



    public static String getSpeedByRate(int r) {
        String rate = null;
        if (r == DicConst.LR_DSR_Gigabit_Ethernet)
            return "1G";
        if (r == DicConst.LR_DSR_Fast_Ethernet)
            return "100M";
        if (r == DicConst.LR_DSR_10Gigabit_Ethernet_LAN || r == DicConst.LR_DSR_10Gigabit_Ethernet_WAN)
            return "10G";
        if (r == DicConst.LR_E1_2M) {
            return "2M";
        }
        if (r == DicConst.LR_E2_8M) {
            return "8M";
        }
        if (r == DicConst.LR_E3_34M) {
            return "34M";
        }
        if (r == DicConst.LR_E4_140M || r == DicConst.LR_STS3c_and_AU4_VC4) {
            return "155M";
        }
        if (r == DicConst.LR_E5_565M) {
            return "565M";
        }

        if (r == DicConst.LR_Section_OC48_STS48_and_RS_STM16) {
            return "2.5G";
        }

        if (r == DicConst.LR_Section_OC192_STS192_and_RS_STM64) {
            return "10G";
        }

        //
        if (r == DicConst.LR_DSR_2M) {
            return "2M";
        }
        if (r == DicConst.LR_DSR_OC3_STM1 || r == DicConst.LR_Section_OC3_STS3_and_RS_STM1 || r == DicConst.LR_Line_OC3_STS3_and_MS_STM1
                || r == DicConst.LR_STS3c_and_AU4_VC4) {
            return "155M";
        }
        if (r == DicConst.LR_DSR_OC12_STM4) {
            return "622M";
        }

        if (r == DicConst.LR_DSR_OC24_STM8) {
            return "1.5G";
        }
        if (r == DicConst.LR_DSR_OC192_and_STM64) {
            return "10G";
        }


        if (r == DicConst.LR_Section_OC12_STS12_and_RS_STM4) {
            return "622M";
        }
        if (r == DicConst.LR_DSR_OC48_and_STM16) {
            return "2.5G";
        }
        if (r == DicConst.LR_DSR_10Gigabit_Ethernet) {
            return "10G";
        }

        if (r == DicConst.LR_OCH_Data_Unit_0) {
            return "1.25G";
        }
        if (r == DicConst.LR_OCH_Data_Unit_1) {
            return "2.5G";
        }
        if (r == DicConst.LR_OCH_Data_Unit_2) {
            return "10G";
        }
        if (r == DicConst.LR_OCH_Data_Unit_3) {
            return "40G";
        }
        if (r == DicConst.LR_OCH_Transport_Unit_1) {
            return "2.5G";
        }
        if (r == DicConst.LR_OCH_Transport_Unit_2) {
            return "10G";
        }
        if (r == DicConst.LR_OCH_Transport_Unit_3) {
            return "40G";
        }

        if (r == 4165 || r == 4166 || r == 4208)
            return "1G";


        return rate;
    }
    
    public static String getSpnPortSubType(String r) {
    	if ("1".equals(r))  return "Eth_U";
    	if ("2".equals(r))  return "Eth_N";
    	if ("3".equals(r))  return "RPR";
    	if ("4".equals(r))  return "RPRSpan";
    	if ("5".equals(r))  return "Eth_MPLS";
    	if ("6".equals(r))  return "VCG_Eos";
    	if ("7".equals(r))  return "VCG_RprSpan";
    	if ("8".equals(r))  return "VCG_EthMpls";
    	if ("9".equals(r))  return "VCG_GfpMpls";
    	if ("10".equals(r))  return "VCG_RpsMpls";
    	if ("11".equals(r))  return "Eth_Emu";
    	if ("12".equals(r))  return "VCG";
    	if ("13".equals(r))  return "Eth_Inn";
    	if ("14".equals(r))  return "UNI";
    	if ("15".equals(r))  return "NNI";
    	if ("30".equals(r))  return "SFUsr";
    	if ("31".equals(r))  return "CTUsr";
    	if ("50".equals(r))  return "MBCC";
    	if ("51".equals(r))  return "VC2FTP";
    	if ("52".equals(r))  return "Eth";
    	if ("53".equals(r))  return "VC4FTP";
    	if ("101".equals(r))  return "STM1";
    	if ("102".equals(r))  return "STM4";
    	if ("103".equals(r))  return "STM16";
    	if ("104".equals(r))  return "STM64";
    	if ("105".equals(r))  return "STM256";
    	if ("106".equals(r))  return "STMx";
    	if ("107".equals(r))  return "STM1e";
    	if ("108".equals(r))  return "OTUP";
    	if ("109".equals(r))  return "optE1";
    	if ("120".equals(r))  return "AtmSTM1UNI";
    	if ("121".equals(r))  return "OA";
    	if ("122".equals(r))  return "AtmNNI";
    	if ("123".equals(r))  return "UNKN";
    	if ("124".equals(r))  return "AG";
    	if ("125".equals(r))  return "PW";
    	if ("126".equals(r))  return "TM-UNI";
    	if ("127".equals(r))  return "TM-NNI";
    	if ("128".equals(r))  return "TM-BG";
    	if ("129".equals(r))  return "PBT-BG";
    	if ("131".equals(r))  return "VCGGFP";
    	if ("132".equals(r))  return "WAN";
    	if ("133".equals(r))  return "Tunnel";
    	if ("134".equals(r))  return "GRE";
    	if ("151".equals(r))  return "T1";
    	if ("152".equals(r))  return "E1";
    	if ("153".equals(r))  return "E2";
    	if ("154".equals(r))  return "E3";
    	if ("155".equals(r))  return "T3";
    	if ("156".equals(r))  return "E4";
    	if ("157".equals(r))  return "ETx";
    	if ("158".equals(r))  return "FAN";
    	if ("159".equals(r))  return "InClk";
    	if ("160".equals(r))  return "ExClk";
    	if ("161".equals(r))  return "TrClk";
    	if ("162".equals(r))  return "EpClk";
    	if ("163".equals(r))  return "1588Clk";
    	if ("164".equals(r))  return "utcClk";
    	if ("165".equals(r))  return "ClkOut";
    	if ("166".equals(r))  return "AlmOut";
    	if ("167".equals(r))  return "AlmConcateIn";
    	if ("168".equals(r))  return "AlmExtIn";
    	if ("200".equals(r))  return "SI";
    	if ("201".equals(r))  return "OW";
    	if ("202".equals(r))  return "IP";
    	if ("203".equals(r))  return "F1";
    	if ("204".equals(r))  return "AI";
    	if ("205".equals(r))  return "dPort";
    	if ("250".equals(r))  return "OSC";
    	if ("254".equals(r))  return "ETHPI";
    	if ("255".equals(r))  return "CES";
    	if ("256".equals(r))  return "IMA";
    	if ("257".equals(r))  return "ML-PPP";
    	if ("258".equals(r))  return "CESFTP";
    	if ("259".equals(r))  return "VLAN";
    	if ("261".equals(r))  return "ATnn";
    	if ("262".equals(r))  return "lbPort";
    	if ("263".equals(r))  return "MLPPPPort";
    	if ("300".equals(r))  return "PCMUI";
    	if ("302".equals(r))  return "V35";
    	if ("303".equals(r))  return "DIX";
    	if ("304".equals(r))  return "AIx";
    	if ("305".equals(r))  return "STUC";
    	if ("306".equals(r))  return "Temperature";
    	if ("307".equals(r))  return "BrdV";
    	if ("308".equals(r))  return "GPSAntenna";
    	if ("309".equals(r))  return "STM1Comp";
    	if ("310".equals(r))  return "ATMComp";
    	if ("311".equals(r))  return "FE1588";
    	if ("312".equals(r))  return "GE";
    	if ("313".equals(r))  return "Eth10G";
    	if ("314".equals(r))  return "PosAtmStm1";
    	if ("315".equals(r))  return "EthCIP";
    	if ("316".equals(r))  return "TdmCIP";
    	if ("317".equals(r))  return "AtmCIP";
    	if ("318".equals(r))  return "EthVIP";
    	if ("319".equals(r))  return "TdmVIP";
    	if ("320".equals(r))  return "AtmVIP";
    	if ("321".equals(r))  return "TMS";
    	if ("323".equals(r))  return "STM1PPPPos3";
    	if ("324".equals(r))  return "SuperVlan";
    	if ("325".equals(r))  return "sndClkDm";
    	if ("326".equals(r))  return "L2VETH";
    	if ("327".equals(r))  return "L3VETH";
    	if ("328".equals(r))  return "QinQ";
    	if ("329".equals(r))  return "AtmSTM4UNI";
    	if ("330".equals(r))  return "STM4PPPPos";
    	if ("331".equals(r))  return "STM16PPPPos";
    	if ("332".equals(r))  return "STM64PPPPos";
    	if ("333".equals(r))  return "L3Qx";
    	if ("334".equals(r))  return "L3LCT";
    	if ("335".equals(r))  return "VDSL2";
    	if ("336".equals(r))  return "SHDSL";
    	if ("337".equals(r))  return "RMPort";
    	if ("338".equals(r))  return "TeTrunk";
    	if ("339".equals(r))  return "VDSL2GRP";
    	if ("340".equals(r))  return "SHDSLGRP";
    	if ("341".equals(r))  return "PPPCIP";
    	if ("342".equals(r))  return "FRCIP";
    	if ("343".equals(r))  return "HDLCCIP";
    	if ("344".equals(r))  return "NMulE1";
    	if ("345".equals(r))  return "XPICGrp";
    	if ("346".equals(r))  return "VirtualLinkBindingGrp";
    	if ("347".equals(r))  return "cstm4_cpos12";
    	if ("348".equals(r))  return "OTU3Port";
    	if ("349".equals(r))  return "Eth40G";
    	if ("350".equals(r))  return "TSuperPort";
    	if ("351".equals(r))  return "OSuperPort";
    	if ("352".equals(r))  return "IPv6Tnnl";
    	if ("353".equals(r))  return "sham-linkX";
    	if ("354".equals(r))  return "virtual-linkX";
    	if ("355".equals(r))  return "ServLoopIntf";
    	if ("356".equals(r))  return "GFP_F";
    	if ("357".equals(r))  return "Eth100G";
    	if ("358".equals(r))  return "STM256PPPPos";
    	if ("359".equals(r))  return "POSGroup";
    	if ("360".equals(r))  return "ESI";
    	if ("361".equals(r))  return "SFI";
    	if ("362".equals(r))  return "P2MPTnnl";
    	if ("363".equals(r))  return "MgmtPort";
    	if ("364".equals(r))  return "OMA";
    	if ("365".equals(r))  return "SVI";
    	if ("366".equals(r))  return "IPSEC";
    	if ("367".equals(r))  return "V_TMPLT";
    	if ("368".equals(r))  return "PVCGROUP";
    	if ("369".equals(r))  return "WOSF_LI";
    	if ("370".equals(r))  return "WOST_LI";
    	if ("371".equals(r))  return "BEP";
    	if ("372".equals(r))  return "GFP_E";
    	if ("373".equals(r))  return "VBUI";
    	if ("374".equals(r))  return "IRB";
    	if ("375".equals(r))  return "TE_GTUNNELI";
    	if ("376".equals(r))  return "GTUNNEL_GROUPI";
    	if ("377".equals(r))  return "SPI";
    	if ("378".equals(r))  return "OTN";
    	if ("379".equals(r))  return "Eth25G";
    	if ("380".equals(r))  return "CPRI";
    	if ("381".equals(r))  return "FlexEGroup";
    	if ("382".equals(r))  return "FlexEVEI";
    	if ("383".equals(r))  return "FlexEVCI";
    	if ("384".equals(r))  return "AIR";
    	if ("385".equals(r))  return "FC";
    	if ("386".equals(r))  return "XGEIS";
    	if ("387".equals(r))  return "CGEIS";
    	if ("388".equals(r))  return "XLGEIS";
    	if ("389".equals(r))  return "GEIS";
    	if ("390".equals(r))  return "BB2M";
    	if ("391".equals(r))  return "SYN64K";
    	if ("392".equals(r))  return "dcn_mcc";
    	if ("393".equals(r))  return "dcn_eth";
    	if ("394".equals(r))  return "UCOM";
    	if ("395".equals(r))  return "Eth50G";
    	if ("396".equals(r))  return "Eth200G";
    	if ("397".equals(r))  return "Eth400G";
    	if ("398".equals(r))  return "PLA";
    	if ("399".equals(r))  return "AIR";
    	if ("700".equals(r))  return "TU1+Port";
    	if ("401".equals(r))  return "OTS_TTP_So";
    	if ("402".equals(r))  return "OTS_TTP_Si";
    	if ("403".equals(r))  return "OMS_TTP_So";
    	if ("404".equals(r))  return "OMS_TTP_Si";
    	if ("405".equals(r))  return "OMS_CTP_So";
    	if ("406".equals(r))  return "OMS_CTP_Si";
    	if ("407".equals(r))  return "OCH_TTP_So";
    	if ("408".equals(r))  return "OCH_TTP_Si";
    	if ("409".equals(r))  return "OCH_CTP_So";
    	if ("410".equals(r))  return "OCH_CTP_Si";
    	if ("411".equals(r))  return "OAC_CTP_So";
    	if ("412".equals(r))  return "OAC_CTP_Si";
    	if ("413".equals(r))  return "OSC_SO";
    	if ("414".equals(r))  return "OSC_SI";
    	if ("415".equals(r))  return "Optical_Bi";
    	if ("417".equals(r))  return "SOMS_CTP_So";
    	if ("418".equals(r))  return "SOMS_Out";
    	if ("419".equals(r))  return "SOMS_TTP_So";
    	if ("420".equals(r))  return "SOMS_TTP_Si";
    	if ("423".equals(r))  return "OP_So_in";
    	if ("424".equals(r))  return "OP_Si_in";
    	if ("425".equals(r))  return "OP_So_out";
    	if ("426".equals(r))  return "OP_Si_out";
    	if ("427".equals(r))  return "OCH_CTP_SoOrSi";
    	if ("428".equals(r))  return "OMS_TTP_SoOrSi";
    	if ("429".equals(r))  return "CTP_in";
    	if ("430".equals(r))  return "CTP_out";
    	if ("431".equals(r))  return "OTSTTP_Bi";
    	if ("448".equals(r))  return "OTSi_Bi";
    	if ("449".equals(r))  return "OCH_OMS_So";
    	if ("450".equals(r))  return "OCH_OMS_Si";
    	if ("451".equals(r))  return "Optical_Service_In";
    	if ("452".equals(r))  return "Optical_Service_Out";
    	if ("453".equals(r))  return "OAC_Bi";
    	if ("454".equals(r))  return "OChTTP_Bi";
    	if ("455".equals(r))  return "OChCTP_Bi";
    	if ("456".equals(r))  return "SOMS_Bi";
    	if ("457".equals(r))  return "PTP_Port";
    	if ("458".equals(r))  return "1PPS_TOD_Port";
    	if ("459".equals(r))  return "OMSTTP_Bi";
    	if ("460".equals(r))  return "OMSCTP_Bi";
    	if ("463".equals(r))  return "Clock_So";
    	if ("464".equals(r))  return "OTSiG_Bi";
    	if ("466".equals(r))  return "EBC_Si";
    	if ("500".equals(r))  return "Port";
    	if ("501".equals(r))  return "module";
    	if ("502".equals(r))  return "OCH_TTP";
    	if ("503".equals(r))  return "OAC_TTP";
    	if ("504".equals(r))  return "AdmNet";
    	if ("601".equals(r))  return "ATMFTP";
    	if ("602".equals(r))  return "Bundle";
    	if ("603".equals(r))  return "QxPort";
    	if ("604".equals(r))  return "VETH";
    	if ("605".equals(r))  return "VETH";
    	if ("606".equals(r))  return "CCVETH";
    	if ("607".equals(r))  return "UNI_LTE";
    	if ("608".equals(r))  return "UNI_3G";
    	if ("1067".equals(r))  return "Backplane_Bus_In";
    	if ("1068".equals(r))  return "Backplane_Bus_Out";
    	if ("1069".equals(r))  return "IP_Out";
    	if ("1070".equals(r))  return "IP_In";
    	if ("1071".equals(r))  return "DataService_out";
    	if ("1072".equals(r))  return "DataService_in";
    	if ("1073".equals(r))  return "Cross_Signal_Out";
    	if ("1074".equals(r))  return "Cross_Signal_In";
    	if ("1075".equals(r))  return "E1_Bi";
    	if ("1076".equals(r))  return "Backplane_Bus_Bi";
    	if ("1077".equals(r))  return "Backplane_Eth_Bi";
    	if ("1078".equals(r))  return "OPT_Module";
    	if ("1079".equals(r))  return "Inside_EDFA_Out";
    	if ("1081".equals(r))  return "Shell_Out";
    	if ("1082".equals(r))  return "Shell_In";
    	if ("1083".equals(r))  return "Shell_Bi";
    	if ("1096".equals(r))  return "Fan";
    	if ("1097".equals(r))  return "Switch";
    	if ("1098".equals(r))  return "Power";
    	if ("1099".equals(r))  return "MEM";
    	if ("1100".equals(r))  return "Switch-L2";
    	if ("1101".equals(r))  return "Inside_Out";
    	if ("1102".equals(r))  return "Inside_In";
    	if ("1103".equals(r))  return "OCH_Mod_lane_In";
    	if ("1104".equals(r))  return "OCH_Mod_lane_Out";
    	if ("1105".equals(r))  return "OAC_Mod_lane_In";
    	if ("1106".equals(r))  return "OAC_Mod_lane_Out";
    	if ("1107".equals(r))  return "OCH_Mod_lane_Bi";
    	if ("1108".equals(r))  return "OAC_Mod_lane_Bi";
    	if ("1109".equals(r))  return "Inside_EDFA_In";
    	if ("1110".equals(r))  return "Inside_Raman_Out";
    	if ("1111".equals(r))  return "VOA_Module";
    	if ("1112".equals(r))  return "Esc_Bi";
    	if ("1113".equals(r))  return "OSC_So";
    	if ("1114".equals(r))  return "OSC_Si";
    	if ("1115".equals(r))  return "Eth_Bi";
    	if ("1116".equals(r))  return "Comm_Slot";
    	if ("1117".equals(r))  return "Esc_Slot";
    	if ("1118".equals(r))  return "Panel_FE_Bi";
    	if ("1119".equals(r))  return "OW";
    	if ("1120".equals(r))  return "Panel_FE_Obi";
    	if ("1121".equals(r))  return "ESC";
    	if ("1122".equals(r))  return "OSC_Bi";
    	if ("1123".equals(r))  return "ESC_Vlan";
    	if ("1128".equals(r))  return "OP_AW_in";
    	if ("1129".equals(r))  return "OP_AP_in";
    	if ("1130".equals(r))  return "OP_AW_out";
    	if ("1131".equals(r))  return "OP_AP_out";
    	if ("1132".equals(r))  return "OP_A_in";
    	if ("1133".equals(r))  return "OP_A_out";
    	if ("1134".equals(r))  return "OP_C_Bi";
    	if ("1135".equals(r))  return "OP_L_Bi";
    	if ("1136".equals(r))  return "OP_BW_in";
    	if ("1137".equals(r))  return "OP_BP_in";
    	if ("1138".equals(r))  return "OP_BW_out";
    	if ("1139".equals(r))  return "OP_BP_out";
    	if ("1140".equals(r))  return "OP_B_in";
    	if ("1141".equals(r))  return "OP_B_out";
    	if ("1144".equals(r))  return "OP_1_in";
    	if ("1145".equals(r))  return "OP_1W_out";
    	if ("1146".equals(r))  return "OP_1P_out";
    	if ("1147".equals(r))  return "OP_1W_in";
    	if ("1148".equals(r))  return "OP_1P_in";
    	if ("1149".equals(r))  return "OP_1_out";
    	if ("1150".equals(r))  return "OP_Bi";
    	if ("1152".equals(r))  return "OP_2_in";
    	if ("1153".equals(r))  return "OP_2W_out";
    	if ("1154".equals(r))  return "OP_2P_out";
    	if ("1155".equals(r))  return "OP_2W_in";
    	if ("1156".equals(r))  return "OP_2P_in";
    	if ("1157".equals(r))  return "OP_2_out";
    	if ("1158".equals(r))  return "OP_W_Bi";
    	if ("1159".equals(r))  return "OP_P_Bi";
    	if ("1161".equals(r))  return "Mntr_in";
    	if ("1162".equals(r))  return "Mntr_out";
    	if ("1163".equals(r))  return "OLOOP";
    	if ("1164".equals(r))  return "ILOOP";
    	if ("1165".equals(r))  return "Fiber_Monitor";
    	if ("1167".equals(r))  return "OptSource_Out";
    	if ("1168".equals(r))  return "OptSource_In";
    	if ("1169".equals(r))  return "OptSource_Bi";
    	if ("1176".equals(r))  return "Ethernet_Bi";
    	if ("1177".equals(r))  return "Inner_Ethernet_Bi";
    	if ("1192".equals(r))  return "ODU_C_I";
    	if ("1193".equals(r))  return "ODU_C_O";
    	if ("1194".equals(r))  return "EDC_V_I";
    	if ("1195".equals(r))  return "EDC_V_O";
    	if ("1196".equals(r))  return "ODU_Bi";
    	if ("1197".equals(r))  return "HO_ODU_Bi";
    	if ("1198".equals(r))  return "Sub_Serv_In";
    	if ("1199".equals(r))  return "Sub_Serv_Out";
    	if ("1200".equals(r))  return "Inner_SDH_Bi";
    	if ("1201".equals(r))  return "OTU_Bi";
    	if ("1202".equals(r))  return "OTU_In";
    	if ("1203".equals(r))  return "OTU_Out";
    	if ("1206".equals(r))  return "OTUC_Bi";
    	if ("1208".equals(r))  return "ETH_So";
    	if ("1209".equals(r))  return "ETH_Si";
    	if ("1210".equals(r))  return "ETH_TRUNK_So";
    	if ("1211".equals(r))  return "ETH_TRUNK_Si";
    	if ("1212".equals(r))  return "ETH_TRUNK_Bi";
    	if ("1213".equals(r))  return "HO_ODU_Out";
    	if ("1214".equals(r))  return "HO_ODU_In";
    	if ("1215".equals(r))  return "HiODU_Bi";
    	if ("1216".equals(r))  return "HiODU_In";
    	if ("1217".equals(r))  return "HiODU_Out";
    	if ("1218".equals(r))  return "EBC_Bi";
    	if ("1219".equals(r))  return "EDC_Virtual_Bi";
    	if ("1220".equals(r))  return "OTN_Virtual";
    	if ("1222".equals(r))  return "OSC_Bi";
    	if ("1223".equals(r))  return "flexe_sg_bi";
    	if ("1224".equals(r))  return "Optical_Out";
    	if ("1225".equals(r))  return "Optical_In";
    	if ("1255".equals(r))  return "WASON";
    	if ("5141".equals(r))  return "lct_x/y";
    	
    	return r;
    }
    
    

    public static void main(String[] args) {
        String sectionDn = "EMS:TZ-OTNU31-1-P@TopologicalLink:/d=src/ManagedElement{70127685(P)}FTP{/direction=sink/rack=0/shelf=7/slot=24/port=78151743}_/d=sink/ManagedElement{70127685(P)}FTP{/direction=src/rack=0/shelf=7/slot=4/port=78200862}";
        String[] split = sectionDn.split("/d=");
        String s = split[2].replaceAll("\\{",":").replaceAll("}","");

        System.out.println("split = " + split);
    }


}
