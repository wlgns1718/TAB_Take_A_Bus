import { FC, useEffect, useState } from "react";
import { WebBoardPageProps } from ".";
import Paper from "@mui/material/Paper";
import { Pagination } from "@mui/material";
import Select from "@mui/joy/Select";
import Option from "@mui/joy/Option";
import {
  Button,
  IconButton,
  Input,
  Stack,
} from "@mui/joy";
import SearchIcon from "@mui/icons-material/Search";
import "./WebBoard.css";
import { useNavigate } from "react-router-dom";
import { BoardTable } from "@/components/web/BoardTable";
import { NoticeTable } from "@/components/web/NoticeTable";
import { boardAPI, noticeAPI } from "@/store/api/api";
import {
  BOARD_ENG,
  BoardData,
  NoticeData,
  WebState,
  changeSelectedBoard,
  saveBoardData,
  saveNoticeData,
} from "@/store/slice/web-slice";
import { WebBoardDetailPage } from "../WebBoardDetailPage";
import { useDispatch, useSelector } from "react-redux";
import { WebNoticeDetailPage } from "../WebNoticeDetailPage";

export const POSTPERPAGE: number = 10;

export const prettyTime = (createTime, sec = false) => {
  if (createTime) {
    const dateTime = new Date(createTime);

    const options: {
      year: string;
      month: string;
      day: string;
      hour: string;
      minute: string;
      second?: string;
    } = {
      year: "numeric",
      month: "long",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    };
    if (sec) {
      options["second"] = "2-digit";
    }
    // @ts-ignore
    const formattedDateTime = dateTime.toLocaleString("ko-KR", options);
    return formattedDateTime;
  } else {
    return " ";
  }
};

export const WebBoardPage: FC<WebBoardPageProps> = (props) => {
  enum BOARD {
    NOTICE = "공지사항",
    FREE = "게시판",
  }

  const data: WebState = useSelector((state: { web: WebState }) => {
    return state.web;
  });
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [noticeData, setNoticeData] = useState<NoticeData[]>([]);
  const [boardData, setBoardData] = useState<BoardData[]>([]);

  const categorys: string[] = [
    "전체게시판",
    "건의사항",
    "칭찬합니다",
    "불만사항",
  ];

  const searchOptions: string[] = ["제목", "내용", "작성자"];
  enum CATEGORY {
    "제목" = "title",
    "내용" = "content",
    "작성자" = "user",
  }

  const boards: string[] = ["공지사항", "게시판"];

  const [currentPage, setCurrentPage] = useState(1);
  const [currentFreePage, setFreeCurrentPage] = useState(1);

  const [pages, setPages] = useState<NoticeData[][]>([]);
  const [freePages, setFreePages] = useState<BoardData[][]>([]);

  const [selectedNoticeId, setSelectedNoticeId] = useState(
    data.selectedNoticeId
  );
  const [selectedPostId, setSelectedPostId] = useState(data.selectedPostId);

  const handleCurrentBoard = (value) => {
    dispatch(changeSelectedBoard(value));
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

  // 검색 기능
  const [searchKeyword, setSearchKeyword] = useState<string>();
  const [searchCategory, setSearchCategory] = useState<string>("제목");
  const handleCategoryChange = (value) => {
    setSearchCategory(value);
  };
  const handleTitleChange = (event) => {
    setSearchKeyword(event.target.value);
  };

  const searchBoard = (e) => {
    e.preventDefault();
    if (!searchCategory) {
      alert("검색옵션을 선택해주세요");
      return;
    }
    if (!searchKeyword.trim().length) {
      alert("검색어를 입력해주세요");
      return;
    }
    if (searchKeyword.trim().length < 2) {
      alert("검색어를 두글자 이상 입력해주세요");
      return;
    }
    console.log("search", searchKeyword);
    boardAPI
      .get(`${CATEGORY[searchCategory]}/${searchKeyword}`)
      .then((response) => {
        console.log(response.data);
        setBoardData(response.data.data.content.reverse());
      });
  };

  const filterSort = (value) => {
    let url = "";
    if (value != "전체게시판") {
      url = `sort/${BOARD_ENG[value]}`;
    }
    boardAPI.get(url).then((response) => {
      console.log(response.data);
      setBoardData(response.data.data.content.reverse());
    });
  };

  useEffect(() => {
    if (!noticeData?.length) {
      noticeAPI.get("list", {params:{
        page:0, size:1000
      }}).then((response) => {
        console.log(response.data);
        // setNoticeData(response.data.content);
        dispatch(saveNoticeData(response.data.content.reverse()));
      });
    }
  }, []);

  useEffect(() => {
    if (!boardData?.length) {
      boardAPI
        .get("", {
          params: {
            page: 0,
            size: 1000,
          },
        })
        .then((response) => {
          console.log(response.data.data.content);
          // setBoardData(response.data.data.content);
          dispatch(saveBoardData(response.data.data.content.reverse()));
        });
    }
  }, []);

  useEffect(() => {
    setNoticeData(data.noticeData);
  }, [data.noticeData]);

  useEffect(() => {
    setBoardData(data.boardData);
  }, [data.boardData]);

  useEffect(() => {
    setPages(paginateNotice(noticeData, POSTPERPAGE));
  }, [noticeData]);

  useEffect(() => {
    setFreePages(paginateBoard(boardData, POSTPERPAGE));
  }, [boardData]);

  useEffect(() => {
    setSelectedNoticeId(data.selectedNoticeId);
  }, [data.selectedNoticeId]);

  useEffect(() => {
    setSelectedPostId(data.selectedPostId);
  }, [data.selectedPostId]);

  return (
    <div {...props}>
      <div className="board-header">
        <div className="board-header-space"></div>
        {boards.map((name, index) => {
          return (
            <div
            style={{ cursor:"pointer"}}
              className={`board-header-item ${
                data.selectedBoard == name ? "board-selected" : null
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
      {data.selectedBoard == "공지사항" ? (
        selectedNoticeId ? (
          <WebNoticeDetailPage postId={selectedNoticeId}></WebNoticeDetailPage>
        ) : (
          <div></div>
        )
      ) : selectedPostId ? (
        <WebBoardDetailPage postId={selectedPostId}></WebBoardDetailPage>
      ) : (
        <div></div>
      )}
      {/* 공지사항 */}
      <div>
        {data.selectedBoard == "공지사항" ? (
          <div>
            <div className="board-select-space"></div>
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
          // 게시판
          <div>
            <Select
              className="board-select board-select-space"
              color="primary"
              placeholder="전체게시판"
              size="md"
            >
              {categorys.map((op, index) => {
                return (
                  <Option
                    value={op}
                    key={index}
                    onClick={() => {
                      filterSort(op);
                    }}
                  >
                    {op}
                  </Option>
                );
              })}
            </Select>
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
            <form onSubmit={searchBoard}>
              <Stack direction={"row"} spacing={2}>
                <Select defaultValue="제목" variant="soft" sx={{ width: 100 }}>
                  {searchOptions.map((op, index) => {
                    return (
                      <Option
                        value={op}
                        key={index}
                        onClick={() => handleCategoryChange(op)}
                      >
                        {op}
                      </Option>
                    );
                  })}
                </Select>
                <Input
                  placeholder="검색어를 입력하세요.."
                  variant="outlined"
                  color="neutral"
                  onChange={handleTitleChange}
                />
                <IconButton onClick={searchBoard}>
                  <SearchIcon fontSize="large"></SearchIcon>
                </IconButton>
              </Stack>
            </form>
          </div>
        )}
        {data.isUserIn ? (
          <div className="board-notice-button">
            <Button
              onClick={() => {
                navigate("post");
              }}
            >
              글 작성
            </Button>
          </div>
        ) : (
          " "
        )}
      </div>
    </div>
  );
};
