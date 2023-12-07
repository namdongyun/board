import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {BrowserRouter} from "react-router-dom";
import {createTheme, ThemeProvider} from "@mui/material";

// 전역 테마 생성
const theme = createTheme({
    typography: {
        fontFamily: 'NanumSquareNeo-Variable, Roboto, "Helvetica Neue", Arial, sans-serif',
    },
    // CssBaseline 컴포넌트를 통해 전역 스타일을 적용합니다.
    components: {
        MuiCssBaseline: {
            styleOverrides: `
                @font-face {
                    font-family: 'NanumSquareNeo-Variable';
                    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_11-01@1.0/NanumSquareNeo-Variable.woff2') format('woff2');
                    font-weight: normal;
                    font-style: normal;
                }
                html {
                  -webkit-font-smoothing: antialiased;
                  -moz-osx-font-smoothing: grayscale;
                }
            `,
        },
    },
});


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <BrowserRouter>
        <ThemeProvider theme={theme}>
            <App/>
        </ThemeProvider>
    </BrowserRouter>
);


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
