package com.tempera.util;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NekoUtil {
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class NekosLifeResponse {
		private String url;
		
		public String getUrl() {
			return url;
		}
		
		public String toString() {
			return url;
		}
	}
	
	public static final String NEKOS_LIFE_URL = "https://nekos.life/api/v2";
	public static final String CATBOYS_URL = "https://api.catboys.com";
	
	private static <T> T getResponse(String url, String header, Class<T> clazzType) {
		ObjectMapper mapper = new ObjectMapper();
		T res = null;
		try {
			res = mapper.readValue(new URL(url + header), clazzType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	private static String getUrl(String url, String header) {
		return getResponse(url, header, NekosLifeResponse.class).getUrl();
	}
	
	public static String getCat() {
		return getUrl(NEKOS_LIFE_URL, "/img/meow");
	}
	
	public static String getDog() {
		return getUrl(NEKOS_LIFE_URL, "/img/woof");
	}
	
	public static String getWaifu() {
		return getUrl(NEKOS_LIFE_URL, "/img/waifu");
	}
	
	public static String getFoxgirl() {
		return getUrl(NEKOS_LIFE_URL, "/img/fox_girl");
	}
	
	public static String getCatgirl() {
		return getUrl(NEKOS_LIFE_URL, "/img/neko");
	}
	
	public static String getCatboy() {
		return getUrl(CATBOYS_URL, "/img");
	}
}
