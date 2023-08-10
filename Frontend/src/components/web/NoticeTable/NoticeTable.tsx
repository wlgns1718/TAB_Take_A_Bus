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
import { Link, useNavigate } from "react-router-dom";

export const NoticeTable: FC<NoticeTableProps> = ({ pages, currentPage }) => {
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
                    {index}
                  </TableCell>
                  <TableCell align="center">공지사항</TableCell>
                  <TableCell align="left">
                    <Link to={`/web/board/detail/${row.id}`}>{row.title}</Link>
                  </TableCell>
                  <TableCell align="center">{row.userName}</TableCell>
                  <TableCell align="center">
                    {`${row.createTime[0]}-${row.createTime[1]}-${row.createTime[2]} ${row.createTime[3]}:${row.createTime[4]}`}
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
