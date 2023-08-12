import { WebLogoutPageProps } from ".";
import { FC, useEffect } from "react";
import { useDispatch } from "react-redux";
import { setIsUserIn, setToken } from "@/store/slice/web-slice";
import axios from "axios";
import webSlice from "@/store/slice/web-slice";
import { useSelector } from "react-redux/es/hooks/useSelector";
import { KioskState} from "@/store/slice/kiosk-slice";
import { WebState } from "@/store/slice/web-slice";
import { webAPI } from "@/store/api/api";

export const WebLogoutPage: FC<WebLogoutPageProps> = (props) => {
  const dispatch = useDispatch();
  const webData = useSelector((state:{kiosk:KioskState; web:WebState})=>{
    return state.web
  })
  useEffect(()=>{ 
    dispatch(setIsUserIn())
    const header = 
    webAPI.delete(`/user/logout`,)
    dispatch(setToken(null))

  })
  return(
    <div>
      
    </div>
  )
    
};
