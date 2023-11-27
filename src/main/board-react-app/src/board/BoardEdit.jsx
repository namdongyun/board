import React, {useEffect, useState} from "react";
import styled from "styled-components";
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";

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

function BoardEdit(props) {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const {id} = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        // 컴포넌트가 마운트될 때 기존 글의 내용을 불러옵니다.
        axios.get(`/api/board/view/${id}`)
            .then(response => {
                setTitle(response.data.title);
                setContent(response.data.content);
            })
            .catch(error => {
                console.log('글 정보를 불러오는 중 오류 발생: ', error);
            })
    }, [id]);

    const handleTitleChange = (e) => {
        setTitle(e.target.value);
    }

    const handleContentChange = (e) => {
        setContent(e.target.value);
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        const boardData = {title, content};

        axios.put(`/api/board/update/${id}`, boardData, {
            withCredentials: true
        })
            .then(response => {
                console.log('글 수정 요청 처리 성공 : ', response);
                
                // 수정 후 처리, 예를 들어 수정된 게시글로 라우팅하거나 메시지 표시
                navigate(`/board/view/${id}`);
            })
            .catch(error => {
                // 에러 처리
                console.error('글 수정 요청 처리 중 오류 발생:', error.response || error);
            });
    };

    return (
        <Container>
            <h1>글 수정하기</h1>
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
                <SubmitButton type="submit">수정하기</SubmitButton>
            </form>
        </Container>
    );
}

export default BoardEdit;