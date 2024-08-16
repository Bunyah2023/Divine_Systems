// CrearClase.js

document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('modal');
    const botonCrearClase = document.getElementById('create-class-btn');
    const botonCerrar = document.querySelector('.close');
    const formularioCrearClase = document.getElementById('create-class-form');
    const botonCancelar = document.querySelector('.btn-cancel');

    // Abrir modal
    botonCrearClase.addEventListener('click', function() {
        modal.style.display = 'block';
    });

    // Cerrar modal
    function cerrarModal() {
        modal.style.display = 'none';
        formularioCrearClase.reset();
    }

    botonCerrar.addEventListener('click', cerrarModal);
    botonCancelar.addEventListener('click', cerrarModal);

    // Cerrar modal si se hace clic fuera
    window.addEventListener('click', function(evento) {
        if (evento.target == modal) {
            cerrarModal();
        }
    });

    // Manejar envío del formulario
    formularioCrearClase.addEventListener('submit', function(e) {
        e.preventDefault();

        const datosFormulario = new FormData(formularioCrearClase);
        const datosClase = {
            nombre: datosFormulario.get('class-name'),
            descripcion: datosFormulario.get('class-description'),
            fechaInicio: datosFormulario.get('start-date'),
            fechaFin: datosFormulario.get('end-date'),
            calificaciones: {
                AU: datosFormulario.get('grade-au'),
                DE: datosFormulario.get('grade-de'),
                SA: datosFormulario.get('grade-sa'),
                NA: datosFormulario.get('grade-na')
            }
        };

        // Enviar datos al servidor
        fetch('/crearClase', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(datosClase)
        })
            .then(respuesta => respuesta.json())
            .then(datos => {
                if (datos.exito) {
                    alert('Clase creada exitosamente');
                    cerrarModal();
                    // Aquí puedes agregar código para actualizar la lista de clases en la página
                } else {
                    alert('Error al crear la clase: ' + datos.mensaje);
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                alert('Error al crear la clase. Por favor, intenta de nuevo.');
            });
    });

    // Función para actualizar la fecha actual
    function actualizarFechaActual() {
        const elementoFechaActual = document.getElementById('current-date');
        const opciones = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
        const fechaActual = new Date().toLocaleDateString('es-ES', opciones);
        elementoFechaActual.textContent = fechaActual;
    }

    // Actualizar la fecha al cargar la página
    actualizarFechaActual();

    // Actualizar la fecha cada minuto
    setInterval(actualizarFechaActual, 60000);
});