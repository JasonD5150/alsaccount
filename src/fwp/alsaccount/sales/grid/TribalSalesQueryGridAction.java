package fwp.alsaccount.sales.grid;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.sales.TribalSalesDTO;
import fwp.alsaccount.utils.HibHelpers;


public class TribalSalesQueryGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(TribalSalesQueryGridAction.class);

    private List<TribalSalesDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    
    private String tribeCd;
    private Integer provNo;
    private Integer itemTypeCd;
    private String usagePeriod;
    private Date fromDt;
    private Date toDt;
    private Date bpFromDt;
    private Date bpToDt;
    
    private String 				userdata;
 
    
	public String buildgrid(){ 
    	HibHelpers hh = new HibHelpers();
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    	setModel(new ArrayList<TribalSalesDTO>());
    	if(validateFields()){	
            try{
            	List<TribalSalesDTO> tsdLst = new ArrayList<TribalSalesDTO>();
            	java.sql.Date upFromDt = null;
            	java.sql.Date upToDt = null;
            	if(usagePeriod != null && !"".equals(usagePeriod)){
            		upFromDt = new java.sql.Date(sdf.parse(usagePeriod.split("_")[0]).getTime());
            		upToDt = new java.sql.Date(sdf.parse(usagePeriod.split("_")[1]).getTime());
            	}
            	tsdLst = hh.getTribalSalesRecords(tribeCd, provNo, itemTypeCd, upFromDt, upToDt, fromDt, toDt, bpFromDt, bpToDt);
            	for(TribalSalesDTO tsd : tsdLst){
            		tsd.setGridKey(tsd.getProvNm()+tsd.getItemTypeCd());
            		model.add(tsd);
            	}
            }
            catch (HibernateException re) {
            	//System.out.println(re.toString());
                log.debug("TribalSalesQueryGrid did not load " + re.getMessage());
            } catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
        setRows(model.size());
        setRecords(model.size());
        setTotal(1);

	    return SUCCESS;
    }

	private Boolean validateFields(){
		Boolean rtn = true;
		if((usagePeriod == null || "".equals(usagePeriod)) && fromDt == null && toDt == null && bpFromDt == null && bpToDt == null){
			userdata = "A date must be queried on.";
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

	public List<TribalSalesDTO> getModel() {
		return model;
	}

	public void setModel(List<TribalSalesDTO> model) {
		this.model = model;
	}

	public String getUserdata() {
		return userdata;
	}

	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}

	public String getTribeCd() {
		return tribeCd;
	}

	public void setTribeCd(String tribeCd) {
		this.tribeCd = tribeCd;
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

	public String getUsagePeriod() {
		return usagePeriod;
	}

	public void setUsagePeriod(String usagePeriod) {
		this.usagePeriod = usagePeriod;
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
    
}
