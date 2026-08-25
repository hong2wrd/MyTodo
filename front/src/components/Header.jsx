import './Header.css';

import { useContext, useEffect } from "react";
import API from "../hooks/API";
import { getMembrInfoByToken, getToken, setToken } from "../hooks/Token";
import { useNavigate } from "react-router-dom";
import { TodoStatusContext } from '../App';

const Header = ({title}) => {
    const { info, dispatch } = useContext(TodoStatusContext);
    
    const nav = useNavigate();

    const accessToken = getToken();

    const loginOnClick = async (e) => {
        
        if(accessToken !== "") {
            try {
            await API.post('/logout');
            } catch(e) {
                console.error(e);
            }
            setToken("");

            dispatch({
                type: "LOGOUT",
                payload: ""
            });

            nav("/", {replace: false})
        } else {
            nav("/login");
        }
    }

    return (
    <div className="Header">
        <div className='Header_left'>
            <button onClick={() => nav(-1)}>뒤로 가기</button>
        </div>
        <div className='Header_cender'>{title}</div>
        <div className='Header_right'>
            <span>
            { `${info.memberName}${info.memberName ? "님" : ""}` }
            </span>
            <button onClick={loginOnClick}>
                {info.isLogin ? "로그아웃" : "로그인"}
            </button>
        </div>
    </div>
    )
}

export default Header;