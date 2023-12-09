import * as React from 'react';
import { Container, Typography, Paper, Grid, Avatar, TextField, Button } from '@mui/material';
import axios from "axios";
import {useContext} from "react";
import {AuthContext} from "../login/AuthContext";

export default function MyPage() {
    // 사용자의 현재 정보를 상태로 관리합니다.
    const [nickname, setNickname] = React.useState('');
    const [password, setPassword] = React.useState('');
    const [confirmPassword, setConfirmPassword] = React.useState('');
    const [image, setImage] = React.useState(null);

    const {auth} = useContext(AuthContext); // 현재 로그인 한 사용자의 auth(인증 상태)를 가져옵니다.

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };
    const handleConfirmPasswordChange = (event) => {
        setConfirmPassword(event.target.value);
    };

    // const handleImageChange = (event) => {
    //     if (event.target.files && event.target.files[0]) {
    //         setImage(URL.createObjectURL(event.target.files[0]));
    //     }
    // };

    // 변경 사항을 저장하는 함수입니다.
    const handleSubmit = () => {
        // 비밀번호와 비밀번호 확인이 같은지 검사
        if (password !== confirmPassword) {
            alert('입력한 비밀번호가 서로 다릅니다.');
            return;
        }

        // URLSearchParams 객체를 사용하여 데이터 인코딩
        const params = new URLSearchParams();
        params.append('username', auth.username); // 사용자 이름 추가
        params.append('newPassword', password); // 새 비밀번호 추가

        // axios를 이용하여 POST 요청 보내기
        axios.post('/api/change-password', params, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            withCredentials: true
        })
            .then(response => {
                // 성공적으로 요청을 보냈을 때의 처리
                alert(response.data);
            })
            .catch(error => {
                // 요청에 실패했을 때의 처리
                alert(error.response.data);
            });
    };

    return (
        <Container maxWidth="sm" sx={{ mt: 4 }}>
            <Paper sx={{ p: 4 }}>
                <Typography variant="h6" gutterBottom>
                    비밀번호 변경
                </Typography>
                <Grid container spacing={1}>
                    <Grid item xs={12}>
                        <TextField
                            required
                            id="password"
                            name="password"
                            label="새 비밀번호"
                            type="password"
                            fullWidth
                            variant="outlined"
                            size="small"
                            value={password}
                            onChange={handlePasswordChange}
                            sx={{width: 300}}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            required
                            id="confirmPassword"
                            name="confirmPassword"
                            label="새 비밀번호 확인"
                            type="password"
                            fullWidth
                            variant="outlined"
                            size="small"
                            value={confirmPassword}
                            onChange={handleConfirmPasswordChange}
                            sx={{width: 300}}
                        />
                    </Grid>
                    {/*<Grid item xs={12} sm={6}>*/}
                    {/*    <Button*/}
                    {/*        variant="contained"*/}
                    {/*        component="label"*/}
                    {/*        startIcon={<PhotoCamera />}*/}
                    {/*    >*/}
                    {/*        프로필 사진 업로드*/}
                    {/*        <input*/}
                    {/*            type="file"*/}
                    {/*            hidden*/}
                    {/*            onChange={handleImageChange}*/}
                    {/*        />*/}
                    {/*    </Button>*/}
                    {/*    {image && (*/}
                    {/*        <Avatar*/}
                    {/*            src={image}*/}
                    {/*            sx={{ width: 56, height: 56, mt: 2 }}*/}
                    {/*        />*/}
                    {/*    )}*/}
                    {/*</Grid>*/}
                </Grid>
                <Button
                    variant="contained"
                    color="primary"
                    sx={{ mt: 3 }}
                    onClick={handleSubmit}
                >
                    저장하기
                </Button>
            </Paper>
        </Container>
    );
}