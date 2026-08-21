import { useNavigate } from "react-router-dom";
import API, { getAccessToken, setAccessToken } from "../components/API";
import { useContext, useEffect, useState } from "react";
import { TodoStatusContext } from '../App';

const HomePage = () => {
    const { info, dispatch } = useContext(TodoStatusContext);
    
    const nav = useNavigate();
    console.log(info);

    const accessToken = getAccessToken();

    useEffect(() => {
        if(accessToken !== "") {
            dispatch({
                type: "LOGIN",
            })
        }
    }, []);
    
    return (
        <div>
            Home
            <span>
                <button onClick={async e => {
                    console.log(accessToken);
                    if(accessToken !== "") {
                        try {
                            await API.post('/logout');

                            setAccessToken("");

                            dispatch({
                                type: "LOGOUT",
                                payload: ""
                            });

                            nav("/", {replace: false})
                        } catch(e) {
                            console.log(e)
                        }
                    } else {
                        nav('/login');
                    }
                    
                }}>{info.isLogin ? "로그아웃" : "로그인"}</button>
            </span>
        </div>
    );
};

export default HomePage;
