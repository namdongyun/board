import {AuthProvider} from "./login/AuthContext";
import BoardHeader2 from "./header/BoardHeader2";
import CssBaseline from "@mui/material/CssBaseline";

function App() {
    return (
        <AuthProvider>
            <BoardHeader2/>
        </AuthProvider>
    );
}

export default App;