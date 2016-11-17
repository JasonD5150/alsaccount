package fwp.alsaccount.sabhrs.grid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.appservice.inventory.AlsNonAlsDetailsAS;
import fwp.als.hibernate.inventory.dao.AlsNonAlsDetails;
import fwp.alsaccount.dto.sabhrs.AlsNonAlsDetailsDTO;

public class AlsNonAlsDetailsGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsNonAlsDetailsGridAction.class);

    private List<AlsNonAlsDetailsDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;

    private String				provNo;
    private String 				bpFrom;
    private String 				bpTo;    
    private Boolean				search = false;
    

	public String buildgrid(){ 
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String srchStr = buildQueryStr();
        try{
        	setModel(new ArrayList<AlsNonAlsDetailsDTO>());  
        	if(search){
        		  
        		AlsNonAlsDetailsAS anadAS = new AlsNonAlsDetailsAS();
        		List<AlsNonAlsDetails> anadLst = new ArrayList<AlsNonAlsDetails>();
        		anadLst = anadAS.findAllByWhere(srchStr);
        		
        		AlsNonAlsDetailsDTO tmp = null;
        		for(AlsNonAlsDetails anad : anadLst){
        			tmp = new AlsNonAlsDetailsDTO();
        			tmp.setGridKey(anad.getIdPk().getApiProviderNo()+"_"+sdf.format(anad.getIdPk().getAirBillingFrom())+"_"+sdf.format(anad.getIdPk().getAirBillingTo())+"_"+anad.getIdPk().getAnadSeqNo());
        			tmp.setAnatCd(anad.getAnatCd());
        			tmp.setAnadDesc(anad.getAnadDesc());
        			tmp.setAnadAmount(anad.getAnadAmount());
        			model.add(tmp);
        		}
        		
        	}
        }
        catch (HibernateException re) {
            log.debug("AlsNonAlsDetails did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());

        setTotal(1);

	    return SUCCESS;
    }
	
	private String buildQueryStr(){
		StringBuilder srchStr = new StringBuilder();
		srchStr.append("WHERE 1=1 ");
		if(provNo != null && !"".equals(provNo)){
			srchStr.append("AND idPk.apiProviderNo = "+provNo+" ");
			search = true;
		}
		if(bpFrom != null){
			srchStr.append("AND idPk.airBillingFrom = TO_DATE('"+bpFrom+"','mm/dd/yyyy') ");
			search = true;
		}
		if(bpTo != null){
			srchStr.append("AND idPk.airBillingTo = TO_DATE('"+bpTo+"','mm/dd/yyyy') ");
			search = true;
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

	public List<AlsNonAlsDetailsDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsNonAlsDetailsDTO> model) {
		this.model = model;
	}

	public String getBpFrom() {
		return bpFrom;
	}

	public void setBpFrom(String bpFrom) {
		this.bpFrom = bpFrom;
	}

	public String getBpTo() {
		return bpTo;
	}

	public void setBpTo(String bpTo) {
		this.bpTo = bpTo;
	}

	public String getProvNo() {
		return provNo;
	}

	public void setProvNo(String provNo) {
		this.provNo = provNo;
	}
}
