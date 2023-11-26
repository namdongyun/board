import {Route, Routes, Navigate} from "react-router-dom";
import MainPage from "./login/MainPage";
import LoginPage from "./login/LoginPage";
import RegisterPage from "./login/RegisterPage";
import BoardList from "./board/BoardList";
import {AuthProvider} from "./login/AuthContext";
import ProtectedRoute from "./login/ProtectedRoute";
import BoardView from "./board/BoardView";

function App() {

    const renderProtected = (Component) => (
        <ProtectedRoute roles={['ADMIN', 'USER']}>
            <Component />
        </ProtectedRoute>
    );

    return (
        <AuthProvider>
            <Routes>
                <Route path="/" element={<MainPage/>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
                <Route path="/board/list" element={renderProtected(BoardList)}/>
                <Route path="/board/view/:id" element={renderProtected(BoardView)}/>
                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </AuthProvider>
    );
}

export default App;