import Editor from "../components/Editor";
import Header from "../components/Header";
import useLoginConfirm from "../hooks/useLoginConfirm";

const New = () => {

    useLoginConfirm();

    return (
        <div className="New">
            <Header title={"새로운 투두"} />
            <Editor/>
        </div>
    );
};

export default New;

