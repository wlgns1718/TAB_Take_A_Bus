import { FC, useEffect, useState } from "react";
import { WebSurveyPageProps } from ".";
import {
  Map,
  MapMarker,
  CustomOverlayMap,
  Roadview,
  Polyline,
} from "react-kakao-maps-sdk";
import { Button } from "@mui/joy";

export const WebSurveyPage: FC<WebSurveyPageProps> = (props) => {
  const startImage = {
    src: "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/red_b.png",
    size: [50, 45],
    options: {
      offset: [200, 200],
    },
  };

  const startDragImage = {
    src: "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/red_drag.png",
    size: [50, 64],
    options: {
      offset: [15, 54],
    },
  };

  const endImage = {
    src: "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/blue_b.png",
    size: [50, 45],
    options: {
      offset: [100,100],
    },
  };

  const endDragImage = {
    src: "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/blue_drag.png",
    size: [50, 64],
    options: {
      offset: [15, 54],
    },
  };

  const [startPoint, setStartPoint] = useState<{ lat: number; lng: number }>();
  const [endPoint, setEndPoint] = useState<{ lat: number; lng: number }>();

  const [focus, setFocus] = useState("nothing");

  const [position, setPosition] = useState<{ lat: number; lng: number }>();

  const [startImg, setStartImg] = useState(startImage);
  const [endImg, setEndImg] = useState(endImage);

  const handleMapClick = (_t, mouseEvent) => {
    if (focus == "nothing") return;
    else if (focus == "start") {
      setStartPoint({
        lat: mouseEvent.latLng.getLat().toFixed(10),
        lng: mouseEvent.latLng.getLng().toFixed(10),
      });
    } else if (focus == "end") {
      setEndPoint({
        lat: mouseEvent.latLng.getLat().toFixed(10),
        lng: mouseEvent.latLng.getLng().toFixed(10),
      });
    }
    setPosition({
      lat: mouseEvent.latLng.getLat().toFixed(10),
      lng: mouseEvent.latLng.getLng().toFixed(10),
    });
  };

  const handleCalculateDistance = () => {
    if (!startPoint || !endPoint) {
      alert("출발지와 도착지를 모두 클릭하세요.");
      return;
    }

    // TODO: Use Kakao Maps API to calculate shortest distance and display it.

    // Example of using Kakao Maps API to calculate distance:
    // const distance = kakao.maps.services.Distance.calculate({
    //   start: new kakao.maps.LatLng(start.getLat(), start.getLng()),
    //   end: new kakao.maps.LatLng(end.getLat(), end.getLng()),
    // });

    // Display the calculated distance using the 'distance' object.

    // Remember to handle API response and errors appropriately.

    // For now, just log the markers for testing.
    console.log("Start:", startPoint);
    console.log("End:", endPoint);
  };

  return (
    <div {...props}>
      <div>수요조사</div>
      <div>
        <Button onClick={() => setFocus("start")}>출발지 설정</Button>
        <label htmlFor="start">출발지 : </label>
        <input
          type="text"
          id="startpoint"
          value={startPoint?.lat}
          placeholder="위도"
        />
        <input
          type="text"
          id="startpoint"
          value={startPoint?.lng}
          placeholder="경도"
        />
        <br />
        <Button onClick={() => setFocus("end")}>도착지 설정</Button>
        <label htmlFor="end">도착지 : </label>
        <input
          type="text"
          id="endpoint"
          value={endPoint?.lat}
          placeholder="위도"
        />
        <input
          type="text"
          id="endpoint"
          value={endPoint?.lng}
          placeholder="경도"
        />
      </div>
      <Map
        center={{ lat: 33.5563, lng: 126.79581 }}
        style={{ width: "100%", height: "360px" }}
        onClick={(_t, MouseEvent) => handleMapClick(_t, MouseEvent)}
      >
        {startPoint && (
          <MapMarker
            image={startImg}
            position={startPoint}
            draggable={true}
            onDragStart={() => {
              setStartImg(startDragImage);
              // handleMapClick(_t, MouseEvent);
            }} // 마커가 이동하면 이미지를 변경합니다
            onDragEnd={(_t, MouseEvent) => {
              setStartImg(startImage);
              setFocus('start');
              // handleMapClick(_t, MouseEvent);
            }} // 마커가 이동하면 이미지를 변경합니다
          />
        )}
        {endPoint && (
          <MapMarker
            image={endImg}
            position={endPoint}
            draggable={true}
            onDragStart={() => {
              setEndImg(endDragImage);
              // handleMapClick(_t, MouseEvent);
            }} // 마커가 이동하면 이미지를 변경합니다
            onDragEnd={(_t, MouseEvent) => {
              setEndImg(endImage);
              setFocus("end");
              // handleMapClick(_t, MouseEvent);
            }} // 마커가 이동하면 이미지를 변경합니다
          />
        )}
        {/* path: [
        new kakao.maps.LatLng(36.12963461, 128.3293215),
        new kakao.maps.LatLng(36.12802335, 128.3331997),
        new kakao.maps.LatLng(36.12688792, 128.3358796),
        new kakao.maps.LatLng(36.12470868, 128.3361004),
        new kakao.maps.LatLng(36.12327282, 128.3378493),
        new kakao.maps.LatLng(36.11941619, 128.338457),
        new kakao.maps.LatLng(36.11679033, 128.3387627),
        new kakao.maps.LatLng(36.11660504, 128.3360182),
        new kakao.maps.LatLng(36.1144838, 128.3322819),
        new kakao.maps.LatLng(36.11171976, 128.332306),
        new kakao.maps.LatLng(36.10891024, 128.332274),
        new kakao.maps.LatLng(36.1074208, 128.3336803),
        new kakao.maps.LatLng(36.10704137, 128.3375395),
        new kakao.maps.LatLng(36.10978231, 128.3393659),
        new kakao.maps.LatLng(36.11280649, 128.3395159),
        new kakao.maps.LatLng(36.11480976, 128.3403246),
        new kakao.maps.LatLng(36.11616004, 128.3440683),
        new kakao.maps.LatLng(36.11535491, 128.34845005),
        new kakao.maps.LatLng(36.11393993, 128.3525919),
        new kakao.maps.LatLng(36.10905501, 128.3567494),
        new kakao.maps.LatLng(36.10545175, 128.3572006),
        new kakao.maps.LatLng(36.10141015, 128.3572649),
        new kakao.maps.LatLng(36.0973352, 128.3587687),
        new kakao.maps.LatLng(36.09374477, 128.3591129),
        new kakao.maps.LatLng(36.09113227, 128.3586011),
        new kakao.maps.LatLng(36.08945612, 128.3544822),
        new kakao.maps.LatLng(36.08869567, 128.3513195),
        new kakao.maps.LatLng(36.08931286, 128.3546454),
        new kakao.maps.LatLng(36.08909176, 128.3550872),
        new kakao.maps.LatLng(36.08695127, 128.3553143),
        new kakao.maps.LatLng(36.08377758, 128.3568039),
        new kakao.maps.LatLng(36.07928507, 128.3535503),
        new kakao.maps.LatLng(36.0778876, 128.3520826),
        new kakao.maps.LatLng(36.07180632, 128.3493242),
        new kakao.maps.LatLng(36.07049884, 128.3478516),
        new kakao.maps.LatLng(36.07111404, 128.3438218),
        new kakao.maps.LatLng(36.07234759, 128.3421197),
        new kakao.maps.LatLng(36.07299228, 128.3400455),
        new kakao.maps.LatLng(36.07094044, 128.337783),
        new kakao.maps.LatLng(36.06829375, 128.3348468),
        new kakao.maps.LatLng(36.06623805, 128.3325861),
        new kakao.maps.LatLng(36.06707248, 128.3309464),
        new kakao.maps.LatLng(36.06846141, 128.3315965),
      ],
      strokeWeight: 5,
      strokeColor: "#F10000",
      strokeOpacity: 0.5,
      strokeStyle: "solid", */}

        {startPoint?.lat && endPoint?.lat ? (
          <Polyline
            path={[
              [
                { lat: startPoint?.lat, lng: startPoint?.lng },
                { lat: endPoint?.lat, lng: endPoint?.lng },
              ],
            ]}
            strokeWeight={5} // 선의 두께 입니다
            strokeColor={"#FFAE00"} // 선의 색깔입니다
            strokeOpacity={0.7} // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
            strokeStyle={"solid"} // 선의 스타일입니다
          />
        ) : (
          ""
        )}

        {/* {startPoint && endPoint ? (
          <CustomOverlayMap position={{ lat: 33.55635, lng: 126.795841 }}>
            <div
              style={{
                padding: "42px",
                backgroundColor: "#fff",
                color: "#000",
              }}
            >
              Custom Overlay!
            </div>
          </CustomOverlayMap>
        ) : (
          " "
        )} */}
        {position && (
          <p>
            {"클릭한 위치의 위도는 " +
              position.lat +
              " 이고, 경도는 " +
              position.lng +
              " 입니다"}
          </p>
        )}
      </Map>
      <button onClick={handleCalculateDistance}>최단거리 계산</button>
      {/* <Roadview // 로드뷰를 표시할 Container
        position={{
          // 지도의 중심좌표
          'lat': 33.450701,
          'lng': 126.570667,
          'radius': 50,
        }}
        style={{
          // 지도의 크기
          width: "100%",
          height: "450px",
        }}
      /> */}
    </div>
  );
};
