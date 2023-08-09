import { BoardData, NoticeData } from "@/pages/webpage/WebBoardPage"
export interface BoardTableProps {
  pages: BoardData[][] | NoticeData[][];
  currentPage: number;
};
