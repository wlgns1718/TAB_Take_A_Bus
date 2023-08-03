import { FC, useState, useEffect } from "react";
import { BusData } from "../../../store/slice/kiosk-slice";
import { ArrivalBusListItem } from "../ArrivalBusListItem";
import Carousel from "react-material-ui-carousel";
import "./ArrivalBusList.css";

interface ArrivalBusListProps {
  data: BusData[];
}

export const ArrivalBusList: FC<ArrivalBusListProps> = (props) => {

  const [pages, setPage] = useState([]);

  const paginateArray = (arr:BusData[], pageSize:number) => {
    const pageCount = Math.ceil(arr.length / pageSize);
    const pagelist = Array.from({ length: pageCount }, (_, index) => {
      const startIndex = index * pageSize;
      return arr.slice(startIndex, startIndex + pageSize);
    });
    return pagelist;
  };

  useEffect(() => {
    // 페이지 지정
    setPage(paginateArray(props.data, 4));
  }, [props.data]);

  return (
    <div className="bus-list-container">
      <Carousel
        duration={500}
        cycleNavigation={true}
        swipe={true}
        animation="fade"
        height={1900}
        interval={5000}
        navButtonsAlwaysVisible={true}
        index={1}
      > 
        {pages.map((page) => {
          return page.map((item: BusData, index: number) => {
            return <ArrivalBusListItem item={item} key={index} />;
          });
        })}
      </Carousel>
    </div>
  );
};
