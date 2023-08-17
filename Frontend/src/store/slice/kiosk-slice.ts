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
  stationLat:number | null;
  stationLon:number | null;
  busStopId: BusStopId;
  busData: BusStoreData[];
  nowCarouselPage: number;
  loading: boolean;
  error: ErrorType;
  masterkey: string;
}

const initialState: KioskState = {
  cityCode: 11,
  busStopId: "SEB114000267",
  stationName: "양천공영차고지",
  stationLat: 35.86897,
  stationLon: 128.5936,
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
    updateLockedBusData(state) {
      const lockbus = state.busData.map((el)=>{
        if(el.isPosted == false && el.remainingStops == 1){
          return{
            ...el,
            isPosted: true
          }}
        }) 
      state.busData = lockbus
    },
    increasePassenger(state, action) {
      const vehicleNo = action.payload.vehicleNo;
      const busNo = action.payload.busNo;
      const plusBus = state.busData.map((el) => {
        if (el.vehicleNo == vehicleNo) {
          console.log(el.busNo)
          return{
            ...el,
            passengerNumber : el.passengerNumber +1,
            isStopHere : true,
          };
         
        }else{
          return el
        }
      });
      state.busData = plusBus
      console.log(vehicleNo)
    },
    syncCarouselPage(state, action) {
      state.nowCarouselPage = action.payload.now;
    },
    SetVulnerable(state, action) {
      const vehicleNo = action.payload.vehicleNo;
      const plusVulnerBus = state.busData.map((el) => {
        if (el.vehicleNo == vehicleNo) {
          console.log(el.busNo)
          return{
            ...el,
            isVulnerable : true,
            passengerNumber: el.passengerNumber+ 1,
            isStopHere : true,
          };
        }
        return el;
      }); 
      state.busData = plusVulnerBus
    },
    checkMaster(state, action) {
      state.busStopId = action.payload.busStopId;
      state.cityCode = action.payload.cityCode;
      state.stationName = action.payload.stationName;
      state.stationLat = action.payload.stationLat;
      state.stationLon = action.payload.stationLon;
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
