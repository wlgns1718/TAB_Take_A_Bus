import { FC, useState } from "react";
import { WebLoginPageProps } from ".";
import Button from "@mui/joy/Button";
import "./WebLogin.css";
import { useNavigate } from "react-router-dom";
import React from "react";
import IconButton from "@mui/material/IconButton";
import Input from "@mui/material/Input";
import FilledInput from "@mui/material/FilledInput";
import OutlinedInput from "@mui/material/OutlinedInput";
import InputLabel from "@mui/material/InputLabel";
import InputAdornment from "@mui/material/InputAdornment";
import FormHelperText from "@mui/material/FormHelperText";
import FormControl from "@mui/material/FormControl";
import TextField from "@mui/material/TextField";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import {
  setToken,
  setIsUserIn,
  WebState,
  setLoginUser,
  LoginData,
} from "@/store/slice/web-slice";
import { useDispatch, useSelector } from "react-redux";
import { webAPI } from "@/store/api/api";
import webSlice from "@/store/slice/web-slice";
import { KioskState } from "@/store/slice/kiosk-slice";
import axios from "axios";
export const WebLoginPage: FC<WebLoginPageProps> = (props) => {
  const webData = useSelector((state: { kiosk: KioskState; web: WebState }) => {
    return state.web;
  });

  const navigate = useNavigate();
  const [Id, setId] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const dispatch = useDispatch();
  const [showPassword, setShowPassword] = React.useState(false);
  const handleClickShowPassword = () => setShowPassword((show) => !show);
  const handleMouseDownPassword = (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.preventDefault();
  };
  const login = () => {
    const loginid = {
      id: Id,
      pw: password,
    };
    webAPI
      .post(`/user/login`, loginid)
      .then((response) => {
        if (response.data.code == 200 || response.data.code == 202) {
          dispatch(setToken(response.data.data.accessToken));
          console.log(webData.Token)
          dispatch(setIsUserIn(true));
          dispatch(
            setLoginUser({
              loginData: {
                id: Id,
                role: response.data.data.role,
              },
            })
          );
          console.log("로그인성공");
          navigate("/home");
        } else {
          alert(response.data.msg)
        }
      })
      .catch((error) => {
        console.log(loginid);
        console.log(error);
        alert("로그인에 실패했습니다.");
      });
  };

    const logOnkeyPress = (e) =>{
      if (e.key ==='Enter') {
        login()
      }
    }
  


  const REDIRECT_URI = "https://i9d111.p.ssafy.io/kakao/";
  const REST_API_KEY = "e9fca19300e2496bcccc630ce29801a3";

  return (
    <div style={{ display: "flex" }}>
      <div className="LoginMain" {...props}>
        <div className="Mainin">
          <div>
            <img
              style={{
                marginRight: "20px",
                maxHeight: "200px",
                maxWidth: "200px",
              }}
              src="/buslogo.png?url"
              alt="fail"
            />
            <h2 className="LoginText">로그인</h2>
          </div>
          <div className="inputBox">
            <div className="loginbox">
              <TextField
                fullWidth
                id="standard-basic"
                label="ID"
                variant="standard"
                onChange={(e) => {
                  setId(e.target.value);
                }}
                onKeyDown={logOnkeyPress}
              />
              <FormControl fullWidth variant="standard">
                <InputLabel htmlFor="standard-adornment-password">
                  Password
                </InputLabel>
                <Input
                  onKeyDown={logOnkeyPress}
                  onChange={(e) => {
                    setPassword(e.target.value);
                  }}
                  id="standard-adornment-password"
                  type={showPassword ? "text" : "password"}
                  endAdornment={
                    <InputAdornment position="end">
                      <IconButton
                        aria-label="toggle password visibility"
                        onClick={handleClickShowPassword}
                        onMouseDown={handleMouseDownPassword}
                      >
                        {showPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  }
                />
              </FormControl>
              <div style={{ display: "flex", flexDirection: "column" }}>
                <Button
                  onClick={login}
                  variant="solid"
                  style={{ marginBottom: "20px", marginTop: "20px" }}
                >
                  로그인
                </Button>
                <a
                  href={`https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`}
                >
                  <img
                    style={{ width: "300px" }}
                    src="/kakaologin.png?url"
                    alt=""
                  />
                </a>
                <Button
                  variant="outlined"
                  onClick={() => {
                    navigate("/signup/");
                  }}
                  style={{ marginBottom: "30px", marginTop: "10px" }}
                >
                  회원가입
                </Button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
