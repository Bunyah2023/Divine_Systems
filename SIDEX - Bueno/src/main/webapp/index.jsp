<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIDEX</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/index.css">

</head>
<body class="froid">
<header>
    <div id="hamburger-menu">☰</div>
    <div id="sidebar" class="sidebar">
        <ul>
            <li><a href="#">Mis cursos</a></li>
            <li><a href="#">Pendientes</a></li>
            <li><a href="#">Examenes</a></li>
            <li><a href="#">Más...</a></li>
        </ul>
    </div>

    <div class="logo">
        <img id="logoDIVINESYSTEMS" src="IMG/LOGO SIDEX by DIVINE SYSTEMS.png" alt="DIVINE SYSTEMS">
    </div>
    <nav>
        <ul>
            <li><a href="#">Mis Cursos</a></li>
            <li><a href="#">Tareas</a></li>
            <li><a href="#">Cursos</a></li>
            <li><a href="#">Carreras</a></li>
            <li><a href="#">Más</a></li>
        </ul>
    </nav>
    <div class="right-section">
        <span class="icon">🔍</span>
        <span class="icon">❓</span>
        <span class="icon">🔔</span>
        <span id="add-class-btn-span" class="icon">+</span>
        <div class="fotoPerfil">
            <a href="configurar_perfil.jsp">
                <img src="IMG/PICTURE_PROFILE_DEFAULT.png" alt="Foto de perfil" class="user-info">
            </a>
        </div>
    </div>
</header>
<div class="sidebar" id="sidebar">
    <ul>
        <li><a href="#">Mis Cursos</a></li>
        <li><a href="#">Tareas</a></li>
        <li><a href="#">Cursos</a></li>
        <li><a href="#">Carreras</a></li>
        <li><a href="#">Más</a></li>
    </ul>
</div>
</header>
<div class="sidebar" id="sidebar">
    <ul>
        <li><a href="#">Mis Cursos</a></li>
        <li><a href="#">Tareas</a></li>
        <li><a href="#">Cursos</a></li>
        <li><a href="#">Carreras</a></li>
        <li><a href="#">Más</a></li>
    </ul>
</div>
</header>

<main>
    <div class="notifications">
        <div class="alert" id="netacad-maintenance">Bienvenido a SIDEX</div>
        <div class="alert" id="skillsforall-maintenance">¡Un buen estudiante no descuida sus deberes!</div>
        <div class="alert" id="current-date"></div>
    </div>
    <section class="learning">
        <h1>Mis cursos...</h1>
        <div class="cursos">
            <div class="curso en-progreso">
                <div class="curso-header">
                    <span class="star">⭐</span>
                    <span class="status">En Progreso</span>
                </div>
                <h2>SISTEMAS OPERATIVOS</h2>
                <p>Universidad Tecnológica Emiliano Zapata</p>
                <div class="curso-content">
                    <div class="curso-icon">

                    </div>
                    <div class="curso-details">
                        <p>Maestro: HDADKSHDKSJ</p>
                        <p>Por favor termina antes del 07 Sep 2024</p>
                    </div>
                </div>
            </div>
            <div class="curso terminado">
                <div class="curso-header">
                    <span class="star">⭐</span>
                    <span class="status">Terminado</span>
                </div>
                <h2>APLICACIONES WEB</h2>
                <p>Interconexión de Redes</p>
                <p>Universidad Tecnológica del Sur del Estado de Morelos</p>
                <p>Maestro: HDADKSHDKSJ</p>
                <p>06 Ene - 30 Abr 2024</p>
                <p class="error">No completaste todos los requisitos.</p>
            </div>
            <!-- Curso 1 -->
            <div class="curso en-progreso">
                <div class="curso-header">
                    <span class="star">⭐</span>
                    <span class="status">En Progreso</span>
                </div>
                <h2>BASE DE DATOS PARA APLICACIONES</h2>
                <p>Universidad Tecnologica Emiliano Zapata</p>
                <p>Maestro: HDADKSHDKSJ</p>
                <div class="curso-content">
                    <div class="curso-icon">

                    </div>
                    <div class="curso-details">
                        <p>Socio: Cisco</p>
                        <p>Por favor termina antes del 01 Dic 2024</p>
                    </div>
                </div>
            </div>
            <!-- Curso 2 -->
            <div class="curso en-progreso">
                <div class="curso-header">
                    <span class="star">⭐</span>
                    <span class="status">En Progreso</span>
                </div>
                <h2>CÁLCULO DIFERENCIAL</h2>
                <p>Universidad Tecnologica Emiliano Zapata</p>
                <p>Maestro: HDADKSHDKSJ</p>
                <div class="curso-content">
                    <div class="curso-icon">

                    </div>
                    <div class="curso-details">
                        <p>Socio: Cisco</p>
                        <p>Por favor termina antes del 15 Nov 2024</p>
                    </div>
                </div>
            </div>
            <!-- Curso 3 -->
            <div class="curso terminado">
                <div class="curso-header">
                    <span class="star">⭐</span>
                    <span class="status">Terminado</span>
                </div>
                <h2>PROBABILIDAD Y ESTADÍSTICA</h2>
                <p>Universidad Tecnologica Emiliano</p>
                <p>Maestro: HDADKSHDKSJ</p>
                <p>01 Mar - 30 May 2024</p>
                <p class="error">No completaste todos los requisitos.</p>
            </div>
            <!-- Curso 4 -->
            <div class="curso en-progreso">
                <div class="curso-header">
                    <span class="star">⭐</span>
                    <span class="status">En Progreso</span>
                </div>
                <h2>OE</h2>
                <p>Universidad Tecnológica Emiliano Zapata</p>
                <p>Maestro: HDADKSHDKSJ</p>
                <div class="curso-content">
                    <div class="curso-icon">

                    </div>
                    <div class="curso-details">
                        <p>Socio: Cisco</p>
                        <p>Por favor termina antes del 20 Dic 2024</p>
                    </div>
                </div>
            </div>
            <!-- Curso 5 -->
            <div class="curso terminado">
                <div class="curso-header">
                    <span class="star">⭐</span>
                    <span class="status">Terminado</span>
                </div>
                <h2>INGLÉS III</h2>
                <p>Universidad Tecnologica Emiliano Zapata</p>
                <p>Maestro: HDADKSHDKSJ</p>
                <p>15 Feb - 15 Abr 2024</p>
                <p class="error">No completaste todos los requisitos.</p>
            </div>
            <!-- Curso 6 -->
            <div class="curso en-progreso">
                <div class="curso-header">
                    <span class="star">⭐</span>
                    <span class="status">En Progreso</span>
                </div>
                <h2>INTEGRADORA 1</h2>
                <p>Universidad Tecnologica Emiliano Zapata</p>
                <p>Maestro: HDADKSHDKSJ</p>
                <div class="curso-content">
                    <div class="curso-icon">

                    </div>
                    <div class="curso-details">
                        <p>Por favor termina antes del 01 Ene 2025</p>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<!-- Ventana Modal -->
<div id="modal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <h2>Agregar Clase</h2>
        <input type="text" id="class-code" placeholder="Ingrese el código de la clase">
        <button id="accept-btn">Aceptar</button>
        <button id="cancel-btn">Cancelar</button>
    </div>
</div>
<script src="JS/index.js"></script>
</body>
</html>