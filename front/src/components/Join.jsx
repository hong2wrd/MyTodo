import './Join.css';
import InputBox from './InputBox';
import ButtonBox from './ButtonBox';
import Input from './Input';
import Button from './Button';

import API from './API';
import { useRef, useState } from 'react';

const Join = () => {
    
    const [input, setInput] = useState({
        memberId : "",
        password1 : "",
        password2 : "",
        memberName : "",
        ddd : "",
        tel1 : "",
        tel2 : "",
        conflict : ""
    });

    const inputRef = useRef({});


    const memberCoflictCheck = async () => {

        if(!input.memberId) {
            return;
        }

        const response = await API.get(`/member/${input.memberId}/conflict`);
        const data = response.data.data;

        setInput({
            ...input,
            conflict : data.conflict
        });
    };

    const memberJoinSubmit = async () => {
        if(!input.memberId) {
            window.alert("아이디를 입력해 주세요.");
            inputRef.current.memberId.focus();
            return;
        }

        if(input.conflict === "") {
            window.alert("아이디 중복 확인이 필요합니다.");
            return;
        }

        if(input.conflict) {
            window.alert("아이디 중복 확인이 필요합니다.");
            return;
        }

        if(!input.password1) {
            window.alert("비밀번호를 입력해 주세요.");
            inputRef.current.password1.focus();
            return;
        }

        
        if(!input.password2) {
            window.alert("비밀번호를 입력해 주세요.");
            inputRef.current.password2.focus();
            return;
        }
        
        const response = await API.post('/member', {
                memberId : input.memberId,
                password : input.password1,
                memberName : input.memberName,
                ddd : input.ddd,
                tel1 : input.tel1,
                tel2 : input.tel2,
        });
        
    }
    const onInputChange = (e) => {
        let name = e.target.name;
        let value = e.target.value;

        setInput({
            ...input,
            [name] : value
        });
    }

    const checkPassword = () => {
        if(input.password1 && input.password2) {
            if(input.password1 === input.password2) {
                return 'POSITIVE';
            }
            return 'NEGATIVE';
        }
        return '';
    }
    

    return (
        <div className="Join">
            <div>
                <h2>가입하기</h2>
            </div>
            <div className="content">
                <p>아이디</p>
                <Input
                    ref={(el) => inputRef.current.memberId = el}
                    type={'text'}
                    name={'memberId'}
                    onChange={onInputChange}/>
                <Button
                    text={'중복 확인'}
                    onClick={memberCoflictCheck}/>
                <span className={`Span_${input.conflict ? "NEGATIVE" : "POSITIVE"}`}>
                    { input.conflict === "" ? "" : (input.conflict ? "사용 불가능" : "사용 가능") }
                </span>
                
            </div>
            <div className="content">
                <p>비밀번호</p>
                <Input
                    ref={(el) => inputRef.current.password1 = el}
                    name={'password1'}
                    type={'password'}
                    onChange={onInputChange}/>
                <Input
                    ref={(el) => inputRef.current.password2 = el}
                    name={'password2'}
                    type={'password'}
                    onChange={onInputChange}/>
                <span className={`Span_${checkPassword()}`}>
                    { checkPassword() ? (checkPassword() === 'POSITIVE' ? '비밀번호 일치' : '비밀번호 불일치') : "" }
                </span>
            </div>
            <div className="content">
                <p>이름</p>
                <Input
                    ref={(el) => inputRef.current.name = el}
                    name={'memberName'}
                    type={'text'}
                    onChange={onInputChange} />
            </div>
            <div className="content">
                <p>핸드폰번호</p>
                <Input
                    ref={(el) => inputRef.current.ddd = el}
                    name={'ddd'}
                    type={'text'}
                    onChange={onInputChange} />
                <Input
                    ref={(el) => inputRef.current.tel1 = el}
                    name={'tel1'}
                    type={'text'}
                    onChange={onInputChange} />
                <Input
                    ref={(el) => inputRef.current.tel2 = el}
                    name={'tel2'}
                    type={'text'}
                    onChange={onInputChange} />
            </div>
            <ButtonBox leftText={'가입하기'} leftOnClick={memberJoinSubmit} rightText={'돌아가기'}/>
        </div>
    );
};

export default Join;
