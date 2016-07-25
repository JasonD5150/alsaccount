package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.dto.sabhrs.AlsTransactionGrpMassCopyDTO;
import fwp.alsaccount.utils.HibHelpers;

public class AlsTransactionGrpMassApprovalGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsTransactionGrpMassApprovalGridAction.class);

    private List<AlsTransactionGrpMassCopyDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    private Date				bpe;
    private Date 				opa;

	public String buildgrid(){  
		HibHelpers hh = new HibHelpers();
    	
        try{        	
        	setModel(new ArrayList<AlsTransactionGrpMassCopyDTO>());
        	model = hh.getTransGroupMassApprovalRecords(bpe, opa);
        	for(AlsTransactionGrpMassCopyDTO tmp : model){
        		tmp.setGridKey(tmp.getProviderNo()+"_"+tmp.getBpe());
        	}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsTransactionGroup did not load " + re.getMessage());
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

	public Date getBpe() {
		return bpe;
	}

	public void setBpe(Date bpe) {
		this.bpe = bpe;
	}

	public Date getOpa() {
		return opa;
	}

	public void setOpa(Date opa) {
		this.opa = opa;
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

	public List<AlsTransactionGrpMassCopyDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsTransactionGrpMassCopyDTO> model) {
		this.model = model;
	}

}
