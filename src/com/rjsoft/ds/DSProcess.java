/*
 * Created on 2003-8-6
 */
package com.rjsoft.ds;
import lotus.domino.AgentBase;
import lotus.domino.Document;
import lotus.domino.Session;

import java.util.Vector;
/**
 * 用于处理数据源的对象接口，所有具体的处理对象要实现本接口
 * @author zhangguiping@rongji.com
 */
public interface DSProcess {
  /**
   * 实际处理数据源，以便数据源中的各域的实际值被正常初始化
   * @param dsItem  DSItem对象集合，代表所有的域
   * @param doc     Domino文档
   * @param context 环境，在这里具体指代理对象
   * @throws Exception  抛出异常
   */
  public void process(Vector dsItem,Document doc,AgentBase context)throws Exception;
  public void process(Vector dsItem,Document doc,Session session)throws Exception;
  /**
   * 输出数据源的XML文本
   * @param w Writer，表示XML文本输出对象
   */
  //public void buildResultXmlbak(Writer w)throws Exception;
  public void buildResultXml(java.io.PrintWriter pw)throws Exception;
  public String getResultXml()throws Exception;
}
