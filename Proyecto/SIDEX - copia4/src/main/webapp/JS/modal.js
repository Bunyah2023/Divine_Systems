// Obtener el modal
var modal = document.getElementById("myModal");

// Obtener el botón que abre el modal
var btn = document.getElementById("openModalBtn");

// Obtener los elementos <span> y los botones que cierran el modal
var span = document.getElementsByClassName("close")[0];
var cancelBtn = document.getElementById("cancelBtn");
var acceptBtn = document.getElementById("acceptBtn");

// Cuando el usuario hace clic en el botón, abre el modal
btn.onclick = function() {
    modal.style.display = "block";
}

// Cuando el usuario hace clic en <span> (x), cierra el modal
span.onclick = function() {
    modal.style.display = "none";
}

// Cuando el usuario hace clic en el botón "Cancelar", cierra el modal
cancelBtn.onclick = function() {
    modal.style.display = "none";
}

// Cuando el usuario hace clic en el botón "Aceptar", muestra un mensaje o realiza alguna acción
acceptBtn.onclick = function() {
    alert("Acción aceptada");
    modal.style.display = "none";
}

// Cuando el usuario hace clic en cualquier lugar fuera del modal, cierra el modal
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
