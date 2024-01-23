import * as React from 'react';
import {Container, Typography, Paper, Button, Stack} from '@mui/material';
import {useEffect, useState} from "react";
import PasswordChangeModal from "./modal/PasswordChangeModal";
import Box from "@mui/material/Box";
import NicknameChangeModal from "./modal/NicknameChangeModal";
import api from "../axiosInterceptor/api";

export default function MyPage() {
    const [passwordModalOpen, setPasswordModalOpen] = useState(false);
    const [nicknameModalOpen, setNicknameModalOpen] = useState(false);
    const [money, setMoney] = useState(0);

    const handleOpenPasswordModal = () => {
        setPasswordModalOpen(true);
    };

    const handleClosePasswordModal = () => {
        setPasswordModalOpen(false);
    };

    const handleOpenNicknameModal = () => {
        setNicknameModalOpen(true);
    };

    const handleCloseNicknameModal = () => {
        setNicknameModalOpen(false);
    };

    useEffect(() => {
        const fetchMoney = async () => {
            try{
                const response = await api('/api/accountDetail/loadMoney');

                if (response.status !== 200) {
                    throw new Error(`서버 통신에 문제가 발생했습니다. 상태 코드: ${response.status}`);
                }
                console.log("money: ", response.data);
                setMoney(response.data);
            } catch (error) {
                console.error("사용자 money 로드 오류: ", error);
            }
        }
        fetchMoney();
    }, []);

    return (
        <Container maxWidth="sm" sx={{ mt: 4 }}>
            <Paper sx={{ p: 4 }}>
                <Typography variant="h6" gutterBottom>
                    정보 변경
                </Typography>
                <Stack direction="column" spacing={1.5}>
                    <Box>
                        <Button variant="contained" color="primary" onClick={handleOpenPasswordModal}>
                            비밀번호 변경
                        </Button>
                    </Box>
                    <PasswordChangeModal
                        open={passwordModalOpen}
                        handleClose={handleClosePasswordModal}
                    />

                    <Box>
                        <Button variant="contained" color="primary" onClick={handleOpenNicknameModal}>
                            닉네임 변경
                        </Button>
                    </Box>
                    <NicknameChangeModal open={nicknameModalOpen} handleClose={handleCloseNicknameModal}/>
                    <Box>
                        <Typography variant="body1" gutterBottom>
                            보유 금액: {money.toLocaleString()}
                        </Typography>
                    </Box>
                </Stack>
            </Paper>
        </Container>
    );
}