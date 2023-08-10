import { createSlice } from "@reduxjs/toolkit";

export type BoardData = {
  id: number;
  userId: string;
  title: string;
  content: string;
  createTime: number[];
  sort: string;
  commentResponseDtoList: [] | null;
};

export type NoticeData = {
  id: number;
  userName: string;
  title: string;
  createTime: number[];
};

export type NoticeDetailData = NoticeData & {
  content : string;
};

	export enum BOARD_KOR {
    REPORT = "신고",
    COMPLAIN = "불만사항",
    COMPLIMENT = "칭찬합니다",
    SUGGESTION = "건의사항",
  }
  export enum BOARD_ENG {
    "신고" = "REPORT",
    "불만사항" = "COMPLAIN",
    "칭찬합니다" = "COMPLIMENT",
    "건의사항" = "SUGGESTION",
  }

export interface WebState {
  noticeData: NoticeData[];
  noticeDetailData: NoticeDetailData | null;
  boardData: BoardData[];
  boardDetailData: BoardData;
  Token: string | null;
}

const initialState: WebState = {
  noticeData: [],
  noticeDetailData: null,
  boardData: [],
  boardDetailData: null,
  Token:
    "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJzdHJpbmciLCJpYXQiOjE2OTE2NDA2MzEsImV4cCI6MTY5MTY0NDIzMX0.QWLhzSqPSrSbVZPKfXjkLuKAnCpfhRZmqUR2nEyIlxE",
};

const webSlice = createSlice({
  name: "web",
  initialState,
  reducers: {
    setToken(state, action) {
      state.Token = action.payload
    }
  },
});

export const { } = webSlice.actions;

export default webSlice;
