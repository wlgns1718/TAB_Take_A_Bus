import { FC } from "react";
import { NoticeTableProps } from ".";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
} from "@mui/material";
import { useDispatch } from "react-redux";
import { changeSelectedNoticeId } from "@/store/slice/web-slice";
import { POSTPERPAGE, prettyTime } from "@/pages/webpage/WebBoardPage";

export const NoticeTable: FC<NoticeTableProps> = ({ pages, currentPage }) => {
  const dispatch = useDispatch();
  return (
    <div>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead>
            <TableRow>
              <TableCell>No</TableCell>
              <TableCell align="center">구분</TableCell>
              <TableCell align="center">제목</TableCell>
              <TableCell align="center">작성자</TableCell>
              <TableCell align="center">작성시간</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {pages?.length ? (
              pages[currentPage - 1].map((row, index) => (
                <TableRow
                  key={index}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <TableCell component="th" scope="row">
                    {(currentPage - 1) * POSTPERPAGE + index + 1}
                  </TableCell>
                  <TableCell align="center">공지사항</TableCell>
                  <TableCell align="left">
                    {/* <Link to={`/web/board/notice/${row.id}`}>{row.title}</Link> */}
                    <span
                      onClick={(e) => {
                        dispatch(changeSelectedNoticeId(row.id));
                      }}
                    >
                      {row.title}
                    </span>
                  </TableCell>
                  <TableCell align="center">{row.userName}</TableCell>
                  <TableCell align="center">
                    {prettyTime(row.createTime)}
                  </TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow
                sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
              >
                <TableCell component="th" scope="row"></TableCell>
                <TableCell align="center"></TableCell>
                <TableCell align="left">
                  <p>등록된 공지사항이 없습니다..</p>
                </TableCell>
                <TableCell align="center"></TableCell>
                <TableCell align="center"></TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};
