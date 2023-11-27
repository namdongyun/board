import {Route, Routes, Navigate} from "react-router-dom";
import MainPage from "./login/MainPage";
import LoginPage from "./login/LoginPage";
import RegisterPage from "./login/RegisterPage";
import BoardList from "./board/BoardList";
import {AuthProvider} from "./login/AuthContext";
import ProtectedRoute from "./login/ProtectedRoute";
import BoardView from "./board/BoardView";
import BoardWrite from "./board/BoardWrite";
import BoardEdit from "./board/BoardEdit";

function App() {

    const renderProtected = (Component) => (
        <ProtectedRoute roles={['ROLE_USER', 'ROLE_ADMIN']}>
            <Component />
        </ProtectedRoute>
    );

    return (
        <AuthProvider>
            <Routes>
                <Route path="/" element={<MainPage/>} />
                <Route path="/login" element={<LoginPage/>} />
                <Route path="/register" element={<RegisterPage/>} />
                <Route path="/board/list" element={renderProtected(BoardList)} />
                <Route path="/board/view/:id" element={renderProtected(BoardView)} />
                <Route path="/board/editPage/:id" element={renderProtected(BoardEdit)} />
                <Route path="/board/write" element={renderProtected(BoardWrite)} />
                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </AuthProvider>
    );
}

export default App;