import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getAccessToken } from "../components/API";

/**
 * Token이 있을 경우 '/' 화면으로 전환
 */
const TokenCheck = () => {
    const nav = useNavigate();

    useEffect(() =>{
        const token = getAccessToken();
        if(token) {
            nav('/',{replace: true});
        }

    }, []);
};

export default TokenCheck;