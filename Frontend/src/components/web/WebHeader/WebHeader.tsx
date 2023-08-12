import { FC } from "react";
import { WebHeaderProps } from ".";
import { Link, Route, NavLink, useNavigate } from "react-router-dom";
import "./WebHeader.css";
import webSlice from "@/store/slice/web-slice";
import { useSelector } from "react-redux/es/hooks/useSelector";
import { KioskState } from "@/store/slice/kiosk-slice";
import { WebState } from "@/store/slice/web-slice";
export const WebHeader: FC<WebHeaderProps> = (props) => {
  const navigation = useNavigate();
  const webData = useSelector((state:{kiosk:KioskState ,web:WebState})=>{
    return state.web
  })
  return (
    <div {...props}>
      <div className="web-header">
        <div className="web-header-logo" >
          <img
            src="/TAB_logo.png?url"
            alt="TAB_logo"
            className="web-header-logo"
            onClick={() => navigation('/web/home')}
          />
        </div>
        <div className="header-links" id="navitems">
          <NavLinkForm dest ="/web/home" word="홈" />
          <NavLinkForm dest ="/web/board"  word="게시판" />
          <NavLinkForm dest ="/web/recommend/" word="관광/맛집" />
          <NavLinkForm dest ="/web/survey" word="수요조사" />
        </div>
        <div className="header-btns" id="navitems">
        {!webData.isUserIn ? <div>
          <NavLinkForm dest ="/web/login/" word="로그인" />
          </div>
          :
          <NavLinkForm dest ="/web/login/" word="로그아웃" />
        }
          
        </div>
      </div>
    </div>
  );
};


function NavLinkForm(props){
  return (
    <NavLink to={props.dest} 
    style={({ isActive, isPending }) => {
      return {
        fontSize:'21px',
        fontWeight: isActive ? "bold" : "",
        color: isActive ? "#016FF2" : "black",
      };
      }} >{props.word}</NavLink>
  )

}