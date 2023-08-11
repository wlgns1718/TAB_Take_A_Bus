import { BoardData } from "@/store/slice/web-slice";

export interface BoardTableProps {
  pages: BoardData[][];
  currentPage: number;
};
