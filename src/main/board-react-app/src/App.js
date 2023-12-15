import {AuthProvider} from "./login/AuthContext";
import BoardHeader2 from "./header/BoardHeader2";

function App() {
    return (
        <div style={{
            // backgroundColor: '#EEF2F6',
            width: '100%',
            height: '100%',
        }}>
            <AuthProvider>
                <BoardHeader2/>
            </AuthProvider>
        </div>
    );
}

export default App;