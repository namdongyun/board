import React, {useContext, useState} from "react";
import axios from "axios";
import {AuthContext} from "./AuthContext";
import {Button, Checkbox, FormControlLabel, TextField, Link, Grid, Typography, Avatar, Container} from "@mui/material";
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Box from "@mui/material/Box";
import {useNavigate} from "react-router-dom";

function LoginPage(props) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const {login} = useContext(AuthContext); // AuthContext에서 login 함수를 가져옵니다.

    // 경로 이동 함수
    const navigate = useNavigate();

    const handleLogin = async (event) => {
        event.preventDefault(); // 폼 제출 시 브라우저가 자동으로 페이지를 새로고침하는 것을 막습니다.

        // application/x-www-form-urlencoded 형식으로 바꿔주기 위함
        const formData = new URLSearchParams();
        formData.append('username', username);
        formData.append('password', password);

        try {
            // Axios 라이브러리를 사용하여 서버의 /login 엔드포인트로 POST 요청을 보냅니다.
            // await 키워드는 해당 비동기 요청이 완료될 때까지 함수 실행을 일시 중지하고,
            // 요청이 성공적으로 완료되면 결과를 response 변수에 저장합니다.
            const response = await axios.post('/api/login', formData, {
                withCredentials: true,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });
            console.log('로그인 response 값 : ', response);

            // 서버로부터 받은 role 값에서 대괄호를 제거합니다.
            const roleWithoutBrackets = response.data.role.replace(/\[|\]/g, '');

            // 로그인 성공 후, AuthProvider의 login 함수를 호출하여 상태를 업데이트합니다.
            login(response.data.username, roleWithoutBrackets);

            console.log('로그인 성공 : ', response.data);
            navigate('/'); // 홈페이지로 이동
        } catch (error){
            console.log(`로그인 실패 : ${error}`);
        }
    };

    return (
        <Container component="main" maxWidth="xs">
            <Box
                sx={{
                    marginTop: 8,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}
            >
                <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                    <LockOutlinedIcon />
                </Avatar>
                <Typography component="h1" variant="h5">
                    Sign in
                </Typography>
                <Box component="form" onSubmit={handleLogin} noValidate sx={{ mt: 1 }}>
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        label="Username"
                        name="username"
                        autoComplete="username"
                        autoFocus
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Password"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <FormControlLabel
                        control={<Checkbox value="remember" color="primary" />}
                        label="Remember me"
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2,}}
                    >
                        Sign In
                    </Button>
                    <Grid container>
                        <Grid item xs>
                            <Link href="#" variant="body2">
                                Forgot password?
                            </Link>
                        </Grid>
                        <Grid item>
                            <Link href="/register" variant="body2">
                                {"Don't have an account? Sign Up"}
                            </Link>
                        </Grid>
                    </Grid>
                </Box>
            </Box>
        </Container>
    );
}

export default LoginPage;