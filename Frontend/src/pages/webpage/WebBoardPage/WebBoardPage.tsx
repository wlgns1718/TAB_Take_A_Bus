import { FC, useEffect, useState } from "react";
import { WebBoardPageProps } from ".";
import Paper from "@mui/material/Paper";
import { Pagination } from "@mui/material";
import Select from "@mui/joy/Select";
import Option from "@mui/joy/Option";
import { Button, dividerClasses } from "@mui/joy";
import "./WebBoard.css";
import { Link, useNavigate } from "react-router-dom";
import { BoardTable } from "@/components/web/BoardTable";

import { noticeAPI } from "@/store/api/api";

export type BoardData = {
  header: string;
  title: string;
  author: string;
  postDate: string;
};

export type NoticeData = {
  id: number;
  username: string;
  title: string;
  content: string;
  createTime: string;
};

export const WebBoardPage: FC<WebBoardPageProps> = (props) => {
  enum BOARD {
    NOTICE = "공지사항",
    FREE = "게시판",
  }

  function createData(
    header: string,
    title: string,
    author: string,
    postDate: string
  ) {
    return { header, title, author, postDate };
  }

  const rows: BoardData[] = [
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
    createData("공지사항", "버스 노선 변경(23년 8월)", "관리자", "2023-07-01"),
  ];
  const freerows: BoardData[] = [
    createData(
      "자유게시판",
      "버스 노선 변경(23년 8월)",
      "관리자",
      "2023-07-01"
    ),
    createData(
      "자유게시판",
      "버스 노선 변경(23년 8월)",
      "관리자",
      "2023-07-01"
    ),
    createData(
      "자유게시판",
      "버스 노선 변경(23년 8월)",
      "관리자",
      "2023-07-01"
    ),
    createData(
      "칭찬합니다",
      "버스 노선 변경(23년 8월)",
      "관리자",
      "2023-07-01"
    ),
    createData(
      "자유게시판",
      "버스 노선 변경(23년 8월)",
      "관리자",
      "2023-07-01"
    ),
    createData(
      "자유게시판",
      "버스 노선 변경(23년 8월)",
      "관리자",
      "2023-07-01"
    ),
  ];

  const options: string[] = [
    "전체게시판",
    "건의사항",
    "칭찬합니다",
    "불만사항",
  ];

  const boards: string[] = ["공지사항", "게시판"];

  const navigate = useNavigate();

  const [currentBoard, setCurrentBoard] = useState(BOARD.NOTICE);

  const [currentPage, setCurrentPage] = useState(1);
  const [currentFreePage, setFreeCurrentPage] = useState(1);
  const [renderPageNo, setRenderPageNo] = useState(1);

  const [pages, setPages] = useState<BoardData[][]>([]);
  const [freePages, setFreePages] = useState<BoardData[][]>([]);
  const [renderPage, setRenderPage] = useState<BoardData[][]>([]);

  const handleCurrentBoard = (value) => {
    setCurrentBoard(value);
  };

  const paginateArray = (arr: BoardData[], pageSize: number) => {
    const pageCount = Math.ceil(arr.length / pageSize);
    const pagelist = Array.from({ length: pageCount }, (_, index) => {
      const startIndex = index * pageSize;
      return arr.slice(startIndex, startIndex + pageSize);
    });
    return pagelist;
  };

  useEffect(() => {
    setPages(paginateArray(rows, 5));
  }, []);

  useEffect(() => {
    setFreePages(paginateArray(freerows, 5));
  }, []);

  useEffect(() => {
    if (currentBoard == "공지사항") {
      setRenderPage(pages);
      setRenderPageNo(currentPage);
    } else {
      setRenderPage(freePages);
      setRenderPageNo(currentFreePage);
    }
  }, [currentBoard, currentPage, currentFreePage]);

  return (
    <div {...props}>
      <div className="board-header">
        <div className="board-header-space"></div>
        {boards.map((name, index) => {
          return (
            <div
              className={`board-header-item ${
                currentBoard == name ? "board-selected" : null
              }`}
              key={index}
              onClick={() => handleCurrentBoard(name)}
            >
              {name}
            </div>
          );
        })}
        <div className="board-header-space"> </div>
      </div>

      {currentBoard == BOARD.FREE ? (
        <Select
          className="board-select board-select-space"
          color="primary"
          placeholder="전체게시판"
          size="md"
        >
          {options.map((op, index) => {
            return (
              <Option value={op} key={index}>
                {op}
              </Option>
            );
          })}
        </Select>
      ) : (
        <div className="board-select-space"></div>
      )}
      <div>
        <BoardTable
          pages={renderPage ? renderPage : []}
          currentPage={renderPageNo}
        ></BoardTable>
        <div className="pagenation">
          <Pagination
            count={renderPage?.length}
            page={renderPageNo}
            variant="outlined"
            color="primary"
            shape="rounded"
            onChange={(e, page) => {
              if (currentBoard == "공지사항") {
                setCurrentPage(page);
                console.log(page);
              } else {
                setFreeCurrentPage(page);
                console.log(page);
              }
            }}
          />
        </div>
        <Button
          onClick={() => {
            navigate("post");
          }}
        >
          글 작성
        </Button>
      </div>
    </div>
  );
};
