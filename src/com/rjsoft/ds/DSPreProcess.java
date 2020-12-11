package com.rjsoft.ds;

import java.io.InputStream;
import java.util.Vector;
import com.ibm.xml.parsers.SAXParser;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.AttributeList;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;

/**
 * ����Ԥ��������Դ�Ķ����书���Ƕ�ȡ����Դ����ģ��xml����Ϣ��Ȼ�����֮���Ա㷵�ؾ��������Դ�������Դ�����
 * @author coca
 */
public class DSPreProcess extends HandlerBase
{
  /**
   * ����Դ�е�ÿһ��item�ļ���
   */
  private Vector allDS=new Vector();	//����Դ�е������ļ���
  /**
   * ����Դ�ж����ʵ���������
   */
  private String imp ="";				//ʵ�ֶ���
  
  private boolean setName=false;		//��������Դ��name�ڵ�ı�־
  private boolean setValue=false;		//��������Դ��value�ڵ�ı�־
  private boolean setDesc=false;		//��������Դ��desc�ڵ�ı�־
  private DSItem tmpDS=null;			//��ʱ������Դ�����
  /**
   * ������
   */
  public DSPreProcess(){}
  
  /**
   * ������ڷ���,����xml�ĵ���
   * @param is  InputStream��xml�ĵ���
   */
  public void parse(InputStream is) throws Exception
  {
    InputSource isrc=new InputSource(is); //��ȡxml������
    SAXParser sp=new SAXParser();   //��ʼ��������
    sp.setDocumentHandler(this);    //���ô����ĵ��¼��Ķ��󣬴˴�Ϊ������
    sp.setErrorHandler(this);       //���ô�������¼��Ķ��󣬴˴�Ϊ������
    try 
    {  
      sp.parse(isrc);   //��ʼ����
    } 
    catch (Exception e) 
    { 
      System.err.println("DSPreProcess Error:"+e.getMessage());
      e.printStackTrace();
      throw new Exception("DSPreProcess�����޷�����XML");
    }
    finally{
    	is.close();
    	isrc=null;
    	is=null;
    }
  }
  /*
	public void parse(InputStream is) throws Exception
	{
		try
		{
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxFactory.newSAXParser();
			DefaultHandler defaultHandler = new DefaultHandler();
			//����һ�� InputStream ʵ����һ��SAX HandlerBase ʵ��
			saxParser.parse(is, defaultHandler);
		}
		catch (Exception e) 
		{ 
			System.err.println("DSPreProcess Error:"+e.getMessage());
			e.printStackTrace();
			throw new Exception("DSPreProcess�����޷�����XML");
		}
		finally
		{
			is.close();
			is=null;
		}
	}
	*/
  /**
   * �ĵ��ڵ㿪ʼ�¼�����������ݲ�ͬ�Ľڵ��������ø��ֱ�Ǻͳ�ʼ�����Ա��
   */
  public void startElement(String name, AttributeList attrs){
    if (name.equalsIgnoreCase("items")){  //ȡ��ʵ���������
      if (attrs!=null) this.imp=attrs.getValue("implement");
    }
    if (name.equalsIgnoreCase("item")){ //����ڵ���������Ϊitem����ô
      if (tmpDS!=null) {  //�������ǰ������Դ�������׷�������������Դ�������
        DSItem ds=new DSItem();
        ds.setDesc(tmpDS.getDesc());
        ds.setName(tmpDS.getName());
        ds.setValue(tmpDS.getValue());
        ds.setOpinionMap(tmpDS.getOpinionMap());
        ds.setDelima(tmpDS.getDelima());
        ds.setFormat(tmpDS.getFormat());
        ds.setFunction(tmpDS.getFunction());
        allDS.addElement(ds);
      }
      tmpDS=new DSItem(); //Ȼ���½�һ����ʱ����Դ�����
      if (attrs!=null) tmpDS.setOpinionMap(attrs.getValue("opinionmap"));  //�������ӳ������
    }
    if (name.equalsIgnoreCase("name")){ //�����name�ڵ㣬����name�ڵ㴦���־
      setName=true;
    }
    if (name.equalsIgnoreCase("desc")){ //�����desc�ڵ㣬����desc�ڵ㴦���־
      setDesc=true;
    }
    if (name.equalsIgnoreCase("value")){ //�����value�ڵ㣬����value�ڵ㴦���־
      setValue=true;
      //����value�ڵ������
      if (attrs!=null) {
        tmpDS.setDelima(attrs.getValue("delima"));
        tmpDS.setFormat(attrs.getValue("format"));
      } //�����ָ����ֵ�ָ�����ô���ö�ֵ�ָ�������
    }
  }
  /**
   *�ĵ��ڵ�����¼��������һ��item�Ľڵ����Ϣ��Ӧ��DataSource���ӵ�������
   */
  public void endElement(String name)  
  { 
    if (name.equalsIgnoreCase("items")) { //�����items�Ľ�β�¼�
      if (tmpDS!=null) {  //�����ʱ������Դ���ĵ���Ϊ�գ���ô�������������Դ��׷�ӵ�������
        DSItem ds=new DSItem();
        ds.setDesc(tmpDS.getDesc());
        ds.setName(tmpDS.getName());
        ds.setValue(tmpDS.getValue());
        ds.setOpinionMap(tmpDS.getOpinionMap());
        allDS.addElement(ds);
      }
    }
  } 
  /**
   * �ĵ��ַ������¼��������������Դ��ĸ����������ý�ȥ
   */
  public void characters(char ch[], int start, int length)  
  { 
    if (setName){ //���������name��־,���name��ֵд����ʱ����Դ��
      tmpDS.setName(new String(ch,start,length).trim());
      setName=false;
    }
    if (setDesc){ //���������desc��־,���desc��ֵд����ʱ����Դ��
      tmpDS.setDesc(new String(ch,start,length).trim());
      setDesc=false;
    }
    if (setValue){ //���������value��־,���value��ֵд����ʱ����Դ��
      tmpDS.setValue(new String(ch,start,length).trim());
      setValue=false;
    }
  }
  /**
   * ��������Դ�����е�item��Ӧ��DataSource����ļ���
   * @return  Vector  DataSource�ļ����б�
   */
  public Vector getDataSourcese(){
    return this.allDS;
  }
  /**
   * ��������Դ��ʵ��������
   * @return  String  ����Դ��ʵ��������
   */
  public String getImplement(){
    return this.imp;
  }
  
  /**
   * ���棬���ڴ�����ֵľ����¼�. 
   */ 
  public void warning(SAXParseException ex)  
  { 
    System.err.println("[Warning] "+": "+ ex.getMessage()); 
  } 

  /**
   * �������ڴ�����ֵĴ����¼�. 
   */ 
  public void error(SAXParseException ex)  
  { 
    System.err.println("[Error] "+ ": "+ex.getMessage()); 
  } 

  /** 
   * ���ش������ڴ�����ֵ����ش����¼�. 
   */ 
  public void fatalError(SAXParseException ex)  
    throws SAXException  
  { 
    System.err.println("[Fatal Error] "+": "+ex.getMessage()); 
    throw ex; 
  } 
}
