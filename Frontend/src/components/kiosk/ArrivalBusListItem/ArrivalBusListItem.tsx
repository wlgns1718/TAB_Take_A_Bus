import { FC, useState } from "react";
import { ArrivalBusListItemProps } from ".";
import "./ArrivalBusListItem.css";
import { Grid } from "@mui/material";

export const ArrivalBusListItem: FC<ArrivalBusListItemProps> = ({ item }) => {
  const [isStopHere, setIsStopHere] = useState(false);

  const busIconURL = `/bus_side_icon.png?url`;
  return (

    <div className="bus-item-container">
      <div className="bus-item-top">
        <div>

          
          <Grid container direction ="column" justifyContent={"center"} alignItems={"center"} className="bus-detail bg-white round-10" style={{width:'380px'}} >

            <Grid container direction="row" justifyContent={"space-around"} alignItems={"center"} >
              <Grid display={"flex"} item xs={6} justifyContent={"center"}> 
                <img src={`/${item.routeType}.png?url`} alt = {item.routeType} />
              </Grid>
              <Grid display={"flex"} item xs={6} justifyContent={"start"}>
                  {item.busNo.length >= 3? 
                    <div className="marquee">
                        <div className="outer">
                            <div className="inner">
                                <span className="content">{item.busNo}</span>
                            </div>
                        </div>
                    </div>
                    :
                    <div style={{ whiteSpace: 'nowrap', overflow: 'hidden' }}>
                        <h1 style={{ fontSize: '75px', marginLeft: '15px', padding: '0' }}>{item.busNo}</h1>
                    </div>
                }
              </Grid>
            </Grid>
            <Grid container direction="row" justifyContent={"space-between"} alignItems={"baes-line"}>
              <Grid xs={6} item style={{fontSize:'35px' , fontWeight:"bolder" , marginLeft:"25px"}}>
                {item.routeType}
                
              </Grid>
              <Grid xs={3}>
                {item.vehicleType === '저상버스' && <img style={{zIndex:3,position:"absolute", width:'110px', height:'80px'}} src={`/wheelchair.png?url`}/>}
              </Grid>
            </Grid>
          </Grid>
        </div>
        <div className="bus-eta bg-white round-10">
          <div className="bus-eta-title text-center ">도착예정시간</div>
          <div className="bus-eta-eta text-center">ETA</div>
          <div className="bus-eta-text ">
            <div className="bus-eta-num ">{Math.round(item?.eta / 60)}</div>
            <div className="bus-eta-min ">
              <div>분</div>
              <div className="bus-eta-min-eng"> min</div>
            </div>
          </div>
        </div>
        <div className="bus-routeinfo bg-white round-10">
          <div className="bus-map-line"> </div>
          {[
            item.remainingStops >= 4 ? item.remainingStops : 4,
            3,
            2,
            1,
            "현",
          ].map((station) => (
            <div
              key={station}
              className={`station-circle ${
                station === item.remainingStops ? "current-station" : ""
              }`}
            >
              {station}
            </div>
          ))}
          <img
            className="bus-icon"
            src={busIconURL}
            style={{
              left: `${
                ((item.remainingStops >= 4 ? 0 : 4 - item.remainingStops) / 5) *
                  720 +
                85
              }px`,
            }}
          ></img>
          <div
            className="current-station-name"
            style={{
              left: `${
                ((item.remainingStops >= 4 ? 0 : 4 - item.remainingStops) / 5) *
                  720 +
                85
              }px`,
            }}
          >
            {item.stationName}
          </div>
        </div>
        <div
          className={`tap-button ${isStopHere ? "tap-on" : "tap-off"}`}
          onClick={() => setIsStopHere(true)}
        >
          {isStopHere ? "정차 예정" : "탑승"}
        </div>
      </div>
      <div className="guide-message">
        {isStopHere ? (
          <span>이 버스는 현 정류장에 정차할 예정입니다.</span>
        ) : (
          <span>이 버스에 탑승하시려면 탑승 버튼을 눌러주세요.</span>
        )}
      </div>
    </div>
  );
};
