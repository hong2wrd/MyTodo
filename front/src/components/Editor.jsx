import { useEffect } from "react";
import API from "../hooks/API";

const Editor = () => {

    useEffect(async () => {
        try {
            const response = await API.get("/todoType/list");

        } catch(e) {
            console.log(e);
        }
    }, []);

    return (
        <div className="Editor">
            <div>
                <p>할일</p>
                <input type={"text"}/>
            </div>
            <div>
                <p>내용</p>
                <textarea/>
            </div>
            <div>
                <select></select>
            </div>
        </div>
    );
}

export default Editor;