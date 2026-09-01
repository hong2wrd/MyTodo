import { useContext, useEffect } from "react";
import { TodoStatusContext } from "../App";
import { useLocation, useNavigate } from "react-router-dom";

const useLoginConfirm = () => {
    const { info } = useContext(TodoStatusContext);
    const nav = useNavigate();    
    const location = useLocation();

    useEffect(() => {
        if(!info.isLogin) {
            nav("/login", {
                state:  {
                    from: location.pathname
                }
            });
        }
    }, []);
};

export default useLoginConfirm;