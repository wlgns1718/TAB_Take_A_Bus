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

export const TableStyle = {
  rowHeight: 80,
  colWidth: ["10%", "15%", "25%", "15%", "20%"],
};
export const NoticeTable: FC<NoticeTableProps> = ({ pages, currentPage }) => {
  const dispatch = useDispatch();

  return (
    <div>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead>
            <TableRow style={{ height: TableStyle.rowHeight }}>
              <TableCell
                align="center"
                style={{ width: TableStyle.colWidth[0] }}
              >
                No
              </TableCell>
              <TableCell
                align="center"
                style={{ width: TableStyle.colWidth[1] }}
              >
                구분
              </TableCell>
              <TableCell
                align="center"
                style={{ width: TableStyle.colWidth[2] }}
              >
                제목
              </TableCell>
              <TableCell
                align="center"
                style={{ width: TableStyle.colWidth[3] }}
              >
                작성자
              </TableCell>
              <TableCell
                align="center"
                style={{ width: TableStyle.colWidth[4] }}
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
                    dispatch(changeSelectedNoticeId(row.id));
                  }}
                >
                  <TableCell align="center" component="th" scope="row">
                    {(currentPage - 1) * POSTPERPAGE + index + 1}
                  </TableCell>
                  <TableCell align="center">공지사항</TableCell>
                  <TableCell align="left">
                    <span>{row.title}</span>
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
