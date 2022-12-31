package com.upm.refreshme.backend;

public class WebPage {
	private String id;
	private String categoria;
	private String name;
	private String ultimosCambios;
	private String url;

	public WebPage(String id, String categoria, String name, String string, String url) {
		this.id = id;
		this.categoria = categoria;
		this.name = name;
		this.ultimosCambios = string;
		this.url = url;
	}

	public WebPage(String nombre, String url, String categoria, String ultimosCambios) {
		this.name = nombre;
		this.url = url;
		this.categoria = categoria;
		this.ultimosCambios = ultimosCambios;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNombre() {
		return name;
	}

	public void setNombre(String name) {
		this.name = name;
	}

	public String getUltimosCambios() {
		return ultimosCambios;
	}

	public void setUltimosCambios(String ultimosCambios) {
		this.ultimosCambios = ultimosCambios;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
