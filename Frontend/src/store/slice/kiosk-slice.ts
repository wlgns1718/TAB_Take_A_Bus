import { createSlice } from "@reduxjs/toolkit";
import { busAPI } from "../api/api";

export type BusData = {
  busNo: string;
  eta: number;
  remainingStops: number;
  routeId: string;
  routeType: string;
  vehicleNo: string | null;
  vehicleType: string;
  stationId: string;
  stationName: string;
  stationOrder: number;
  latitude: number;
  longtitude: number;
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
  cityCode: number;
  stationName: string;
  busStopId: BusStopId;
  busData: BusStoreData[];
  nowCarouselPage: number;
  loading: boolean;
  error: ErrorType;
  masterkey: string;
}

const initialState: KioskState = {
  cityCode: 22,
  busStopId: "DGB7001004100",
  stationName: "약령시앞",
  masterkey: "123123123",
  busData: [
    {
      busNo: "급행2",
      eta: 501,
      remainingStops: 2,
      routeId: "DGB1000002000",
      routeType: "급행버스",
      vehicleNo: "대구70자2401",
      vehicleType: "일반차량",
      stationId: "DGB7021015400",
      stationName: "대구삼성창조캠퍼스",
      stationOrder: 123,
      isStopHere: false,
      latitude: 35.88114,
      longtitude: 128.59603,
      passengerNumber: 0,
      isVulnerable: false,
      isPosted: false,
    },
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
        if (el.vehicleNo == vehicleNo) {
          el.passengerNumber += 1;
          el.isStopHere = true;
        }
        return el;
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
      state.busStopId = action.payload.busStopId;
      state.cityCode = action.payload.cityCode;
      state.stationName = action.payload.stationName;
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
