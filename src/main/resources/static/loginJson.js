 const createButton = document.getElementById('login-btn');
    if (createButton) {
        createButton.addEventListener('click', (event) => {
            fetch(`/api/login`, {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    title: document.getElementById('email').value,
                    content: document.getElementById('password').value,
                }),
            }).then((response) => {
                alert('try Login : Complete.');
                location.replace(`/login`);
            });
        });
    }
