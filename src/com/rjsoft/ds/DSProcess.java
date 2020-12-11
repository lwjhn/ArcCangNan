/*
 * Created on 2003-8-6
 */
package com.rjsoft.ds;
import lotus.domino.AgentBase;
import lotus.domino.Document;
import lotus.domino.Session;

import java.util.Vector;
/**
 * ���ڴ�������Դ�Ķ���ӿڣ����о���Ĵ������Ҫʵ�ֱ��ӿ�
 * @author zhangguiping@rongji.com
 */
public interface DSProcess {
  /**
   * ʵ�ʴ�������Դ���Ա�����Դ�еĸ����ʵ��ֵ��������ʼ��
   * @param dsItem  DSItem���󼯺ϣ��������е���
   * @param doc     Domino�ĵ�
   * @param context ���������������ָ�������
   * @throws Exception  �׳��쳣
   */
  public void process(Vector dsItem,Document doc,AgentBase context)throws Exception;
  public void process(Vector dsItem,Document doc,Session session)throws Exception;
  /**
   * �������Դ��XML�ı�
   * @param w Writer����ʾXML�ı��������
   */
  //public void buildResultXmlbak(Writer w)throws Exception;
  public void buildResultXml(java.io.PrintWriter pw)throws Exception;
  public String getResultXml()throws Exception;
}
