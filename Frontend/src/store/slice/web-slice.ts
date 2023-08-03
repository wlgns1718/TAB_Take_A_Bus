import { createSlice } from "@reduxjs/toolkit";

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
