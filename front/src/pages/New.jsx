import Editor from "../components/Editor";
import Header from "../components/Header";
import Token from "../hooks/Token";

const New = () => {
    Token();

    return (
        <div>
            <Header title={"투두 리스트"} />
            <Editor/>
        </div>
    );
};

export default New;

