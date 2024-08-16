document.getElementById('login-form').addEventListener('submit', function(event) {
    event.preventDefault();
    // Aquí puedes agregar la lógica para manejar la autenticación
    alert('Iniciar sesión no está implementado.');
});

const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const submitButton = document.querySelector('button');

function validateInputs() {
    submitButton.disabled = !(emailInput.value && passwordInput.value);
}

emailInput.addEventListener('input', validateInputs);
passwordInput.addEventListener('input', validateInputs);

validateInputs();
