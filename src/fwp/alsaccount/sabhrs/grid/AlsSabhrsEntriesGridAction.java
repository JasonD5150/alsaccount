package fwp.alsaccount.sabhrs.grid;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.alsaccount.appservice.sabhrs.AlsSabhrsEntriesAS;
import fwp.alsaccount.dao.sabhrs.AlsSabhrsEntries;
import fwp.alsaccount.dto.sabhrs.AlsSabhrsEntriesDTO;
import fwp.alsaccount.utils.HibHelpers;
import fwp.alsaccount.utils.Utils;

public class AlsSabhrsEntriesGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(AlsSabhrsEntriesGridAction.class);

    private List<AlsSabhrsEntriesDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;

    private String provNo;
    private String bpFrom;
    private String bpTo;


	public String buildgrid() throws ParseException{ 
		HibHelpers hh = new HibHelpers();
    	AlsSabhrsEntriesAS aseAS = new AlsSabhrsEntriesAS();
    	List<AlsSabhrsEntries> aseLst = new ArrayList<AlsSabhrsEntries>();    	
        try{
        	String transIdentifier = null;
        	if(!Utils.isNil(provNo) && !Utils.isNil(bpTo)){
        		//transIdentifier = Utils.createIntProvGroupIdentifier(Integer.parseInt(provNo), bpTo, 1);
            	transIdentifier = hh.getTransGrpIdMaxSeq(provNo, bpTo.toString());
        	}
        	if(!Utils.isNil(transIdentifier)){
        		aseLst = aseAS.getRemittanceRecords(transIdentifier, 8);
        	}
        	
			setModel(new ArrayList<AlsSabhrsEntriesDTO>());
			AlsSabhrsEntriesDTO tmp;

        	for(AlsSabhrsEntries aa : aseLst){
				tmp = new AlsSabhrsEntriesDTO();

				tmp.setGridKey(aa.getIdPk().getAseWhenEntryPosted()+"_"+aa.getIdPk().getAseSeqNo()+"_"+aa.getIdPk().getAseDrCrCd()+"_"+aa.getIdPk().getAseTxnCdSeqNo());
				tmp.setIdPk(aa.getIdPk());
				tmp.setAsacBudgetYear(aa.getAsacBudgetYear());
				tmp.setAsacReference(aa.getAsacReference());
				tmp.setAamAccount(aa.getAamAccount());
				tmp.setAamFund(aa.getAamFund());
				tmp.setAocOrg(aa.getAocOrg());
				tmp.setAsacProgram(aa.getAsacProgram());
				tmp.setAsacSubclass(aa.getAsacSubclass());
				tmp.setAamBusinessUnit(aa.getAamBusinessUnit());
				tmp.setAsacProjectGrant(aa.getAsacProjectGrant());
				tmp.setAseAmt(aa.getAseAmt());
				tmp.setAsacSystemActivityTypeCd(aa.getAsacSystemActivityTypeCd());
				tmp.setAsacTxnCd(aa.getAsacTxnCd());
				tmp.setAseLineDescription(aa.getAseLineDescription());
				tmp.setJlr(aseAS.getReferenceDesc(aa.getAsacReference()));
				/*if(aa.getAsacReference() == null){
					tmp.setJlr("0");
				}else{
					tmp.setJlr(aseAS.getReferenceDesc(aa.getAsacReference()));
				}*/
				
				
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

	
	public String getJSON() throws ParseException
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
    
	public String getProvNo() {
		return provNo;
	}
	
	public void setProvNo(String provNo) {
		this.provNo = provNo;
	}

	public String getBpTo() {
		return bpTo;
	}

	public void setBpTo(String bpTo) {
		this.bpTo = bpTo;
	}

	public String getBpFrom() {
		return bpFrom;
	}

	public void setBpFrom(String bpFrom) {
		this.bpFrom = bpFrom;
	}
	
}
