import React from "react"
import { useDispatch } from "react-redux"
import { useEffect } from "react"
import { useNavigate } from "react-router-dom";
import axios from "axios";
function Kakaopage () {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    useEffect(()=>{
        const getkakao = new URL(window.location.href).searchParams.get('code')
        axios.post('http://i9d111.p.ssafy.io:8000/tab/user/login/kakao',getkakao)
        .then((response)=>{
            console.log(response)
        }).catch((error)=>{
            console.log(error)
        })
    })
    return(
        <div>
            <h1> 로그인에 성공하였습니다.</h1>
            <button onClick={()=>{
                navigate('/web/')
            }}>홈화면으로</button>
        </div>
        

    )
    
}








export default Kakaopage