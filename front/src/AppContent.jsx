import { Route, Routes } from "react-router-dom";
import TodoList from "./pages/TodoList";
import JoinPage from "./pages/Join";
import LoginPage from "./pages/LoginPage";
import New from "./pages/New";
import TodotypePage from "./pages/TodotypePage";
import { useEffect } from "react";
import { getToken } from "./hooks/useToken";
import useLogin from "./hooks/useLogin";
import Edit from "./pages/Edit";

function AppContent() {

    const login = useLogin();

    // 토큰에 유무에 따른 로그인 정보 설정
    useEffect(() => {
        if( getToken() ) {    
            login();
        }
    }, []);

    return<>
        <Routes>
            <Route path="/" element={<TodoList/>}/>
            <Route path="/join" element={<JoinPage/>}/>
            <Route path="/login" element={<LoginPage/>}/>
            <Route path="/new" element={<New/>}/>
            <Route path="/todoType" element={<TodotypePage/>}/>
            <Route path="/edit/:todoId" element={<Edit/>}/>
        </Routes>
    </>
}

export default AppContent;