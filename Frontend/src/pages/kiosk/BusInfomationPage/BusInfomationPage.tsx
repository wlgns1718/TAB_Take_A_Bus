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

export const BusInfomationPage: FC<BusInfomationPageProps> = (props) => {
  const [comingSoonBusList, setComingSoonBusList] = useState<BusStoreData[]>([]);
  const [oldData, setOldData] = useState<BusStoreData[]>([]);

  const dispatch = useDispatch();

  const data: KioskState = useSelector(
    (state: { kiosk: KioskState; web: object }) => {
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
        .get(`/${data.cityCode}/${data.busStopId}/api`, {
          timeout: 10000,
        })
        .then((response) => {
          if (response.data.code == "500") {
            console.log("500 Error: " + response.data.msg);
          } else if (response.data.code == "200") {
            console.log(response.data)
            // 도착예정시간 순으로 정렬해서 저장.
            const addData: BusStoreData[] = response.data.data.map((el) => {
              el.isStopHere = false;
              el.passengerNumber = 0;
              el.isVulnerable = false;
              el.isPosted = false;
              return el;
            });

            const stateBusData: BusStoreData[] = addData.map(
              (newdata: BusStoreData) => {
                const recordedItem = data.busData.find((old: BusStoreData) => {
                  return old.busNo == newdata.busNo;
                });
                if (recordedItem) {
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
                    return newdata
                  }
                  else{
                    console.log(recordedItem.isPosted);
                    if (newdata.remainingStops == 1) {
                      arduinoAPI
                        .post(
                          `regist`,
                          {
                            busStation: `${data.busStopId}`,
                            count: `${recordedItem.passengerNumber}`,
                            vehicleNo: `${recordedItem.vehicleNo}`,
                            routeNo: `${recordedItem.routeId}`,
                            vulnerable: `${recordedItem.isVulnerable}`,
                          }
                        )
                        .then((response) => {
                          console.log(
                            `${recordedItem.vehicleNo}`,
                            response.data
                          );
                          newdata = { ...newdata, isPosted: true };
                          return newdata;
                        })
                        .catch((error) => {
                          console.log(error);
                          return newdata;
                        });
                    }
                    else{
                      return newdata;
                    }
                  }
                } else {
                  return newdata;
                }
              }
            );
            console.log(stateBusData);
            

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
          const err = error as AxiosError;
          console.error("Error fetching buslist data:");
          throw err;
        });
    },
    { staleTime: 10000, refetchInterval: 10000 }
  );

  useEffect(() => {
  //  console.log(data.busData)
  }, [fetchBusData]);

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
