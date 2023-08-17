import { FC } from "react";
import { BusInfomationPageProps } from ".";
import { KioskHeader } from "../../../components/kiosk/KioskHeader";
import { ComingSoonBusList } from "../../../components/kiosk/ComingSoonBusList";
import { ArrivalBusList } from "../../../components/kiosk/ArrivalBusList";
import { LivingInformationBox } from "../../../components/kiosk/LivingInfomationBox";
import { BottomButtonBox } from "../../../components/kiosk/BottomButtonBox";
import {
  BusData,
  BusStoreData,
  KioskState,
  updateBusData,
  updateLockedBusData,
} from "../../../store/slice/kiosk-slice";
import { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { arduinoAPI, busAPI } from "../../../store/api/api";
import { useQuery, QueryClient } from "react-query";
import { AxiosError } from "axios";
import { RootState } from "@/store/store";

export const BusInfomationPage: FC<BusInfomationPageProps> = (props) => {
  const [comingSoonBusList, setComingSoonBusList] = useState<BusStoreData[]>([]);
  const [oldData, setOldData] = useState<BusStoreData[]>([]);

  const dispatch = useDispatch();

  const data: KioskState = useSelector(
    (state: RootState) => {
      return state.kiosk;
    }
  );

  const [pages, setPage] = useState<BusStoreData[][]>([]);

  useEffect(() => {
    // 12분 이내 도착 예정인 버스 리스트
    setComingSoonBusList(
      data.busData.slice(0, 5).filter((el: BusStoreData) => {
        // 임시로 120분
        return el.eta <= 900;
      })
    );
  }, [data.busData]);

  const fetchBusData = useQuery(
    "fetchBus",
    () => {
      busAPI
        .get(`/${data.cityCode}/${data.busStopId}/api`)
        .then((response) => {
          if (response.data.code == "500") {
            console.log("500 Error: " + response.data.msg);
          } else {
 
            // 도착예정시간 순으로 정렬해서 저장.
            const addData: BusStoreData[] = response.data.data.map((el) => {
              el.isStopHere = false;
              el.passengerNumber = 0;
              el.isVulnerable = false;
              el.isPosted = false;
              return el;
            });

            // const filteredData:BusStoreData[] = addData.filter((el)=>{
            //   return el.vehicleNo != null
            // }) 


            const stateBusData: BusStoreData[] = addData.map(
              (newdata: BusStoreData) => {

                const recordedItem = data.busData.find((old: BusStoreData) => {
                  return old.busNo == newdata.busNo && old.vehicleNo == newdata.vehicleNo
                });
                if (recordedItem){
                  if (recordedItem.isStopHere == true) {
                    newdata = {
                      ...newdata,
                      isStopHere: recordedItem.isStopHere,
                    };
                  }
                  if (recordedItem.passengerNumber != newdata.passengerNumber) {
                    newdata = {
                      ...newdata,
                      passengerNumber: recordedItem.passengerNumber,
                    };
                  }
                  if (recordedItem.isVulnerable == true) {
                    newdata = {
                      ...newdata,
                      isVulnerable: recordedItem.isVulnerable,
                    };
                  }
                  if (recordedItem.isPosted == true) {
                    newdata = { ...newdata, isPosted: recordedItem.isPosted };
                  }
                  if (recordedItem.isPosted == false && newdata.remainingStops == 1 ) {
                    newdata = { ...newdata, isPosted: true }
                    }
                return newdata;
                } else {
                  return newdata;
                }
              }
            );

            dispatch(
              updateBusData(
                stateBusData.sort((a: BusStoreData, b: BusStoreData) => {
                  return a.eta - b.eta;
                })
              )
            );
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    { staleTime: 21000, refetchInterval: 21000 }
  );

  useEffect(() => {
    
    const locked = data.busData.filter((el)=>{
      el.isPosted = true
    })

   locked.map((el)=>{
    arduinoAPI.post('regist',{
      count :el.passengerNumber,
      routeNo:el.routeId,
      stationId:el.stationName,
      vehicleNo:el.vehicleNo,
      vulnerable:el.isVulnerable,
    }).then((response)=>{
      console.log(response)
    }).catch((err)=>{
      console.log(err)
    })
   },[fetchBusData]);
  }
  )

  const paginateArray = (arr: BusStoreData[], pageSize: number) => {
    const pageCount = Math.ceil(arr.length / pageSize);
    const pagelist = Array.from({ length: pageCount }, (_, index) => {
      const startIndex = index * pageSize;
      return arr.slice(startIndex, startIndex + pageSize);
    });
    return pagelist;
  };

  useEffect(() => {
    // 페이지 지정
    setPage(paginateArray(data.busData, 4));
  }, [data.busData]);

  return (
    <div style={{ backgroundColor: "#ECF0F3", maxWidth: "2160px" }} {...props}>
      <KioskHeader />
      <ComingSoonBusList data={comingSoonBusList ? comingSoonBusList : []} />
      <ArrivalBusList pages={pages ? pages : []} />
      <LivingInformationBox />
      <BottomButtonBox pages={pages ? pages : []} />
    </div>
  );
};
