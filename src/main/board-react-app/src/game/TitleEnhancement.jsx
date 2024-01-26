import React, {useEffect, useState} from 'react';
import { Button, Typography, Paper, Stack, Snackbar, Alert } from '@mui/material';
import api from "../axiosInterceptor/api";

export default function TitleEnhancement() {
    const [titleLevel, setTitleLevel] = useState(1); // 현재 레벨
    const [points, setPoints] = useState(0); // 사용자의 포인트
    const [enhanceProbability, setEnhanceProbability] = useState(100); // 강화 확률
    const [enhanceStatus, setEnhanceStatus] = useState({ success: false, message: '' });
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [flashGreen, setFlashGreen] = useState(false); // 강화 성공 시 깜박임을 위한 상태

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await api('/api/accountDetail/loadDetail');
                if (response.status !== 200) {
                    throw new Error(`서버 통신에 문제가 발생했습니다. 상태 코드: ${response.status}`);
                }
                setTitleLevel(response.data.titleLevel);
                setPoints(response.data.money);
            } catch (error) {
                console.error('Error fetching user data:', error);
            }
        };
        fetchUserData();
        calculateEnhanceProbability();
    }, [titleLevel, points]);

    const calculateEnhanceProbability = () => {
        // 예를 들어, 강화 확률을 기본 100%에서 시작하여 레벨당 1%씩 감소한다고 가정
        const newProbability = Math.max(100 - (titleLevel - 1), 0); // 0% 이하로 내려가지 않도록
        setEnhanceProbability(newProbability);
    };

    const enhanceCost = titleLevel * 10; // 강화 비용은 레벨에 비례하여 증가

    const handleEnhanceClick = async () => {
        if (points >= enhanceCost) {
            // 강화 성공 여부를 결정하는 랜덤 확률
            const randomChance = Math.random() * 100; // 0과 100 사이의 난수 생성
            console.log(randomChance);
            if (randomChance <= enhanceProbability) { // 난수가 확률 이하일 경우 강화 성공
                const newTitleLevel = titleLevel + 1;
                const newPoints = points - enhanceCost;

                // 서버에 강화 성공 결과 전송
                try {
                    const response = await api.post('/api/titleEnhancement/update', {
                        titleLevel: newTitleLevel,
                        money: newPoints,
                    });

                    if (response.status === 200) {
                        // 서버 응답이 성공적이면 상태 업데이트
                        setTitleLevel(newTitleLevel);
                        setPoints(newPoints);
                        setOpenSnackbar(true);
                        setEnhanceStatus({ success: true, message: '칭호가 강화되었습니다! 현재 레벨: ' + (newTitleLevel) });

                        setFlashGreen(true); // 강화 성공시 깜박임 상태를 true로 설정
                        setTimeout(() => setFlashGreen(false), 400); // 0.3초 후에 깜박임 상태
                    } else {
                        throw new Error(`서버 통신에 문제가 발생했습니다. 상태 코드: ${response.status}`);
                    }
                } catch (error) {
                    console.error('DB 업데이트 중 오류 발생:', error);
                    alert('강화 실패: 서버 오류');
                }
            } else {
                // 강화 실패 처리
                setEnhanceStatus({ success: false, message: '강화에 실패하였습니다.' });
            }
            setOpenSnackbar(true); // 성공과 실패 모두 Snackbar를 표시
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
        <Paper
            elevation={3}
            sx={{
                padding: '20px',
                maxWidth: '320px',
                margin: 'auto',
                marginTop: '50px',
                backgroundColor: flashGreen ? 'lightgreen' : 'white', // 깜박임 상태에 따라 배경색 변경
                transition: 'background-color 500ms', // 부드러운 색상 전환 효과
            }}
        >
            <Stack spacing={2} alignItems="center">
                <Typography variant="h5">칭호 강화</Typography>
                <Typography variant="subtitle1">현재 레벨: {titleLevel}</Typography>
                <Typography variant="subtitle1">보유 포인트: {points}</Typography>
                <Typography variant="subtitle1">강화 비용: {enhanceCost}</Typography>
                <Typography variant="subtitle1">강화 확률: {enhanceProbability}%</Typography>
                <Button variant="contained" onClick={handleEnhanceClick} disabled={titleLevel >= 1000}>
                    강화하기
                </Button>
            </Stack>
            <Snackbar open={openSnackbar} autoHideDuration={1000} onClose={handleCloseSnackbar}>
                <Alert onClose={handleCloseSnackbar} severity={enhanceStatus.success ? "success" : "error"} sx={{ width: '100%' }}>
                    {enhanceStatus.message}
                </Alert>
            </Snackbar>
        </Paper>
    );
}
