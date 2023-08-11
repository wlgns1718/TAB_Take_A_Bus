import { createSlice } from "@reduxjs/toolkit";

export type user = {
  email :string;
  password :string;
  confirmPwd : string;
  nickname: string;
  master: string | null;
}

export type CommentData = {
  id : number,
  boardId : number,
  userId : string,
  content : string,
  createTime: number[],
}
export type BoardData = {
  id: number;
  userId: string;
  title: string;
  content: string;
  createTime: number[];
  sort: string;
  commentResponseDtoList: CommentData[] | null;
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
  User:user | null;
}

const initialState: WebState = {
  noticeData: [],
  noticeDetailData: null,
  boardData: [],
  boardDetailData: null,
  Token:
    "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJzdHJpbmciLCJpYXQiOjE2OTE3MzIwNTcsImV4cCI6MTY5MTczNTY1N30.1tAqLkg3IDExjUP6fecr5Gm-DD-ukwlFJWtYSCbbidc",
  selectedNoticeId: null,
  selectedPostId: null,
  User: null,
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
    saveBoardData(state, action) {
      state.boardData = action.payload;
    },
    saveNoticeData(state, action) {
      state.noticeData = action.payload;
    },
    deleteOneNotice(state, action) {
      const indexToDelete = state.noticeData.findIndex(
        (post) => post.id === action.payload
      );
      if (indexToDelete !== -1) {
        state.noticeData.splice(indexToDelete, 1);
        console.log("delete one");
      }
    },
    deleteOneBoard(state, action) {
      const indexToDelete = state.boardData.findIndex(
        (post) => post.id === action.payload
      );
      if (indexToDelete !== -1) {
        state.boardData.splice(indexToDelete, 1);
        console.log("delete one");
      }
    },
  },
});

export const {
  changeSelectedNoticeId,
  changeSelectedPostId,
  saveBoardData,
  saveNoticeData,
  deleteOneNotice,
  deleteOneBoard,
} = webSlice.actions;

export default webSlice;
