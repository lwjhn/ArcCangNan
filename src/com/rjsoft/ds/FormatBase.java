package com.rjsoft.ds;

import com.rjsoft.ds.DSItem;

/**
 * ���ڸ�ʽ������Դ��ֵ�����ʽ��ͨ�ýӿڡ� <br>
 * ������Ҫ��ʽ�������ֵ�Ķ������ʵ�ִ˽ӿڡ�
 * 
 * @author zhangguiping@rongji.com Created 2004-1-8
 */
public interface FormatBase {
	/**
	 * ��ʽ���������ĳ�ʼֵ������
	 * 
	 * @param raw
	 *            ����ĳ�ʼֵ
	 * @return String����ʽ��������
	 */
	public String format(String raw);

	/**
	 * 
	 * @param item
	 *            ����ĳ�ʼ�����
	 * @return DSItem����ֵ����ʽ���DSItem����
	 */
	public DSItem format(DSItem item);
}
