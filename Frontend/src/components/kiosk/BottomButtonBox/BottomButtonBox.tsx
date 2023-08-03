import { FC } from "react";
import { BottomButtonBoxProps } from ".";
import "./BottomButtonBox.css";
import { useState } from "react";

type BusState = {
  busNo: number;
  redLight: boolean;
  wheelchair: boolean;
};

export const BottomButtonBox: FC<BottomButtonBoxProps> = (props) => {
  const [bus, setBus] = useState<Array<BusState>>([
    { busNo: 123, redLight: false, wheelchair: false },
    { busNo: 501, redLight: false, wheelchair: false },
    { busNo: 305, redLight: false, wheelchair: false },
  ]);

  const onBusClick = (busName) => {
    const temp = [...bus];

    setBus(
      temp.map((element) => {
        if (element.busNo === busName) {
          console.log(element);
          element.redLight = true;
          element.wheelchair = true;
          console.log(element);
        }
        return element;
      })
    );
  };
  return (
    <div {...props}>
      <div>
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
              src="/src/assets/image/wheelchair_big.png"
              alt="실패"
            />
          </div>

          <div style={{ display: "flex", justifyContent: "space-around" }}>
            {bus.map((element: object, index: number) => {
              return <Btn props={{ element, onBusClick }} key={index} />;
            })}
          </div>
        </div>
      </div>
    </div>
  );
};

function Btn({ props: { element, onBusClick } }) {
  return (
    <button
      className="bottom-button"
      style={{
        fontSize: "55px",
        backgroundColor: !element.redLight ? "white" : "red",
      }}
      onClick={() => {
        onBusClick(element.busNo);
      }}
    >
      {element.busNo}
    </button>
  );
}
