import { FC, useState } from "react";
import { PostBoardFormProps } from ".";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { Button, FormControl, FormLabel, Input } from "@mui/joy";
import Option from "@mui/joy/Option";
import Select from "@mui/joy/Select";
import "./PostBoardForm.css";
import { boardAPI, noticeAPI } from "@/store/api/api";

export const PostBoardForm: FC<PostBoardFormProps> = (props) => {
  const [category, setCategory] = useState("");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const options: string[] = ["공지사항", "건의사항", "칭찬합니다", "불만사항"];

  const handleCategoryChange = (value) => {
    console.log(value);
    setCategory(value);
  };

  const handleTitleChange = (event) => {
    setTitle(event.target.value);
  };

  const handleContentChange = (value) => {
    setContent(value);
  };

  const postPost = () => {
    // 머리말이 공지사항이면 noticeAPI 아니면 게시글
    if (category == "공지사항") {
      noticeAPI
        .post(
          "write",
          {
            context: `${content}`,
            title: `${title}`,
          },
          {
            headers: {
              Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJhZG1pbiIsImlhdCI6MTY5MTU2NzA3OSwiZXhwIjoxNjkxNTcwNjc5fQ.Lov_0wP71g49P70IP8m05oS6GVvoEwGtLkNGY1U-kaE`,
            },
          }
        )
        .then((res) => {
          console.log(res.data);
        });
    } else {
      boardAPI
        .post(
          "",
          {
            title: `${title}`,
            content: `${content}`,
            sort: `${category}`,
          },
          {
            headers: {
              Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJhZG1pbiIsImlhdCI6MTY5MTU2NzA3OSwiZXhwIjoxNjkxNTcwNjc5fQ.Lov_0wP71g49P70IP8m05oS6GVvoEwGtLkNGY1U-kaE`,
            },
          }
        )
        .then((res) => {
          console.log(res.data);
        });
    }
  };

  return (
    <div>
      <Select
        className="board-select"
        id="category"
        color="primary"
        placeholder="말머리 선택"
        size="md"
      >
        {options.map((op, index) => {
          return (
            <Option
              value={op}
              key={index}
              onClick={() => handleCategoryChange(op)}
            >
              {op}
            </Option>
          );
        })}
      </Select>
      <table className="tb_data tb_write">
        <tbody>
          <tr>
            <th>제목</th>
            <td colSpan={3}>
              <Input
                type="text"
                id="title"
                placeholder="제목을 입력해주세요."
                variant="outlined"
                value={title}
                onChange={handleTitleChange}
              />
            </td>
          </tr>
          <tr>
            <th>상세내용</th>
            <td colSpan={3}>
              <ReactQuill value={content} onChange={handleContentChange} />
              <p>{content.valueOf().length}/1000</p>
            </td>
          </tr>
          {/* <tr>
            <th>첨부파일</th>
            <td colSpan={3}>
              <input />
            </td>
          </tr> */}
        </tbody>
      </table>
      <div>
        <Button onClick={postPost}>등록</Button>
      </div>
    </div>
  );
};
