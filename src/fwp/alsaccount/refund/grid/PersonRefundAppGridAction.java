package fwp.alsaccount.refund.grid;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.refund.AlsRefundInfoAS;
import fwp.alsaccount.dto.refund.AlsRefundInfoDTO;


public class PersonRefundAppGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(PersonRefundAppGridAction.class);

    private List<AlsRefundInfoDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    

	private Integer maxSearchSize = 10000;
    
    private Date 	dob;
    private Integer alsNo;
    private Date 	upFrom;
    private Date 	upTo;
    private Integer itemTypeCd;
    private Integer reasonCd;
    private Integer itemIndCd;
    private Date 	warCreateDt;
    private String 	itemFeeRefApp;
    private String 	appFeeRefApp;
    private String 	prefFeeRefApp;
    private Boolean srchAll;
    private Boolean noWarrant;
    private Boolean minRefund;
    private Boolean hasComments;
    private Integer	appTypeCd;
    
    private String 				userdata;
 
    
	public String buildgrid(){ 
		AlsRefundInfoAS ariAS = new AlsRefundInfoAS();
    	setModel(new ArrayList<AlsRefundInfoDTO>());
    	if(validateFields()){
    		List<AlsRefundInfoDTO> tmpLst = ariAS.getPersonRefundRecords(dob, alsNo, upFrom, upTo, itemTypeCd, reasonCd, 
    																		itemIndCd, appTypeCd, warCreateDt, itemFeeRefApp, appFeeRefApp, 
    																		prefFeeRefApp, srchAll, noWarrant, minRefund, hasComments);
    		if(tmpLst.size() > maxSearchSize){
    			userdata = "Search result exceeds result size limit " + maxSearchSize + ".  Please enter additional criteria.";
    		}else{
    			for(AlsRefundInfoDTO tmp : tmpLst){
            		if(tmp.getItemTypeCd() != null){
            			tmp.setAppType(ariAS.getAppType(tmp.getItemTypeCd()));
            		}
            		model.add(tmp);
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
		if(upFrom != null && upTo == null){
			userdata = "Usage Period To is required if Usage Period From is queried on.";
			rtn = false;
		}else if(upTo != null && upFrom == null){
			userdata = "Usage Period From is required if Usage Period To is queried on.";
			rtn = false;
		}else if(upFrom != null && upTo != null && !upTo.after(upFrom)){
			userdata = "Usage Period To must be after Usage Period From.";
			rtn = false;
		}
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

	public List<AlsRefundInfoDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsRefundInfoDTO> model) {
		this.model = model;
	}

	public String getUserdata() {
		return userdata;
	}

	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}

	public Integer getItemTypeCd() {
		return itemTypeCd;
	}

	public void setItemTypeCd(Integer itemTypeCd) {
		this.itemTypeCd = itemTypeCd;
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

	public Date getUpFrom() {
		return upFrom;
	}

	public void setUpFrom(Date upFrom) {
		this.upFrom = upFrom;
	}

	public Date getUpTo() {
		return upTo;
	}

	public void setUpTo(Date upTo) {
		this.upTo = upTo;
	}

	public Integer getReasonCd() {
		return reasonCd;
	}

	public void setReasonCd(Integer reasonCd) {
		this.reasonCd = reasonCd;
	}

	public Integer getItemIndCd() {
		return itemIndCd;
	}

	public void setItemIndCd(Integer itemIndCd) {
		this.itemIndCd = itemIndCd;
	}

	public Date getWarCreateDt() {
		return warCreateDt;
	}

	public void setWarCreateDt(Date warCreateDt) {
		this.warCreateDt = warCreateDt;
	}

	public String getItemFeeRefApp() {
		return itemFeeRefApp;
	}

	public void setItemFeeRefApp(String itemFeeRefApp) {
		this.itemFeeRefApp = itemFeeRefApp;
	}

	public String getAppFeeRefApp() {
		return appFeeRefApp;
	}

	public void setAppFeeRefApp(String appFeeRefApp) {
		this.appFeeRefApp = appFeeRefApp;
	}

	public String getPrefFeeRefApp() {
		return prefFeeRefApp;
	}

	public void setPrefFeeRefApp(String prefFeeRefApp) {
		this.prefFeeRefApp = prefFeeRefApp;
	}

	public Boolean getSrchAll() {
		return srchAll;
	}

	public void setSrchAll(Boolean srchAll) {
		this.srchAll = srchAll;
	}

	public Boolean getNoWarrant() {
		return noWarrant;
	}

	public void setNoWarrant(Boolean noWarrant) {
		this.noWarrant = noWarrant;
	}

	public Boolean getMinRefund() {
		return minRefund;
	}

	public void setMinRefund(Boolean minRefund) {
		this.minRefund = minRefund;
	}

	public Boolean getHasComments() {
		return hasComments;
	}

	public void setHasComments(Boolean hasComments) {
		this.hasComments = hasComments;
	}

	public Integer getAppTypeCd() {
		return appTypeCd;
	}

	public void setAppTypeCd(Integer appTypeCd) {
		this.appTypeCd = appTypeCd;
	}

}
