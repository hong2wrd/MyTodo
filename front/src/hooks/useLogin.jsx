import { useContext } from "react";
import { TodoStatusContext } from '../App';
import { getMembrInfoByToken } from "./useToken";

export const loginCofirm = ( info, nav) => {
    if(!info.isLogin) {
        nav("/login");
    }
}

const useLogin = () => {
    const { dispatch } = useContext(TodoStatusContext);
    
    const execute = () => {
        dispatch({
            type: "LOGIN",
            payload: {
                memberId : getMembrInfoByToken("id"),
                memberName : getMembrInfoByToken("name")
            }
        });    
    };

    return execute;
};

export default useLogin;