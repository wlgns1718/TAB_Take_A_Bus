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
import { Button } from "@mui/joy";
import ArrowBackOutlinedIcon from "@mui/icons-material/ArrowBackOutlined";
import { useDispatch, useSelector } from "react-redux";
import { prettyTime } from "../WebBoardPage";

export const WebBoardDetailPage: FC<WebBoardDetailPageProps> = ({ postId }) => {
  const [boardDetailData, setBoardDetailData] = useState<BoardData | null>();

  const data: WebState = useSelector((state: { web: WebState }) => {
    return state.web;
  });

  const dispatch = useDispatch();

  useEffect(() => {
    // const postId = params.postId;
    console.log(postId);
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
        console.log(response.data);
        dispatch(deleteOneBoard(boardDetailData.id));
        dispatch(changeSelectedPostId(null));
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
      <Container maxWidth="xl" sx={{ paddingTop: 10 }}>
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
        <h3> 댓글 영역</h3>
        <div>
          {boardDetailData.commentResponseDtoList != null ||
          boardDetailData.commentResponseDtoList.length != 0 ? (
            boardDetailData.commentResponseDtoList.map((el, index) => {
              return <div>dd</div>;
            })
          ) : (
            <div>댓글이 아직 없습니다..</div>
          )}
        </div>
      </Container>
    </div>
  );
};
