import { useState } from 'react'
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import { WebMain } from './pages/webpage/WebMain';
import { MobileMain } from './pages/mobile/MobileMain';
import {BusInfomationPage} from "./pages/kiosk/BusInfomationPage";
import { AuthPage } from "./pages/kiosk/AuthPage";
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <BrowserRouter>
        <div>
          <Link to="/">홈</Link> |<Link to="/mobile/"> 모바일</Link> |
          <Link to="/kiosk/info/"> 키오스크 버스 정보</Link> |
          <Link to="/kiosk/Auth/"> 키오스크 관리자</Link>
        </div>
        {count}
        <button
          onClick={() => {
            setCount(count + 1);
          }}
        >
          {" "}
          버튼{" "}
        </button>
        <Routes>
          <Route path="/" element={<WebMain />}></Route>
          <Route path="/mobile/" element={<MobileMain />}></Route>
          <Route path="/kiosk/info/" element={<BusInfomationPage />}></Route>
          <Route path="/kiosk/Auth/" element={<AuthPage />}></Route>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App
