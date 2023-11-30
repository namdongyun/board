import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import styled from "styled-components";

// 스타일 컴포넌트 정의
const Container = styled.div`
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
`;

const Title = styled.h2`
  text-align: center;
  color: #333;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
`;

const Label = styled.label`
  margin-bottom: 5px;
  color: #666;
`;

const Input = styled.input`
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
`;

const SubmitButton = styled.button`
  padding: 10px 20px;
  background-color: #5c6bc0;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: #3f51b5;
  }
`;

function RegisterPage(props) {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');

    // 경로 이동 함수
    const navigate = useNavigate();

    const handleUsernameChange = (e) => {
        setUsername(e.target.value);
    };

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    };

    const handleEmailChange = (e) => {
        setEmail(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const accountDTO = {
            username,
            password,
            email
        };

        try {
            const response = await axios.post('/api/register', accountDTO);
            // 회원가입 성공 시 처리
            console.log('회원가입 성공:', response);
            navigate('/');
        } catch (error) {
            // 회원가입 실패 시 처리
            console.error('회원가입 실패:', error.response || error);
        }
    };

    return (
        <Container>
            <Title>회원가입</Title>
            <Form onSubmit={handleSubmit}>
                <div>
                    <Label>아이디:</Label>
                    <Input type="text" value={username} onChange={handleUsernameChange} />
                </div>
                <div>
                    <Label>비밀번호:</Label>
                    <Input type="password" value={password} onChange={handlePasswordChange} />
                </div>
                <div>
                    <Label>이메일:</Label>
                    <Input type="email" value={email} onChange={handleEmailChange} />
                </div>
                <SubmitButton type="submit">회원가입 완료</SubmitButton>
            </Form>
        </Container>
    );
}

export default RegisterPage;