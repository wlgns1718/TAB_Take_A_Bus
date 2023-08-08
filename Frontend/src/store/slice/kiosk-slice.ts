import { createSlice } from "@reduxjs/toolkit";
import { busAPI } from "../api/api";

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

export type BusStoreData = BusData & {
  isStopHere: boolean;
  passengerNumber: number;
  isVulnerable: boolean;
  isPosted: boolean;
};

type ErrorType = string | null;
type BusStopId = string | null;

export interface KioskState {
  citycode: number;
  busStopId: BusStopId;
  busData: BusStoreData[];
  nowCarouselPage: number;
  loading: boolean;
  error: ErrorType;
  masterkey: string;
}

const initialState: KioskState = {
  citycode: 22,
  busStopId: "DGB7001004100",
  masterkey: "123123123",
  busData: [],
  nowCarouselPage: 0,
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
    updateLockedBusData(state, action) {
      // state.busData.forEach(bus=>{
      //   if(action.payload.find())
      // })
    },
    increasePassenger(state, action) {
      const vehicleNo = action.payload.vehicleNo;
      const remainingStops = action.payload.remainingStops;
      state.busData.forEach((el) => {
        if (el.vehicleNo == vehicleNo && el.remainingStops == remainingStops) {
          el.passengerNumber += 1;
          el.isStopHere = true;
          return;
        }
      });
      console.log(action.payload.vehicleNo);
    },
    syncCarouselPage(state, action) {
      state.nowCarouselPage = action.payload.now;
    },
    SetVulnerable(state, action) {
      const vehicleNo = action.payload.vehicleNo;
      const remainingStops = action.payload.remainingStops;
      state.busData.forEach((el) => {
        if (el.vehicleNo == vehicleNo && el.remainingStops == remainingStops) {
          el.isVulnerable = true;
          el.passengerNumber += 1;
          el.isStopHere = true;
          return;
        }
      });
    },
    checkMaster(state, action) {
      state.busStopId = action.payload;
      console.log(state.busStopId);
    },
  },
});

export const {
  updateBusData,
  increasePassenger,
  updateLockedBusData,
  SetVulnerable,
  syncCarouselPage,
  checkMaster,
} = kioskSlice.actions;

export default kioskSlice;
