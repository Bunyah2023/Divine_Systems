// Función para crear una estrella
function crearEstrella() {
    const estrella = document.createElement('div');
    estrella.className = 'estrella'; // Usar className en lugar de classList.add para asignar la clase

    // Posicionamiento aleatorio
    estrella.style.left = Math.random() * window.innerWidth + 'px';
    estrella.style.top = Math.random() * window.innerHeight + 'px';

    // Tamaño aleatorio entre 2px y 8px
    const tamaño = Math.random() * (9 - 1) + 1; // Debería ser (8 - 2) + 2 para rangos de 2px a 8px
    estrella.style.width = tamaño + 'px';
    estrella.style.height = tamaño + 'px';

    // Agregar la estrella al contenedor (cuerpo del documento)
    document.body.appendChild(estrella);

    // Esta es la animación de parpadeo de cada una de las estrellas
    setInterval(() => {
        estrella.style.opacity = Math.random() > 0.5 ? '1' : '0';
    }, Math.random() * 1000 + 500); // Parpadeará entre 0.5 y 1.5 segundos

    // Eliminar la estrella después de 2 segundos para evitar sobrecarga del DOM
    setTimeout(() => {
        document.body.removeChild(estrella);
    }, 2000);
}

// Función para generar estrellas continuamente
function generarEstrellas() {
    setInterval(crearEstrella, 100); // Generar una estrella cada 100 milisegundos
}

// Llamar a la función para generar estrellas continuamente
generarEstrellas();

// Script para el astronauta

// Obtener referencia al astronauta
const astronauta = document.querySelector('.astronauta');

// Variables para controlar la dirección y la velocidad del movimiento
let velocidadX = 2;
let velocidadY = 2;

// Función para mover el astronauta libremente con rebote
function moverAstronauta() {
    // Obtener las coordenadas actuales del astronauta
    let posX = parseFloat(astronauta.style.left) || 0;
    let posY = parseFloat(astronauta.style.top) || 0;

    // Obtener las dimensiones de la ventana del navegador
    const ventanaAncho = window.innerWidth;
    const ventanaAlto = window.innerHeight;

    // Calcular sus nuevas coordenadas
    posX += velocidadX;
    posY += velocidadY;

    // Esto hace rebotar al astronauta con los bordes horizontales
    if (posX <= 0 || posX >= ventanaAncho - astronauta.clientWidth) {
        velocidadX = -velocidadX;
    }

    // Esto hace rebotar al astronauta con los bordes verticales
    if (posY <= 0 || posY >= ventanaAlto - astronauta.clientHeight) {
        velocidadY = -velocidadY;
    }

    // Aplicar las nuevas coordenadas
    astronauta.style.left = posX + 'px';
    astronauta.style.top = posY + 'px';
}

// Llamar a la función para mover el astronauta cada 10 milisegundos
setInterval(moverAstronauta, 10);
