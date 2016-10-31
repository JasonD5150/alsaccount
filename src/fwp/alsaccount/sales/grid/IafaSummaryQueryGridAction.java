package fwp.alsaccount.sales.grid;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.sales.IafaSummaryDTO;
import fwp.alsaccount.utils.HibHelpers;


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
            try{
            	List<IafaSummaryDTO> iafaLst = new ArrayList<IafaSummaryDTO>();
            	iafaLst = hh.getIafaSummaryRecords(fromDt, toDt, issProvNo, upFromDt, upToDt, itemTypeCd, procCatCd, appType, procTypeCd, amountTypeCd);
            	for(IafaSummaryDTO isd : iafaLst){
            		isd.setGridKey(isd.getProvNo()+isd.getItemTypeCd()+isd.getAmtType()+isd.getResStatus()+isd.getUpFromDt());
            		isd.setAmtTypeDesc(hh.getAmKey2("IAFA_AMOUNT_TYPE", isd.getAmtType().toString()));
            		isd.setProcessTypeDesc(hh.getAmKey2("PROCESS TYPE", isd.getProcessTypeCd().toString()));
            		isd.setProcCatDesc(hh.getAmKey2("PROCESS_CATEGORY", isd.getProcCatCd().toString()));
            		model.add(isd);
            	}
            }
            catch (HibernateException re) {
            	//System.out.println(re.toString());
                log.debug("IafaSummaryQueryGrid did not load " + re.getMessage());
            }
    	}
        setRows(model.size());
        setRecords(model.size());
        setTotal(1);

	    return SUCCESS;
    }

	private Boolean validateFields(){
		Boolean rtn = true;
		if(fromDt == null && toDt == null && upFromDt == null && upToDt == null){
			userdata = "A date field must be quiried on.";
			rtn = false;
		}else if(fromDt != null && toDt == null){
			userdata = "To Date must be entered.";
			rtn = false;
		}else if(toDt != null && fromDt == null){
			userdata = "From Date must be entered.";
			rtn = false;
		}else if(fromDt != null && toDt != null){
			if(fromDt.after(toDt)){
				userdata = "To Date should be greater than From Date.";
				rtn = false;
			}
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
		/*if(qryType == 1){
			if(sumOnly != true && issProvNo == null){
			userdata = "Provider Number is required for producing detail reports (Summary Only is not checked).";
			rtn = false;
			}
		}*/
		return rtn;
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
