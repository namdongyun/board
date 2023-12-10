import * as React from 'react';
import {Container, Typography, Paper, Button, Stack} from '@mui/material';
import {useState} from "react";
import PasswordChangeModal from "./modal/PasswordChangeModal";
import Box from "@mui/material/Box";
import NicknameChangeModal from "./modal/NicknameChangeModal";

export default function MyPage() {
    const [passwordModalOpen, setPasswordModalOpen] = useState(false);
    const [nicknameModalOpen, setNicknameModalOpen] = useState(false);

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
                </Stack>
            </Paper>
        </Container>
    );
}