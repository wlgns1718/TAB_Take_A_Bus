import { FC, useEffect } from "react";
import { WebSurveyPageProps } from '.';
import {
  Map,
  MapMarker,
  CustomOverlayMap,
  MarkerClusterer,
} from "react-kakao-maps-sdk";



export const WebSurveyPage: FC<WebSurveyPageProps> = (props) => {
  
	return (
    <div {...props}>
      <div>수요조사</div>
      <Map
        center={{ lat: 33.5563, lng: 126.79581 }}
        style={{ width: "100%", height: "360px" }}
      >
        <MapMarker position={{ lat: 33.55635, lng: 126.795841 }}>
          <div style={{ color: "#000" }}>Hello World!</div>
        </MapMarker>
        <CustomOverlayMap position={{ lat: 33.55635, lng: 126.795841 }}>
          <div
            style={{ padding: "42px", backgroundColor: "#fff", color: "#000" }}
          >
            Custom Overlay!
          </div>
        </CustomOverlayMap>
      </Map>
    </div>
  );
};
