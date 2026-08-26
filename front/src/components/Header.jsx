import './Header.css';

import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { TodoStatusContext } from '../App';
import useLogout from '../hooks/useLogout';

const Header = ({title}) => {
    const { info, dispatch } = useContext(TodoStatusContext);
    
    const nav = useNavigate();

    const logout = useLogout();

    const loginOnClick = async (e) => {
        logout();
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