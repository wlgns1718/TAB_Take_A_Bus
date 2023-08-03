import { FC } from 'react';
import { AuthPageProps } from '.';
import './AuthPage.css' ;
// import TextField from '@mui/material/TextField';
// import Button from '@mui/material/Button';
// import { useState } from 'react';



export const AuthPage: FC<AuthPageProps> = (props) => {
	// const [authKey,setAuthkey] = useState<string>('')
	// const [busStopId,setBusStopId] = useState<string>('')
	
	// return <div className='mainbox'{...props}>AuthPage
	// <h1 style={{fontSize:'200px',justifyContent:'center'}}>정류장 선택</h1>

	// <div>
	// <TextField style={{marginBottom:'100px'}} className='textfiled' id="outlined-basic"  label="관리자 Key" variant="outlined" 
	// onInput={(e)=> {
	// 	let copy = [...authKey]
	// 	copy = e.target.value
	// 	setAuthkey(copy)
	// 	console.log(authKey)}}/>
	// <br />
	// <TextField style={{marginBottom:'50px'}} className='textfiled' id="outlined-basic" label="정류장 ID" variant="outlined" 
	// onInput={(e)=> {
	// 	let copy = [...busStopId]
	// 	copy = e.target.value
	// 	setBusStopId(copy)
	// 	console.log(busStopId)}}/>
	// </div>	
	// <Button onClick={()=>{
		
	// }} 
	// style={{fontSize:'100px'}} className='button' variant="contained">
	// 	확인
	// </Button>
	

	// </div>;
	return <div {...props}></div>
};
