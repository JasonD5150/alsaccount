package fwp.alsaccount.utils;


import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.internal.OracleTypes;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;

import fwp.alsaccount.appservice.admin.AlsMiscAS;
import fwp.alsaccount.dao.admin.AlsMisc;
import fwp.alsaccount.dao.admin.AlsProviderInfo;
import fwp.alsaccount.dto.admin.AlsTribeItemDTO;
import fwp.alsaccount.dto.sabhrs.AlsProviderBankDetailsDTO;
import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;
import fwp.alsaccount.dto.sabhrs.AlsTransactionGrpMassCopyDTO;
import fwp.alsaccount.hibernate.HibernateSessionFactory;

public class HibHelpers {
	public Session getSession() {
		return HibernateSessionFactory.getSession();
	}
	
	/*
	 * FISCAL YEAR END PROCESS
	 */
	public Boolean accountingTableCopyCompleted() {
		Boolean rtn = false;
		Integer tmp = 0;
		
		String queryString = "SELECT COUNT(1) count FROM ALS_ORG_CONTROL "
						   + "WHERE asac_budget_year = (SELECT TO_NUMBER(am.am_par_val+1) FROM als.als_misc am "
						   							   + "WHERE am.am_key1 = 'BUDGET YEAR')";

		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("count", IntegerType.INSTANCE);
			
			tmp = (Integer) query.uniqueResult();
			if(tmp > 0){
				rtn = true;
			}
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	//Fiscal Year End Step One
	public Integer accountingTableCopy() {
		Integer rtnCd = 0;

		Connection conn = ((SessionImpl) getSession()).connection();
		try {
			CallableStatement cs = conn
					.prepareCall("{call ALS.ALSU1010(?,?)}");

			cs.setDouble(1, rtnCd);
			cs.registerOutParameter(1, OracleTypes.INTEGER);
			cs.registerOutParameter(2, OracleTypes.DATE);
			cs.execute();

			rtnCd = cs.getInt(1);

			conn.close();
			getSession().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
		return rtnCd;
	}
	
	//Fiscal Year End Step Two
	public String updateAccountingCodes(Date upf, Date upt, String itc, String budgYear) {
		String outString = "";
		Clob tmpClob = null;
		Connection conn = ((SessionImpl) getSession()).connection();
		try {
			CallableStatement cs = conn
					.prepareCall("{call als.als_accounting.als_update_account_codes (?,?,?,?,?)}");

			cs.setDate(1, upf);
			cs.setDate(2, upt);
			cs.setString(3, itc);
			cs.setString(4, budgYear);
			cs.setClob(5, tmpClob);

			cs.registerOutParameter(5, OracleTypes.CLOB);
			cs.execute();
			
			tmpClob = cs.getClob(5);
			
			if (tmpClob != null && tmpClob.length() > 10)
				outString = tmpClob.getSubString(1, (int) tmpClob.length());
			else {
				outString = null;
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
		return outString;
	}
	
	public String updateAccountingCodesAll() {
		String outString = "";
		Clob tmpClob = null;
		Connection conn = ((SessionImpl) getSession()).connection();
		try {
			CallableStatement cs = conn
					.prepareCall("{call als.als_accounting.als_update_account_codes_all(?)}");

			cs.setClob(1, tmpClob);

			cs.registerOutParameter(1, OracleTypes.CLOB);
			cs.execute();
			
			tmpClob = cs.getClob(1);
			
			if (tmpClob != null && tmpClob.length() > 10)
				outString = tmpClob.getSubString(1, (int) tmpClob.length());
			else {
				outString = null;
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
		return outString;
	}
	
	public Boolean alsNonAlsTemplateCopyCompleted() {
		Boolean rtn = false;
		Integer tmp = 0;
		
		String queryString = "SELECT COUNT(1) count FROM als.als_non_als_template "
						   + "WHERE anat_budget_year = (SELECT TO_NUMBER(am_par_val+1) FROM als.als_misc "
													 + "WHERE am_key1 = 'BUDGET YEAR')";

		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("count", IntegerType.INSTANCE);
			
			tmp = (Integer) query.uniqueResult();
			if(tmp > 0){
				rtn = true;
			}
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	//Fiscal Year End Step Three
	public Integer alsNonAlsTemplateCopy() {
		Integer rtnCd = 0;

		Connection conn = ((SessionImpl) getSession()).connection();
		try {
			CallableStatement cs = conn
					.prepareCall("{call als.als_accounting.als_copy_non_als_template(?)}");

			cs.setInt(1, rtnCd);
			cs.registerOutParameter(1, OracleTypes.INTEGER);
			cs.execute();

			rtnCd = cs.getInt(1);

			conn.close();
			getSession().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
		return rtnCd;
	}
	
	//Fiscal Year End Step Four
	public void createTransactionGroup(String budgYear) {
		Connection conn = ((SessionImpl) getSession()).connection();
		try {
			CallableStatement cs = conn
					.prepareCall("{call als.als_fy_end.create_sabhrs_entries(?)}");

			cs.setString(1, budgYear);
			cs.execute();

			conn.close();
			getSession().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public AlsProviderInfo getProviderInfo(Integer providerNo) {
		List<AlsProviderInfo> rtn = new ArrayList<AlsProviderInfo>();

		String queryString = "SELECT api_business_nm apiBusinessNm, "+
							 "       ar_business_region arBusinessRegion "+
							 "FROM ALS.ALS_PROVIDER_INFO  "+
							 "WHERE api_provider_no = "+providerNo;

		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("apiBusinessNm", StringType.INSTANCE)
					.addScalar("arBusinessRegion", StringType.INSTANCE)
	
					.setResultTransformer(
							Transformers.aliasToBean(AlsProviderInfo.class));

			rtn = query.list();
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn.get(0);
	}
	
	public Date getBillPeriodEndDate(Integer year) {
		Date rtn = null;

		String queryString = "SELECT MAX(APR_BILLING_TO) maxBillingTo "
						   + "FROM ALS.ALS_PROVIDER_REMITTANCE "
						   + "WHERE APR_BILLING_TO  <= To_Date('30-JUN-"+year+"')";

		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("maxBillingTo", DateType.INSTANCE);

			rtn = (Date) query.uniqueResult();
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	
	/*
	 * MISCELLANEOUS
	 */
	public String getCurrentBudgetYear() {
		String curBudgYear = null;
		
		AlsMiscAS appSer = new AlsMiscAS();
		List<AlsMisc> tmpLst = new ArrayList<AlsMisc>();
		AlsMisc tmp = new AlsMisc();
		
		try {
			String queryString =  " WHERE amKey1 = 'BUDGET YEAR'";
			tmpLst = appSer.findAllByWhere(queryString);
			if(!tmpLst.isEmpty()){
				tmp = tmpLst.get(0);
				curBudgYear = tmp.getAmParVal();			
			}
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return curBudgYear;
	}
	
	public String getCurrentBudgetYearChangeDt() {
		String curBudgYearChangeDt = null;
		
		AlsMiscAS appSer = new AlsMiscAS();
		List<AlsMisc> tmpLst = new ArrayList<AlsMisc>();
		AlsMisc tmp = new AlsMisc();
		
		try {
			String queryString =  " WHERE amKey1 = 'BUDGET YEAR CHANGE DATE'";
			tmpLst = appSer.findAllByWhere(queryString);
			if(!tmpLst.isEmpty()){
				tmp = tmpLst.get(0);
				curBudgYearChangeDt = tmp.getAmParVal();			
			}
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return curBudgYearChangeDt;
	}
	
	//Upload SABHRS entries into the Summary
	public Integer uploadSabhrsToSum() {
		Integer rtnCd = 0;

		Connection conn = ((SessionImpl) getSession()).connection();
		try {
			CallableStatement cs = conn
					.prepareCall("{call ALS.ALSU0290(?,?)}");

			cs.setDouble(1, rtnCd);
			cs.registerOutParameter(1, OracleTypes.INTEGER);
			cs.registerOutParameter(2, OracleTypes.DATE);
			cs.execute();

			rtnCd = cs.getInt(1);

			conn.close();
			getSession().close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
		return rtnCd;
	}
	
	public String getBusinessUnit() {
		String curBudgYear = null;
		
		AlsMiscAS appSer = new AlsMiscAS();
		List<AlsMisc> tmpLst = new ArrayList<AlsMisc>();
		AlsMisc tmp = new AlsMisc();
		
		try {
			String queryString =  " WHERE amKey1 = 'BUSINESS UNIT'";
			tmpLst = appSer.findAllByWhere(queryString);
			if(!tmpLst.isEmpty()){
				tmp = tmpLst.get(0);
				curBudgYear = tmp.getAmParVal();			
			}
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return curBudgYear;
	}
	
	@SuppressWarnings("unchecked")
	public Integer getTransGrpBudgYear(Integer transCd, String grpIdentifier) {
		Integer rtn = null;
		
		String queryString =  "SELECT DISTINCT "
				+ "asac_budget_year year "
				+ "FROM als.als_sabhrs_entries "
				+ "WHERE atg_transaction_cd = "+transCd+" "
				+ "AND atgs_group_identifier = '"+grpIdentifier+"' "
				+ "AND ase_allow_upload_to_summary = 'Y'";
		
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("year", StringType.INSTANCE);

			List<String> tmpLst = query.list();
			
			if(!tmpLst.isEmpty() && tmpLst.size() == 1){
				rtn = Integer.parseInt(tmpLst.get(0));
			}
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	@SuppressWarnings("unchecked")
	public Integer getTransGrpProgYear(Integer transCd, String grpIdentifier) {
		Integer rtn = null;
		
		String queryString =  "SELECT DISTINCT "
				+ "asac_program year "
				+ "FROM als.als_sabhrs_entries "
				+ "WHERE atg_transaction_cd = "+transCd+" "
				+ "AND atgs_group_identifier = '"+grpIdentifier+"' "
				+ "AND ase_allow_upload_to_summary = 'Y'";
		
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("year", StringType.INSTANCE);
			List<String> tmpLst = query.list();
			if(!tmpLst.isEmpty() && tmpLst.size() == 1){
				rtn = Integer.parseInt(tmpLst.get(0));
			}
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	public String getAirOfflnPaymentApproved(String transGroupIdentifier) {
		String rtn = null;
		
		String queryString =  "SELECT Air_Offln_Payment_Approved rtn "
						    + "FROM Als.Als_Internal_Remittance "
						    + "Where  Api_Provider_No = To_Number(Substr('"+transGroupIdentifier+"',2,6)) "
						    + "AND Air_Billing_To = To_Date(Substr('"+transGroupIdentifier+"',9,10),'YYYY/MM/DD')";
		
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("rtn", StringType.INSTANCE);
			rtn = (String) query.uniqueResult();
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	public StringBuffer getTransGroupStatHistRpt(String where) {		

		StringBuffer rtrn = new StringBuffer();
		
		Clob tmpClob = null;
       
        Connection conn = ((SessionImpl)getSession()).connection();
        try {

			CallableStatement cs = conn.prepareCall("{call ALS.ALS_ACCOUNTING.trans_grp_stat_history_rpt(?,?)}");
			
            cs.setString(1,where);
            cs.setClob(2,tmpClob);
            cs.registerOutParameter(2,OracleTypes.CLOB);

            cs.execute();
            
            tmpClob = cs.getClob(2);
            
            if ( tmpClob != null ) {
            	rtrn.append(tmpClob.getSubString(1, (int) tmpClob.length()));
            } else {
            	rtrn.append("No values returned");
            }
            
			
            conn.close();
            
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			getSession().close();
		}
        
        return rtrn;
	}	
	
	public StringBuilder getTreasuryDepositTicket(String txIdentifier, String transCd) {		
		Integer runCd=0;
		StringBuilder rtrn = new StringBuilder();
		
		Clob tmpClob = null;
		
        Connection conn = ((SessionImpl)getSession()).connection();
        try {

			CallableStatement cs = conn.prepareCall("{call ALS.ALS_ACCOUNTING.ALSR0334(?,?,?,?)}");
			
            cs.setString(1,txIdentifier);
            cs.setString(2, transCd);
            cs.setInt(3, runCd);
            cs.setClob(4,tmpClob);
            cs.registerOutParameter(3,OracleTypes.INTEGER);
            cs.registerOutParameter(4,OracleTypes.CLOB);

            cs.execute();
            
            tmpClob = cs.getClob(4);
            
            if ( tmpClob != null ) {
            	rtrn.append(tmpClob.getSubString(1, (int) tmpClob.length()));
            } else {
            	rtrn.append("No values returned");
            }
            
			
            conn.close();
            
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			getSession().close();
		}
        
        return rtrn;
	}	
	
	public Integer getAlsDepIdSeq(String budgetYear, String type) {		
		Integer rtrn = 0;

        Connection conn = ((SessionImpl)getSession()).connection();
        try {

			CallableStatement cs = conn.prepareCall("{? = call ALS.ALS_GET_DEP_ID_SEQ(?,?)}");
			
			cs.registerOutParameter(1,OracleTypes.NUMBER);
            cs.setString(2, budgetYear);
            cs.setString(3, type);           
           
            cs.execute();            
            
            rtrn = cs.getInt(1);
            conn.close();
            
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			getSession().close();
		}
        return rtrn;
	}	
	
	public Integer getTransGroupCnt(String transGroupIdentifier) {
		Integer cnt = 0;
		String ipTranGrp = transGroupIdentifier.substring(0, 17);
		String queryString =  "SELECT COUNT(*) cnt "
						    + "FROM als.als_transaction_grp_status "
						    + "WHERE '"+transGroupIdentifier+"' IN "
															    + "(SELECT MIN(atgs_group_identifier) "
															    + "FROM als.als_transaction_grp_status "
															    + "WHERE atg_transaction_cd = 8 "
															    + "AND atgs_group_identifier LIKE '"+ipTranGrp+"%' "
															    + "UNION "
															    + "SELECT MAX(atgs_group_identifier) "
															    + "FROM als.als_transaction_grp_status "
															    + "WHERE atg_transaction_cd = 8 "
															    + "AND atgs_group_identifier LIKE '"+ipTranGrp+"%')";
		
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("cnt", IntegerType.INSTANCE);
			cnt = (Integer) query.uniqueResult();
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return cnt;
	}
	
	@SuppressWarnings("unchecked")
	public List<AlsTransactionGrpMassCopyDTO> getTransGroupMassApprovalRecords(Date bpe, Date opa) {
		List<AlsTransactionGrpMassCopyDTO> lst = new ArrayList<AlsTransactionGrpMassCopyDTO>();

		String queryString =  "SELECT SUBSTR(a.atgs_group_identifier, 2, 6) providerNo, "
								   + "TO_DATE(SUBSTR(a.atgs_group_identifier, 9, 10), 'YYYY/MM/DD') bpe, "
								   + "SUM(NVL(a.atgs_net_dr_cr, 0)) atgsNetDrCr,"
								   + "(SELECT api.api_business_nm FROM als_provider_info api WHERE api.api_provider_no = TRIM(SUBSTR(a.atgs_group_identifier, 2, 6))) providerName,"
								   + "(SELECT DECODE(NVL(Apr_Remitt_Per_Status,'N'),'D','Delinquent',"
								   												 + "'O','Off-line Payment Due',"
								   												 + "'OP','Off-line Payment Pending',"
								   												 + "'P','PAE Generated',"
								   												 + "'I','Investigation',"
								   												 + "'C','Collected Outside of ALS',"
								   												 + "'None')"
								   + "FROM ALS.Als_Provider_Remittance "
								   + "WHERE  Api_Provider_No = TO_NUMBER(SUBSTR(a.atgs_group_identifier, 2, 6))"
								   + "AND  Apr_Billing_To = TO_DATE(SUBSTR(a.atgs_group_identifier, 9, 10), 'YYYY/MM/DD')) remPerStat "
						    + "FROM ALS.Als_Transaction_Grp_status A "
						    + "WHERE a.atg_transaction_cd = 8 "
						    + "AND a.atgs_interface_status IS NULL "
						    + "AND LENGTH(a.atgs_group_identifier) = 22 ";
				if(bpe != null){
					queryString += "AND TO_DATE(SUBSTR(a.atgs_group_identifier, 9, 10),'YYYY/MM/DD') = TO_DATE('"+bpe+"','YYYY/MM/DD') ";
				}else{
					queryString += "AND SUBSTR(a.atgs_group_identifier, 9, 10) = SUBSTR(a.atgs_group_identifier, 9, 10) ";
				}
				queryString    += "AND EXISTS (SELECT 1 "
						    		    + "FROM als.als_internal_remittance b "
						    		    + "WHERE b.api_provider_no = TO_NUMBER(SUBSTR(a.atgs_group_identifier, 2, 6)) "
						    		    + "AND b.air_billing_to = TO_DATE(SUBSTR(a.atgs_group_identifier, 9, 10), 'YYYY/MM/DD') "
						    		    + "AND b.air_offln_payment_approved = 'Y' "
						    		    + "AND b.air_offln_payment_app_dt = ";
				if(opa != null){
					queryString += opa+") ";
				}else{
					queryString += "b.air_offln_payment_app_dt) ";
				}
				queryString += "GROUP BY "
						    + "SUBSTR(a.atgs_group_identifier, 2, 6), "
						    + "TO_DATE(SUBSTR(a.atgs_group_identifier, 9, 10), 'YYYY/MM/DD') "
						    + "ORDER BY 1, 2";
		
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("providerNo", IntegerType.INSTANCE)
					.addScalar("bpe", DateType.INSTANCE)
					.addScalar("atgsNetDrCr", DoubleType.INSTANCE)
					.addScalar("providerName", StringType.INSTANCE)
					.addScalar("remPerStat", StringType.INSTANCE)
	
					.setResultTransformer(
							Transformers.aliasToBean(AlsTransactionGrpMassCopyDTO.class));

			lst = query.list();
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return lst;
	}
@SuppressWarnings("unchecked")
public List<AlsTribeItemDTO> findTribeBankItems()
	{
		
		List<AlsTribeItemDTO> toReturn = new ArrayList<AlsTribeItemDTO>();
		String queryString = "SELECT DISTINCT a.AICT_USAGE_PERIOD_FROM, a.AICT_USAGE_PERIOD_TO, a.AICT_ITEM_TYPE_CD , b.AIT_TYPE_DESC "
				+ "FROM ALS.ALS_ITEM_CONTROL_TABLE a, ALS.ALS_ITEM_TYPE b, als.als_tribe_item_info c "
				+ "WHERE a.AICT_ITEM_TYPE_CD  = b.AI_ITEM_ID||b.AIC_CATEGORY_ID||b.AIT_TYPE_ID "
				+ "AND AICT_ITEM_TRIBAL_IND = 'Y' "
				+ "ORDER BY AICT_USAGE_PERIOD_FROM DESC, AICT_USAGE_PERIOD_TO DESC, AICT_ITEM_TYPE_CD ";
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					/*.addScalar("providerNo", IntegerType.INSTANCE)
					.addScalar("bpe", DateType.INSTANCE)
					.addScalar("atgsNetDrCr", DoubleType.INSTANCE)
					.addScalar("providerName", StringType.INSTANCE)
					.addScalar("remPerStat", StringType.INSTANCE)*/
	
					.setResultTransformer(
							Transformers.aliasToBean(AlsTribeItemDTO.class));

			toReturn = query.list();
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return toReturn;
		
	}
	@SuppressWarnings("unchecked")
	public List<AlsSabhrsEntriesDTO> getSabhrsQueryRecords(String queryStr) {
		List<AlsSabhrsEntriesDTO> lst = new ArrayList<AlsSabhrsEntriesDTO>();

		String queryString = queryStr;
		
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("aseWhenEntryPosted")
					.addScalar("aseSeqNo", IntegerType.INSTANCE)
					.addScalar("aseDrCrCd")
					.addScalar("aseTxnCdSeqNo", IntegerType.INSTANCE)
					.addScalar("asacBudgetYear", IntegerType.INSTANCE)
					.addScalar("asacSystemActivityTypeCd")
					.addScalar("asacTxnCd")
					.addScalar("aamAccount")
					.addScalar("aamBusinessUnit")
					.addScalar("aamFund")
					.addScalar("aocOrg")
					.addScalar("asacProgram", IntegerType.INSTANCE)
					.addScalar("asacSubclass")
					.addScalar("asacProjectGrant")
					.addScalar("jlr")
					.addScalar("aseAmt", DoubleType.INSTANCE)
					.addScalar("aseAllowUploadToSummary")
					.addScalar("upToSummDt")
					.addScalar("asesSeqNo", IntegerType.INSTANCE)
					.addScalar("apiProviderNo", IntegerType.INSTANCE)
					.addScalar("bpFromDt")
					.addScalar("bpToDt")
					.addScalar("aiafaSeqNo", IntegerType.INSTANCE)
					.addScalar("aseWhoLog")
					.addScalar("aseWhenLog")
					.addScalar("aseWhenUploadedToSumm")
					.addScalar("atgTransactionCd", IntegerType.INSTANCE)
					.addScalar("atgsGroupIdentifier")
					.addScalar("aseNonAlsFlag")
					.addScalar("aseLineDescription")
					.addScalar("atiTribeCd")
					.addScalar("anatCd")
					.addScalar("gridKey")
					.addScalar("sumStat")
					.addScalar("intStat")
	
					.setResultTransformer(
							Transformers.aliasToBean(AlsSabhrsEntriesDTO.class));

			lst = query.list();
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return lst;
	}
	
	public Integer getSabhrsQueryCount(String queryStr) {
		Integer cnt = 0;

		String queryString = queryStr;
		
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("cnt", IntegerType.INSTANCE);

			cnt = (Integer) query.uniqueResult();
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return cnt;
	}
	
	@SuppressWarnings("unchecked")
	public List<AlsProviderBankDetailsDTO> getIntProviderTreasuryDepositTickets(String queryStr) {
		List<AlsProviderBankDetailsDTO> lst = new ArrayList<AlsProviderBankDetailsDTO>();

		String queryString = queryStr;
		
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("gridKey", StringType.INSTANCE)
					.addScalar("providerNo", IntegerType.INSTANCE)
					.addScalar("providerName", StringType.INSTANCE)
					.addScalar("apbdAmountDeposit", DoubleType.INSTANCE)
					.addScalar("apbdDepositDate", TimestampType.INSTANCE)
					.addScalar("apbdDepositId", StringType.INSTANCE)
	
					.setResultTransformer(
							Transformers.aliasToBean(AlsProviderBankDetailsDTO.class));

			lst = query.list();
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return lst;
	}
	
	public StringBuilder loadTmpDepositIds(String key, String ids) {
		StringBuilder rtrn = new StringBuilder();
		
		Clob tmpClob = null;
		
        Connection conn = ((SessionImpl)getSession()).connection();
        try {
			CallableStatement cs = conn
					.prepareCall("{call als.als_accounting.load_temp_alsr0908(?,?,?)}");

			cs.setString(1, key);
			cs.setString(2, ids);
            cs.setClob(3,tmpClob);
            cs.registerOutParameter(3,OracleTypes.CLOB);

            cs.execute();
            
            tmpClob = cs.getClob(3);
            
            if ( tmpClob != null ) {
            	rtrn.append(tmpClob.getSubString(1, (int) tmpClob.length()));
            } else {
            	rtrn.append("No values returned");
            }
            
			
            conn.close();
            
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			getSession().close();
		}
        
        return rtrn;
	}
}

