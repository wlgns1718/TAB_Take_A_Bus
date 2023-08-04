import { createSlice } from "@reduxjs/toolkit";

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

type BusStoreData = BusData & {
  isStopHere: boolean;
  passengerNumber: number;
  isVulnerable:boolean;
};

type ErrorType = string | null
type BusStopId = string | null

export interface KioskState {
  citycode: number;
  busStopId: BusStopId;
  busData: BusStoreData[];
  nowBusListPage: number;
  loading: boolean;
  error: ErrorType;
}

const initialState: KioskState = {
  citycode: 22,
  busStopId: "DGB7001004100",
  busData: [
    {
      busNo: "105",
      eta: 200,
      remainingStops: 1,
      routeId: "temp1",
      routeType: "간선버스",
      vehicleNo: "temp1",
      vehicleType: "temp1",
      stationId: "temp1",
      stationName: "temp1",
      stationOrder: 10,
      isStopHere: true,
      passengerNumber: 1,
      isVulnerable: false,
    },
    {
      busNo: "115",
      eta: 200,
      remainingStops: 2,
      routeId: "temp1",
      routeType: "급행버스",
      vehicleNo: "temp1",
      vehicleType: "temp1",
      stationId: "temp1",
      stationName: "temp2",
      stationOrder: 10,
      isStopHere: true,
      passengerNumber: 1,
      isVulnerable: true,
    },
    {
      busNo: "200",
      eta: 300,
      remainingStops: 4,
      routeId: "temp1",
      routeType: "간선버스",
      vehicleNo: "temp1",
      vehicleType: "temp1",
      stationId: "temp1",
      stationName: "temp2",
      stationOrder: 10,
      isStopHere: false,
      passengerNumber: 0,
      isVulnerable: false,
    },
    {
      busNo: "500",
      eta: 500,
      remainingStops: 6,
      routeId: "temp1",
      routeType: "일반버스",
      vehicleNo: "temp1",
      vehicleType: "temp1",
      stationId: "temp1",
      stationName: "temp2",
      stationOrder: 10,
      isStopHere: false,
      passengerNumber: 0,
      isVulnerable: false,
    },
  ],
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
