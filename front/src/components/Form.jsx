import './Form.css';
import ButtonBox from './ButtonBox';

import API from '../hooks/API';
import { useContext, useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { TodoStatusContext } from '../App';

const Form = ({ title, isUpdate}) => {
    const nav = useNavigate();

    const {info} = useContext(TodoStatusContext);
    
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

    useEffect(() => {
        API.get("/member")
        .then(res => {
            const data = res.data.data;
            console.log(data);
            setInput({...data});
        }).catch(e => {
            console.log(e);
        });

    }, []);

    const onClickMemberCoflictCheck = async () => {

        if(!input.memberId) {
            return;
        }

        await API.get(`/member/${input.memberId}/conflict`)
            .then(res => {
                const data = res.data.data;
                setInput({
                    ...input,
                    conflict : data.conflict
                });
            }
            ).catch(e => {
                setInput({
                    ...input,
                    conflict : ""
                });
            }
        );
    };

        const memberJoin = async () => {
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
        
        try {
            const requestBody = {
                memberId : input.memberId,
                password : input.password1,
                memberName : input.memberName,
                ddd : input.ddd,
                tel1 : input.tel1,
                tel2 : input.tel2,
            }

            const response = await API.post('/member', requestBody);

            const data = response.data;

            window.alert(data.msg)
            nav("/");
            
        } catch(e) {
            const data = e.response?.data;
            if(data?.code === -1) {
                window.alert(data.msg);
            }
        }
    }

    const memberUpdate = async () => {
        try {
            const requestBody = {
                memberName : input.memberName,
                ddd : input.ddd,
                tel1 : input.tel1,
                tel2 : input.tel2,
            };

            const response = await API.put('/member', requestBody);

            if(response.status === 200) {
                window.alert(response.data.msg)
            }
        } catch(e) {
            window.alert('수정이 실패하였습니다.');
        }

    }

    const memberJoinSubmit = () => {
        if(isUpdate) {
            memberUpdate();
        } else {
            memberJoin();
        }
       
    };

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
    
    const onClickPassowrdChange = () => {
        if(!input.password1 || !input.password2) {
            window.alert("비밀번호를 입력해주세요.");
            return;
        }
        if(input.password1 !== input.password2) {
            window.alert("변경할 비밀번호를 확인해주세요.");
            return;
        }

        API.patch('/member', {
            changePassword :input.password1
        })
    }

    const onClickRetire = async () => {
        if(window.confirm("정말로 탈퇴하시겠습니까?")) {
            try {
                const response = await API.delete('/member');

                if(response.status === 200) {
                    window.alert(response.data.msg);
                }

            } catch(e) {
                window.alert('탈퇴에 실패하였습니다.');
            }
        }
    }

    return (
        <div className="Join">
            <h2>{title}</h2>
            <div className="content">
                <p>아이디</p>
                <input
                    ref={(el) => inputRef.current.memberId = el}
                    type={'text'}
                    name={'memberId'}
                    onChange={onInputChange}
                    disabled={isUpdate}
                    value={input.memberId}
                />
                {
                    isUpdate ? <>
                        <button onClick={onClickRetire}>탈퇴하기</button>
                    </> :
                    <>
                        <button onClick={onClickMemberCoflictCheck}>{'중복 확인'}</button>
                        <span className={`Span_${input.conflict ? "NEGATIVE" : "POSITIVE"}`}>
                            { input.conflict === "" ? "" : (input.conflict ? "사용 불가능" : "사용 가능") }
                        </span>
                    </>   
                }
            </div>
            <div className="content">
                <p>비밀번호</p>
                <input
                    ref={(el) => inputRef.current.password1 = el}
                    name={'password1'}
                    type={'password'}
                    onChange={onInputChange}/>
                <input
                    ref={(el) => inputRef.current.password2 = el}
                    name={'password2'}
                    type={'password'}
                    onChange={onInputChange}/>
                {
                    isUpdate ? <button onClick={onClickPassowrdChange}>변경</button> : <></>
                }
                <span className={`Span_${checkPassword()}`}>
                    { checkPassword() ? (checkPassword() === 'POSITIVE' ? '비밀번호 일치' : '비밀번호 불일치') : "" }
                </span>
            </div>
            <div className="content">
                <p>이름</p>
                <input
                    ref={(el) => inputRef.current.name = el}
                    name={'memberName'}
                    type={'text'}
                    value={input.memberName}
                    onChange={onInputChange} />
            </div>
            <div className="content">
                <p>핸드폰번호</p>
                <input
                    ref={(el) => inputRef.current.ddd = el}
                    name={'ddd'}
                    type={'text'}
                    value={input.ddd}
                    onChange={onInputChange} />
                <input
                    ref={(el) => inputRef.current.tel1 = el}
                    name={'tel1'}
                    type={'text'}
                    value={input.tel1}
                    onChange={onInputChange} />
                <input
                    ref={(el) => inputRef.current.tel2 = el}
                    name={'tel2'}
                    type={'text'}
                    value={input.tel2}
                    onChange={onInputChange} />
            </div>
            <ButtonBox leftText={isUpdate ? '수정하기' : '가입하기'} leftOnClick={memberJoinSubmit} rightText={'돌아가기'} rightOnClick={() => nav(-1)}/>
        </div>
    );
};

export default Form;
