import axios from "axios";


export const busAPI = axios.create({ baseURL: "http://127.0.0.1/api/stops/"});
