package fwp.alsaccount.action;

import java.io.File;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Action handler for the ALS Person search pages to download CSV export file.
 *
 * @author c8a650
 */
public class DownloadCsvAction extends ActionSupport {

	private static final Logger log = LoggerFactory.getLogger(DownloadCsvAction.class);
	private static final long serialVersionUID = -8877192033064929268L;

	private FileInputStream csvFileInputStream;
	private String csvFileName;
	private String fileName;

	public String execute() throws Exception {
		downloadcsv();
		return SUCCESS;
	}

	private void downloadcsv() throws Exception {
		File file = new File(
				String.format("%s%s%s",
						System.getProperty("java.io.tmpdir"),
						File.separator,
						csvFileName));	
		csvFileInputStream = new FileInputStream(file);
		file.deleteOnExit();
	}

	public String getCsvFileName() {
		return csvFileName;
	}

	public void setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
	}

	public FileInputStream getCsvFileInputStream() {
		return csvFileInputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}

