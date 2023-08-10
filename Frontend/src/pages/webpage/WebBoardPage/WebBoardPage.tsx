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
import { NoticeTable } from "@/components/web/NoticeTable";
import { boardAPI, noticeAPI } from "@/store/api/api";
import { BoardData, NoticeData } from "@/store/slice/web-slice";

export const WebBoardPage: FC<WebBoardPageProps> = (props) => {
  enum BOARD {
    NOTICE = "공지사항",
    FREE = "게시판",
  }

  const [noticeData, setNoticeData] = useState<NoticeData[]>();
  const [boardData, setBoardData] = useState<BoardData[]>();

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

  const [pages, setPages] = useState<NoticeData[][]>([]);
  const [freePages, setFreePages] = useState<BoardData[][]>([]);

  const handleCurrentBoard = (value) => {
    setCurrentBoard(value);
  };

  const paginateBoard = (arr: BoardData[], pageSize: number) => {
    const pageCount = Math.ceil(arr?.length / pageSize);
    const pagelist = Array.from({ length: pageCount }, (_, index) => {
      const startIndex = index * pageSize;
      return arr.slice(startIndex, startIndex + pageSize);
    });
    return pagelist;
  };
  const paginateNotice = (arr: NoticeData[], pageSize: number) => {
    const pageCount = Math.ceil(arr?.length / pageSize);
    const pagelist = Array.from({ length: pageCount }, (_, index) => {
      const startIndex = index * pageSize;
      return arr.slice(startIndex, startIndex + pageSize);
    });
    return pagelist;
  };

  useEffect(() => {
    noticeAPI.get("list").then((response) => {
      console.log(response.data);
      setNoticeData(response.data.content);
    });
  }, []);

  useEffect(() => {
    boardAPI.get("").then((response) => {
      console.log(response.data.data.content);
      setBoardData(response.data.data.content);
    });
  }, []);

  useEffect(() => {
    setPages(paginateNotice(noticeData, 5));
  }, [noticeData]);

  useEffect(() => {
    setFreePages(paginateBoard(boardData, 5));
  }, [boardData]);

  useEffect(() => {
    setFreePages(paginateBoard(boardData, 5));
  }, []);

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
        {currentBoard == "공지사항" ? (
          <div>
            <NoticeTable pages={pages} currentPage={currentPage}></NoticeTable>
            <div className="pagenation">
              <Pagination
                count={pages?.length}
                page={currentPage}
                variant="outlined"
                color="primary"
                shape="rounded"
                onChange={(e, page) => {
                  setCurrentPage(page);
                  console.log(page);
                }}
              />
            </div>
          </div>
        ) : (
          <div>
            <BoardTable
              pages={freePages}
              currentPage={currentFreePage}
            ></BoardTable>
            <div className="pagenation">
              <Pagination
                count={freePages?.length}
                page={currentFreePage}
                variant="outlined"
                color="primary"
                shape="rounded"
                onChange={(e, page) => {
                  setFreeCurrentPage(page);
                  console.log(page);
                }}
              />
            </div>
          </div>
        )}
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
