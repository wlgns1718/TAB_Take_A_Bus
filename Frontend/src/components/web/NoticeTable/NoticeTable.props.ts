import { NoticeData } from "@/store/slice/web-slice";

export interface NoticeTableProps {
  pages: NoticeData[][];
  currentPage: number;
};
