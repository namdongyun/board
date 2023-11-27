import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import styled from "styled-components";

const BoardContainer = styled.div`
  padding: 20px;
  background-color: #f8f9fa;
  margin: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
`;

const Title = styled.h1`
  font-size: 24px;
  color: #343a40;
`;

const Content = styled.p`
  font-size: 16px;
  color: #495057;
`;

const EditButton = styled.button`
  padding: 8px 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 15px;

  &:hover {
    background-color: #0059bb;
  }
`;

const DeleteButton = styled.button`
  padding: 8px 12px;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 15px;

  &:hover {
    background-color: #bd2130;
  }
`;

const BackButton = styled.button`
  padding: 8px 12px;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;

  &:hover {
    background-color: #5a6268;
  }
`;






// {match} : 현재 URL의 정보를 객체 형태로 컴포넌트에 전달하는 것을 의미합니다.
// 클릭한 게시글 id를 확인하기 위함 입니다.
function BoardView() {
    const [boardView, setBoardView] = useState("");
    const {id} = useParams(); // URL 파라미터에서 id를 추출합니다.

    const navigate = useNavigate();

    // 컴포넌트가 마운트될 때 게시글 정보를 불러옵니다.
    useEffect(() => {
        const fetchBoardView = async () => {
            try {
                const response = await axios.get(`/api/board/view/${id}`);
                setBoardView(response.data); // 서버로부터 받은 데이터로 상태를 업데이트합니다.
            } catch (error) {
                console.error('게시글을 불러오는 데 실패했습니다.', error);
            }
        };
        // async 함수는 자동으로 Promise 라는 특별한 객체를 반환하는데
        // useEffect의 return 값은 clean up 작업이기 때문에 따로 async 함수를 선언하고 아래에서 함수를 사용합니다.
        fetchBoardView(); //
    }, [id]);

    // 게시글 삭제 함수
    const handleDelete = async () => {
        if(window.confirm('정말로 글을 삭제하시겠습니까?')) {
            try {
                await axios.delete(`/api/board/delete/${id}`);

                console.log('글 삭제 성공');

                navigate('/board/list');
            } catch (error) {
                console.error('글 삭제에 실패했습니다.', error);
            }
        }
    }

    return (
        <div>
            <BoardContainer>
                <Title>{boardView.title}</Title>
                <Content>{boardView.content}</Content>
                <BackButton onClick={() => navigate('/board/list')}>글 목록</BackButton>
                <DeleteButton onClick={handleDelete}>글삭제</DeleteButton>
                <EditButton onClick={() => navigate(`/board/editPage/${id}`)}>글 수정</EditButton>
            </BoardContainer>
        </div>
    );
}

export default BoardView;