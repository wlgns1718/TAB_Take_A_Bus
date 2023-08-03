import { createSlice } from "@reduxjs/toolkit";
import axios, {AxiosError} from "axios";

export type BusData = {
  busNo: string;
  eta: number;
  remainingStops: number;
  routeId: string;
  routeType: string;
  vehicleNo: string;
  vehicleType: string;
  stationId: string;
  stationName: string;
  stationOrder: number;
};

type ErrorType = string | null
type BusStopId = string | null

export interface KioskState {
  citycode : number,
  busStopId : BusStopId,
  busData: BusData[],
  nowBusListPage: number,
  loading : boolean,
  error : ErrorType ,
}

const initialState: KioskState = {
  citycode: 37050,
  busStopId: "GMB383",
  busData: [],
  nowBusListPage: 0,
  loading: false,
  error: null,
};

const kioskSlice = createSlice({
  name: "kiosk",
  initialState,
  reducers: {
    updateBusData(state, action) {
      state.busData = action.payload;
    },
    
  },
});

export const { updateBusData } = kioskSlice.actions;

export default kioskSlice;
