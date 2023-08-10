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
  content: string;
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
  selectedNoticeId: number | null;
  selectedPostId: number | null;
}

const initialState: WebState = {
  noticeData: [],
  noticeDetailData: null,
  boardData: [],
  boardDetailData: null,
  Token:
    "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJzdHJpbmciLCJpYXQiOjE2OTE2NTIzNzMsImV4cCI6MTY5MTY1NTk3M30.T9QkI25cz0hklLfpgwyeLoVNLIRKvRj_Kn5b_AdKZj4",
  selectedNoticeId: null,
  selectedPostId: null,
};

const webSlice = createSlice({
  name: "web",
  initialState,
  reducers: {
    setToken(state, action) {
      state.Token = action.payload;
    },
    changeSelectedNoticeId(state, action) {
      state.selectedNoticeId = action.payload;
    },
    changeSelectedPostId(state, action) {
      state.selectedPostId = action.payload;
    },
  },
});

export const { changeSelectedNoticeId, changeSelectedPostId } =
  webSlice.actions;

export default webSlice;
