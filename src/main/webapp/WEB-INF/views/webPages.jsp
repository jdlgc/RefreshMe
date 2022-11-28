<%@page import="com.upm.refreshme.backend.WebPage"%>
<%@page import="java.util.ArrayList"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>RefreshMe</title>

        <style>
            .container {
                text-align: center;
                margin-top: 20px;
            }
            .footer-dark {
                padding:50px 0;
                color:#f0f9ff;
                background-color:#000000;
                position:absolute;
                bottom:0;
                width:100%;
            }

            .footer-dark h3 {
                text-transform: uppercase;
                margin-top:0;
                margin-bottom:12px;
                font-weight:bold;
                font-size:16px;
            }

            .footer-dark .item.text p {
                opacity:0.6;
                margin-bottom:0;
            }
        </style>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    </head>

    <body>
        <nav class="navbar navbar-dark navbar-expand-lg bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">RefreshMe</a>

                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                Mi usuario
                            </a>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="configureProfile">Configurar</a></li>
                                <li><a class="dropdown-item" href="logIn">Desconectarse</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container">
            <a type="button" class="btn btn-dark" href="addWebPage">A&ntildeadir p&aacutegina web</a>
        </div>

        <div class="container">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th scope="col">Nombre</th>
                    <th scope="col">URL</th>
                    <th scope="col">Categor&iacutea</th>
                    <th scope="col">&Uacuteltimos cambios</th>
                </tr>
                </thead>
                <tbody>
                    <%ArrayList<WebPage> data = (ArrayList<WebPage>)request.getAttribute("data");
                        for(WebPage wp:data){%>
                    <tr>
                        <td><%=wp.getNombre()%></td>
                        <td><%=wp.getUrl()%></td>
                        <td><%=wp.getCategoria()%></td>
                        <td><%=wp.getUltimosCambios()%></td>
                    </tr>
                    <%}%>
                </tbody>
            </table>
        </div>

        <!-- Footer -->
        <div class="footer-dark">
            <footer>
                <div class="container">
                    <h3>About us</h3>
                    <p> RefreshMe es un gestor de p&aacuteginas web implementado por el equipo 1 de la asignatura
                        Profundizaci&oacuten en Ingenier&iacutea del Software del Master Universitario en
                        Ingenier&iacutea Inform&aacutetica de la Universidad Polit&eacutecnica de Madrid.
                    </p>
                    <p>
                        <a href="https://github.com/jdlgc/RefreshMe">GitHub link</a>
                    </p>
                </div>
            </footer>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
    </body>
</html>
