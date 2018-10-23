package com.jingtum.chainApp.util;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author wslai
 * 随机数处理公用类
 */
public class RandomUtils {
	// 生产随机数，字符方式，max为最大随机,字符串的位数等于最大值的位数，前面 补0
	public static String getRandomSequence(int max)  
	{  
		String str="";
		int len=String.valueOf(max).length();
    	java.util.Random random =new java.util.Random();
        int num = random.nextInt(max+1);  
        if(String.valueOf(num).length()<len){
        	str = String.format("%0"+len+"d", num);
        }else{
        	str=String.valueOf(num);
        }
	    return str;    
	} 
	
	// 生产随机数，数字方式，max为最大随机
	public static int getRandom(int max)  
	{  
    	java.util.Random random =new java.util.Random();
        int num = random.nextInt(max+1);  
	    return num;    
	} 
	
	/**
	 * 获取最大为max，不在arrayNums中的随机数
	 * @param max
	 * @param arrayNums
	 * @return
	 */
	public static int getNotrepeatRandom(int max,String[]arrayNums){
		int num=-1;
		if(arrayNums!=null && arrayNums.length>0){
			HashMap<String,Integer> mapNum=new HashMap<String,Integer>();
			for (int i=0;i<arrayNums.length;i++){
				mapNum.put(arrayNums[i], Integer.parseInt(arrayNums[i]));
			}
			ArrayList<Integer> lst=new ArrayList<Integer>();
			for(int i=0;i<=max;i++){
				if(!mapNum.containsKey(String.valueOf(i))){
					lst.add(i);
				}
			}
			if(lst.size()>0){
				int idx=getRandom(lst.size()-1);
				num=lst.get(idx);
			}
		}else{
			num=getRandom(max);
		}
		return num;
	}
	
	public static void main(String[] args) {
		ArrayList lst=new ArrayList();
		for(int i=0;i<100;i++){
			String[] astr=null;
			if(lst.size()>0){
				astr=new String[lst.size()];
				for(int j=0;j<lst.size();j++){
					astr[j]=lst.get(j).toString();
				}
			}
			int tem= getNotrepeatRandom(99,astr);
			lst.add(tem);
			System.out.println(tem);
		}
	}
}
