package fwp.alsaccount.refund.json;

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
import fwp.alsaccount.dto.refund.AlsRefundInfoDTO;
/**
 * Action handler for the SABHRS Query search page to CSV export file.
 *
 * @author cfa027
 */
public class PersonRefundGridToCsvAction extends ActionSupport {

	private static final long serialVersionUID = -198737835399515405L;

	private List<AlsRefundInfoDTO> refundRecords = new ArrayList<>();
	private List<ListComp> columnNameValues = new ArrayList<>();
	private String filters;

	private String csvFileName;
	private String fileName;

	public String execute() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		File tempFile = File.createTempFile("personrefundrecords", "csv");
		fileName="PersonRefund.csv";
		StringBuilder titleLine = new StringBuilder();

	
		for (ListComp listComp : this.columnNameValues) {
			if (validColumn(listComp)) {
				if (titleLine.length() > 0) {
					titleLine.append(",");
				}
				titleLine.append(StringEscapeUtils.escapeCsv(listComp.getItemLabel()));
			}
		}
		titleLine.append("\n");
		FileWriter fileWriter = new FileWriter(tempFile);
		fileWriter.write("Person Refund Report,\n\n");
		fileWriter.write("Report Created =  "+ sdf.format(new Date())+"\n");
		if(!"".equals(filters)&& filters != null){
			String[] criteria = URLDecoder.decode(filters,"UTF-8").split("&");
			for(String tmp : criteria){
				if(!tmp.contains("_widget")&&!tmp.contains("qryType")&&!tmp.contains("sumOnly")){
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
			}
			fileWriter.write("\n");
		}
		fileWriter.write(titleLine.toString());
		
		for (AlsRefundInfoDTO ie : refundRecords) {			
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
					if (gotten != null && gotten instanceof Date) {
						line.append(StringEscapeUtils.escapeCsv(DateFormatUtils.format((Date) gotten, "MM/dd/yyyy")));
					} else if(gotten != null && gotten instanceof Double){
						line.append(StringEscapeUtils.escapeCsv("$"+gotten.toString()));
					}else {
						line.append(StringEscapeUtils.escapeCsv(ObjectUtils.toString(gotten)));
					}
				}
			}
			line.append("\n");
			fileWriter.write(line.toString());
		}
		fileWriter.close();
		csvFileName = tempFile.getName();
		
		return SUCCESS;
	}
	
	private Boolean validColumn(ListComp listComp) {
		try {
			AlsRefundInfoDTO.class.getMethod("get" + StringUtils.capitalize(listComp.getItemVal()));
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}
	
	private String getColumnLabel(String column){
		switch(column){
		case "dob":
			return "Date of Birth ";
		case "alsNo":
			return "ALS No ";	
		case "upFrom":
			return "Usage Period From ";		
		case "upTo":
			return "Usage Period To ";	
		case "itemTypeCd":
			return "Item Type Code ";		
		case "reasonCd":
			return "Refund Reason ";	
		case "itemIndCd":
			return "Item Indicator ";
		case "warCreateDt":
			return "Warrant Creation Date ";
		case "itemFeeRefApp":
			return "Item Fee Refund Approve ";
		case "appFeeRefApp":
			return "Application Fee Refund Approve ";
		case "prefFeeRefApp":
			return "Preference Fee Refund Approve ";
		case "noWarrant":
			return "No Warrant ";
		case "srchAll":
			return "Search All Dates ";
		case "minRefund":
			return "Minimum Refund ";
		case "hasComments":
			return "Has Comments ";
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
	
	public List<AlsRefundInfoDTO> getRefundRecords() {
		return refundRecords;
	}

	public void setRefundRecords(List<AlsRefundInfoDTO> refundRecords) {
		this.refundRecords = refundRecords;
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
}

