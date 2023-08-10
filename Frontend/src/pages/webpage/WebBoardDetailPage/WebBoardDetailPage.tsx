import { FC, useEffect, useState } from "react";
import { WebBoardDetailPageProps } from '.';
import { BoardData } from '@/store/slice/web-slice';
import { boardAPI } from "@/store/api/api";
import { useParams } from "react-router-dom";

export const WebBoardDetailPage: FC<WebBoardDetailPageProps> = (props) => {
	const [boardDetailData, setBoardDetailData] = useState<BoardData>();
	const params = useParams();
	useEffect(() => {
		const postId = params.postId;
		boardAPI.get(`${postId}`).then(response=>{
			console.log(response.data)
			setBoardDetailData(response.data.data);
		});
	}, [])

	return (
    <div {...props}>
      <div>{boardDetailData.title}</div>
      <div>{boardDetailData.content}</div>
      <div>{boardDetailData.sort}</div>
      <div>{boardDetailData.userId}</div>
      <div>{`${boardDetailData.createTime[0]}-${boardDetailData.createTime[1]}-${boardDetailData.createTime[2]} ${boardDetailData.createTime[3]}:${boardDetailData.createTime[4]}`}</div>
      <div></div>
    </div>
  );
};
