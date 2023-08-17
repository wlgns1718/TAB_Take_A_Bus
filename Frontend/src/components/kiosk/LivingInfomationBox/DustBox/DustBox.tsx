import { FC } from "react";
import { useEffect, useState, useRef } from "react";
import axios from "axios";
import { DustBoxProps } from ".";
import "./DustBox.css";
import proj4 from "proj4";
import { useSelector } from "react-redux";
import { KioskState } from "@/store/slice/kiosk-slice";

// 미세먼지 정보를 담을 인터페이스 (API 응답 데이터 구조에 맞게 정의)
interface DustData {
  dataTime: string;
  pm10Value: number;
  pm25Value: number;
  // 다른 필요한 미세먼지 정보도 추가할 수 있습니다.
}

export const DustBox: FC<DustBoxProps> = (props) => {
  const [dustInfo, setDustInfo] = useState<DustData | null>(null);
  const [measuringStation, setMeasuringStation] = useState("종로구");

  const [state10, setState10] = useState(1);
  const [state25, setState25] = useState(1);

  const dustState = [
    {
      icon: "/good.jpg?url",
      text: "좋음",
    },
    {
      icon: "/soso.jpg?url",
      text: "보통",
    },
    {
      icon: "/bad.jpg?url",
      text: "나쁨",
    },
    {
      icon: "/worst.jpg?url",
      text: "최악",
    },
  ];

  const data: KioskState = useSelector((state: { kiosk: KioskState }) => {
    return state.kiosk;
  });

  const TM =
    "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs";

  useEffect(() => {
    fetchMeasuringStation();
  }, []);

  const fetchMeasuringStation = async () => {
    const [tmx, tmy] = convertLatLngToTM(data.stationLon, data.stationLat);
    console.log("tmx, tmy :", tmx, tmy);
    axios
      .get(
        `https://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList?serviceKey=vg04G9ynpCLz1hobrfscOxzbc5l7eekaJGTiY6DeHxeEND3j%2FJ%2BTtcVlTP1%2F0OPAvW%2FsnRPZBDDLR9cXKpPzpg%3D%3D&returnType=json&tmX=${tmx}&tmY=${tmy}&ver=1.0`
      )
      .then((response) => {
        // 가까운 측정소 이름을 저장
        // console.log(response.data.response.body.items[0].stationName);
        setMeasuringStation(response.data.response.body.items[0].stationName);
      })
      .catch((error) => {
        console.error("Error fetching dust information:", error);
      });
  };

  // proj4 라이브러리에 정의 추가
  proj4.defs("TM", TM);

  const convertLatLngToTM = (lon: number, lat: number): [number, number] => {
    return proj4("WGS84", "TM", [lon, lat]);
  };

  // const busStop = "정류장이름";
  // const latitude: number = 36.12;
  // const longitude: number = 128.34;

  const useTempData = false;

  const tempDustData = {
    pm25Grade1h: "1",
    pm10Value24: "31",
    so2Value: "0.002",
    pm10Grade1h: "1",
    o3Grade: "2",
    pm10Value: 18,
    khaiGrade: "2",
    pm25Value: 12,
    mangName: "도시대기",
    no2Value: "0.006",
    so2Grade: "1",
    khaiValue: "66",
    coValue: "0.3",
    no2Grade: "1",
    pm25Value24: "22",
    pm25Grade: "2",
    coGrade: "1",
    dataTime: "2023-08-01 23:00",
    pm10Grade: "2",
    o3Value: "0.040",
  };

  useEffect(() => {
    if (dustInfo == null) {
      return;
    }
    if (dustInfo.pm10Value <= 15) {
      setState10(0);
    } else if (dustInfo.pm10Value <= 35) {
      setState10(1);
    } else if (dustInfo.pm10Value <= 75) {
      setState10(2);
    } else {
      setState10(3);
    }
    if (dustInfo.pm25Value <= 15) {
      setState25(0);
    } else if (dustInfo.pm25Value <= 35) {
      setState25(1);
    } else if (dustInfo.pm25Value <= 75) {
      setState25(2);
    } else {
      setState25(3);
    }
  }, [dustInfo]);

  function useInterval(callback: () => void, delay: number | null) {
    const savedCallback = useRef<typeof callback>(callback);

    useEffect(() => {
      savedCallback.current = callback;
    }, [callback]);

    useEffect(() => {
      const tick = () => {
        savedCallback.current();
      };

      if (delay !== null) {
        tick();
        const interval = setInterval(tick, delay);
        return () => clearInterval(interval);
      }
    }, [delay]);
  }

  // 미세먼지 API 호출 함수
  // 한국환경공단_에어코리아_대기오염정보
  // https://www.data.go.kr/data/15073861/openapi.do
  // 측정소 정보 필요
  const fetchDustInfo = async () => {
    axios
      .get(
        "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty",
        {
          params: {
            serviceKey:
              "vg04G9ynpCLz1hobrfscOxzbc5l7eekaJGTiY6DeHxeEND3j/J+TtcVlTP1/0OPAvW/snRPZBDDLR9cXKpPzpg==",
            numOfRows: 1,
            pageNo: 1,
            stationName: measuringStation, // 측정소 이름 (예: 강남구)
            dataTerm: "DAILY",
            ver: "1.3",
            returnType: "json",
          },
        }
      )
      .then((response) => {
        // API 응답 데이터를 추출하여 미세먼지 정보 객체로 변환
        console.log(response.data);

        const data: DustData = {
          dataTime: response.data.response.body.items[0].dataTime,
          pm10Value: response.data.response.body.items[0].pm10Value,
          pm25Value: response.data.response.body.items[0].pm25Value,
        };

        // 상태 변수 업데이트
        setDustInfo(data);
      })
      .catch((error) => {
        console.error("Error fetching dust information:", error);
      });
  };

  const tempFetchDustInfo = () => {
    setDustInfo({
      dataTime: tempDustData.dataTime,
      pm10Value: tempDustData.pm10Value,
      pm25Value: tempDustData.pm25Value,
    });
  };

  const func = useTempData ? tempFetchDustInfo : fetchDustInfo;
  useInterval(func, 30 * 60 * 1000);
  // useInterval(tempFetchDustInfo, 30 * 60 * 1000);
  // console.log(fetchDustInfo);

  return (
    <div {...props} className="dust-box">
      <div className="dust-info-box">
        <div className="fine-dust-box">
          <div className="info-title">미세먼지</div>
          <div className="dust-text">
            <div>
              <img
                className="dust-icon"
                src={dustState[state10].icon}
                alt={`10_${dustState[state10].icon}_icon`}
              />
            </div>
            <div>{dustState[state10].text}</div>
            <div>{dustInfo?.pm10Value} ㎍/㎥</div>
          </div>
        </div>
        <div className="micro-dust-box">
          <div className="info-title">초미세먼지</div>
          <div className="dust-text">
            <div>
              <img
                className="dust-icon"
                src={dustState[state25].icon}
                alt={`10_${dustState[state25].icon}_icon`}
              />
            </div>
            <div>{dustState[state25].text}</div>
            <div>{dustInfo?.pm25Value} ㎍/㎥</div>
          </div>
        </div>
      </div>
      {dustInfo ? (
        <div className="measurement-time">
          <p>
            측정 시간: {dustInfo.dataTime}, {measuringStation}
          </p>
        </div>
      ) : (
        <div className="">
          <p>데이터 로딩 중...</p>
        </div>
      )}
    </div>
  );
};
