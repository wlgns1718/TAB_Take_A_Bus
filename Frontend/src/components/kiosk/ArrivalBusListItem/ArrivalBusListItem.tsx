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
          <div className="text-center">도착예정시간</div>
          <div>{Math.round(item?.eta / 60)} 분후 도착</div>
        </div>
        <div className="bus-routeinfo bg-white round-10">
          <div>
            {item?.remainingStops} 정거장 남음 <br />
            현재 {item.stationName}
          </div>
        </div>
        <button
          className={`tap-button round-10 ${isStopHere ? "tap-on" : "tap-off"}`}
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
