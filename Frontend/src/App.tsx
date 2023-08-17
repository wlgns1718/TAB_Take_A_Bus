import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import { WebMainPage } from "./pages/webpage/WebMainPage";
import { MobileMain } from "./pages/mobile/MobileMain";
import { BusInfomationPage } from "./pages/kiosk/BusInfomationPage";
import { AuthPage } from "./pages/kiosk/AuthPage";
import "./App.css";
import { MobileSetting } from "./pages/mobile/MobileSetting";

function App() {
  return (
    <>
      <BrowserRouter>
        {/* <div>
          <Link to="/">홈</Link> |<Link to="/mobile"> 모바일</Link> |
          <Link to="/kiosk/info/DGB7001004100"> 키오스크 버스 정보</Link> |
          <Link to="/kiosk/auth"> 키오스크 관리자</Link>
        </div> */}
        <Routes>
          <Route path="/*" element={<WebMainPage />}></Route>
          <Route path="/mobile/" element={<MobileSetting />}></Route>
          <Route path="/mobile/:vehicleNo" element={<MobileMain />}></Route>
          <Route path="/kiosk">
            <Route path="info/:id" element={<BusInfomationPage />}></Route>
            <Route path="auth" element={<AuthPage />}></Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
