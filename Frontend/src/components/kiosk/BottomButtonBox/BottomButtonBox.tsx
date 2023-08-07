import { FC } from "react";
import { BottomButtonBoxProps } from ".";
import "./BottomButtonBox.css";
import {
  BusStoreData,
  KioskState,
  SetVulnerable,
} from "@/store/slice/kiosk-slice";
import { useDispatch, useSelector } from "react-redux";

export const BottomButtonBox: FC<BottomButtonBoxProps> = ({ pages }) => {
  const nowPage: number = useSelector(
    (state: { kiosk: KioskState; web: object }) => {
      return state.kiosk.nowCarouselPage;
    }
  );
  return (
    <div className="font">
      <div
        className="setbtn"
        style={{
          backgroundColor: "#208EF4",
          display: "flex",
          flexDirection: "column",
          borderRadius: "40px 40px 0 0",
        }}
      >
        <div
          style={{
            justifyContent: "center",
            display: "flex",
            height: "180px",
            marginBottom: "50px",
          }}
        >
          <h1 style={{ fontSize: "80px", color: "white" }}>
            교통약자 탑승버튼
          </h1>
          <img
            style={{ height: "160px", width: "180", margin: "20px" }}
            src="/wheelchair_big.png?url"
            alt="실패"
          />
        </div>

        <div className="button-pages">
          {pages.map((page, pagenumber) => {
            return (
              pagenumber == nowPage && (
                <div key={pagenumber} className="button-page">
                  {page.map((item: BusStoreData, index: number) => {
                    return <Btn item={item} key={index} />;
                  })}
                </div>
              )
            );
          })}
        </div>
      </div>
    </div>
  );
};

function Btn({ item }: { item: BusStoreData }) {
  const dispatch = useDispatch();
  return (
    <button
      className="bottom-button"
      style={{
        backgroundColor: !item.isVulnerable ? "white" : "#FF0505",
        alignItems: "center",
        display: "flex",
        justifyContent: "center",
        color: !item.isVulnerable ? "black" : "white",
      }}
      onClick={() => {
        dispatch(
          SetVulnerable({
            vehicleNo: item.vehicleNo,
            remainingStops: item.remainingStops,
          })
        );
      }}
    >
      <div className="kmarquee">
        <div className="outer">
          <div className="inner">
            {item.isVulnerable ? (
              <div className="incontent" style={{ lineHeight: "0%" }}>
                <p>{item.busNo}</p>
              </div>
            ) : (
              <div className="incontent" style={{ lineHeight: "0%" }}>
                <p>{item.busNo}</p>
              </div>
            )}
          </div>
        </div>
        {item.isVulnerable ? (
          <p
            style={{
              fontSize: "38px",
              left: "3px",
              position: "relative",
            }}
          >
            정차예정
          </p>
        ) : (
          <p
            style={{
              fontSize: "38px",
              left: "3px",
              position: "relative",
            }}
          >
            탑승하기
          </p>
        )}
      </div>
    </button>
  );
}
