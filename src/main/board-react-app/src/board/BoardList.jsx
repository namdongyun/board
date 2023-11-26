import React, {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";

function BoardList(props) {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        axios.get('/api/board/list').then(response => setPosts(response.data));
    }, []);

    return (
        <div>
            <h1>게시판</h1>
            <ul>
                {posts.map(post => (
                    <li key={post.id}>
                        <Link to={`/board/view/${post.id}`}>{post.title}</Link>
                    </li>
                ))}
            </ul>
            <Link to={"/post/write"}>글쓰기</Link>
        </div>
    );
}

export default BoardList;