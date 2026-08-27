import './Login.css';
import { useRef, useState } from 'react';
import Button from './Button';
import API from '../hooks/API';
import { useLocation, useNavigate } from 'react-router-dom';
import {  setToken } from '../hooks/useToken';
import useLogin from '../hooks/useLogin';

const Login = () => {
    const nav = useNavigate();
    const location = useLocation();

    const [input, setInput] = useState({
        memberId : "",
        password : ""
    });
    const [span, setSpan] = useState({
        memberId : "",
        password : ""
    });

    const inputRef = useRef({});

    const login = useLogin();

    const onChangeInput = (e) => {
        let name = e.target.name;
        let value = e.target.value;

        setInput({
            ...input,
            [name] : value
        });

        setSpan({
            ...span,
            [name] : ""
        });
    };

    const onClickButton = async () => {

        if(validationCheck("memberId", "아이디")) {
            return;
        }

        const regex = /^[a-z0-9]+$/; // 숫자, 영어소문자
        if(! regex.test(input.memberId)) {
            setSpan({
                ...span,
                memberId : "아이디는 영문(소문자) 및 숫자만 입력이 가능합니다."
            });
            return;
        }


        if(validationCheck("password", "비밀번호")) {
            return;
        }

        try {
            const response = await API.post("/login", {
            ...input
            });
            
            const accessToken = response.headers.getAuthorization();
            setToken(accessToken);

            window.alert(response.data.msg);
            
            login();

            const from = location.state?.from || "/";
            
            nav(from, {replace: true});
        } catch(e) {
            console.log(e);
            const data = e.response.data;
            
            if(data.code === -1) {
                window.alert(data.msg);

                setInput({
                    ...input,
                    password : ""
                });

                inputRef.current.password.focus();
            }
        }
    }

    /**
     * 유효성 검사
     * @param {*} key 
     * @param {*} msg 
     * @returns 
     */
    const validationCheck = (key, msg) => {        
        if(!input[key]) {
            setSpan({
                ...span,
                [key] : `${msg}를 입력해주세요.`
            });

            inputRef.current[key].focus();
            return true;
        }

        return false;
    }

    return (
        <div className='Login'>
            <h2>로그인</h2>
            <div className='container'>
                <div className='input_box'>
                    <p>아이디</p>
                    <input
                        name='memberId'
                        type='text'
                        onChange={onChangeInput}
                        value={input.memberId}
                        maxLength={20}
                        ref={el => inputRef.current.memberId = el}
                        placeholder='아이디'
                    />
                </div>
                <span>{span.memberId}</span>
                <div className='input_box'>
                    <p>비밀번호</p>
                    <input
                        name='password'
                        type='password'
                        onChange={onChangeInput}
                        value={input.password}
                        ref={el => inputRef.current.password = el}
                        placeholder='비밀번호'
                    />
                </div>
                <span>{span.password}</span>
            </div>
            <Button text={'로그인'} onClick={onClickButton}/>
        </div>      
    );
};

export default Login;

