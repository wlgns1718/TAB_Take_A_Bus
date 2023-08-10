import { FC, useState, useEffect } from "react";
import { WebNoticeDetailPageProps } from ".";
import { noticeAPI } from "@/store/api/api";
import { NoticeDetailData, changeSelectedNoticeId } from "@/store/slice/web-slice";
import { Container, IconButton, Paper, Typography } from "@mui/material";
import { Button } from "@mui/joy";
import ArrowBackOutlinedIcon from "@mui/icons-material/ArrowBackOutlined";
import "./WebNoticeDetailPage.css";
import { useDispatch } from "react-redux";

export const WebNoticeDetailPage: FC<WebNoticeDetailPageProps> = ({ postId }) => {
  const [noticeDetailData, setNoticeDetailData] = useState<NoticeDetailData>();

  const dispatch = useDispatch();

  useEffect(() => {
    noticeAPI
      .get(`detail/${postId}`)
      .then((response) => {
        console.log(response.data);
        if (response.data.code == "200") {
          setNoticeDetailData(response.data.data);
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


  if (!noticeDetailData) {
    return <div></div>;
  }

  return (
    <div >
      <Container maxWidth="xl" sx={{ paddingTop: 10 }}>
        <div className="detail-header">
          <IconButton
            onClick={() => {
              dispatch(changeSelectedNoticeId(null));
            }}
          >
            <ArrowBackOutlinedIcon fontSize="large" />
          </IconButton>
          <div style={{ fontSize: "30px", fontWeight: "bold" }}>공지사항</div>
        </div>
        <div className="detail">
          <Typography variant="h4" sx={{ margin: 5 }}>
            {noticeDetailData?.title}
          </Typography>

          <div>{`작성자 : ${noticeDetailData?.userName}`}</div>
          <div>{`작성시간 : ${prettyTime(noticeDetailData?.createTime)}`}</div>
          {/* html 코드 출력 */}
          <div
            dangerouslySetInnerHTML={{ __html: noticeDetailData?.content }}
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
    </div>
  );
};
