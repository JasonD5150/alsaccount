package fwp.alsaccount.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import fwp.security.user.UserDTO;


public class Utils {
	
	public static Integer YearFromTimestamp(Timestamp inDate){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(inDate.getTime());
		Integer rtn = cal.get(Calendar.YEAR);
		return rtn;
	}

	public static Timestamp StrToTimestamp(String inDate, String type){
		DateFormat shortFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat longFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		
		Date date = null;
		
		try {
			if("short".equals(type)){
				date = (Date) shortFormat.parse(inDate);
			}else{
				date = (Date) longFormat.parse(inDate);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Timestamp rtn = new Timestamp(date.getTime());
		
		return rtn;
	}

	/**
	 * Does the security subject (user) have any role in the provided
	 * string array
	 *
	 * @param roles
	 * @return true or false
    */
	public Boolean hasAnyRole(String... roles) {
		String env = System.getProperty("environment");
		String appsOu = "";
		if ("DEV".equals(env)) {
			appsOu = "_D";
		} else if ("TEST".equals(env)) {
			appsOu = "_T";
		}
		if (roles != null && roles.length > 0) {
			for (String role : roles) {
				if(SecurityUtils.getSubject().hasRole(role+appsOu)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * get the person id of the person logged in
	 * @param request
	 * @return int
	 */
	public int getPersonId(HttpServletRequest request)
    {
        int personId = 0;
        UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
		
        personId = userInfo.getUserAuditId(); 
        
        return personId;
    }
	
	
	/**
	 * check the groups of person logged in
	 * @param request
	 * @return int
	 */
	public boolean checkGroup(HttpServletRequest request,String usrGroup)
    {
        boolean inGroup = false;

        Subject currentUser = SecurityUtils.getSubject();		
		if (currentUser.hasRole(usrGroup) ) {
			inGroup = true;
		}
        
        return inGroup;
    }
	
	/**
	 * check a string to make sure it is an integer
	 * @param str
	 * @return boolean
	 */
	public boolean isInteger(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	}
	
	/**
	 * get the user id of the person logged in
	 * @param request
	 * @return int
	 */
	public String getUserId(HttpServletRequest request)
    {
        String userId = "";
      //Get user information
        UserDTO userInfo = (UserDTO)SecurityUtils.getSubject().getSession().getAttribute("userInfo");
        
        userId = String.valueOf(userInfo.getStateId());
        
        return userId;
    }

//	 * From CLOB to String
//	 * @return string representation of clob

	public String clobToString(java.sql.Clob data)
	{
	    final StringBuilder sb = new StringBuilder();

	    try
	    {
	        final Reader         reader = data.getCharacterStream();
	        final BufferedReader br     = new BufferedReader(reader);

	        int b;
	        while(-1 != (b = br.read()))
	        {
	            sb.append((char)b);
	        }

	        br.close();
	    }
	    catch (SQLException e)
	    {
	        return e.toString();
	    }
	    catch (IOException e)
	    {
	        return e.toString();
	    }

	    return sb.toString();
	}
	
	public Clob createClob(org.hibernate.Session s, String text) {
	    return s.getLobHelper().createClob(text);
	}
	
	
	public String getStringDate(Date inDate){
		String rtv = "";
		SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");
		if (inDate != null){
			rtv = sdfDate.format(inDate);
		}
		return rtv;
	}	
   
   public static String nullFix(String in){
       if (in == null) return "";
       else return in;
	}

	public static Integer nullFix(Integer in){
       if (in == null) return 0;
       else return in;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String buildStr(String where, String filters){
    	try {
            Hashtable<String,Object> jsonFilter = (Hashtable<String, Object>) (new gov.fwp.mt.RPC.FWPJsonRpc().new JsonParser(filters)).FromJson();
            String groupOp = (String) jsonFilter.get("groupOp");
            ArrayList rules = (ArrayList) jsonFilter.get("rules");

            int rulesCount = rules.size();
            String tmpCond = "";
            
    		for (int i = 0; i < rulesCount; i++) {
    			Hashtable<String,String> rule = (Hashtable<String, String>) rules.get(i);
    			
    			String tmp = rule.get("field");
    			
    			if (i == 0) {
    				tmpCond = "and (";
    			} else {
    				tmpCond = groupOp;
    			}
    			
    			if(rule.get("data").equalsIgnoreCase("yes")){
    				rule.put("data", "Y");
    			}else if(rule.get("data").equalsIgnoreCase("no")){
    				rule.put("data", "N");
    			}
    			
    	        if (rule.get("op").equalsIgnoreCase("eq")) {		    	  
    	        	where = where + " " +tmpCond+" " + tmp + " = '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("ne")) {
    	        	where = where + " " +tmpCond+" " + tmp + " <> '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("lt")) {
    	        	where = where + " " +tmpCond+" " + tmp + " < '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("gt")) {
    	        	where = where + " " +tmpCond+" " + tmp + " > '" + rule.get("data")+"'";
    	        } else if (rule.get("op").equalsIgnoreCase("cn")) {
    	        	where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('%" + rule.get("data")+"%')";
    		    } else if (rule.get("op").equalsIgnoreCase("bw")) {
    		    	where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('" + rule.get("data")+"%')";
    	        } else if (rule.get("op").equalsIgnoreCase("ew")) {
    	        	where = where + " " +tmpCond+ " upper(" + tmp + ") like upper('%" + rule.get("data")+"')";
    	        }			
    		}
    		 where = where + ")";	
     
    		  }
    		  catch(Exception ex) {
    			  where = "Build String Error: " + ex;  
    	  }
          return where;
    }
	
	
	
	//Breaks a string into parts
	public static List<String> getParts(String string, int partitionSize) {
        List<String> parts = new ArrayList<String>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize)
        {
            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        }
        return parts;
    }
	
	public static java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
	    return new java.sql.Date(date.getTime());
	} 
	
	
}