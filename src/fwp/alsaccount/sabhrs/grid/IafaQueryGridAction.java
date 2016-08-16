package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;
import fwp.alsaccount.dto.sabhrs.IafaQueryDTO;
import fwp.alsaccount.utils.HibHelpers;

public class IafaQueryGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(IafaQueryGridAction.class);

    private List<IafaQueryDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    
    private Integer 			issProvNo;
    private Date				fromDt;
    private Date 				toDt;
    
    private Boolean				alxInd;
    private Integer				entProvNo;
    private Date 				bpFromDt;
    private Date 				bpToDt;
    private Integer 			iafaSeqNo;
    
    private String 				sessStat;
    private Date				upFromDt;
    private Date 				upToDt;
    private String 				tribeCd;
    private String 				appType;
    
    private String 				itemTypeCd;
    private String				noCharge;
    private Double				amount;
    
    private String 				itemInd;
    private String 				itemStat;
    
    private String 				itemCatCd;
    private String 				appDis;
    
    private String 				itemTransInd;
    private Integer 			seqNoInItemTrans;
    private Date 				dob;
    private Integer				alsNo;
    private String				bonusPoints;
    
    private String 				amountTypeCd;
    private String 				costPrereqCd;
    
    private String 				procCatCd;
    private String 				sessOrigin;
    private String				resIndicator;

	private String				procTypeCd;
    private Date				batchRecDt;
    
    private String 				reasonCd;
    private String 				transGrpIdentifier;
    private String 				nullTGI;
    
    private String 				ahmType;
    private String 				ahmCd;
	private Date				sessDt;
    private Date				sessVoidDt;
    private Date				recordVoidDt;
    
    private String 				sumAppStat;
    private String				intAppStat;
    private String 				otherTransGrp;
    
    private String				modeOfPayment;
    private Integer				chckNo;
    private String 				chckWriter;
    private String 				remarks;
    
    private Boolean 			search = false;
    private String 				userdata;
    
	public String buildgrid(){ 
    	HibHelpers hh = new HibHelpers();
    	String where = buildQueryStr();
    	
    	List<IafaQueryDTO> iafaLst = new ArrayList<IafaQueryDTO>();
    	IafaQueryDTO iafa = null;
        try{
        	setModel(new ArrayList<IafaQueryDTO>());
        	if (search) {
        		Integer cnt = hh.getIafaQueryCount(where);
        		if(cnt > 10000){
        			setUserdata("Please narrow search. The search grid is limited to 10000 rows. There were " + cnt + " entries selected.");
            	}else{
        			iafaLst = hh.getIafaQueryRecords(where);
        			for(IafaQueryDTO tmp : iafaLst){
            			iafa = tmp;
            			iafa.setOtherTxnGrp(hh.getOtherTxnGrp(iafa.getApiProviderNo(), iafa.getAprBillingFrom(), iafa.getAprBillingTo(), iafa.getAiafaSeqNo(), iafa.getAtgsGroupIdentifier()));
            			if(iafa.getAcdCostGroupSeqNo() != null){
            				if(iafa.getAcdCostGroupSeqNo() == 0){
                				iafa.setCostGrpDesc("INDIVIDUAL");
                			}else{
                				iafa.setCostGrpDesc(hh.getCostGrpDesc(iafa.getAictUsagePeriodFrom(), iafa.getAictUsagePeriodTo(), iafa.getAictItemTypeCd(), iafa.getResidency(), iafa.getAcdCostGroupSeqNo()));
                			}
            			}
            			iafa.setSessionTotal(hh.getSessionTotal(iafa.getAhmType(), iafa.getAhmCd(), iafa.getAsSessionDt(), fromDt, toDt));
            			iafa.setSessionDt(iafa.getAsSessionDt());
            			iafa.setSeqNoforPrintedItems(hh.getSeqNoForPrintedItems(iafa.getDob(), iafa.getAlsNo(), iafa.getAictUsagePeriodFrom(), iafa.getAictUsagePeriodTo(), iafa.getAictItemTypeCd(), iafa.getAiiItemTxnInd(), iafa.getAiiSeqNo()));
            			model.add(iafa);
        			}
        		}
    		}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsSabhrsEntries did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());

        setTotal(1);

	    return SUCCESS;
    }

	private String buildQueryStr(){
		StringBuilder srchStr = new StringBuilder();
		/*SELECT*/
		srchStr.append(
				/*FROM*/
				 "FROM ALS.ALS_ITEM_APPL_FEE_ACCT, "
				+ "ALS.ALS_SESSIONS, "
				+ "ALS.ALS_SESSION_TRANS, "
				+ "ALS.ALS_ITEM_INFORMATION, "
				+ "ALS.ALS_PROVIDER_INFO, "
				+ "ALS.ALS_BATCH_INFO, "
				+ "ALS.ALS_ITEM_TYPE, "
				+ "ALS.ALS_ITEM_CATEGORY, "
				+ "ALS.ALS_APPLICATION_INFORMATION, "
				+ "ALS.ALS_SABHRS_ENTRIES, "
				+ "ALS.ALS_TRANSACTION_GRP_STATUS "
				/*WHERE*/
				+ "WHERE ALS.ALS_ITEM_APPL_FEE_ACCT.AIAFA_WHEN_LOG >= TO_DATE('"+fromDt+"','YYYY-MM-DD') "
				+ "AND ALS.ALS_ITEM_APPL_FEE_ACCT.AIAFA_WHEN_LOG < (TO_DATE('"+toDt+"','YYYY-MM-DD') + 1) "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.AHM_TYPE = ALS_SESSIONS.AHM_TYPE(+) "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.AHM_CD = ALS_SESSIONS.AHM_CD(+) "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.AS_SESSION_DT = ALS_SESSIONS.AS_SESSION_DT(+) "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.API_PROVIDER_NO = ALS_PROVIDER_INFO.API_PROVIDER_NO "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.AHM_TYPE = ALS_SESSION_TRANS.AHM_TYPE(+) "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.AHM_CD = ALS_SESSION_TRANS.AHM_CD(+) "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.AS_SESSION_DT = ALS_SESSION_TRANS.AS_SESSION_DT(+) "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.AST_TRANSACTION_CD = ALS_SESSION_TRANS.AST_TRANSACTION_CD(+) "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.AST_TRANSACTION_SEQ_NO = ALS_SESSION_TRANS.AST_TRANSACTION_SEQ_NO(+) "
				+ "AND ALS_SESSIONS.ABI_LICENSE_YR = ALS_BATCH_INFO.ABI_LICENSE_YR(+) "
				+ "AND ALS_SESSIONS.ABI_BATCH_NO = ALS_BATCH_INFO.ABI_BATCH_NO(+) "
				+ "AND SUBSTR(ALS_SESSION_TRANS.AICT_ITEM_TYPE_CD,1,2) = ALS_ITEM_TYPE.AI_ITEM_ID(+) "
				+ "AND SUBSTR(ALS_SESSION_TRANS.AICT_ITEM_TYPE_CD,3,2) = ALS_ITEM_TYPE.AIC_CATEGORY_ID(+) "
				+ "AND SUBSTR(ALS_SESSION_TRANS.AICT_ITEM_TYPE_CD,5,3) = ALS_ITEM_TYPE.AIT_TYPE_ID(+) "
				+ "AND SUBSTR(ALS_SESSION_TRANS.AICT_ITEM_TYPE_CD,1,2) = ALS_ITEM_CATEGORY.AI_ITEM_ID(+) "
				+ "AND SUBSTR(ALS_SESSION_TRANS.AICT_ITEM_TYPE_CD,3,2) = ALS_ITEM_CATEGORY.AIC_CATEGORY_ID(+) "
				+ "AND ALS_SESSION_TRANS.AICT_USAGE_PERIOD_FROM = ALS_ITEM_INFORMATION.AICT_USAGE_PERIOD_FROM(+) "
				+ "AND ALS_SESSION_TRANS.AICT_USAGE_PERIOD_TO = ALS_ITEM_INFORMATION.AICT_USAGE_PERIOD_TO(+) "
				+ "AND ALS_SESSION_TRANS.AICT_ITEM_TYPE_CD = ALS_ITEM_INFORMATION.AICT_ITEM_TYPE_CD(+) "
				+ "AND ALS_SESSION_TRANS.API_DOB = ALS_ITEM_INFORMATION.API_DOB(+) "
				+ "AND ALS_SESSION_TRANS.API_ALS_NO = ALS_ITEM_INFORMATION.API_ALS_NO(+) "
				+ "AND ALS_SESSION_TRANS.AII_ITEM_TXN_IND = ALS_ITEM_INFORMATION.AII_ITEM_TXN_IND(+) "
				+ "AND ALS_SESSION_TRANS.AII_SEQ_NO = ALS_ITEM_INFORMATION.AII_SEQ_NO(+) "
				+ "AND ALS_SESSION_TRANS.AAI_APP_IDENTIFICATION_NO = ALS_APPLICATION_INFORMATION.AAI_APP_IDENTIFICATION_NO(+) "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.API_PROVIDER_NO = ALS_SABHRS_ENTRIES.API_PROVIDER_NO(+) "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.APR_BILLING_FROM = ALS_SABHRS_ENTRIES.APR_BILLING_FROM(+) "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.APR_BILLING_TO = ALS_SABHRS_ENTRIES.APR_BILLING_TO(+) "
				+ "AND ALS_ITEM_APPL_FEE_ACCT.AIAFA_SEQ_NO = ALS_SABHRS_ENTRIES.AIAFA_SEQ_NO(+) "
				+ "AND ALS_SABHRS_ENTRIES.ATG_TRANSACTION_CD = ALS_TRANSACTION_GRP_STATUS.ATG_TRANSACTION_CD(+) "
				+ "AND ALS_SABHRS_ENTRIES.ATGS_GROUP_IDENTIFIER = ALS_TRANSACTION_GRP_STATUS.ATGS_GROUP_IDENTIFIER(+) "
				+ "AND NVL(ALS.ALS_SABHRS_ENTRIES.ATGS_GROUP_IDENTIFIER,0) NOT LIKE 'DRWGRSLT%' ");
		if(fromDt != null && toDt != null){
			search = true;
		}
		if(issProvNo != null && !"".equals(issProvNo)){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.API_PROVIDER_NO = "+issProvNo+" ");
			search = true;
		}
		if(entProvNo != null && !"".equals(entProvNo)){
			srchStr.append("AND ALS_SESSIONS.AS_DATA_ENTRY_PROVIDER_NO = "+entProvNo+" ");
			search = true;
		}
		if(bpFromDt != null){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.APR_BILLING_FROM	= TO_DATE('"+bpFromDt+"','YYYY-MM-DD') ");
			search = true;
		}
		if(bpToDt != null){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.APR_BILLING_TO = TO_DATE('"+bpToDt+"','YYYY-MM-DD') ");
			search = true;
		}
		if(upFromDt != null){
			srchStr.append("AND NVL(ALS_SESSION_TRANS.AICT_USAGE_PERIOD_FROM,SYSDATE) = TO_DATE('"+upFromDt+"','YYYY-MM-DD') ");
			search = true;
		}
		if(upToDt != null){
			srchStr.append("AND NVL(ALS_SESSION_TRANS.AICT_USAGE_PERIOD_TO,SYSDATE)  = TO_DATE('"+upToDt+"','YYYY-MM-DD') ");
			search = true;
		}
		if(modeOfPayment != null && !"".equals(modeOfPayment)){
			srchStr.append("AND ALS_SESSIONS.AS_MODE_PAYMENT = '"+modeOfPayment+"' ");
			search = true;
		}
		if(chckNo != null){
			srchStr.append("AND ALS_SESSIONS.AS_CHECK_NO = '"+chckNo+"' ");
			search = true;
		}
		if(chckWriter != null && !"".equals(chckWriter)){
			srchStr.append("AND ALS_SESSIONS.AS_CHECK_WRITER = '"+chckWriter+"' ");
			search = true;
		}
		if(remarks != null && !"".equals(remarks)){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.AIAFA_REMARKS = '"+remarks+"' ");
			search = true;
		}
		if(iafaSeqNo != null){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.AIAFA_SEQ_NO = "+iafaSeqNo+" ");
			search = true;
		}
		if(dob != null){
			srchStr.append("AND NVL(ALS_SESSION_TRANS.API_DOB,SYSDATE) = TO_DATE('"+dob+"','YYYY-MM-DD') ");
			search = true;
		}
		if(alsNo != null){
			srchStr.append("AND ALS_SESSION_TRANS.API_ALS_NO = "+alsNo+" ");
			search = true;
		}
		if(transGrpIdentifier != null && !"".equals(transGrpIdentifier)){
			srchStr.append("AND ALS_TRANSACTION_GRP_STATUS.ATGS_GROUP_IDENTIFIER = '"+transGrpIdentifier+"' ");
			search = true;
		}
		if(sumAppStat != null && !"".equals(sumAppStat)){
			srchStr.append("AND ALS_TRANSACTION_GRP_STATUS.ATGS_SUMMARY_STATUS = "+sumAppStat+" ");
			search = true;
		}
		if(intAppStat != null && !"".equals(intAppStat)){
			srchStr.append("AND ALS_TRANSACTION_GRP_STATUS.ATGS_INTERFACE_STATUS = "+intAppStat+" ");
			search = true;
		}
		if(ahmType != null && !"".equals(ahmType)){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.AHM_TYPE = '"+ahmType+"' ");
			search = true;
		}
		if(ahmCd != null && !"".equals(ahmCd)){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.AHM_CD = "+ahmCd+" ");
			search = true;
		}
		if(recordVoidDt != null){
			srchStr.append("ALS_ITEM_APPL_FEE_ACCT.AIAFA_RECORD_VOID_DT = TO_DATE('"+recordVoidDt+"','YYYY-MM-DD') ");
			search = true;
		}
		if(tribeCd != null && !"".equals(tribeCd)){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.ATI_TRIBE_CD = "+tribeCd+" ");
			search = true;
		}
		if(appType != null && !"".equals(appType)){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.AIAFA_APP_TYPE = '"+appType+"' ");
			search = true;
		}
		if(amountTypeCd != null && !"".equals(amountTypeCd)){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.AIAFA_AMT_TYPE = '"+amountTypeCd+"' ");
			search = true;
		}
		if(amount != null){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.AIAFA_AMT = "+amount+" ");
			search = true;
		}
		if(sessDt != null){
			srchStr.append("AND TRUNC(NVL(ALS_ITEM_APPL_FEE_ACCT.AS_SESSION_DT,SYSDATE)) = TO_DATE('"+sessDt+"','YYYY-MM-DD') ");
			search = true;
		}
		if(sessVoidDt != null){
			srchStr.append("AND TRUNC(NVL(ALS_SESSIONS.AS_SESSION_VOID_DT,SYSDATE)) = TO_DATE('"+sessVoidDt+"','YYYY-MM-DD') ");
			search = true;
		}
		if(sessStat != null && !"".equals(sessStat)){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.AIAFA_STATUS	= '"+sessStat+"' ");
			search = true;
		}
		if(reasonCd != null && !"".equals(reasonCd)){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.AIAFA_REASON_CD = "+reasonCd+" ");
			search = true;
		}
		if(itemTypeCd != null && !"".equals(itemTypeCd)){
			srchStr.append("AND ALS_SESSION_TRANS.AICT_ITEM_TYPE_CD	= "+itemTypeCd+" ");
			search = true;
		}
		if(itemCatCd != null && !"".equals(itemCatCd)){
			srchStr.append("AND SUBSTR(ALS_SESSION_TRANS.AICT_ITEM_TYPE_CD,1,4)	= "+itemCatCd+" ");
			search = true;
		}
		if(bonusPoints != null && !"".equals(bonusPoints)){
			srchStr.append("AND ALS_APPLICATION_INFORMATION.AAI_BONUS_POINTS_IND = "+bonusPoints+" ");
			search = true;
		}
		if(itemInd != null && !"".equals(itemInd)){
			srchStr.append("AND ALS_ITEM_INFORMATION.AIIN_ITEM_IND_CD = "+itemInd+" ");
			search = true;
		}
		if(itemStat != null && !"".equals(itemStat)){
			srchStr.append("AND ALS_ITEM_INFORMATION.AIS_ITEM_STATUS_CD = "+itemStat+" ");
			search = true;
		}
		if(costPrereqCd != null && !"".equals(costPrereqCd)){
			srchStr.append("AND (ALS_ITEM_INFORMATION.APC_PREREQUISITE_COST_CD = "+itemStat+" OR ALS_APPLICATION_INFORMATION.APC_PREREQUISITE_COST_CD = "+itemStat+") ");
			search = true;
		}
		if(resIndicator != null && !"".equals(resIndicator)){
			srchStr.append("AND (ALS_ITEM_INFORMATION.AII_RESIDENCY_STATUS = "+itemStat+" OR ALS_APPLICATION_INFORMATION.AAI_ITEM_RESIDENCY_IND = "+itemStat+") ");
			search = true;
		}
		if(appDis != null && !"".equals(appDis)){
			srchStr.append("AND ALS_APPLICATION_INFORMATION.AAI_DISPOSITION_CD = "+appDis+" ");
			search = true;
		}
		if(procCatCd != null && !"".equals(procCatCd)){
			srchStr.append("AND ALS_ITEM_APPL_FEE_ACCT.AIAFA_PROCESS_CATEGORY_CD = "+procCatCd+" ");
			search = true;
		}
		if(procTypeCd != null && !"".equals(procTypeCd)){
			srchStr.append("AND ALS_SESSION_TRANS.AST_PROCESS_TYPE_CD = "+procTypeCd+" ");
			search = true;
		}
		if(batchRecDt != null){
			srchStr.append("AND ALS_BATCH_INFO.ABI_RECONCILED_ON = TO_DATE('"+batchRecDt+"','YYYY-MM-DD') ");
			search = true;
		}
		if(noCharge != null && !"".equals(noCharge)){
			srchStr.append("AND Decode(NVL(ALS_SESSION_TRANS.AST_NOCHARGE_REASON,'N'),'N','N','Y') = Decode('"+noCharge+"','N','N','Y') ");
			search = true;
		}
		if(itemTransInd != null && !"".equals(itemTransInd)){
			srchStr.append("AND ALS_SESSION_TRANS.AII_ITEM_TXN_IND = '"+itemTransInd+"' ");
			search = true;
		}
		if(seqNoInItemTrans != null){
			srchStr.append("AND ALS_SESSION_TRANS.AII_SEQ_NO = '"+seqNoInItemTrans+"' ");
			search = true;
		}
		if(alxInd != null){
			if(alxInd == true){
				srchStr.append("AND ALS_PROVIDER_INFO.API_ALX_PROV_IND = 'Y' ");
				search = true;
			}
		}
		if(nullTGI != null && !"".equals(nullTGI)){
			srchStr.append("AND ("+nullTGI+" <> 'Y' OR ("+nullTGI+" = 'Y' AND NOT EXISTS (SELECT 1 "
																								+ "FROM als.als_transaction_grp_status atgs, "
																								+ "als.als_sabhrs_entries ase "
																								+ "WHERE ase.api_provider_no = als_item_appl_fee_acct.api_provider_no "
																								+ "AND ase.apr_billing_from = als_item_appl_fee_acct.apr_billing_from "
																								+ "AND ase.apr_billing_to = als_item_appl_fee_acct.apr_billing_to "
																								+ "AND ase.aiafa_seq_no = als_item_appl_fee_acct.aiafa_seq_no "
																								+ "AND atgs.atg_transaction_cd = ase.atg_transaction_cd "
																								+ "AND atgs.atgs_group_identifier = ase.atgs_group_identifier) ");
			search = true;
		}
		/*ORDER*/
    	srchStr.append("ORDER BY als_item_appl_fee_acct.aiafa_seq_no, als_item_appl_fee_acct.as_session_dt");
		return srchStr.toString();
	}

	public String getJSON()
	{
		return buildgrid();
	}

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public boolean isLoadonce() {
        return loadonce;
    }

    public void setLoadonce(boolean loadonce) {
        this.loadonce = loadonce;
    }

	public List<IafaQueryDTO> getModel() {
		return model;
	}

	public void setModel(ArrayList<IafaQueryDTO> arrayList) {
		this.model = arrayList;
	}

	public Integer getIssProvNo() {
		return issProvNo;
	}

	public void setIssProvNo(Integer issProvNo) {
		this.issProvNo = issProvNo;
	}

	public Date getFromDt() {
		return fromDt;
	}

	public void setFromDt(Date fromDt) {
		this.fromDt = fromDt;
	}

	public Date getToDt() {
		return toDt;
	}

	public void setToDt(Date toDt) {
		this.toDt = toDt;
	}

	public Boolean getAlxInd() {
		return alxInd;
	}

	public void setAlxInd(Boolean alxInd) {
		this.alxInd = alxInd;
	}

	public Integer getEntProvNo() {
		return entProvNo;
	}

	public void setEntProvNo(Integer entProvNo) {
		this.entProvNo = entProvNo;
	}

	public Date getBpFromDt() {
		return bpFromDt;
	}

	public void setBpFromDt(Date bpFromDt) {
		this.bpFromDt = bpFromDt;
	}

	public Date getBpToDt() {
		return bpToDt;
	}

	public void setBpToDt(Date bpToDt) {
		this.bpToDt = bpToDt;
	}

	public Integer getIafaSeqNo() {
		return iafaSeqNo;
	}

	public void setIafaSeqNo(Integer iafaSeqNo) {
		this.iafaSeqNo = iafaSeqNo;
	}

	public String getSessStat() {
		return sessStat;
	}

	public void setSessStat(String sessStat) {
		this.sessStat = sessStat;
	}

	public Date getUpFromDt() {
		return upFromDt;
	}

	public void setUpFromDt(Date upFromDt) {
		this.upFromDt = upFromDt;
	}

	public Date getUpToDt() {
		return upToDt;
	}

	public void setUpToDt(Date upToDt) {
		this.upToDt = upToDt;
	}

	public String getTribeCd() {
		return tribeCd;
	}

	public void setTribeCd(String tribeCd) {
		this.tribeCd = tribeCd;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getItemTypeCd() {
		return itemTypeCd;
	}

	public void setItemTypeCd(String itemTypeCd) {
		this.itemTypeCd = itemTypeCd;
	}

	public String getNoCharge() {
		return noCharge;
	}

	public void setNoCharge(String noCharge) {
		this.noCharge = noCharge;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getItemInd() {
		return itemInd;
	}

	public void setItemInd(String itemInd) {
		this.itemInd = itemInd;
	}

	public String getItemStat() {
		return itemStat;
	}

	public void setItemStat(String itemStat) {
		this.itemStat = itemStat;
	}

	public String getItemCatCd() {
		return itemCatCd;
	}

	public void setItemCatCd(String itemCatCd) {
		this.itemCatCd = itemCatCd;
	}

	public String getAppDis() {
		return appDis;
	}

	public void setAppDis(String appDis) {
		this.appDis = appDis;
	}

	public String getItemTransInd() {
		return itemTransInd;
	}

	public void setItemTransInd(String itemTransInd) {
		this.itemTransInd = itemTransInd;
	}

	public Integer getSeqNoInItemTrans() {
		return seqNoInItemTrans;
	}

	public void setSeqNoInItemTrans(Integer seqNoInItemTrans) {
		this.seqNoInItemTrans = seqNoInItemTrans;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Integer getAlsNo() {
		return alsNo;
	}

	public void setAlsNo(Integer alsNo) {
		this.alsNo = alsNo;
	}

	public String getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(String bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public String getAmountTypeCd() {
		return amountTypeCd;
	}

	public void setAmountTypeCd(String amountTypeCd) {
		this.amountTypeCd = amountTypeCd;
	}

	public String getCostPrereqCd() {
		return costPrereqCd;
	}

	public void setCostPrereqCd(String costPrereqCd) {
		this.costPrereqCd = costPrereqCd;
	}

	public String getProcCatCd() {
		return procCatCd;
	}

	public void setProcCatCd(String procCatCd) {
		this.procCatCd = procCatCd;
	}

	public String getSessOrigin() {
		return sessOrigin;
	}

	public void setSessOrigin(String sessOrigin) {
		this.sessOrigin = sessOrigin;
	}

	public String getProcTypeCd() {
		return procTypeCd;
	}

	public void setProcTypeCd(String procTypeCd) {
		this.procTypeCd = procTypeCd;
	}

	public Date getBatchRecDt() {
		return batchRecDt;
	}

	public void setBatchRecDt(Date batchRecDt) {
		this.batchRecDt = batchRecDt;
	}

	public String getReasonCd() {
		return reasonCd;
	}

	public void setReasonCd(String reasonCd) {
		this.reasonCd = reasonCd;
	}

	public String getTransGrpIdentifier() {
		return transGrpIdentifier;
	}

	public void setTransGrpIdentifier(String transGrpIdentifier) {
		this.transGrpIdentifier = transGrpIdentifier;
	}

	public String getNullTGI() {
		return nullTGI;
	}

	public void setNullTGI(String nullTGI) {
		this.nullTGI = nullTGI;
	}

	public String getAhmType() {
		return ahmType;
	}

	public void setAhmType(String ahmType) {
		this.ahmType = ahmType;
	}

	public String getAhmCd() {
		return ahmCd;
	}

	public void setAhmCd(String ahmCd) {
		this.ahmCd = ahmCd;
	}

	public Date getSessDt() {
		return sessDt;
	}

	public void setSessDt(Date sessDt) {
		this.sessDt = sessDt;
	}

	public Date getSessVoidDt() {
		return sessVoidDt;
	}

	public void setSessVoidDt(Date sessVoidDt) {
		this.sessVoidDt = sessVoidDt;
	}

	public Date getRecordVoidDt() {
		return recordVoidDt;
	}

	public void setRecordVoidDt(Date recordVoidDt) {
		this.recordVoidDt = recordVoidDt;
	}

	public String getSumAppStat() {
		return sumAppStat;
	}

	public void setSumAppStat(String sumAppStat) {
		this.sumAppStat = sumAppStat;
	}

	public String getIntAppStat() {
		return intAppStat;
	}

	public void setIntAppStat(String intAppStat) {
		this.intAppStat = intAppStat;
	}

	public String getOtherTransGrp() {
		return otherTransGrp;
	}

	public void setOtherTransGrp(String otherTransGrp) {
		this.otherTransGrp = otherTransGrp;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public Integer getChckNo() {
		return chckNo;
	}

	public void setChckNo(Integer chckNo) {
		this.chckNo = chckNo;
	}

	public String getChckWriter() {
		return chckWriter;
	}

	public void setChckWriter(String chckWriter) {
		this.chckWriter = chckWriter;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean getSearch() {
		return search;
	}

	public void setSearch(Boolean search) {
		this.search = search;
	}
	
	public String getResIndicator() {
		return resIndicator;
	}

	public void setResIndicator(String resIndicator) {
		this.resIndicator = resIndicator;
	}

	/**
	 * @return the userdata
	 */
	public String getUserdata() {
		return userdata;
	}

	/**
	 * @param userdata the userdata to set
	 */
	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}
    
	
}
