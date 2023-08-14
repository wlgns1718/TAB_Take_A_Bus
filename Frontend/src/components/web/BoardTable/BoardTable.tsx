import { FC } from "react";
import { BoardTableProps } from ".";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
} from "@mui/material";
import { BOARD_KOR, changeSelectedPostId } from "@/store/slice/web-slice";
import { useDispatch } from "react-redux";
import { POSTPERPAGE, prettyTime } from "@/pages/webpage/WebBoardPage";
import { TableStyle } from "../NoticeTable";

export const BoardTable: FC<BoardTableProps> = ({ pages, currentPage }) => {
  const dispatch = useDispatch();
  return (
    <div>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead>
            <TableRow style={{ height: TableStyle.rowHeight }}>
              <TableCell
                style={{ width: TableStyle.colWidth[0] }}
                align="center"
              >
                No
              </TableCell>
              <TableCell
                style={{ width: TableStyle.colWidth[1] }}
                align="center"
              >
                구분
              </TableCell>
              <TableCell
                style={{ width: TableStyle.colWidth[2] }}
                align="center"
              >
                제목
              </TableCell>
              <TableCell
                style={{ width: TableStyle.colWidth[3] }}
                align="center"
              >
                작성자
              </TableCell>
              <TableCell
                style={{ width: TableStyle.colWidth[4] }}
                align="center"
              >
                작성시간
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {pages?.length ? (
              pages[currentPage - 1].map((row, index) => (
                <TableRow
                  style={{ height: TableStyle.rowHeight }}
                  key={index}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                  onClick={(e) => {
                    window.scrollTo(0,0)
                    dispatch(changeSelectedPostId(row.id));
                  }}
                >
                  <TableCell component="th" scope="row" align="center">
                    {(currentPage - 1) * POSTPERPAGE + index + 1}
                  </TableCell>
                  <TableCell align="center">{BOARD_KOR[row.sort]}</TableCell>
                  <TableCell align="left">
                    <span>{row.title}</span>
                  </TableCell>
                  <TableCell align="center">{row.userId}</TableCell>
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
                  <p>등록된 게시물이 없습니다..</p>
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
