import {AuthProvider} from "./login/AuthContext";
import BoardHeader2 from "./header/BoardHeader2";

function App() {
    return (
        <AuthProvider>
            <BoardHeader2/>
        </AuthProvider>
    );
}

export default App;