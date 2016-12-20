package fwp.alsaccount.sabhrs.grid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

import fwp.als.appservice.inventory.AlsProviderRemittanceAS;
import fwp.als.hibernate.inventory.dao.AlsProviderRemittance;
import fwp.als.hibernate.inventory.dao.AlsProviderRemittanceIdPk;
import fwp.alsaccount.appservice.admin.AlsBankCodeAS;
import fwp.alsaccount.appservice.sabhrs.AlsProviderBankDepositSlipAS;
import fwp.alsaccount.appservice.sabhrs.AlsProviderBankDetailsAS;
import fwp.alsaccount.dao.admin.AlsBankCode;
import fwp.alsaccount.dao.sabhrs.AlsProviderBankDepositSlip;
import fwp.alsaccount.dao.sabhrs.AlsProviderBankDetails;
import fwp.alsaccount.dto.sabhrs.InternalProviderBankCdDepLinkDTO;

public class InternalProviderBankCdDepLinkGridAction extends ActionSupport{
    private static final long   serialVersionUID = 5078264277068533593L;
    private static final Logger    log              = LoggerFactory.getLogger(InternalProviderBankCdDepLinkGridAction.class);

    private List<InternalProviderBankCdDepLinkDTO>    model;
    private Integer             rows             = 0;
    private Integer             page             = 0;
    private Integer             total            = 0;
    private Integer             records           = 0;
    private String              sord;
    private String              sidx;
    private String              filters;
    private boolean             loadonce         = false;
    
	private String 				bpFrom;
	private String				bpTo;
    private String				provNo;
    
    private Boolean 			search = false;


    public String buildgrid(){ 
		AlsProviderBankDetailsAS apbdAS = new AlsProviderBankDetailsAS();
		List<AlsProviderBankDetails> apbdLst = new ArrayList<AlsProviderBankDetails>();

		InternalProviderBankCdDepLinkDTO tmp;
		
		AlsBankCodeAS absAS = new AlsBankCodeAS();
		AlsBankCode abc;
		
		AlsProviderRemittanceAS aprAS  = new AlsProviderRemittanceAS();
		AlsProviderRemittanceIdPk aprIdPk = new AlsProviderRemittanceIdPk();
		AlsProviderRemittance apr = new AlsProviderRemittance();
		
		String srchStr = buildQueryStr();

        try{
        	setModel(new ArrayList<InternalProviderBankCdDepLinkDTO>());
        	if (search) {
        		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        		apbdLst = apbdAS.findAllByWhere(srchStr);
        		for(AlsProviderBankDetails apbd : apbdLst){
        			tmp = new InternalProviderBankCdDepLinkDTO();
        			tmp.setGridKey(sdf.format(apbd.getIdPk().getApbdBillingTo())+"_"+apbd.getIdPk().getApbdSeqNo()+"_"+apbd.getIdPk().getApiProviderNo());
        			tmp.setIdPk(apbd.getIdPk());
        			abc = absAS.findById(apbd.getAbcBankCd());
        			if(abc != null){
        				tmp.setBankName(abc.getAbcBankNm());
        			}
  
        			aprIdPk = new AlsProviderRemittanceIdPk();
        			aprIdPk.setApiProviderNo(apbd.getIdPk().getApiProviderNo());
        			aprIdPk.setAprBillingFrom(apbd.getApbdBillingFrom());
        			aprIdPk.setAprBillingTo(apbd.getIdPk().getApbdBillingTo());
        			apr = aprAS.findById(aprIdPk);
        			if(apr != null){
        				tmp.setDeadlineDate(apr.getAprEftDepositDeadlineDt());
        				tmp.setAmtDue(apr.getAprAmtDue());
        			}
        			tmp.setAbcBankCd(apbd.getAbcBankCd());
        			tmp.setApbdAmountDeposit(apbd.getApbdAmountDeposit());
        			tmp.setDepositDate(apbd.getApbdDepositDate());
        			tmp.setBillingFrom(apbd.getApbdBillingFrom());
        			tmp.setApbdBillingTo(apbd.getIdPk().getApbdBillingTo());
        			tmp.setApbdDepositId(apbd.getApbdDepositId());
        			tmp.setApbdCashInd(apbd.getApbdCashInd());
        			
        			AlsProviderBankDepositSlipAS apbdsAS = new AlsProviderBankDepositSlipAS();
        			List<AlsProviderBankDepositSlip> apbdsLst = apbdsAS.findByApbdId(apbd.getIdPk().getApiProviderNo(), apbd.getIdPk().getApbdBillingTo(), apbd.getIdPk().getApbdSeqNo());
        			if(!apbdsLst.isEmpty()){
        				tmp.setHasDepositSlip(true);
        			}
        			model.add(tmp);
        		}
    		}
        }
        catch (HibernateException re) {
        	//System.out.println(re.toString());
            log.debug("AlsProviderBankDetails did not load " + re.getMessage());
        }
        setRows(model.size());
        setRecords(model.size());

        setTotal(1);

	    return SUCCESS;
    }
	
	private String buildQueryStr(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		StringBuilder srchStr = new StringBuilder();
		srchStr.append("WHERE 1=1 ");
		if(provNo != null && !"".equals(provNo)){
			srchStr.append("AND idPk.apiProviderNo ="+provNo+" ");
			search = true;
		}
		if(bpFrom != null){
			srchStr.append("AND apbdBillingFrom = TO_DATE('"+bpFrom+"','mm/dd/yyyy') ");
			search = true;
		}
		if(bpTo != null){
			srchStr.append("AND idPk.apbdBillingTo = TO_DATE('"+bpTo+"','mm/dd/yyyy') ");
			search = true;
		}
		srchStr.append("ORDER BY apbdDepositDate DESC, abcBankCd ASC");
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

	public List<InternalProviderBankCdDepLinkDTO> getModel() {
		return model;
	}

	public void setModel(List<InternalProviderBankCdDepLinkDTO> model) {
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
