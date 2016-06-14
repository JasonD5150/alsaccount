package fwp.alsaccount.utils;

import fwp.alsaccount.admin.appservice.AlsAccCdControlAS;
import fwp.alsaccount.admin.appservice.AlsAccountMasterAS;
import fwp.alsaccount.admin.appservice.AlsSysActivityControlAS;
import fwp.alsaccount.hibernate.HibernateSessionFactory;
import fwp.alsaccount.hibernate.dao.AlsAccCdControl;
import fwp.alsaccount.hibernate.dao.AlsAccountMaster;
import fwp.alsaccount.hibernate.dao.AlsSysActivityControl;
import fwp.alsaccount.hibernate.utils.ListComp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.StringType;

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
	public String getAccountListTxt(String year, boolean addSelectOne)
			throws Exception {
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
	 * This method retrieves all provider numbers from the database as formatted
	 * text for a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public String getOrgListTxt(boolean addSelectOne) throws Exception {
		List<String> tmpLst = null;
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		String queryString = "SELECT aoc_org FROM als.als_activity_account_linkage "
				+ "UNION "
				+ "SELECT aoc_org FROM als.als_org_control "
				+ "ORDER BY 1";
		Query query = getSession().createSQLQuery(queryString);
		tmpLst = query.list();

		for (String i : tmpLst) {
			retVal += ";" + i + ":" + i;
		}
		getSession().close();

		return retVal;
	}

	/**
	 * This method retrieves fund codes from the database as formatted text for
	 * a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public String getFundListTxt(boolean addSelectOne) throws Exception {
		List<String> tmpLst = null;
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		String queryString = "SELECT DISTINCT AAM_FUND FROM ALS.ALS_ACTIVITY_ACCOUNT_LINKAGE WHERE aam_fund IS NOT NULL ORDER BY aam_fund";
		Query query = getSession().createSQLQuery(queryString);
		tmpLst = query.list();

		for (String i : tmpLst) {
			retVal += ";" + i + ":" + i;
		}
		getSession().close();

		return retVal;
	}

	/**
	 * This method retrieves fund codes from the database as formatted text for
	 * a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public String getSubclassListTxt(boolean addSelectOne) throws Exception {
		List<String> tmpLst = null;
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		String queryString = "SELECT distinct ASAC_SUBCLASS  FROM ALS.ALS_ACTIVITY_ACCOUNT_LINKAGE WHERE asac_subclass IS NOT NULL ORDER BY asac_subclass";
		Query query = getSession().createSQLQuery(queryString);
		tmpLst = query.list();

		for (String i : tmpLst) {
			retVal += ";" + i + ":" + i;
		}
		getSession().close();

		return retVal;
	}

	/**
	 * This method retrieves fund codes from the database as formatted text for
	 * a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public String getJLRListTxt(boolean addSelectOne) throws Exception {
		List<String> tmpLst = null;
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		String queryString = "SELECT DISTINCT am_val_desc || '00' am_val_desc "
				+ "FROM als.als_misc "
				+ "WHERE am_key1 = 'JOURNAL_LINE_REFERENCE' "
				+ "ORDER BY am_val_desc";
		Query query = getSession().createSQLQuery(queryString);
		tmpLst = query.list();

		for (String i : tmpLst) {
			retVal += ";" + i + ":" + i;
		}
		getSession().close();

		return retVal;
	}

	/**
	 * This method retrieves fund codes from the database as formatted text for
	 * a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public String getProjectGrantsListTxt(boolean addSelectOne)
			throws Exception {
		List<String> tmpLst = null;
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		String queryString = "SELECT distinct ASAC_PROJECT_GRANT FROM ALS.ALS_SYS_ACTIVITY_CONTROL";
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
}