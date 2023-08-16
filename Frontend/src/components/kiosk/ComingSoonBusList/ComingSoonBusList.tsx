import { FC } from "react";
import { ComingSoonBusListProps } from ".";
import { BusData } from "../../../store/slice/kiosk-slice";
import "./ComingSoonBusList.css";

interface ComingSoonBusListItemProps {
  item: BusData;
}

const ComingBusItem: FC<ComingSoonBusListItemProps> = ({ item }) => {
  return (
    <div className="comingsoon-bus-item">
      <div className="comingsoon-bus-type">
        {
          <img
            src={`/${item.routeType}.png?url`}
            alt={item.routeType}
            className="comingsoon-bus-route-icon"
          />
        }
      </div>
      <div className="comingsoon-bus-no">{item.busNo}</div>
      <div className="comingsoon-bus-remain">
        {item.remainingStops >= 3
          ? (item.eta / 60).toFixed(0) + " 분"
          : item.remainingStops >= 2
          ? "전전"
          : "전"}
      </div>
    </div>
  );
};

export const ComingSoonBusList: FC<ComingSoonBusListProps> = (props) => {
  return (
    <div {...props} className="comingsoon-box">
      <div className="comingsoon-text">곧 도착</div>
      {props.data.map((item: BusData, index: number) => {
        return <ComingBusItem key={index} item={item} />;
      })}
    </div>
  );
};
