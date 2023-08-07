import { FC } from "react";
import { WebMainPageProps } from "./WebMainPage.props";
import { WebHeader } from "@/components/web/WebHeader";
import { WebLoginPage } from "../WebLoginPage";
import { WebSignupPage } from "../WebSignupPage";
import { WebBoardPage } from "../WebBoardPage";
import { WebRecommendPage } from "../WebRecommendPage";
import { WebSurveyPage } from "../WebSurveyPage";
import { Web404Page } from "../Web404Page";
import { Routes, Route } from "react-router-dom";

export const WebMainPage: FC<WebMainPageProps> = (props) => {
  return (
    <div {...props} className="web-body">
      <WebHeader />
      <div>메인</div>
      <Routes>
          <Route path="/" element={<div>메인 콘텐츠</div>}></Route>
          <Route path="signup" element={<WebSignupPage />}></Route>
          <Route path="login" element={<WebLoginPage />}></Route>
          <Route path="board" element={<WebBoardPage />}></Route>
          <Route path="recommend" element={<WebRecommendPage />}></Route>
          <Route path="survey" element={<WebSurveyPage />}></Route>
          <Route path="*" element={<Web404Page />}></Route>
      </Routes>
    </div>
  );
};
