import { createSlice } from "@reduxjs/toolkit";

export type user = {
  email :string;
  id : string;
  name: string;
  password :string;
  role: string;
}


export type loginuser = {
  id : string;
  password:string;
  token : string;
}

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
  isUserIn:boolean;
}

const initialState: WebState = {
  noticeData: [],
  noticeDetailData: null,
  boardData: [],
  boardDetailData: null,
  Token:
    "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJzdHJpbmciLCJpYXQiOjE2OTE2NzU5OTgsImV4cCI6MTY5MTY3OTU5OH0.TYVoT8K-9Fyv6UL0DNGfHdDNT1maZiQnBT9YF6vT2dA",
  selectedNoticeId: null,
  selectedPostId: null,
  isUserIn :false,
}

const webSlice = createSlice({
  name: "web",
  initialState,
  reducers: {
    setIsUserIn(state){
      state.isUserIn = !state.isUserIn
    },
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
  setIsUserIn,
  changeSelectedNoticeId,
  changeSelectedPostId,
  saveBoardData,
  saveNoticeData,
  deleteOneNotice,
  deleteOneBoard,
  setToken,
} = webSlice.actions;

export default webSlice;
