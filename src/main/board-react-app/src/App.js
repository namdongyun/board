import {Route, Routes, Navigate} from "react-router-dom";
import MainPage from "./login/MainPage";
import LoginPage from "./login/LoginPage";
import RegisterPage from "./login/RegisterPage";

function App() {
  return (
    <Routes>
      <Route path="/" element={<MainPage/>}/>
      <Route path="/login" element={<LoginPage/>}/>
      <Route path="/register" element={<RegisterPage/>}/>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;