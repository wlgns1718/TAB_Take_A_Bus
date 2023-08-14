import { FC, useEffect, useState } from "react";
import { WebHeader } from "@/components/web/WebHeader";
import { WebRecommendPageProps } from ".";
import axios from "axios";
import { Option, Select } from "@mui/joy";

export const WebRecommendPage: FC<WebRecommendPageProps> = (props) => {
  // 도시코드 리스트
  // https://apis.data.go.kr/1613000/BusRouteInfoInqireService/getCtyCodeList?serviceKey=5ts%2Baf9Tv7mT28mcFD0Y8pzBg7sy1TYdLve4W7vJd5pt44kEEAkpi8AbNEVKnb%2Fk2z79M9WDxTozeVzNWlPkdA%3D%3D&_type=xml

  // 노선 리스트
  //  https://apis.data.go.kr/1613000/BusRouteInfoInqireService/getRouteNoList?serviceKey=5ts%2Baf9Tv7mT28mcFD0Y8pzBg7sy1TYdLve4W7vJd5pt44kEEAkpi8AbNEVKnb%2Fk2z79M9WDxTozeVzNWlPkdA%3D%3D&pageNo=1&numOfRows=100&_type=xml&cityCode=37050
  const [citis, setCitis] = useState([]);
  const [routes, setRoutes] = useState([]);

  const [selectedCity, setSelectedCity] = useState({
    citycode: "11",
    cityname: "서울특별시",
  });
  const [selectedRouteId, setSelectedRouteId] = useState({
    endnodenm: "세종고속시외버스터미널",
    endvehicletime: 2300,
    routeid: "SJB293000024",
    routeno: 430,
    routetp: "간선버스",
    startnodenm: "가톨릭꽃동네대학교",
    startvehicletime: "0620",
  });

  useEffect(() => {
    axios
      .get(
        `https://apis.data.go.kr/1613000/BusRouteInfoInqireService/getCtyCodeList?serviceKey=5ts%2Baf9Tv7mT28mcFD0Y8pzBg7sy1TYdLve4W7vJd5pt44kEEAkpi8AbNEVKnb%2Fk2z79M9WDxTozeVzNWlPkdA%3D%3D&_type=json`
      )
      .then((response) => {
        console.log(response.data.response.body.items.item);
        setCitis(response.data.response.body.items.item);
      });
  }, []);
  useEffect(() => {
    if (citis.length > 0) {
      axios
        .get(
          `https://apis.data.go.kr/1613000/BusRouteInfoInqireService/getRouteNoList?serviceKey=5ts%2Baf9Tv7mT28mcFD0Y8pzBg7sy1TYdLve4W7vJd5pt44kEEAkpi8AbNEVKnb%2Fk2z79M9WDxTozeVzNWlPkdA%3D%3D&pageNo=1&numOfRows=100&_type=json&cityCode=${selectedCity.citycode}`
        )
        .then((response) => {
          console.log(response.data.response.body.items.item);
          setRoutes(response.data.response.body.items.item);
        });
    }
  }, [selectedCity]);

  const handleCityChange = (value) => {
    console.log(value);
    setSelectedCity(value);
  };
  const handleRouteChange = (value) => {
    console.log(value);
    setSelectedRouteId(value);
  };

  return (
    <div {...props}>
      <div>관광/맛집</div>
      <div>
        <Select size="lg" placeholder="시/군 선택" className="select-city">
          {citis.map((op, index) => {
            return (
              <Option
                key={index}
                value={op.cityname}
                onClick={() => handleCityChange(op)}
                className="options-city"
              >
                {op.cityname}
              </Option>
            );
          })}
        </Select>
        <Select
          size="lg"
          placeholder="버스 노선 선택"
          className="select-bus-route"
        >
          {routes.map((op, index) => {
            return (
              <Option
                key={index}
                value={op}
                onClick={() => handleRouteChange(op)}
                className="options-city"
              >
                {op.routeno}
              </Option>
            );
          })}
        </Select>
      </div>
    </div>
  );
};
