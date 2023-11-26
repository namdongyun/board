import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import axios from "axios";

// {match} : 현재 URL의 정보를 객체 형태로 컴포넌트에 전달하는 것을 의미합니다.
// 클릭한 게시글 id를 확인하기 위함 입니다.
function BoardView() {
    const [boardView, setBoardView] = useState("");
    const {id} = useParams(); // URL 파라미터에서 id를 추출합니다.

    // 컴포넌트가 마운트될 때 게시글 정보를 불러옵니다.
    useEffect(() => {
        const fetchBoardView = async () => {
            try {
                const response = await axios.get(`/api/board/view?id=${id}`);
                setBoardView(response.data); // 서버로부터 받은 데이터로 상태를 업데이트합니다.
            } catch (error) {
                console.error('게시글을 불러오는 데 실패했습니다.', error);
            }
        };
        // async 함수는 자동으로 Promise 라는 특별한 객체를 반환하는데
        // useEffect의 return 값은 clean up 작업이기 때문에 따로 async 함수를 선언하고 아래에서 함수를 사용합니다.
        fetchBoardView(); //
    }, [id]);

    return (
        <div>
            <h1>{boardView.title}</h1>
            <p>{boardView.content}</p>
            <button>글삭제</button>
            <div>
                <a href={`/board/editPage/${boardView.id}`}>글 수정</a>
            </div>
        </div>
    );
}

export default BoardView;