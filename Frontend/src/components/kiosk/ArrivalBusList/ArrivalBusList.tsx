import { FC, useState, useEffect } from "react";
import { ArrivalBusListProps } from ".";
import { ArrivalBusListItem } from "../ArrivalBusListItem";
import Carousel from "react-material-ui-carousel";
import "./ArrivalBusList.css";
import {
  BusStoreData,
  syncCarouselPage,
} from "../../../store/slice/kiosk-slice";
import { useDispatch, useSelector } from "react-redux";
import { Container } from "@mui/joy";

export const ArrivalBusList: FC<ArrivalBusListProps> = ({ pages }) => {
  const dispatch = useDispatch();

  return (
    <div className="bus-list-container">
      <Carousel
        indicatorIconButtonProps={{
          style: {
            padding: "20px", // 1
          },
        }}
        indicatorContainerProps={{
          style: {
            marginTop: "-30px", // 5
            textAlign: "center", // 4
          },
        }}
        duration={500}
        cycleNavigation={true}
        swipe={true}
        animation="fade"
        height={1900}
        interval={10000}
        stopAutoPlayOnHover={true}
        navButtonsAlwaysVisible={true}
        index={1}
        className="carouselll"
        onChange={(now, prev): void => {
          dispatch(syncCarouselPage({ now: now }));
          return;
        }}
      >
        {pages.length > 0 ? (
          pages.map((page) => {
            return page.map((item: BusStoreData, index: number) => {
              return <ArrivalBusListItem item={item} key={index} />;
            });
          })
        ) : (
          <Container sx={{ fontSize: 60, marginTop: 20, textAlign: "center" }}>
            도착 예정 정보가 없습니다..
          </Container>
        )}
      </Carousel>
    </div>
  );
};
