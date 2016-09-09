package fwp.alsaccount.admin.grid;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;




import fwp.als.hibernate.item.dao.AlsItemAccountTable;
import fwp.alsaccount.appservice.item.AlsItemAccountTableAS;
import fwp.alsaccount.dto.admin.AccCdDistByItemTypeDTO;
import fwp.alsaccount.utils.HibHelpers;

public class AccCdDistByItemTypeGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AccCdDistByItemTypeGridAction.class);

    private List<AccCdDistByItemTypeDTO>    acdbitModel;
	private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    private Integer budgYear;
    private Date upFromDt;
    private Date upToDt;
    private String itemTypeCd;
    private Integer accCd;

	public String buildgrid(){   
		HibHelpers hh = new HibHelpers();
        try{
        	setAcdbitModel(new ArrayList<AccCdDistByItemTypeDTO>());

        	if(itemTypeCd == null || "".equals(itemTypeCd)){
        		AlsItemAccountTableAS aiatAS = new AlsItemAccountTableAS();
        		List<AlsItemAccountTable> aiatLst = new ArrayList<AlsItemAccountTable>();
        	
        		aiatLst = aiatAS.findAllByUsagePeriodFromToBudgPeriod(upFromDt, upToDt, budgYear);
        		if(!aiatLst.isEmpty()){
        			for(AlsItemAccountTable aiat : aiatLst){
        				if(itemTypeCd == null || "".equals(itemTypeCd)){
        					itemTypeCd = "('"+aiat.getIdPk().getAictItemTypeCd()+"'";
        				}else{
        					if(!itemTypeCd.contains(aiat.getIdPk().getAictItemTypeCd())){
        						itemTypeCd += ",'"+aiat.getIdPk().getAictItemTypeCd()+"'";
            				}
        				}
        			}
        			itemTypeCd += ")";
        		}
        	}
        	setAcdbitModel(hh.getAccCdDistByItemTypeRecords(upFromDt, upToDt, budgYear, accCd, itemTypeCd));
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AccCdDistByItemTypeGrid did not load " + re.getMessage());
        }
        setRows(acdbitModel.size());
        setRecords(acdbitModel.size());

        setTotal(1);

	    return SUCCESS;
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


    public List<AccCdDistByItemTypeDTO> getAcdbitModel() {
		return acdbitModel;
	}

	public void setAcdbitModel(List<AccCdDistByItemTypeDTO> acdbitModel) {
		this.acdbitModel = acdbitModel;
	}

	public Integer getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
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

	public String getItemTypeCd() {
		return itemTypeCd;
	}

	public void setItemTypeCd(String itemTypeCd) {
		this.itemTypeCd = itemTypeCd;
	}

	public Integer getAccCd() {
		return accCd;
	}

	public void setAccCd(Integer accCd) {
		this.accCd = accCd;
	}

}
