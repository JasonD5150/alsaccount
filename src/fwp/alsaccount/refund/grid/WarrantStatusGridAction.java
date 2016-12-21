package fwp.alsaccount.refund.grid;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.refund.AlsWarrantStatusAS;
import fwp.alsaccount.dao.refund.AlsWarrantStatus;


public class WarrantStatusGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(WarrantStatusGridAction.class);

    private List<AlsWarrantStatus>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    
    private Date 	downloadDt;
    private Date 	apiDob;
    private Integer apiAlsNo;

    private String 				userdata;

	public String buildgrid(){ 
		AlsWarrantStatusAS awsAS = new AlsWarrantStatusAS();
    	setModel(new ArrayList<AlsWarrantStatus>());
    	if(validateFields()){
    		model = awsAS.getPersonRefundWarrantStatusRecords(downloadDt, apiDob, apiAlsNo);
    	}
        setRows(model.size());
        setRecords(model.size());
        setTotal(1);

	    return SUCCESS;
    }

	private Boolean validateFields(){
		Boolean rtn = true;
		
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

	public List<AlsWarrantStatus> getModel() {
		return model;
	}

	public void setModel(List<AlsWarrantStatus> model) {
		this.model = model;
	}

	public String getUserdata() {
		return userdata;
	}

	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}

	public Date getDownloadDt() {
		return downloadDt;
	}

	public void setDownloadDt(Date downloadDt) {
		this.downloadDt = downloadDt;
	}

	public Date getApiDob() {
		return apiDob;
	}

	public void setApiDob(Date apiDob) {
		this.apiDob = apiDob;
	}

	public Integer getApiAlsNo() {
		return apiAlsNo;
	}

	public void setApiAlsNo(Integer apiAlsNo) {
		this.apiAlsNo = apiAlsNo;
	}

	
}
