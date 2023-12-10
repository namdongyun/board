import React, {useContext, useState} from 'react';
import { Button, TextField, Grid, Modal, Box } from '@mui/material';
import axios from "axios";
import {AuthContext} from "../../login/AuthContext";

export default function PasswordChangeModal({ open, handleClose}) {
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const {auth} = useContext(AuthContext); // 현재 로그인 한 사용자의 auth(인증 상태)를 가져옵니다.

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const handleConfirmPasswordChange = (event) => {
        setConfirmPassword(event.target.value);
    };

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

    const style = {
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        width: 400,
        bgcolor: 'background.paper',
        boxShadow: 24,
        p: 4,
        borderRadius: '8px',
    };

    return (
        <Modal
            open={open}
            onClose={() => {
                setPassword('');
                setConfirmPassword('');
                handleClose();
            }}
            aria-labelledby="password-change-modal"
            aria-describedby="password-change-modal-description"
        >
            <Box sx={style}>
                <Grid container spacing={2}>
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
                        />
                    </Grid>
                </Grid>
                <Button
                    variant="contained"
                    color="primary"
                    sx={{ mt: 3 }}
                    onClick={handleSubmit}
                >
                    저장
                </Button>
            </Box>
        </Modal>
    );
}