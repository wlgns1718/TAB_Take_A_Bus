import { configureStore,combineReducers} from "@reduxjs/toolkit";
import kioskSlice from "./slice/kiosk-slice";
import webSlice from "./slice/web-slice";
import { KioskState } from "./slice/kiosk-slice";
import { WebState } from "./slice/web-slice";
import { persistReducer, FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER } from 'redux-persist';
import storage from 'redux-persist/lib/storage'



const persistConfig = {
  key: "root", // localStorage key 
  storage, // session
  whitelist: ["kiosk","web"], // target (reducer name)
}

const rootReducer = combineReducers({
  kiosk: kioskSlice.reducer,
  web : webSlice.reducer
});

const persistedReducer = persistReducer(persistConfig, rootReducer);


const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
      },
    }),
});


// const store = configureStore({
//   reducer: {
//     kiosk: kioskSlice.reducer,
//     web: webSlice.reducer,
//   },
// });


export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export default store;