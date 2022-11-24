package com.upm.refreshme.backend;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/WebPagesServlet")
public class WebPagesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String nombre = req.getParameter("nombre");
        String url = req.getParameter("url");
        String categoria = req.getParameter("categoria");

        System.out.println("nombre :: " + nombre);
        System.out.println("url :: " + url);
        System.out.println("categoria :: " + categoria);

        // use RequestDispatcher to forward request internally
        try {
            req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void goGet(HttpServletRequest req, HttpServletResponse resp) {

    }
}
