import { FC } from "react";
import { BusInfomationPageProps } from ".";
import { Header } from "../../../components/kiosk/Header";
import { ComingSoonBusList } from "../../../components/kiosk/ComingSoonBusList";
import { ArrivalBusList } from "../../../components/kiosk/ArrivalBusList";
import { LivingInformationBox } from "../../../components/kiosk/LivingInfomationBox";
import { BottomButtonBox } from "../../../components/kiosk/BottomButtonBox";
import { BusData, KioskState, updateBusData } from "../../../store/slice/kiosk-slice";
// import axios from "axios";
import { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { busAPI } from "../../../store/api/api";
import { useQuery } from "react-query";
import { AxiosError } from 'axios'
// import { changeBusStop } from "../../../store/slice/web-slice";



export const BusInfomationPage: FC<BusInfomationPageProps> = (props) => {
  const [busDatas, setBusData] = useState<BusData[]>([]);
  const [comingSoonBusList, setComingSoonBusList] = useState<BusData[]>([]);
  
  const dispatch = useDispatch()

  const data: KioskState = useSelector((state : {
    kiosk : KioskState,
    web : object
  }) => {
    return state.kiosk;
  });


  useEffect(() => {
    // 12분 이내 도착 예정인 버스 리스트
    setComingSoonBusList(
      data.busData.slice(0,5).filter((el: BusData) => {
        // 임시로 120분
        return el.eta <= 900;
      })
    );
  }, [busDatas]);

  const fetchBusData = useQuery(
    "fetchBus",
    () => {
      busAPI
        .get(`/${data.citycode}/${data.busStopId}`, {
          timeout: 10000,
        })
        .then((response) => {
          if (response.data.code == "500") {
            console.log("500 Error: " + response.data.msg);
          } else if (response.data.code == "200") {
            // 도착예정시간 순으로 정렬해서 저장
            dispatch(updateBusData(
              response.data.data.sort((a: BusData, b: BusData) => {
              return a.eta - b.eta;
            })
            ));
          }
        })
        .catch((error) => {
          const err = error as AxiosError;
          console.error("Error fetching buslist data:");
          throw err;
        });
    },
    { staleTime: 100, refetchInterval: 10000 }
  );

  useEffect(()=>{
    console.log(data.busData)
  },[fetchBusData])



  return (
    <div {...props}>
      <Header />
      <ComingSoonBusList data={comingSoonBusList ? comingSoonBusList : []} />
      <ArrivalBusList data={data.busData ? data.busData : []} />
      <LivingInformationBox />
      <BottomButtonBox />
    </div>
  );
};
