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
  busData: [{
    busNo: '1',
    eta: 100 ,
    remainingStops: 100,
    routeId: 'df',
    routeType: 'sdf',
    vehicleNo: 'asdf',
    vehicleType: 'asdf',
    stationId: 'asdf',
    stationName: 'asdf',
    stationOrder:123,
    isStopHere: false,
  passengerNumber: 1,
  isVulnerable: false,
  isPosted: false,}
  ],
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
      state.busData.map((el) => {
        if (el.vehicleNo == vehicleNo){
          el.passengerNumber += 1;
          el.isStopHere = true;}
          return el
      });
      
    },
    syncCarouselPage(state, action) {
      state.nowCarouselPage = action.payload.now;
    },
    SetVulnerable(state, action) {
      const vehicleNo = action.payload.vehicleNo;
      const remainingStops = action.payload.remainingStops;
      state.busData.map((el) => {
        if (el.vehicleNo == vehicleNo) {
          el.isVulnerable = true;
          el.passengerNumber += 1;
          el.isStopHere = true;
          return el;
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
