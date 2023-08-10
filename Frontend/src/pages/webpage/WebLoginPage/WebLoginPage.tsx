import { FC } from "react";
import { WebLoginPageProps } from ".";
import Input from "@mui/joy/Input";
import Button from "@mui/joy/Button";
import "./WebLogin.css";
import TextField from "@mui/material/TextField";
import { useNavigate } from "react-router-dom";

export const WebLoginPage: FC<WebLoginPageProps> = (props) => {
  const navigate = useNavigate();

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
              />
              <TextField
                id="standard-basic"
                label="Password"
                variant="standard"
              />
              <Button
                variant="soft"
                style={{ marginBottom: "20px", marginTop: "20px" }}
              >
                로그인
              </Button>
              <Button
                variant="soft"
                onClick={() => {
                  navigate("/web/signup");
                }}
                style={{ marginBottom: "20px" }}
              >
                회원가입
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
