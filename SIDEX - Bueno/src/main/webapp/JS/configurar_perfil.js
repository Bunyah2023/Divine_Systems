document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('modal');
    const addClassBtn = document.getElementById('add-class-btn-span');
    const closeBtn = document.querySelector('.modal .close');
    const cancelBtn = document.getElementById('cancel-btn');
    const saveChangesBtn = document.getElementById('save-changes-btn');

    addClassBtn.addEventListener('click', function () {
        modal.style.display = 'flex';
    });

    closeBtn.addEventListener('click', function () {
        modal.style.display = 'none';
    });

    cancelBtn.addEventListener('click', function () {
        modal.style.display = 'none';
    });

    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

    const hamburgerMenu = document.getElementById('hamburger-menu');
    const sidebar = document.getElementById('sidebar');

    hamburgerMenu.addEventListener('click', function () {
        if (sidebar.style.left === '-250px') {
            sidebar.style.left = '0';
        } else {
            sidebar.style.left = '-250px';
        }
    });

    document.addEventListener('click', function (event) {
        if (sidebar.style.left === '0px' && !sidebar.contains(event.target) && event.target !== hamburgerMenu) {
            sidebar.style.left = '-250px';
        }
    });

    saveChangesBtn.addEventListener('click', function () {
        const profilePic = document.getElementById('upload-photo').files[0];
        const emailNotifications = document.getElementById('email-notifications').checked;
        const currentPassword = document.getElementById('current-password').value;
        const newPassword = document.getElementById('new-password').value;
        const confirmPassword = document.getElementById('confirm-password').value;
        const language = document.getElementById('language-select').value;
        const visibility = document.querySelector('input[name="visibility"]:checked').value;

        const formData = new FormData();
        formData.append('profilePic', profilePic);
        formData.append('emailNotifications', emailNotifications);
        formData.append('currentPassword', currentPassword);
        formData.append('newPassword', newPassword);
        formData.append('confirmPassword', confirmPassword);
        formData.append('language', language);
        formData.append('visibility', visibility);

        fetch('URL_DE_TU_API_ENDPOINT', {
            method: 'POST',
            body: formData,
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Cambios guardados con éxito.');
            } else {
                alert('Error al guardar cambios: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al guardar cambios.');
        });
    });
});
