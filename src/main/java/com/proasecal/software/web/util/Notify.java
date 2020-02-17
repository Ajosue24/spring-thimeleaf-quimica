package com.proasecal.software.web.util;

public class Notify {
	
	private String title;
	private String message;
	private String type;
	
	
	public Notify(String title, String type, String message) {
		this.title = title;
		this.type = type;
		this.message = message;
	}
	
	public static Notify SUCCESS(String titulo,  String message) {
		return new Notify(titulo, "success", message);
	}
	
	public static Notify ERROR(String titulo,  String message) {
		return new Notify(titulo, "error", message);
	}
	
	public static Notify INFO(String titulo,  String message) {
		return new Notify(titulo, "info", message);
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public String getType() {
		return type;
	}
	

}
