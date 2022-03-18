package com.rithsagea.test;

import com.rithsagea.util.NekoUtil;

public class NekoTest {
	public static void main(String[] args) {
		System.setProperty("http.agent", "Chrome");
		
		String url = NekoUtil.getCatgirl();
		
		System.out.println(url);
	}
}
