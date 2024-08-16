document.addEventListener('DOMContentLoaded', function() {
    // Habilitar inputs al hacer clic en "Modificar"
    document.querySelectorAll('.edit-btn').forEach(button => {
        button.addEventListener('click', function() {
            const form = this.closest('form');
            const inputs = form.querySelectorAll('input, select');

            inputs.forEach(input => {
                if (input.name !== 'id') { // No habilitar el campo de ID
                    input.disabled = false;
                }
            });

            // Cambiar el botón para que permita guardar
            this.textContent = 'Guardar';
            this.type = 'submit';
            this.classList.remove('btn-warning');
            this.classList.add('btn-success');
        });
    });

    // Manejar eliminación de usuario con el modal
    let userIdToDelete = null;
    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function() {
            userIdToDelete = this.getAttribute('data-id');
            $('#deleteModal').modal('show');
        });
    });

    document.getElementById('confirmDelete').addEventListener('click', function() {
        if (userIdToDelete !== null) {
            window.location.href = `eliminarUsuario?id=${userIdToDelete}`;
        }
    });
});
