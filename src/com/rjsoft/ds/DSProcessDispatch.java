package com.rjsoft.ds;

//import java.io.Writer;
import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Arrays; //import java.util.StringTokenizer;
import java.text.SimpleDateFormat;

import com.rjsoft.ds.DSItem;
import com.rjsoft.ds.DSProcess;
import com.rjsoft.ds.FormatBase;

import lotus.domino.*;

/**
 * ��ʼ����������Դ�Ķ���ʵ��DSProcess�ӿ�
 * 
 * @author zhangguiping@rongji.com Created 2003-10-20
 */
public class DSProcessDispatch implements DSProcess {
	private Session session; // domino�Ự����
	private Database db, ndb; // domino���ݿ���󣬱�ʾ��ǰ���ݿ�
	private View viewOpinion, pview, pviewold; // ����ĵ���ͼ
	private Document document, ndoc; // ��ǰ��������ĵ�
	private DSItem dsItems[]; // ��������Դ�еľ�����Ķ����б�

	/**
	 * ȱʡ������
	 * 
	 */
	public DSProcessDispatch() {
		
	}

	public void process(Vector dsItem, Document doc, Session session) throws Exception {	
		db = ndb = null;
		viewOpinion = pview=pviewold=null;
		document= ndoc=null;
		dsItems = null;
		
		if(session==null) throw new Exception("session is null at DSProcessDispatch.process !");
		if (doc != null && session != null) {
			this.session = session;
			db = doc.getParentDatabase();
			ndb = session.getDatabase("", "names.nsf");
			pview = ndb.getView("PeopleByName");
			pviewold = ndb.getView("PeopleByNameold");
			document = doc;

			String mssServer = doc.getItemValueString("MSSServer");
			String mssOpinion = doc.getItemValueString("MSSOpinion");
			if ("".equals(mssOpinion))
				mssOpinion = doc.getItemValueString("OpinionlogDatabase");
			if (!"".equals(mssOpinion)) {
				viewOpinion = session.getDatabase(mssServer, mssOpinion).getView("Opinion");
			}

			if (viewOpinion == null) {
				viewOpinion = db.getView("Opinion");
			}
		}
		dsItems = new DSItem[dsItem.size()];
		int i = 0;
		for (Enumeration e = dsItem.elements(); e.hasMoreElements();) {
			DSItem item = (DSItem) e.nextElement();
			if (doc != null && session != null)
				dsItems[i++] = initValue(item);
			else
				dsItems[i++] = item;
		}
	}
	
	public void process(Vector dsItem, Document doc, AgentBase context) throws Exception {
		db = ndb = null;
		viewOpinion = pview=pviewold=null;
		document= ndoc=null;
		dsItems = null;
		
		if (doc != null && context != null) {
			session = context.getSession();
			// db = session.getAgentContext().getCurrentDatabase();
			db = doc.getParentDatabase();
			ndb = session.getDatabase("", "names.nsf");
			pview = ndb.getView("PeopleByName");
			pviewold = ndb.getView("PeopleByNameold");
			document = doc;

			String mssServer = doc.getItemValueString("MSSServer");
			String mssOpinion = doc.getItemValueString("MSSOpinion");
			if ("".equals(mssOpinion))
				mssOpinion = doc.getItemValueString("OpinionlogDatabase");
			if (!"".equals(mssOpinion)) {
				viewOpinion = session.getDatabase(mssServer, mssOpinion).getView("Opinion");
			}

			if (viewOpinion == null) {
				viewOpinion = db.getView("Opinion");
			}
		}
		dsItems = new DSItem[dsItem.size()];
		int i = 0;
		for (Enumeration e = dsItem.elements(); e.hasMoreElements();) {
			DSItem item = (DSItem) e.nextElement();
			if (doc != null && context != null)
				dsItems[i++] = initValue(item);
			else
				dsItems[i++] = item;
		}
	}

	private DSItem initValue(DSItem item) {
		try {
			String rawVal = item.getValue(); // ��ֵ���㹫ʽ
			String name = item.getName(); // ����
			String oMap = item.getOpinionMap(); // ������־
			if (oMap != null && oMap.length() > 0) // �����������ͣ���ô�����������
			{

				item.setValue(getOpinion(name, item.getFormat()));
				return item;
			}
			String function = item.getFunction();
			if (function == "getOpinionUser") {
				getOpinionUser(rawVal);
				return item;
			} else if (function == "getOpinionBody") {
				getOpinionBody(rawVal);
				return item;
			}

			Vector v = null;
			if (rawVal == null || rawVal.length() < 1)
				v = document.getItemValue(name); // �1�71�1�7���ֵû�м��㹫ʽ����ôֱ�ӷ�����ք1�7
			else
				v = session.evaluate(item.getValue(), document); // �����ֵ�м��㹫ʽ����ô������㹫ʽ�����ؽ��

			if (v.isEmpty())
				item.setValue(""); // �����ʽû�з���ֵ����ô���ؿմ�
			else
				item.setValue(getValueFromVector(v, item.getDelima())); // ������ʽ����ֵ
			// �ж��Ƿ���ֵ�ĸ�ʽ�����壬����У�Ҫ��ʽ��ֵ
			if (item.getFormat() != null) {
				String formatClassName = item.getFormat();
				if (formatClassName.length() > 0) {
					try {
						FormatBase format = (FormatBase) Class.forName(formatClassName).newInstance();
						item.setValue(format.format(item.getValue()));
					} catch (ClassNotFoundException cnfe) {
						System.out.println("Format Value Error:ClassNotFoundException:" + formatClassName);
					} catch (IllegalAccessException iae) {
						System.out.println("Format Value Error:IllegalAccessException:" + formatClassName);
					} catch (InstantiationException ie) {
						System.out.println("Format Value Error:InstantiationException:" + formatClassName);
					} catch (Exception e) {
						System.out.println("Format Value Error:" + e.getMessage() + "\nFormat Class Name:" + formatClassName);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("initValue() Error in DSProcessDispatch.java:" + e.getMessage());
			e.printStackTrace();
		}
		return item;
	}

	/**
	 * ��Vector�н������ݣ�Ȼ�󷵻����ݶ�Ӧ���ַ���ֵ
	 * ����зָ������岢��Vector���ݲ�ֻһ������ô�÷ָ����ָ���Щֵ��������ȱʡ��<b>","</b>�ָ�
	 * 
	 * @param v
	 *            Vector
	 * @param delima
	 *            �ָ���
	 * @return
	 */
	private String getValueFromVector(Vector v, String delima) {
		String tmpVal = "";
		if (v == null)
			return "";
		if (v.isEmpty())
			return "";
		if (v.size() == 1) {
			tmpVal = "" + v.firstElement();
			// if (tmpVal.lastIndexOf(".0")>=0)
			// tmpVal=tmpVal.substring(0,tmpVal.length()-2);
			return process4Xml(process4Html(tmpVal));
		}
		String retStr = "";
		int vcount = v.size();
		int count = 1;
		for (Enumeration e = v.elements(); e.hasMoreElements();) {
			tmpVal = e.nextElement().toString();
			// if (tmpVal.lastIndexOf(".0")>=0)
			// tmpVal=tmpVal.substring(0,tmpVal.length()-2);
			if (count < vcount) {
				if (delima != null && delima.length() > 0)
					retStr += tmpVal + delima;
				else
					retStr += tmpVal + ",";
			} else
				retStr += tmpVal + "";
			count++;
		}
		return process4Xml(process4Html(retStr));
	}

	/**
	 * ���ݴ�����ĵ��������ض�Ӧ��ֵ
	 * 
	 * @param itmName
	 *            ����
	 * @return String ��ֵ
	 * @throws Exception
	 */
	private String getItemValue(String itmName) throws Exception {
		String retVal = "";
		try {
			Item itm = document.getFirstItem(itmName);
			if (itm != null) {
				switch (itm.getType()) { // �ж�notes�������Ա�ֱ���
				case lotus.domino.Item.DATETIMES: // ����ʱ��
					retVal = itm.getDateTimeValue().toString();
					break;
				case lotus.domino.Item.NUMBERS: // ����
					retVal = itm.getValueInteger() + "";
					break;
				case lotus.domino.Item.RICHTEXT: // rtf��
					retVal = process4Xml(itm.getValueString());
					break;
				default: // ȱʡ������������
					String tmpVal = itm.getValueString();
					// ����зǷ��ַ�,Ҫ��cdata�ķ�ʽ��ʾ
					retVal = process4Xml(tmpVal);
					break;
				}
			}
		} catch (NotesException e) {
			System.err.println("getItemValue(itmName) Error:" + e.text + e.id);
			return "";
		}
		char old = '\013'; // �滻�����ַ�
		retVal = retVal.replace(old, ' ');
		return retVal;
	}

	private String getOpinion(String itemName, String format) throws Exception {
		ViewEntryCollection vec = viewOpinion.getAllEntriesByKey(document.getUniversalID());
		if (vec.getCount() < 1)	return "";

		StringBuffer sb = new StringBuffer("");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss");
		String tempopinion = "", sep = "", tempsort = "";
		Vector tempf;
		Vector vcIndex = document.getItemValue("C_UnitIndexAll");
		Vector vcUser = session.evaluate("@Name([Abbreviate];C_UserReader)",document);
		String userName = document.getItemValueString("UserName");
		ViewEntry entry = vec.getFirstEntry();
		Document opinionDoc = null;
		while (entry != null) {
			opinionDoc = entry.getDocument();
			if (opinionDoc.getItemValueString("OpinionField").equals(itemName) || opinionDoc.getItemValueString("OpinionType").equals(itemName)) {
			if(vcUser.indexOf(opinionDoc.getItemValueString("OpinionUser"))==-1 || vcIndex.indexOf(opinionDoc.getItemValueString("UnitIndex"))==-1 || userName.equals(opinionDoc.getItemValueString("OpinionUser")) || userName.equals("")){
				if (format == null) {

					tempopinion += (sep + "<opinion>\n");
					String temp;
					temp = opinionDoc.getItemValueString("OpinionUser");
					ndoc = pview.getDocumentByKey(temp, true);
					if (ndoc == null && pviewold != null) {
						ndoc = pviewold.getDocumentByKey(temp, true);
					}
					if (ndoc == null) {
						tempsort = tempsort + sep + "999999";
						tempopinion = tempopinion + "<userNum>999999</userNum>";
					} else {
						tempf = session.evaluate("@right(\"000000\"+@text(NUM);6)", ndoc);
						temp = (String) tempf.firstElement();
						tempf = session.evaluate("@right(\"000000\"+@If(@Contains(@text(NUM);\".\");@ReplaceSubstring(@text(NUM);\".\";\"\");@text(NUM)+\"0\");6)", ndoc);

						temp = (String) tempf.firstElement();
						if ("".equals(temp))
							tempopinion = tempopinion + "<userNum>999999</userNum>";
						else
							tempopinion = tempopinion + "<userNum>" + temp + "</userNum>";
						tempsort = tempsort + sep + opinionDoc.getItemValueString("num");
					}

					tempopinion += "<body>" + process4Xml(process4Html(opinionDoc.getItemValueString("OpinionBody"))) + "</body>\n";
					if (opinionDoc.hasItem("OpinionOpenType")) {
						tempopinion += "<openType>" + opinionDoc.getItemValueString("OpinionOpenType") + "</openType>\n";
						tempopinion += "<openBody>" + process4Xml(process4Html(opinionDoc.getItemValueString("OpinionOpenBody"))) + "</openBody>\n";
					}
					if (opinionDoc.hasItem("OpinionUserTitle"))
						temp = opinionDoc.getItemValueString("OpinionUserTitle") + "/";
					else
						temp = opinionDoc.getItemValueString("OpinionUser") + "/";
					
					
					
					
					
					tempopinion += "<opinionDeptTitle>" + opinionDoc.getItemValueString("OpinionDeptTitle") + "</opinionDeptTitle>";
					tempopinion += "<groupByOpinionUserDept>" + opinionDoc.getItemValueString("GroupByOpinionUserDept") + "</groupByOpinionUserDept>";
					
					
					tempopinion += "<user>" + temp.substring(0, temp.indexOf("/")) + "</user>";
					tempopinion += "<agent>" + opinionDoc.getItemValueString("OpinionAgent") + "</agent>";
					Vector vcDate = opinionDoc.getItemValueDateTimeArray("OpinionTime");
					Date date = ((DateTime) vcDate.elementAt(0)).toJavaDate();
					tempopinion += "<date>" + formatter.format(date) + "</date>";
					tempopinion += "<unid>" + opinionDoc.getUniversalID() + "</unid>";
					tempopinion += "</opinion>\n";

					sep = "~~";

				} else {
					sb.append(process4Xml(process4Html(opinionDoc.getItemValueString(format))));
				}
			}
			}
			entry = vec.getNextEntry(entry);
		}
		
		String[] oplist = tempopinion.split("~~");
		Arrays.sort(oplist);
		for (int i = 0; i < oplist.length; i++) {
			sb.append(oplist[i]);
		}
		if(!vcIndex.isEmpty()) vcIndex.removeAllElements();
		
		return sb.toString();
	}
	private String getOpinionOld(String itemName) throws Exception {
		ViewEntryCollection vec = viewOpinion.getAllEntriesByKey(document.getUniversalID());
		if (vec.getCount() < 1) return "";

		StringBuffer sb = new StringBuffer("");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss");

		ViewEntry entry = vec.getFirstEntry();
		Document opinionDoc = null;
		while (entry != null) {
			opinionDoc = entry.getDocument();
			if (opinionDoc.getItemValueString("OpinionField").equals(itemName) || opinionDoc.getItemValueString("OpinionType").equals(itemName)) {
				sb.append("<opinion>\n");

				sb.append("<body>" + process4Xml(process4Html(opinionDoc.getItemValueString("OpinionBody"))) + "</body>\n");
				String temp;
				if (opinionDoc.hasItem("OpinionUserTitle"))
					temp = opinionDoc.getItemValueString("OpinionUserTitle") + "/";
				else
					temp = opinionDoc.getItemValueString("OpinionUser") + "/";
				sb.append("<user>" + temp.substring(0, temp.indexOf("/")) + "</user>");
				sb.append("<register>" + opinionDoc.getItemValueString("OpinionRegister") + "</register>");
				if (opinionDoc.hasItem("OpinionAgentTitle"))
					sb.append("<agent>" + opinionDoc.getItemValueString("OpinionAgentTitle") + "</agent>");
				else
					sb.append("<agent>" + opinionDoc.getItemValueString("OpinionAgent") + "</agent>");

				Vector vcDate = opinionDoc.getItemValueDateTimeArray("OpinionTime");
				Date date = ((DateTime) vcDate.elementAt(0)).toJavaDate();
				sb.append("<date>" + formatter.format(date) + "</date>");
				sb.append("<unid>" + opinionDoc.getUniversalID() + "</unid>");

				// Vector vcDate = session.evaluate("@Text(@Year(OpinionTime))+\"��\"+@Right(\"0\"+@Text(@Month(OpinionTime));2)+\"��\"+@Right(\"0\"+@Text(@Day(OpinionTime));2)+\"�� \"+@Text(OpinionTime;\"T0S1\")",opinionDoc);
				// sb.append("<date>" + vcDate.elementAt(0) + "</date>");
				sb.append("</opinion>\n");
			}
			entry = vec.getNextEntry(entry);
		}
		return sb.toString();
	}
	
	private String getOpinionUser(String fields) throws Exception {
		return getOpinionAttribute(fields, "User");
	}

	private String getOpinionBody(String fields) throws Exception {
		return getOpinionAttribute(fields, "Body");
	}

	private String getOpinionAttribute(String fields, String type) throws Exception {

		ViewEntryCollection vec = viewOpinion.getAllEntriesByKey(document.getUniversalID());
		if (vec.getCount() < 1)
			return "";

		StringBuffer sb = new StringBuffer("");
		// SimpleDateFormat formatter = new
		// SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss");

		String[] fieldArr = fields.split(":");
		int i = 0, len = fieldArr.length;
		String opinionField, opinionType;
		ViewEntry entry = vec.getFirstEntry();
		Document opinionDoc = null;
		while (entry != null) {
			opinionDoc = entry.getDocument();
			opinionType = opinionDoc.getItemValueString("OpinionType");
			opinionField = opinionDoc.getItemValueString("OpinionField");
			for (i = 0; i < len; i++) {
				if (!(opinionField.equalsIgnoreCase(fieldArr[i]) || opinionType.equalsIgnoreCase(fieldArr[i]))) {
					continue;
				}
				sb.append(process4Xml(process4Html(opinionDoc.getItemValueString(fieldArr[i] + type) + " ")));
			}

			entry = vec.getNextEntry(entry);
		}

		return sb.toString();
	}

	/**
	 * ���ַ����еĻس�ת��Ϊhtml����
	 * 
	 * <pre>
	 * &lt;br&gt;
	 * </pre>
	 * 
	 * @param in
	 * @return
	 */
	private String process4Html(String in) {
		if (in == null || in.equals(""))
			return "";
		int pos = 0;
		int count = 1;
		pos = in.indexOf("\r\n");
		if (pos < 0)
			pos = in.indexOf("\n");
		else
			count = 2;
		if (pos < 0)
			pos = in.indexOf("\r");
		if (pos == 0) {
			in = "<br>" + in.substring(count);
			return process4Html(in);
		} else if (pos > 0) {
			try {
				in = in.substring(0, pos) + "<br>" + in.substring(pos + count);
			} catch (StringIndexOutOfBoundsException e) {
				in = in.substring(0, pos) + "<br>";
			}
			return process4Html(in);
		} else
			return in;
	}

	/**
	 * ����ı�����xml��ʵ���ַ�����Ҫ����CDATA�ķ�ʽ���
	 * 
	 * @param in
	 *            ������ı�
	 * @return ����Ĵ������ı�
	 */
	private String process4Xml(String in) {
		if (in == null || in.equals(""))
			return "";
		if (in.indexOf('>') >= 0 || in.indexOf('<') >= 0 || in.indexOf('"') >= 0 || in.indexOf('&') >= 0 || in.indexOf("'") >= 0) {
			char[] chars1 = new char[1];
			chars1[0] = 91; // [
			char[] chars2 = new char[1];
			chars2[0] = 93; // ]
			return "<!" + new String(chars1) + "CDATA" + new String(chars1) + " " + in + " " + new String(chars2) + new String(chars2) + ">";
		} else
			return in;
	}

	public void buildResultXml(java.io.PrintWriter pw) throws Exception {
		if (dsItems == null)
			throw new Exception("����Դ��Ŀ�����ڻ���û����ȷ��ʼ����");
		boolean oMapFlag = false;
		pw.println("content-type:text/xml; charset=UTF-8");
		pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		pw.println("<items>");

		for (int i = 0; i < dsItems.length; i++) {
			if (dsItems[i].getOpinionMap() != null && dsItems[i].getOpinionMap().length() > 0) // �����������־����ô���������ʽxmlƬ��
			{
				pw.println("<item opinionmap=\"" + dsItems[i].getOpinionMap() + "\">");
				oMapFlag = true;
			} else
				pw.println("<item>");
			// ����xmlƬ��
			pw.println("<name>" + process4Xml(dsItems[i].getName() + "") + "</name>");
			// ������xmlƬ��
			pw.println("<desc>" + process4Xml(dsItems[i].getDesc() + "") + "</desc>");
			// ��ֵxmlƬ��
			if (dsItems[i].getValue().trim().length() > 0) { // �������ֵ
				if (dsItems[i].getDelima() != null)
					pw.print("<value delima=\"" + dsItems[i].getDelima() + "\">");
				else
					pw.print("<value>");
				pw.print(dsItems[i].getValue());
			} else { // ���û����ֵ
				if (dsItems[i].getDelima() != null)
					pw.print("<value delima=\"" + dsItems[i].getDelima() + "\">");
				else
					pw.print("<value>");
			}
			pw.println("</value>");
			pw.println("</item>");
		}
		pw.println("</items>");
	}

	public String getResultXml() throws Exception {
		if (dsItems == null)
			throw new Exception("����Դ��Ŀ�����ڻ���û����ȷ��ʼ����");
		boolean oMapFlag = false;
		String ret = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		ret += "<items>";
		for (int i = 0; i < dsItems.length; i++) {
			// �����������־����ô���������ʽxmlƬ��
			if (dsItems[i].getOpinionMap() != null && dsItems[i].getOpinionMap().length() > 0) {
				ret += "<item opinionmap=\"" + dsItems[i].getOpinionMap() + "\">";
				oMapFlag = true;
			} else
				ret += "<item>";
			// ����xmlƬ��
			ret += "<name>" + process4Xml(dsItems[i].getName() + "") + "</name>";
			// ������xmlƬ��
			ret += "<desc>" + process4Xml(dsItems[i].getDesc() + "") + "</desc>";
			// ��ֵxmlƬ��
			if (dsItems[i].getValue().trim().length() > 0) { // �������ֵ
				if (dsItems[i].getDelima() != null)
					ret += "<value delima=\"" + dsItems[i].getDelima() + "\">";
				else
					ret += "<value>";
				ret += dsItems[i].getValue();
			} else { // ���û����ֵ
				if (dsItems[i].getDelima() != null)
					ret += "<value delima=\"" + dsItems[i].getDelima() + "\">";
				else
					ret += "<value>";
			}
			ret += "</value>";
			ret += "</item>";
		}
		ret += "</items>";
		return ret;
	}

}
