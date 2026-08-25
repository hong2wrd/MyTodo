import './HomePage.css';

import Header from '../components/Header';
import Button from '../components/Button';
import { useNavigate } from 'react-router-dom';

const HomePage = () => {
    const nav = useNavigate();
    return (
        <div className="Home">
            <Header title={'Home'}/>
            <Button onClick={() => nav('/todoType')}>{'todoType'}</Button>
        </div>
    );
};

export default HomePage;
