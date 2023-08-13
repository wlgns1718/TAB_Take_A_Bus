import { FC, useState, useEffect, useRef } from "react";
import { WebBoardUpdatePageProps } from ".";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { Button, Input } from "@mui/joy";
import { boardAPI, noticeAPI } from "@/store/api/api";
import {
  BOARD_ENG,
  WebState,
} from "@/store/slice/web-slice";
import { useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";

export const WebBoardUpdatePage: FC<WebBoardUpdatePageProps> = (props) => {
  const [category, setCategory] = useState("");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const navigate = useNavigate();

  const params = useParams();

  const data: WebState = useSelector((state: { web: WebState }) => {
    return state.web;
  });
  const handleTitleChange = (event) => {
    setTitle(event.target.value);
  };

  const handleContentChange = (value) => {
    setContent(value);
  };

  const backToList = () => {
    navigate(-1);
  };

  useEffect(() => {}, []);

  useEffect(() => {
    if (!data.isUserIn) {
      alert("로그인이 필요한 기능입니다");
      if (confirm("로그인 화면으로 이동하시겠습니까?")) {
        navigate("../../login");
        return;
      }
      navigate(-1);
      return;
    }
    setCategory(params.category);
    if (params.category == "공지사항") {
      noticeAPI
        .get(`detail/${params.postId}`)
        .then((response) => {
          console.log(response.data);
          if (response.data.code == "200") {
            setTitle(response.data.data.title);
            setContent(response.data.data.content);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    } else {
      boardAPI
        .get(`${params.postId}`)
        .then((response) => {
          console.log(response.data);
          if (response.data.code == "200") {
            setTitle(response.data.data.title);
            setContent(response.data.data.content);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }, []);

  const updatePost = () => {
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
    if (!data.isUserIn) {
      alert("로그인이 필요한 기능입니다");
      if (confirm("로그인 화면으로 이동하시겠습니까?")) {
        navigate("../../login");
        return;
      }
      navigate(-1);
      return;
    }
    // 머리말이 공지사항이면 noticeAPI 아니면 게시글
    if (category == "공지사항") {
      noticeAPI
        .put(
          `modify/${params.postId}`,
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
					if(res.data.code == 200){
						alert("공지사항이 정상적으로 수정되었습니다")
					}
          navigate(-1);
        })
        .catch((error) => {
          console.log(error);
        });
    } else {
      boardAPI
        .put(
          `${params.postId}`,
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
					if(res.data.code == 200){
						alert("게시글이 정상적으로 수정되었습니다");
					}
          navigate(-1);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };
  // 포커스 이동
  const quillRef = useRef(null);

  const handleTdClick = () => {
    if (quillRef.current) {
      quillRef.current.focus();
    }
  };

  return (
    <div {...props}>
      <div>
        <div style={{ textAlign: "center", fontSize: 30 }}>게시글 수정</div>
        <div>
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
                <td colSpan={3} onClick={handleTdClick}>
                  <ReactQuill
                    value={content}
                    onChange={handleContentChange}
                    ref={quillRef}
                  />
                  <p>{content.valueOf().length}/1000</p>
                </td>
              </tr>
            </tbody>
          </table>
          <div className="post-bottom-buttons">
            <Button onClick={updatePost}>수정</Button>
            <Button onClick={backToList}>취소</Button>
          </div>
        </div>
      </div>
    </div>
  );
};
