import { FC } from "react";
import { WebHeaderProps } from ".";
import { Link, Route, NavLink, useNavigate } from "react-router-dom";
import "./WebHeader.css";
import webSlice from "@/store/slice/web-slice";
import { useSelector } from "react-redux/es/hooks/useSelector";
import { KioskState } from "@/store/slice/kiosk-slice";
import { WebState } from "@/store/slice/web-slice";
export const WebHeader: FC<WebHeaderProps> = (props) => {
  const navigation = useNavigate;
  const webData = useSelector((state:{kiosk:KioskState ,web:WebState})=>{
    return state.web
  })
  return (
    <div {...props}>
      <div className="web-header">
        <div className="web-header-logo" onClick={() => navigation()}>
          <img
            src="/TAB_logo.png?url"
            alt="TAB_logo"
            className="web-header-logo"
          />
        </div>
        <div className="header-links">
          <NavLink to="/web">홈</NavLink>
          <NavLink to="/web/board"> 게시판</NavLink>
          <NavLink to="/web/recommend/"> 관광/맛집</NavLink>
          <NavLink to="/web/survey"> 수요조사 </NavLink>
        </div>
        <div className="header-btns">
          {!webData.isUserIn ? <div>
          <NavLink to="/web/login/"> 로그인</NavLink>
          </div>
          :
          <NavLink to="/web/loout/"> 로그아웃</NavLink>}
          
          
        </div>
      </div>
    </div>
  );
};
