import './ButtonBox.css';
import Button from './Button';

const ButtonBox = ({ leftText, leftOnClick, rightText, rightOnClick }) => {
    return (
        <div className="ButtonBox">
            <Button text={leftText} onClick={leftOnClick}/>
            <Button text={rightText} onClick={rightOnClick}/>
        </div>
    )
};

export default ButtonBox;