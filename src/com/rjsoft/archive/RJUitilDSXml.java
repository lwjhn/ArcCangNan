package com.rjsoft.archive;

import com.lwjhn.util.*;
import com.rjsoft.ds.DSInitProcess;
import com.rjsoft.ds.DSProcess;
import com.rjsoft.ds.DSProcessDispatch;

import java.io.*;

import lotus.domino.*;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class RJUitilDSXml extends UtilXML {
	private static DSInitProcess dspp = new DSInitProcess();
	private static DSProcess dsp = new DSProcessDispatch();
	
	public static String parseDSXml(InputStream xml,Document doc,Session session) throws Exception{
		dspp.parse(xml);
		dsp.process(dspp.getDataSourcese(), doc, session);
		return dsp.getResultXml();
	}
	
	private static String getDataSourceTName(Document configDoc, String name) {
		if (configDoc == null) return "";
		try {
			Vector vectorTName = configDoc.getItemValue(name);
			if (vectorTName == null || vectorTName.isEmpty()) return "";
			return (String) vectorTName.lastElement();
		} catch (NotesException e) {
			return "";
		}		
	}
	
	public static void parseDSHtml(Document doc,Session session, File outpath, Charset charset) throws Exception{
		String DataSourceTName = "", FileStyleTName = "";
		List query = new ArrayList();		
		Document xmldoc = null,xsldoc = null ,tdoc = null;
		EmbeddedObject oxml = null,oxsl = null;
		InputStream isxml = null, isxsl =null; 
		try {
			if(! ( (DataSourceTName = doc.getItemValueString("DocWord"))==null || "".equals(DataSourceTName) ) )
				query.add("Form=\"WordConfig\" & DocWord=\""+DataSourceTName.replaceAll("\"","\\\"")+"\"");
			if(! ( (DataSourceTName = doc.getItemValueString("C_FlowCategory"))==null || "".equals(DataSourceTName) ) && !( (FileStyleTName = doc.getItemValueString("C_FlowName"))==null || "".equals(FileStyleTName)  ) )
				query.add("Form=\"WordConfig\" & FlowName=\""+DataSourceTName.replaceAll("\"","\\\"")+"\\"+FileStyleTName.replaceAll("\"","\\\"")+"\"");
			query.add("Form=\"WordConfig\" & Name=\"DefaultConfig\"");			
			DataSourceTName = FileStyleTName = "";
			for(int i=0;i<query.size();i++){
				if( !(tdoc==null || xmldoc == tdoc || xsldoc == tdoc))
					try{tdoc.recycle();}catch(Exception e){}
				tdoc = doc.getParentDatabase().search(String.valueOf(query.get(i)), null, 1).getFirstDocument();
				if(tdoc == null) continue;
				if(xmldoc == null && !"".equals(DataSourceTName = getDataSourceTName(tdoc,"DataSourceTName"))){
					xmldoc = tdoc;				
				}
				if(xsldoc == null && !"".equals(FileStyleTName = getDataSourceTName(tdoc,"FileStyleTName"))){
					xsldoc = tdoc;
				}
			}
			if(xmldoc==null ) throw new Exception("Can't find the xml Template in all profiles ! " + doc.getUniversalID());
			if(xsldoc==null) throw new Exception("Can't find the xsl Template in all profiles ! " + doc.getUniversalID());
			//System.out.println("DataSourceTName="+DataSourceTName + "  FileStyleTName="+FileStyleTName);
			if((oxml = xmldoc.getAttachment(DataSourceTName))==null)
				throw new Exception("Can't find xml Template File:" + DataSourceTName + "in Configration Document . " + xmldoc.getUniversalID());			
			if((oxsl = xsldoc.getAttachment(FileStyleTName))==null)
				throw new Exception("Can't find xml Template File:" + DataSourceTName + "in Configration Document . " + xsldoc.getUniversalID());
			
			transformer(isxsl = oxsl.getInputStream(), parseDSXml(isxml=oxml.getInputStream(),doc,session), outpath, charset);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(isxml!=null) try{isxml.close();}catch(Exception e){}
			if(isxsl!=null) try{isxsl.close();}catch(Exception e){}
			if(oxsl!=null) try{oxsl.recycle();}catch(Exception e){}
			if(oxml!=null) try{oxml.recycle();}catch(Exception e){}
			if(xmldoc!=null) try{xmldoc.recycle();}catch(Exception e){}
			if(xsldoc!=null) try{xsldoc.recycle();}catch(Exception e){}
			if(tdoc!=null) try{tdoc.recycle();}catch(Exception e){}
		}
	}
	
	public static void parseDSHtml(Document doc,Session session, OutputStream outpath, Charset charset) throws Exception{
		String DataSourceTName = "", FileStyleTName = "";
		List query = new ArrayList();		
		Document xmldoc = null,xsldoc = null ,tdoc = null;
		EmbeddedObject oxml = null,oxsl = null;
		InputStream isxml = null, isxsl =null; 
		try {
			if(! ( (DataSourceTName = doc.getItemValueString("DocWord"))==null || "".equals(DataSourceTName) ) )
				query.add("Form=\"WordConfig\" & DocWord=\""+DataSourceTName.replaceAll("\"","\\\"")+"\"");
			if(! ( (DataSourceTName = doc.getItemValueString("C_FlowCategory"))==null || "".equals(DataSourceTName) ) && !( (FileStyleTName = doc.getItemValueString("C_FlowName"))==null || "".equals(FileStyleTName)  ) )
				query.add("Form=\"WordConfig\" & FlowName=\""+DataSourceTName.replaceAll("\"","\\\"")+"\\"+FileStyleTName.replaceAll("\"","\\\"")+"\"");
			query.add("Form=\"WordConfig\" & Name=\"DefaultConfig\"");			
			DataSourceTName = FileStyleTName = "";
			for(int i=0;i<query.size();i++){
				if( !(tdoc==null || xmldoc == tdoc || xsldoc == tdoc))
					try{tdoc.recycle();}catch(Exception e){}
				tdoc = doc.getParentDatabase().search(String.valueOf(query.get(i)), null, 1).getFirstDocument();
				if(tdoc == null) continue;
				if(xmldoc == null && !"".equals(DataSourceTName = getDataSourceTName(tdoc,"DataSourceTName"))){
					xmldoc = tdoc;				
				}
				if(xsldoc == null && !"".equals(FileStyleTName = getDataSourceTName(tdoc,"FileStyleTName"))){
					xsldoc = tdoc;
				}
			}
			if(xmldoc==null ) throw new Exception("Can't find the xml Template in all profiles ! " + doc.getUniversalID());
			if(xsldoc==null) throw new Exception("Can't find the xsl Template in all profiles ! " + doc.getUniversalID());
			//System.out.println("DataSourceTName="+DataSourceTName + "  FileStyleTName="+FileStyleTName);
			if((oxml = xmldoc.getAttachment(DataSourceTName))==null)
				throw new Exception("Can't find xml Template File:" + DataSourceTName + "in Configration Document . " + xmldoc.getUniversalID());			
			if((oxsl = xsldoc.getAttachment(FileStyleTName))==null)
				throw new Exception("Can't find xml Template File:" + DataSourceTName + "in Configration Document . " + xsldoc.getUniversalID());
			
			transformer(isxsl = oxsl.getInputStream(), parseDSXml(isxml=oxml.getInputStream(),doc,session), outpath, charset);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(isxml!=null) try{isxml.close();}catch(Exception e){}
			if(isxsl!=null) try{isxsl.close();}catch(Exception e){}
			if(oxsl!=null) try{oxsl.recycle();}catch(Exception e){}
			if(oxml!=null) try{oxml.recycle();}catch(Exception e){}
			if(xmldoc!=null) try{xmldoc.recycle();}catch(Exception e){}
			if(xsldoc!=null) try{xsldoc.recycle();}catch(Exception e){}
			if(tdoc!=null) try{tdoc.recycle();}catch(Exception e){}
		}
	}
}
