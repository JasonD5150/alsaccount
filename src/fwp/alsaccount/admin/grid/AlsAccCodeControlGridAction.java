package fwp.alsaccount.admin.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.admin.AlsAccCdControlAS;
import fwp.alsaccount.dao.admin.AlsAccCdControl;
import fwp.alsaccount.dto.admin.AlsAccCdControlDTO;
import fwp.alsaccount.utils.Utils;

public class AlsAccCodeControlGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsAccCodeControlGridAction.class);

    private List<AlsAccCdControlDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    private Integer budgYear;
    private Integer accCd;

	@SuppressWarnings("unchecked")
	public String buildgrid(){    	
    	AlsAccCdControlAS aaccAS = new AlsAccCdControlAS();
    	List<AlsAccCdControl> aacc;
    	
        try{
        	aacc = aaccAS.findAllByBudgYearAccCd(budgYear, accCd);
        	
			setModel(new ArrayList<AlsAccCdControlDTO>());
			AlsAccCdControlDTO tmp;
        	for(AlsAccCdControl aa : aacc){
				tmp = new AlsAccCdControlDTO();
				tmp.setGridKey(aa.getIdPk().getAsacBudgetYear()+"_"+aa.getIdPk().getAaccAccCd()+"_"+aa.getIdPk().getAaccSeqNo());
				tmp.setIdPk(aa.getIdPk());
				tmp.setAaccAllocatedAmt(aa.getAaccAllocatedAmt());
				tmp.setAaccBalancingAmtFlag(aa.getAaccBalancingAmtFlag());
				tmp.setAaccFund(aa.getAaccFund());
				tmp.setAaccJlrRequired(aa.getAaccJlrRequired());
				tmp.setAaccOrgFlag(aa.getAaccOrgFlag());
				tmp.setAaccRemarks(aa.getAaccRemarks());
				tmp.setAamAccount(aa.getAamAccount());
				tmp.setAocOrg(aa.getAocOrg());
				tmp.setAsacSubclass(aa.getAsacSubclass());
				model.add(tmp);
			}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsAccCodeControl did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());

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

	public List<AlsAccCdControlDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsAccCdControlDTO> model) {
		this.model = model;
	}
    
    public Integer getBudgYear() {
		return budgYear;
	}

	public void setBudgYear(Integer budgYear) {
		this.budgYear = budgYear;
	}

	public Integer getAccCd() {
		return accCd;
	}

	public void setAccCd(Integer accCd) {
		this.accCd = accCd;
	}

}

