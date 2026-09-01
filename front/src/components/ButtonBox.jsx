import './ButtonBox.css';

const ButtonBox = ({ leftText, leftOnClick, rightText, rightOnClick }) => {
    return (
        <div className="ButtonBox">
            <button onClick={leftOnClick}>{leftText}</button>
            <button onClick={rightOnClick}>{rightText}</button>
        </div>
    )
};

export default ButtonBox;