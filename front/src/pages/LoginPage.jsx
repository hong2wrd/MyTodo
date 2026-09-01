import Login from '../components/Login';
import useToken from '../hooks/useToken';

const LoginPage = () => {
    useToken();
    return (
        <div>
            <Login/>
        </div>      
    );
};

export default LoginPage;