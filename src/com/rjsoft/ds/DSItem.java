/*
 * Created on 2003-8-6
 */
package com.rjsoft.ds;

/**
 * ����Դ����󣬴�������Դ�е�һ��������һ������Դ�а����������Դ��
 * <br>���¼��Ϊ���򡱻��ߡ������
 * <br>��������Ե�˵����������ο��������е�˵��
 * @author coca
 */
public class DSItem {
  private String name=null;           //������
  private String desc=null;           //��������������Ϣ
  private String opinionMap=null;     //��ʾƥ��������ť���ƣ�������Դ����xml�ĵ���item�ڵ�������opinionmap����
  private String value=null;          //��ֵ
  private String delima=null;         //��ֵ��ķָ�����������Դ����xml�ĵ���value�ڵ�������delima����
  private String format=null;         //ֵ�ĸ�ʽ���������ƣ�������Դ����xml�ĵ���value�ڵ�������format����
  private String function=null;
  
  /**
   * ȱʡ������
   */
  public DSItem(){}
  /**
   * ��������������������������ֵ�����ӳ��
   * @param n String  ��ʾ�����ƣ�һ�����ĵ��е������ƶ�Ӧ�����磺Subject����������������д�����Ӧ��Ӣ�����ƣ����磺ǩ����ӦSign
   * @param d String  ��������������Ϣ�����磺�ļ�����
   * @param v String  ��ֵ������ע���������£�<br>
   * <ul>
   * <li>&lt;value&gt;&lt;/value&gt;����ʾȱʡ������������Ϊֵ����Դ��ʵ�ʴ�������Դʱϵͳ�Զ����ĵ���ȡ�����ƶ�Ӧ��ֵ</li>
   * <li>&lt;value&gt;<b>Domino��ʽ</b>&lt;/value&gt;����ʾȱʡ����Domino��ʽ��������Ϊֵ����Դ��ʵ�ʴ�������Դʱϵͳ���㹫ʽ�Ľ��</li>
   * </ul>
   * ����Դ������Ϣxml�ļ���Ԥ����󷵻ص������õ�ֵ���߹�ʽ
   * ���ص�����Դ����ʽ�����ֵ���ǽ���Ľ��
   * @param o String  ��ʾƥ��������ť����<br>
   * ����ָʾ�İ쵥����д�����־��ƥ���Լ�ָ������Ϊ������������Ҫ���⴦��,���Ǹ���ѡ����,����ʱֵֻ��Ϊ��
   */
  public DSItem(String n,String d,String v,String o){
    this.name=n;
    this.desc=d;
    this.value =v;
    this.opinionMap =o;
  }
  
	/**
	 * @return
	 */
	public String getDesc() {
		return desc;
	}

	/**
   * ������Դ����xml�ĵ���item�ڵ�������opinionmap���ԣ���ʾ�������˵��
	 * @return
	 */
	public String getOpinionMap() {
		return opinionMap;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param string
	 */
	public void setDesc(String string) {
		desc = string;
	}

	/**
	 * @param string
	 */
	public void setOpinionMap(String string) {
		opinionMap = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setValue(String string) {
		value = string;
	}

  /**
   * ���ض�ֵ��ķָ�����������Դ����xml�ĵ���value�ڵ�������delima����
   * ��ѡ����
   * @return
   */
  public String getDelima() {
    return delima;
  }

  /**
   * @param string
   */
  public void setDelima(String string) {
    delima = string;
  }

  /**
   * ����ֵ�ĸ�ʽ���������ƣ�������Դ����xml�ĵ���value�ڵ�������format���ԣ�����com.rjsoft.oa.ds.ConvertToChineseDate
   * ��ѡ����
   * @return String
   */
  public String getFormat() {
    return format;
  }

  /**
   * @param string
   */
  public void setFormat(String string) {
    format = string;
  }
  
  public String getFunction(){
	  return function;
  }
  
  public void setFunction(String string){
	  function = string;
  }
}
