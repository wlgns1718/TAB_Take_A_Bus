import { FC } from "react";
import { MobileMainProps } from ".";
import "./MobileMain.css";

export const MobileMain: FC<MobileMainProps> = (props) => {
  return (
    <div {...props} className="mobile-body">
      <div></div>
    </div>
  );
};
