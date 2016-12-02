package fwp.alsaccount.sabhrs.grid;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.hibernate.item.dao.AlsItemApplFeeAcctIdPk;
import fwp.alsaccount.appservice.sales.AlsItemApplFeeAcctAS;
import fwp.alsaccount.dto.sabhrs.ProvAdjEntAIAFAGridDTO;
import fwp.alsaccount.utils.Utils;

public class ProvAdjEntIAFAGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(ProvAdjEntIAFAGridAction.class);

    private List<ProvAdjEntAIAFAGridDTO>    model;
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
    private Integer 			transCd;
    private String 				groupId;
    private String 				tribeCd;
    private String 				appTypeCd;
    private Integer				amtTypeCd;
    private Integer				reasonCd;


	public String buildgrid(){ 		
    	AlsItemApplFeeAcctAS aiafaAS = new AlsItemApplFeeAcctAS();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    	setModel(new ArrayList<ProvAdjEntAIAFAGridDTO>());
        try{
    		groupId = Utils.createIntProvGroupIdentifier(provNo, sdf.format(bpTo), 000);
    		setModel(aiafaAS.getRemittanceProviderAdjEntriesRecords(groupId.substring(0, groupId.length()-3)+'%', provNo, iafaSeqNo, bpFrom, bpTo));
        }
        catch (HibernateException re) {
            log.debug("ProvAdjEntIAFAGridAction did not load " + re.getMessage());
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
	public Integer getProvNo() {
		return provNo;
	}
	public void setProvNo(Integer provNo) {
		this.provNo = provNo;
	}

	public Date getBpFrom() {
		return bpFrom;
	}

	public void setBpFrom(Date bpFrom) {
		this.bpFrom = bpFrom;
	}

	public Date getBpTo() {
		return bpTo;
	}

	public void setBpTo(Date bpTo) {
		this.bpTo = bpTo;
	}

	public String getTribeCd() {
		return tribeCd;
	}
	public void setTribeCd(String tribeCd) {
		this.tribeCd = tribeCd;
	}
	public String getAppTypeCd() {
		return appTypeCd;
	}
	public void setAppTypeCd(String appTypeCd) {
		this.appTypeCd = appTypeCd;
	}
	public Integer getAmtTypeCd() {
		return amtTypeCd;
	}
	public void setAmtTypeCd(Integer amtTypeCd) {
		this.amtTypeCd = amtTypeCd;
	}
	public Integer getReasonCd() {
		return reasonCd;
	}
	public void setReasonCd(Integer reasonCd) {
		this.reasonCd = reasonCd;
	}
	public Integer getIafaSeqNo() {
		return iafaSeqNo;
	}
	public void setIafaSeqNo(Integer iafaSeqNo) {
		this.iafaSeqNo = iafaSeqNo;
	}
	public Integer getTransCd() {
		return transCd;
	}
	public void setTransCd(Integer transCd) {
		this.transCd = transCd;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public List<ProvAdjEntAIAFAGridDTO> getModel() {
		return model;
	}
	public void setModel(List<ProvAdjEntAIAFAGridDTO> model) {
		this.model = model;
	}

	
}
