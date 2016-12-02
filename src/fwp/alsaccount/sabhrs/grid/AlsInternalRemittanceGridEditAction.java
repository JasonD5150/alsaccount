package fwp.alsaccount.sabhrs.grid;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.appservice.inventory.AlsProviderRemittanceAS;
import fwp.als.hibernate.inventory.dao.AlsInternalRemittance;
import fwp.als.hibernate.inventory.dao.AlsInternalRemittanceIdPk;
import fwp.als.hibernate.inventory.dao.AlsProviderRemittance;
import fwp.als.hibernate.inventory.dao.AlsProviderRemittanceIdPk;
import fwp.als.hibernate.item.dao.AlsItemApplFeeAcct;
import fwp.als.hibernate.item.dao.AlsItemApplFeeAcctIdPk;
import fwp.alsaccount.appservice.admin.AlsAccountMasterAS;
import fwp.alsaccount.appservice.sabhrs.AlsInternalRemittanceAS;
import fwp.alsaccount.appservice.sabhrs.AlsProviderBankDetailsAS;
import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesAS;
import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesSummaryAS;
import fwp.alsaccount.appservice.sabhrs.AlsTransactionGrpStatusAS;
import fwp.alsaccount.appservice.sales.AlsItemApplFeeAcctAS;
import fwp.alsaccount.dao.admin.AlsAccountMaster;
import fwp.alsaccount.dao.sabhrs.AlsProviderBankDetails;
import fwp.alsaccount.dao.sabhrs.AlsProviderBankDetailsIdPk;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntriesSummary;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGrpStatus;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGrpStatusIdPk;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.Utils;
import fwp.security.user.UserDTO;


public class AlsInternalRemittanceGridEditAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String oper;
	
	private String id;
	
	private Date airBillingFrom;
	private Date airBillingTo;
	private Boolean compByProv;
	private Boolean airOfflnPaymentApproved;
	private String airOfflnPaymentAppCom;
	
	private String provComp;
	private String remApp;
	private String remRev;
	private String 	disAppCom;
	private String 	ccSales;


	UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
	Timestamp date = new Timestamp(System.currentTimeMillis());
	HibHelpers hh = new HibHelpers();
	String curBudgYear = hh.getCurrentBudgetYear();

	public String execute() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String errMsg="";		
		
		try{
			if(oper.equalsIgnoreCase("edit")){	
				AlsInternalRemittanceAS tmpAS = new AlsInternalRemittanceAS();
				AlsInternalRemittanceIdPk tmpIdPk = new AlsInternalRemittanceIdPk();
				AlsInternalRemittance original = null;	

				Integer provNo = Integer.parseInt(id.split("_")[2]);
				Timestamp bpFrom = new Timestamp(sdf.parse(id.split("_")[0]).getTime());
				Timestamp bpTo = new Timestamp(sdf.parse(id.split("_")[1]).getTime());
				tmpIdPk.setApiProviderNo(provNo);
				tmpIdPk.setAirBillingFrom(bpFrom);
				tmpIdPk.setAirBillingTo(bpTo);
				original = tmpAS.findById(tmpIdPk);

				if(original == null){
					addActionError("Original record not found.");
					 return "error_json";
				}
				
				Boolean approveSummary = false;
				if((original.getAirNonAlsSales() == null ||  original.getAirNonAlsSales() == 0.0) && 
				   (original.getAirShortSales() == null ||  original.getAirShortSales() == 0.0) &&
				   (original.getAirOverSales() == null ||  original.getAirOverSales() == 0.0)){
					approveSummary = true;
				}
				/*Credit Card Sales Changed*/
				if(original.getAirCreditSales() != null){
					if(Double.compare(original.getAirCreditSales(), Double.valueOf(ccSales)) != 0){
						original.setAirCreditSales(Double.valueOf(ccSales));
					}
				}
				
				/*Approved By Provider*/
				if(original.getAirCompleteProvider() == null && "true".equals(provComp)){
					original.setAirCompleteProvider(date);
				}else if(original.getAirCompleteProvider() != null && "false".equals(provComp)){
					original.setAirCompleteProvider(null);
				}
				/*Remittance NOT Approved, Comments Changed*/
				if(original.getAirOfflnPaymentAppCom() != null && !original.getAirOfflnPaymentAppCom().equals(disAppCom)){
					original.setAirOfflnPaymentAppCom(disAppCom);
				}
				/*Remittance Reviewed*/
				if(!"Y".equals(original.getAirOfflnPaymentReviewed())&&"true".equals(remRev)){
					postEntries(original, provNo, bpFrom, bpTo);
					updateProviderRemittance("A", provNo, bpFrom, bpTo, null);
					original.setAirOfflnPaymentReviewed("Y");
				}
				/*Remittance Un-Reviewed*/
				if("Y".equals(original.getAirOfflnPaymentReviewed())&&"false".equals(remRev)){
					deleteEntries(original, provNo, bpFrom, bpTo);
					updateProviderRemittance("D", provNo, bpFrom, bpTo, null);
					original.setAirOfflnPaymentReviewed("N");
				}
				/*Remittance Approved*/
				if(!"Y".equals(original.getAirOfflnPaymentApproved())&&"true".equals(remApp)){
					updateTransactionGroup(provNo, sdf.parse(id.split("_")[1]), true, approveSummary);
					original.setAirOfflnPaymentApproved("Y");
					original.setAirOfflnPaymentAppBy(userInfo.getStateId());
					original.setAirOfflnPaymentAppDt(date);
					original.setAirOfflnPaymentAppCom(disAppCom);
				}
				/*Remittance Disapproved*/
				if("Y".equals(original.getAirOfflnPaymentApproved())&&"false".equals(remApp)){
					updateTransactionGroup(provNo, sdf.parse(id.split("_")[1]), false, approveSummary);
					original.setAirOfflnPaymentApproved("N");
					original.setAirOfflnPaymentAppBy(null);
					original.setAirOfflnPaymentAppDt(null);
					original.setAirOfflnPaymentAppCom(disAppCom);
				}
				tmpAS.save(original);
			}else{
				return "error_json";
			}
		}  catch(Exception ex) {
			 if (ex.toString().contains("ORA-02292")){
				  errMsg += "Grid has child record(s) which would need to be deleted first.";
			  } else if (ex.toString().contains("ORA-02291")){
				  errMsg += "Parent record not found.";
			  } else if (ex.toString().contains("ORA-00001")){
				  errMsg += "Unable to add this record due to duplicate.";
			  }	else {
				  errMsg += " " + ex.toString();
			  }
		    addActionError(errMsg);
	        return "error_json";
		}	
		return SUCCESS;
	}
	
	private String postEntries(AlsInternalRemittance original, Integer provNo, Timestamp bpFrom, Timestamp bpTo){
		String grpIdentifier = Utils.createIntProvGroupIdentifier(provNo, bpTo.toString().substring(0, 10).replace("-", "/"),001);
		Integer transGrpCd = 8;
		Double totBankDep = 0.0;
		Integer seqNo = 2;
		String bankCd = null;
		
		AlsProviderBankDetailsAS apbdAS = new AlsProviderBankDetailsAS();
		List<AlsProviderBankDetails> apbdLst = new ArrayList<AlsProviderBankDetails>();
		String where = "WHERE idPk.apiProviderNo = "+provNo+" AND idPk.apbdBillingTo = TO_TIMESTAMP('"+bpTo+"', 'yyyy-MM-dd HH24:MI:SS.FF')";
		apbdLst = apbdAS.findAllByWhere(where);
		for(AlsProviderBankDetails apbd : apbdLst){
			String grpIdentifier1 = Utils.createIntProvGroupIdentifier(provNo, bpTo.toString().substring(0, 10).replace("-", "/"),seqNo);
			
			if(apbdLst.indexOf(apbd) == 0){
				bankCd = apbd.getAbcBankCd();
            }
			
			if(apbd.getApbdAmountDeposit() != null && apbd.getApbdAmountDeposit() > 0){
				totBankDep += apbd.getApbdAmountDeposit();
			}
			
			insertTransGrpStatus(original, transGrpCd, grpIdentifier1, apbd.getApbdDepositDate(), apbd.getApbdDepositId());
			
			/*POST E42 ENTRIES*/
			if(hh.postE42Entries(apbd.getApbdAmountDeposit(), provNo, bpFrom, bpTo, 8, grpIdentifier1,"E42","BANK DEPOSIT",null,null,null,null)!= 1){
				addActionError("Error while posting SABHRS entry.");
				return "error_json";
			};
			
			updateNetAmount(transGrpCd, grpIdentifier1, apbd.getAbcBankCd());
			
			/*UPDATE TRANSACTION GROUP IDENTIFIER*/
			AlsProviderBankDetailsIdPk apbdIdPk = new AlsProviderBankDetailsIdPk();
			apbdIdPk.setApiProviderNo(provNo);
			apbdIdPk.setApbdBillingTo(bpTo);
			apbdIdPk.setApbdSeqNo(apbd.getIdPk().getApbdSeqNo());
			AlsProviderBankDetails apbdTmp = apbdAS.findById(apbdIdPk);
			if(apbdTmp != null){
				apbdTmp.setAtgTransactionCd(transGrpCd);
				apbdTmp.setAtgsGroupIdentifier(grpIdentifier1);
				apbdAS.save(apbdTmp);
			}else{
				addActionError("Error while updating Group Identifier into Als_Provider_Bank_Details table.");
				return "error_json";
			}
			seqNo += 1;
		}
		
		String grpIdentifier2 = Utils.createIntProvGroupIdentifier(provNo, bpTo.toString().substring(0, 10).replace("-", "/"),seqNo);
		if(totBankDep > 0){
			insertTransGrpStatus(original, transGrpCd, grpIdentifier2, null, null);
			if(hh.postE42Entries(totBankDep, provNo, bpFrom, bpTo, 8, grpIdentifier2,"E43","TOTAL BANK DEPOSIT",null,null,null,null)!= 1){
				addActionError("Error while posting SABHRS entry.");
				return "error_json";
			};
		}else{
			insertTransGrpStatus(original, transGrpCd, grpIdentifier2, null, null);
		}
		
		if(original.getAirCreditSales() > 0){
			if(hh.postE42Entries(totBankDep, provNo, bpFrom, bpTo, transGrpCd, grpIdentifier2,"E40","CREDIT CARD SALES",null,null,null,null)!= 1){
				addActionError("Error while posting E40 SABHRS entry.");
				return "error_json";
			};
		}
		
		String amtType = hh.getPval("IAFA_AMOUNT_TYPE","PAE",null);
		String shortSalesReason = hh.getPval("PAE_REASON","PROV. OFFLINE PAYMENT OVERPAY",null);
		if(original.getAirOverSales() > 0){
			if(hh.postE42Entries(totBankDep, provNo, bpFrom, bpTo, transGrpCd, grpIdentifier2,"E19","OVER SALES",amtType,shortSalesReason,null,null)!= 1){
				addActionError("Error while posting E19 AIAFA and SABHRS entry.");
				return "error_json";
			};
		}
		
		if(original.getAirShortSales() > 0){
			if(hh.postE42Entries(totBankDep, provNo, bpFrom, bpTo, 8, grpIdentifier2,"E18","SHORT OF SALES", amtType,shortSalesReason,null,null)!= 1){
				addActionError("Error while posting E18 AIAFA and SABHRS entry.");
				return "error_json";
			};
		}
		
		postC7Through10Entries(provNo, bpFrom, bpTo, transGrpCd, grpIdentifier2);
		updateNetAmount(transGrpCd, grpIdentifier, bankCd);
		updateNetAmount(transGrpCd, grpIdentifier2, bankCd);
		
		return SUCCESS;
	}
	
	private String deleteEntries(AlsInternalRemittance original, Integer provNo, Timestamp bpFrom, Timestamp bpTo){
		String where = null;

		String grpIdentifier = Utils.createIntProvGroupIdentifier(provNo, bpTo.toString().substring(0, 10).replace("-", "/"),null).trim();
		String grpIdentifier1 = Utils.createIntProvGroupIdentifier(provNo, bpTo.toString().substring(0, 10).replace("-", "/"),001);
		Integer transGrpCd = 8;
		String interfaceStatus = null;
	
		AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
		List<AlsTransactionGrpStatus> atgsLst = new ArrayList<AlsTransactionGrpStatus>();
		AlsTransactionGrpStatusIdPk atgsIdPk = new AlsTransactionGrpStatusIdPk();
		where = "WHERE idPk.atgTransactionCd = "+transGrpCd+" AND substr(idPk.atgsGroupIdentifier,1,18) = '"+grpIdentifier+"' AND nvl(atgsInterfaceStatus,'D') = 'A' AND rownum < 2)";
		atgsLst = atgsAS.findAllByWhere(where);
		if(!atgsLst.isEmpty()){
			interfaceStatus = atgsLst.get(0).getAtgsInterfaceStatus();
		}
		
		/*TO DELETE BANK DEPOSITS ENTRIES AND CONSOLIDATED BANK DEPOSIT,C7/8/9/10, Credit Entry.*/
		where = "WHERE idPk.atgTransactionCd = "+transGrpCd+" AND idPk.atgsGroupIdentifier LIKE '"+grpIdentifier+"%' And To_Number(Substr(idPk.atgsGroupIdentifier,-3,3)) > 1";
		atgsLst = atgsAS.findAllByWhere(where);
		if(!atgsLst.isEmpty()){
			for(AlsTransactionGrpStatus atgs : atgsLst){
				AlsSabhrsEntriesAS aseAS = new AlsSabhrsEntriesAS();
				List<AlsSabhrsEntries> aseLst = new ArrayList<AlsSabhrsEntries>();
				where = "WHERE atgTransactionCd = "+transGrpCd+" AND atgsGroupIdentifier = '"+atgs.getIdPk().getAtgsGroupIdentifier()+"' ";
				aseLst = aseAS.findAllByWhere(where);
				if(!aseLst.isEmpty()){
					for(AlsSabhrsEntries ase : aseLst){
						aseAS.delete(ase);
					}
				}else{
					addActionError("Error while deleting record from Als_Sabhrs_Entries for Transaction Type 8 and Group Identifier "+atgs.getIdPk().getAtgsGroupIdentifier()+".");
					return "error_json";
				}
				
				if(atgs.getAtgsWhenUploadToSummary() != null){
					AlsSabhrsEntriesSummaryAS asesAS = new AlsSabhrsEntriesSummaryAS();
					List<AlsSabhrsEntriesSummary> asesLst = new ArrayList<AlsSabhrsEntriesSummary>();
					asesLst = asesAS.findAllByWhere(where);
					if(!asesLst.isEmpty()){
						for(AlsSabhrsEntriesSummary ases : asesLst){
							asesAS.delete(ases);
						}
					}else{
						addActionError("Error while deleting record from Als_Sabhrs_Entries for Transaction Type 8 and Group Identifier "+atgs.getIdPk().getAtgsGroupIdentifier()+".");
						return "error_json";
					}
				}
				
				AlsProviderBankDetailsAS apbdAS = new AlsProviderBankDetailsAS();
				List<AlsProviderBankDetails> apbdLst = new ArrayList<AlsProviderBankDetails>();
				apbdLst = apbdAS.findAllByWhere(where);
				if(!apbdLst.isEmpty()){
					for(AlsProviderBankDetails apbd : apbdLst){
						apbd.setAtgTransactionCd(null);
						apbd.setAtgsGroupIdentifier(null);
						apbdAS.save(apbd);
					}
				}
				
				List<AlsTransactionGrpStatus> atgsLst2 = new ArrayList<AlsTransactionGrpStatus>();
				where = "WHERE idPk.atgTransactionCd = "+transGrpCd+" AND idPk.atgsGroupIdentifier = '"+atgs.getIdPk().getAtgsGroupIdentifier()+"' ";
				atgsLst2 = atgsAS.findAllByWhere(where);
				if(!atgsLst2.isEmpty()){
					for(AlsTransactionGrpStatus atgs2 : atgsLst2){
						atgsAS.delete(atgs2);
					}
				}else{
					addActionError("Error while deleting the record from Als_Transaction_Grp_Status for Transaction Type 8 and Group Identifier "+atgs.getIdPk().getAtgsGroupIdentifier()+".");
					return "error_json";
				}
			}
		}
		
		/*TO DELETE E18 AND E19 (SHORT AND OVER SALES) ENTRIES*/
		AlsSabhrsEntriesAS aseAS = new AlsSabhrsEntriesAS();
		List<AlsSabhrsEntries> aseLst = new ArrayList<AlsSabhrsEntries>();

		aseLst = hh.getIntProvRemittanceSabhrsEntries(provNo, bpFrom, bpTo);
		if(!aseLst.isEmpty()){
			for(AlsSabhrsEntries ase : aseLst){
				AlsItemApplFeeAcctAS aiafaAS = new AlsItemApplFeeAcctAS();
				AlsItemApplFeeAcct aiafa = new AlsItemApplFeeAcct();
				AlsItemApplFeeAcctIdPk aiafaIdPk = new AlsItemApplFeeAcctIdPk();
				aiafaIdPk.setApiProviderNo(ase.getApiProviderNo());
				aiafaIdPk.setAprBillingFrom(ase.getAprBillingFrom());
				aiafaIdPk.setAprBillingTo(ase.getAprBillingTo());
				aiafaIdPk.setAiafaSeqNo(ase.getAiafaSeqNo());
				aiafa = aiafaAS.findById(aiafaIdPk);
				if(aiafa != null){
					aiafaAS.delete(aiafa);
				}else{
					addActionError("Error while deleting record from Als_Item_Appl_Fee_Acct table.");
					return "error_json";
				}
				aseAS.delete(ase);
			}
		}
		
		where = "WHERE atgTransactionCd ="+transGrpCd+"AND atgsGroupIdentifier = '"+grpIdentifier1+"'";
		aseLst = aseAS.findAllByWhere(where);
		if(!aseLst.isEmpty()){
			for(AlsSabhrsEntries ase : aseLst){
				ase.setAseWhenUploadedToSummary(null);
				ase.setAsesSeqNo(null);
				if(ase.getAseAllowUploadToSummary() == "N"){
					ase.setAseAllowUploadToSummary(null);
				}else{
					ase.setAseWhenUploadedToSumm(Timestamp.valueOf("1900-1-1 00:00:00"));
				}
				aseAS.merge(ase);
			}
		}else{
			addActionError("Error while updating Als_Sabhrs_Entries table for Transaction Type 8 and Group Identifier "+grpIdentifier1+".");
			return "error_json";
		}
		
		aseLst = aseAS.findAllByWhere(where);
		if(!aseLst.isEmpty()){
			for(AlsSabhrsEntries ase : aseLst){
				aseAS.delete(ase);
			}
		}else{
			addActionError("Error while deleting record from Als_Sabhrs_Entries_Summary table for Transaction Type 8 and Group Identifier "+grpIdentifier1+".");
			return "error_json";
		}
		where = "WHERE idPk.atgTransactionCd = "+transGrpCd+" AND idPk.atgsGroupIdentifier = '"+grpIdentifier1+"' ";
		atgsLst = atgsAS.findAllByWhere(where);
		if(!atgsLst.isEmpty()){
			for(AlsTransactionGrpStatus atgs : atgsLst){
				atgs.setAtgsSummaryApprovedBy(null);
				atgs.setAtgsSummaryDt(null);
				atgs.setAtgsSummaryStatus(null);
				atgs.setAtgsInterfaceApprovedBy(null);
				atgs.setAtgsInterfaceDt(null);
				atgs.setAtgsInterfaceStatus(null);
				atgs.setAtgsWhenUploadToSummary(null);
				atgsAS.save(atgs);	
			}
		}else{
			addActionError("Error while updating Als_Transaction_Grp_Status table for Transaction Type 8 and Group Identifier "+grpIdentifier1+".");
			return "error_json";
		}
		return SUCCESS;
	}
	
	private void insertTransGrpStatus(AlsInternalRemittance original, Integer gransGrpCd, String grpIdentifier1, Timestamp apbdDepositDt, String apbdDepositId){
		String where = "";
		/*INSERT TRANS GRP STATUS*/
		String aprvStatus = null;
		Timestamp aprvDt = null;
		String aprvUser = null;
		String gi001 = null;
		if(original.getAirNonAlsSales() == 0 && original.getAirShortSales() == 0 && original.getAirOverSales()==0){
			aprvStatus = "A";
			aprvDt = date;
			aprvUser = "auto_"+userInfo.getStateId();
			gi001 = grpIdentifier1.substring(0, grpIdentifier1.length()-4)+ " 001";
		}
		AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
		AlsTransactionGrpStatus atgs = new AlsTransactionGrpStatus();
		List<AlsTransactionGrpStatus> atgsLst = new ArrayList<AlsTransactionGrpStatus>();
		AlsTransactionGrpStatusIdPk atgsIdPk = new AlsTransactionGrpStatusIdPk();
		atgsIdPk.setAtgTransactionCd(gransGrpCd);
		atgsIdPk.setAtgsGroupIdentifier(grpIdentifier1);
		atgs.setIdPk(atgsIdPk);
		atgs.setAtgsWhenCreated(date);
		atgs.setAtgsWhoCreated(userInfo.getStateId());
		atgs.setAtgsAccountingDt(apbdDepositDt);
		atgs.setAtgsNetDrCr(0.0);
		atgs.setAtgsNonAlsFlag(null);
		atgs.setAbcBankCd(null);
		atgs.setAtgsDepositId(apbdDepositId);
		atgs.setAtgsSummaryStatus(aprvStatus);
		atgs.setAtgsSummaryDt(aprvDt);
		atgs.setAtgsSummaryApprovedBy(aprvUser);
		atgs.setAtgsInterfaceStatus(aprvStatus);
		atgs.setAtgsInterfaceDt(aprvDt);
		atgs.setAtgsInterfaceApprovedBy(aprvUser);
		atgsAS.save(atgs);
		if(gi001 != null){
			where = "WHERE idPk.atgsGroupIdentifier = '"+gi001+"' ";
			atgsLst = atgsAS.findAllByWhere(where);
			if(!atgsLst.isEmpty()){
				for(AlsTransactionGrpStatus atgs2 : atgsLst){
					atgs2.setAtgsSummaryStatus(aprvStatus);
					atgs2.setAtgsSummaryDt(aprvDt);
					atgs2.setAtgsSummaryApprovedBy(aprvUser);
					atgs2.setAtgsInterfaceStatus(aprvStatus);
					atgs2.setAtgsInterfaceDt(aprvDt);
					atgs2.setAtgsInterfaceApprovedBy(aprvUser);
					atgsAS.save(atgs2);
				}
			}
		}
	}

	private String updateNetAmount(Integer transGrpCd, String grpIdentifier1, String bankCd){
		AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
		AlsTransactionGrpStatus atgs = new AlsTransactionGrpStatus();
		List<AlsTransactionGrpStatus> atgsLst = new ArrayList<AlsTransactionGrpStatus>();
		AlsTransactionGrpStatusIdPk atgsIdPk = new AlsTransactionGrpStatusIdPk();
		String where = "";

		AlsAccountMasterAS aamAS = new AlsAccountMasterAS();
		List<AlsAccountMaster> aamLst = new ArrayList<AlsAccountMaster>();
		AlsAccountMaster aam = new AlsAccountMaster();
		String prevCashAcc = null;
		String cashAcc = null;
		where = "WHERE idPk.asacBudgetYear = "+(Integer.parseInt(hh.getCurrentBudgetYear())-1)+" "
				+ "AND aamAccountDesc = 'CASH' ";
		aamLst = aamAS.findAllByWhere(where);
		if(!aamLst.isEmpty()){
			aam = aamLst.get(0);
			prevCashAcc = aam.getIdPk().getAamAccount();
		}else{
			addActionError("Cash account not maintained in Als_Account_Master for Budget Year "+(Integer.parseInt(hh.getCurrentBudgetYear())-1)+".");
			return "error_json";
		}
		where = "WHERE idPk.asacBudgetYear = "+(Integer.parseInt(hh.getCurrentBudgetYear()))+" "
				+ "AND aamAccountDesc = 'CASH' ";
		aamLst = aamAS.findAllByWhere(where);
		if(!aamLst.isEmpty()){
			aam = aamLst.get(0);
			cashAcc = aam.getIdPk().getAamAccount();
		}else{
			addActionError("Cash account not maintained in Als_Account_Master for Budget Year "+(Integer.parseInt(hh.getCurrentBudgetYear())-1)+".");
			return "error_json";
		}
		where = "WHERE idPk.atgTransactionCd = 8 AND idPk.atgsGroupIdentifier = '"+grpIdentifier1+"'";
		atgsLst = atgsAS.findAllByWhere(where);
		if(!atgsLst.isEmpty()){
			atgs = atgsLst.get(0);
			atgs.setAtgsNetDrCr(hh.getSabhrsNetAmt(8, grpIdentifier1, cashAcc, prevCashAcc));
			atgs.setAbcBankCd(bankCd);
			atgs.setAtgsWhoModi(userInfo.getStateId());
			atgs.setAtgsWhenModi(date);
			atgsAS.save(atgs);
		}else{
			addActionError("Net Amount and Bank Code cannot be updated in Als_Transaction_Grp_Status table for "+grpIdentifier1);
			return "error_json";
		}
		return SUCCESS;
	}
	
	private String postC7Through10Entries(Integer provNo, Timestamp bpFrom, Timestamp bpTo, Integer transGrpCd, String grpIdentifier2){
		AlsAccountMasterAS aamAS = new AlsAccountMasterAS();
		List<AlsAccountMaster> aamLst = new ArrayList<AlsAccountMaster>();
		AlsAccountMaster aam = new AlsAccountMaster();
		String where = "WHERE idPk.asacBudgetYear = "+curBudgYear+" AND aamAccountDesc = 'ACCOUNT RECEIVABLE'";
		aamLst = aamAS.findAllByWhere(where);
		if(!aamLst.isEmpty()){
			aam = aamLst.get(0);
		}
		String activeEntry = null;
		List<AlsSabhrsEntries> aseLst = hh.getFundTribeAccountReceivalbeEntries(provNo, bpFrom, bpTo, aam.getIdPk().getAamAccount());
		for(AlsSabhrsEntries ase : aseLst){
			if(ase.getAseAmt() > 0 ){
				activeEntry = "C7";
			}else{
				activeEntry = "C8";
			}
			if(hh.postE42Entries(ase.getAseAmt(), provNo, bpFrom, bpTo, transGrpCd, grpIdentifier2,activeEntry,"ACCOUNT RECEIVABLE", null,null,ase.getAamFund(),ase.getAtiTribeCd() )!= 1){
				addActionError("Error while posting "+activeEntry+" AIAFA and SABHRS entry.");
				return "error_json";
			};
		}
		where = "WHERE idPk.asacBudgetYear = "+curBudgYear+" AND aamAccountDesc = 'ACCOUNT PAYABLE'";
		aamLst = aamAS.findAllByWhere(where);
		if(!aamLst.isEmpty()){
			aam = aamLst.get(0);
		}
		aseLst = hh.getFundTribeAccountReceivalbeEntries(provNo, bpFrom, bpTo, aam.getIdPk().getAamAccount());
		for(AlsSabhrsEntries ase : aseLst){
			if(ase.getAseAmt() < 0 ){
				activeEntry = "C9";
			}else{
				activeEntry = "C10";
			}
			if(hh.postE42Entries(ase.getAseAmt(), provNo, bpFrom, bpTo, transGrpCd, grpIdentifier2,activeEntry,"ACCOUNT PAYABLE", null,null,ase.getAamFund(),ase.getAtiTribeCd() )!= 1){
				addActionError("Error while posting "+activeEntry+" AIAFA and SABHRS entry.");
				return "error_json";
			};
		}
		
		return SUCCESS;
	}
	
	private String updateProviderRemittance(String flg, Integer provNo, Timestamp bpFrom, Timestamp bpTo, Double airNetOverShort){
		AlsProviderRemittanceAS aprAS = new AlsProviderRemittanceAS();
		AlsProviderRemittanceIdPk aprIdPk = new AlsProviderRemittanceIdPk();
		AlsProviderRemittance apr = null;	
		aprIdPk.setApiProviderNo(provNo);
		aprIdPk.setAprBillingFrom(bpFrom);
		aprIdPk.setAprBillingTo(bpTo);
		apr = aprAS.findById(aprIdPk);
		if(Utils.isNil(airNetOverShort)){
			airNetOverShort = 0.0;
		}
		if(apr != null){
			if("A".equals(flg)){
				apr.setAprAmtDue(apr.getAprAmtDue() + airNetOverShort);
				apr.setAprAmtReceived(apr.getAprAmtDue() + airNetOverShort);
				apr.setAprOldAmtReceived(apr.getAprAmtReceived());
				apr.setAprRemittPerStatus(null);
			}else if("D".equals(flg)){
				apr.setAprAmtReceived(apr.getAprOldAmtReceived());
				apr.setAprOldAmtReceived(apr.getAprAmtReceived());
				apr.setAprAmtDue(apr.getAprAmtDue() - airNetOverShort);
				apr.setAprRemittPerStatus("O");
			}
			aprAS.save(apr);
		}else{
			addActionError("Amount Received could not be updated in Als_Provider_Remittance table.");
			return "error_json";
		}		
		return SUCCESS;
	}
	
	public String updateTransactionGroup(Integer provNo, Date bpToDt, Boolean approve, Boolean approveSummary){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		AlsTransactionGrpStatusAS atgsAS = new AlsTransactionGrpStatusAS();
		List<AlsTransactionGrpStatus> atgsLst = new ArrayList<AlsTransactionGrpStatus>();
		Timestamp curDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
		String errMsg="";
		String where = "";
		try{
			where = "WHERE idPk.atgTransactionCd = 8 "
				  + "AND Substr(idPk.atgsGroupIdentifier,2,6) = LPAD("+provNo+", 6, '0') "
				  + "AND Substr(idPk.atgsGroupIdentifier,9,10) = '"+sdf.format(bpToDt)+"' ";
			atgsLst = atgsAS.findAllByWhere(where);
			
			if(!atgsLst.isEmpty()){
				for(AlsTransactionGrpStatus tmp:atgsLst){
					tmp.setAtgsSummaryStatus(approve?"A":null);
					tmp.setAtgsSummaryApprovedBy(approve?"auto_"+userInfo.getStateId().toString():null);
					tmp.setAtgsSummaryDt(approve?curDate:null);
					if(approveSummary){
						tmp.setAtgsInterfaceStatus(approve?"A":null);
						tmp.setAtgsInterfaceApprovedBy(approve?"auto_"+userInfo.getStateId().toString():null);
						tmp.setAtgsInterfaceDt(approve?curDate:null);
					}
					tmp.setAtgsWhoModi(userInfo.getStateId().toString());
					tmp.setAtgsWhenModi(curDate);
					atgsAS.save(tmp);
				}
			}else{
				this.addActionError("Error while updating Interface Status in ALS.ALSTRANSACTION_GRP_STATUS for Provider "+provNo+", for BPE "+bpToDt.toString()+".");
				return "error_json";
			}		
		}  catch(Exception ex) {
			 if (ex.toString().contains("ORA-02292")){
				  errMsg += "Grid has child record(s) which would need to be deleted first.";
			  } else if (ex.toString().contains("ORA-02291")){
				  errMsg += "Parent record not found.";
			  } else if (ex.toString().contains("ORA-00001")){
				  errMsg += "Unable to add this record due to duplicate.";
			  }	else {
				  errMsg += " " + ex.toString();
			  }
		    addActionError(errMsg);
	        return "error_json";
		}	
		return SUCCESS;
	}
		
	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public Date getAirBillingFrom() {
		return airBillingFrom;
	}

	public void setAirBillingFrom(Date airBillingFrom) {
		this.airBillingFrom = airBillingFrom;
	}

	public Date getAirBillingTo() {
		return airBillingTo;
	}

	public void setAirBillingTo(Date airBillingTo) {
		this.airBillingTo = airBillingTo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Boolean getAirOfflnPaymentApproved() {
		return airOfflnPaymentApproved;
	}

	public void setAirOfflnPaymentApproved(Boolean airOfflnPaymentApproved) {
		this.airOfflnPaymentApproved = airOfflnPaymentApproved;
	}
	
	public String getAirOfflnPaymentAppCom() {
		return airOfflnPaymentAppCom;
	}

	public void setAirOfflnPaymentAppCom(String airOfflnPaymentAppCom) {
		this.airOfflnPaymentAppCom = airOfflnPaymentAppCom;
	}

	public String getDisAppCom() {
		return disAppCom;
	}

	public void setDisAppCom(String disAppCom) {
		this.disAppCom = disAppCom;
	}

	public Boolean getCompByProv() {
		return compByProv;
	}

	public void setCompByProv(Boolean compByProv) {
		this.compByProv = compByProv;
	}

	public String getProvComp() {
		return provComp;
	}

	public void setProvComp(String provComp) {
		this.provComp = provComp;
	}

	public String getRemApp() {
		return remApp;
	}

	public void setRemApp(String remApp) {
		this.remApp = remApp;
	}
	
	public String getCcSales() {
		return ccSales;
	}

	public void setCcSales(String ccSales) {
		this.ccSales = ccSales;
	}

	public String getRemRev() {
		return remRev;
	}

	public void setRemRev(String remRev) {
		this.remRev = remRev;
	}
	
}
