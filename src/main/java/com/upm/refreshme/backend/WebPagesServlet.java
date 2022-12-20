package com.upm.refreshme.backend;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.Timestamp;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/webPages")
public class WebPagesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    Servlet servlet = new Servlet();

    protected void updateAttributes(HttpServletRequest request, String filtro) {
        if (filtro != "") {
            // Busqueda en base de datos con filtro
        }

        ArrayList<WebPage> webPages = servlet.getWebPagesList(filtro);

        request.setAttribute("data", webPages);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String url = request.getParameter("url");
        String categoria = request.getParameter("categoria");
        
        
        servlet.addNewWebPage(new WebPage(nombre, url, categoria, Timestamp.now().toString()));

        updateAttributes(request, "");
        request.getRequestDispatcher("/WEB-INF/views/webPages.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filtro = request.getParameter("nombre");
        updateAttributes(request, filtro);
        request.getRequestDispatcher("/WEB-INF/views/webPages.jsp").forward(request, response);
    }
}
