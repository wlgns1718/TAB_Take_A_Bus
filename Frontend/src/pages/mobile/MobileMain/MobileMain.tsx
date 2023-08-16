import { FC, useState, useEffect  } from "react";
import { MobileMainProps } from ".";
import axios from "axios";

import "./MobileMain.css";

export const MobileMain: FC<MobileMainProps> = (props) => {
  
  let [vehicleNo, setVehicleNo] = useState<string>("경북70자5101");
  const [code, setCode] = useState<string>("");
  const [msg, setMsg] = useState<string>(""); // Change 'any' to a more appropriate type if possible
  const [createDate, setCreateDate] = useState<string>("");
  const [vulnerable, setVulnerable] = useState<Boolean>(false);
  const [stationId, setStationId] = useState<string>("");

  const fetchData = () => {
    
    const apiUrl = `https://i9d111.p.ssafy.io/tab/arduino/${vehicleNo}`;

    fetch(apiUrl)
      .then((response) => response.json())
      .then((responseData) => {
        console.log(responseData);
        console.log(responseData.msg);
        console.log(responseData.data.createDate);
        console.log(responseData.data.routeNo);
        console.log(responseData.data.stationId);
        console.log(responseData.data.vulnerable);
        setCode(responseData.code);
        setMsg(responseData.msg);
        setCreateDate(responseData.data.createDate);
        setStationId(responseData.data.stationId);
        setVulnerable(responseData.data.vulnerable);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
      });
  };


  useEffect(() => {
    fetchData(); //

    const interval = setInterval(() => { // 10초마다 feth요청 반복
      fetchData(); 
      
    }, 10000); 

    return () => {
      clearInterval(interval); // interval 초기화 시킴
    };
  }, [vehicleNo]);


  return (
    <div {...props} className="mobile-body">
      
      {
        code === "200"
          ?
          <div className="content">
            
            <div className="content-header">
              <div style={{display:"flex"}}>
                <h1 className="white">차량번호</h1>
                <h1 className="yellow"> : {vehicleNo}</h1>
              </div>
              <div style={{ display: "flex" }}>
                <h1 className="white">이번 정류장</h1>
                <h1 className="yellow">: [{stationId}]</h1>
              </div>
            </div>
            <div className="content-body">
              <h1 className="blinking-text"> 탑승객이 있습니다.</h1>
              <h1 className="yellow blinking-text">{vulnerable === true ? '교통약자O' : '교통약자X'}</h1>
            </div>
          </div>
          :
          <div className="content">
            <div className="content-header">
              <div style={{display:"flex"}}>
                  <h1 className="white">차량번호</h1>
                  <h1 className="yellow"> : {vehicleNo}</h1>
              </div>
              
            </div>
            <div className="content-body">
              <h1> 탑승객이 없습니다.</h1>
            </div>
          </div>
      }
      
    </div>
  );
};
