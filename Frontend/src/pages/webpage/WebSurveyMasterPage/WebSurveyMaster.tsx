import { useEffect } from "react";
import axios from "axios";
import { WebState } from "@/store/slice/web-slice";
import { KioskState } from "@/store/slice/kiosk-slice";
import { useSelector } from "react-redux";
import './SurveyMaster.css'
import { WebSurveyMasterProps } from ".";

let { google }:any = window; // window 전역 객체에 등록된 google를 함수형 컴포넌트에서 인식 시키기 위함

/*
수요조사 결과를 보여주는 Result 컴포넌트 (구글맵)
*/
function WebSurveyMaster() {
  
  const webdata: WebState = useSelector(
    (state: { kiosk: KioskState; web: WebState }) => {
      return state.web;
    }
  );

  useEffect(() => {
    async function initMap() {
      // 구글맵 api가 callBack함수 initMap을 필요해서 생성해줌

      let arr = [
        // 더미데이터 (구미 위도, 경도여서 구글맵 이동시키면 보여요)
       
      ];

      /*axios // 백엔드 완성되면 이걸로 받을 예정 ( accessToken 넘겨줘야 함 )
        */
       await axios
       .get("https://i9d111.p.ssafy.io/tab/survey/all", {
        headers: {
          Authorization:
            `Bearer ${webdata.Token}`,
        },
      })
      .then((res) => {
        res.data.data.map((mapindex)=>{
          arr.push(
            [mapindex.longtitude,mapindex.latitude]
            )
        })
        console.log(arr)
      })
      .catch((err) => {
        console.log(err);
      });

      let heatMapData:any = []; // 받은 좌표를 저장할 heatMap

      for (const coords of arr) {
        // 서버로 부터 받은 좌표들을 heatMapData 배열에 저장
        const lat:any = coords[1];
        const lng:any = coords[0];
        const latLng:any = new google.maps.LatLng(lat, lng);
        heatMapData.push(latLng);
      }

      // 구글맵 만드는데 필요한 옵션
      const mapOptions:any = {
        center: new google.maps.LatLng(35.8714354, 128.583052), // 중앙의 위도, 경도
        zoom: 13,
        mapTypeId: "roadmap",
      };

      // 구글맵 생성
      const map:any = new google.maps.Map(
        document.getElementById("resultMap"),
        mapOptions
      );

      // 히트맵 생성
      const heatmap:any = new google.maps.visualization.HeatmapLayer({
        data: heatMapData,
      });

      // 히트맵 구글맵에 set
      heatmap.setMap(map);
    }

    // 구글맵이 callBack 함수로 initMap이 필요해서 등록해줌
    google.initMap = initMap();
  }, []);
  return (
    <div id="result">
      <div className="surveyResult">
        <h1 style={{marginBottom:"20px"}}>설문결과</h1>
        <hr style={{width:"300px", marginBottom:"30px"}} />
        <p>대구광역시 버스 이용객을 대상으로한 수요조사 결과가 아래에 표시됩니다.
        </p>
       <br />
      </div>
      
      <div id="resultMap" style={{ width: "100%", height: "700px" ,marginBottom:"100px"}}></div>
    </div>
  );
}

export default WebSurveyMaster;
