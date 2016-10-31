package fwp.alsaccount.sabhrs.json;

import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.als.appservice.inventory.AlsNonAlsDetailsAS;
import fwp.als.hibernate.inventory.dao.AlsNonAlsDetails;
import fwp.alsaccount.appservice.sabhrs.AlsOverUnderSalesDetsAS;
import fwp.alsaccount.dao.sabhrs.AlsOverUnderSalesDets;
import fwp.alsaccount.dto.sabhrs.AlsInternalRemittanceDTO;
import fwp.alsaccount.utils.Utils;

/**
 * Action handler for the SABHRS Query search page to CSV export file.
 *
 * @author cfa027
 */
public class InternalRemittanceReportAction extends ActionSupport {

	private static final long serialVersionUID = -198737835399515405L;

	private List<AlsInternalRemittanceDTO> remittanceRecords = new ArrayList<>();
	private List<ListComp> columnNameValues = new ArrayList<>();
	private String filters;
	private String selectedRow;

	private String csvFileName;
	private String fileName;

	public String execute() throws Exception {
		buildcsv();
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	private void buildcsv() throws Exception {
		File tempFile = File.createTempFile("remittancerecords", "csv");
		fileName="InternalProviderRemittance.csv";

		FileWriter fileWriter = new FileWriter(tempFile);
		fileWriter.write("Internal Provider Remittance Report\n\n");
		
		fileWriter.write("Search Criteria\n");
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
			fileWriter.write("\n\n");
		}
		
		AlsNonAlsDetailsAS anadAS = new AlsNonAlsDetailsAS();
		List<AlsNonAlsDetails> anadLst = null;
		AlsOverUnderSalesDetsAS aousdAS = new AlsOverUnderSalesDetsAS();
		List<AlsOverUnderSalesDets> aousdLst = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
		for (AlsInternalRemittanceDTO rr : remittanceRecords) {	
			Boolean print = false;
			String where = "WHERE idPk.apiProviderNo = "+rr.getGridKey().split("_")[2]+" "
					+ "AND idPk.airBillingFrom = TO_DATE('"+rr.getGridKey().split("_")[0]+"','dd/mm/yyyy') "
					+ "AND idPk.airBillingTo = TO_DATE('"+rr.getGridKey().split("_")[1]+"','dd/mm/yyyy') ";
			
			if(selectedRow == null && "".equals(selectedRow)){
				print = true;
			}else if(selectedRow.equals(rr.getGridKey())){
				print = true;
			}else{
				print = false;
			}
			
			if(print){
				StringBuilder line = new StringBuilder();
				Date bpFrom = sdf.parse(rr.getGridKey().split("_")[0]);
				Date bpTo = sdf.parse(rr.getGridKey().split("_")[1]);
				sdf.applyPattern("mm/dd/yyyy");
				line.append(rr.getProvNm()+"\n");
				line.append("Provider No:,"+rr.getGridKey().split("_")[2]+"\n");
				line.append("Billing Period From:, "+sdf.format(bpFrom)+"\n");
				line.append("Billing Period To:, "+sdf.format(bpTo)+"\n\n");
				line.append(",Amount, Total\n");
				line.append("System Sales:,$"+rr.getAirSystemSales()+"\n");
				line.append("OTC Sales:,$"+rr.getAirOtcPhoneSales()+"\n");
				line.append("PAEs:,$"+rr.getAirPae()+"\n");
				line.append("Total ALS Sales:,,$"+rr.getAmtDue()+"\n");
				
				/*NON ALS SALES DETAILS*/
				anadLst = new ArrayList<AlsNonAlsDetails>();
				anadLst = anadAS.findAllByWhere(where);
				if(!anadLst.isEmpty()){
					line.append("\nNon ALS Sales Details\n");
					//line.append("Description\n");
					for(AlsNonAlsDetails anadTmp : anadLst){
						line.append(StringEscapeUtils.escapeCsv(anadTmp.getAnadDesc())+",$"+anadTmp.getAnadAmount()+"\n");
					}
				}
				
				line.append("Total Non ALS Sales:,,$"+Utils.nullFix(rr.getAirNonAlsSales())+"\n");
				line.append("Total Sales:,,$"+Utils.nullFix(rr.getAirTotSales())+"\n");
				line.append("Total Bank Deposits:,$"+Utils.nullFix(rr.getTotBankDep())+"\n");
				line.append("Credit Card Sales:,$"+Utils.nullFix(rr.getAirCreditSales())+"\n");
				line.append("Total Funds Received:,,$"+Utils.nullFix(rr.getTotFundsRec())+"\n");
				line.append("Difference:,,$"+Utils.nullFix(rr.getAirDifference())+"\n");
				
				/*TOTAL FUNDS RECEIVED SHORT OF SALES*/
				aousdLst = new ArrayList<AlsOverUnderSalesDets>();
				aousdLst = aousdAS.findAllByWhere(where);
				if(!aousdLst.isEmpty()){
					line.append("\nTotal Funds Received Short of Sales -\n");
					line.append("Details\n");
					for(AlsOverUnderSalesDets aousdTmp : aousdLst){
						line.append(StringEscapeUtils.escapeCsv(aousdTmp.getAousdDesc())+",$"+aousdTmp.getAousdAmount()+"\n");
					}
					line.append("Net Over/Short of Sales:,,$"+rr.getNetOverShortOfSales());
				}
				line.append("\n\n");
				fileWriter.write(line.toString());
			}
			
		}
		
		fileWriter.close();
		csvFileName = tempFile.getName();
	}
	
	private String getColumnLabel(String column){
		switch(column){
		case "provNo":
			return "Provider No ";
		case "bpFrom":
			return "Billing Period From ";	
		case "bpTo":
			return "Billing Period To ";
		case "comByProv":
			return "Completed By Provider ";
		case "comByProvDt":
			return "Completed By Provider Date ";
		case "app":
			return "Remittance Approved ";
		case "appBy":
			return "Remittance Approved By ";
		case "appDt":
			return "Remittance Approved Date ";
		case "appCom":
			return "Comments ";
		case "hasNonAlsDetails":
			return "Has Non ALS Details ";
		case "hasOverShortDetails":
			return "Has Over/Short Details ";
		case "hasPaeAmt":
			return "Has PAE Amount ";
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

	public List<AlsInternalRemittanceDTO> getRemittanceRecords() {
		return remittanceRecords;
	}

	public void setRemittanceRecords(
			List<AlsInternalRemittanceDTO> remittanceRecords) {
		this.remittanceRecords = remittanceRecords;
	}

	public void setColumnNameValues(List<ListComp> columnNameValues) {
		this.columnNameValues = columnNameValues;
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

	public String getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(String selectedRow) {
		this.selectedRow = selectedRow;
	}


	
}

