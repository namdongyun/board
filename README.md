# Spring, React를 이용한 게시판 입니다.
## Login 구현은 Spring Security를 사용하였습니다.
## 23/11/28
- await와 .then()을 함께 사용하는 것은 권장되지 않는데, 그 이유는 두 가지 모두 비동기 작업을 처리하는 방법이지만, 각각 다른 스타일의 코드 작성을 의미하기 때문입니다.

- .then()과 .catch() 방식은 마치 사다리를 타고 올라가듯이, 한 단계씩 비동기 작업을 처리하는 방식입니다. .then()은 성공했을 때 다음 단계로 넘어가고, .catch()는 문제가 생겼을 때 오류를 잡아내는 역할을 합니다.
- async-await 방식은 마치 일반적인 동기적 코드를 작성하듯이 비동기 작업을 처리합니다. async로 함수를 특별히 표시해주고, await으로 비동기 작업이 끝날 때까지 기다렸다가 결과를 바로 사용할 수 있습니다. 오류 처리는 try-catch로 합니다.

간단히 말해서, .then()과 .catch()는 '단계별로' 처리하고, async-await은 '기다렸다가 바로' 처리하는 느낌입니다.
```js
const handleLogout = () => {
    axios.post('/api/logout', {}, {
        withCredentials: true // 쿠키를 포함시키기 위해 withCredentials 옵션을 true 설정
    })
        .then(response => {
            // 로그아웃 성공 시 처리
            console.log("로그아웃 성공");
            // 메인 페이지로 이동
            // window.location.href = '/';
            navigate('/'); // 홈페이지로 이동
            logout();
        })
        .catch(error => {
            // 로그아웃 실패 시
            console.error(`로그아웃 실패 ${error}`);
        })
}
```
```js
const handleLogout = async () => {
    try {
        // axios.post 호출과 await를 사용하여 비동기 요청을 기다립니다.
        const response = await axios.post('/api/logout', {}, {
            withCredentials: true // 쿠키를 포함시키기 위해 withCredentials 옵션을 true로 설정합니다.
        });
    
        // 로그아웃 성공 시 처리
        console.log("로그아웃 성공");
        // 홈페이지로 이동
        navigate('/');
        logout();
    } catch (error) {
        // 로그아웃 실패 시
        console.error(`로그아웃 실패: ${error}`);
    }
    }
```
---

- Login 구현에서 username과 password를 Spring 서버로 넘겨줄 때 Spring security는 application/x-www-form-urlencoded 형식으로 받기 때문에 
```js
const formData = new URLSearchParams();
        formData.append('username', username);
        formData.append('password', password);
```
```js
const response = await axios.post('/api/login', formData, {
                withCredentials: true,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });
```
이렇게 형식을 바꿔서 spring 서버로 넘겨줘야 합니다.

- WebSecurityConfig.java
```java
.formLogin(form -> form
    // 로그인 API 호출 주소
    .loginProcessingUrl("/api/login")
    
    // 로그인에 성공했을 때 실행될 AuthenticationSuccessHandler를 정의합니다.
    .successHandler(new AuthenticationSuccessHandler() {
    
        // 로그인 성공 시 실행될 메소드를 오버라이드합니다.
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                     Authentication authentication) throws IOException, ServletException {
            // 사용자 정보에 접근하여 필요한 정보를 가져옵니다.
            Object principal = authentication.getPrincipal();
            String username = "";
            String role = "";
    
            // principal이 UserDetails 인스턴스라면, 사용자 이름과 권한 정보를 가져옵니다.
            if(principal instanceof UserDetails){
                UserDetails userDetails = (UserDetails) principal;
                username = userDetails.getUsername();
                role = userDetails.getAuthorities().toString();
            }
            // 응답의 Content-Type을 JSON으로 설정
            response.setContentType("application/json;charset=UTF-8");
    
            // HTTP 응답 상태 코드를 200(성공)으로 설정
            response.setStatus(HttpServletResponse.SC_OK);
    
            // JSON 문자열을 만들어 응답의 본문에 쓰기
            response.getWriter()
                    .write("{\"username\": \"" + username + "\", \"role\": \"" + role + "\"}");
        }
    })
    .permitAll()
)
```
