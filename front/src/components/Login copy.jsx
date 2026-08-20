import './Login.css';
import { useState } from 'react';
import Input from './InputBox';
import Button from './Button';
import API from './API';

const Login = () => {

    const [input, setInput] = useState({
        memberId : "",
        password : ""
    });

    const onChangeInput = (e) => {
        let name = e.target.name;
        let value = e.target.value;

        setInput({
            ...input,
            [name] : value
        });
    };

    const onClickButton = (e) => {
        handleLogin();
    };

    const handleLogin = async () => {
        try {
            const response = await API.post("/login", {
            ...input
            });
            console.log(response);
        } catch(e) {
            console.log(e);
        }
        


    }

    return (
        <div className='Login'>
            <div className='input_section'>
                <input
                    name='memberId'
                    type='text'
                    onChange={onChangeInput}
                    value={input.memberId}
                />
                <input
                    name='password'
                    type='password'
                    onChange={onChangeInput}
                    value={input.password}
                />
                
            </div>
            <Button text={'로그인'} onClick={onClickButton}/>
        </div>      
    );
};

export default Login;

