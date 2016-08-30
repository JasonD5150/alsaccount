package fwp.alsaccount.sabhrs.json;

import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.opensymphony.xwork2.ActionSupport;

import fwp.ListComp;
import fwp.alsaccount.dto.sabhrs.IafaDetailsDTO;

/**
 * Action handler for the SABHRS Query search page to CSV export file.
 *
 * @author cfa027
 */
public class IafaQueryBuildCsvAction extends ActionSupport {

	private static final long serialVersionUID = -198737835399515405L;

	private List<IafaDetailsDTO> iafaEntries = new ArrayList<>();
	private List<ListComp> columnNameValues = new ArrayList<>();
	private String filters;

	private String csvFileName;
	private String fileName;

	public String execute() throws Exception {
		buildcsv();
		return SUCCESS;
	}

	private void buildcsv() throws Exception {
		File tempFile = File.createTempFile("iafaentries", "csv");
		fileName="IAFADetail.csv";
		StringBuilder titleLine = new StringBuilder();

		String[] csvHeaders={"Issuing Provider No",
							"Issuing Provider Name",
							"ALX Provider Indicator",
							"Data Entry Provider No",
							"Billing Period From",
							"Billing Period To",
							"IAFA Seq No",
							"Session Status",
							"Usage Period From",
							"Usage Period To",
							"Item Type Code",
							"Item Type Description",
							"Amount",
							"No Charge Reason",
							"Item Indicator",
							"Item Indicator Description",
							"Item Status",
							"Item Status Description",
							"Item Catagory Code",
							"Item Category Description",
							"Application Disposition",
							"Application Disposition Description",
							"Bonus Points",
							"ItemTcnInd",
							"Seq No Within Item Transaction",
							"Date of Birth",
							"ALS No",
							"Amount Type",
							"Amount Type Description",
							"Residency",
							"Cost Group Code",
							"Cost Group Description",
							"Prerequisite Code",
							"Prerequisite Description",
							"Process Category Code",
							"Process Category Description",
							"Process Type Code",
							"Process Type Description",
							"Batch Reconcilation Date",
							"Batch No",
							"Sub-Batch No",
							"Reason Type",
							"Reason Code",
							"Reason",
							"Tribe Code",
							"Transaction Group Identifier",
							"Other Transaction Group",
							"Hardware Type",
							"Hardware Code (Device No)",
							"Session Date",
							"Session Origin",
							"Session Void Date",
							"Record Void Date",
							"Seq No for Printed Item",
							"Session Total",
							"Mode of Payment",
							"Check No",
							"Check Writer",
							"Summary Approval Status",
							"Interface Approval Status",
							"Remarks"};
		for(int i=0;i<csvHeaders.length;i++){
			titleLine.append(csvHeaders[i]+",");
		}
		/*SELECTED COLUMNS
		 * for (ListComp listComp : this.columnNameValues) {
			if (validColumn(listComp)) {
				if (titleLine.length() > 0) {
					titleLine.append(",");
				}
				titleLine.append(StringEscapeUtils.escapeCsv(listComp.getItemLabel()));
			}
		}*/
		titleLine.append("\n");
		FileWriter fileWriter = new FileWriter(tempFile);
		fileWriter.write("IAFA Detail Report\n\n");
		
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
					//System.out.println(column +" = "+value);
				}
			}
			fileWriter.write("\n");
		}
		fileWriter.write(titleLine.toString());
		
		Double amountTotal = 0.0;
		for (IafaDetailsDTO ie : iafaEntries) {			
			StringBuilder line = new StringBuilder();
			Boolean firstColumn = true;
			for (ListComp listComp : this.columnNameValues) {
				if (validColumn(listComp)) {
					if (!firstColumn) {
						line.append(",");
					} else {
						firstColumn = false;
					}
					Object gotten;
					
					gotten = ie.getClass().getMethod("get" + StringUtils.capitalize(listComp.getItemVal())).invoke(ie);
		
					if("aiafaAmt".equals(listComp.getItemVal())){
						amountTotal = amountTotal+Double.valueOf(ObjectUtils.toString(gotten));
					}
					if("atgsSummaryStatus".equals(listComp.getItemVal())||"atgsInterfaceStatus".equals(listComp.getItemVal())){
						String status = ObjectUtils.toString(gotten);
						if("A".equals(status)){
							gotten = "Approved";
						}else if("D".equals(status)){
							gotten =  "Disapproved";
						}else if("N".equals(status)){
							gotten =  "Not Applicable";
						}else{
							gotten = "";
						}
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
		fileWriter.write("Total No of Records,"+iafaEntries.size()+",,,,,,,,,Sum of IAFA Amount ,$"+amountTotal);
		fileWriter.close();
		csvFileName = tempFile.getName();
	}
	private Boolean validColumn(ListComp listComp) {
		try {
			IafaDetailsDTO.class.getMethod("get" + StringUtils.capitalize(listComp.getItemVal()));
			//System.out.println("Found get" + StringUtils.capitalize(listComp.getItemVal()));
			return true;
		} catch (NoSuchMethodException e) {
			//System.out.println("Cannot find get" + StringUtils.capitalize(listComp.getItemVal()));
			return false;
		}
	}
	
	private String getColumnLabel(String column){
		switch(column){
		case "issProvNo":
			return "Issuing Provider No ";
		case "fromDt":
			return "From Date ";	
		case "toDt":
			return "To Date ";	
		case "alxInd":
			return "ALX Indicators Provider/Session ";	
		case "entProvNo":
			return "Data Entry Provider No ";	
		case "bpFromDt":
			return "Billing Period From Date ";	
		case "bpToDt":
			return "Billing Period To Date ";	
		case "iafaSeqNo":
			return "IAFA Seq No ";	
		case "sessStat":
			return "Session Status ";	
		case "upFromDt":
			return "Usage Period From ";	
		case "upToDt":
			return "Usage Period To ";	
		case "tribeCd":
			return "Tribe Code ";	
		case "appType":
			return "Application Type ";	
		case "itemTypeCd": 
			return "Item Type Code ";	
		case "noCharge":
			return "No Charge ";	
		case "amount":
			return "Amount ";
		case "itemInd":
			return "Item Indicator ";
		case "itemStat":
			return "Item Status ";
		case "itemCatCd":
			return "Item Category Code ";
		case "appDis":
			return "Application Disposition ";
		case "itemTransInd":
			return "Item Transaction Indicator ";
		case "seqNoInItemTrans":
			return "Seq No Within Item Transaction ";
		case "dob":
			return "Date of Birth ";
		case "alsNo":
			return "ALS No ";
		case "bonusPoints":
			return "Bonus Points ";
		case "amountTypeCd":
			return "Amount Type Code ";
		case "costPrereqCd":
			return "Cost Prereq Code ";
		case "procCatCd":
			return "Process Category Code ";
		case "sessOrigin":
			return "Session Origin ";
		case "resIndicator":
			return "Residency Indicator ";
		case "procTypeCd":
			return "Process Type Code ";
		case "batchRecDt":
			return "Batch Reconciliation Date ";
		case "reasonCd":
			return "Reason Code ";
		case "transGrpIdentifier":
			return "Transaction Group Identifier ";
		case "nullTGI":
			return "NULL TGI ";
		case "ahmType":
			return "Hardware Type ";
		case "ahmCd":
			return "Harware Code (Device No) ";
		case "sessDt":
			return "Session Date ";
		case "sessVoidDt":
			return "Session Void Date ";
		case "recordVoidDt":
			return "Record Void Date ";
		case "sumAppStat":
			return "Summary Approval Status ";
		case "intAppStat":
			return "Interface Approval Status ";
		case "otherTransGrp":
			return "Other Transaction Group ";
		case "modeOfPayment":
			return "Mode of Payment ";
		case "chckNo":
			return "Check No ";
		case "chckWriter":
			return "Check Writer ";
		case "remarks":
				return "Remarks ";
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

	public List<IafaDetailsDTO> getIafaEntries() {
		return iafaEntries;
	}

	public void setIafaEntries(List<IafaDetailsDTO> iafaEntries) {
		this.iafaEntries = iafaEntries;
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

	/**
	 * @return the filters
	 */
	public String getFilters() {
		return filters;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFilters(String filters) {
		this.filters = filters;
	}
}

