package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.sabhrs.IafaSummaryDTO;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.Utils;
import fwp.utils.FwpNumberUtils;


public class IafaSummaryQueryGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(IafaSummaryQueryGridAction.class);

    private List<IafaSummaryDTO>    model;
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
    private Date				upFromDt;
    private Date 				upToDt;
    private String 				appType;
    private String 				itemTypeCd;
    private String 				amountTypeCd;
    private String 				procCatCd;
	private String				procTypeCd;
    private String 				reasonCd;
    private Boolean 			sumOnly;

	private Boolean 			search = false;
    private String 				userdata;
    private Integer 			qryType;
    
	public String buildgrid(){ 
    	HibHelpers hh = new HibHelpers();
    	setModel(new ArrayList<IafaSummaryDTO>());
    	if(validateFields()){	
    		if(qryType == 1){
        		String where = buildQueryStr();
            	
            	List<IafaSummaryDTO> iafaLst = new ArrayList<IafaSummaryDTO>();
            	IafaSummaryDTO iafa = null;
                try{
                	iafaLst = hh.getIafaSummaryByItemTypeRecords(where);
                	Integer curItemType = null;
                	String curItemDesc = "";
                	String curProcType = "";
                	Integer cnt = 0;
                	Double amt = 0.0;
                	for(IafaSummaryDTO isd : iafaLst){
                		if(curItemType == null || !curItemType.equals(isd.getItemTypeCd())){
                			if(curItemType != null&&!curItemType.equals(isd.getItemTypeCd())){
                				iafa = new IafaSummaryDTO();
                				iafa.setItemTypeCd(curItemType);
                				iafa.setItemTypeDesc(curItemDesc);
                				iafa.setProcessTypeCd(curProcType);
                				iafa.setCount(cnt);
                				iafa.setAmount(amt);
                				model.add(iafa);
                			}
                			curItemType = isd.getItemTypeCd();
                			curItemDesc = isd.getItemTypeDesc();
                			curProcType = isd.getProcessTypeCd();
                			cnt = 1;
                        	amt = isd.getAmount();
                		}else{
                			cnt += 1;
                			amt += isd.getAmount();
                		}
                		if(iafaLst.indexOf(isd) == (iafaLst.size()-1)){
                			iafa = new IafaSummaryDTO();
            				iafa.setItemTypeCd(curItemType);
            				iafa.setItemTypeDesc(curItemDesc);
            				iafa.setProcessTypeCd(curProcType);
            				iafa.setCount(cnt);
            				iafa.setAmount(amt);
            				model.add(iafa);
                		}
                	}
                }
                catch (HibernateException re) {
                	//System.out.println(re.toString());
                    log.debug("AlsSabhrsEntries did not load " + re.getMessage());
                }
    		}else{
        		String where = buildQueryStr();
            	
            	List<IafaSummaryDTO> iafaLst = new ArrayList<IafaSummaryDTO>();
            	IafaSummaryDTO iafa = null;
                try{
                	iafaLst = hh.getIafaSummaryByAmountTypeRecords(where);
                	
                	Integer curItemTypeCd = null;
                	String curPrereqCostDesc = null;
                	
                	Double tAmt = 0.0;
                	
                	
                	iafa = new IafaSummaryDTO();
                	for(IafaSummaryDTO isd : iafaLst){
                		if((curItemTypeCd == null||!curItemTypeCd.equals(isd.getItemTypeCd()))||(curPrereqCostDesc == null || !curPrereqCostDesc.equals(isd.getPrereqCostDesc()))){
                			if(curItemTypeCd != null&&!curItemTypeCd.equals(isd.getItemTypeCd())){
                				iafa.setTotalCount(FwpNumberUtils.nullFix(iafa.getNonResCount())+FwpNumberUtils.nullFix(iafa.getResCount()));
                				iafa.setTotalAmount(Utils.nullFix(iafa.getNonResAmount())+Utils.nullFix(iafa.getResAmount()));
                				model.add(iafa);
                				iafa = new IafaSummaryDTO();
                			}else if(curPrereqCostDesc != null && !curPrereqCostDesc.equals(isd.getPrereqCostDesc())){
                				iafa.setTotalCount(FwpNumberUtils.nullFix(iafa.getNonResCount())+FwpNumberUtils.nullFix(iafa.getResCount()));
                				iafa.setTotalAmount(Utils.nullFix(iafa.getNonResAmount())+Utils.nullFix(iafa.getResAmount()));
                				model.add(iafa);
                				iafa = new IafaSummaryDTO();
                			}
                			
                			curItemTypeCd = isd.getItemTypeCd();
                			curPrereqCostDesc = isd.getPrereqCostDesc();
                			iafa.setProvNo(isd.getProvNo());
                			iafa.setProvNm(isd.getProvNm());
                			iafa.setBpFromDt(isd.getBpFromDt());
                			iafa.setBpToDt(isd.getBpToDt());
                			iafa.setProcCatCd(isd.getProcCatCd());
                			iafa.setProcCatDesc(isd.getProcCatDesc());
                			iafa.setDeviceNo(isd.getDeviceNo());
                			iafa.setAmtType(isd.getAmtType());
                			iafa.setItemTypeCd(isd.getItemTypeCd());
                			iafa.setItemTypeDesc(isd.getItemTypeDesc());
                			iafa.setUpFromDt(isd.getUpFromDt());
                			iafa.setUpToDt(isd.getUpToDt());
                			iafa.setResStatus(isd.getResStatus());
                			iafa.setProcessTypeCd(isd.getProcessTypeCd());
                			
                        	if("R".equals(isd.getResStatus())){
                        		iafa.setResCostGrpDesc(isd.getCostGrpDesc());
                        		iafa.setResPrereqCostDesc(isd.getPrereqCostDesc());
                        		iafa.setResCount(isd.getCount());
                        		iafa.setResAmount(isd.getAmount());
                        	}else{
                        		iafa.setNonResCostGrpDesc(isd.getCostGrpDesc());
                    			iafa.setNonResPrereqCostDesc(isd.getPrereqCostDesc());
                    			iafa.setNonResCount(isd.getCount());
                    			iafa.setNonResAmount(isd.getAmount());
                        	}
                		}else{
                			if("R".equals(isd.getResStatus())){
                        		iafa.setResCostGrpDesc(isd.getCostGrpDesc());
                        		iafa.setResPrereqCostDesc(isd.getPrereqCostDesc());
                        		iafa.setResCount(isd.getCount());
                        		iafa.setResAmount(isd.getAmount());
                        	}else{
                        		iafa.setNonResCostGrpDesc(isd.getCostGrpDesc());
                    			iafa.setNonResPrereqCostDesc(isd.getPrereqCostDesc());
                    			iafa.setNonResCount(isd.getCount());
                    			iafa.setNonResAmount(isd.getAmount());
                        	}
                		}
                		if(iafaLst.indexOf(isd) == (iafaLst.size()-1)){
                			iafa.setTotalCount(FwpNumberUtils.nullFix(iafa.getNonResCount())+FwpNumberUtils.nullFix(iafa.getResCount()));
            				iafa.setTotalAmount(Utils.nullFix(iafa.getNonResAmount())+Utils.nullFix(iafa.getResAmount()));
            				model.add(iafa);
                		}
                	}
                }
                catch (HibernateException re) {
                	//System.out.println(re.toString());
                    log.debug("AlsSabhrsEntries did not load " + re.getMessage());
                }
        		
        	}
    	}
        setRows(model.size());
        setRecords(model.size());

        setTotal(1);

	    return SUCCESS;
    }

	private Boolean validateFields(){
		Boolean rtn = true;
		if(fromDt == null){
			userdata = "From Date must be entered.";
			rtn = false;
		}else if(toDt == null){
			userdata = "To Date must be entered.";
			rtn = false;
		}else if(fromDt.after(toDt)){
			userdata = "To Date should be greater than From Date.";
			rtn = false;
		}else if(upFromDt != null && upToDt == null){
			userdata = "Usage Period To must be entered.";
			rtn = false;
		}else if(upToDt != null && upFromDt == null){
			userdata = "Usage Period From must be entered.";
			rtn = false;
		}else if(upFromDt != null && upToDt != null){
			if(upFromDt.after(upToDt)){
				userdata = "Usage Period To should be greater than Usage Period From.";
				rtn = false;
			}
		}
		if(qryType == 1){
			if(sumOnly != true && issProvNo == null){
			userdata = "Provider Number is required for producing detail reports (Summary Only is not checked).";
			rtn = false;
			}
		}
		return rtn;
	}
	
	private String buildQueryStr(){
		StringBuilder srchStr = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		if(qryType == 1){
			if(fromDt != null&&toDt != null){
				srchStr.append("WHERE TRUNC(DECODE(als.als_drawing_item(als_session_trans.aict_usage_period_from, als_session_trans.aict_usage_period_to, als_session_trans.aict_item_type_cd), 'Y', NVL(als_session_trans.ast_issue_dt, als_session_trans.as_session_dt), DECODE(als_session_trans.ast_process_type_cd, als.als_package.get_pval('PROCESS TYPE', 'OFFLINE ISSUE'), NVL(als_session_trans.ast_issue_dt, als_session_trans.as_session_dt), als_session_trans.as_session_dt))) BETWEEN TO_DATE('"+sdf.format(fromDt)+"','MM/DD/YYYY') AND TO_DATE('"+sdf.format(toDt)+"','MM/DD/YYYY')");
			}
			if(issProvNo != null && !"".equals(issProvNo)){
				srchStr.append("AND Als_Item_Appl_Fee_Acct.API_PROVIDER_NO ="+issProvNo+" ");
			}
			if(upFromDt != null&&upToDt != null){
				srchStr.append("AND Als_Session_Trans.Aict_Usage_Period_From = To_Date('"+sdf.format(upFromDt)+"','MM/DD/YYYY') "
	                    	  +"AND Als_Session_Trans.Aict_Usage_Period_To = To_Date('"+sdf.format(upToDt)+"','MM/DD/YYYY') ");
			}
			if(itemTypeCd != null&&!"".equals(itemTypeCd)){
				srchStr.append("AND Als_Session_Trans.AICT_ITEM_TYPE_CD = "+itemTypeCd+" ");
			}
			if(procCatCd != null&&!"".equals(procCatCd)){
				srchStr.append("AND Als_Item_Appl_Fee_Acct.Aiafa_Process_Category_Cd = "+procCatCd+" ");
			}
			if(appType != null&&!"".equals(appType)){
				srchStr.append("AND Als_Item_Appl_Fee_Acct.aiafa_app_type LIKE '"+appType+"%' ");
			}
			if(procTypeCd != null&&!"".equals(procTypeCd)){
				srchStr.append("AND ALS_SESSION_TRANS.AST_PROCESS_TYPE_CD = '"+procTypeCd+"%' ");
			}
		}else{
			if(fromDt != null&&toDt != null){
				srchStr.append("Where Als_Item_Appl_Fee_Acct.AIAFA_When_Log >= To_Date('"+sdf.format(fromDt)+"','MM/DD/YYYY') "
							 + "AND Als_Item_Appl_Fee_Acct.AIAFA_When_Log < To_Date('"+sdf.format(Utils.addDays(toDt, 1))+"','MM/DD/YYYY') ");
			}
			if(issProvNo != null && !"".equals(issProvNo)){
				srchStr.append("AND Als_Item_Appl_Fee_Acct.API_PROVIDER_NO = "+issProvNo+" ");
			}
			if(upFromDt != null&&upToDt != null){
				srchStr.append("And Als_Session_Trans.Aict_Usage_Period_From = To_Date('"+sdf.format(upFromDt)+"','MM/DD/YYYY') "
							  +"And Als_Session_Trans.Aict_Usage_Period_To = To_Date('"+sdf.format(upToDt)+"','MM/DD/YYYY') ");
			}
			if(itemTypeCd != null&&!"".equals(itemTypeCd)){
				srchStr.append("AND Als_Session_Trans.AICT_ITEM_TYPE_CD = "+itemTypeCd+" ");
			}
			if(procCatCd != null&&!"".equals(procCatCd)){
				srchStr.append("AND Als_Item_Appl_Fee_Acct.Aiafa_Process_Category_Cd = "+procCatCd+" ");
			}
			if(amountTypeCd != null&&!"".equals(amountTypeCd)){
				srchStr.append("AND Als_Item_Appl_Fee_Acct.AIAFA_AMT_TYPE = "+amountTypeCd+" ");
			}
			if(reasonCd != null&&!"".equals(reasonCd)){
				srchStr.append("AND And Als_Item_Appl_Fee_Acct.AIAFA_REASON_CD = "+reasonCd+" ");
			}
			
		}

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

	public List<IafaSummaryDTO> getModel() {
		return model;
	}

	public void setModel(List<IafaSummaryDTO> model) {
		this.model = model;
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

	public String getAmountTypeCd() {
		return amountTypeCd;
	}

	public void setAmountTypeCd(String amountTypeCd) {
		this.amountTypeCd = amountTypeCd;
	}

	public String getProcCatCd() {
		return procCatCd;
	}

	public void setProcCatCd(String procCatCd) {
		this.procCatCd = procCatCd;
	}

	public String getProcTypeCd() {
		return procTypeCd;
	}

	public void setProcTypeCd(String procTypeCd) {
		this.procTypeCd = procTypeCd;
	}

	public String getReasonCd() {
		return reasonCd;
	}

	public void setReasonCd(String reasonCd) {
		this.reasonCd = reasonCd;
	}

	public Boolean getSearch() {
		return search;
	}

	public void setSearch(Boolean search) {
		this.search = search;
	}
	
	public String getUserdata() {
		return userdata;
	}

	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}
	
	public Boolean getSumOnly() {
		return sumOnly;
	}

	public void setSumOnly(Boolean sumOnly) {
		this.sumOnly = sumOnly;
	}

	/**
	 * @return the qryType
	 */
	public Integer getQryType() {
		return qryType;
	}

	/**
	 * @param qryType the qryType to set
	 */
	public void setQryType(Integer qryType) {
		this.qryType = qryType;
	}
    
	
}
