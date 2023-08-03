import { FC } from 'react';
import { ComingSoonBusListProps } from '.';
import { BusData } from '../../../pages/kiosk/BusInfomationPage';
import './ComingSoonBusList.css'

interface ComingSoonBusListItemProps {
  item: BusData;
}

const busColor: object = {
  일반버스: 'red_bus',
  좌석버스: 'blue_bus',
  저상버스: 'green_bus',
};


const ComingBusItem: FC<ComingSoonBusListItemProps> = ({ item }) => {
  return (
    <div className="comingsoon-bus-item">
      <div className="comingsoon-bus-type">
        {
          <img
            src={`../../../src/assets/image/${busColor[item.routeType]}.png`}
            alt={item.routeType}
            className="comingsoon-bus-route-icon"
          />
        }
      </div>
      <div className="comingsoon-bus-no">{item.busNo}</div>
      <div className="comingsoon-bus-remain">전전</div>
    </div>
  );
};


export const ComingSoonBusList: FC<ComingSoonBusListProps> = (props) => {
	return (
    <div {...props} className="comingsoon-box">
      <div className="comingsoon-text">곧 도착</div>
      {
        props.data.map((item : BusData, index: number)=>{
          return <ComingBusItem key={index} item={item}/>;
        })

      }

    </div>
  );
};
