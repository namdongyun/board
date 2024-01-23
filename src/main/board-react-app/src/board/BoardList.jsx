import React, {useEffect, useState} from "react";
import axios from "axios";
import {Link, useNavigate} from "react-router-dom";
import styled from "styled-components";
import {
    Button,
    Pagination,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TablePagination,
    TableRow,
    Typography
} from "@mui/material";
import Box from "@mui/material/Box";

export default function BoardList() {
    const navigate = useNavigate();
    const [posts, setPosts] = useState([]);
    // 페이지네이션 상태
    const [page, setPage] = useState(0); // 페이지 번호는 0부터 시작
    const [totalPages, setTotalPages] = useState(0); // 전체 페이지 수
    const [order, setOrder] = useState('desc');
    const [orderBy, setOrderBy] = useState('createdAt'); // 기본 정렬 필드
    const rowsPerPage = 8; // 한 페이지에 표시할 게시글 수

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                // 백엔드에서 페이징 처리된 데이터를 받아옵니다.
                const response = await axios.get(`/api/board/list?page=${page}&size=${rowsPerPage}&sort=${orderBy},${order}`);
                setPosts(response.data.content); // 현재 페이지의 게시글
                setTotalPages(response.data.totalPages); // 전체 페이지 수 설정

                console.log('글 리스트 불러오기 성공: ', response.data);
            } catch (error) {
                console.error('글 리스트 불러오는 중 오류 발생:', error.response || error);
            }
        };
        fetchPosts(); // 함수 호출
    }, [page, order, orderBy]);

    // 정렬 상태 변경 함수
    const handleSortRequest = (cellId) => {
        const isAsc = orderBy === cellId && order === 'asc';
        setOrder(isAsc ? 'desc' : 'asc');
        setOrderBy(cellId);
    };

    // 페이지네이션 상태 변경
    const handleChange = (event, value) => {
        // MUI Pagination 컴포넌트는 1부터 시작하지만 백엔드는 0부터 시작하므로 조정합니다.
        setPage(value - 1);
    };

    // 게시글 row 클릭 시
    const handleRowClick = (id) => {
        navigate(`/board/view/${id}`);
    };

    return (
        <Box>
            <TableContainer component={Paper} sx={{
                boxShadow: 'none',
                borderRadius: '12px',
            }}>
                <Table aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            {/*<TableCell>번호</TableCell>*/}
                            <TableCell onClick={() => handleSortRequest('title')} style={{ width: "50%" }}>제목</TableCell>
                            <TableCell style={{ width: "20%" }}>작성자</TableCell>
                            <TableCell onClick={() => handleSortRequest('createdAt')} style={{ width: "20%" }}>작성일</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {posts.map((post) => (
                            <TableRow key={post.id} onClick={() => handleRowClick(post.id)} hover style={{ textDecoration: 'none' }}>
                                {/*<TableCell component="th" scope="row">*/}
                                {/*    {post.id}*/}
                                {/*</TableCell>*/}
                                <TableCell>{post.title}</TableCell>
                                <TableCell>{post.accountNickname}</TableCell>
                                <TableCell>
                                    {new Date(post.createdAt).toLocaleString('ko-KR', {
                                                        year: '2-digit', // 연도: 'numeric'(2019), '2-digit'(19)
                                                        month: 'numeric', // 월: 'numeric'(12), '2-digit'(12), 'long'(12월), 'short'(12월), 'narrow'(12월)
                                                        day: 'numeric', // 일: 'numeric'(31), '2-digit'(31)
                                                        hour: 'numeric', // 시: 'numeric'(13), '2-digit'(13)
                                                        minute: 'numeric', // 분: 'numeric'(9), '2-digit'(09)
                                                        hour12: false, // 12시간제 여부: true(오전/오후), false(24시간제)
                                    })}
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <Box display="flex" justifyContent="center" my={2}>
                <Pagination
                    count={totalPages}
                    page={page + 1} // MUI Pagination 컴포넌트는 1부터 시작하므로 조정합니다.
                    onChange={handleChange}
                    showFirstButton
                    showLastButton
                />
            </Box>
            <Box display="flex" justifyContent="flex-end" mb={2}>
                <Button variant="contained" color="primary" component={Link}  to={`/board/write`}>
                    글쓰기
                </Button>
            </Box>
        </Box>
    );
}