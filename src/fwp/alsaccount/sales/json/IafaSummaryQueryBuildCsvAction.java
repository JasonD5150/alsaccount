package fwp.alsaccount.sales.json;

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
import fwp.alsaccount.dto.sales.IafaSummaryDTO;

/**
 * Action handler for the SABHRS Query search page to CSV export file.
 *
 * @author cfa027
 */
public class IafaSummaryQueryBuildCsvAction extends ActionSupport {

	private static final long serialVersionUID = -198737835399515405L;

	private List<IafaSummaryDTO> iafaSummaryRecords = new ArrayList<>();
	private List<ListComp> columnNameValues = new ArrayList<>();
	private String filters;

	private String csvFileName;
	private String fileName;

	public String execute() throws Exception {
		File tempFile = File.createTempFile("iafasummaryrecords", "csv");
		fileName="IAFASummary.csv";
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
		fileWriter.write("IAFA Summary Report\n\n");
		
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
		
		for (IafaSummaryDTO ie : iafaSummaryRecords) {			
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
					} else {
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
			IafaSummaryDTO.class.getMethod("get" + StringUtils.capitalize(listComp.getItemVal()));
			return true;
		} catch (NoSuchMethodException e) {
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
		case "upFromDt":
			return "Usage Period From ";	
		case "upToDt":
			return "Usage Period To ";		
		case "appType":
			return "App Type Code ";	
		case "procCatCd":
			return "Process Category Code ";
		case "procTypeCd":
			return "Process Type Code ";
		case "itemTypeCd":
			return "Item Type Code ";
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
	
	public List<IafaSummaryDTO> getIafaSummaryRecords() {
		return iafaSummaryRecords;
	}

	public void setIafaSummaryRecords(List<IafaSummaryDTO> iafaSummaryRecords) {
		this.iafaSummaryRecords = iafaSummaryRecords;
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

