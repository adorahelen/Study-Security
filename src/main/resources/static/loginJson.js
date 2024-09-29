const createButton = document.getElementById('login-btn');
if (createButton) {
    createButton.addEventListener('click', (event) => {
        event.preventDefault(); // 기본 버튼 클릭 동작 방지
        fetch(`/api/login`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username: document.getElementById('username').value, // username으로 수정
                password: document.getElementById('password').value, // password로 수정
            }),
        }).then((response) => {
            if (response.ok) {
                alert('Login successful.');
                location.replace(`/service`);
            } else {
                alert('Login failed: ' + response.statusText);
            }
        }).catch((error) => {
            console.error('Error during login:', error);
            alert('An error occurred. Please try again later.');
        });
    });
}