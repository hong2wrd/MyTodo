import API from "./API";
import { removeToken } from "./useToken";

import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { TodoStatusContext } from '../App';

const useLogout = () => {
    const { info, dispatch } = useContext(TodoStatusContext);
    
    const nav = useNavigate();

    const execute = async () => {
        
        if( info.isLogin ) {
            try {
                await API.post('/logout');
            
                removeToken();

                dispatch({
                    type: "LOGOUT",
                    payload: ""
                });

                nav("/", {replace: false})
            } catch(e) {
                console.error(e);
            }
        } else {
            nav("/login");
        }    
    };

    return execute;
};

export default useLogout;

