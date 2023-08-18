import React from "react";
import { useDispatch } from "react-redux";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { setToken,setIsUserIn,setLoginUser } from "@/store/slice/web-slice";

function Kakaopage() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  useEffect(() => {
    const getkakao = new URL(window.location.href).searchParams.get("code");
    axios
      .post("https://i9d111.p.ssafy.io/tab/user/login/kakao", {
        code: getkakao,
      })
      .then((response) => {
        console.log(response);
        dispatch(setToken(response.data.data.accessToken));
        dispatch(setIsUserIn(true));
        dispatch(
          setLoginUser({
            loginData: {
              id: response.data.data.id,
              role: response.data.data.role,
            },
          })
          );
        navigate("/");
      })
      .catch((error) => {
        console.log(error);
        navigate("/");
      });
  });
  return <div></div>;
}

export default Kakaopage;
