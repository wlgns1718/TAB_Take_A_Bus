import { FC, useState, useEffect } from "react";
import { WebNoticeDetailPageProps } from '.';
import { noticeAPI } from '@/store/api/api';
import { useParams } from 'react-router-dom';
import { NoticeDetailData } from "@/store/slice/web-slice";
import { Container, IconButton, Paper, Typography } from "@mui/material";
import ArrowBackOutlinedIcon from "@mui/icons-material/ArrowBackOutlined";

export const WebNoticeDetailPage: FC<WebNoticeDetailPageProps> = (props) => {
	const [noticeDetailData, setNoticeDetailData] = useState<NoticeDetailData>({
  id: 1,
  userName: "string",
  title: 'string',
  createTime: [2,2,2,2,2,2,2],
	content : "content"
});
  const params = useParams();
  useEffect(() => {
    const postId = params.postId;
    noticeAPI.get(`${postId}`).then((response) => {
      console.log(response.data);
      setNoticeDetailData(response.data.data);
    });
  }, []);

  return (
    <div {...props}>
      <div>{noticeDetailData.title}</div>
      <div>{noticeDetailData.content}</div>
      <div>{noticeDetailData.userName}</div>
      <div>{`${noticeDetailData.createTime[0]}-${noticeDetailData.createTime[1]}-${noticeDetailData.createTime[2]} ${noticeDetailData.createTime[3]}:${noticeDetailData.createTime[4]}`}</div>
      <div></div>
      <Container maxWidth="md" sx={{ paddingTop: 32 }}>
        <Paper elevation={3} sx={{ padding: 16 }}>
          <IconButton
            onClick={() => {
              // 뒤로 가기 버튼 클릭 시 이전 페이지로 이동
              // (이 예시에서는 게시판 목록 페이지로 간주)
            }}
          >
            <ArrowBackOutlinedIcon />
          </IconButton>
          <Typography variant="h4" sx={{ marginBottom: 16 }}>
            {noticeDetailData.title}
          </Typography>
          <Typography>{noticeDetailData.content}</Typography>
        </Paper>
      </Container>
    </div>
  );
};
