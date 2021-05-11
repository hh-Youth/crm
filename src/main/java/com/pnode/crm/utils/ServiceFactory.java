package com.pnode.crm.utils;

public class ServiceFactory {
	
	public static Object getService(Object service){
		System.out.println("getService");
		return new TransactionInvocationHandler(service).getProxy();
		
	}
	
}
