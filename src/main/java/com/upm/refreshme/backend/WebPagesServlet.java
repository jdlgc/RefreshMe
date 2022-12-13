package com.upm.refreshme.backend;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;

@WebServlet("/webPages")
public class WebPagesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void updateAttributes(HttpServletRequest request, String filtro) throws IOException {
        if (filtro != "") {
            // Busqueda en base de datos con filtro
        }

        // Mock data
        ArrayList<WebPage> webPages = new ArrayList<WebPage>();
        webPages.add(new WebPage("Ejemplo1", "www.ejemplo1.com", "comercio", new Date()));
        webPages.add(new WebPage("Ejemplo2", "www.ejemplo2.com", "revista", new Date()));
        webPages.add(new WebPage("Ejemplo3", "www.ejemplo3.com", "redSocial", new Date()));
        webPages.add(new WebPage("Ejemplo4", "www.ejemplo4.com", "noticias", new Date()));
        
        System.out.println("Firebase response - Web pages:" + Servlet.getWebPages());
        
//        that generates a exception because while the listener is still getting the web pages from firebase,
//        the page is already being buid:
//        Servlet.getWebPages().forEach((webPage) -> {
//        	webPages.add(webPage);
//        });

        request.setAttribute("data", webPages);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String url = request.getParameter("url");
        String categoria = request.getParameter("categoria");

        updateAttributes(request, "");
        Servlet.saveNewWebPage(new WebPage(nombre, url, categoria, new Date()));
        request.getRequestDispatcher("/WEB-INF/views/webPages.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filtro = request.getParameter("nombre");
        updateAttributes(request, filtro);
        request.getRequestDispatcher("/WEB-INF/views/webPages.jsp").forward(request, response);
    }
}
