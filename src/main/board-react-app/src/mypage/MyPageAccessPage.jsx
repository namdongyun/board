import React from "react";
import {Avatar, Button, Container, Grid, Paper, Typography} from "@mui/material";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

export default function MyPageAccessPage() {

    return (
        <Container sx={{ flexGrow: 1, mt: 3 }}>
            <Grid container spacing={3}>
                <Grid item xs={12}>
                    <Paper sx={{ p: 2, textAlign: 'center' }}>
                        <Avatar sx={{ m: 'auto', bgcolor: 'secondary.main' }}>
                            <AccountCircleIcon />
                        </Avatar>
                        <Typography variant="h5">사용자 이름</Typography>
                        <Typography variant="subtitle1">회원 정보를 수정하려면 아래 버튼을 클릭하세요.</Typography>
                        <Button variant="contained" color="primary" sx={{ mt: 2 }}>
                            정보 수정
                        </Button>
                    </Paper>
                </Grid>
                {/* 여기에 추가적인 마이페이지 내용을 구현할 수 있습니다. */}
            </Grid>
        </Container>
    );
}