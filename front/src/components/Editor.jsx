import { useContext, useEffect, useState } from "react";
import API from "../hooks/API";
import './Editor.css';
import { TodoStatusContext } from "../App";
import { useNavigate } from "react-router-dom";


const Editor = () => {
    const nav = useNavigate();
    const { info, dispatch } = useContext(TodoStatusContext);
    const [ todoTypes, setTodoTypes ] = useState([]);
    const [ todo, setTodo ] = useState(
        {
            title : "",
            content : "",
            todoType : ""
        }
    );

    const onChangeSelect = (e) => {
        const value = e.target.value;

        setTodo({
            ...todo,
            todoType : value
        });
    };

    const onChangeInput = (e) => {
        const value = e.target.value;

        setTodo({
            ...todo,
            title : value
        });
    };

    const onChangeTextarea = (e) => {
        const value = e.target.value;

        setTodo({
            ...todo,
            content : value
        });
    };

    const onSubmit = async (e) => {

        if(!todo.title) {
            window.alert("할일을 작성해주세요.");
            return;
        }

        try {
            const response = await API
                .post('/todo',
                    {
                        ...todo,
                        memberId: info.memberId
                    }
                )
            console.log(response);
            if(response.status === 201) {
                window.alert(response.data.msg);
                nav("/",{replace: true})
            }
        } catch(e) {
            console.log(e)
        }
    }

    useEffect(() => {
        try {
            API.get("/todoType")
            .then(res => {
                const data = res.data.data;
                
                setTodoTypes(data);
                console.log(data)

                if(data.length > 0) {
                    setTodo({...todo, todoType: Number(data[0].todoTypeId)})
                }
            });
        } catch(e) {
            console.log(e);
        }
    }, []);

    return (
        <div className="Editor">
            <div className="content">
                <select onChange={onChangeSelect} value={todo.todoType}>
                    {todoTypes.map(todoType => (
                        <option value={Number(todoType.todoTypeId)}>
                            {todoType.todoTypeTitle}
                        </option>
                    ))}
                </select>
            </div>
            <div className="content">
                <p>할일</p>
                <input
                    type={"text"}
                    maxLength={20}
                    placeholder='할일을 적어주세요.'
                    onChange={onChangeInput}
                />
            </div>
            <div className="content">
                <p>내용</p>
                <textarea
                    maxLength={100}
                    placeholder='상세 내역을 적어주세요.'
                    onChange={onChangeTextarea}
                />
            </div>
            <div className="content btn">
                <button className="POSITIVE" onClick={onSubmit}>추가하기</button>
            </div>
        </div>
    );
}

export default Editor;