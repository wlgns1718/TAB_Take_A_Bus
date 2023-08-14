import { FC } from 'react';
import { WebSignupPageProps } from '.';
import './WebSignup.css'
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import Switch, { SwitchProps } from '@mui/material/Switch';
import { useEffect, useState } from "react";
import TextField from '@mui/material/TextField';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import FormHelperText from '@mui/material/FormHelperText';
import FormControl from '@mui/material/FormControl';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import IconButton from '@mui/material/IconButton';
import Input from '@mui/material/Input';
import React from 'react'
import Button from '@mui/joy/Button';
import { setIsUserIn, user } from 'store/slice/web-slice'
import kioskSlice, { checkMaster } from '@/store/slice/kiosk-slice';
import { KioskState } from '@/store/slice/kiosk-slice';
import { useSelector, useDispatch } from "react-redux";
import { webAPI } from '@/store/api/api';
import { setToken } from 'store/slice/web-slice';

export const WebSignupPage: FC<WebSignupPageProps> = (props) => {
  
  const kiosdata: KioskState = useSelector(
    (state: { kiosk: KioskState; web: object }) => {
      return state.kiosk;
    }
  );

  const [userdata,setUser] = useState<user>();

  const dispatch = useDispatch();

  //유효성검사.
  const validateEmail:Function = (email) => {
    return email
      .toLowerCase()
      .match(/^[A-Za-z0-9_]+[A-Za-z0-9]*[@]{1}[A-Za-z0-9]+[A-Za-z0-9]*[.]{1}[A-Za-z]{1,3}$/);
  };

  const validatePwd = (password) => {
    return password
      .toLowerCase()
      .match(/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/);
  }

  const validateId = (id) => {
    return id
      .toLowerCase()
      .match(/^[a-zA-z0-9]{6,15}$/);
  }





const [email, setEmail] = useState<string>("ex) abcde123@samsung.com 와 같은 형식으로 작성해주세요.");
const [password, setPassword] = useState<string>("숫자,영문,특수문자를 포함한 8자리 이상을 필요로합니다.");
const [confirmPwd, setConfirmPwd] = useState<string>("비밀번호가 일치하지 않습니다.");
const [Id, setId] = useState<string>("6자 이상 15자 이하 영문,숫자조합을 필요로합니다.");
const [master, setMaster] = useState<boolean>(false)
const [masterCheck,setMasterCheck] = useState<boolean>(false)
const [name,setName] = useState<string>('') 



const [emailMsg, setEmailMsg] = useState<string>("");
const [pwdMsg, setPwdMsg] = useState<string>('');
const [confirmPwdMsg, setConfirmPwdMsg]= useState<string>("")
const [IdMsg, setIdMsg] = useState<string>("")
const [masterMsg, setMasterMsg] = useState<string>('');


// 1-1에 잡아뒀던 유효성 검사 함수로 정리하기
const isEmailValid:boolean = validateEmail(email);
const isPwdValid:boolean = validatePwd(password);
const isConfirmPwd:boolean = password === confirmPwd;
const isIdValid:boolean = validateId(Id);



const onChangeEmail = (e)=>{
  const currentEmail = e.target.value;
  setEmail(currentEmail);
  if(validateEmail(currentEmail)){
    setEmailMsg('올바른 이메일 형식입니다.')
  }else{
    setEmailMsg('ex) abcde123@samsung.com 와 같은 형식으로 작성해주세요.')
  }
}

const onChangeId = (e)=>{
  const currentId = e.target.value;
  setId(currentId)
  if(validateId(currentId)){
    setIdMsg('올바른 ID형식입니다.')
  }else{
    setIdMsg('6자 이상 15자 이하 영문,숫자조합을 필요로합니다.')
  }
}

const onChangePass = (e)=>{
  const currentPass = e.target.value;
  setPassword(currentPass)
  if(validatePwd(currentPass)){
    setPwdMsg('올바른 비밀번호 형식입니다.')
  }else{
    setPwdMsg('숫자,영문,특수문자를 포함한 8자리 이상을 필요로합니다.')
  }
}

const onChangeName = (e)=>{
  const currentName = e.target.value;
  setName(currentName)
}

const checkConfirm = (e)=>{
  const currentConPass = e.target.value;
  setConfirmPwd(currentConPass)
  if(currentConPass == password){
    setConfirmPwdMsg('비밀번호가 일치합니다.')
  }else{
    setConfirmPwdMsg('비밀번호가 일치하지 않습니다.')
  }
}

const onChangeMaster = (e)=>{
  const currentMaster = e.target.value;
  if(currentMaster==kiosdata.masterkey){
    setMasterCheck(true)
  }else{
    setMasterMsg('마스터키를 입력해주세요')
  }
}

const isAllValid:boolean = isEmailValid && isConfirmPwd && isPwdValid && isIdValid




	return (
    <div {...props}>
      <div style={{display:"flex", flexDirection:"column", alignItems:"center"}}>
			<div className='signupTop'>
        <h2>회원가입</h2>
        <h5 style={{marginTop:"0px"}}>Take A Bus에 오신것을 환영합니다.</h5>
        <h2>필수정보</h2>
        <hr style={{width:'500px'}}/>
      </div>
      <div className='signUpMid'>
        <div className='signUpMidLeft'>
          <p><span style={{color:'red'}}>*</span>이메일</p>
          <p><span style={{color:'red'}}>*</span>성명</p>
          <p><span style={{color:'red'}}>*</span>아이디</p>
          <p><span style={{color:'red'}}>*</span>비밀번호</p>
          <p><span style={{color:'red'}}>*</span>비밀번호 확인</p>
          
          
        </div>
        <div className='signUpMidRight'>
        <TextField style={{height:'55px'}} helperText={`${emailMsg}`} id="E-mail"  variant="standard" onChange={onChangeEmail}/>
        <TextField style={{height:'55px',minWidth:'350px'}} helperText={'성명을입력해주세요.'} id="Name" onChange={onChangeName}  variant="standard" /> 
        <TextField style={{height:'55px'}} helperText={`${IdMsg}`} id="Id"  variant="standard" onChange={onChangeId} />
        <PasswordBox pass={onChangePass} helptext ={`${pwdMsg}`} id={'pass'} />
        <PasswordBox pass={checkConfirm} helptext ={`${confirmPwdMsg}`} id={'passconf'} />
        </div>
      </div>


      <div className='signUpBottom'>
        {!isAllValid ? 
        <Button variant="outlined"  style={{marginBottom:"20px",marginTop:"20px"}} disabled>회원가입</Button> 
        : 
        <Button onClick={()=>{
          {
            if(master){
              if(masterCheck){
                const userSetting = {
                  email : email,
                  id : Id,
                  name: name,
                  pw : password,
                  role: 'MANAGER'
                }
                webAPI
                .post(`/user/join`,userSetting)
                .then((response)=>{
                const loginid = {
                  id : Id,
                  pw: password
                }
               
                      webAPI
                  .post(`/user/login`,loginid)
                  .then((response)=>{
                    dispatch(setToken(response.data.data.accessToken))
                    dispatch(setIsUserIn())
                  })
                  .catch((error)=>{
                    console.log(error)

                  })
                })
                .catch((error)=>{
                  console.log(error)
                })
              }else{
                alert('마스터키가 옳지 않습니다.')
              }
              
            }
          else{
            const userSetting = {
              email : email,
              id : Id,
              name: name,
              password : password,
              role: 'USER'
            }
            webAPI
            .post(`/user/join`,userSetting)
            .then((response)=>{
              console.log(response)
            })
            .catch((error)=>{
              console.log(error)
            })
              
            }
          }
          }
          }
          variant='solid' style={{marginBottom:"20px",marginTop:"20px"}}>회원가입</Button> }
    
        <hr style={{width:'500px'}}/>
          <FormGroup>
            <FormControlLabel
            style={{fontWeight:'bold'}}
            value="top"
            control={<Switch color="primary" />}
            label="관리자"
            labelPlacement="top"
            onChange={()=>{
              setMaster(!master)
            }}
           />
          </FormGroup>
          <div>
            {master == true ? 
          <PasswordBox pass={onChangeMaster} helptext ={`${masterMsg}`} id={'mastercon'}/> : null }</div>
          </div>
          </div>
          
    </div>
  );
};





function PasswordBox(props){
  const [showPassword, setShowPassword] = React.useState(false);
  const handleClickShowPassword = () => setShowPassword((show) => !show);
  const handleMouseDownPassword = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
  };
  return(
          <FormControl style={{height:'55px'}} fullWidth variant="standard">
          <Input
            onChange={props.pass}
            id={props.id}
            type={showPassword ? 'text' : 'password'}
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
  )
  
}


