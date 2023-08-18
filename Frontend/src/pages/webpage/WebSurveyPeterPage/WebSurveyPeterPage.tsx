import React, { useEffect, useState } from "react";
import axios from "axios";
import "./WebSurveyPeterPage.css";
import webSlice from "@/store/slice/web-slice";;
import { WebState } from "@/store/slice/web-slice";
import { KioskState } from "@/store/slice/kiosk-slice";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";



interface LatLng {
  lat: number;
  lng: number;
}

function WebSurveyPerterPage() {
  const [start, setStart] = useState<LatLng>({ lat: 0, lng: 0 });
  const [dest, setDest] = useState<LatLng>({ lat: 0, lng: 0 });
  const [reset,setReset] = useState<boolean>(false);

  const navigate = useNavigate()
  const webdata: WebState = useSelector(
    (state: { kiosk: KioskState; web: WebState }) => {
      return state.web;
    }
  );

  let startImageSrc =
    "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/red_b.png";
  let startImageSize = new kakao.maps.Size(50, 45);
  let startImageOption = { offset: new kakao.maps.Point(15, 43) };
  let startMarkerImage = new kakao.maps.MarkerImage(
    startImageSrc,
    startImageSize,
    startImageOption
  );

  let endImageSrc =
    "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/blue_b.png";
  let endImageSize = new kakao.maps.Size(50, 45);
  let endImageOption = { offset: new kakao.maps.Point(15, 43) };
  let endMarkerImage = new kakao.maps.MarkerImage(
    endImageSrc,
    endImageSize,
    endImageOption
  );
    


  useEffect(() => {

    


    const mapContainer = document.getElementById("map");

    const mapOption = {
      center: new kakao.maps.LatLng(35.8714354, 128.583052),
      level: 7,
    };

    const map = new kakao.maps.Map(mapContainer, mapOption);

    let marker1 = new kakao.maps.Marker({
      position: null,
      image: startMarkerImage,
    });

    let marker2 = new kakao.maps.Marker({
      position: null,
      image: endMarkerImage,
    });

    marker1.setMap(map);
    marker2.setMap(map);

    let cnt = 0;
    let marker1Lat: number;
    let marker1Lng: number;
    let marker2Lat: number;
    let marker2Lng: number;
    let polyline: kakao.maps.Polyline;

    const textContainer = document.getElementById("text");

    textContainer.innerHTML = "<h2>출발지를 선택하세요</h2>"

    kakao.maps.event.addListener(map, "click", function (mouseEvent) {
      var latlng = mouseEvent.latLng;
      
      if (cnt %2 == 0  && !polyline) {
        marker1.setPosition(latlng);
        console.log(marker1.getPosition)
        cnt += 1;
        console.log(cnt)
        textContainer.innerHTML = "<h2>목적지를 선택하세요</h2>";
      } else{
        if(cnt !=3){marker2.setPosition(latlng);
          cnt = 2;
          textContainer.innerHTML = "<h2>경로가 설정되었습니다.</h2>";}
      }
      if (cnt == 2) {
        marker1Lat = marker1.getPosition().getLat();
        marker1Lng = marker1.getPosition().getLng();

        marker2Lat = marker2.getPosition().getLat();
        marker2Lng = marker2.getPosition().getLng();

        var linePath = [
          new kakao.maps.LatLng(marker1Lat, marker1Lng),
          new kakao.maps.LatLng(marker2Lat, marker2Lng),
        ];

        polyline = new kakao.maps.Polyline({
          path: linePath,
          strokeWeight: 5,
          strokeColor: "#FFAE00",
          strokeOpacity: 0.7,
          strokeStyle: "solid",
        });
        
        polyline.setMap(map);
        cnt=3
      setStart({ lat: marker1Lat, lng: marker1Lng });
      setDest({ lat: marker2Lat, lng: marker2Lng });
        setTimeout(function() {
          alert('경로설정이 완료되었습니다.변경을 원하시면 아래의 버튼을 눌러주세요');
        }, 400);
      }


      
    });
  }, [reset]);


  return (
    <div className="mainServeyBox">
      <div className="surveyTop">
        <h1 style={{marginBottom:"20px"}}>수요조사</h1>
        <p style={{width:'500px'}}>교통카드 태깅으로는 완벽하게 알 수 없는 버스 이용객들의 수요를 조사하기 위해 탑승, 하차 희망 지역을 조사하고있습니다. <br />
        버스 이용객 님들의 편익 향상을 위해 많은 참여 부탁드립니다.
        본 수요조사 서비스는 한달에 한번 이용할 수 있습니다.
        </p>
        <hr style={{width:'700px'}}/>
        <br />
      </div>
    <div
      id="kakao"
      style={{ display: "flex", flexDirection: "column", alignItems: "center" }}
    >
      <div id="text">
 
        <h2>출발지를 선택하세요</h2>
      </div>

      <div id="map" style={{ width: "80%", height: "65vh" }}></div>

      {/* <div>
        출발지 위도 : {start.lat}, 경도 : {start.lng} <br />
        목적지 위도 : {dest.lat}, 경도 : {dest.lng}
      </div> */}
    <div>
    <button
        className="peterSurveyBtn"
        onClick={() => {
          if (!webdata.isUserIn){
            alert("로그인이 필요한 기능입니다.")
            navigate("../login")
            return
          }
            axios
              .post(
                "https://i9d111.p.ssafy.io/tab/survey",
                {
                  destinationLatitude: dest.lat,
                  destinationLongtitude: dest.lng,
                  startLatitude: start.lat,
                  startLongtitude: start.lng,
                },
                {
                  headers: {
                    Authorization: `Bearer ${webdata.Token}`,
                  },
                }
              )
              .then((res) => {
                alert("설문조사 성공!");
                console.log(res);
              })
              .catch((err) => {
                alert("설문조사 실패ㅜㅜ 다시 시도해 주세요");
                console.log(err);
              });
        }}
      >
        설문제출
      </button>

      <button 
      className="resetBtn"
      onClick={()=>{
        setReset(true)
        setStart({ lat: 0, lng: 0 });
        setDest({ lat: 0, lng: 0 });
        alert('다시 경로를 설정해 주세요.')
        setTimeout(() => {
          setReset(false)
          console.log(reset)
        }, 100);
       
        }} >재설정하기 </button>
    </div>
     
    </div>
    </div>
  );
}

export default WebSurveyPerterPage;

