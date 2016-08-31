package fwp.alsaccount.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

import fwp.ListComp;
import fwp.als.appservice.admin.AlsMiscAS;
import fwp.als.appservice.generic.AlsWebGenCodesAS;
import fwp.als.hibernate.admin.dao.AlsMisc;
import fwp.als.hibernate.admin.dao.AlsSysActivityControl;
import fwp.als.hibernate.admin.dao.AlsTribeInfo;
import fwp.als.hibernate.generic.dao.AlsWebGenCodes;
import fwp.als.hibernate.item.dao.AlsItemCategory;
import fwp.alsaccount.appservice.admin.AlsAccCdControlAS;
import fwp.alsaccount.appservice.admin.AlsAccountMasterAS;
import fwp.alsaccount.appservice.admin.AlsItemCategoryAS;
import fwp.alsaccount.appservice.admin.AlsSysActivityControlAS;
import fwp.alsaccount.appservice.admin.AlsTribeInfoAS;
import fwp.alsaccount.appservice.sabhrs.AlsTransactionGrpStatusAS;
import fwp.alsaccount.dao.admin.AlsAccCdControl;
import fwp.alsaccount.dao.admin.AlsAccountMaster;
import fwp.alsaccount.dao.sabhrs.AlsTransactionGrpStatus;
import fwp.alsaccount.hibernate.HibernateSessionFactory;


public class ListUtils {
	public Session getSession() {
		return HibernateSessionFactory.getSession();
	}

	/**
     * Return a list of miscellaneous codes and descriptions for the given type.
     *
     * @return list of ListComp for the item status
     * @throws Exception
     */
    
	public List<ListComp> getMiscCodes(String amKey1Value, String amKey2Value, String amKey3Value, String amKey4Value, String amKey5Value, String labelFieldName,
			List<String> excludeList, Boolean useCodeInLabel, final Boolean sortByLabel) throws Exception {
        List<ListComp> lcList = new ArrayList<ListComp>();

        try {
        	
        	AlsMiscAS appServ = new AlsMiscAS();
        	
        	String whereClause = "WHERE amKey1 = '" + amKey1Value + "'"; 
    		
        	if (StringUtils.isNotBlank(amKey2Value)) {
        		whereClause += " AND amKey2 = '" + amKey2Value + "'";
        	}
        	if (StringUtils.isNotBlank(amKey3Value)) {
        		whereClause += " AND amKey3 = '" + amKey3Value + "'";
        	}
        	if (StringUtils.isNotBlank(amKey4Value)) {
        		whereClause += " AND amKey4 = '" + amKey4Value + "'";
        	}
        	if (StringUtils.isNotBlank(amKey5Value)) {
        		whereClause += " AND amKey5 = '" + amKey5Value + "'";
        	}
        	
    		List<AlsMisc> miscCodeLst = appServ.findAllByWhere(whereClause);		
	        
    		if (miscCodeLst != null && !miscCodeLst.isEmpty()) {
    	        for (AlsMisc am : miscCodeLst) {
    	            ListComp lc = new ListComp();
    	            lc.setItemVal(am.getAmParVal());
    	            
    	            if (labelFieldName == null || "amValDesc".equals(labelFieldName)) {
        	            lc.setItemLabel(am.getAmValDesc());
    	            }
    	            else if ("amKey2".equals(labelFieldName)) {
        	            lc.setItemLabel(am.getAmKey2());
    	            }
    	            else {
    	            	throw new Exception("labelFieldName: "+labelFieldName+" has not been implemented yet.");
    	            }
    	            
    	            if (useCodeInLabel) {
    	            	lc.setItemLabel(lc.getItemVal() + " - " + lc.getItemLabel());
    	            }
    	            
    				boolean skipAdd=false;
    				if (excludeList != null && !excludeList.isEmpty()) {    					
    					for (String itemVal : excludeList) {    						
    						if (itemVal.equals(lc.getItemLabel())) {
    							skipAdd=true;
    							break;
    						}
    					}
    				}
    				
    				if (!skipAdd) {
        	            lcList.add(lc);
    				}
    	        }

    	        Collections.sort(lcList, new Comparator<ListComp>() {
    	            @Override
    	            public int compare(ListComp o1, ListComp o2) {
    	            	if (sortByLabel) {
        	                return o1.getItemLabel().compareTo(o2.getItemLabel());
    	            	}
    	            	else {
        	                return o1.getItemVal().compareTo(o2.getItemVal());
    	            	}
    	            }
    	        });
    		}	        	        
	    }
	    catch (Exception ex) {
			throw new Exception (this.getClass().getName() + ".findMiscCodes - " +ex.getMessage());
	    } finally {
	    	if (getSession() != null) {
	    		getSession().close();
	    	}
	    }
	    
        return lcList;
    }
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getItemTypeCd(String itemCd){
		List<ListComp> lst = new ArrayList<ListComp>();
		
		String where = "";
		
		if (itemCd != null) {
			where = " where LPAD(AI_ITEM_ID,2,0)||LPAD(AIC_CATEGORY_ID,2,0)||LPAD(AIT_TYPE_ID,3,0) = '" + itemCd +"' ";
		}
		
		String queryString = 
				" select LPAD(AI_ITEM_ID,2,0)||LPAD(AIC_CATEGORY_ID,2,0)||LPAD(AIT_TYPE_ID,3,0) itemVal, " +
				" LPAD(AI_ITEM_ID,2,0)||LPAD(AIC_CATEGORY_ID,2,0)||LPAD(AIT_TYPE_ID,3,0)||' - '||ait_type_desc itemLabel " +
				" from als.als_item_type " +
				where +
				" order by LPAD(AI_ITEM_ID,2,0)||LPAD(AIC_CATEGORY_ID,2,0)||LPAD(AIT_TYPE_ID,3,0)"; 
		
		Query query = getSession().createSQLQuery(queryString )
				.addScalar("itemVal")
				.addScalar("itemLabel")
				
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

	
		lst = query.list();
		getSession().close();
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getBudgetYearList() {
		List<ListComp> lst = new ArrayList<ListComp>();
		lst.add(new ListComp());
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
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getItemCategoryList() {
		List<ListComp> lst = new ArrayList<ListComp>();
		ListComp tmp;
		
		String where = " WHERE 1=1 "
				+ " ORDER BY idPk.aicCategoryId ";
		
		AlsItemCategoryAS appSer = new AlsItemCategoryAS();
		List<AlsItemCategory> aicLst = appSer.findAllByWhere(where);

		for (AlsItemCategory aic : aicLst) {
			tmp = new ListComp();
			tmp.setItemLabel(aic.getIdPk().getAicCategoryId()+" - "+aic.getAicCategoryDesc());
			tmp.setItemVal(aic.getIdPk().getAicCategoryId());
			lst.add(tmp);
		}

		getSession().close();

		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getTribeCdList() {
		List<ListComp> lst = new ArrayList<ListComp>();
		ListComp tmp;
		
		String where = " WHERE 1=1 ";
		
		AlsTribeInfoAS appSer = new AlsTribeInfoAS();
		List<AlsTribeInfo> atiLst = appSer.findAllByWhere(where);

		for (AlsTribeInfo ati : atiLst) {
			tmp = new ListComp();
			tmp.setItemLabel(ati.getAtiTribeCd());
			tmp.setItemVal(ati.getAtiTribeCd());
			lst.add(tmp);
		}

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
	public List<ListComp> getTransGrpAppProviderList() {
		List<ListComp> lst = new ArrayList<ListComp>();

		String queryString = "SELECT DISTINCT api.api_provider_no itemLabel, api.api_provider_no itemVal "
				+ "FROM als.als_provider_info api, "
				+ "     als.als_provider_status aps "
				+ "WHERE api.api_provider_cat = 'O' "
				+ "AND api.api_provider_class = 'I' "
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

	/**
	 * This method retrieves all accounts for a given year from the database as
	 * formatted text for a jqgrid column select list.
	 */
	@SuppressWarnings("unchecked")
	public List<ListComp> getAccountList(String budgetYear) throws Exception {
		List<ListComp> lst = new ArrayList<ListComp>();
		ListComp tmp;
		HibHelpers hh = new HibHelpers();
		String year = (budgetYear == null) ? hh.getCurrentBudgetYear() : budgetYear;

		String where = " WHERE idPk.asacBudgetYear = " + year
				+ " ORDER BY idPk.aamAccount ";
		
		AlsAccountMasterAS appSer = new AlsAccountMasterAS();
		List<AlsAccountMaster> aamLst = appSer.findAllByWhere(where);

		for (AlsAccountMaster aam : aamLst) {
			tmp = new ListComp();
			tmp.setItemLabel(aam.getIdPk().getAamAccount());
			tmp.setItemVal(aam.getIdPk().getAamAccount());
			lst.add(tmp);
		}

		getSession().close();

		return lst;
	}

	/**
	 * This method retrieves all org numbers from the database as ListComp
	 */
	@SuppressWarnings("unchecked")
	public List<ListComp> getOrgList(String budgetYear) throws Exception {
		List<ListComp> lst = new ArrayList<ListComp>();
		HibHelpers hh = new HibHelpers();
		String year = (budgetYear == null) ? hh.getCurrentBudgetYear() : budgetYear;

		String queryString = "SELECT aoc_org itemVal, "
						   + "aoc_org itemLabel " 
						   + "FROM als.als_activity_account_linkage "
						   + "WHERE aoc_org IS NOT NULL "
						   + "AND asac_budget_year ="+year+" "
						   + "UNION "
						   + "SELECT aoc_org itemVal, "
						   + "aoc_org itemLabel "
						   + "FROM als.als_org_control "
						   + "WHERE aoc_org IS NOT NULL "
						   + "AND asac_budget_year ="+year+" "
						   + "ORDER BY 1";
		
		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close(); 
		return lst;
	}

	@SuppressWarnings("unchecked")
	public List<ListComp> getJLRList() {
		List<ListComp> lst = new ArrayList<ListComp>();
		
		String queryString = "SELECT DISTINCT am_val_desc  itemVal, "
						   + "am_val_desc  itemLabel "
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
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getJLRBudgYearList(String year) {
		HibHelpers hh = new HibHelpers();
		if(year == null || "".equals(year)){
			year = hh.getCurrentBudgetYear();
		}
		List<ListComp> lst = new ArrayList<ListComp>();
		
		String queryString = "SELECT DISTINCT am_val_desc || SUBSTR('"+year+"',3,4)  itemVal, "
						   + "am_val_desc || SUBSTR('"+year+"',3,4)  itemLabel "
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

	/**
	 * This method retrieves fund codes from the database as ListComp
	 */
	@SuppressWarnings("unchecked")
	public List<ListComp> getFundList(String budgetYear) throws Exception {
		HibHelpers hh = new HibHelpers();
		String year = (budgetYear == null) ? hh.getCurrentBudgetYear() : budgetYear;
		
		List<ListComp> lst = new ArrayList<ListComp>();

		String queryString = "SELECT DISTINCT AAM_FUND itemVal,"
						   + "AAM_FUND itemLabel "
						   + "FROM ALS.ALS_ACTIVITY_ACCOUNT_LINKAGE "
						   + "WHERE aam_fund IS NOT NULL "
						   + "AND asac_budget_year ="+year+" "
						   + "UNION "
						   + "SELECT DISTINCT AACC_FUND itemVal, "
						   + "AACC_FUND itemLabel "
						   + "FROM ALS.ALS_ACC_CD_CONTROL "
						   + "WHERE aacc_fund IS NOT NULL "
						   + "AND asac_budget_year ="+year+" "
						   + "ORDER BY itemVal";
						   
		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close(); 
		return lst;
	}
	
	/**
	 * This method retrieves subclass codes from the database as ListComp
	 */
	@SuppressWarnings("unchecked")
	public List<ListComp> getSubclassList(String budgetYear) throws Exception {
		HibHelpers hh = new HibHelpers();
		String year = (budgetYear == null) ? hh.getCurrentBudgetYear() : budgetYear;

		List<ListComp> lst = new ArrayList<ListComp>();

		String queryString = "SELECT DISTINCT ASAC_SUBCLASS itemVal, "
						   + "ASAC_SUBCLASS itemLabel "
						   + "FROM ALS.ALS_ACTIVITY_ACCOUNT_LINKAGE "
						   + "WHERE ASAC_SUBCLASS IS NOT NULL "
						   + "AND ASAC_BUDGET_YEAR="+year+" "
						   + "UNION "
						   + "SELECT DISTINCT ASAC_SUBCLASS itemVal,"
						   + "ASAC_SUBCLASS itemLabel "
						   + "FROM ALS.ALS_ACC_CD_CONTROL "
						   + "WHERE ASAC_SUBCLASS IS NOT NULL "
						   + "AND ASAC_BUDGET_YEAR="+year+" "
						   + "ORDER BY itemVal";
		
		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close(); 
		return lst;
	}
	
	/**
	 * This method retrieves all system activity codes for a given year from the
	 * database as ListComp
	 */
	@SuppressWarnings("unchecked")
	public List<ListComp> getActTypeTranCdList(String budgetYear) {
		HibHelpers hh = new HibHelpers();
		String year = (budgetYear == null) ? hh.getCurrentBudgetYear() : budgetYear;
		
		List<ListComp> lst = new ArrayList<ListComp>();
		ListComp tmp;
		String where = " WHERE idPk.asacBudgetYear = "
						+ year
						+ " ORDER BY idPk.asacSystemActivityTypeCd,cast(idPk.asacTxnCd as int) ASC ";
		
		AlsSysActivityControlAS appSer = new AlsSysActivityControlAS();
		List<AlsSysActivityControl> asacLst = appSer.findAllByWhere(where);

		for (AlsSysActivityControl asac : asacLst) {
			tmp = new ListComp();
			tmp.setItemLabel(asac.getIdPk().getAsacSystemActivityTypeCd()+ asac.getIdPk().getAsacTxnCd());
			tmp.setItemVal(asac.getIdPk().getAsacSystemActivityTypeCd()+ asac.getIdPk().getAsacTxnCd());
			lst.add(tmp);
		}

		getSession().close();

		return lst;
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
	public List<ListComp> getTransGrpBankCodeList() {
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
	public String getTransGrpBankCodeListTxt(boolean addSelectOne)
			throws Exception {
		String retVal = ": ";

		if (addSelectOne) {
			retVal = ":-- Select One --";
		}

		try {
			List<ListComp> bankCodeLst = this.getTransGrpBankCodeList();

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
	public List<ListComp> getProviderBankCodeList(Integer provNo) {
		List<ListComp> lst = new ArrayList<ListComp>();
		
		String queryString = "SELECT DISTINCT abc.ABC_BANK_CD itemVal, "
							+ "abc.ABC_BANK_CD itemLabel "
							+ "FROM  ALS.ALS_BANK_CODE abc  "
							//+ "WHERE abc.api"
							+ "ORDER BY 1";

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
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getBillingToLst(Integer provNo) {
		List<ListComp> lst = new ArrayList<ListComp>();
		
		String queryString = "SELECT AIR_BILLING_TO BILLING_DT||' - '||TO_CHAR(AIR_BILLING_TO,''MM/DD/YYYY'') itemVal,"
							+ "AIR_BILLING_TO BILLING_DT||' - '||TO_CHAR(AIR_BILLING_TO,''MM/DD/YYYY'') itemLabel "
		 	   	            +"FROM ALS.ALS_INTERNAL_REMITTANCE "
		 	   	            +"WHERE API_PROVIDER_NO = "+provNo+" "
		 	                +"Order By  1 DESC";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close(); 
		return lst;
	}
	
	
	
	
	
	
	
	
	
	/*IAFA Query Lists*/
	@SuppressWarnings("unchecked")
	public List<ListComp> getIafaQueryProviderList() {
		List<ListComp> lst = new ArrayList<ListComp>();

		String queryString = "SELECT DISTINCT ALS_ITEM_APPL_FEE_ACCT.API_PROVIDER_NO itemVal, "
							+ "ALS_ITEM_APPL_FEE_ACCT.API_PROVIDER_NO||' - '||API_BUSINESS_NM itemLabel "
							+ "FROM ALS.ALS_PROVIDER_INFO, ALS.ALS_ITEM_APPL_FEE_ACCT "
							+ "WHERE ALS_PROVIDER_INFO.API_PROVIDER_NO = ALS_ITEM_APPL_FEE_ACCT.API_PROVIDER_NO "
							+ "ORDER BY 1,2";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close();
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getIafaApplicationTypeList() {
		List<ListComp> lst = new ArrayList<ListComp>();

		String queryString = "SELECT DISTINCT AIAFA_APP_TYPE itemVal, "
							+ "AIAFA_APP_TYPE itemLabel "
							+ "FROM ALS.ALS_ITEM_APPL_FEE_ACCT "
							+ "WHERE AIAFA_APP_TYPE IS NOT NULL "
							+ "ORDER BY 1";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close();
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getIafaPrerequisiteList() {
		List<ListComp> lst = new ArrayList<ListComp>();

		String queryString = "SELECT APC_PREREQUISITE_CD itemVal, "
							+ "APC_PREREQUISITE_CD||' - '||APC_PREREQUISITE_DESC itemLabel "
							+ "FROM ALS.ALS_PREREQUISITE_CD "
							+ "ORDER BY APC_PREREQUISITE_DESC";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close();
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getTransGrpIdList(Integer provNo) {
		List<ListComp> lst = new ArrayList<ListComp>();
		ListComp tmp;
		StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
		if(provNo != null){
			where.append("AND TRIM(TRIM(LEADING 0 FROM substr(idPk.atgsGroupIdentifier,3,6))) = '"+provNo+"' ");
		}
		where.append("ORDER BY 1,2 DESC");;
		
		AlsTransactionGrpStatusAS appSer = new AlsTransactionGrpStatusAS();
		List<AlsTransactionGrpStatus> atgsLst = appSer.findAllByWhere(where.toString());

		for (AlsTransactionGrpStatus atgs : atgsLst) {
			tmp = new ListComp();
			tmp.setItemLabel(atgs.getIdPk().getAtgsGroupIdentifier());
			tmp.setItemVal(atgs.getIdPk().getAtgsGroupIdentifier());
			lst.add(tmp);
		}
		getSession().close();
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<ListComp> getIafaSummaryProcessTypeList() {
		List<ListComp> lst = new ArrayList<ListComp>();

		String queryString = "SELECT AM_PAR_VAL itemVal, "
							+ "AM_PAR_VAL||' - '||AM_KEY2 itemLabel "
							+ "FROM ALS.ALS_MISC "
							+ "WHERE Am_Key1='PROCESS TYPE' And Am_Key2 IN ('ISSUE','OFFLINE ISSUE') "
							+ "ORDER BY  2,1";

		Query query = getSession().createSQLQuery(queryString)
				.addScalar("itemVal", StringType.INSTANCE)
				.addScalar("itemLabel", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(ListComp.class));

		lst = query.list();
		getSession().close();
		return lst;
	}
}