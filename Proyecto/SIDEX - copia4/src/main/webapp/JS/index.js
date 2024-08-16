document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('modal');
    const addClassBtn = document.getElementById('add-class-btn-span');
    const closeBtn = document.querySelector('#modal .close');
    const cancelBtn = document.getElementById('cancel-btn');
    const form = document.getElementById('join-class-form');
    const classCodeInput = document.getElementById('class-code');

    addClassBtn.addEventListener('click', function () {
        modal.style.display = 'flex'; // Mostrar la ventana modal
    });

    closeBtn.addEventListener('click', function () {
        modal.style.display = 'none'; // Ocultar la ventana modal
    });

    cancelBtn.addEventListener('click', function () {
        modal.style.display = 'none'; // Ocultar la ventana modal
    });

    // Formatear el código de clase mientras se escribe
    classCodeInput.addEventListener('input', function (e) {
        let input = e.target.value.replace(/-/g, ''); // Eliminar guiones si existen
        if (input.length > 3) input = input.slice(0, 3) + '-' + input.slice(3);
        if (input.length > 7) input = input.slice(0, 7) + '-' + input.slice(7);
        e.target.value = input.slice(0, 11); // Limitar la longitud total a 11 caracteres
    });

    form.addEventListener('submit', function (event) {
        // Formatear el código de clase antes de enviarlo
        const formattedCode = classCodeInput.value.replace(/-/g, '').replace(/(.{3})(?=.)/g, '$1-');
        classCodeInput.value = formattedCode;
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
