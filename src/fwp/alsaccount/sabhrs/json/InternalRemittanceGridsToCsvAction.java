package fwp.alsaccount.sabhrs.json;

import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.als.hibernate.inventory.dao.AlsInternalRemittanceIdPk;
import fwp.als.hibernate.inventory.dao.AlsNonAlsDetails;
import fwp.alsaccount.dao.sabhrs.AlsOverUnderSalesDets;
import fwp.alsaccount.dto.sabhrs.AlsInternalRemittanceDTO;
import fwp.alsaccount.dto.sabhrs.InternalProviderBankCdDepLinkDTO;
import fwp.alsaccount.utils.Utils;

/**
 * Action handler for the SABHRS Query search page to CSV export file.
 *
 * @author cfa027
 */
public class InternalRemittanceGridsToCsvAction extends ActionSupport {

	private static final long serialVersionUID = -198737835399515405L;

	private List<AlsInternalRemittanceDTO> alsInternalRemittanceEntries = new ArrayList<>();
	private List<InternalProviderBankCdDepLinkDTO> depositsEntries = new ArrayList<>();
	private List<AlsNonAlsDetails> alsNonAlsEntries = new ArrayList<>();
	private List<AlsOverUnderSalesDets> alsOverUnderSalesEntries = new ArrayList<>();
	private List<ListComp> columnNameValues = new ArrayList<>();
	private List<ListComp> alsIntRemittanceSelectedColumns = new ArrayList<>();
	

	private String filters;
	private String selRow;
	private String csvFileName;
	private String fileName;

	public String execute() throws Exception {
		buildcsv();
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	private void buildcsv() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		File tempFile = File.createTempFile("remittancerecords", "csv");
		fileName="InternalProviderRemittance.csv";

		FileWriter fileWriter = new FileWriter(tempFile);
		fileWriter.write("Internal Provider Remittance Report\n\n");
		
		if(!"".equals(filters)&& filters != null){
			String[] criteria = URLDecoder.decode(filters,"UTF-8").split("&");
			for(String tmp : criteria){
				Integer length = tmp.trim().split("=").length;
				String column;
				String value;
				if(tmp.split("=")[0].contains("__checkbox_")){
					column = tmp.split("=")[0].replace("__checkbox_", "");
					value = tmp.split("=")[1];
					if(value == "true"){
						fileWriter.write(getColumnLabel(column)+" = "+value+"\n");
					}
				}else if(length > 1){
					column = tmp.split("=")[0];
					value = tmp.split("=")[1];
					fileWriter.write(getColumnLabel(column)+" = "+value+"\n");
				}
			}
			fileWriter.write("\n");
		}
		
		StringBuilder titleLine = new StringBuilder();
		for (ListComp listComp : this.alsIntRemittanceSelectedColumns) {
			if (validColumn(listComp)) {
				if (titleLine.length() > 0) {
					titleLine.append(",");
				}
				titleLine.append(StringEscapeUtils.escapeCsv(listComp.getItemLabel()));
			}
		}
		fileWriter.write(titleLine.toString());
		fileWriter.write("\n");
		
		for (AlsInternalRemittanceDTO air : alsInternalRemittanceEntries) {	
			if(air.getGridKey().equals(selRow)){
				AlsInternalRemittanceIdPk airIdPk = new AlsInternalRemittanceIdPk();
				airIdPk.setApiProviderNo(Integer.parseInt(air.getGridKey().split("_")[2]));
				airIdPk.setAirBillingFrom(Utils.StrToTimestamp(air.getGridKey().split("_")[0], "short"));
				airIdPk.setAirBillingTo(Utils.StrToTimestamp(air.getGridKey().split("_")[0], "short"));
				air.setIdPk(airIdPk);
				StringBuilder line = new StringBuilder();
				Boolean firstColumn = true;
				for (ListComp listComp : this.alsIntRemittanceSelectedColumns) {
					if (validColumn(listComp)) {
						if (!firstColumn) {
							if("amtRec".equals(listComp.getItemVal())){
								line.append(",$");
							}else{
								line.append(",");
							}
						} else {
							firstColumn = false;
						}
						Object gotten;
						
						switch(listComp.getItemVal()){
						case "idPk.apiProviderNo":
							gotten = air.getIdPk().getApiProviderNo();break;
						case "idPk.airBillingFrom":
							gotten = air.getIdPk().getAirBillingFrom();break;
						case "idPk.airBillingTo":
							gotten = air.getIdPk().getAirBillingTo();break;
						default:
							gotten = air.getClass().getMethod("get" + StringUtils.capitalize(listComp.getItemVal())).invoke(air);
						}	
						
						if (gotten != null && gotten instanceof Date) {
							line.append(StringEscapeUtils.escapeCsv(DateFormatUtils.format((Date) gotten, "MM/dd/yyyy")));
						} else {
							line.append(StringEscapeUtils.escapeCsv(ObjectUtils.toString(gotten)));
						}
					}
				}
				line.append("\n");
				fileWriter.write(line.toString());
			}
		}
		fileWriter.write("\n\n");
		
		fileWriter.write("Bank Details\n");
		fileWriter.write("Seq No,Bank Code,Bank Name,Amount Deposited,Deposit Date,Billing Period From,Billing Period To,Amount Due,Deposit Id\n");
		for (InternalProviderBankCdDepLinkDTO ipbcdl : depositsEntries) {	
			StringBuilder line = new StringBuilder();
			String seqNo = ipbcdl.getGridKey().split("_")[1];
			line.append(seqNo+","+ipbcdl.getAbcBankCd()+","+ipbcdl.getBankName()+",$"+
						ipbcdl.getApbdAmountDeposit()+","+sdf.format(ipbcdl.getDepositDate())+","+sdf.format(ipbcdl.getBillingFrom())+","+
						sdf.format(ipbcdl.getApbdBillingTo())+",$"+ipbcdl.getAmtDue()+","+ipbcdl.getApbdDepositId());
			line.append("\n");
			fileWriter.write(line.toString());
		}
		fileWriter.write("\n\n");
		
		fileWriter.write("Non Als Details\n");
		fileWriter.write("Code,Description,Amount\n");
		for (AlsNonAlsDetails anad : alsNonAlsEntries) {	
			StringBuilder line = new StringBuilder();
			line.append(anad.getAnatCd()+","+anad.getAnadDesc()+",$"+anad.getAnadAmount());
			line.append("\n");
			fileWriter.write(line.toString());
		}
		fileWriter.write("\n\n");
		
		fileWriter.write("Total Funds Received Over / Short of Sales - Details\n");
		fileWriter.write("Over/Short of Sales,Description,Amount\n");
		for (AlsOverUnderSalesDets aousd : alsOverUnderSalesEntries) {	
			StringBuilder line = new StringBuilder();
			line.append(Utils.nullFix(aousd.getAousdFlag())+","+aousd.getAousdDesc()+",$"+aousd.getAousdAmount());
			line.append("\n");
			fileWriter.write(line.toString());
		}
		
		fileWriter.close();
		csvFileName = tempFile.getName();
	}
	
	private Boolean validColumn(ListComp listComp) {
		try {
			if(listComp.getItemVal().contains("idPk")){
				AlsInternalRemittanceIdPk.class.getMethod("get" + StringUtils.capitalize(listComp.getItemVal().replace("idPk.", "")));
			}else{
				AlsInternalRemittanceDTO.class.getMethod("get" + StringUtils.capitalize(listComp.getItemVal()));
			}
			
			//System.out.println("Found get" + StringUtils.capitalize(listComp.getItemVal()));
			return true;
		} catch (NoSuchMethodException e) {
			//System.out.println("Cannot find get" + StringUtils.capitalize(listComp.getItemVal()));
			return false;
		}
	}
	
	private String getColumnLabel(String column){
		switch(column){
		case "provNo":
			return "Provider No ";
		case "bpFrom":
			return "Billing Period From ";	
		case "bpTo":
			return "Billing Period To ";	
		default:
			return "N/A";
		}	
	}

	public String getCsvFileName() {
		return csvFileName;
	}

	public void setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
	}

	public List<ListComp> getColumnNameValues() {
		return columnNameValues;
	}

	public List<AlsInternalRemittanceDTO> getAlsInternalRemittanceEntries() {
		return alsInternalRemittanceEntries;
	}

	public void setAlsInternalRemittanceEntries(
			List<AlsInternalRemittanceDTO> alsInternalRemittanceEntries) {
		this.alsInternalRemittanceEntries = alsInternalRemittanceEntries;
	}

	public List<InternalProviderBankCdDepLinkDTO> getDepositsEntries() {
		return depositsEntries;
	}

	public void setDepositsEntries(
			List<InternalProviderBankCdDepLinkDTO> depositsEntries) {
		this.depositsEntries = depositsEntries;
	}

	public List<AlsNonAlsDetails> getAlsNonAlsEntries() {
		return alsNonAlsEntries;
	}

	public void setAlsNonAlsEntries(List<AlsNonAlsDetails> alsNonAlsEntries) {
		this.alsNonAlsEntries = alsNonAlsEntries;
	}

	public List<AlsOverUnderSalesDets> getAlsOverUnderSalesEntries() {
		return alsOverUnderSalesEntries;
	}

	public void setAlsOverUnderSalesEntries(
			List<AlsOverUnderSalesDets> alsOverUnderSalesEntries) {
		this.alsOverUnderSalesEntries = alsOverUnderSalesEntries;
	}

	public void setColumnNameValues(List<ListComp> columnNameValues) {
		this.columnNameValues = columnNameValues;
	}
	
	public List<ListComp> getAlsIntRemittanceSelectedColumns() {
		return alsIntRemittanceSelectedColumns;
	}

	public void setAlsIntRemittanceSelectedColumns(
			List<ListComp> alsIntRemittanceSelectedColumns) {
		this.alsIntRemittanceSelectedColumns = alsIntRemittanceSelectedColumns;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}
	
	public String getSelRow() {
		return selRow;
	}

	public void setSelRow(String selRow) {
		this.selRow = selRow;
	}
}

