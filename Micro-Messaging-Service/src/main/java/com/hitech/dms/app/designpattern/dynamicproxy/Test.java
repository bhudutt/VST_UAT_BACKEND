package com.hitech.dms.app.designpattern.dynamicproxy;

import java.math.BigDecimal;

public class Test {

    public static void main(String[] args) {
//        PayService service = new PayServiceImpl();
//        PayProxy payProxy = new PayProxy(service);
//        PayService payService = (PayService) payProxy.getProxy();
//        payService.pay("Dinesh Jak", BigDecimal.TEN);

		/*
		 * PayProxy payProxy2 = new PayProxy(new PayServiceImpl()); PayServiceImpl
		 * payService2 = (PayServiceImpl) payProxy2.getPayProxy(); payService2 . pay (
		 * "Dinesh Jak 2" , BigDecimal . TEN );
		 */
    	
		/*
		 * int x = 10; int y = 20; int z = x += y -= x += y; System.out.println(z);
		 */  
    	String followup="13-Sept-2024";
    	String followEod="13-Nov-2024";
    	
    	String date=followup;
    	String[] arr=date.split("-");
    	String month=arr[1].toString();
    	System.out.println(month);
    	String newDate=month.length()>3?month.substring(0, 3):month;
    	String followupdate=arr[0]+"-"+newDate+"-"+arr[2];
    	System.out.println(followupdate);
    	
    	
    	String dateeod=followEod;
    	String[] arreod=dateeod.split("-");
    	String montheod=arreod[1].toString();
    	System.out.println(montheod);
    	String newDateeod=montheod.length()>3?montheod.substring(0, 3):montheod;
    	System.out.println(newDateeod);
    	String followupEod=arreod[0]+"-"+newDateeod+"-"+arreod[2];
    	System.out.println(followupEod);
    	
    
    }

}
