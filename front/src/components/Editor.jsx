import { useContext, useEffect, useState } from "react";
import API from "../hooks/API";
import './Editor.css';
import { TodoStatusContext } from "../App";
import { useNavigate, useParams } from "react-router-dom";


const Editor = () => {
    const nav = useNavigate();
    const params = useParams();
    const { info } = useContext(TodoStatusContext);
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
            const todoId = Number(params.todoId);

            const requestBody = {
                ...todo,
                memberId: info.memberId,
                todoId: todoId
            };

            const response = todoId ? await API.put('/todo', requestBody) : await API.post('/todo', requestBody);

            if([201, 200].includes(response.status)) {
                window.alert(response.data.msg);
                nav("/",{replace: true})
            }
        } catch(e) {
            window.alert("Todo 처리에 문제가 발생하였습니다.");
        }
    }

    useEffect(() => {
        API.get("/todoType")
            .then(res => {
                const data = res.data.data;
                
                setTodoTypes(data);

                if(data.length > 0) {
                    setTodo({...todo, todoType: Number(data[0].todoTypeId)})
                }
            })
            .then(() => {
                const todoId = params.todoId;
                if(todoId) {
                    API.get(`/todo/${todoId}`)
                        .then(res => {
                            const data = res.data.data
                            setTodo({
                                ...res.data.data,
                                todoType: data.todoType.todoTypeId
                            });
                    });
                }
            })
            .catch(e => {
                window.alert("Todo 조회에 문제가 발생하였습니다.");
            });
    }, []);

    return (
        <div className="Editor">
            <div className="content">
                <select onChange={onChangeSelect} value={todo.todoType}>
                    {todoTypes.map(todoType => (
                        <option
                            key={todoType.todoTypeId}
                            value={Number(todoType.todoTypeId)}>
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
                    value={todo.title}
                    onChange={onChangeInput}
                />
            </div>
            <div className="content">
                <p>내용</p>
                <textarea
                    maxLength={100}
                    placeholder='상세 내역을 적어주세요.'
                    value={todo.content}
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
