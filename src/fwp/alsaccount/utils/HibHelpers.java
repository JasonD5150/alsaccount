package fwp.alsaccount.utils;


import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.internal.OracleTypes;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;

import fwp.als.hibernate.admin.dao.AlsMisc;
import fwp.als.hibernate.provider.dao.AlsProviderInfo;
import fwp.alsaccount.appservice.admin.AlsMiscAS;
import fwp.alsaccount.dto.admin.AccCdDistByItemTypeDTO;
import fwp.alsaccount.dto.admin.AlsTribeItemDTO;
import fwp.alsaccount.dto.sabhrs.AlsProviderBankDetailsDTO;
import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;
import fwp.alsaccount.dto.sabhrs.AlsTransactionGrpMassCopyDTO;
import fwp.alsaccount.dto.sabhrs.IafaDetailsDTO;
import fwp.alsaccount.dto.sabhrs.InternalProviderTdtDTO;
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
	public List<AlsTransactionGrpMassCopyDTO> getTransGroupMassApprovalRecords(String queryStr) {
		List<AlsTransactionGrpMassCopyDTO> lst = new ArrayList<AlsTransactionGrpMassCopyDTO>();

		try {
			Query query = getSession()
					.createSQLQuery(queryStr)
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
	public List<AlsTribeItemDTO> findTribeBankItems(String tribeId)
		{
			
			List<AlsTribeItemDTO> toReturn = new ArrayList<AlsTribeItemDTO>();
			String queryString = "SELECT DISTINCT a.AICT_USAGE_PERIOD_FROM||a.AICT_USAGE_PERIOD_TO||a.AICT_ITEM_TYPE_CD itemKey, a.AICT_USAGE_PERIOD_FROM aictUsagePeriodFrom, a.AICT_USAGE_PERIOD_TO aictUsagePeriodTo, a.AICT_ITEM_TYPE_CD aictItemTypeCd, b.AIT_TYPE_DESC aitTypeDesc "
					+ "FROM ALS.ALS_ITEM_CONTROL_TABLE a, ALS.ALS_ITEM_TYPE b, als.als_tribe_item_info c "
					+ "WHERE a.AICT_ITEM_TYPE_CD  = b.AI_ITEM_ID||b.AIC_CATEGORY_ID||b.AIT_TYPE_ID "
					+ "AND AICT_ITEM_TRIBAL_IND = 'Y' "
					+ "AND c.ati_tribe_cd = :tribeId "
					+ "AND a.aict_item_type_cd = c.aict_item_type_cd "
					+ "AND c.aict_usage_period_from = a.aict_usage_period_from "
					+ "AND c.aict_usage_period_to = a.aict_usage_period_to "
					+ "ORDER BY aictUsagePeriodFrom DESC, aictUsagePeriodTo DESC, aictItemTypeCd DESC";
			try {
				Query query = getSession()
						.createSQLQuery(queryString)
						.addScalar("itemKey", StringType.INSTANCE)
						.addScalar("aitTypeDesc", StringType.INSTANCE)
						.addScalar("aictUsagePeriodFrom", DateType.INSTANCE)
						.addScalar("aictUsagePeriodTo", DateType.INSTANCE)
						.addScalar("aictItemTypeCd", StringType.INSTANCE)
		
						.setResultTransformer(
								Transformers.aliasToBean(AlsTribeItemDTO.class));
				 query.setString("tribeId", tribeId);

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
	public List<AlsTribeItemDTO> findUnusedTribeBankItems(String tribeId)
		{
			
			List<AlsTribeItemDTO> toReturn = new ArrayList<AlsTribeItemDTO>();
			String queryString =  " SELECT DISTINCT d.itemkey itemKey, d.aictusageperiodfrom aictUsagePeriodFrom, d.aictusageperiodto aictUsagePeriodTo, d.aictitemtypecd aictItemTypeCd, d.aittypedesc aitTypeDesc, c.ati_tribe_cd"
								+ " FROM (SELECT DISTINCT a.AICT_USAGE_PERIOD_FROM||a.AICT_USAGE_PERIOD_TO||a.AICT_ITEM_TYPE_CD itemKey, "
								+ " 	         a.AICT_USAGE_PERIOD_FROM aictUsagePeriodFrom,"
								+ "     	     a.AICT_USAGE_PERIOD_TO aictUsagePeriodTo,"
								+ "              a.AICT_ITEM_TYPE_CD aictItemTypeCd,"
								+ "              b.AIT_TYPE_DESC aitTypeDesc    "
								+ "       FROM ALS.ALS_ITEM_CONTROL_TABLE a,"
								+ "              ALS.ALS_ITEM_TYPE b"
	                            + "       WHERE a.AICT_ITEM_TYPE_CD  = b.AI_ITEM_ID||b.AIC_CATEGORY_ID||b.AIT_TYPE_ID"
	                            + "       AND AICT_ITEM_TRIBAL_IND = 'Y') d"
	                            + " LEFT JOIN ALS.Als_Tribe_Item_Info c"
	                            + " ON c.aict_usage_period_from = aictUsagePeriodFrom"
	                            + " and c.aict_usage_period_to = aictUsagePeriodTo"
	                            + " and c.aict_item_type_cd = aictItemTypeCd"
	                            + " WHERE c.ati_tribe_cd IS NULL";
			try {
				Query query = getSession()
						.createSQLQuery(queryString)
						.addScalar("itemKey", StringType.INSTANCE)
						.addScalar("aitTypeDesc", StringType.INSTANCE)
						.addScalar("aictUsagePeriodFrom", DateType.INSTANCE)
						.addScalar("aictUsagePeriodTo", DateType.INSTANCE)
						.addScalar("aictItemTypeCd", StringType.INSTANCE)
		
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
	public List<AlsSabhrsEntriesDTO> getSabhrsQueryRecords(Integer providerNo, Integer seqNo, Date bpFromDt, Date bpToDt,
															Date fromDt, Date toDt, String sumAppStat, String intAppStat, String jlr,
															String account, String fund, String org, String subclass, String tribeCd,
															String txnGrpIdentifier, Integer budgYear, Integer progYear, String sysActTypeCd,
															String 	transGrpType) {
		List<AlsSabhrsEntriesDTO> lst = new ArrayList<AlsSabhrsEntriesDTO>();
		StringBuilder queryStr = new StringBuilder("SELECT ase.ase_When_Entry_Posted aseWhenEntryPosted, "
						+ "ase.ase_Seq_No aseSeqNo, "
						+ "ase.ase_Dr_Cr_Cd aseDrCrCd, "
						+ "ase.ase_Txn_Cd_Seq_No aseTxnCdSeqNo, "
						+ "ase.asac_Budget_Year asacBudgetYear, "
						+ "ase.asac_System_Activity_Type_Cd asacSystemActivityTypeCd, "
						+ "ase.asac_Txn_Cd asacTxnCd, " 
						+ "ase.aam_Account aamAccount, "
						+ "ase.aam_Business_Unit aamBusinessUnit, "
						+ "ase.aam_Fund aamFund, "
						+ "ase.aoc_Org aocOrg, "
						+ "ase.asac_Program asacProgram, "
						+ "ase.asac_Subclass asacSubclass, "
						+ "ase.asac_Project_Grant asacProjectGrant, "
						+ "(SELECT DISTINCT AM_VAL_DESC||SUBSTR(ase.asac_Reference,-2) rtn "
									+ "FROM ALS.ALS_MISC "
									+ "WHERE AM_KEY1 ='JOURNAL_LINE_REFERENCE' "
									+ "AND LTRIM(AM_PAR_VAL,'0')=LTRIM(SUBSTR(LPAD(ase.asac_Reference,5,'0'),1,3),'0') "
									+ "AND ROWNUM <2) jlr, "
						+ "ase.ase_Amt aseAmt, "
						+ "ase.ase_Allow_Upload_To_Summary aseAllowUploadToSummary, "
						+ "ase.ase_When_Uploaded_To_Summary upToSummDt, "
						+ "ase.ases_Seq_No asesSeqNo, "
						+ "ase.api_Provider_No apiProviderNo, "
						+ "ase.apr_Billing_From bpFromDt, "
						+ "ase.apr_Billing_To bpToDt, "
						+ "ase.aiafa_Seq_No aiafaSeqNo, "
						+ "ase.ase_Who_Log aseWhoLog, "
						+ "ase.ase_When_Log aseWhenLog, "
						+ "ase.ase_When_Uploaded_To_Summ aseWhenUploadedToSumm, "
						+ "ase.atg_Transaction_Cd atgTransactionCd, "
						+ "ase.atgs_Group_Identifier atgsGroupIdentifier, "
						+ "ase.ase_Non_Als_Flag aseNonAlsFlag, "
						+ "ase.ase_Line_Description aseLineDescription, "
						+ "ase.ati_Tribe_Cd atiTribeCd, "
						+ "ase.anat_Cd anatCd, "
						+ "TO_CHAR(ase.ase_When_Entry_Posted, 'yyyy-mm-dd hh:mm:ss')||'_'||ase.ase_Seq_No||'_'||ase.ase_Dr_Cr_Cd||'_'||ase.ase_Txn_Cd_Seq_No gridKey, "
						+ "(SELECT ATGS_SUMMARY_STATUS rtn "
							+ "FROM ALS.ALS_TRANSACTION_GRP_STATUS "
							+ "WHERE ATG_TRANSACTION_CD = ase.atg_Transaction_Cd "
							+ "AND ATGS_GROUP_IDENTIFIER = ase.atgs_Group_Identifier) sumStat, "
						+ "(SELECT ATGS_INTERFACE_STATUS rtn "
							+ "FROM ALS.ALS_TRANSACTION_GRP_STATUS "
							+ "WHERE ATG_TRANSACTION_CD = ase.atg_Transaction_Cd "
							+ "AND ATGS_GROUP_IDENTIFIER = ase.atgs_Group_Identifier) intStat "
						+ "FROM ALS.ALS_SABHRS_ENTRIES ase ");
						if((sumAppStat != null && !"".equals(sumAppStat)) || (intAppStat != null && !"".equals(intAppStat))){
							queryStr.append(", ALS.ALS_TRANSACTION_GRP_STATUS atgs ");
						}
						queryStr.append("WHERE 1=1 ");
				    	if(!Utils.isNil(fromDt)){
				    		if(!Utils.isNil(toDt)){
				    			queryStr.append("AND ase.ase_When_Entry_Posted BETWEEN :fromDt AND :toDt ");//plus one to todt
				        	}else{
				        		queryStr.append("AND ase.ase_When_Entry_Posted BETWEEN :fromDt AND SYSDATE ");
				        	}
				    	}
				    	if(!Utils.isNil(bpFromDt)){
				    		queryStr.append("AND ase.apr_Billing_From = :bpFromDt ");
				    	}
				    	if(!Utils.isNil(bpToDt)){
				    		queryStr.append("AND ase.apr_Billing_To = :bpToDt ");
				    	}
				    	if(!Utils.isNil(providerNo)){
				    		queryStr.append("AND ase.api_Provider_No = :providerNo ");
				    	}
				    	if(!Utils.isNil(seqNo)&& !Utils.isNil(providerNo)&& !Utils.isNil(bpFromDt)&& !Utils.isNil(bpToDt)){
				    		queryStr.append("AND ase.aiafa_Seq_No = :seqNo ");
				    		
				    	}
				    	if(!Utils.isNil(jlr)){
				    		queryStr.append("AND ase.ASAC_REFERENCE = (SELECT AM_PAR_VAL||SUBSTR(:jlr,-2) "
				    										   + "FROM ALS.ALS_MISC WHERE AM_KEY1 = 'JOURNAL_LINE_REFERENCE' "
				    										   + "AND LPAD(AM_VAL_DESC,28,'0') = SUBSTR(LPAD(:jlr,30,'0'),1,28) "
				    										   + "AND ROWNUM <2) ");
				    	}
				    	if(!Utils.isNil(account)){
				    		queryStr.append("AND ase.aam_Account = :account ");
				    	}
				    	if(!Utils.isNil(fund)){
				    		queryStr.append("AND ase.aam_Fund = :fund ");
				    	}
				    	if(!Utils.isNil(org)){
				    		queryStr.append("AND ase.aoc_Org = :org ");
				    	}
				    	if(!Utils.isNil(subclass)){
				    		queryStr.append("AND ase.asac_Subclass = :subclass ");
				    	}
				    	if(!Utils.isNil(tribeCd)){
				    		queryStr.append("AND ase.ati_Tribe_Cd = :tribeCd ");
				    	}
				    	if(!Utils.isNil(txnGrpIdentifier)){
				    		queryStr.append("AND ase.atgs_Group_Identifier = :txnGrpIdentifier ");
				    	}
				    	if(!Utils.isNil(budgYear)){
				    		queryStr.append("AND ase.asac_Budget_Year = :budgYear ");
				    	}
				    	if(!Utils.isNil(progYear)){
				    		queryStr.append("AND ase.asac_Program = :progYear ");
				    	}
				    	if(!Utils.isNil(sysActTypeCd)){
				    		queryStr.append("AND ase.asac_System_Activity_Type_Cd||asac_Txn_Cd = :sysActTypeCd ");
				    	}
				    	if(!Utils.isNil(transGrpType)){
				    		queryStr.append("AND ase.atg_Transaction_Cd = :transGrpType ");
				    	}
				    	if(!Utils.isNil(sumAppStat) || !Utils.isNil(intAppStat)){
				    		queryStr.append("AND ase.atg_transaction_cd = atgs.atg_transaction_cd "
				    					 + "AND ase.atgs_group_identifier = atgs.atgs_group_identifier ");
				    	}
				    	if(!Utils.isNil(sumAppStat)){
				    		queryStr.append("AND atgs.atgs_summary_status = :sumAppStat ");
				    	}
				    	if(!Utils.isNil(intAppStat)){
				    		queryStr.append("AND atgs.atgs_interface_status = :intAppStat ");
				    	}
				    	queryStr.append("AND ROWNUM < 10001 ");
		try {
			Query query = getSession()
					.createSQLQuery(queryStr.toString())
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
					.addScalar("intStat");
					
					if(!Utils.isNil(providerNo))
						query.setInteger("providerNo", providerNo);
					if(!Utils.isNil(seqNo))
						query.setInteger("seqNo", seqNo);
					if(!Utils.isNil(bpFromDt))
						query.setDate("bpFromDt", bpFromDt);
					if(!Utils.isNil(bpToDt))
						query.setDate("bpToDt", bpToDt);
					if(!Utils.isNil(fromDt))
						query.setDate("fromDt", fromDt);
					if(!Utils.isNil(toDt))
						query.setDate("toDt", fromDt.equals(toDt)?Utils.addDays(toDt, 1):toDt);
					if(!Utils.isNil(sumAppStat))
						query.setString("sumAppStat", sumAppStat);
					if(!Utils.isNil(intAppStat))
						query.setString("intAppStat", intAppStat);
					if(!Utils.isNil(jlr))
						query.setString("jlr", jlr);
					if(!Utils.isNil(account))
						query.setString("account", account);
					if(!Utils.isNil(fund))
						query.setString("fund", fund);
					if(!Utils.isNil(org))
						query.setString("org", org);
					if(!Utils.isNil(subclass))
						query.setString("subclass", subclass);
					if(!Utils.isNil(tribeCd))
						query.setString("tribeCd", tribeCd);
					if(!Utils.isNil(txnGrpIdentifier))
						query.setString("txnGrpIdentifier", txnGrpIdentifier);
					if(!Utils.isNil(budgYear))
						query.setInteger("budgYear", budgYear);
					if(!Utils.isNil(progYear))
						query.setInteger("progYear", progYear);
					if(!Utils.isNil(sysActTypeCd))
						query.setString("sysActTypeCd", sysActTypeCd.toUpperCase());
					if(!Utils.isNil(transGrpType))
						query.setString("transGrpType", transGrpType);
					query.setResultTransformer(
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
	
	@SuppressWarnings("unchecked")
	public List<AlsProviderBankDetailsDTO> getInternalProviderTdt(String queryStr) {
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
	
	@SuppressWarnings("unchecked")
	public List<InternalProviderTdtDTO> getDeposits(String ids)
		{
			List<InternalProviderTdtDTO> rtn = new ArrayList<InternalProviderTdtDTO>();
			String queryString = "SELECT apbd.apbd_billing_to bpe, "
									+ "apbd.api_provider_no provNo, "
									+ "apbd.abc_bank_cd bankCd, " 
									+ "apbd.apbd_deposit_date depDt, "
									+ "apbd.apbd_deposit_id depId, "
									+ "apbd.apbd_amount_deposit depAmt, "
									+ "(SELECT abc_bank_nm FROM als.als_bank_code abc WHERE abc.abc_bank_cd = apbd.abc_bank_cd) bankNm, "
									+ "(SELECT api_business_nm FROM als.als_provider_info api WHERE api.api_provider_no = apbd.api_provider_no) businessNm,"
									+ "(als.als_package.get_pval('SABHRS BUSINESS UNIT', (SELECT atg_interface_file FROM als.als_transaction_group WHERE atg_transaction_cd = 8))) businessUnit "
									+ "FROM	als.als_provider_bank_details apbd "
									+ "WHERE apbd_deposit_id IN ("+ids+") "
									+ "ORDER BY 1, 2, 3, 4";
			
			try {
				Query query = getSession()
						.createSQLQuery(queryString)
						.addScalar("bpe", DateType.INSTANCE)
						.addScalar("provNo", IntegerType.INSTANCE)
						.addScalar("bankCd", IntegerType.INSTANCE)
						.addScalar("depDt", DateType.INSTANCE)
						.addScalar("depId", StringType.INSTANCE)
						.addScalar("depAmt", DoubleType.INSTANCE)
						.addScalar("bankNm", StringType.INSTANCE)
						.addScalar("businessNm", StringType.INSTANCE)
						.addScalar("businessUnit", StringType.INSTANCE)
	
		
						.setResultTransformer(
								Transformers.aliasToBean(InternalProviderTdtDTO.class));

				rtn = query.list();
			} catch (RuntimeException re) {
				System.out.println(re.toString());
			}
			finally {
				getSession().close();
			}
			return rtn;
			
		}
	
	@SuppressWarnings("unchecked")
	public List<IafaDetailsDTO> getIafaQueryRecords(Date fromDt, Date toDt, Integer issProvNo, Integer entProvNo, 
													Date bpFromDt, Date bpToDt, Date upFromDt, Date upToDt,
													String modeOfPayment, Integer chckNo, String chckWriter, String remarks,
													Integer iafaSeqNo, Date dob, Integer alsNo, String transGrpIdentifier,
													String sumAppStat, String intAppStat, String ahmType, String ahmCd,
													Date recordVoidDt, String tribeCd, String appType, String amountTypeCd,
													Double amount, Date sessDt, Date sessVoidDt, String sessStat, String reasonCd,
													String itemTypeCd, String itemCatCd, String bonusPoints, String itemInd,
													String itemStat, String costPrereqCd, String resIndicator, String appDis,
													String procCatCd, String procTypeCd, Date batchRecDt, String noCharge, 
													String itemTransInd, Integer seqNoInItemTrans, Boolean alxInd, Boolean nullTGI) throws HibernateException {
		List<IafaDetailsDTO> lst = new ArrayList<IafaDetailsDTO>();
		try {
			StringBuilder queryStr = new StringBuilder("SELECT /*+ RULE */ DISTINCT a.API_PROVIDER_NO apiProviderNo, "
					+ "e.API_BUSINESS_NM apiBusinessNm, "
					+ "e.API_ALX_PROV_IND apiAlxProvInd, "
					+ "a.APR_BILLING_FROM aprBillingFrom, "
					+ "a.APR_BILLING_TO aprBillingTo, "
					+ "a.AIAFA_SEQ_NO aiafaSeqNo, "
					+ "b.AS_DATA_ENTRY_PROVIDER_NO asDataEntryProviderNo, "
					+ "c.AICT_USAGE_PERIOD_FROM aictUsagePeriodFrom, "
					+ "c.AICT_USAGE_PERIOD_TO aictUsagePeriodTo, "
					+ "c.AICT_ITEM_TYPE_CD aictItemTypeCd, "
					+ "SUBSTR(c.AICT_ITEM_TYPE_CD,1,4) itemCatCd, "
					+ "g.AIT_TYPE_DESC aitTypeDesc, "
					+ "c.AII_ITEM_TXN_IND aiiItemTxnInd, "
					+ "c.AII_SEQ_NO aiiSeqNo, "
					+ "c.API_DOB dob, "
					+ "c.API_ALS_NO  alsNo, "
					+ "d.AIIN_ITEM_IND_CD aiinItemIndCd, "
					+ "d.AIS_ITEM_STATUS_CD aisItemStatusCd, "
					+ "i.AAI_DISPOSITION_CD aaiDispositionCd, "
					+ "h.AIC_CATEGORY_DESC aicCategoryDesc, "
					+ "NVL(d.APC_PREREQUISITE_COST_CD,i.APC_PREREQUISITE_COST_CD) apcPrerequisiteCostCd , "
					+ "NVL(d.AII_RESIDENCY_STATUS,i.AAI_ITEM_RESIDENCY_IND) residency, "
					+ "DECODE(NVL(d.AII_RESIDENCY_STATUS,i.AAI_ITEM_RESIDENCY_IND),'R','Residents','N','Nonresidents','Others') itemResidency, "
					+ "NVL(d.ACG_COST_GROUP_SEQ_NO,i.ACG_SEQ_NO) acdCostGroupSeqNo, "
					+ "a.AIAFA_AMT_TYPE aiafaAmtType, "
					+ "a.AIAFA_AMT aiafaAmt, "
					+ "DECODE(a.AIAFA_STATUS , 'A' ,a.AIAFA_AMT,0) sumIafa, "
					+ "a.AIAFA_PROCESS_CATEGORY_CD aiafaProcessCategoryCd, "
					+ "c.AST_PROCESS_TYPE_CD astProcessTypeCd, "
					+ "a.AIAFA_REASON_CD aiafaReasonCd, "
					+ "a.AHM_TYPE ahmType, "
					+ "a.AHM_CD ahmCd, "
					+ "DECODE(a.AIAFA_STATUS,'A', 'Active','V','Void') aiafaStatus, "
					+ "a.AS_SESSION_DT asSessionDt, "
					+ "c.AST_WHO_LOG  sessionOrigin, "
					+ "b.AS_SESSION_VOID_DT asSessionVoidDt, "
					+ "b.ABI_BATCH_NO  abiBatchNO, "
					+ "b.ASI_SUBBATCH_NO  asiSubatchNo, "
					+ "a.AIAFA_RECORD_VOID_DT aiafaRecordVoidDt, "
					+ "a.AIAFA_REMARKS aiafaRemarks, "
					+ "f.ABI_RECONCILED_ON abiReconciledOn, "
					+ "a.AIAFA_WHEN_LOG aiafaWhenLog, "
					+ "b.AS_MODE_PAYMENT asModePayment, "
					+ "b.AS_CHECK_NO asCheckNo, "
					+ "b.AS_CHECK_WRITER  asCheckWriter, "
					+ "i.AAI_BONUS_POINTS_IND aaiBonusPointsInd, "
					+ "a.ATI_TRIBE_CD atiTribeCd, "
					+ "k.ATGS_GROUP_IDENTIFIER atgsGroupIdentifier, "
					+ "k.ATGS_SUMMARY_STATUS atgsSummaryStatus, "
					+ "k.ATGS_INTERFACE_STATUS atgsInterfaceStatus, "
					+ "a.AIAFA_APP_TYPE aiafaAppType, "
					+ "c.AST_NOCHARGE_REASON astNochargeReason, "
					+ "(SELECT AIT_TYPE_DESC FROM ALS.ALS_ITEM_TYPE WHERE AI_ITEM_ID||AIC_CATEGORY_ID||AIT_TYPE_ID = c.AICT_ITEM_TYPE_CD) itemTypeDesc, "
					+ "(SELECT AM_KEY2 FROM ALS.ALS_MISC WHERE AM_KEY1 = 'ITEM INDICATOR' AND SUBSTR(LPAD(LTRIM(RTRIM(AM_PAR_VAL)),30,'0'),29,2) = LPAD(TO_CHAR(d.AIIN_ITEM_IND_CD),2,'0')) itemIndDesc, "
					+ "(SELECT AM_KEY2 FROM ALS.ALS_MISC WHERE AM_KEY1 = 'ITEM STATUS' AND SUBSTR(LPAD(LTRIM(RTRIM(AM_PAR_VAL)),30,'0'),29,2) = LPAD(TO_CHAR(d.AIS_ITEM_STATUS_CD),2,'0')) itemStatusDesc, "
					+ "(SELECT AM_KEY2 FROM ALS.ALS_MISC WHERE AM_KEY1 = 'APPLICATION DISPOSITION' AND SUBSTR(LPAD(LTRIM(RTRIM(AM_PAR_VAL)),30,'0'),29,2) = LPAD(TO_CHAR(i.AAI_DISPOSITION_CD),2,'0')) appDispositionDesc, "
					+ "(SELECT APC_PREREQUISITE_DESC FROM ALS.ALS_PREREQUISITE_CD WHERE APC_PREREQUISITE_CD = NVL(d.APC_PREREQUISITE_COST_CD,i.APC_PREREQUISITE_COST_CD)) prereqDesc, "
					+ "(SELECT AIC_CATEGORY_DESC FROM ALS.ALS_ITEM_CATEGORY WHERE AI_ITEM_ID||AIC_CATEGORY_ID = SUBSTR(c.AICT_ITEM_TYPE_CD,1,4)) itemCatDesc, "
					+ "(SELECT AM_VAL_DESC FROM ALS.ALS_MISC WHERE AM_KEY1 = 'IAFA_AMOUNT_TYPE' AND SUBSTR(LPAD(LTRIM(RTRIM(AM_PAR_VAL)),30,'0'),27,4) = LPAD(TO_CHAR(a.AIAFA_AMT_TYPE),4,'0')) amtTypeDesc, "
					+ "(SELECT AM_KEY2 FROM ALS.ALS_MISC WHERE AM_KEY1 = 'PROCESS_CATEGORY' AND AM_KEY2 <> 'SESSION VOID' AND SUBSTR(LPAD(LTRIM(RTRIM(AM_PAR_VAL)),30,'0'),28,3) = LPAD(TO_CHAR(a.AIAFA_PROCESS_CATEGORY_CD),3,'0')) processCatDesc, "
					+ "(SELECT AM_KEY2 FROM ALS.ALS_MISC WHERE AM_KEY1 = 'PROCESS TYPE' AND AM_KEY2 <> 'SESSION VOID' AND SUBSTR(LPAD(LTRIM(RTRIM(AM_PAR_VAL)),30,'0'),27,4) = LPAD(TO_CHAR(c.AST_PROCESS_TYPE_CD),4,'0')) processTypeDesc, "
					+ "(SELECT AM_DESC2 FROM ALS.ALS_MISC WHERE AM_KEY1 = 'PAE_REASON' AND SUBSTR(LPAD(LTRIM(RTRIM(AM_PAR_VAL)),30,'0'),27,4) = LPAD(TO_CHAR(a.AIAFA_REASON_CD),4,'0')) reasonDesc "
					+ "FROM ALS.ALS_ITEM_APPL_FEE_ACCT a, "
					+ "ALS.ALS_SESSIONS b, "
					+ "ALS.ALS_SESSION_TRANS c, "
					+ "ALS.ALS_ITEM_INFORMATION d, "
					+ "ALS.ALS_PROVIDER_INFO e, "
					+ "ALS.ALS_BATCH_INFO f, "
					+ "ALS.ALS_ITEM_TYPE g, "
					+ "ALS.ALS_ITEM_CATEGORY h, "
					+ "ALS.ALS_APPLICATION_INFORMATION i, "
					+ "ALS.ALS_SABHRS_ENTRIES j, "
					+ "ALS.ALS_TRANSACTION_GRP_STATUS k "
					/*WHERE*/
					+ "WHERE ALS.a.AIAFA_WHEN_LOG >= :fromDt "
					+ "AND ALS.a.AIAFA_WHEN_LOG < :toDt "
					+ "AND a.AHM_TYPE = b.AHM_TYPE(+) "
					+ "AND a.AHM_CD = b.AHM_CD(+) "
					+ "AND a.AS_SESSION_DT = b.AS_SESSION_DT(+) "
					+ "AND a.API_PROVIDER_NO = e.API_PROVIDER_NO "
					+ "AND a.AHM_TYPE = c.AHM_TYPE(+) "
					+ "AND a.AHM_CD = c.AHM_CD(+) "
					+ "AND a.AS_SESSION_DT = c.AS_SESSION_DT(+) "
					+ "AND a.AST_TRANSACTION_CD = c.AST_TRANSACTION_CD(+) "
					+ "AND a.AST_TRANSACTION_SEQ_NO = c.AST_TRANSACTION_SEQ_NO(+) "
					+ "AND b.ABI_LICENSE_YR = f.ABI_LICENSE_YR(+) "
					+ "AND b.ABI_BATCH_NO = f.ABI_BATCH_NO(+) "
					+ "AND SUBSTR(c.AICT_ITEM_TYPE_CD,1,2) = g.AI_ITEM_ID(+) "
					+ "AND SUBSTR(c.AICT_ITEM_TYPE_CD,3,2) = g.AIC_CATEGORY_ID(+) "
					+ "AND SUBSTR(c.AICT_ITEM_TYPE_CD,5,3) = g.AIT_TYPE_ID(+) "
					+ "AND SUBSTR(c.AICT_ITEM_TYPE_CD,1,2) = h.AI_ITEM_ID(+) "
					+ "AND SUBSTR(c.AICT_ITEM_TYPE_CD,3,2) = h.AIC_CATEGORY_ID(+) "
					+ "AND c.AICT_USAGE_PERIOD_FROM = d.AICT_USAGE_PERIOD_FROM(+) "
					+ "AND c.AICT_USAGE_PERIOD_TO = d.AICT_USAGE_PERIOD_TO(+) "
					+ "AND c.AICT_ITEM_TYPE_CD = d.AICT_ITEM_TYPE_CD(+) "
					+ "AND c.API_DOB = d.API_DOB(+) "
					+ "AND c.API_ALS_NO = d.API_ALS_NO(+) "
					+ "AND c.AII_ITEM_TXN_IND = d.AII_ITEM_TXN_IND(+) "
					+ "AND c.AII_SEQ_NO = d.AII_SEQ_NO(+) "
					+ "AND c.AAI_APP_IDENTIFICATION_NO = i.AAI_APP_IDENTIFICATION_NO(+) "
					+ "AND a.API_PROVIDER_NO = j.API_PROVIDER_NO(+) "
					+ "AND a.APR_BILLING_FROM = j.APR_BILLING_FROM(+) "
					+ "AND a.APR_BILLING_TO = j.APR_BILLING_TO(+) "
					+ "AND a.AIAFA_SEQ_NO = j.AIAFA_SEQ_NO(+) "
					+ "AND j.ATG_TRANSACTION_CD = k.ATG_TRANSACTION_CD(+) "
					+ "AND j.ATGS_GROUP_IDENTIFIER = k.ATGS_GROUP_IDENTIFIER(+) "
					+ "AND NVL(ALS.j.ATGS_GROUP_IDENTIFIER,0) NOT LIKE 'DRWGRSLT%' ");
					if(!Utils.isNil(issProvNo))
						queryStr.append("AND a.API_PROVIDER_NO = NVL(:issProvNo , a.API_PROVIDER_NO) ");
					if(!Utils.isNil(entProvNo))
						queryStr.append("AND b.AS_DATA_ENTRY_PROVIDER_NO = NVL(:entProvNo, b.AS_DATA_ENTRY_PROVIDER_NO) ");
					if(!Utils.isNil(bpFromDt))
						queryStr.append("AND a.APR_BILLING_FROM = NVL(:bpFromDt,a.APR_BILLING_FROM) ");
					if(!Utils.isNil(bpToDt))
						queryStr.append("AND a.APR_BILLING_TO = NVL(:bpToDt,a.APR_BILLING_TO) ");
					if(!Utils.isNil(upFromDt))
						queryStr.append("AND NVL(c.AICT_USAGE_PERIOD_FROM,SYSDATE) = AND NVL(:upFromDt,NVL(c.AICT_USAGE_PERIOD_FROM,SYSDATE)) ");
					if(!Utils.isNil(upToDt))
						queryStr.append("AND NVL(c.AICT_USAGE_PERIOD_TO,SYSDATE) = AND NVL(:upToDt,AND NVL(c.AICT_USAGE_PERIOD_TO,SYSDATE)) ");
					if(!Utils.isNil(modeOfPayment))
						queryStr.append("AND b.AS_MODE_PAYMENT = NVL(:modeOfPayment,b.AS_MODE_PAYMENT) ");
					if(!Utils.isNil(chckNo))
						queryStr.append("AND b.AS_CHECK_NO = NVL(:chckNo,b.AS_CHECK_NO) ");
					if(!Utils.isNil(chckWriter))
						queryStr.append("AND b.AS_CHECK_WRITER = NVL(:chckWriter,b.AS_CHECK_WRITER) ");
					if(!Utils.isNil(remarks))
						queryStr.append("AND a.AIAFA_REMARKS LIKE NVL(:remarks,a.AIAFA_REMARKS) ");
					if(!Utils.isNil(iafaSeqNo))
						queryStr.append("AND a.AIAFA_SEQ_NO = NVL(:iafaSeqNo,a.AIAFA_SEQ_NO) ");
					if(!Utils.isNil(dob))
						queryStr.append("AND NVL(c.API_DOB,SYSDATE) = NVL(:dob,NVL(c.API_DOB,SYSDATE)) ");
					if(!Utils.isNil(alsNo))
						queryStr.append("AND c.API_ALS_NO = NVL(:alsNo,c.API_ALS_NO) ");
					if(!Utils.isNil(transGrpIdentifier))
						queryStr.append("AND k.ATGS_GROUP_IDENTIFIER = NVL(:transGrpIdentifier,k.ATGS_GROUP_IDENTIFIER) ");
					if(!Utils.isNil(sumAppStat))
						queryStr.append("AND k.ATGS_SUMMARY_STATUS = NVL(:sumAppStat,k.ATGS_SUMMARY_STATUS) ");
					if(!Utils.isNil(intAppStat))
						queryStr.append("AND k.ATGS_INTERFACE_STATUS = NVL(:intAppStat,k.ATGS_INTERFACE_STATUS) ");
					if(!Utils.isNil(ahmType))
						queryStr.append("AND a.AHM_TYPE = NVL(:ahmType,a.AHM_TYPE) ");
					if(!Utils.isNil(ahmCd))
						queryStr.append("AND a.AHM_CD = NVL(:ahmCd,a.AHM_CD) ");
					if(!Utils.isNil(recordVoidDt))
						queryStr.append("AND a.AIAFA_RECORD_VOID_DT = NVL(:recordVoidDt,a.AIAFA_RECORD_VOID_DT) ");
					if(!Utils.isNil(tribeCd))
						queryStr.append("AND a.ATI_TRIBE_CD = NVL(:tribeCd,a.ATI_TRIBE_CD) ");
					if(!Utils.isNil(appType))
						queryStr.append("AND a.AIAFA_APP_TYPE = NVL(:appType,a.AIAFA_APP_TYPE) ");
					if(!Utils.isNil(amountTypeCd))
						queryStr.append("AND a.AIAFA_AMT_TYPE = NVL(:amountTypeCd,a.AIAFA_AMT_TYPE) ");
					if(!Utils.isNil(amount))
						queryStr.append("AND a.AIAFA_AMT = NVL(:amount,a.AIAFA_AMT) ");
					if(!Utils.isNil(sessDt))
						queryStr.append("AND TRUNC(NVL(a.AS_SESSION_DT,SYSDATE)) = NVL(:sessDt,TRUNC(NVL(a.AS_SESSION_DT,SYSDATE))) ");
					if(!Utils.isNil(sessVoidDt))
						queryStr.append("AND TRUNC(NVL(b.AS_SESSION_VOID_DT,SYSDATE)) = NVL(:sessVoidDt,TRUNC(NVL(b.AS_SESSION_VOID_DT,SYSDATE))) ");
					if(!Utils.isNil(sessStat)){
						if("AV".equals(sessStat)){
							queryStr.append("AND (a.AIAFA_STATUS = :sessStat OR a.AIAFA_STATUS = :sessStat2) ");
						}else{
							queryStr.append("AND a.AIAFA_STATUS = NVL(:sessStat,a.AIAFA_STATUS) ");
						}
					}
					if(!Utils.isNil(reasonCd))
						queryStr.append("AND a.AIAFA_REASON_CD = NVL(:reasonCd,a.AIAFA_REASON_CD) ");
					if(!Utils.isNil(itemTypeCd))
						queryStr.append("AND c.AICT_ITEM_TYPE_CD = NVL(:itemTypeCd,c.AICT_ITEM_TYPE_CD) ");
					if(!Utils.isNil(itemCatCd))
						queryStr.append("AND SUBSTR(c.AICT_ITEM_TYPE_CD,1,4) = NVL(:itemCatCd,SUBSTR(c.AICT_ITEM_TYPE_CD,1,4)) ");
					if(!Utils.isNil(bonusPoints))
						queryStr.append("AND i.AAI_BONUS_POINTS_IND = NVL(:bonusPoints,i.AAI_BONUS_POINTS_IND) ");
					if(!Utils.isNil(itemInd))
						queryStr.append("AND d.AIIN_ITEM_IND_CD = NVL(:itemInd,d.AIIN_ITEM_IND_CD) ");
					if(!Utils.isNil(itemStat))
						queryStr.append("AND d.AIS_ITEM_STATUS_CD = NVL(:itemStat,d.AIS_ITEM_STATUS_CD) ");
					if(!Utils.isNil(costPrereqCd))
						queryStr.append("AND (d.APC_PREREQUISITE_COST_CD = :costPrereqCd OR i.APC_PREREQUISITE_COST_CD = :costPrereqCd) ");
					if(!Utils.isNil(resIndicator))
						queryStr.append("AND (d.AII_RESIDENCY_STATUS = :resIndicator OR i.AAI_ITEM_RESIDENCY_IND = :resIndicator) ");
					if(!Utils.isNil(appDis))
						queryStr.append("AND i.AAI_DISPOSITION_CD = NVL(:appDis,i.AAI_DISPOSITION_CD) ");
					if(!Utils.isNil(procCatCd))
						queryStr.append("AND a.AIAFA_PROCESS_CATEGORY_CD = NVL(:procCatCd,a.AIAFA_PROCESS_CATEGORY_CD) ");
					if(!Utils.isNil(procTypeCd))
						queryStr.append("AND c.AST_PROCESS_TYPE_CD = NVL(:procTypeCd,c.AST_PROCESS_TYPE_CD) ");
					if(!Utils.isNil(batchRecDt))
						queryStr.append("AND f.ABI_RECONCILED_ON = NVL(:batchRecDt,f.ABI_RECONCILED_ON) ");
					if(!Utils.isNil(noCharge))
						queryStr.append("AND Decode(NVL(c.AST_NOCHARGE_REASON,'N'),'N','N','Y') = Decode(:noCharge,'N','N','Y') ");
					if(!Utils.isNil(itemTransInd))
						queryStr.append("AND c.AII_ITEM_TXN_IND = NVL(:itemTransInd,c.AII_ITEM_TXN_IND) ");
					if(!Utils.isNil(seqNoInItemTrans))
						queryStr.append("AND c.AII_SEQ_NO = NVL(:seqNoInItemTrans,c.AII_SEQ_NO) ");
							
					if(alxInd != null){
						if(alxInd == true){
							queryStr.append("AND e.API_ALX_PROV_IND = 'Y' ");
						}
					}
					if(nullTGI != null && nullTGI == true){
						queryStr.append("AND NOT EXISTS (SELECT 1 "
														+ "FROM als.k atgs, "
														+ "als.ALS_SABHRS_ENTRIES ase "
														+ "WHERE ase.api_provider_no = a.api_provider_no "
														+ "AND ase.apr_billing_from = a.apr_billing_from "
														+ "AND ase.apr_billing_to = a.apr_billing_to "
														+ "AND ase.aiafa_seq_no = a.aiafa_seq_no "
														+ "AND atgs.atg_transaction_cd = ase.atg_transaction_cd "
														+ "AND atgs.atgs_group_identifier = ase.atgs_group_identifier) ");
					}
					queryStr.append("AND ROWNUM < 10001 "
								  + "ORDER BY a.aiafa_seq_no, a.as_session_dt ");

			Query query = getSession()
					.createSQLQuery(queryStr.toString())
					.addScalar("apiProviderNo", IntegerType.INSTANCE)
					.addScalar("apiBusinessNm")
					.addScalar("apiAlxProvInd")
					.addScalar("aprBillingFrom", DateType.INSTANCE)
					.addScalar("aprBillingTo", DateType.INSTANCE)
					.addScalar("aiafaSeqNo", IntegerType.INSTANCE)
					.addScalar("asDataEntryProviderNo", IntegerType.INSTANCE)
					.addScalar("aictUsagePeriodFrom", DateType.INSTANCE)
					.addScalar("aictUsagePeriodTo", DateType.INSTANCE)
					.addScalar("aictItemTypeCd", IntegerType.INSTANCE)
					.addScalar("itemCatCd", IntegerType.INSTANCE)
					.addScalar("aitTypeDesc")
					.addScalar("aiiItemTxnInd")
					.addScalar("aiiSeqNo", IntegerType.INSTANCE)
					.addScalar("dob", DateType.INSTANCE)
					.addScalar("alsNo", IntegerType.INSTANCE)
					.addScalar("aiinItemIndCd", IntegerType.INSTANCE)
					.addScalar("aisItemStatusCd", IntegerType.INSTANCE)
					.addScalar("aaiDispositionCd", IntegerType.INSTANCE)
					.addScalar("aicCategoryDesc")
					.addScalar("apcPrerequisiteCostCd", IntegerType.INSTANCE)
					.addScalar("residency")
					.addScalar("itemResidency")
					.addScalar("acdCostGroupSeqNo", IntegerType.INSTANCE)
					.addScalar("aiafaAmtType", IntegerType.INSTANCE)
					.addScalar("aiafaAmt", DoubleType.INSTANCE)
					.addScalar("sumIafa", DoubleType.INSTANCE)
					.addScalar("aiafaProcessCategoryCd", IntegerType.INSTANCE)
					.addScalar("astProcessTypeCd", IntegerType.INSTANCE)
					.addScalar("aiafaReasonCd", IntegerType.INSTANCE)
					.addScalar("ahmType")
					.addScalar("ahmCd", IntegerType.INSTANCE)
					.addScalar("aiafaStatus")
					.addScalar("asSessionDt", TimestampType.INSTANCE)
					.addScalar("sessionOrigin")
					.addScalar("asSessionVoidDt", DateType.INSTANCE) 
					.addScalar("abiBatchNO")
					.addScalar("asiSubatchNo")
					.addScalar("aiafaRecordVoidDt", DateType.INSTANCE) 
					.addScalar("aiafaRemarks")
					.addScalar("abiReconciledOn", DateType.INSTANCE)
					.addScalar("aiafaWhenLog", DateType.INSTANCE)
					.addScalar("asModePayment")
					.addScalar("asCheckNo", IntegerType.INSTANCE)
					.addScalar("asCheckWriter")
					.addScalar("aaiBonusPointsInd")
					.addScalar("atiTribeCd")            
					.addScalar("atgsGroupIdentifier")    
					.addScalar("atgsSummaryStatus")   
					.addScalar("atgsInterfaceStatus")
					.addScalar("aiafaAppType")
					.addScalar("astNochargeReason")
					.addScalar("itemTypeDesc")
					.addScalar("itemIndDesc")
					.addScalar("itemStatusDesc")
					.addScalar("appDispositionDesc")
					.addScalar("prereqDesc")
					.addScalar("itemCatDesc")
					.addScalar("amtTypeDesc")
					.addScalar("processCatDesc")
					.addScalar("processTypeDesc")
					.addScalar("reasonDesc");
					query.setDate("fromDt", fromDt);
					query.setDate("toDt", toDt);
					if(!Utils.isNil(issProvNo))
					query.setInteger("issProvNo", issProvNo);
					if(!Utils.isNil(entProvNo))
					query.setInteger("entProvNo", entProvNo);
					if(!Utils.isNil(bpFromDt))
					query.setDate("bpFromDt", bpFromDt);
					if(!Utils.isNil(bpToDt))
					query.setDate("bpToDt", bpToDt);
					if(!Utils.isNil(upFromDt))
					query.setDate("upFromDt", upFromDt);
					if(!Utils.isNil(upToDt))
					query.setDate("upToDt", upToDt);
					if(!Utils.isNil(modeOfPayment))
					query.setString("modeOfPayment", modeOfPayment);
					if(!Utils.isNil(chckNo))
					query.setInteger("chckNo", chckNo);
					if(!Utils.isNil(chckWriter))
					query.setString("chckWriter", chckWriter);
					if(!Utils.isNil(remarks))
					query.setString("remarks", remarks);
					if(!Utils.isNil(iafaSeqNo))
					query.setInteger("iafaSeqNo", iafaSeqNo);
					if(!Utils.isNil(dob))
					query.setDate("dob", dob);
					if(!Utils.isNil(alsNo))
					query.setInteger("alsNo", alsNo);
					if(!Utils.isNil(transGrpIdentifier))
					query.setString("transGrpIdentifier", transGrpIdentifier);
					if(!Utils.isNil(sumAppStat))
					query.setString("sumAppStat", sumAppStat);
					if(!Utils.isNil(intAppStat))
					query.setString("intAppStat", intAppStat);
					if(!Utils.isNil(ahmType))
					query.setString("ahmType", ahmType);
					if(!Utils.isNil(ahmCd))
					query.setString("ahmCd", ahmCd);
					if(!Utils.isNil(recordVoidDt))
					query.setDate("recordVoidDt", recordVoidDt);
					if(!Utils.isNil(tribeCd))
					query.setString("tribeCd", tribeCd);
					if(!Utils.isNil(appType))
					query.setString("appType", appType);
					if(!Utils.isNil(amountTypeCd))
					query.setString("amountTypeCd", amountTypeCd);
					if(!Utils.isNil(amount))
					query.setDouble("amount", amount);
					if(!Utils.isNil(sessDt))
					query.setDate("sessDt", sessDt);
					if(!Utils.isNil(sessVoidDt))
					query.setDate("sessVoidDt", sessVoidDt);
					if(!Utils.isNil(sessStat)){
						if("AV".equals(sessStat)){
							query.setString("sessStat", "A");
							query.setString("sessStat2", "V");
						}else{
							query.setString("sessStat", sessStat);
						}
						
					}
					if(!Utils.isNil(reasonCd))
					query.setString("reasonCd", reasonCd);
					if(!Utils.isNil(itemTypeCd))
					query.setString("itemTypeCd", itemTypeCd);
					if(!Utils.isNil(itemCatCd))
					query.setString("itemCatCd", itemCatCd);
					if(!Utils.isNil(bonusPoints))
					query.setString("bonusPoints", bonusPoints);
					if(!Utils.isNil(itemInd))
					query.setString("itemInd", itemInd);
					if(!Utils.isNil(itemStat))
					query.setString("itemStat", itemStat);
					if(!Utils.isNil(costPrereqCd))
					query.setString("costPrereqCd", costPrereqCd);
					if(!Utils.isNil(resIndicator))
					query.setString("resIndicator", resIndicator);
					if(!Utils.isNil(appDis))
					query.setString("appDis", appDis);
					if(!Utils.isNil(procCatCd))
					query.setString("procCatCd", procCatCd);
					if(!Utils.isNil(procTypeCd))
					query.setString("procTypeCd", procTypeCd);
					if(!Utils.isNil(batchRecDt))
					query.setDate("batchRecDt", batchRecDt);
					if(!Utils.isNil(noCharge))
					query.setString("noCharge", noCharge);
					if(!Utils.isNil(itemTransInd))
					query.setString("itemTransInd", itemTransInd);
					if(!Utils.isNil(seqNoInItemTrans))
					query.setInteger("seqNoInItemTrans", seqNoInItemTrans);
					query.setResultTransformer(
							Transformers.aliasToBean(IafaDetailsDTO.class));
			lst = query.list();
		}
		catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public String getOtherTxnGrp(Integer provNo, java.util.Date date, java.util.Date date2, Integer iafaSeqNo, String transGroupIdentifier) {
		List<String> rtnLst;
		String rtn = "";
		String queryString =  "SELECT DISTINCT atgs_group_identifier  atgsGroupIdentifier "
								+ "FROM als.als_sabhrs_entries "
								+ "WHERE api_provider_no = "+provNo+" "
								+ "AND apr_billing_from = TO_DATE('"+date+"','YYYY-MM-DD') "
								+ "AND apr_billing_to = TO_DATE('"+date2+"','YYYY-MM-DD') "
								+ "AND aiafa_seq_no = "+iafaSeqNo+" "
								+ "AND NVL(atgs_group_identifier,0) <> NVL('"+transGroupIdentifier+"',0) "
								+ "AND atgs_group_identifier IS NOT NULL";
		
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("atgsGroupIdentifier");
			rtnLst = (List<String>) query.list();
			
			for(String tmp :rtnLst){
				if(tmp != null && !"".equals(tmp)){
					if("".equals(rtn)){
						rtn += tmp;
					}else{
						rtn += ","+tmp;
					}
				}
			}
		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	public String getCostGrpDesc(java.util.Date date, java.util.Date date2, Integer itemTypeCd, String residencyInd, Integer costGroupSeqNo) {
		String rtn = "";
		String queryString =  "SELECT ACG_SUPPLEMENT_COST_GRP_DESC costGrpDesc "
				+ "FROM ALS.ALS_COST_GROUP "
				+ "WHERE ALS_COST_GROUP.AICT_USAGE_PERIOD_FROM = TO_DATE('"+date+"','YYYY-MM-DD') "
				+ "AND ALS_COST_GROUP.AICT_USAGE_PERIOD_TO = TO_DATE('"+date2+"','YYYY-MM-DD') "
				+ "AND ALS_COST_GROUP.AICT_ITEM_TYPE_CD = "+itemTypeCd+" "
				+ "AND ALS_COST_GROUP.AIR_RESIDENCY_IND = '"+residencyInd+"' "
				+ "AND ALS_COST_GROUP.ACG_SEQ_NO = "+costGroupSeqNo+" ";
		
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("costGrpDesc");
			rtn = (String) query.uniqueResult();

		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	public Double getSessionTotal(String string, Integer ahmCd, java.util.Date date, Date fromDt, Date toDt) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Double rtn = 0.0;
		String queryString =  "SELECT NVL(SUM(AIAFA_AMT),0)  rtn "
							+ "FROM Als.ALS_ITEM_APPL_FEE_ACCT "
							+ "WHERE Ahm_Type = '"+string+"' "
							+ "AND Ahm_Cd = "+ahmCd+" "
							+ "AND As_Session_Dt = TO_DATE('"+sdf.format(date)+"','dd-mm-yyyy hh24:mi:ss') "
							+ "AND AIAFA_STATUS = 'A' "
							+ "AND AIAFA_WHEN_LOG >= TO_DATE('"+fromDt+"','YYYY-MM-DD') "
							+ "AND AIAFA_WHEN_LOG < (TO_DATE('"+toDt+"','YYYY-MM-DD')+1)";
		
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("rtn", DoubleType.INSTANCE);
			rtn = (Double) query.uniqueResult();

		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	public Integer getSeqNoForPrintedItems(java.util.Date dob, Integer alsNo, java.util.Date upFrom, java.util.Date upTo, Integer itemTypeCd, String string, Integer aiiSeqNo) {

		Integer rtn = 0;
		String queryString =  "SELECT B.APRI_SEQ_NO rtn "
							+ "FROM ALS.ALS_PRINTED_ITEMS A, "
							+ "ALS.ALS_PRINTED_ITEM_DETAIL B "
							+ "WHERE A.API_DOB =  TO_DATE('"+dob+"','yyyy-mm-dd') "
							+ "AND A.API_ALS_NO = "+alsNo+" "
							+ "AND A.APRI_STATUS = 'A' "
							+ "AND B.API_DOB = A.API_DOB "
							+ "AND B.API_ALS_NO = A.API_ALS_NO "
							+ "AND A.APRI_SEQ_NO = B.APRI_SEQ_NO "
							+ "AND B.AICT_USAGE_PERIOD_FROM = TO_DATE('"+upFrom+"','yyyy-mm-dd') "
							+ "AND B.AICT_USAGE_PERIOD_TO = TO_DATE('"+upTo+"','yyyy-mm-dd') "
							+ "AND B.AICT_ITEM_TYPE_CD = "+itemTypeCd+" "
							+ "AND B.AII_ITEM_TXN_IND = '"+string+"' "
							+ "AND B.AII_SEQ_NO = "+aiiSeqNo+" "
							+ "and rownum < 2";
		
		try {
			Query query = getSession()
					.createSQLQuery(queryString)
					.addScalar("rtn", IntegerType.INSTANCE);
			rtn = (Integer) query.uniqueResult();

		} catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return rtn;
	}
	
	public String updateTransactionGrpStatus(String depId, String ipTranGrp, String minMax) {
		String tmp = "ERROR";
		int rowCount = 0;
		String updtString = "";
		if("min".equals(minMax)){
			updtString = "UPDATE als.als_transaction_grp_status "
					+ "SET atgs_deposit_id = :depId "
					+ "WHERE atg_transaction_cd = 8 "
					+ "AND atgs_deposit_id IS NULL "
					+ "AND atgs_group_identifier = (SELECT MIN(atgs_group_identifier) "
													+ "FROM als.als_transaction_grp_status "
													+ "WHERE atg_transaction_cd = 8 "
													+ "AND atgs_group_identifier LIKE ':ipTranGrp%')";
		}else if("max".equals(minMax)){
			updtString = "UPDATE als.als_transaction_grp_status "
					+ "SET atgs_deposit_id = :depId "
					+ "WHERE atg_transaction_cd = 8 "
					+ "AND atgs_deposit_id IS NULL "
					+ "AND atgs_group_identifier = (SELECT MAX(atgs_group_identifier) "
													+ "FROM als.als_transaction_grp_status "
													+ "WHERE atg_transaction_cd = 8 "
													+ "AND atgs_group_identifier LIKE ':ipTranGrp%')";
		}
		

		try {
			Session session = getSession();
			Query query = session.createSQLQuery(updtString);

			query.setString("depId", depId);
			query.setString("ipTranGrp", ipTranGrp);

			rowCount = query.executeUpdate();

		} catch (RuntimeException re) {
			tmp = "ERROR";
		} finally {
			if (rowCount == 0) {
				tmp = "ERROR";
			} else if (rowCount > 0) {
				tmp = "SUCCESS";
			}
			getSession().close();
		}
		return tmp;
	}
	
	@SuppressWarnings("unchecked")
	public List<AccCdDistByItemTypeDTO> getAccCdDistByItemTypeRecords(Date upFromDt, Date upToDt,Integer budgYear, Integer accCd, 
															  String itemTypeCd) throws HibernateException {
		List<AccCdDistByItemTypeDTO> lst = new ArrayList<AccCdDistByItemTypeDTO>();
		try {
			StringBuilder queryStr = new StringBuilder("SELECT DISTINCT "
					+ "aiat.aict_item_type_cd itemTypeCd, "
					+ "aiat.aict_usage_period_from||'-'||aiat.aict_usage_period_to||'-'||aiat.aict_item_type_cd||'-'||aiat.asac_budget_yr||'-'||aiat.aiat_residency_ind||'-'||aiat.aict_seq_no gridKey,"
					+ "ait.ait_type_desc itemTypeDesc, "
					+ "TO_CHAR(aiat.aict_usage_period_from, 'MM/DD/YYYY') upFromDt, "
					+ "TO_CHAR(aiat.aict_usage_period_to, 'MM/DD/YYYY') upToDt, "
					+ "NVL(act.act_cost, 0) itemCost, "
					+ "act.act_prerequisite_cd costPrereq, "
					+ "apc.apc_prerequisite_desc costPrereqDesc, "
					+ "aiat.aiat_residency_ind residency, "
					+ "aiat.asac_budget_yr budgYear, "
					+ "aiat.aiat_partial_cost_acc_cd partialCost, "
					+ "aiat.aiat_drawing_fee_acc_cd drawFee, "
					+ "aiat.aacc_acc_cd accCd, "
					+ "aacc.aam_account account, "
					+ "aacc.aacc_fund fund, "
					+ "aacc.aacc_balancing_amt_flag balancing,"
					+ "aacc.aacc_allocated_amt dist, "
					+ "aacc.asac_subclass subclass, "
					+ "aacc.aoc_org org, "
					+ "aoc.aoc_org orgMult "
					+ "FROM als.als_cost_table act, "
						 + "als.als_item_type ait, "
						 + "als.als_prerequisite_cd apc, "
						 + "als.als_org_control aoc, "
						 + "als.als_acc_cd_control aacc, "
						 + "als.als_item_account_table aiat "
					+ "WHERE aiat.asac_budget_yr = :budgYear "
					+ "AND aacc.asac_budget_year = aiat.asac_budget_yr "
					+ "AND aacc.aacc_acc_cd = aiat.aacc_acc_cd "
					+ "AND aoc.asac_budget_year(+) = aiat.asac_budget_yr "
					+ "AND aoc.aacc_acc_cd(+) = aiat.aacc_acc_cd "
					+ "AND ai_item_id||aic_category_id||ait_type_id = aiat.aict_item_type_cd "
					+ "AND TO_CHAR(aiat.aict_item_type_cd) IN ("+itemTypeCd+") "
					+ "AND act.aict_usage_period_from(+) = aiat.aict_usage_period_from "
					+ "AND act.aict_usage_period_to(+) = aiat.aict_usage_period_to "
					+ "AND act.aict_item_type_cd(+) = aiat.aict_item_type_cd "
					+ "AND act.act_prerequisite_cd(+) = aiat.apc_prerequisite_cd "
					+ "AND apc.apc_prerequisite_cd = aiat.apc_prerequisite_cd "
					+ "AND (act.air_residency_ind = aiat.aiat_residency_ind "
						 + "OR   act.air_residency_ind = 'B' "
						 + "AND NOT EXISTS (SELECT 1 "
										 + "FROM als.als_cost_table "
										 + "WHERE aict_usage_period_from = aiat.aict_usage_period_from "
										 + "AND aict_usage_period_to = aiat.aict_usage_period_from "
										 + "AND aict_item_type_cd = aiat.aict_item_type_cd)) ");
			if(!Utils.isNil(accCd))
				queryStr.append("AND aiat.aacc_acc_cd = :accCd ");
			if(!Utils.isNil(upFromDt))
				queryStr.append("AND aiat.aict_usage_period_from = :upFromDt ");
			if(!Utils.isNil(upToDt))
				queryStr.append("AND aiat.aict_usage_period_to = :upToDt ");

			queryStr.append("ORDER BY 1, 8 DESC, 6, 12, 15");
			
			Query query = getSession()
					.createSQLQuery(queryStr.toString())
					.addScalar("itemTypeCd")
					.addScalar("gridKey")
					.addScalar("itemTypeDesc")
					.addScalar("upFromDt")
					.addScalar("upToDt")
					.addScalar("itemCost", DoubleType.INSTANCE)
					.addScalar("costPrereq", IntegerType.INSTANCE)
					.addScalar("costPrereqDesc")
					.addScalar("residency")
					.addScalar("budgYear", IntegerType.INSTANCE)
					.addScalar("partialCost")
					.addScalar("drawFee")
					.addScalar("accCd", IntegerType.INSTANCE)
					.addScalar("account", IntegerType.INSTANCE)
					.addScalar("fund", IntegerType.INSTANCE)
					.addScalar("balancing")
					.addScalar("dist", DoubleType.INSTANCE)
					.addScalar("subclass")
					.addScalar("org")
					.addScalar("orgMult")
					.setInteger("budgYear",budgYear)
					.setResultTransformer(
							Transformers.aliasToBean(AccCdDistByItemTypeDTO.class));
					if(!Utils.isNil(upFromDt))
						query.setDate("upFromDt", upFromDt);
					if(!Utils.isNil(upToDt))
						query.setDate("upToDt", upToDt);
					if(!Utils.isNil(accCd))
						query.setInteger("accCd", accCd);
						
					
			lst = query.list();
		}
		catch (RuntimeException re) {
			System.out.println(re.toString());
		}
		finally {
			getSession().close();
		}
		return lst;
	}
	
}

