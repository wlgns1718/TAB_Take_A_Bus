import { FC } from "react";
import { KioskHeaderProps } from "./KioskHeader.props";
import './KioskHeader.css'


export const KioskHeader: FC<KioskHeaderProps> = (props) => {
  const time = new Date();
  const hour = time.getHours();
  const min = time.getMinutes();

  const busStop = "우리집 앞";
  const logoURL = `/대구광역시_logo.png?url`;

  const fillZero = (num: number): string => {
    return num.toString().padStart(2, "0");
  };
  return (
    <div {...props}>
      <div
        className="kiosk-header"
        style={{
          fontFamily: "Inter",
          fontSize: "90px",
          fontWeight: "600",
          display: "flex",
          justifyContent: "center",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <div
          className="header-top"
          style={{
            width: "2100px",
            display: "flex",
            justifyContent: "space-between",
          }}
        >
          <div className="logo-wrap">
            <img
              id="logo"
              style={{ height: "180px", width: "580px" }}
              src={logoURL}
              alt="대구광역시로고"
            />
          </div>
          <div
            className="time-box"
            style={{ fontFamily: "Inter", fontSize: "60px" }}
          >
            <span style={{ color: "black" }}>현재시간 </span>
            <span>
              {fillZero(hour)}:{fillZero(min)}
            </span>
          </div>
        </div>
        <div className="busstop-title">
          이곳은 <span className="busstop-name">{busStop}</span> 정류장 입니다
        </div>
      </div>
    </div>
  );
};
