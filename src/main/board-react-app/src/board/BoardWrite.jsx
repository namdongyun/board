import React, {useContext, useState} from 'react';
import axios from 'axios';
import styled from 'styled-components';
import {useNavigate} from "react-router-dom";
import {AuthContext} from "../login/AuthContext";
import api from "../axiosInterceptor/api";

const Container = styled.div`
  padding: 20px;
  max-width: 600px;
  margin: auto;
`;

const TitleInput = styled.input`
  width: 100%;
  margin-bottom: 10px;
  padding: 10px;
  font-size: 16px;
`;

const ContentTextArea = styled.textarea`
  width: 100%;
  height: 200px;
  margin-bottom: 10px;
  padding: 10px;
  font-size: 16px;
`;

const SubmitButton = styled.button`
  width: 100%;
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: #45a049;
  }
`;

function BoardWrite(props) {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');

    // 경로 이동 함수
    const navigate = useNavigate();

    const handleTitleChange = (e) => {
        setTitle(e.target.value);
    };

    const handleContentChange = (e) => {
        setContent(e.target.value);
    };

    // 글 쓰기 완료
    const handleSubmit = async (e) => {
        e.preventDefault();
        const boardData = {title, content};

        try {
            const response = await api.post('/api/board/write', boardData);

            console.log('글 작성 처리 성공', response);

            const postId = response.data.id;

            navigate(`/board/View/${postId}`);
        } catch (error) {
            console.error('요청 처리 중 오류 발생', error.response || error);
        }
    };

    return (
        <Container>
            <h1>글쓰기</h1>
            <form onSubmit={handleSubmit}>
                <TitleInput
                    type="text"
                    placeholder="제목을 입력하세요"
                    value={title}
                    onChange={handleTitleChange}
                />
                <ContentTextArea
                    placeholder="내용을 입력하세요"
                    value={content}
                    onChange={handleContentChange}
                />
                <SubmitButton type="submit">글쓰기</SubmitButton>
            </form>
        </Container>
    );
}

export default BoardWrite;