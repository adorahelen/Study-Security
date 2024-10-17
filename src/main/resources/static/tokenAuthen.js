// // 아직은 안쓰는 코드, (아티클 베이스)
//
// // * 1. 인증 파트
// const token = "받은 accessToken"; // 서버에서 받은 JWT
//
// fetch('/api/protected-endpoint', {
//     method: 'GET',
//     headers: {
//         'Authorization': 'Bearer ' + token
//     }
// })
//     .then(response => response.json())
//     .then(data => console.log(data))
//     .catch(error => console.error('Error:', error));
//
// // * 2. 데이터 파트
// const modifyButton = document.getElementById('modify-btn');
// if (modifyButton) {
//     modifyButton.addEventListener('click', event => {
//         let params = new URLSearchParams(location.search);
//         let id = params.get('id');
//
//         fetch(`/api/articles/${id}`, {
//             method: 'PUT',
//             headers: {
//                 "Content-Type": "application/json",
//                 'Authorization': 'Bearer ' + token // JWT 여기에서 추가한 다음 보낸다.
//                 // 이 부분을 (클라이언트에서 추가해서 보내야 한다.)
//                 // 1. 브라우저에서 가지고 온다? (쿠키 : 엑세스 토큰?)
//                 // 2. 로컬에서 가지고 온다? : 리프레쉬 토큰
//             },
//             body: JSON.stringify({
//                 title: document.getElementById('title').value,
//                 content: document.getElementById('content').value
//             })
//         })
//             .then(() => {
//                 alert('Update Complete.');
//                 location.replace(`/articles/${id}`); // 수정해야 하는 부분
//             })
//             .catch(error => console.error('Error:', error)); // Catch and log any errors
//     });
// }
//
// // 이 코드에서 두 개의 fetch 요청은 서로 다른 시점에 따로따로 동작
// //
// // 1.	첫 번째 fetch 요청 (/api/protected-endpoint)은 페이지가 로드될 때 즉시 실행됩니다.
// //      이 요청은 GET 방식으로 서버에 데이터를 요청하며, 서버로부터 받은 JWT 토큰을 Authorization 헤더에 포함하여 요청합니다.
// //      이 요청은 페이지를 로드할 때, 혹은 그와 관련된 이벤트가 발생할 때 동작합니다.
// // 2.	두 번째 fetch 요청 (/api/articles/${id})은 사용자가 특정 버튼(여기서는 modifyButton)을 클릭했을 때 실행됩니다.
// //      버튼을 클릭하는 이벤트가 발생해야 이 요청이 시작되며, PUT 방식을 사용하여 서버에 수정된 데이터를 보냅니다.
// //      이때도 JWT 토큰을 Authorization 헤더에 포함하여 요청합니다.
// //
// //     따라서 이 두 요청은 같이 동작하는 것이 아니라, 각각 별도의 시점에서 동작합니다.
// //
// // 	•	첫 번째 요청은 페이지가 로드되거나 특정 이벤트가 발생할 때 서버로 데이터를 가져오는 요청입니다.
// // 	•	두 번째 요청은 사용자가 버튼을 클릭했을 때, 수정된 데이터를 서버로 보내는 요청입니다.
// //
// //     이 두 요청은 서로 독립적으로 이루어지며, 이벤트에 따라 따로 동작합니다.