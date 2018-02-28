package com.example.app.bean.weather;

import java.util.List;

/**
 * Created by Hal on 15/4/26.
 */
public class WeatherListData {
	private String message;
	private String cod;
	private int count;
	private List<WeatherData> list;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<WeatherData> getList() {
		return list;
	}
	public void setList(List<WeatherData> list) {
		this.list = list;
	}
	
}
