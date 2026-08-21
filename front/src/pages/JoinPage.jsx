import Join from '../components/Join';
import TokenCheck from '../hooks/TokenCheck';

const JoinPage = () => {
    TokenCheck();
    return (
        <>
        <Join/>
        </>
    );
}

export default JoinPage;
