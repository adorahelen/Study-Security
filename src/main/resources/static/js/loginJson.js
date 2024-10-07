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
                username: document.getElementById('username').value, // 유저네임 입력값
                password: document.getElementById('password').value, // 패스워드 입력값
            }),
        }).then((response) => {
            if (response.ok) {
                // Authorization 헤더에서 토큰을 가져옵니다.
                const accessToken = response.headers.get("Authorization");
                if (accessToken) {
                    // 'Bearer ' 이후의 문자열을 추출합니다.
                    const token = accessToken.substr(7);
                    // 액세스 토큰을 로컬 스토리지에 저장
                    localStorage.setItem('access_token', token);

                    alert('Login successful.');
                    location.replace(`/articles?token=`+token)
                } else {
                    alert('Login failed: Access token not received.');
                }
            } else {
                alert('Login failed: ' + response.statusText);
                return Promise.reject('Login failed');
            }
        }).catch((error) => {
            console.error('Error during login:', error);
            alert('An error occurred. Please try again later.');
        });
    });
}