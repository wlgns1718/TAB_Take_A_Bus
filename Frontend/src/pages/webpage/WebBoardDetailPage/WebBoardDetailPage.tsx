import { FC, useEffect, useState } from "react";
import { WebBoardDetailPageProps } from ".";
import { BoardData } from "@/store/slice/web-slice";
import { boardAPI } from "@/store/api/api";
import { useParams } from "react-router-dom";

export const WebBoardDetailPage: FC<WebBoardDetailPageProps> = (props) => {
  const [boardDetailData, setBoardDetailData] = useState<BoardData>({
    id: 3,
    userId: "string",
    title: "string",
    content: "string",
    createTime: [2, 2, 2, 2, 2, 2, 2],
    sort: "string",
    commentResponseDtoList: [],
  });
  const params = useParams();
  useEffect(() => {
    const postId = params.postId;
    console.log(postId);
    boardAPI.get(`${postId}`).then((response) => {
      console.log(response.data);
      if (response.data.data.length) {
        setBoardDetailData(response.data.data);
      }
    }).catch(error=>{
      console.log(error)
    })
  }, []);

  return (
    <div {...props}>
      {boardDetailData?.title ? (
        <div>
          <div>{boardDetailData.title}</div>
          <div>{boardDetailData.content}</div>
          <div>{boardDetailData.sort}</div>
          <div>{boardDetailData.userId}</div>
          <div>{`${boardDetailData.sort[0]}-${boardDetailData.createTime[1]}-${boardDetailData.createTime[2]} ${boardDetailData.createTime[3]}:${boardDetailData.createTime[4]}`}</div>
        </div>
      ) : (
        <div></div>
      )}
      <div></div>
    </div>
  );
};
