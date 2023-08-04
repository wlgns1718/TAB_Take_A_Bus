import { FC, useState } from "react";
import { ArrivalBusListItemProps } from ".";
import "./ArrivalBusListItem.css";

export const ArrivalBusListItem: FC<ArrivalBusListItemProps> = ({ item }) => {
  const [isStopHere, setIsStopHere] = useState(false);

  const busIconURL = `/bus_side_icon.png?url`;
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
          <div className="bus-eta-title text-center ">도착예정시간</div>
          <div className="bus-eta-eta text-center">ETA</div>
          <div className="bus-eta-text ">
            <div className="bus-eta-num ">{Math.round(item?.eta / 60)}</div>
            <div className="bus-eta-min ">
              <div>분</div>
              <div className="bus-eta-min-eng"> min</div>
            </div>
          </div>
        </div>
        <div className="bus-routeinfo bg-white round-10">
          <div className="bus-map-line"> </div>
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
            src={busIconURL}
            style={{
              left: `${
                ((item.remainingStops >= 4 ? 0 : 4 - item.remainingStops) / 5) *
                  720 +
                85
              }px`,
            }}
          ></img>
          <div
            className="current-station-name"
            style={{
              left: `${
                ((item.remainingStops >= 4 ? 0 : 4 - item.remainingStops) / 5) *
                  720 +
                85
              }px`,
            }}
          >
            {item.stationName}
          </div>
        </div>
        <div
          className={`tap-button ${isStopHere ? "tap-on" : "tap-off"}`}
          onClick={() => setIsStopHere(true)}
        >
          {isStopHere ? "정차 예정" : "탑승"}
        </div>
      </div>
      <div className="guide-message">
        {isStopHere ? (
          <span>이 버스는 현 정류장에 정차할 예정입니다.</span>
        ) : (
          <span>이 버스에 탑승하시려면 탑승 버튼을 눌러주세요.</span>
        )}
      </div>
    </div>
  );
};
