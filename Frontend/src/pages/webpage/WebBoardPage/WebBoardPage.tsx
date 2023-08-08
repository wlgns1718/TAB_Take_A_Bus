import { FC } from 'react';
import { WebBoardPageProps } from '.';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { Pagination } from '@mui/material';
import Select from '@mui/joy/Select';
import Option from "@mui/joy/Option";
import './WebBoard.css'

export const WebBoardPage: FC<WebBoardPageProps> = (props) => {

  function createData(
    header: string,
    title: string,
    author: string,
    postDate: string
  ) {
    return { header, title, author, postDate };
  }

  const rows = [
    createData("공지사항", '버스 노선 변경(23년 8월)', '관리자','2023-07-01'),
    createData("공지사항", '버스 노선 변경(23년 8월)', '관리자','2023-07-01'),
    createData("공지사항", '버스 노선 변경(23년 8월)', '관리자','2023-07-01'),
    createData("공지사항", '버스 노선 변경(23년 8월)', '관리자','2023-07-01'),
  ];

  const options : string[] = [
    '공지사항',
    '건의사항',
    '칭찬합니다',
    '고장신고',
  ]

	return (
    <div {...props}>
      <div className="board-title">게시판</div>
      <Select color="primary" placeholder="게시판 구분" size="md">{
        options.map(op=>{
          return <Option value={op}>{op}</Option>
        })
        }
      </Select>
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
              {rows.map((row, index) => (
                <TableRow
                  key={index}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <TableCell component="th" scope="row">
                    {index}
                  </TableCell>
                  <TableCell align="center">{row.header}</TableCell>
                  <TableCell align="left">{row.title}</TableCell>
                  <TableCell align="center">{row.author}</TableCell>
                  <TableCell align="center">{row.postDate}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <div className="pagenation">
          <Pagination
            count={10}
            defaultPage={1}
            variant="outlined"
            color="primary"
            shape="rounded"
          />
        </div>
      </div>
    </div>
  );
};
