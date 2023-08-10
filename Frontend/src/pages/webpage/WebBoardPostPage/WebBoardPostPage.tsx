import { FC, useState, useEffect } from 'react';
import { WebBoardPostPageProps } from '.';
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { Button, FormControl, FormLabel, Input } from "@mui/joy";
import Option from "@mui/joy/Option";
import Select from "@mui/joy/Select";
import "./WebBoardPostPage.css";
import { boardAPI, noticeAPI } from "@/store/api/api";
import { BOARD_ENG, WebState } from '@/store/slice/web-slice';
import { useSelector } from 'react-redux';

export const WebBoardPostPage: FC<WebBoardPostPageProps> = (props) => {
	const [category, setCategory] = useState("");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const options: string[] = ["공지사항", "건의사항", "칭찬합니다", "불만사항"];

	const data: WebState = useSelector((state: { web: WebState }) => {
    return state.web;
  });

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
              Authorization: `Bearer ${data.Token}`,
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
            sort: `${BOARD_ENG[category]}`,
          },
          {
            headers: {
              Authorization: `Bearer ${data.Token}`,
            },
          }
        )
        .then((res) => {
          console.log(res.data);
        });
    }
  };
	return <div {...props}>
		<div>게시판 작성</div>
		<div>
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
		</div>
	</div>;
};