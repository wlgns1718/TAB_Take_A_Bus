import { configureStore } from "@reduxjs/toolkit";
import kioskSlice from "./slice/kiosk-slice";
import webSlice from "./slice/web-slice";

const store = configureStore({
  reducer: {
    kiosk: kioskSlice.reducer,
    web: webSlice.reducer,
  },
});

export default store;