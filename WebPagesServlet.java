import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/webPages")
public class WebPagesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void updateAttributes(HttpServletRequest request, String filtro) {
        if (filtro != "") {
            // Busqueda en base de datos con filtro
        }

        // Mock data
        ArrayList<WebPage> webPages = new ArrayList<WebPage>();
        webPages.add(new WebPage("Ejemplo1", "www.ejemplo1.com", "comercio", "03/08/2021"));
        webPages.add(new WebPage("Ejemplo2", "www.ejemplo2.com", "revista", "12/10/2022"));
        webPages.add(new WebPage("Ejemplo3", "www.ejemplo3.com", "redSocial", "03/07/2021"));
        webPages.add(new WebPage("Ejemplo4", "www.ejemplo4.com", "noticias", "02/11/2021"));

        request.setAttribute("data", webPages);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String url = request.getParameter("url");
        String categoria = request.getParameter("categoria");

        System.out.println("nombre :: " + nombre);
        System.out.println("url :: " + url);
        System.out.println("categoria :: " + categoria);

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
