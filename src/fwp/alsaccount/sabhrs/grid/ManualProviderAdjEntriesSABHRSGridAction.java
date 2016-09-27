package fwp.alsaccount.sabhrs.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesAS;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;

public class ManualProviderAdjEntriesSABHRSGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(ManualProviderAdjEntriesSABHRSGridAction.class);

    private List<AlsSabhrsEntriesDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    
    private Integer 			provNo;
    private Integer 			iafaSeqNo;
    private Date				bpFrom;
    private Date				bpTo;


	@SuppressWarnings("unchecked")
	public String buildgrid(){ 		
    	AlsSabhrsEntriesAS aseAS = new AlsSabhrsEntriesAS();
    	List<AlsSabhrsEntries> aseLst;
    	
        try{
        	aseLst = aseAS.getManualProviderAdjEntriesRecords(provNo, iafaSeqNo, bpFrom, bpTo);
        	
			setModel(new ArrayList<AlsSabhrsEntriesDTO>());
			AlsSabhrsEntriesDTO tmp;

        	for(AlsSabhrsEntries ase : aseLst){
				tmp = new AlsSabhrsEntriesDTO();

				tmp.setGridKey(ase.getIdPk().getAseWhenEntryPosted()+"_"+ase.getIdPk().getAseSeqNo()+"_"+ase.getIdPk().getAseDrCrCd()+"_"+ase.getIdPk().getAseTxnCdSeqNo());
				tmp.setIdPk(ase.getIdPk());
				tmp.setAsacBudgetYear(ase.getAsacBudgetYear());
				tmp.setAsacReference(ase.getAsacReference());
				tmp.setAamAccount(ase.getAamAccount());
				tmp.setAamFund(ase.getAamFund());
				tmp.setAocOrg(ase.getAocOrg());
				tmp.setAsacProgram(ase.getAsacProgram());
				tmp.setAsacSubclass(ase.getAsacSubclass());
				tmp.setAamBusinessUnit(ase.getAamBusinessUnit());
				tmp.setAsacProjectGrant(ase.getAsacProjectGrant());
				tmp.setAseAmt(ase.getAseAmt());
				tmp.setAsacSystemActivityTypeCd(ase.getAsacSystemActivityTypeCd());
				tmp.setAsacTxnCd(ase.getAsacTxnCd());
				tmp.setAseLineDescription(ase.getAseLineDescription());
				
				model.add(tmp);
			}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsSabhrsEntries did not load " + re.getMessage());
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

	public List<AlsSabhrsEntriesDTO> getModel() {
		return model;
	}

	public void setModel(List<AlsSabhrsEntriesDTO> model) {
		this.model = model;
	}

	public Integer getProvNo() {
		return provNo;
	}

	public void setProvNo(Integer provNo) {
		this.provNo = provNo;
	}

	public Integer getIafaSeqNo() {
		return iafaSeqNo;
	}

	public void setIafaSeqNo(Integer iafaSeqNo) {
		this.iafaSeqNo = iafaSeqNo;
	}

	public Date getBpTo() {
		return bpTo;
	}

	public void setBpTo(Date bpTo) {
		this.bpTo = bpTo;
	}

	public Date getBpFrom() {
		return bpFrom;
	}

	public void setBpFrom(Date bpFrom) {
		this.bpFrom = bpFrom;
	}
	
	
}
