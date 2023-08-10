import { createSlice } from "@reduxjs/toolkit";

export type user = {
  email :string;
  password :string;
  confirmPwd : string;
  nickname: string;
  master: string | null;
}

const webSlice = createSlice({
  name: "web",
  initialState: { 
    busStop: "우리집 앞", },
  reducers: {
    changeBusStop(state) {
      state.busStop = "우리집 뒤";
    },
  },
});

export const { changeBusStop } = webSlice.actions;

export default webSlice;
