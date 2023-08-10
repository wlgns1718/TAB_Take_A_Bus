import { FC, useEffect, useState } from "react";
import { WebBoardDetailPageProps } from ".";
import { BOARD_KOR, BoardData, changeSelectedPostId } from "@/store/slice/web-slice";
import { boardAPI } from "@/store/api/api";
import { useNavigate, useParams } from "react-router-dom";
import { Container, IconButton, Paper, Typography } from "@mui/material";
import { Button } from "@mui/joy";
import ArrowBackOutlinedIcon from "@mui/icons-material/ArrowBackOutlined";
import { useDispatch } from "react-redux";

export const WebBoardDetailPage: FC<WebBoardDetailPageProps> = ({ postId }) => {
  const [boardDetailData, setBoardDetailData] = useState<BoardData | null>();

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
  }, []);

  const prettyTime = (createTime) => {
    if (createTime) {
      return `${createTime[0]}-${createTime[1]}-${createTime[2]} ${createTime[3]}:${createTime[4]}`;
    } else {
      return " ";
    }
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
          <Button color="neutral" onClick={function () {}} variant="soft">
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
