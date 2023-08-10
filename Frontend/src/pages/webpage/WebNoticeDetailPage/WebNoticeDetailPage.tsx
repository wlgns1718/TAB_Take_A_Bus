import { FC, useState, useEffect } from "react";
import { WebNoticeDetailPageProps } from ".";
import { noticeAPI } from "@/store/api/api";
import { useNavigate, useParams } from "react-router-dom";
import { NoticeDetailData } from "@/store/slice/web-slice";
import { Container, IconButton, Paper, Typography } from "@mui/material";
import ArrowBackOutlinedIcon from "@mui/icons-material/ArrowBackOutlined";
import { Button } from "@mui/joy";

export const WebNoticeDetailPage: FC<WebNoticeDetailPageProps> = (props) => {
  const [noticeDetailData, setNoticeDetailData] = useState<NoticeDetailData>({
    id: 1,
    userName: "string",
    title: "string",
    createTime: [2, 2, 2, 2, 2, 2, 2],
    content: "content",
  });
  const params = useParams();

  useEffect(() => {
    const noticeId = params.noticeId;
    noticeAPI
      .get(`detail/${noticeId}`)
      .then((response) => {
        console.log(response.data);
        if (response.data.data.length) {
          setNoticeDetailData(response.data.data);
        }
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  const navigate = useNavigate();

  return (
    <div {...props}>
      <Container maxWidth="xl" sx={{ paddingTop: 10 }}>
        <IconButton
          onClick={() => {
            navigate(-1);
          }}
        >
          <ArrowBackOutlinedIcon />
        </IconButton>
        <Typography variant="h4" sx={{ marginBottom: 10 }}>
          {noticeDetailData?.title}
        </Typography>
        <div>{`작성자 : ${noticeDetailData?.userName}`}</div>
        <div>
          {`작성시간 : ${noticeDetailData?.createTime[0]}-${noticeDetailData?.createTime[1]}-${noticeDetailData?.createTime[2]} ${noticeDetailData?.createTime[3]}:${noticeDetailData?.createTime[4]}`}
        </div>
        <div></div>
        <Typography>{noticeDetailData?.content}</Typography>
      </Container>
      <Container maxWidth="xl">
        <Button color="neutral" onClick={function () {}} variant="soft">
          수정
        </Button>
        <Button color="neutral" onClick={function () {}} variant="soft">
          삭제
        </Button>
      </Container>
    </div>
  );
};
