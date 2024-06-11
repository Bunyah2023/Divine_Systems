document.addEventListener('DOMContentLoaded', function() {
    const registroForm = document.getElementById('registroForm');
    const nombres = document.getElementById('nombres');
    const apellido = document.getElementById('apellido');
    const apellidoMaterno = document.getElementById('apellidoMaterno');
    const correo = document.getElementById('correo');
    const contrasena = document.getElementById('contrasena');
    const repetirContrasena = document.getElementById('repetirContrasena');
    const registroTabla = document.getElementById('registroTabla')?.querySelector('tbody');

    if (registroForm) {
        registroForm.addEventListener('submit', function(event) {
            event.preventDefault();

            if (validateForm()) {
                saveData();
                lockInputs();
                setTimeout(() => {
                    window.location.href = 'index.html';
                }, 1000);
            }
        });
    }

    function validateForm() {
        if (!correo.value.endsWith('@utez.edu.mx')) {
            alert('El correo debe tener el dominio @utez.edu.mx');
            return false;
        }

        if (contrasena.value !== repetirContrasena.value) {
            alert('Las contraseñas no coinciden');
            return false;
        }

        if (isEmailRegistered(correo.value)) {
            alert('El correo ya está asociado a una cuenta creada');
            return false;
        }

        return true;
    }

    function isEmailRegistered(email) {
        let registros = JSON.parse(localStorage.getItem('registros')) || [];
        return registros.some(registro => registro.correo === email);
    }

    function saveData() {
        const registro = {
            nombres: nombres.value,
            apellido: apellido.value,
            apellidoMaterno: apellidoMaterno.value,
            correo: correo.value
        };

        let registros = JSON.parse(localStorage.getItem('registros')) || [];
        registros.push(registro);
        localStorage.setItem('registros', JSON.stringify(registros));

        if (registroTabla) {
            addRowToTable(registro);
        }
    }

    function addRowToTable(registro) {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${registro.nombres}</td>
            <td>${registro.apellido}</td>
            <td>${registro.apellidoMaterno}</td>
            <td>${registro.correo}</td>
            <td>
                <button class="modificar">Modificar</button>
                <button class="eliminar">Eliminar</button>
            </td>
        `;
        registroTabla.appendChild(row);
    }

    function eliminarRegistro(button) {
        const row = button.parentNode.parentNode;
        row.parentNode.removeChild(row);
        actualizarLocalStorage();
    }

    function actualizarRegistro(button) {
        const row = button.parentNode.parentNode;
        const inputs = row.querySelectorAll('td:not(:last-child)');
        const originalData = Array.from(inputs).map(td => td.textContent.trim());

        inputs.forEach(td => {
            const input = document.createElement('input');
            input.type = 'text';
            input.value = td.textContent.trim();
            td.textContent = '';
            td.appendChild(input);
        });

        const buttonsContainer = row.querySelector('td:last-child');
        buttonsContainer.innerHTML = `
            <button class="guardar">Guardar</button>
            <button class="cancelar">Cancelar</button>
        `;

        buttonsContainer.querySelector('.guardar').onclick = function() {
            guardarCambios(this, originalData);
        };
        buttonsContainer.querySelector('.cancelar').onclick = function() {
            cancelarCambios(this, originalData);
        };
    }

    function guardarCambios(button, originalData) {
        const row = button.parentNode.parentNode;
        const inputs = row.querySelectorAll('input');
        const newData = {
            nombres: inputs[0].value.trim() || inputs[0].defaultValue,
            apellido: inputs[1].value.trim() || inputs[1].defaultValue,
            apellidoMaterno: inputs[2].value.trim() || inputs[2].defaultValue,
            correo: inputs[3].value.trim() || inputs[3].defaultValue
        };

        inputs.forEach((input, index) => {
            const td = row.cells[index];
            td.textContent = input.value;
        });

        actualizarLocalStorage();

        const buttonsContainer = row.querySelector('td:last-child');
        buttonsContainer.innerHTML = `
            <button class="modificar">Modificar</button>
            <button class="eliminar">Eliminar</button>
        `;
        buttonsContainer.querySelector('.modificar').onclick = function() {
            actualizarRegistro(this);
        };
        buttonsContainer.querySelector('.eliminar').onclick = function() {
            eliminarRegistro(this);
        };
    }

    function cancelarCambios(button, originalData) {
        const row = button.parentNode.parentNode;
        const inputs = row.querySelectorAll('input');

        inputs.forEach((input, index) => {
            const td = row.cells[index];
            td.textContent = originalData[index];
        });

        const buttonsContainer = row.querySelector('td:last-child');
        buttonsContainer.innerHTML = `
            <button class="modificar">Modificar</button>
            <button class="eliminar">Eliminar</button>
        `;
        buttonsContainer.querySelector('.modificar').onclick = function() {
            actualizarRegistro(this);
        };
        buttonsContainer.querySelector('.eliminar').onclick = function() {
            eliminarRegistro(this);
        };
    }

    function actualizarLocalStorage() {
        const registros = [];
        registroTabla.querySelectorAll('tr').forEach(row => {
            const cols = row.querySelectorAll('td:not(:last-child)');
            if (cols.length > 0) {
                const registro = {
                    nombres: cols[0].textContent.trim(),
                    apellido: cols[1].textContent.trim(),
                    apellidoMaterno: cols[2].textContent.trim(),
                    correo: cols[3].textContent.trim()
                };
                registros.push(registro);
            }
        });
        localStorage.setItem('registros', JSON.stringify(registros));
    }

    function lockInputs() {
        nombres.disabled = true;
        apellido.disabled = true;
        apellidoMaterno.disabled = true;
        correo.disabled = true;
        contrasena.disabled = true;
        repetirContrasena.disabled = true;
        registroForm.querySelector('button[type="submit"]').disabled = true;
    }

    if (registroTabla) {
        let registros = JSON.parse(localStorage.getItem('registros')) || [];
        registros.forEach(addRowToTable);

        registroTabla.addEventListener('click', function(event) {
            if (event.target.classList.contains('eliminar')) {
                eliminarRegistro(event.target);
            } else if (event.target.classList.contains('modificar')) {
                actualizarRegistro(event.target);
            }
        });
    }
});
