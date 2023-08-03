import { FC, useState } from "react";
import { ArrivalBusListItemProps } from ".";
import "./ArrivalBusListItem.css";

export const ArrivalBusListItem: FC<ArrivalBusListItemProps> = ({ item }) => {
  const [isStopHere, setIsStopHere] = useState(false);
  return (
    <div className="bus-item-container">
      <div className="bus-item-top">
        <div className="bus-detail bg-white round-10">
          <div className="">
            <div className="">대충 아이콘</div>
            <div>{item.routeType}</div>
          </div>
          <div className="">
            <h1>{item.busNo}</h1>
            <div className="">{item.vehicleType}</div>
          </div>
        </div>
        <div className="bus-eta bg-white round-10">
          <div className="bus-eta-title text-center flex-1">도착예정시간</div>
          <div className="bus-eta-text flex-2">
            <div className="bus-eta-num flex-2">
              {Math.round(item?.eta / 60)}
            </div>
            <div className="bus-eta-min flex-1"> 분 </div>
          </div>
        </div>
        <div className="bus-routeinfo bg-white round-10">
          <div className="bus-map-line"> d</div>
          {[
            item.remainingStops >= 4 ? item.remainingStops : 4,
            3,
            2,
            1,
            "현",
          ].map((station) => (
            <div
              key={station}
              className={`station-circle ${
                station === item.remainingStops ? "current-station" : ""
              }`}
            >
              {station}
            </div>
          ))}
          <img
            className="bus-icon"
            src="..\..\src\assets\image\bus_side_icon.png"
            style={{
              left: `${
                ((item.remainingStops >= 4 ? 0 : 4 - item.remainingStops) / 5) *
                  750 +
                75
              }px`,
            }}
          ></img>
        </div>
        <button
          className={`tap-button ${isStopHere ? "tap-on" : "tap-off"}`}
          onClick={() => setIsStopHere(true)}
        >
          {isStopHere ? "정차 예정" : "탑승"}
        </button>
      </div>
      <div className="guide-message">
        {isStopHere ? (
          <span>이 버스는 현 정류장에 정차할 예정입니다.</span>
        ) : (
          <span>이 버스에 탑승하시려면 탑승 버튼을 눌러주세요.</span>
        )}
      </div>
      {/* <div>busNo : {item.busNo} </div>
      <div>routeId : {item.routeId}</div>
      <div>routeType : {item.routeType}</div>
      <div>vehicleType : {item.vehicleType}</div>
      <div>vehicleNo : {item.vehicleNo}</div>
      <div>stationOrder : {item.stationOrder}</div>
      <div>stationName : {item.stationName}</div>
      <div>stationId : {item.stationId}</div> */}
    </div>
  );
};
