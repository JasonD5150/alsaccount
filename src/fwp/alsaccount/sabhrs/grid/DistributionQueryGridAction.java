package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;
import fwp.alsaccount.dto.sabhrs.DistributionQueryDTO;
import fwp.alsaccount.utils.HibHelpers;

public class DistributionQueryGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(DistributionQueryGridAction.class);

    private List<DistributionQueryDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    
    private Integer 			provNo;
    private Integer 			itemTypeCd;
    private Date 				fromDt;
    private Date 				toDt;
  
    private String 				userdata;

    
	public String buildgrid(){ 
    	HibHelpers hh = new HibHelpers();
        try{
        	model = hh.getDistributionQueryRecords(provNo, itemTypeCd, fromDt, toDt);
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("DistributionQueryGrid did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());
        setTotal(1);

	    return SUCCESS;
    }

	public String getJSON(){
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

	public List<DistributionQueryDTO> getModel() {
		return model;
	}

	public void setModel(List<DistributionQueryDTO> model) {
		this.model = model;
	}
	
	public String getUserdata() {
		return userdata;
	}

	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}

	public Integer getProvNo() {
		return provNo;
	}

	public void setProvNo(Integer provNo) {
		this.provNo = provNo;
	}

	public Integer getItemTypeCd() {
		return itemTypeCd;
	}

	public void setItemTypeCd(Integer itemTypeCd) {
		this.itemTypeCd = itemTypeCd;
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
	
	
}
