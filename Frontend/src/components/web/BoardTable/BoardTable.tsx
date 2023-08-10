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



export const BoardTable: FC<BoardTableProps> = ({ pages, currentPage }) => {
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
                    {index}
                  </TableCell>
                  <TableCell align="center">{BOARD_KOR[row.sort]}</TableCell>
                  <TableCell align="left">
                    {/* <Link to={`/web/board/detail/${row.id}`}>{row.title}</Link> */}
                    <span
                      onClick={(e) => {
                        dispatch(changeSelectedPostId(row.id));
                      }}
                    >
                      {row.title}
                    </span>
                  </TableCell>
                  <TableCell align="center">{row.userId}</TableCell>
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
