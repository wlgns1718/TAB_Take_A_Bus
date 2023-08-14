import { FC } from "react";
import { WebSignupPageProps } from ".";
import "./WebSignup.css";
import FormGroup from "@mui/material/FormGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import Switch, { SwitchProps } from "@mui/material/Switch";
import { useEffect, useState } from "react";
import TextField from "@mui/material/TextField";
import InputLabel from "@mui/material/InputLabel";
import InputAdornment from "@mui/material/InputAdornment";
import FormHelperText from "@mui/material/FormHelperText";
import FormControl from "@mui/material/FormControl";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import IconButton from "@mui/material/IconButton";
import Input from "@mui/material/Input";
import React from "react";
import Button from "@mui/joy/Button";
import { setIsUserIn, setLoginUser } from "store/slice/web-slice";
import kioskSlice, { checkMaster } from "@/store/slice/kiosk-slice";
import { KioskState } from "@/store/slice/kiosk-slice";
import { useSelector, useDispatch } from "react-redux";
import { webAPI } from "@/store/api/api";
import { setToken } from "store/slice/web-slice";
import { useNavigate } from "react-router-dom";
import axios from "axios";



export const WebSignupPage: FC<WebSignupPageProps> = (props) => {
  const kiosdata: KioskState = useSelector(
    (state: { kiosk: KioskState; web: object }) => {
      return state.kiosk;
    }
  );
  const navigate = useNavigate();
  const dispatch = useDispatch();

  //유효성검사.
  const validateEmail: Function = (email) => {
    return email
      .toLowerCase()
      .match(
        /^[A-Za-z0-9_]+[A-Za-z0-9]*[@]{1}[A-Za-z0-9]+[A-Za-z0-9]*[.]{1}[A-Za-z]{1,3}$/
      );
  };

  const validatePwd = (password) => {
    return password
      .toLowerCase()
      .match(/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/);
  };

  const validateId = (id) => {
    return id.toLowerCase().match(/^[a-zA-z0-9]{6,15}$/);
  };

  const [email, setEmail] = useState<string>(
    "ex) abcde123@samsung.com 와 같은 형식으로 작성해주세요."
  );
  const [password, setPassword] = useState<string>(
    "숫자,영문,특수문자를 포함한 8자리 이상을 필요로합니다."
  );
  const [confirmPwd, setConfirmPwd] =
    useState<string>("비밀번호가 일치하지 않습니다.");
  const [Id, setId] = useState<string>(
    "6자 이상 15자 이하 영문,숫자조합을 필요로합니다."
  );
  const [master, setMaster] = useState<boolean>(false);
  const [masterCheck, setMasterCheck] = useState<boolean>(false);
  const [name, setName] = useState<string>("");
  const [emailCode,setEmailCode] = useState<string>('jhqwjkf  h2urh23dunirhc2ip3urhjkwebfjwehbfjkhWEBFYI2EG ROIUN3YRIHG23RHJKQWEBFJLHQWEGFLUYE  2GOIY2EGFILQUFHKQEJFGqeiy');
  const [emailCodeChecker,setEmailCodeChecker] = useState<string>('')
  const [showModal,setShowModal] = useState<boolean>(false);
  const [emailMsg, setEmailMsg] = useState<string>("");
  const [pwdMsg, setPwdMsg] = useState<string>("");
  const [confirmPwdMsg, setConfirmPwdMsg] = useState<string>("");
  const [IdMsg, setIdMsg] = useState<string>("");
  const [masterMsg, setMasterMsg] = useState<string>("");
  const [emailCheckMsg,setEmailCheckMsg] = useState<string>("인증번호를 입력해주세요.");
  
  // 1-1에 잡아뒀던 유효성 검사 함수로 정리하기
  const isEmailValid: boolean = validateEmail(email);
  const isPwdValid: boolean = validatePwd(password);
  const isConfirmPwd: boolean = password === confirmPwd;
  const isIdValid: boolean = validateId(Id);
  const [emailcheck,setEmailcheck] = useState<boolean>(false);
  const [isSameId,setIsSameId] = useState<boolean | null>(null)

  const onChangeEmail = (e) => {
    const currentEmail = e.target.value;
    setEmail(currentEmail);
    if (validateEmail(currentEmail)) {
      setEmailMsg("올바른 이메일 형식입니다.");
    } else {
      setEmailMsg("ex) abcde123@samsung.com 와 같은 형식으로 작성해주세요.");
    }
  };

  const onChangeId = (e) => {
    const currentId = e.target.value;
    setId(currentId);
    if (validateId(currentId)) {
      setIdMsg("올바른 ID형식입니다.");
    } else {
      setIdMsg("6자 이상 15자 이하 영문,숫자조합을 필요로합니다.");
    }
  };

  const onChangePass = (e) => {
    const currentPass = e.target.value;
    setPassword(currentPass);
    if (validatePwd(currentPass)) {
      setPwdMsg("올바른 비밀번호 형식입니다.");
    } else {
      setPwdMsg("숫자,영문,특수문자를 포함한 8자리 이상을 필요로합니다.");
    }
  };

  const onChangeName = (e) => {
    const currentName = e.target.value;
    setName(currentName);
  };

  const checkConfirm = (e) => {
    const currentConPass = e.target.value;
    setConfirmPwd(currentConPass);
    if (currentConPass == password) {
      setConfirmPwdMsg("비밀번호가 일치합니다.");
    } else {
      setConfirmPwdMsg("비밀번호가 일치하지 않습니다.");
    }
  };

  const onChangeMaster = (e) => {
    const currentMaster = e.target.value;
    if (currentMaster == kiosdata.masterkey) {
      setMasterCheck(true);
    } else {
      setMasterMsg("마스터키를 입력해주세요");
    }
  };


  const onEmailKey = (e) =>{
    setEmailCodeChecker(e.target.value)
  }

  const onChangeEmailCheck = () => {
    if (emailCodeChecker == emailCode) {
      setEmailcheck(true)
      alert('인증되었습니다.')
      setEmailCheckMsg("인증되었습니다.");
      console.log(emailCodeChecker)
      console.log(emailCode)
      
    } else {
      setEmailCheckMsg("인증번호가 일치하지 않습니다.");
    }
  };

  const sendEmailCheck = () =>{
    const maildata = {
      email : email,
      type : "register",
      userId: "string"
    }
    webAPI.post('/user/mail',maildata)
    .then((response)=>{
      setEmailCode(response.data.data)
      console.log(response)
    })
    .catch((error)=>{
      console.log(error)
    })
    setShowModal(true)
    alert('작성하신 email로 인증번호가 발급되었습니다.')
  }
  const isAllValid: boolean =
  isEmailValid && isConfirmPwd && isPwdValid && isIdValid&&emailcheck &&isSameId;
  /////style 
  

 

  return (
    <div {...props}>
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <div className="signupTop">
          <h2>회원가입</h2>
          <h5 style={{ marginTop: "0px" }}>
            Take A Bus에 오신것을 환영합니다.
          </h5>
          <h2>필수정보</h2>
          <hr style={{ width: "600px" }} />
        </div>
        <div className="signUpMid">
          <div className="signUpMidLeft">
            <p>
              <span style={{ color: "red" }}>*</span>이메일
              {showModal ? <div style={{minHeight:"50px"}}></div> : null}
            </p>
            <p>
              <span style={{ color: "red" }}>*</span>성명
            </p>
            <p>
              <span style={{ color: "red" }}>*</span>아이디
            </p>
            <p>
              <span style={{ color: "red" }}>*</span>비밀번호
            </p>
            <p>
              <span style={{ color: "red" }}>*</span>비밀번호 확인
            </p>
          </div>
          <div className="signUpMidRight">
            <TextField
              style={{ height: "55px" }}
              helperText={`${emailMsg}`}
              id="E-mail"
              variant="standard"
              onChange={onChangeEmail}
            />
            {showModal ? <CheckEmailBox check={emailcheck} text={emailCheckMsg} change={onEmailKey}  emailConfirm={onChangeEmailCheck} /> : null}
            <TextField
              style={{ height: "55px", minWidth: "350px" }}
              helperText={"성명을입력해주세요."}
              id="Name"
              onChange={onChangeName}
              variant="standard"
            />
            <TextField
              {...isSameId==false ? {error:true} : {error:false} }
              style={{ height: "55px" }}
              helperText={`${IdMsg}`}
              id="Id"
              variant="standard"
              onChange={onChangeId}
            />
            

            <PasswordBox
              pass={onChangePass}
              helptext={`${pwdMsg}`}
              id={"pass"}
            />
            <PasswordBox
              pass={checkConfirm}
              helptext={`${confirmPwdMsg}`}
              id={"passconf"}
            />
          </div>
          <div style={{display:'flex',flexDirection:'column'}}>
            <button onClick={isEmailValid ? sendEmailCheck : ()=>{alert('올바른 이메일을 입력해주세요.')}} style={{marginLeft:'20px'}}>이메일 인증</button>
            <div style={showModal ? {minHeight:"145px"} : {minHeight:"80px"} }></div>
            <button onClick={()=>{
              webAPI.get(`user/checkId/${Id}`)
              .then((response)=>{
                console.log(response)
                if(response.data.code == 200){
                  setIdMsg("사용가능한 아이디입니다.")
                  setIsSameId(true)
                }else if(response.data.code == 401){
                  setIdMsg("중복된 아이디입니다.")
                 setIsSameId(false)
                }
              })
            }} style={{marginLeft:'20px'}}>ID 중복확인</button>
          </div>
        </div>

        <div className="signUpBottom">
          {!isAllValid ? (
            <Button
              variant="outlined"
              style={{ marginBottom: "20px", marginTop: "20px" }}
              disabled
            >
              회원가입
            </Button>
          ) : (
            <Button
              onClick={() => {
                {
                  let role = "";
                  if (master) {
                    role = "MANAGER";
                    if (!masterCheck) {
                      alert("마스터키가 옳지 않습니다.");
                      return;
                    }
                  } else {
                    role = "USER";
                  }
                  const userSetting = {
                    email: email,
                    id: Id,
                    name: name,
                    pw: password,
                    role: role,
                  };
                  webAPI
                    .post(`/user/join`, userSetting)
                    .then((response) => {
                      if (
                        !(
                          response.data.code == 200 || response.data.code == 202
                        )
                      ) {
                        alert(response.data.data);
                        return;
                      }
                      const loginid = {
                        id: Id,
                        pw: password,
                      };
                      webAPI
                        .post(`/user/login`, loginid)
                        .then((response) => {
                          if (
                            response.data.code == 200 ||
                            response.data.code == 202
                          ) {
                            dispatch(setToken(response.data.data.accessToken));
                            dispatch(setIsUserIn(true));
                            dispatch(
                              setLoginUser({
                                loginData: {
                                  id: Id,
                                  role: response.data.data.role,
                                },
                              })
                            );
                            alert("성공적으로 회원가입 되셨습니다");
                            navigate("/web/home");
                          }
                        })
                        .catch((error) => {
                          console.log(error);
                        });
                    })
                    .catch((error) => {
                      console.log(error);
                    });
                }
              }}
              variant="solid"
              style={{ marginBottom: "20px", marginTop: "20px" }}
            >
              회원가입
            </Button>
          )}

          <hr style={{ width: "500px" }} />
          <FormGroup>
            <FormControlLabel
              style={{ fontWeight: "bold" }}
              value="top"
              control={<Switch color="primary" />}
              label="관리자"
              labelPlacement="top"
              onChange={() => {
                setMaster(!master);
              }}
            />
          </FormGroup>
          <div>
            {master == true ? (
              <PasswordBox
                pass={onChangeMaster}
                helptext={`${masterMsg}`}
                id={"mastercon"}
              />
            ) : null}
          </div>
        </div>
      </div>
    </div>
  );
};

function PasswordBox(props) {
  const [showPassword, setShowPassword] = React.useState(false);
  const handleClickShowPassword = () => setShowPassword((show) => !show);
  const handleMouseDownPassword = (
    event: React.MouseEvent<HTMLButtonElement>
  ) => {
    event.preventDefault();
  };
  return (
    <FormControl style={{ height: "55px" }} fullWidth variant="standard">
      <Input
        onChange={props.pass}
        id={props.id}
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
      <FormHelperText id="component-helper-text">
        {props.helptext}
      </FormHelperText>
    </FormControl>
  );
}


function CheckEmailBox(props) {
  return(
    <div>
  <TextField
    {...props.check==false ? {error:true} : {error:false} }
    style={{height: "55px" }}
    helperText={`${props.text}`}
    id="Id"
    variant="standard"
    onChange={props.change}
  />
  <button onClick={props.emailConfirm}>인증하기</button>
  </div>
  )
}