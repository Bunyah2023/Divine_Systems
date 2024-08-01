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

    const hamburgerMenu = document.getElementById('hamburger-menu');
    const sidebar = document.getElementById('sidebar');

    // Muestra/Oculta la barra lateral al hacer clic en el menú de hamburguesa
    hamburgerMenu.addEventListener('click', function (event) {
        event.stopPropagation(); // Evita que el clic se propague al body
        if (sidebar.style.left === '-250px' || sidebar.style.left === '') {
            sidebar.style.left = '0';
        } else {
            sidebar.style.left = '-250px';
        }
    });

    // Cierra la barra lateral al hacer clic fuera de ella
    document.body.addEventListener('click', function (event) {
        if (sidebar.style.left === '0px' && !sidebar.contains(event.target) && event.target !== hamburgerMenu) {
            sidebar.style.left = '-250px';
        }
    });

    // Permitir que la barra lateral permanezca abierta al hacer clic dentro de ella
    sidebar.addEventListener('click', function (event) {
        event.stopPropagation();
    });
});