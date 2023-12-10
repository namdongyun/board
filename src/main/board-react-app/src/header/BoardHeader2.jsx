import * as React from 'react';
import { styled, useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import MuiDrawer from '@mui/material/Drawer';
import MuiAppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import CssBaseline from '@mui/material/CssBaseline';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import ChatIcon from '@mui/icons-material/Chat';
import ForumIcon from '@mui/icons-material/Forum';
import {useNavigate} from "react-router-dom";
import {Button} from "@mui/material";
import {useContext} from "react";
import {AuthContext} from "../login/AuthContext";
import axios from "axios";
import PageRoutes from "../protectedRoute/PageRoutes";
import SettingsIcon from '@mui/icons-material/Settings';

// 사이드바 넓이를 240 픽셀로 설정
const drawerWidth = 240;

// 사이드바가 열렸을 때의 스타일을 정의하는 함수입니다.
const openedMixin = (theme) => ({
    width: drawerWidth, // 열린 사이드바의 너비를 drawerWidth 로 설정
    // 너비 변화에 대한 애니메이션 효과를 정의합니다.
    transition: theme.transitions.create('width', {

        // 애니메이션의 속도 곡선과 지속 시간을 Material-UI 테마에서 가져온 값을 사용합니다.
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.enteringScreen,
    }),
    // 가로 스크롤바가 보이지 않도록 설정합니다.
    overflowX: 'hidden',
});

// 사이드바가 닫혔을 때의 스타일을 정의하는 함수입니다.
const closedMixin = (theme) => ({
    // 너비 변화에 대한 애니메이션 효과를 정의합니다.
    transition: theme.transitions.create('width', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    overflowX: 'hidden',
    // 닫혔을 때의 사이드바 너비를 테마에서 제공하는 간격 값에 1px을 더한 값으로 설정합니다.
    width: `calc(${theme.spacing(7)} + 1px)`,
    // 화면 크기가 'sm' 이상일 때는 사이드바 너비를 다르게 설정합니다.
    [theme.breakpoints.up('sm')]: {
        width: `calc(${theme.spacing(8)} + 1px)`,
    },
});

// 사이드바 상단 부분의 스타일을 정의하는 컴포넌트입니다.
const DrawerHeader = styled('div')(({ theme }) => ({
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    // 상단 부분에 패딩을 줍니다. 세로 방향은 0, 가로 방향은 1의 간격 값을 사용합니다.
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
}));

// 상단 앱 바의 스타일을 정의하고, 'open' 속성이 스타일에 적용되지 않도록 설정합니다.
const AppBar = styled(MuiAppBar, {
    shouldForwardProp: (prop) => prop !== 'open',
})(({ theme, open }) => ({
    // 앱 바가 사이드바 위에 오도록 z-index 값을 설정합니다.
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    ...(open && {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    }),
}));

// 사이드바 컴포넌트의 스타일을 정의하고, 'open' 속성이 스타일에 적용되지 않도록 설정합니다.
const Drawer = styled(MuiDrawer, { shouldForwardProp: (prop) => prop !== 'open' })(
    ({ theme, open }) => ({
        width: drawerWidth,
        flexShrink: 0,
        whiteSpace: 'nowrap',
        boxSizing: 'border-box',
        ...(open && {
            ...openedMixin(theme),
            '& .MuiDrawer-paper': openedMixin(theme),
        }),
        ...(!open && {
            ...closedMixin(theme),
            '& .MuiDrawer-paper': closedMixin(theme),
        }),
    }),
);

export default function BoardHeader2() {
    const navigate = useNavigate();

    const theme = useTheme();
    const [open, setOpen] = React.useState(false);
    const {logout, isAuthenticated} = useContext(AuthContext); // AuthContext에서 logout 함수를 가져옵니다.

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

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
        <Box sx={{ display: 'flex' }}>
            <CssBaseline />
            <AppBar position="fixed" open={open} sx={
                {backgroundColor: ''}
            }>
                <Toolbar>
                    <IconButton
                        color="inherit"
                        aria-label="open drawer"
                        onClick={handleDrawerOpen}
                        edge="start"
                        sx={{
                            marginRight: 5,
                            ...(open && { display: 'none' }),
                        }}
                    >
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1, cursor: 'pointer' }} onClick={() => navigate('/')}>
                        누구세요
                    </Typography>
                    {!isAuthenticated() && (
                        <>
                            <Button color="inherit" onClick={() => navigate('/login')}>
                                LOGIN
                            </Button>
                        </>
                    )}
                    {isAuthenticated() && (
                        <Button color="inherit" onClick={handleLogout}>
                            LOGOUT
                        </Button>
                    )}
                </Toolbar>
            </AppBar>
            <Drawer variant="permanent" open={open}>
                <DrawerHeader>
                    <IconButton onClick={handleDrawerClose}>
                        {theme.direction === 'rtl' ? <ChevronRightIcon /> : <ChevronLeftIcon />}
                    </IconButton>
                </DrawerHeader>
                <Divider />
                <List>
                    <ListItem disablePadding sx={{ display: 'block' }}>
                        <ListItemButton
                            sx={{
                                minHeight: 48,
                                justifyContent: open ? 'initial' : 'center',
                                px: 2.5,
                            }}
                            onClick={() => navigate('board/chat')} // 여기에 클릭 이벤트를 추가합니다.
                        >
                            <ListItemIcon
                                sx={{
                                    minWidth: 0,
                                    mr: open ? 3 : 'auto',
                                    justifyContent: 'center',
                                }}
                            >
                                <ChatIcon />
                            </ListItemIcon>
                            <ListItemText primary="채팅방" sx={{ opacity: open ? 1 : 0 }} />
                        </ListItemButton>
                    </ListItem>
                    <ListItem disablePadding sx={{ display: 'block' }}>
                        <ListItemButton
                            sx={{
                                minHeight: 48,
                                justifyContent: open ? 'initial' : 'center',
                                px: 2.5,
                            }}
                            onClick={() => navigate('board/list')} // 여기에 클릭 이벤트를 추가합니다.
                        >
                            <ListItemIcon
                                sx={{
                                    minWidth: 0,
                                    mr: open ? 3 : 'auto',
                                    justifyContent: 'center',
                                }}
                            >
                                <ForumIcon />
                            </ListItemIcon>
                            <ListItemText primary="게시판" sx={{ opacity: open ? 1 : 0 }} />
                        </ListItemButton>
                    </ListItem>
                    <Divider />
                    <ListItem disablePadding sx={{ display: 'block' }}>
                        <ListItemButton
                            sx={{
                                minHeight: 48,
                                justifyContent: open ? 'initial' : 'center',
                                px: 2.5,
                            }}
                            onClick={() => navigate('mypage')} // 여기에 클릭 이벤트를 추가합니다.
                        >
                            <ListItemIcon
                                sx={{
                                    minWidth: 0,
                                    mr: open ? 3 : 'auto',
                                    justifyContent: 'center',
                                }}
                            >
                                <SettingsIcon />
                            </ListItemIcon>
                            <ListItemText primary="마이페이지" sx={{ opacity: open ? 1 : 0 }} />
                        </ListItemButton>
                    </ListItem>
                </List>
            </Drawer>
            <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
                <DrawerHeader />
                <PageRoutes/>
            </Box>
        </Box>
    );
}