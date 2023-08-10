import { FC } from 'react';
import { WebSignupPageProps } from '.';
import './WebSignup.css'
import { styled } from '@mui/material/styles';
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import Switch, { SwitchProps } from '@mui/material/Switch';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import { useEffect, useState } from "react";
import TextField from '@mui/material/TextField';


export const WebSignupPage: FC<WebSignupPageProps> = (props) => {
  interface userInfo {
    email :string;
    password :string;
    confirmPwd : string;
    nickname: string;
  }

  const [user,setUser] = useState<userInfo | null>();


  //유효성검사.
  const validateEmail = (email) => {
    return email
      .toLowerCase()
      .match(/([\w-.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/);
  };

  const validatePwd = (password) => {
    return password
      .toLowerCase()
      .match(/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{10,25}$/);
  }

  const validateNickname = (nickname) => {
    return nickname
      .toLowerCase()
      .match(/^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|].{1,8}$/)
  }




const [email, setEmail] = useState<string>("");
const [password, setPassword] = useState<string>("");
const [confirmPwd, setConfirmPwd] = useState<string>("");
const [nickname, setNickname] = useState<string>("");
const [master, setMaster] = useState<boolean>(false)

const [emailMsg, setEmailMsg] = useState<string>("");
const [pwdMsg, setPwdMsg] = useState<string>('');
const [confirmPwdMsg, setConfirmPwdMsg]= useState<string>("")
const [nicknameMsg, setNicknameMsg] = useState<string>("")


// 1-1에 잡아뒀던 유효성 검사 함수로 정리하기
const isEmailValid = validateEmail(email);
const isPwdValid = validatePwd(password);
const isConfirmPwd = password === confirmPwd;
const isNicknameValid = validateNickname(nickname);


	return (
    <div {...props}>
			<div className='signupTop'>
        <h2>회원가입</h2>
        <h5 style={{marginTop:"0px"}}>Take A Bus에 오신것을 환영합니다.</h5>
        <h2>필수정보</h2>
        <hr style={{width:'500px'}}/>
      </div>
      <div className='signUpMid'>
        <div className='signUpMidLeft'>
          <p><span style={{color:'red'}}>*</span>이메일</p>
          <p><span style={{color:'red'}}>*</span>아이디</p>
          <p><span style={{color:'red'}}>*</span>성명</p>
          <p><span style={{color:'red'}}>*</span>비밀번호</p>
          
          
        </div>
        <div className='signUpMidRight'>
        <TextField  id="E-mail"  variant="standard" onChange={(e)=>{
          const currEmail:string = e.target.value
          setEmail(currEmail)

        }}/>

        <TextField  id="Id"  variant="standard" />
        <TextField  id="Name"  variant="standard" />
        <TextField  id="Password"  variant="standard" />
        </div>
      </div>
      
      <div className='signUpBottom'>
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
          <TextField  id="masterkey"  variant="standard" label="MasterKey를 입력하세요" /> : null }</div>
          </div>
    </div>
  );
};




// "email": "string",
// "id": "string",
// "name": "string",
// "pw": "string",
// "role": "USER"