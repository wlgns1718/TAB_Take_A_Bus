import axios from "axios";


export const busAPI = axios.create({
  baseURL: "https://i9d111.p.ssafy.io:8001/api/stops/",
});
