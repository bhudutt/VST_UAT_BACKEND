package com.hitech.dms.app.designpattern.dynamicproxy;

import java.math.BigDecimal;

public class PayServiceImpl implements PayService {

	@Override
	public void pay(String username, BigDecimal money) {
		System.out.println(username + "paid" + money + "inr");
	}

	@Override
	public void a() {
		System.out.println("a");
	}

	@Override
	public void b() {
		System.out.println("b");
	}

}