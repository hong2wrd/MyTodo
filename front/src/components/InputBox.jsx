import './InputBox.css';

const InputBox = ({ text, inputType, max }) => {
    return (
    <div className="InputBox">
        <p>{text}</p>
        <input type={inputType} max={max}></input>
    </div>
    )
    
}

export default InputBox;