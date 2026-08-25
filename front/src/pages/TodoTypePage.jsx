import Header from "../components/Header";
import TodoType from "../components/TodoType";
import Token from "../hooks/Token";

const TodoTypePage = () => {
    
    return (
        <div>
            <Header title={"Todo Type"} />
            <TodoType/>
        </div>      
    );
};

export default TodoTypePage;