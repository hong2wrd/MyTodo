import './Input.css';

const Input = ({ type, name, onChange, ref }) => {
    return <input ref={ref} type={type} name={name} onChange={onChange}/>
};

export default Input;