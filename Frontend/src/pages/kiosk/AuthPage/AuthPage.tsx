import { FC } from "react";
import { AuthPageProps } from ".";
import "./AuthPage.css";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import { useDispatch } from "react-redux";
import { checkMaster, KioskState } from "@/store/slice/kiosk-slice";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import React, { FunctionComponent, useState, useRef, ChangeEvent } from "react";
import KeyboardWrapper from "./keyboard/keyBoard";

export const AuthPage: FC<AuthPageProps> = (props) => {
  const [authKey, setAuthkey] = useState<string>("");
  const [busStopId, setBusStopId] = useState<string>("");
  const [AuthBus, setAuthBus] = useState<string | null>(null);
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
            if (data.masterkey == authKey) {
              dispatch(checkMaster(busStopId));
              navigate(`/kiosk/info/${busStopId}`);
            } else {
              alert("마스터키가 틀렸습니다. 다시 입력 해주세요.");
              setAuthkey("");
              setBusStopId("");
            }
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
