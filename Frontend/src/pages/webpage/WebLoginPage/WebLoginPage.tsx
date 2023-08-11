import { FC, useState } from 'react';
import { WebLoginPageProps } from '.';
import Button from '@mui/joy/Button';
import './WebLogin.css'
import { useNavigate } from 'react-router-dom';
import React from 'react'
import IconButton from '@mui/material/IconButton';
import Input from '@mui/material/Input';
import FilledInput from '@mui/material/FilledInput';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import FormHelperText from '@mui/material/FormHelperText';
import FormControl from '@mui/material/FormControl';
import TextField from '@mui/material/TextField';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';


export const WebLoginPage: FC<WebLoginPageProps> = (props) => {
  const navigate = useNavigate(); 
  const [id,setId] = useState<string>('')
  const [password,setPassword] = useState<string>('')


  const [showPassword, setShowPassword] = React.useState(false);
  const handleClickShowPassword = () => setShowPassword((show) => !show);
  const handleMouseDownPassword = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
  };

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
     
      <div className='loginbox'>
      <TextField fullWidth id="standard-basic" label="ID" variant="standard" />
      <FormControl fullWidth  variant="standard">
          <InputLabel htmlFor="standard-adornment-password">Password</InputLabel>
          <Input
          
            id="standard-adornment-password"
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
        </FormControl>
        <Button variant='solid' style={{marginBottom:"20px",marginTop:"20px"}}>로그인</Button>
        <Button variant='outlined' onClick={()=>{navigate('/web/signup')}} style={{marginBottom:"20px"}}>회원가입</Button>

        
      </div>
      
    </div>
    
    </div>
  );
};
