import React, {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import styled from "styled-components";

const Container = styled.div`
  padding: 20px;
  max-width: 1000px;
  margin: 40px auto;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  background: #fff;
`;

const Title = styled.h1`
  color: #333;
  text-align: center;
  margin-bottom: 30px;
`;

const PostList = styled.ul`
  list-style: none;
  margin: 0;
  padding: 0;
`;

const PostHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px; // PostItem과 동일한 패딩 값으로 설정
  border-bottom: 2px solid #0366d6;
  background-color: #f8f9fa;
  font-weight: bold;
`;

const PostItem = styled.li`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px; // PostHeader와 동일한 패딩 값으로 설정
  border-bottom: 1px solid #eee;

  &:last-child {
    border-bottom: none;
  }
`;

const PostTitle = styled(Link)`
  text-decoration: none;
  color: #0366d6;
  font-size: 18px;

  &:hover {
    text-decoration: underline;
  }
`;

const WriteButton = styled(Link)`
  display: inline-block;
  padding: 10px 15px;
  margin-top: 20px;
  background-color: #5cb85c;
  color: white;
  border-radius: 4px;
  text-decoration: none;
  text-align: center;

  &:hover {
    background-color: #4cae4c;
  }
`;

const HomeButton = styled(Link)`
  display: inline-block;
  padding: 10px 15px;
  margin-top: 20px;
  background-color: #6c757d;
  color: white;
  border-radius: 4px;
  text-decoration: none;
  text-align: center;

  &:hover {
    background-color: #5a6268;
  }
`;

function BoardList(props) {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const response = await axios.get('/api/board/list');
                setPosts(response.data);
                console.log('글 리스트 불러오기 성공: ', response.data);
            } catch (error) {
                console.error('글 리스트 불러오는 중 오류 발생:', error.response || error);
            }
        };
        fetchPosts(); // 함수 호출
    }, []);

    return (
        <Container>
            <Title>게시판</Title>
            <PostHeader>
                <span>작성자</span>
                <span>제목</span>
                <span>날짜</span>
            </PostHeader>
            <PostList>
                {posts.map(post => (
                    <PostItem key={post.id}>
                        <span>{post.account_username}</span> {/* 작성자 username 을 표시 */}
                        <PostTitle to={`/board/view/${post.id}`}>{post.title}</PostTitle>
                        <span>{new Date(post.createdAt).toLocaleString('ko-KR', {
                            year: '2-digit', // 연도: 'numeric'(2019), '2-digit'(19)
                            month: 'numeric', // 월: 'numeric'(12), '2-digit'(12), 'long'(12월), 'short'(12월), 'narrow'(12월)
                            day: 'numeric', // 일: 'numeric'(31), '2-digit'(31)
                            hour: 'numeric', // 시: 'numeric'(13), '2-digit'(13)
                            minute: 'numeric', // 분: 'numeric'(9), '2-digit'(09)
                            hour12: false, // 12시간제 여부: true(오전/오후), false(24시간제)
                        })}</span>
                    </PostItem>
                ))}
            </PostList>
            <WriteButton to={"/board/write"}>글쓰기</WriteButton >
            <HomeButton to={"/"}>메인화면으로</HomeButton>
        </Container>
    );
}

export default BoardList;