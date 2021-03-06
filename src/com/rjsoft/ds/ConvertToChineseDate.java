
package com.rjsoft.ds;

import com.rjsoft.ds.DSItem;
import com.rjsoft.ds.FormatBase;

/**
 * 转换英文格式日期到中文格式日期的对象
 * @author zhangguiping@rongji.com
 * Created  2004-1-8
 */
public class ConvertToChineseDate implements FormatBase {
  private String rawin=null;    //传入的初始日期格式，其格式类似为：2004-01-08
  /**
   * @see com.rjsoft.ds.FormatBase#format(java.lang.String)
   */
  public String format(String raw) {
    rawin=raw;
    return convert();
  }

  /**
   * @see com.rjsoft.ds.FormatBase#format(com.rjsoft.ds.DSItem)
   */
  public DSItem format(DSItem item) {
    rawin=item.getValue();
    item.setValue(convert());
    return item;
  }
  /**
   * 转换英文格式日期到中文格式日期
   * 
   * @author zhangguiping@rongji.com
   * Created  2004-1-8
   */
  private String convert(){
    if (rawin==null) return null;
    if (rawin.equals("")) return "";
    String ret="";
    int iv;
    String cns[]={"○","一","二","三","四","五","六","七","八","九","十"};
    try{
      String arabicYear=rawin.substring(0,4);
      String arabicMonth=rawin.substring(5,7);
      String arabicDate=rawin.substring(8,10);
      //处理年
      for (int i=0;i<arabicYear.length();i++){
        ret+=cns[Integer.parseInt(String.valueOf(arabicYear.charAt(i)))];
      }
      ret+="年";
      //处理月
      iv=Integer.parseInt(arabicMonth);
      if (iv<=10) {
        ret+=cns[iv];
      }
      else{
        ret+=cns[10]+cns[iv-10];
      }
      ret+="月";
      //处理日期
      iv=Integer.parseInt(arabicDate);
      if (iv<=10) {
        ret+=cns[iv];
      }
      else if (iv>10 & iv<20){
        ret+=cns[10]+cns[iv-10];
      }
      else{
        if (iv-20==0) ret+=cns[2]+cns[10];
        else if ((iv-20)>0 && (iv-20)<10) ret+=cns[2]+cns[10]+cns[iv-20];
        else if (iv-30==0) ret+=cns[3]+cns[10];
        else if ((iv-30)>0 && (iv-30)<10) ret+=cns[3]+cns[10]+cns[iv-30];
      }
      ret+="日";
    }
    catch (NumberFormatException ne)
    {
      System.out.println("NumberFormatException error:"+ne.getMessage());
      ne.printStackTrace();
      return rawin;
    }
    catch (IndexOutOfBoundsException ie)
    {
      System.out.println("IndexOutOfBoundsException error:"+ie.getMessage());
      ie.printStackTrace();
      return rawin;
    }
    catch (Exception e)
    {
      System.out.println("NumberFormatException error:"+e.getMessage());
      e.printStackTrace();
      return rawin;
    }
    return ret;
  }
  /*
  //测试代码
  public static void main(String args[]){
    ConvertToChineseDate cndate=new ConvertToChineseDate();
    System.out.println("result="+cndate.format("2004-01-01"));
    System.out.println("result="+cndate.format("1990-10-31"));
    System.out.println("result="+cndate.format("2003-11-10"));
    System.out.println("result="+cndate.format("2004-12-11"));
    System.out.println("result="+cndate.format("2114-09-25"));
    System.out.println("result="+cndate.format("1002-09-20"));
    System.out.println("result="+cndate.format("2114-09-30"));
  }
  */
}
