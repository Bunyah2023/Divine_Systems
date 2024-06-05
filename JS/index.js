// JavaScript para manejar la visualización de la ventana modal
document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('modal');
    const addClassBtn = document.getElementById('add-class-btn-span');
    const closeBtn = document.querySelector('.modal .close');
    const cancelBtn = document.getElementById('cancel-btn');

    addClassBtn.addEventListener('click', function () {
        modal.style.display = 'flex'; // Mostrar la ventana modal
    });

    closeBtn.addEventListener('click', function () {
        modal.style.display = 'none'; // Ocultar la ventana modal
    });

    cancelBtn.addEventListener('click', function () {
        modal.style.display = 'none'; // Ocultar la ventana modal
    });

    // Ocultar la ventana modal si se hace clic fuera de ella
    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            modal.style.display = 'none'; // Ocultar la ventana modal
        }
    });
    
});
