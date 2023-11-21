import React from "react";
import Book from "./book";

function Library(props) {
    return (
        <div>
            <Book name="1" numOfPage={100} />
            <Book name="2" numOfPage={200} />
            <Book name="3" numOfPage={300} />
        </div>
    );
}

export default Library;