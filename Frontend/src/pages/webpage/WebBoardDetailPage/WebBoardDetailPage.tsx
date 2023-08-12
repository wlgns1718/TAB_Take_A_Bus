import { FC, useEffect, useState } from "react";
import { WebBoardDetailPageProps } from ".";
import {
  BOARD_KOR,
  BoardData,
  WebState,
  changeSelectedPostId,
  deleteOneBoard,
} from "@/store/slice/web-slice";
import { boardAPI } from "@/store/api/api";
import { Container, IconButton, Typography } from "@mui/material";
import { Button, Input, Stack } from "@mui/joy";
import ArrowBackOutlinedIcon from "@mui/icons-material/ArrowBackOutlined";
import { useDispatch, useSelector } from "react-redux";
import { prettyTime } from "../WebBoardPage";

export const WebBoardDetailPage: FC<WebBoardDetailPageProps> = ({ postId }) => {
  const [boardDetailData, setBoardDetailData] = useState<BoardData | null>();

  const data: WebState = useSelector((state: { web: WebState }) => {
    return state.web;
  });

  const dispatch = useDispatch();

  const [commentContent, setCommentContent] = useState("");

  const handleCommentContent = (e) => {
    setCommentContent(e.target.value);
  };

  const postComment = () => {
    if (!commentContent.trim()) {
      alert("내용을 입력해주세요");
      return;
    }
    boardAPI
      .post(
        `${postId}/comment/`,
        {
          content: commentContent,
        },
        {
          headers: {
            Authorization: `Bearer ${data.Token}`,
          },
        }
      )
      .then((response) => {
        console.log(response.data);
        updateDetailData();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const updateDetailData = () => {
    boardAPI
      .get(`${postId}`)
      .then((response) => {
        console.log(response.data);
        if (response.data.code == "200") {
          setBoardDetailData(response.data.data);
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };

  useEffect(() => {
    // const postId = params.postId;
    console.log(postId);
    updateDetailData();
  }, [postId]);

  const deleteBoard = () => {
    if (!data.Token) {
      alert("로그인이 필요한 기능입니다.");
      return;
    }
    if (!confirm("게시글을 삭제하시겠습니까?")) {
      return;
    }
    boardAPI
      .delete(`${boardDetailData.id}`, {
        headers: { Authorization: `Bearer ${data.Token}` },
      })
      .then((response) => {
        if(response.data.code === 401) {
          alert("본인의 게시글만 삭제할 수 있습니다.");
          return;
        } else {
          console.log(response.data.code);
          dispatch(deleteOneBoard(boardDetailData.id));
          dispatch(changeSelectedPostId(null));
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const deleteComment = (commentId) => {
    if (!data.Token) {
      alert("로그인이 필요한 기능입니다.");
      return;
    }
    if (!confirm("댓글을 삭제하시겠습니까?")) {
      return;
    }
    boardAPI
      .delete(`${postId}/comment/${commentId}`, {
        headers: {
          Authorization: `Bearer ${data.Token}`,
        },
      })
      .then((response) => {
        console.log(response.data);
        updateDetailData();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  if (!boardDetailData) {
    return <div></div>;
  }

  return (
    <div>
      <Container maxWidth="xl" sx={{ paddingTop: 8 }}>
        <div className="detail-header">
          <IconButton
            onClick={() => {
              dispatch(changeSelectedPostId(null));
            }}
          >
            <ArrowBackOutlinedIcon fontSize="large" />
          </IconButton>
          <div style={{ fontSize: "30px", fontWeight: "bold" }}>게시판</div>
        </div>
        <div className="detail">
          <Typography variant="h4" sx={{ margin: 5 }}>
            {boardDetailData?.title}
          </Typography>

          <div>{`작성자 : ${boardDetailData?.userId}`}</div>
          <div>{BOARD_KOR[boardDetailData.sort]}</div>

          <div>{`작성시간 : ${prettyTime(boardDetailData?.createTime)}`}</div>
          {/* html 코드 출력 */}
          <div
            dangerouslySetInnerHTML={{ __html: boardDetailData?.content }}
          ></div>
        </div>
      </Container>
      <Container maxWidth="xl">
        <div className="bottom-buttons">
          <Button color="neutral" onClick={function () {}} variant="soft">
            수정
          </Button>
          <Button color="neutral" onClick={deleteBoard} variant="soft">
            삭제
          </Button>
        </div>
      </Container>
      <Container maxWidth="xl" sx={{ paddingTop: 2 }}>
        <span style={{ fontSize: 16 }}>댓글</span>{" "}
        <span style={{ fontSize: 12 }}>
          {boardDetailData.commentResponseDtoList
            ? boardDetailData.commentResponseDtoList.length
            : 0}
          개
        </span>
        <hr />
        <div>
          {boardDetailData.commentResponseDtoList == null ||
          boardDetailData.commentResponseDtoList.length == 0 ? (
            <div>댓글이 아직 없습니다..</div>
          ) : (
            boardDetailData.commentResponseDtoList.map((el, index) => {
              return (
                <Container maxWidth="xl" sx={{ paddingTop: 8, height: 140 }}>
                  <Stack direction={"row"}>
                    <div>{el.userId}</div>
                    <div>{prettyTime(el.createTime)}</div>|
                    <Button size="sm" variant="soft">
                      수정
                    </Button>
                    |<Button onClick={() => deleteComment(el.id)}>삭제</Button>
                  </Stack>
                  <div>{el.content}</div>
                </Container>
              );
            })
          )}
        </div>
        <Stack direction={"row"} maxWidth="xl" sx={{ paddingTop: 8 }}>
          <Input
            size="md"
            placeholder="내용을 입력하세요.."
            variant="outlined"
            color="neutral"
            onChange={(value) => handleCommentContent(value)}
          ></Input>
          <Button variant="soft" color="neutral" onClick={postComment}>
            {" "}
            등록{" "}
          </Button>
        </Stack>
      </Container>
    </div>
  );
};
