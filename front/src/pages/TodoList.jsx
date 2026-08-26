import './TodoList.css';

import Header from '../components/Header';
import { useNavigate } from 'react-router-dom';
import { useContext, useEffect, useState } from 'react';
import API from '../hooks/API';
import { TodoStatusContext } from '../App';

const TodoList = () => {
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

    const onChangeComplete = async (e) => {
        const todoId = Number(e.target.value);
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

    const onChangeSelect = async (e) => {
        
        const todoId = Number(e.target.getAttribute("todoId"));
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

    const onClickContent = async (e) => {

    }

    const onClickDelete = async (e) => {
        const todoId = Number(e.target.value);
        try {
            const response = await API.delete(`/todo/${todoId}`);
            console.log(response);

            window.alert(response.data.msg);

            setTodos(
                todos.filter(todo => todo.todoId !== todoId)
            )

        } catch(e) {
            window.alert(e.response.data.msg);
        }

    }

    return (
        <div className="TodoList">
            <Header title={'Todo'}/>
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
                    <div className='todo_item'>
                        <div>
                            <input
                                type={'checkbox'}
                                checked={todo.completed}
                                value={todo.todoId}
                                onChange={onChangeComplete}/>
                        </div>
                        <div>
                            <select
                                onChange={onChangeSelect}
                                value={todo.todoType.todoTypeId}
                                todoId={todo.todoId}
                                key={`select_${todo.todoId}`}>
                                {todoTypes.map(todoType => (
                                    <option value={Number(todoType.id)}>
                                        {todoType.title}
                                    </option>
                                ))}
                        </select>
                        </div>
                        <div className='item_content'>
                            <span onClick={onClickContent}>
                                {todo.title}
                            </span>
                            <p >{todo.content}</p>
                        </div>
                        <div>
                            <button
                                className='NEGATIVE'
                                value={todo.todoId}
                                onClick={onClickDelete}
                                >
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

export default TodoList;
