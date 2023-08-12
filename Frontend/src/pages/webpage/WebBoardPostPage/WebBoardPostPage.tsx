import { FC, useState, useEffect } from "react";
import { WebBoardPostPageProps } from ".";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { Button, FormControl, FormLabel, Input } from "@mui/joy";
import Option from "@mui/joy/Option";
import Select from "@mui/joy/Select";
import "./WebBoardPostPage.css";
import { boardAPI, noticeAPI } from "@/store/api/api";
import { BOARD_ENG, WebState } from "@/store/slice/web-slice";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

export const WebBoardPostPage: FC<WebBoardPostPageProps> = (props) => {
  const [category, setCategory] = useState("");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const navigate = useNavigate();

  const options: string[] = ["공지사항", "건의사항", "칭찬합니다", "불만사항"];

  const data: WebState = useSelector((state: { web: WebState }) => {
    return state.web;
  });

  const handleCategoryChange = (value) => {
    setCategory(value);
  };

  const handleTitleChange = (event) => {
    setTitle(event.target.value);
  };

  const handleContentChange = (value) => {
    setContent(value);
  };

  const backToList = () => {
    navigate(-1);
  };

  useEffect(() => {
    // 로그인 체크
  }, []);

  const postPost = () => {
    if (!category) {
      alert("말머리를 선택해주세요");
      return;
    }
    const sendTitle = title.trim();
    console.log(sendTitle);

    if (!sendTitle) {
      alert("제목을 입력해주세요");
      return;
    }
    // HTML로 된 문자열에서 태그를 지우고 검사
    const sendContent = content.trim();
    const sendContentwithoutTags = sendContent.replace(/<[^>]*>/g, "");
    if (!sendContentwithoutTags.trim()) {
      alert("상세내용을 입력해주세요");
      return;
    }
    // 머리말이 공지사항이면 noticeAPI 아니면 게시글
    if (category == "공지사항") {
      noticeAPI
        .post(
          "write",
          {
            title: `${sendTitle}`,
            context: `${sendContent}`,
          },
          {
            headers: {
              Authorization: `Bearer ${data.Token}`,
            },
          }
        )
        .then((res) => {
          console.log(res.data);
          navigate(-1);
        });
    } else {
      boardAPI
        .post(
          "",
          {
            title: `${sendTitle}`,
            content: `${sendContent}`,
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
          navigate(-1);
        });
    }
  };
  return (
    <div {...props}>
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
          <div className="post-bottom-buttons">
            <Button onClick={postPost}>등록</Button>
            <Button onClick={backToList}>취소</Button>
          </div>
        </div>
      </div>
    </div>
  );
};
