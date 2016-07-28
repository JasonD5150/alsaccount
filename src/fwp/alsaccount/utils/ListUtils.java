package fwp.alsaccount.utils;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

import fwp.als.appservice.generic.AlsWebGenCodesAS;
import fwp.als.hibernate.generic.dao.AlsWebGenCodes;
import fwp.alsaccount.appservice.admin.AlsAccCdControlAS;
import fwp.alsaccount.appservice.admin.AlsAccountMasterAS;
import fwp.alsaccount.appservice.admin.AlsSysActivityControlAS;
import fwp.alsaccount.dao.admin.AlsAccCdControl;
import fwp.alsaccount.dao.admin.AlsAccountMaster;
import fwp.alsaccount.dao.admin.AlsSysActivityControl;
import fwp.alsaccount.hibernate.HibernateSessionFactory;
import fwp.alsaccount.hibernate.utils.ListComp;

public class ListUtils {
	public Session getSession() {
		return HibernateSessionFactory.getSession();
	}

	@SuppressWarnings("unchecked")
	public List<ListComp> getBudgetYearList() {
		List<ListComp> lst = new ArrayList<ListComp>();

		String queryString = "SELECT DISTINCT asac_budget_year itemLabel, asac_budget_year itemVal "
				+ "FROM als.ALS_SYS_ACTIVITY_CONTROL "
				+ "ORDER BY asac_budget_year DESC";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close();
		return lst;
	}

	@SuppressWarnings("unchecked")
	public List<ListComp> getProviderList() {
		List<ListComp> lst = new ArrayList<ListComp>();

		String queryString = "SELECT DISTINCT api.api_provider_no itemLabel, api.api_provider_no itemVal "
				+ "FROM als.als_provider_info api, "
				+ "     als.als_provider_status aps "
				+ "WHERE api.api_provider_no = aps.api_provider_no "
				+ "AND aps.api_provider_status = 'A' "
				+ "ORDER BY api.api_provider_no";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close();
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getItemTypeList(String upf, String upt) {
		List<ListComp> lst = new ArrayList<ListComp>();
		

		String queryString = "SELECT ai_item_id||aic_category_id||ait_type_id itemVal, "
						   + "ai_item_id||aic_category_id||ait_type_id||' - '||ait_type_desc itemLabel "
						   + "FROM als.als_item_type "
						   + "WHERE EXISTS (SELECT 1 "
						   + "FROM als.als_item_control_table "
						   + "WHERE aict_usage_period_from = to_date('"+upf+"','MM/DD/YYYY') "
						   + "AND aict_usage_period_to = to_date('"+upt+"','MM/DD/YYYY') "
						   + "AND aict_item_type_cd = ai_item_id||aic_category_id||ait_type_id) "
						   + "ORDER BY "
						   + "ai_item_id||aic_category_id||ait_type_id";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close(); 
		return lst;
	}

	/**
	 * This method retrieves all accounts for a given year from the database as
	 * formatted text for a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public String getAccountListTxt(String budgetYear, boolean addSelectOne) throws Exception {
		HibHelpers hh = new HibHelpers();
		String year = (budgetYear == null) ? hh.getCurrentBudgetYear() : budgetYear;
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		String where = " WHERE idPk.asacBudgetYear = " + year
				+ " ORDER BY idPk.aamAccount ";
		AlsAccountMasterAS appSer = new AlsAccountMasterAS();
		List<AlsAccountMaster> tmpLst = appSer.findAllByWhere(where);

		for (AlsAccountMaster tmp : tmpLst) {
			retVal += ";" + tmp.getIdPk().getAamAccount() + ":"
					+ tmp.getIdPk().getAamAccount();
		}

		getSession().close();

		return retVal;
	}

	/**
	 * This method retrieves all system activity codes for a given year from the
	 * database as formatted text for a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public String getActTypeTranCdListTxt(String year, boolean addSelectOne) {
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		String where = " WHERE idPk.asacBudgetYear = "
				+ year
				+ " ORDER BY idPk.asacSystemActivityTypeCd,cast(idPk.asacTxnCd as int) ASC ";
		AlsSysActivityControlAS appSer = new AlsSysActivityControlAS();
		List<AlsSysActivityControl> tmpLst = appSer.findAllByWhere(where);

		for (AlsSysActivityControl tmp : tmpLst) {
			retVal += ";" + tmp.getIdPk().getAsacSystemActivityTypeCd() + " "
					+ tmp.getIdPk().getAsacTxnCd() + ":"
					+ tmp.getIdPk().getAsacSystemActivityTypeCd() + " "
					+ tmp.getIdPk().getAsacTxnCd();
		}

		getSession().close();

		return retVal;
	}

	/**
	 * This method retrieves all provider numbers from the database as formatted
	 * text for a jqgrid column select list.
	 */
	public String getProviderListTxt(boolean addSelectOne) throws Exception {
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		try {
			List<ListComp> providerLst = this.getProviderList();

			if (providerLst != null && !providerLst.isEmpty()) {
				for (ListComp lc : providerLst) {
					retVal += ";" + String.valueOf(lc.getItemVal()) + ":" + lc.getItemLabel();
				}
			}
		} catch (Exception ex) {
			throw new Exception(this.getClass().getName() + ".retrieveAllInventoryCodesAsText - " + ex.getMessage());
		}

		return retVal;
	}

	/**
	 * This method retrieves all account codes for a given year from the
	 * database as formatted text for a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public String getAccCdListTxt(String year, boolean addSelectOne)
			throws Exception {
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		String where = " WHERE idPk.asacBudgetYear = " + year
				+ " ORDER BY idPk.aaccAccCd ";
		AlsAccCdControlAS appSer = new AlsAccCdControlAS();
		List<AlsAccCdControl> tmpLst = appSer.findAllByWhere(where);
		String tmpCd = null;
		for (AlsAccCdControl tmp : tmpLst) {
			if (!tmp.getIdPk().getAaccAccCd().equals(tmpCd)) {
				tmpCd = tmp.getIdPk().getAaccAccCd();
				retVal += ";" + tmp.getIdPk().getAaccAccCd() + ":"
						+ tmp.getIdPk().getAaccAccCd();
			}
		}

		getSession().close();

		return retVal;
	}

	/**
	 * This method retrieves all org numbers from the database as formatted
	 * text for a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public String getOrgListTxt(String budgetYear, boolean addSelectOne) throws Exception {
		HibHelpers hh = new HibHelpers();
		String year = (budgetYear == null) ? hh.getCurrentBudgetYear() : budgetYear;
		List<String> tmpLst = null;
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		String queryString = "SELECT aoc_org FROM als.als_activity_account_linkage "
						   + "WHERE aoc_org IS NOT NULL "
						   + "AND asac_budget_year ="+year+" "
						   + "UNION "
						   + "SELECT aoc_org FROM als.als_org_control "
						   + "WHERE aoc_org IS NOT NULL "
						   + "AND asac_budget_year ="+year+" "
						   + "ORDER BY 1";
		Query query = getSession().createSQLQuery(queryString);
		tmpLst = query.list();

		for (String i : tmpLst) {
			if(!"".equals(i) && i != null){
				retVal += ";" + i + ":" + i;
			}
			
		}
		getSession().close();

		return retVal;
	}

	/**
	 * This method retrieves fund codes from the database as formatted text for
	 * a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public String getFundListTxt(String budgetYear, boolean addSelectOne) throws Exception {
		HibHelpers hh = new HibHelpers();
		String year = (budgetYear == null) ? hh.getCurrentBudgetYear() : budgetYear;
		List<String> tmpLst = null;
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		String queryString = "SELECT DISTINCT AAM_FUND "
						   + "FROM ALS.ALS_ACTIVITY_ACCOUNT_LINKAGE "
						   + "WHERE aam_fund IS NOT NULL "
						   + "AND asac_budget_year ="+year+" "
						   + "UNION "
						   + "SELECT DISTINCT AACC_FUND "
						   + "FROM ALS.ALS_ACC_CD_CONTROL "
						   + "WHERE aacc_fund IS NOT NULL "
						   + "AND asac_budget_year ="+year+" "
						   + "ORDER BY aam_fund";
						   
		Query query = getSession().createSQLQuery(queryString);
		tmpLst = query.list();

		for (String i : tmpLst) {
			retVal += ";" + i + ":" + i;
		}
		getSession().close();

		return retVal;
	}

	/**
	 * This method retrieves subclass codes from the database as formatted text for
	 * a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public String getSubclassListTxt(String budgetYear, boolean addSelectOne) throws Exception {
		HibHelpers hh = new HibHelpers();
		String year = (budgetYear == null) ? hh.getCurrentBudgetYear() : budgetYear;
		List<String> tmpLst = null;
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		String queryString = "SELECT distinct ASAC_SUBCLASS  "
						   + "FROM ALS.ALS_ACTIVITY_ACCOUNT_LINKAGE "
						   + "WHERE ASAC_SUBCLASS IS NOT NULL "
						   + "AND ASAC_BUDGET_YEAR="+year+" "
						   + "UNION "
						   + "SELECT distinct ASAC_SUBCLASS  "
						   + "FROM ALS.ALS_ACC_CD_CONTROL "
						   + "WHERE ASAC_SUBCLASS IS NOT NULL "
						   + "AND ASAC_BUDGET_YEAR="+year+" "
						   + "ORDER BY asac_subclass";
		
		Query query = getSession().createSQLQuery(queryString);
		tmpLst = query.list();

		for (String i : tmpLst) {
			retVal += ";" + i + ":" + i;
		}
		getSession().close();

		return retVal;
	}
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getJLRList() {
		List<ListComp> lst = new ArrayList<ListComp>();
		
		String queryString = "SELECT DISTINCT am_val_desc || '00'  itemVal, "
						   + "am_val_desc || '00'  itemLabel "
						   + "FROM als.als_misc "
						   + "WHERE am_key1 = 'JOURNAL_LINE_REFERENCE' "
						   + "ORDER BY itemVal ";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close(); 
		return lst;
	}

	/**
	 * This method retrieves Journal Line Reference codes from the database as formatted text for
	 * a jqgrid column select list.
	 */
	public String getJLRListTxt(boolean addSelectOne) throws Exception {
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		List<ListComp> jlrLst = this.getJLRList();

		for (ListComp i : jlrLst) {
			retVal += ";" + i.getItemVal() + ":" + i.getItemLabel();
		}
		getSession().close();

		return retVal;
	}

	/**
	 * This method retrieves Project Grants from the database as formatted text for
	 * a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public String getProjectGrantsListTxt(String budgetYear, boolean addSelectOne)
			throws Exception {
		HibHelpers hh = new HibHelpers();
		String year = (budgetYear == null) ? hh.getCurrentBudgetYear() : budgetYear;
		List<String> tmpLst = null;
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		String queryString = "SELECT distinct ASAC_PROJECT_GRANT "
						   + "FROM ALS.ALS_SYS_ACTIVITY_CONTROL "
						   + "WHERE ASAC_PROJECT_GRANT IS NOT NULL "
						   + "AND ASAC_BUDGET_YEAR ="+year+" "
						   + "ORDER BY ASAC_PROJECT_GRANT ";

		Query query = getSession().createSQLQuery(queryString);
		tmpLst = query.list();
		if (tmpLst.size() == 1) {
			retVal = "";
		} else {
			for (String i : tmpLst) {
				retVal += ";" + i + ":" + i;
			}
		}

		getSession().close();

		return retVal;
	}
	
	/**
	 * getIdGenCode 
	 * retrieve an List of relevant AlsWebGenCode
	 * awgcId will be in itemVal, description in itemLabel
	 * @param codeType
	 * @return List<ListComp>
	 */		
	public List<ListComp> getIdGenCode(String codeType){		
		ListComp lc;
		List<ListComp> lst = new ArrayList<ListComp>();
		AlsWebGenCodesAS awgcAS = new AlsWebGenCodesAS();
		List<AlsWebGenCodes> records = awgcAS.findAllByWhere("where awgcName = '" + codeType + "'",null);
		
		for(AlsWebGenCodes row: records){
			lc = new ListComp();
			lc.setItemVal(row.getAwgcId().toString());
			lc.setItemLabel(row.getAwgcDescr());
			lst.add(lc);
		}
		return lst;		
	}	
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getBankCodeList() {
		List<ListComp> lst = new ArrayList<ListComp>();
		
		String queryString = "SELECT DISTINCT atgs.ABC_BANK_CD itemVal, "
				   + "atgs.ABC_BANK_CD||' - '||abc.ABC_BANK_NM itemLabel "
				   + "FROM ALS.ALS_TRANSACTION_GRP_STATUS atgs, "
				   + "ALS.ALS_BANK_CODE abc "
				   + "WHERE atgs.ABC_BANK_CD = abc.ABC_BANK_CD "
				   + "AND atgs.ATG_TRANSACTION_CD = atgs.ATG_TRANSACTION_CD "
				   + "ORDER BY 1";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close(); 
		return lst;
	}
	
	/**
	 * This method retrieves Project Grants from the database as formatted text for
	 * a jqgrid column select list.
	 */
	public String getBankCodeListTxt(boolean addSelectOne)
			throws Exception {
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		try {
			List<ListComp> bankCodeLst = this.getBankCodeList();

			if (bankCodeLst != null && !bankCodeLst.isEmpty()) {
				for (ListComp lc : bankCodeLst) {
					retVal += ";" + String.valueOf(lc.getItemVal()) + ":" + lc.getItemLabel();
				}
			}
		} catch (Exception ex) {
			throw new Exception(this.getClass().getName() + ".retrieveAllInventoryCodesAsText - " + ex.getMessage());
		}

		return retVal;
	}
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getGroupIdentifierList() {
		List<ListComp> lst = new ArrayList<ListComp>();
		
		String queryString = "SELECT ATG_TRANSACTION_CD itemVal,"
						   + "ATGS_GROUP_IDENTIFIER itemLabel "
						   + "FROM ALS.ALS_TRANSACTION_GRP_STATUS "
						   + "WHERE ATG_TRANSACTION_CD = ATG_TRANSACTION_CD "
						   + "ORDER BY 1,2 Desc";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close(); 
		return lst;
	}

	public String getGroupIdentifierListTxt(boolean addSelectOne)
			throws Exception {

		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		try {
			List<ListComp> groupIdentifierLst = this.getGroupIdentifierList();

			if (groupIdentifierLst != null && !groupIdentifierLst.isEmpty()) {
				for (ListComp lc : groupIdentifierLst) {
					retVal += ";" + lc.getItemLabel() + ":" + String.valueOf(lc.getItemVal())+" - "+lc.getItemLabel();
				}
			}
		} catch (Exception ex) {
			throw new Exception(this.getClass().getName() + ".retrieveAllInventoryCodesAsText - " + ex.getMessage());
		}

		return retVal;
	}
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getSabhrsQueryAccountLst() {
		List<ListComp> lst = new ArrayList<ListComp>();
		
		String queryString = "SELECT DISTINCT AAM_ACCOUNT itemVal, "
							+ "AAM_ACCOUNT itemLabel "
							+ "FROM ALS.ALS_SABHRS_ENTRIES "
							+ "ORDER BY AAM_ACCOUNT ";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close(); 
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getSabhrsTransGroupTypeLst() {
		List<ListComp> lst = new ArrayList<ListComp>();
		
		String queryString = "SELECT DISTINCT TG.ATG_TRANSACTION_CD itemVal, "
							+ "TG.ATG_TRANSACTION_CD||' - '||TG.ATG_TRANSACTION_DESC itemLabel "
							+ "FROM ALS.ALS_TRANSACTION_GROUP TG, "
							+ "ALS.ALS_TRANSACTION_GRP_STATUS TGS "
							+ "WHERE TG.ATG_TRANSACTION_CD = TGS.ATG_TRANSACTION_CD "
							+ "ORDER BY 1,2 desc";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close(); 
		return lst;
	}
	
}