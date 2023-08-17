import { FC, useState, useEffect } from "react";
import { MobileMainProps } from ".";

import "./MobileMain.css";
import { useParams } from "react-router-dom";

export const MobileMain: FC<MobileMainProps> = (props) => {
  const params = useParams();
  const [vehicleNo, setVehicleNo] = useState<string>("");
  const [vulnerable, setVulnerable] = useState<boolean>(false);
  const [stationId, setStationId] = useState<string>("");
  const [stationName, setStationName] = useState<string>("");
  const [isShowTime, setIsShowTime] = useState<boolean>(false);

  const [time, setTime] = useState<string>("");

  const audio1 = new Audio("/정차벨.mp3?url");
  const audio2 = new Audio("/정차벨_교통약자안내음.mp3?url");

  useEffect(() => {
    setVehicleNo(params?.vehicleNo ? params?.vehicleNo : "");
  }, [vehicleNo]);

  const fetchData = () => {
    const apiUrl = `https://i9d111.p.ssafy.io/tab/arduino/${vehicleNo}`;

    fetch(apiUrl)
      .then((response) => response.json())
      .then((responseData) => {
        console.log(responseData);

        if (
          responseData.code == "404" ||
          responseData.code == "401" ||
          responseData.code == "500"
        ) {
          return;
        }
        console.log(responseData.data.routeNo);
        console.log(responseData.data.stationId);
        console.log(responseData.data.vulnerable);
        setStationId(responseData.data.stationId);
        setVulnerable(responseData.data.vulnerable);
        startShowTime();
        if (responseData.data.vulnerable) {
          playSoundVulnerable();
        } else {
          playSound();
        }
      })
      .then()
      .catch((error) => {
        console.error("Error fetching data:", error);
      });
  };


  const startShowTime = () => {
    setIsShowTime(true);

    setTimeout(() => {
      setIsShowTime(false);
    }, 15000); // 10초 후에 isShowTime을 다시 false로 설정
  };

  useEffect(() => {
    if (!vehicleNo) {
      return;
    }
    fetchData(); //

    const interval = setInterval(() => {
      // 10초마다 feth요청 반복
      fetchData();
    }, 5000);

    return () => {
      clearInterval(interval); // interval 초기화 시킴
    };
  }, [vehicleNo]);

  const playSound = () => {
    audio1.play();
  };
  const playSoundVulnerable = () => {
    audio2.play();
  };

  // ************************여기부터 시계************************

  function clock() {
    var date = new Date();

    var month = date.getMonth();

    var clockDate = date.getDate();

    var day = date.getDay();

    var week = ["일", "월", "화", "수", "목", "금", "토"];

    var hours = date.getHours();

    var minutes = date.getMinutes();

    var seconds = date.getSeconds();

    setTime(
      `${month + 1}월 ${clockDate}일 ${week[day]}요일` +
        `${hours < 10 ? `0${hours}` : hours}:${
          minutes < 10 ? `0${minutes}` : minutes
        }:${seconds < 10 ? `0${seconds}` : seconds}`
    );
  }

  function init() {
    clock();

    setInterval(clock, 1000);
  }

  useEffect(() => {
    init();
  }, []);

  // ************************여기까지 시계************************

  return (
    <div {...props} className="mobile-body">
      <h1 id="clock">{time}</h1>
      <div className="content">
        <div className="content-header">
          <div style={{ display: "flex" }}>
            <h1 className="white">차량번호</h1>

            <h1 className="yellow"> : {vehicleNo}</h1>
          </div>
        </div>

        {isShowTime ? (
          <div className="content-body">
            <div style={{ display: "flex" }}>
              <h1 className="white">이번 정류장</h1>
              <h1 className="yellow">: [ {stationId} ]</h1>
            </div>
            <h1 className="blinking-text"> 탑승객이 있습니다.</h1>
            <h1 className="yellow blinking-text">
              {vulnerable === true ? "교통약자O" : "교통약자X"}
            </h1>
          </div>
        ) : (
          <div className="content-body">
            <h1> 탑승객이 없습니다.</h1>
          </div>
        )}
      </div>
    </div>
  );
};
