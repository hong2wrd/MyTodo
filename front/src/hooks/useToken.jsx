import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from 'jwt-decode';

const Authorization = "Authorization";

export const setToken = token => {
    localStorage.setItem(Authorization, token);
};

export const getToken = () => {
    return localStorage.getItem(Authorization);
};

export const removeToken = () => {
    localStorage.removeItem(Authorization);
}

export const getMembrInfoByToken = (key) => {
    const token = getToken();
    
    if((!token || token === 'undefined') || !key) {
        return "";
    }
    
    return jwtDecode(token)[key];
}

/**
 * Token이 있을 경우 '/' 화면으로 전환
 */
const useToken = () => {
    const nav = useNavigate();
    
    useEffect(() => {
        if( getToken() ) {
            nav('/',{replace: true});
        }
    }, []);
};

export default useToken;