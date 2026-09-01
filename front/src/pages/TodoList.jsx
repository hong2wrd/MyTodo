import Header from '../components/Header';
import Todo from '../components/Todo';

const TodoList = () => {
    return (
        <div className="TodoList">
            <Header title={'Todo'}/>
            <Todo/>
        </div>
    );
};

export default TodoList;
