import React, {useContext, useState} from "react";
import {Box, Button, Grid, Modal, TextField} from "@mui/material";
import {AuthContext} from "../../login/AuthContext";
import axios from "axios";

export default function NicknameChangeModal({ open, handleClose }) {
    const [nickname, setNickname] = useState('');

    const handleNicknameChange = (event) => {
        setNickname(event.target.value);
    };

    const {accessToken} = useContext(AuthContext); // 현재 로그인 한 사용자의 jwt를 가져옵니다.

    // 변경 사항을 저장하는 함수입니다.
    const handleSubmit = async () => {

        // URLSearchParams 객체를 사용하여 데이터 인코딩
        const params = new URLSearchParams();
        params.append('newNickname', nickname); // 새 비밀번호 추가

        try {
            // axios를 이용하여 POST 요청 보내기
            const response = await axios.post('/api/change-nickname', params, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Authorization': `Bearer ${accessToken}`
                },
            });

            // 성공적으로 요청을 보냈을 때의 처리
            alert(response.data);
        } catch (error) {
            // 요청에 실패했을 때의 처리
            alert(error.response.data);
        }
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
                setNickname('');
                handleClose();
            }}
            aria-labelledby="nickname-change-modal"
            aria-describedby="nickname-change-modal-description"
        >
            <Box sx={style}>
                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        <TextField
                            required
                            id="nickname"
                            name="nickname"
                            label="새 닉네임"
                            type="text"
                            fullWidth
                            variant="outlined"
                            size="small"
                            value={nickname}
                            onChange={handleNicknameChange}
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