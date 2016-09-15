package fwp.alsaccount.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import fwp.als.hibernate.admin.dao.AlsActivityAccountLinkage;
import fwp.als.hibernate.admin.dao.AlsSysActivityControl;
import fwp.als.hibernate.admin.dao.AlsSysActivityTypeCodes;
import fwp.als.hibernate.admin.dao.AlsSysActivityTypeTranCds;
import fwp.als.hibernate.provider.dao.AlsProviderInfo;
import fwp.alsaccount.appservice.admin.AlsAccountMasterAS;
import fwp.alsaccount.appservice.admin.AlsActivityAccountLinkageAS;
import fwp.alsaccount.appservice.admin.AlsNonAlsTemplateAS;
import fwp.alsaccount.appservice.admin.AlsOrgControlAS;
import fwp.alsaccount.appservice.admin.AlsSysActivityControlAS;
import fwp.alsaccount.appservice.admin.AlsSysActivityTypeCodesAS;
import fwp.alsaccount.appservice.admin.AlsSysActivityTypeTranCdsAS;
import fwp.alsaccount.dao.admin.AlsAccountMaster;
import fwp.alsaccount.dao.admin.AlsOrgControl;
import fwp.alsaccount.dto.sabhrs.InternalProviderBankCdDepLinkDTO;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.Utils;
import fwp.utils.FwpNumberUtils;




public class GenDocCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     try {
       	 
       	String rptType = request.getParameter("rptType");
       	/*String genToOpt = request.getParameter("genToOpt");
       	String rptBody = request.getParameter("rptBody");
       	String fileName = request.getParameter("reportName");*/
       	
       	if (rptType.equals("sysActivityControl")){
       		sysActivityControlCSV(request, response);
       	} else if (rptType.equals("accountMaster")){
       		accountMasterCSV(request, response);
       	} else if (rptType.equals("activityAccountLinkage")){
       		activityAccountLinkageCSV(request, response);
       	} else if (rptType.equals("appendixM")){
       		appendixMCSV(request, response);
       	} else if (rptType.equals("orgControl")){
       		orgControlCSV(request, response);
       	} else if (rptType.equals("alsNonAlsTemplate")){
       		alsNonAlsTemplateCSV(request, response);
       	} else if (rptType.equals("updateAccCd")){
       		alsUpdateAccCdWord(request, response);
       	} else if (rptType.equals("transGrpStatHistory")){
       		alsTransGrpStatHistory(request, response);
       	}else if (rptType.equals("intProvBankCdDepLink")){
       		intProvBankCdDepLink(request, response);
       	}
      } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	  }
		
	}	

	
	
	@SuppressWarnings("unchecked")
	public void sysActivityControlCSV(HttpServletRequest request,HttpServletResponse htmlResp) throws ParseException, IOException, JSONException {	

		AlsSysActivityControlAS asacAS = new AlsSysActivityControlAS();
		List<AlsSysActivityControl> tmpLst = new ArrayList<AlsSysActivityControl>();
		StringBuffer hold = new StringBuffer("");
		
		// Retrieve grid filters.
		String filters = request.getParameter("filters");
		String year = request.getParameter("budgYear");
		
		String where = " where idPk.asacBudgetYear = "+year;
    	String orderStr = " order by idPk.asacBudgetYear desc,idPk.asacSystemActivityTypeCd,cast(idPk.asacTxnCd as int) asc";
		
		if(filters != null){
    		where = buildStr(where, filters);
    	}
		
		tmpLst = asacAS.findAllByWhere(where+orderStr);
		//header
		
		hold.append("System Activity Control Codes");
		hold.append(System.getProperty("line.separator"));
		hold.append("Budget Year "+ year);
		hold.append(System.getProperty("line.separator"));
		hold.append("System Activity Control Code, Transaction Code, Desc, Program");
		for(AlsSysActivityControl tmp : tmpLst){
			hold.append(System.getProperty("line.separator"));
			hold.append(tmp.getIdPk().getAsacSystemActivityTypeCd()+","+
						tmp.getIdPk().getAsacTxnCd()+","+
						tmp.getAsacSysActivityTypeCdDesc().replace(",","")+","+
						tmp.getAsacProgram());
		}	

		genCSVCreate("sysActivityControlCSV", hold, htmlResp);	
		
	}
	
	@SuppressWarnings("unchecked")
	public void accountMasterCSV(HttpServletRequest request,HttpServletResponse htmlResp) throws ParseException, IOException, JSONException {	

		AlsAccountMasterAS aamAS = new AlsAccountMasterAS();
		List<AlsAccountMaster> tmpLst = new ArrayList<AlsAccountMaster>();
		StringBuffer hold = new StringBuffer("");
		
		// Retrieve grid filters.
		String filters = request.getParameter("filters");
		String year = request.getParameter("budgYear");
		
		String where = " where idPk.asacBudgetYear = "+year;
    	String orderStr = " order by idPk.asacBudgetYear desc, idPk.aamAccount";
    	
		if(filters != null){
    		where = buildStr(where, filters);
    	}
		
		tmpLst = aamAS.findAllByWhere(where+orderStr);
		//header
		
		hold.append("Account Master");
		hold.append(System.getProperty("line.separator"));
		hold.append("Budget Year "+ year);
		hold.append(System.getProperty("line.separator"));
		hold.append("Account, Desc");
		for(AlsAccountMaster tmp : tmpLst){
			hold.append(System.getProperty("line.separator"));
			hold.append(tmp.getIdPk().getAamAccount()+","+
						tmp.getAamAccountDesc().replace(",",""));
		}	

		genCSVCreate("accountMasterCSV", hold, htmlResp);	
		
	}
	
	@SuppressWarnings("unchecked")
	public void activityAccountLinkageCSV(HttpServletRequest request,HttpServletResponse htmlResp) throws ParseException, IOException, JSONException {	

		AlsActivityAccountLinkageAS appSer = new AlsActivityAccountLinkageAS();
		List<AlsActivityAccountLinkage> tmpLst = new ArrayList<AlsActivityAccountLinkage>();
		StringBuffer hold = new StringBuffer("");
		
		// Retrieve grid filters.
		String filters = request.getParameter("filters");
		String year = request.getParameter("budgYear");
		
		String where = " where idPk.asacBudgetYear = "+year;
    	String orderStr = " order by idPk.asacBudgetYear desc,idPk.asacSystemActivityTypeCd,cast(idPk.asacTxnCd as int) asc, idPk.aaalDrCrCd";
    	
		if(filters != null){
    		where = buildStr(where, filters);
    	}
		
		tmpLst = appSer.findAllByWhere(where+orderStr);
		//header
		
		hold.append("Activity Account Linkage");
		hold.append(System.getProperty("line.separator"));
		hold.append("Budget Year "+ year);
		hold.append(System.getProperty("line.separator"));
		hold.append("Sys Activity Type Code, Transaction Code, Credit / Debit, Account, Open Item Key, Fund, Org, Sub-Class, In Accounting Code Control Table");
		for(AlsActivityAccountLinkage tmp : tmpLst){
			hold.append(System.getProperty("line.separator"));
			hold.append(tmp.getIdPk().getAsacSystemActivityTypeCd()+","+
						tmp.getIdPk().getAsacTxnCd()+","+
						tmp.getIdPk().getAaalDrCrCd()+","+
						Utils.nullFix(tmp.getAamAccount())+",");
			
			if(FwpNumberUtils.nullFix(tmp.getAaalReference()) != 0){
				hold.append(FwpNumberUtils.nullFix(tmp.getAaalReference()));
			}
			
			hold.append(","+
						Utils.nullFix(tmp.getAamFund())+","+
						Utils.nullFix(tmp.getAocOrg())+","+
						Utils.nullFix(tmp.getAsacSubclass())+","+
						Utils.nullFix(tmp.getAaalAccountingCdFlag()));
		}	

		genCSVCreate("accountMasterCSV", hold, htmlResp);	
		
	}
	
	@SuppressWarnings("unchecked")
	public void appendixMCSV(HttpServletRequest request,HttpServletResponse htmlResp) throws ParseException, IOException, JSONException {	
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		
		AlsSysActivityTypeCodesAS asatcAppSer = new AlsSysActivityTypeCodesAS();
		List<AlsSysActivityTypeCodes> asatcTmpLst = new ArrayList<AlsSysActivityTypeCodes>();
		AlsSysActivityTypeTranCdsAS asattcAppSer = new AlsSysActivityTypeTranCdsAS();
		List<AlsSysActivityTypeTranCds> asattcTmpLst = new ArrayList<AlsSysActivityTypeTranCds>();
		
		
		StringBuffer hold = new StringBuffer("");

		
		// Retrieve grid filters.
		String filters = request.getParameter("filters");
		String actTypCd = request.getParameter("actTypCd");
		
		String asatcWhere = " where asacSystemActivityTypeCd = '"+actTypCd+"'" ;
		String asattcWhere = " where idPk.asacSystemActivityTypeCd ='"+actTypCd+"'";
		if(filters != null){
			asattcWhere = buildStr(asattcWhere, filters);
    	}
		
		asatcTmpLst = asatcAppSer.findAllByWhere(asatcWhere);
		asattcTmpLst = asattcAppSer.findAllByWhere(asattcWhere);
		//header
		
		hold.append("Appendix M");
		hold.append(System.getProperty("line.separator"));
		hold.append("Type,Code,Desc,Create User, Create Date, Mod User, Mod Date");
		hold.append(System.getProperty("line.separator"));
		hold.append("Activity Type Codes,,,,,,");
		for(AlsSysActivityTypeCodes tmp : asatcTmpLst){
			hold.append(System.getProperty("line.separator"));
			hold.append(","+tmp.getAsacSystemActivityTypeCd()+","+
						tmp.getAsatcDesc().replace(",","")+","+
						Utils.nullFix(tmp.getAsatcWhoLog())+",");
			hold.append(tmp.getAsatcWhenLog() != null ? formatter.format(tmp.getAsatcWhenLog()) : ""+",");
			hold.append(Utils.nullFix(tmp.getAsatcWhoModi())+",");
			hold.append(tmp.getAsatcWhenModi() != null ? formatter.format(tmp.getAsatcWhenModi()) : "");
		}
		hold.append(System.getProperty("line.separator"));
		hold.append("Transaction Codes,,,,,,");
		for(AlsSysActivityTypeTranCds tmp : asattcTmpLst){
			hold.append(System.getProperty("line.separator"));
			hold.append(","+tmp.getIdPk().getAsacTxnCd()+","+
						tmp.getAsattcDesc().replace(",","").replace("\n","")+","+
						Utils.nullFix(tmp.getAsattcWhoLog())+",");
			hold.append(tmp.getAsattcWhenLog() != null ? formatter.format(tmp.getAsattcWhenLog()) : ""+",");
			hold.append(Utils.nullFix(tmp.getAsattcWhoModi())+",");
			hold.append(tmp.getAsattcWhenModi() != null ? formatter.format(tmp.getAsattcWhenModi()) : "");
		}

		genCSVCreate("appendixMCSV", hold, htmlResp);	
		
	}
	
	@SuppressWarnings("unchecked")
	public void orgControlCSV(HttpServletRequest request,HttpServletResponse htmlResp) throws ParseException, IOException, JSONException {	

		AlsOrgControlAS appSer = new AlsOrgControlAS();
		List<AlsOrgControl> tmpLst = new ArrayList<AlsOrgControl>();
		StringBuffer hold = new StringBuffer("");
		
		// Retrieve grid filters.
		String filters = request.getParameter("filters");
		String year = request.getParameter("budgYear");
		
		String srchStr = " where idPk.asacBudgetYear = "+year;
    	String orderStr = " order by idPk.aaccAccCd asc, idPk.apiProviderNo asc";
    	
		if(filters != null){
			srchStr = buildStr(srchStr, filters);
    	}
		
		tmpLst = appSer.findAllByWhere(srchStr+orderStr);
		//header
		
		hold.append("ORG Control Table");
		hold.append(System.getProperty("line.separator"));
		hold.append("Budget Year "+ year);
		hold.append(System.getProperty("line.separator"));
		hold.append("Account Code, Issuing Provider Num, Org, Provider Region Num, Provider Name");
		
		HibHelpers hh = new HibHelpers();
		AlsProviderInfo api = null;
		for(AlsOrgControl tmp : tmpLst){
			api = hh.getProviderInfo(tmp.getIdPk().getApiProviderNo());
			hold.append(System.getProperty("line.separator"));
			hold.append(tmp.getIdPk().getAaccAccCd()+","+
						tmp.getIdPk().getApiProviderNo()+","+
						tmp.getAocOrg()+","+
						api.getArBusinessRegion()+","+
						api.getApiBusinessNm());
		}	

		genCSVCreate("orgControlCSV", hold, htmlResp);	
		
	}
	
	public void alsNonAlsTemplateCSV(HttpServletRequest request,HttpServletResponse htmlResp) throws ParseException, IOException, JSONException, NumberFormatException, SQLException {	

		AlsNonAlsTemplateAS appSer = new AlsNonAlsTemplateAS();

		StringBuffer hold = new StringBuffer("");
		
		// Retrieve grid filters.
		String year = request.getParameter("rptBudgetYear");
		String providerNum = request.getParameter("rptProviderNum");
		String org = request.getParameter("rptOrg");
		String jlr = request.getParameter("rptJLR");
		String account = request.getParameter("rptAccount");
		String tempToRpt = request.getParameter("tempToReport");
		
		if("".equals(providerNum)){ providerNum = null;}
		if("".equals(org)){ org = null;}
		if("".equals(jlr)){ jlr = null;}
		if("".equals(account)){ account = null;}
		
		//header
		hold.append("Non-ALS SABHRS Entry Template Maintenance Report");
		hold.append("\r\n");
		hold.append("Parameters Selected");
		hold.append("\r\n");
		hold.append("Program Year:"+year);
		if(providerNum != null){
			hold.append("\r\n");
			hold.append("Provider:"+providerNum);
		}
		if(org != null){
			hold.append("\r\n");
			hold.append("Org:"+org);
		}
		if(jlr != null){
			hold.append("\r\n");
			hold.append("JLR: "+jlr);
		}
		if(account != null){
			hold.append("\r\n");
			hold.append("Account: "+account);
		}
		hold.append("\r\n");
		hold.append("Template: ");
		switch(tempToRpt){
		case "A":	
			hold.append("All");
			break;
		case "S":
			hold.append("Single Org");
			break;
		case "M":
			hold.append("Multiple Orgs");
			break;
		case "N":
			hold.append("No Orgs");
			break;
		}
		hold.append("\r\n");
		hold.append(", Template, Description, Budget Year, Account, Fund, Sub-class, Project Grant, JLR, Line Description, Multiple Orgs, Orgs, Provider");
		hold.append("\r\n");
		//Get report data
		hold.append(appSer.genReport(Integer.parseInt(year), providerNum, org, jlr, account, tempToRpt));
		
		genCSVCreate("orgControlCSV", hold, htmlResp);	
		
	}
	
	public void alsUpdateAccCdWord(HttpServletRequest request,HttpServletResponse htmlResp) throws ParseException, IOException, JSONException {	
		String hold = request.getParameter("rptBody");

		genWordCreate("updateAccCd", hold, htmlResp);	
		
	}
	
	public void alsTransGrpStatHistory(HttpServletRequest request,HttpServletResponse htmlResp) throws ParseException, IOException, JSONException {	
		HibHelpers hh = new HibHelpers();
		String filters = request.getParameter("filters");
		String srchStr = " 1=1 ";
		if(filters != null){
			srchStr = buildTransGrpStatHistoryRptStr(srchStr, filters);
    	}
		
		StringBuffer hold = hh.getTransGroupStatHistRpt(srchStr);

		genCSVCreate("TransactionGroupApprovalHistory", hold, htmlResp);
		
	}
	
	public void intProvBankCdDepLink(HttpServletRequest request,HttpServletResponse htmlResp) throws ParseException, IOException, JSONException {	
		HibHelpers hh = new HibHelpers();
		String filters = request.getParameter("filters");
		String provNo = request.getParameter("provNo");
		
		StringBuffer hold = new StringBuffer("Internal Provider Bank Code and Deposit Linkage report\n");
		hold.append("\n");
		hold.append("Provider No: "+provNo+"\n");
		hold.append("\n");
		
		hold.append("Deposit Deadline Date,Billing Period End Date,ALS Amount Due,Bank Code,Bank Name,Amount Deposited,Deposit Date,Deposit Id\n");

		Date curDeadlineDt = null;
		Double total = null;
		List<InternalProviderBankCdDepLinkDTO> tmpLst = hh.getIntProvBankCdDepLinkCSVRecords(Integer.parseInt(provNo));
		for(InternalProviderBankCdDepLinkDTO tmp : tmpLst){
			if(tmp.getDeadlineDate() != null){
				if(curDeadlineDt == null || tmp.getDeadlineDate().after(curDeadlineDt)){
					if(curDeadlineDt != null&&tmp.getDeadlineDate().after(curDeadlineDt)){
						hold.append(",,,,Total:,$"+total+"\n");
					}
					curDeadlineDt = tmp.getDeadlineDate();
					total = tmp.getApbdAmountDeposit();
					hold.append(tmp.getDeadlineDate()+","+tmp.getApbdBillingTo()+",$"+tmp.getAmtDue()+","+tmp.getAbcBankCd()+","+tmp.getBankName()+",$"+tmp.getApbdAmountDeposit()+","+tmp.getDepositDate()+","+tmp.getApbdDepositId()+"\n");
				}else{
					total += tmp.getApbdAmountDeposit();
					hold.append(",,,"+tmp.getAbcBankCd()+","+tmp.getBankName()+",$"+tmp.getApbdAmountDeposit()+","+tmp.getDepositDate()+","+tmp.getApbdDepositId()+"\n");
				}
				if(tmpLst.indexOf(tmp) == (tmpLst.size()-1)){
					hold.append(",,,,Total:,$"+total+"\n");
				}
			}
		}
		genCSVCreate("InternalProviderBankCodeDepositLinkage", hold, htmlResp);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

	}

	public void genPdfCreate(String sFileName,ByteArrayOutputStream mstr,HttpServletResponse htmlResp) {
		htmlResp.reset();
		
	
		htmlResp.setContentType("application/pdf");
		htmlResp.setContentLength(mstr.size());
		htmlResp.addHeader("Content-Disposition", "attachment; filename=\"" + sFileName + ".pdf\"");		
		
		try {
			htmlResp.getOutputStream();

			htmlResp.getOutputStream().write(mstr.toByteArray());
			htmlResp.getOutputStream().flush();
			htmlResp.getOutputStream().close();
		} catch (IOException e) {
			System.err.println(e);
		}		
	}
	
	public void genWordCreate(String sFileName,String mstr,HttpServletResponse htmlResp) {
		htmlResp.reset();
		
		htmlResp.setContentType("application/vnd.ms-word");
		htmlResp.setContentLength(mstr.length());
		htmlResp.addHeader("Content-Disposition", "attachment; filename=\"" + sFileName + ".doc\"");
		try {
			htmlResp.getOutputStream();
			byte[] bCSV = mstr.toString().getBytes();
			htmlResp.getOutputStream().write(bCSV);
			htmlResp.getOutputStream().flush();
			htmlResp.getOutputStream().close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public void genCSVCreate(String sFileName,StringBuffer mstr,HttpServletResponse htmlResp) {	
		htmlResp.reset();
	
	    
		htmlResp.setContentType("text/csv");
		htmlResp.setContentLength(mstr.length());
		htmlResp.addHeader("Content-Disposition", "attachment; filename=\"" + sFileName + ".csv\"");
		try {
			htmlResp.getOutputStream();
			byte[] bCSV = mstr.toString().getBytes();
			htmlResp.getOutputStream().write(bCSV);
			htmlResp.getOutputStream().flush();
			htmlResp.getOutputStream().close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void genExcelCreate(String sFileName, StringBuffer mstr, HttpServletResponse htmlResp) {
		htmlResp.reset();

		Cookie userCookie = new Cookie("alsRpts", "alsRpts");
		htmlResp.addCookie(userCookie);

		htmlResp.setContentType("application/vnd.ms-excel");
		htmlResp.setContentLength(mstr.length());
		htmlResp.addHeader("Content-Disposition", "attachment; filename=\""
				+ sFileName + ".xls\"");
		try {
			htmlResp.getOutputStream();
			byte[] bCSV = mstr.toString().getBytes();
			htmlResp.getOutputStream().write(bCSV);
			htmlResp.getOutputStream().flush();
			htmlResp.getOutputStream().close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String buildStr(String where,String filters){
    	try {
            Hashtable<String,Object> jsonFilter = (Hashtable<String, Object>) (new gov.fwp.mt.RPC.FWPJsonRpc().new JsonParser(filters)).FromJson();
            String groupOp = (String) jsonFilter.get("groupOp");
            ArrayList rules = (ArrayList) jsonFilter.get("rules");

            int rulesCount = rules.size();
            String tmpCond = "";
            
    		for (int i = 0; i < rulesCount; i++) {
    			Hashtable<String,String> rule = (Hashtable<String, String>) rules.get(i);
    			
    			String tmp = rule.get("field");
    			
    			if("provider".equals(tmp)){
    				tmp = "TRIM(TRIM(LEADING 0 FROM substr(idPk.atgsGroupIdentifier,3,6))) ";
    			}
    			
    			if (i == 0) {
    				tmpCond = "and (";
    			} else {
    				tmpCond = groupOp;
    			}
    			
    	        if (rule.get("op").equalsIgnoreCase("eq")) {		    	  
    	        	where = where + " " +tmpCond+" " + tmp + " = '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("ne")) {
    	        	where = where + " " +tmpCond+" " + tmp + " <> '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("lt")) {
    	        	where = where + " " +tmpCond+" " + tmp + " < '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("gt")) {
    	        	where = where + " " +tmpCond+" " + tmp + " > '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("cn")) {
    	        	if("sysActTypeTransCd".equals(tmp)){
    	        		String[] dataSplit = rule.get("data").split("");
    	        		String transCd = "";
    	        		for (int g = 0;g < dataSplit.length; g++){
    	        		    if(dataSplit[g].matches("[A-Za-z]")){
    	        		    	where = where + " and (asac_system_activity_type_cd = '"+dataSplit[g].toUpperCase()+"')";
    	        		    } else if (dataSplit[g].matches("[0-9]")){
    	        		    	transCd += dataSplit[g];
    	        		    }
    	        		} 
    	        		if(transCd != null){
    	        			where = where + " and (asac_txn_cd = "+transCd;
    	        		}  	        	
    	        	}else{
    	        		where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('%" + rule.get("data")+"%')";
    	        	}
    		    } else if (rule.get("op").equalsIgnoreCase("bw")) {
    		    	where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('" + rule.get("data")+"%')";
    	        } else if (rule.get("op").equalsIgnoreCase("ew")) {
    	        	where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('%" + rule.get("data")+"')";
    	        }			
    		}
    		 where = where + ")";	
     
    		  }
    		  catch(Exception ex) {	  
    		  }
          return where;
    	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String buildTransGrpStatHistoryRptStr(String where,String filters){
    	try {
            Hashtable<String,Object> jsonFilter = (Hashtable<String, Object>) (new gov.fwp.mt.RPC.FWPJsonRpc().new JsonParser(filters)).FromJson();
            String groupOp = (String) jsonFilter.get("groupOp");
            ArrayList rules = (ArrayList) jsonFilter.get("rules");

            int rulesCount = rules.size();
            String tmpCond = "";
            
    		for (int i = 0; i < rulesCount; i++) {
    			Hashtable<String,String> rule = (Hashtable<String, String>) rules.get(i);
    			
    			String tmp = rule.get("field");
    			
    			if("provider".equals(tmp)){
    				tmp = "TRIM(TRIM(LEADING 0 FROM substr(idPk.atgsGroupIdentifier,3,6))) ";
    			}else if("atgsSummaryStatus".equals(tmp)){
    				tmp = "ATGS_SUMMARY_STATUS";
    			}else if("atgsInterfaceStatus".equals(tmp)){
    				tmp = "ATGS_INTERFACE_STATUS";
    			}else if("idPk.atgsSummaryDt".equals(tmp)){
    				tmp = "ATGS_SUMMARY_DT";
    			}else if("atgsInterfaceDt".equals(tmp)){
    				tmp = "ATGS_INTERFACE_DT";
    			}else if("atgsSummaryApprovedBy".equals(tmp)){
    				tmp = "ATGS_SUMMARY_APPROVED_BY";
    			}else if("atgsInterfaceApprovedBy".equals(tmp)){
    				tmp = "ATGS_INTERFACE_APPROVED_BY";
    			}else if("atgsWhenUploadToSummary".equals(tmp)){
    				tmp = "ATGS_WHEN_UPLOAD_TO_SUMMARY";
    			}else if("atgsFileName".equals(tmp)){
    				tmp = "ATGS_FILE_NAME";
    			}else if("atgsRemarks".equals(tmp)){
    				tmp = "ATGS_REMARKS";
    			}else if("abcBankCd".equals(tmp)){
    				tmp = "ABC_BANK_CD";
    			}else if("atgsAccountingDt".equals(tmp)){
    				tmp = "ATGS_ACCOUNTING_DT";
    			}else if("ATGS_NET_DR_CR".equals(tmp)){
    				tmp = "atgsNetDrCr";
    			}else if("atgsNonAlsFlag".equals(tmp)){
    				tmp = "ATGS_NON_ALS_FLAG";
    			}else if("atgsWhenUploadedToSabhrs".equals(tmp)){
    				tmp = "ATGS_WHEN_UPLOADED_TO_SABHRS";
    			}else if("atgsDepositId".equals(tmp)){
    				tmp = "ATGS_DEPOSIT_ID";
    			}else if("atgsArGlFlag".equals(tmp)){
    				tmp = "ATGS_AR_GL_FLAG";
    			}else if("atgsFileCreationDt".equals(tmp)){
    				tmp = "ATGS_FILE_CREATION_DT";
    			}else if("atgsBankReferenceNo".equals(tmp)){
    				tmp = "ATGS_BANK_REFERENCE_NO";
    			}else if("atgsFyePriorProgram".equals(tmp)){
    				tmp = "ATGS_FYE_PRIOR_PROGRAM";
    			}else if("idPk.atgsGroupIdentifier".equals(tmp)){
    				tmp = "atgs_group_identifier";
    			}
    			
    			if (i == 0) {
    				tmpCond = "and (";
    			} else {
    				tmpCond = groupOp;
    			}
    			
    	        if (rule.get("op").equalsIgnoreCase("eq")) {		    	  
    	        	where = where + " " +tmpCond+" " + tmp + " = '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("ne")) {
    	        	where = where + " " +tmpCond+" " + tmp + " <> '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("lt")) {
    	        	where = where + " " +tmpCond+" " + tmp + " < '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("gt")) {
    	        	where = where + " " +tmpCond+" " + tmp + " > '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("cn")) {
    	        		where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('%" + rule.get("data")+"%')";
    		    } else if (rule.get("op").equalsIgnoreCase("bw")) {
    		    	where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('" + rule.get("data")+"%')";
    	        } else if (rule.get("op").equalsIgnoreCase("ew")) {
    	        	where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('%" + rule.get("data")+"')";
    	        }			
    		}
    		 where = where + ")";	
     
    		  }
    		  catch(Exception ex) {	  
    		  }
          return where;
    	}
	
}
