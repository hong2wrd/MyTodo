import './Todo.css';

import { useNavigate } from 'react-router-dom';
import { useContext, useEffect, useState } from 'react';
import API from '../hooks/API';
import { TodoStatusContext } from '../App';

const Todo = () => {
    const [ todos, setTodos ] = useState([]);
    const { info } = useContext(TodoStatusContext);
    const [ todoTypes, setTodoTypes ] = useState([]);
    const nav = useNavigate();

    useEffect(() => {
        if(info.isLogin) {
            API.get("/todoType")
            .then(res => {
                setTodoTypes(
                    res.data.data.map(todoType => {
                        return {
                            id: todoType.todoTypeId,
                            title: todoType.todoTypeTitle
                        }
                    }));
            }).then(() => {
                API.get(`/todo/${info.memberId}/0`)
                .then(res => {
                    const data = res.data.data;
                    setTodos(
                        data
                    )
                });
            })
        } else {
            setTodoTypes([]);
            setTodos([]);
        }



    }, [info]);

    const onChangeComplete = async (todoId, e) => {
        await API.patch(`/todo/${todoId}`);

        setTodos(
            todos.map(todo => {
                if(todo.todoId === todoId) {
                    todo.completed = !todo.completed
                }
                return todo;
            }).sort((a, b) => a.completed - b.completed)
        )
    }

    /**
     * TodoType 변경 시
     * @param {*} todoId 
     * @param {*} e 
     */
    const onChangeSelect = async (todoId, e) => {
        const todoTypeId = Number(e.target.value);
        await API.patch(`/todo/${todoId}/${todoTypeId}`);

        setTodos(
            todos.map(todo => {
                if(todo.todoId === todoId) {
                    todo.todoType.todoTypeId = todoTypeId;

                    todo.todoType.todoTypeTitle = todoTypes.find(type => {
                        if(type.id === todoTypeId) {
                            return type.title;
                        }
                        return "";
                    }).title
                }
                return todo;
            })
        )
    }

    const onClickContent = async (todoId, e) => {
        nav(`/edit/${todoId}`);
    }

    /**
     * 삭제 버튼 클릭 시
     * @param {*} e 
     */
    const onClickDelete = async (todoId, e) => {
        try {
            const response = await API.delete(`/todo/${todoId}`);

            window.alert(response.data.msg);

            setTodos(
                todos.filter(todo => todo.todoId !== todoId)
            );

        } catch(e) {
            window.alert(e.response.data.msg);
        }
    }

    return (
        <div className="Todo">
            <div className='todo_btn'>
                <button
                    className='POSITIVE'
                    onClick={() => nav("/new")}>
                        새로운 Todo 등록
                </button>
            </div>
            {
            todos.length === 0 ?
                <div className='todo_empty'>
                    <p>오늘 할일을 등록해주세요!</p>
                </div>
                : todos.map(todo => {
                    return (
                    <div className='todo_item' key={`todo_item${todo.todoId}`}>
                        <div className='item_checkbox'>
                            <input
                                type={'checkbox'}
                                checked={todo.completed}
                                onChange={(e) => onChangeComplete(todo.todoId, e)}
                                />
                        </div>
                        <div className='item_select'>
                            <select
                                onChange={(e) => onChangeSelect(todo.todoId, e)}
                                value={todo.todoType.todoTypeId}
                                key={`select_${todo.todoId}`}
                                >
                                {todoTypes.map(todoType => (
                                    <option
                                        value={Number(todoType.id)}
                                        key={todoType.id}>
                                        {todoType.title}
                                    </option>
                                ))}
                        </select>
                        </div>
                        <div className='item_content'>
                            <span
                                className={`${todo.completed ? "completed" : ""}`}
                                onClick={(e) => onClickContent(todo.todoId, e)}>
                                {todo.title}
                            </span>
                            <p>{todo.content}</p>
                        </div>
                        <div className='item_btn'>
                            <button
                                className='NEGATIVE'
                                onClick={(e) => onClickDelete(todo.todoId, e)}>
                                삭제
                            </button>
                        </div>
                    </div>
                    )
                })
            }
        </div>
    );
};

export default Todo;
