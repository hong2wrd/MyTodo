import Login from '../components/Login';
import TokenCheck from '../hooks/TokenCheck';

const LoginPage = () => {
    TokenCheck();
    return (
        <div>
            <Login/>
        </div>      
    );
};

export default LoginPage;