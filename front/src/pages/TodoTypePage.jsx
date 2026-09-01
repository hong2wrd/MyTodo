import Header from "../components/Header";
import TodoType from "../components/TodoType";
import useLoginConfirm from "../hooks/useLoginConfirm";

const TodoTypePage = () => {
    
    useLoginConfirm();

    return (
        <div>
            <Header title={"Todo Type"} />
            <TodoType/>
        </div>      
    );
};

export default TodoTypePage;