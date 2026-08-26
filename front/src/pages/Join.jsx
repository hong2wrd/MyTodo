import JoinForm from '../components/JoinForm';
import useToken from '../hooks/useToken';

const JoinPage = () => {
    useToken();
    return (
        <div>
            <JoinForm/>
        </div>
    );
}

export default JoinPage;
