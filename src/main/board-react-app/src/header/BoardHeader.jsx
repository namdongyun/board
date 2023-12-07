import {
    AppBar,
    Button,
    Drawer,
    IconButton,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Toolbar,
    Typography
} from "@mui/material";
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MenuIcon from "@mui/icons-material/Menu";
import MailIcon from '@mui/icons-material/Mail';
import React, {useContext, useState} from "react";
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../login/AuthContext";
import axios from "axios";

function BoardHeader(){
    const navigate = useNavigate();

    const {logout, isAuthenticated} = useContext(AuthContext); // AuthContext에서 logout 함수를 가져옵니다.
    const [drawerOpen, setDrawerOpen] = useState(false); // Drawer 상태를 관리하기 위한 state

    // 메뉴 왼쪽 토글 열고 닫는 함수
    const toggleDrawer = (open) => (event) => {
        if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
            return;
        }
        setDrawerOpen(open);
    };

    const drawerList = () => (
        <div
            role="presentation"
            onClick={toggleDrawer(false)}
            onKeyDown={toggleDrawer(false)}
        >
            <List>
                {['게시판', '마이페이지', '친구 목록', '채팅방'].map((text, index) => (
                    <ListItem button key={text}>
                        <ListItemIcon>
                            {index % 2 === 0 ? <InboxIcon /> : <MailIcon />}
                        </ListItemIcon>
                        <ListItemText primary={text} />
                    </ListItem>
                ))}
            </List>
        </div>
    );

    const handleLogout = async () => {      // 로그아웃 처리 함수
        try {
            // axios.post 호출과 await를 사용하여 비동기 요청을 기다립니다.
            const response = await axios.post('/api/logout', {}, {
                withCredentials: true // 쿠키를 포함시키기 위해 withCredentials 옵션을 true로 설정합니다.
            });

            // 로그아웃 성공 시 처리
            console.log("로그아웃 성공 : ", response);
            // 홈페이지로 이동
            navigate('/');
            logout();
        } catch (error) {
            // 로그아웃 실패 시
            console.error(`로그아웃 실패: ${error}`);
        }
    }

    return (
        <>
            <AppBar position="fixed">
                <Toolbar>
                    {/* 햄버거 메뉴 버튼 추가 */}
                    <IconButton
                        edge="start"
                        color="inherit"
                        aria-label="menu"
                        onClick={toggleDrawer(true)} // 클릭 시 Drawer를 여는 이벤트 처리
                    >
                        <MenuIcon />
                    </IconButton>
                    {/* 타이틀 */}
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        BOARD
                    </Typography>
                    {!isAuthenticated() && (
                        <>
                            <Button color="inherit" onClick={() => navigate('/register')}>
                                REGISTER
                            </Button>
                            <Button color="inherit" onClick={() => navigate('/login')}>
                                LOGIN
                            </Button>
                        </>
                    )}
                    {isAuthenticated() && (
                        <Button color="inherit" onClick={handleLogout}>
                            로그아웃
                        </Button>
                    )}
                </Toolbar>
            </AppBar>
            <Toolbar /> {/* AppBar와 같은 높이의 여백을 추가 */}
            {/* Drawer 컴포넌트 추가 */}

            <Drawer
                anchor="left"
                open={drawerOpen}
                onClose={toggleDrawer(false)} // 외부 클릭 또는 esc 키를 누를 때 Drawer를 닫는 이벤트 처리
            >
                {drawerList()}
            </Drawer>
        </>
    )
}

export default BoardHeader;