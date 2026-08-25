import Login from '../components/Login';
import Token from '../hooks/Token';

const LoginPage = () => {
    Token();
    return (
        <div>
            <Login/>
        </div>      
    );
};

export default LoginPage;