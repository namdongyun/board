import React, {useEffect, useState} from 'react';
import { Button, Typography, Paper, Stack, Snackbar, Alert } from '@mui/material';
import api from "../axiosInterceptor/api";

export default function TitleEnhancement() {
    const [level, setLevel] = useState(1); // 현재 레벨
    const [points, setPoints] = useState(0); // 사용자의 포인트
    const [openSnackbar, setOpenSnackbar] = useState(false);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await api('/api/accountDetail/loadMoney');
                if (response.status !== 200) {
                    throw new Error(`서버 통신에 문제가 발생했습니다. 상태 코드: ${response.status}`);
                }
                setLevel(response.data.level);
                setPoints(response.data.points);
            } catch (error) {
                console.error('Error fetching user data:', error);
            }
        };
        fetchUserData();
    }, []);

    const enhanceCost = level * 10; // 강화 비용은 레벨에 비례하여 증가

    const handleEnhanceClick = () => {
        if (points >= enhanceCost) {
            if (level < 1000) {
                setLevel(level + 1);
                setPoints(points - enhanceCost);
                setOpenSnackbar(true);
            } else {
                alert('최대 레벨에 도달했습니다.');
            }
        } else {
            alert('포인트가 부족합니다.');
        }
    };

    const handleCloseSnackbar = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpenSnackbar(false);
    };

    return (
        <Paper elevation={3} sx={{ padding: '20px', maxWidth: '320px', margin: 'auto', marginTop: '50px' }}>
            <Stack spacing={2} alignItems="center">
                <Typography variant="h5">칭호 강화</Typography>
                <Typography variant="subtitle1">현재 레벨: {level}</Typography>
                <Typography variant="subtitle1">보유 포인트: {points}</Typography>
                <Typography variant="subtitle1">강화 비용: {enhanceCost}</Typography>
                <Button variant="contained" onClick={handleEnhanceClick} disabled={level >= 1000}>
                    강화하기
                </Button>
            </Stack>
            <Snackbar open={openSnackbar} autoHideDuration={2000} onClose={handleCloseSnackbar}>
                <Alert onClose={handleCloseSnackbar} severity="success" sx={{ width: '100%' }}>
                    칭호가 강화되었습니다! 현재 레벨: {level}
                </Alert>
            </Snackbar>
        </Paper>
    );
}
