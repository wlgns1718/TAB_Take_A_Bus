import axios from "axios";


export const busAPI = axios.create({
  baseURL: "https://i9d111.p.ssafy.io:8001/api/stops/",
});
export const noticeAPI = axios.create({
  // baseURL: "https://i9d111.p.ssafy.io:8001/notice/",
  baseURL: "http://localhost:8000/notice/",
});
export const boardAPI = axios.create({
  // baseURL: "https://i9d111.p.ssafy.io:8001/notice/",
  baseURL: "http://localhost:8000/board/",
});
