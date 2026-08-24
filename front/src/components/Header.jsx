import { useContext, useEffect } from "react";
import API, { getAccessToken, getMembrInfoByToken, setAccessToken } from "./API";
import { useNavigate } from "react-router-dom";
import { TodoStatusContext } from '../App';

const Header = ({title}) => {
    const { info, dispatch } = useContext(TodoStatusContext);
    
    const nav = useNavigate();

    const accessToken = getAccessToken();

    useEffect(() => {
        if(accessToken !== "") {
            dispatch({
                type: "LOGIN",
                payload : {
                    memberName : getMembrInfoByToken("name")
                }
            })
        }
    }, []);

    const loginOnClick = async (e) => {
        if(accessToken !== "") {
            await API.post('/logout');

            setAccessToken("");

            dispatch({
                type: "LOGOUT",
                payload: ""
            });

            nav("/", {replace: false})
        } else {
            nav('/login');
        }
    }

    return <>
        <button onClick={() => nav(-1)}>뒤로 가기</button>
        <div>{title}</div>
        <div>
            {info.memberName}
            <button onClick={loginOnClick}>
                {info.isLogin ? "로그아웃" : "로그인"}
            </button>
        </div>
    </>
}

export default Header;