import Form from '../components/Form';
import useToken from '../hooks/useToken';

const JoinPage = () => {
    useToken();
    return (
        <div>
            <Form title={'가입하기'} isUpdate={false}/>
        </div>
    );
}

export default JoinPage;
