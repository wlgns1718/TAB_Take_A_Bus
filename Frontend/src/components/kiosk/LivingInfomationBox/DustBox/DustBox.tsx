import { FC } from "react";
import { useEffect, useState, useRef } from "react";
import axios from "axios";
import { DustBoxProps } from ".";
import "./DustBox.css";

// 미세먼지 정보를 담을 인터페이스 (API 응답 데이터 구조에 맞게 정의)
interface DustData {
  dataTime: string;
  pm10Value: number;
  pm25Value: number;
  // 다른 필요한 미세먼지 정보도 추가할 수 있습니다.
}

export const DustBox: FC<DustBoxProps> = (props) => {
  const [dustInfo, setDustInfo] = useState<DustData | null>(null);

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
            // 위도와 경도를 이용하여 가장 가까운 측정소 이름을 찾습니다.
            // 이 부분은 추가적인 로직이 필요합니다.
            // 예를 들어, 다른 API를 이용하거나 미리 측정소와 위도/경도 정보를 매핑해둔 데이터를 사용해야 합니다.
            stationName: "형곡동", // 측정소 이름 (예: 강남구)
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
            <div>아이콘</div>
            <div>좋음</div>
            <div>{dustInfo?.pm10Value} ㎍/㎥</div>
          </div>
        </div>
        <div className="micro-dust-box">
          <div className="info-title">초미세먼지</div>
          <div className="dust-text">
            <div>아이콘</div>
            <div>좋음</div>
            <div>{dustInfo?.pm25Value} ㎍/㎥</div>
          </div>
        </div>
      </div>
      {dustInfo ? (
        <div className="measurement-time">
          <p>측정 시간: {dustInfo.dataTime}</p>
        </div>
      ) : (
        <div className="">
          <p>데이터 로딩 중...</p>
        </div>
      )}
    </div>
  );
};
