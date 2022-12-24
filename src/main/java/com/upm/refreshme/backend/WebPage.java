package com.upm.refreshme.backend;

import java.util.Date;

public class WebPage {
	private String id;
	private String categoria;
	private String name;
	private String ultimosCambios;
	private String url;
	
	public WebPage(String id, String  categoria, String name, String string, String url) {
		this.id=id;
		this.categoria=categoria;
		this.name=name;
		this.ultimosCambios=string;
		this.url=url;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
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
