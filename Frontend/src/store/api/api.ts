import axios from "axios";


export const arduinoAPI = axios.create({
  baseURL: "https://i9d111.p.ssafy.io/tab/arduino/",
});
export const busAPI = axios.create({
  baseURL: "https://i9d111.p.ssafy.io/tab/station/",
});
export const noticeAPI = axios.create({
  baseURL: "https://i9d111.p.ssafy.io/tab/notice/",
});
export const boardAPI = axios.create({
  baseURL: "https://i9d111.p.ssafy.io/tab/board",
});
export const webAPI = axios.create({
  baseURL: "https://i9d111.p.ssafy.io/tab/"
})

