import { FC } from "react";
import { QRcodeBoxProps } from ".";
import { useState } from "react";
import { QRCodeSVG  } from "qrcode.react";
import "./QRcodeBox.css";

export const QRcodeBox: FC<QRcodeBoxProps> = (props) => {
  const [address, setAdress] = useState("https://i9d111.p.ssafy.io/");
  setAdress;
  return (
    <div {...props} className="qrcode-box">
      <div className="qrcode-text">모바일 QR코드</div>
      <div className="qrcode-img">
        <QRCodeSVG className="qrcode-img" value={address} size={400} level="Q" />,
      </div>
    </div>
  );
};
