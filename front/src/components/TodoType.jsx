import "./TodoType.css";

import { useContext, useEffect, useState } from "react";
import API from "../hooks/API";
import Button from "./Button";
import { TodoStatusContext } from "../App";

const TodoType = () => {
    const { info, dispatch } = useContext(TodoStatusContext);
    const [ todoTypes, setTodoTypes ] = useState([]);

    useEffect(() => {
        API.get("/todoType")
            .then(res => {
                setTodoTypes(
                    res.data.data.map(todoType => {
                        return {
                            id: todoType.todoTypeId,
                            title: todoType.todoTypeTitle
                        }
                    }));
            });
    }, []);

    const onClickAddButton = (e) => {
        setTodoTypes([
            {   
                id: "",
                title: ""
            },
            ...todoTypes
        ])
    };

    const onClickSaveButton = async (e) => {
        const index = Number(e.target.getAttribute("index"));

        try {
                if(todoTypes[index].id) {
                    // 수정
                    const response = await API.patch('/todoType', {
                        memberId: info.memberId,
                        todoTypeId: todoTypes[index].id,
                        todoTypeTitle: todoTypes[index].title
                    });

                    if(response.status === 200) {
                        window.alert("수정되었습니다.")
                    }
                } else {
                    const response = await API.post('/todoType', {
                        memberId: info.memberId,
                        todoTypeTitle: todoTypes[index].title
                    });
                
                    setTodoTypes(
                        todoTypes.map((todoType, i) => {
                            if(i === index) {
                                todoType.id = response.data.data.todoTypeId
                            }
                            return todoType;
                        })
                    );

                    if(response.status === 201) {
                        window.alert("저장되었습니다.")
                    }
                }
        } catch(e) {
            const response = e.response;
            if(response.data) {
                window.alert(response.data.data.todoTypeTitle);
            }
        }
    }

    const onClickDelButton = async (e) => {
        const index = Number(e.target.getAttribute("index"));
        const todoTypeId = todoTypes[index].id;
        
        try {
            if(todoTypeId) {
                const response = await API.delete(`/todoType/${todoTypeId}`);

                setTodoTypes(
                    todoTypes.filter(todoType => todoType.id !== todoTypeId)
                )

                if(response.status === 200) {
                    window.alert("삭제되었습니다.");
                }
            } else {
                setTodoTypes(
                    todoTypes.filter((todoType, i) => i !== index)
                );
            }
            
        } catch(e) {
            const response = e.response;
            
            window.alert("에러가 발생하였습니다.")
        }
    };

    const onChangeTitle = (e) => {
        const index = Number(e.target.getAttribute("index"));
        const value = e.target.value;
        console.log(index);

        setTodoTypes(
            todoTypes.map((todoType, i) => {
                if(index === i) {
                    todoType.title = value;
                }

                return todoType;
            })
        )
    };

    return (
    <div className="TodoType">
        <div className="top">
            <Button text={'추가하기'} onClick={onClickAddButton} />
        </div>
        <div className="content">
            {
            todoTypes?.map((type, i) => (
                <div className="item">
                    <input
                        name={"todoTypeTitle"}
                        type={"text"}
                        value={type.title}
                        onChange={onChangeTitle}
                        index={i}
                    />
                    <div className="item_btn">
                        <button
                            onClick={onClickSaveButton}
                            key={`save_btn_${type.id}`}
                            index={i}
                        >{'저장하기'}</button>
                        <button
                            onClick={onClickDelButton}
                            key={`delete_btn_${type.id}`}
                            index={i}
                        >{'삭제하기'}</button>
                    </div>
                    
                </div>
                )
            )
            }
        </div>
    </div>
    );
};

export default TodoType;

