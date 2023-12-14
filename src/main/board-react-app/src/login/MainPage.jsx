import React, {useContext} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import styled from "styled-components";
import {AuthContext} from "./AuthContext";
import BoardList from "../board/BoardList";

function MainPage(props) {
    const navigate = useNavigate();


    return (
        <BoardList/>
    );
}

export default MainPage;