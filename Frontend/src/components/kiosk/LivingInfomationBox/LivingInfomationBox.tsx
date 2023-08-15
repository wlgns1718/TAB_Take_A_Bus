import { FC } from "react";
import { LivingInformationBoxProps } from "./LivingInformation.props";
import { QRcodeBox } from "./QRcodeBox";
import { DustBox } from "./DustBox";
import { WeatherBox } from "./WeatherBox";

import "./LivingInfomationBox.css";

export const LivingInformationBox: FC<LivingInformationBoxProps> = (props) => {
  return (
    <div {...props} className="lv-info-container">
      <div className="weather-dust-box">
        <WeatherBox />
        <DustBox />
      </div>
      <QRcodeBox />
    </div>
  );
};
