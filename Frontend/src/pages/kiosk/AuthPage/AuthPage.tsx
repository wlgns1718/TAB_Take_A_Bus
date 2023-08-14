import { FC, useEffect } from "react";
import { AuthPageProps } from ".";
import "./AuthPage.css";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import { useDispatch } from "react-redux";
import {
  checkMaster,
  KioskState,
} from "@/store/slice/kiosk-slice";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import React, { FunctionComponent, useState, useRef, ChangeEvent } from "react";
import KeyboardWrapper from "./keyboard/keyBoard";
import { Option, Select } from "@mui/joy";
// import Papa from "papaparse";
import { busAPI } from "@/store/api/api";

export const AuthPage: FC<AuthPageProps> = (props) => {
  const [authKey, setAuthkey] = useState<string>("");
  const [busStopId, setBusStopId] = useState<string>("");
  const [AuthBus, setAuthBus] = useState<string | null>(null);

  // const [csvData, setCsvData] = useState([]);
  const citis = [
    { 도시명: "가평군", 도시코드: "31370" },
    { 도시명: "강릉시", 도시코드: "32030" },
    { 도시명: "강진군", 도시코드: "36390" },
    { 도시명: "거제시", 도시코드: "38090" },
    { 도시명: "거창군", 도시코드: "38390" },
    { 도시명: "경산시", 도시코드: "37100" },
    { 도시명: "경주시", 도시코드: "37020" },
    { 도시명: "계룡시", 도시코드: "34070" },
    { 도시명: "고령군", 도시코드: "37370" },
    { 도시명: "고성군", 도시코드: "38340" },
    { 도시명: "고양시", 도시코드: "31100" },
    { 도시명: "고창군", 도시코드: "35370" },
    { 도시명: "고흥군", 도시코드: "36350" },
    { 도시명: "공주시", 도시코드: "34020" },
    { 도시명: "과천시", 도시코드: "31110" },
    { 도시명: "광명시", 도시코드: "31060" },
    { 도시명: "광양시", 도시코드: "36060" },
    { 도시명: "광주광역시", 도시코드: "24" },
    { 도시명: "광주시", 도시코드: "31250" },
    { 도시명: "괴산군", 도시코드: "33360" },
    { 도시명: "구례군", 도시코드: "36330" },
    { 도시명: "구리시", 도시코드: "31120" },
    { 도시명: "구미시", 도시코드: "37050" },
    { 도시명: "군산시", 도시코드: "35020" },
    { 도시명: "군위군", 도시코드: "37310" },
    { 도시명: "군포시", 도시코드: "31160" },
    { 도시명: "금산군", 도시코드: "34310" },
    { 도시명: "김제시", 도시코드: "35060" },
    { 도시명: "김천시", 도시코드: "37030" },
    { 도시명: "김포시", 도시코드: "31230" },
    { 도시명: "김해시", 도시코드: "38070" },
    { 도시명: "나주시", 도시코드: "36040" },
    { 도시명: "남양주시", 도시코드: "31130" },
    { 도시명: "남원시", 도시코드: "35050" },
    { 도시명: "남해군", 도시코드: "38350" },
    { 도시명: "논산시", 도시코드: "34060" },
    { 도시명: "단양군", 도시코드: "33380" },
    { 도시명: "담양군", 도시코드: "36310" },
    { 도시명: "당진시", 도시코드: "34390" },
    { 도시명: "대구광역시", 도시코드: "22" },
    { 도시명: "대전광역시", 도시코드: "25" },
    { 도시명: "동두천시", 도시코드: "31080" },
    { 도시명: "동해시", 도시코드: "32040" },
    { 도시명: "목포시", 도시코드: "36010" },
    { 도시명: "무안군", 도시코드: "36420" },
    { 도시명: "무주군", 도시코드: "35330" },
    { 도시명: "문경시", 도시코드: "37090" },
    { 도시명: "밀양시", 도시코드: "38080" },
    { 도시명: "보령시", 도시코드: "34030" },
    { 도시명: "보은군", 도시코드: "33320" },
    { 도시명: "봉화군", 도시코드: "37410" },
    { 도시명: "부산광역시", 도시코드: "21" },
    { 도시명: "부안군", 도시코드: "35380" },
    { 도시명: "부여군", 도시코드: "34330" },
    { 도시명: "부천시", 도시코드: "31050" },
    { 도시명: "사천시", 도시코드: "38060" },
    { 도시명: "산청군", 도시코드: "38370" },
    { 도시명: "삼척시", 도시코드: "32070" },
    { 도시명: "상주시", 도시코드: "37080" },
    { 도시명: "서산시", 도시코드: "34050" },
    { 도시명: "서울특별시", 도시코드: "11" },
    { 도시명: "서천군", 도시코드: "34340" },
    { 도시명: "성남시", 도시코드: "31020" },
    { 도시명: "성주군", 도시코드: "37380" },
    { 도시명: "세종특별시", 도시코드: "12" },
    { 도시명: "속초시", 도시코드: "32060" },
    { 도시명: "수원시", 도시코드: "31010" },
    { 도시명: "순창군", 도시코드: "35360" },
    { 도시명: "순천시", 도시코드: "36030" },
    { 도시명: "시흥시", 도시코드: "31150" },
    { 도시명: "신안군", 도시코드: "36480" },
    { 도시명: "아산시", 도시코드: "34040" },
    { 도시명: "안동시", 도시코드: "37040" },
    { 도시명: "안산시", 도시코드: "31090" },
    { 도시명: "안성시", 도시코드: "31220" },
    { 도시명: "안양시", 도시코드: "31040" },
    { 도시명: "양구군", 도시코드: "32380" },
    { 도시명: "양산시", 도시코드: "38100" },
    { 도시명: "양양군", 도시코드: "32410" },
    { 도시명: "양주시", 도시코드: "31260" },
    { 도시명: "양평군", 도시코드: "31380" },
    { 도시명: "여수시", 도시코드: "36020" },
    { 도시명: "여주시", 도시코드: "31320" },
    { 도시명: "연천군", 도시코드: "31350" },
    { 도시명: "영광군", 도시코드: "36440" },
    { 도시명: "영덕군", 도시코드: "37350" },
    { 도시명: "영동군", 도시코드: "33340" },
    { 도시명: "영암군", 도시코드: "36410" },
    { 도시명: "영양군", 도시코드: "37340" },
    { 도시명: "영월군", 도시코드: "32330" },
    { 도시명: "영주시", 도시코드: "37060" },
    { 도시명: "영천시", 도시코드: "37070" },
    { 도시명: "예산군", 도시코드: "34370" },
    { 도시명: "예천군", 도시코드: "37400" },
    { 도시명: "오산시", 도시코드: "31140" },
    { 도시명: "옥천군", 도시코드: "33330" },
    { 도시명: "완도군", 도시코드: "36460" },
    { 도시명: "완주군", 도시코드: "35310" },
    { 도시명: "용인시", 도시코드: "31190" },
    { 도시명: "울릉군", 도시코드: "37430" },
    { 도시명: "울산광역시", 도시코드: "26" },
    { 도시명: "울진군", 도시코드: "37420" },
    { 도시명: "원주시", 도시코드: "32020" },
    { 도시명: "음성군", 도시코드: "33370" },
    { 도시명: "의령군", 도시코드: "38310" },
    { 도시명: "의성군", 도시코드: "37320" },
    { 도시명: "의왕시", 도시코드: "31170" },
    { 도시명: "의정부시", 도시코드: "31030" },
    { 도시명: "이천시", 도시코드: "31210" },
    { 도시명: "익산시", 도시코드: "35030" },
    { 도시명: "인제군", 도시코드: "32390" },
    { 도시명: "인천광역시", 도시코드: "23" },
    { 도시명: "임실군", 도시코드: "35350" },
    { 도시명: "장성군", 도시코드: "36450" },
    { 도시명: "장수군", 도시코드: "35340" },
    { 도시명: "전주시", 도시코드: "35010" },
    { 도시명: "정선군", 도시코드: "32350" },
    { 도시명: "정읍시", 도시코드: "35040" },
    { 도시명: "제주도", 도시코드: "39" },
    { 도시명: "제천시", 도시코드: "33030" },
    { 도시명: "증평군", 도시코드: "33390" },
    { 도시명: "진도군", 도시코드: "36470" },
    { 도시명: "진안군", 도시코드: "35320" },
    { 도시명: "진주시", 도시코드: "38030" },
    { 도시명: "진천군", 도시코드: "33350" },
    { 도시명: "창녕군", 도시코드: "38330" },
    { 도시명: "창원시", 도시코드: "38010" },
    { 도시명: "천안시", 도시코드: "34010" },
    { 도시명: "철원군", 도시코드: "32360" },
    { 도시명: "청도군", 도시코드: "37360" },
    { 도시명: "청송군", 도시코드: "37330" },
    { 도시명: "청양군", 도시코드: "34350" },
    { 도시명: "청주시", 도시코드: "33010" },
    { 도시명: "춘천시", 도시코드: "32010" },
    { 도시명: "충주시", 도시코드: "33020" },
    { 도시명: "칠곡군", 도시코드: "37390" },
    { 도시명: "태백시", 도시코드: "32050" },
    { 도시명: "통영시", 도시코드: "38050" },
    { 도시명: "파주시", 도시코드: "31200" },
    { 도시명: "평창군", 도시코드: "32340" },
    { 도시명: "평택시", 도시코드: "31070" },
    { 도시명: "포천시", 도시코드: "31270" },
    { 도시명: "포항시", 도시코드: "37010" },
    { 도시명: "하남시", 도시코드: "31180" },
    { 도시명: "하동군", 도시코드: "38360" },
    { 도시명: "함안군", 도시코드: "38320" },
    { 도시명: "함양군", 도시코드: "38380" },
    { 도시명: "함평군", 도시코드: "36430" },
    { 도시명: "합천군", 도시코드: "38400" },
    { 도시명: "해남군", 도시코드: "36400" },
    { 도시명: "홍성군", 도시코드: "34360" },
    { 도시명: "홍천군", 도시코드: "32310" },
    { 도시명: "화성시", 도시코드: "31240" },
    { 도시명: "횡성군", 도시코드: "32320" },
  ];

  const [busStopList, setBusStopList] = useState();

  const [selectedCity, setSelectedCity] = useState({
    도시명: "서울특별시",
    도시코드: "11",
  });
  const keyboard = useRef(null);

  const onChangeInputAuth = (event: ChangeEvent<HTMLInputElement>): void => {
    const input = event.target.value;
    setAuthkey(input);
  };

  const onChangeInputBusId = (event: ChangeEvent<HTMLInputElement>): void => {
    const input = event.target.value;
    setBusStopId(input);
  };

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const data: KioskState = useSelector(
    (state: { kiosk: KioskState; web: object }) => {
      return state.kiosk;
    }
  );

  const updateBusStopList = () =>{
    busAPI.get(`${selectedCity.도시명}`).then((response) => {
      const busStops = response.data.data.map(
        (item) =>
          {return {
            busStopId : item.정류장ID, 
            busStationName : item.정류장명,
          }}
      );
      setBusStopList(busStops);
    });
  }

  const settingKiosk = () => {
    if (data.masterkey == authKey) {
        busAPI.get(`${selectedCity.도시코드}/${busStopId}`).then((response) => {
          console.log(response.data);
          busAPI.get(`present/${busStopId}`).then((response) => {
            console.log(response.data);
            dispatch(checkMaster({busStopId,cityCode:selectedCity.도시코드,stationName:selectedCity.도시명}));
            navigate(`/kiosk/info/${busStopId}`);
          });
        });

    } else {
      alert("마스터키가 틀렸습니다. 다시 입력 해주세요.");
      setAuthkey("");
      setBusStopId("");
      return;
    }
  };

  const handleCityChange = (value) => {
    console.log(value);
    setSelectedCity(value);
  };

  return (
    <div className="mainbox" {...props}>
      <div style={{ paddingTop: "800px" }}>
        <h1
          style={{
            fontSize: "160px",
            textAlign: "center",
            marginTop: "0%",
            marginBottom: "300px",
          }}
        >
          정류장 선택
        </h1>
      </div>

      <Select
        size="lg"
        placeholder="시/군 선택"
        defaultValue="대구광역시"
        value={selectedCity.도시명}
        className="select-city"
      >
        {citis.map((op, index) => {
          return (
            <Option
              key={index}
              value={op.도시명}
              onClick={() => handleCityChange(op)}
              className="options-city"
            >
              {op.도시명}
            </Option>
          );
        })}
      </Select>

      <div className="Authkey">
        <input
          className="keyboardinput"
          value={authKey}
          placeholder={"masterKey를 입력해주세요"}
          onChange={(e) => onChangeInputAuth(e)}
          onClick={() => setAuthBus("Auth")}
        />
      </div>

      <div className="Buskey">
        <input
          className="keyboardinput"
          value={busStopId}
          placeholder={"정류장 번호를 입력해 주세요."}
          onChange={(e) => onChangeInputBusId(e)}
          onClick={() => setAuthBus("Bus")}
        />
      </div>
      <div>
        {AuthBus == "Auth" ? (
          <KeyboardWrapper keyboardRef={keyboard} onChange={setAuthkey} />
        ) : null}
        {AuthBus == "Bus" ? (
          <KeyboardWrapper keyboardRef={keyboard} onChange={setBusStopId} />
        ) : null}
      </div>

      <div style={{ display: "flex", justifyContent: "center" }}>
        <Button
          onClick={() => {
            settingKiosk();
          }}
          style={{ fontSize: "100px", marginTop: "100px" }}
          className="button"
          variant="contained"
        >
          확인
        </Button>
      </div>
    </div>
  );
};
