import Form from "../components/Form";
import Header from "../components/Header";

const MyPage = () => {
    return (
        <div>
            <Header title={'My Page'}/>
            <Form title={'수정하기'} isUpdate={true}/>
        </div>
    )
}

export default MyPage;