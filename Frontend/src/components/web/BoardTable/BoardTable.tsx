import { FC } from 'react';
import { BoardTableProps } from '.';
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { Link, useNavigate } from "react-router-dom";


export const BoardTable: FC<BoardTableProps> = ({ pages, currentPage }) => {
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
                  <TableCell align="center">{row.header}</TableCell>
                  <TableCell align="left">
                    <Link to={`/web/board/detail/${"123"}`}>{row.title}</Link>
                  </TableCell>
                  <TableCell align="center">{row.author}</TableCell>
                  <TableCell align="center">{row.postDate}</TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow
                sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
              >
                <TableCell component="th" scope="row">
                  0
                </TableCell>
                <TableCell align="center">.</TableCell>
                <TableCell align="left">
                  <Link to={`/web/board/detail/${"123"}`}>.</Link>
                </TableCell>
                <TableCell align="center">.</TableCell>
                <TableCell align="center">.</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};
