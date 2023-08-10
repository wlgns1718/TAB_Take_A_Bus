import { FC } from "react";
import { WebMainPageProps } from "./WebMainPage.props";
import { WebHeader } from "@/components/web/WebHeader";
import { WebLoginPage } from "../WebLoginPage";
import { WebSignupPage } from "../WebSignupPage";
import { WebBoardPage } from "../WebBoardPage";
import { WebBoardPostPage } from "../WebBoardPostPage";
import { WebBoardDetailPage } from "../WebBoardDetailPage";
import { WebRecommendPage } from "../WebRecommendPage";
import { WebSurveyPage } from "../WebSurveyPage";
import { Web404Page } from "../Web404Page";
import { Routes, Route } from "react-router-dom";
import {  Grid } from "@mui/material";
import { Button } from "@mui/joy";


import './WebMainPage.css'
import { WebNoticeDetailPage } from "../WebNoticeDetailPage";

export const WebMainPage: FC<WebMainPageProps> = (props) => {
  return (
    <div {...props} id="web-root">
      <WebHeader />
      <div className="web-body">
        <Routes>
          <Route
            path="/"
            element={
              <div>
                <h1>Take A Bus</h1>
                <Grid
                  container
                  direction="row"
                  className="main-content-container"
                >
                  <Grid item xs={6} className="main-content-text">
                    <h2>title</h2>
                    <p>description</p>
                    <Button
                      color="primary"
                      onClick={function () {}}
                      size="lg"
                      variant="soft"
                    >
                      TAB
                    </Button>
                  </Grid>
                  <Grid item xs={6}>
                    <img src="/main_bus.png?url" style={{ width: "100%" }} />
                  </Grid>
                </Grid>
              </div>
            }
          ></Route>
          <Route path="signup" element={<WebSignupPage />}></Route>
          <Route path="login" element={<WebLoginPage />}></Route>
          <Route path="board/">
            <Route path="" element={<WebBoardPage />}></Route>
            <Route path="post" element={<WebBoardPostPage />}></Route>
            <Route path="detail/:postId" element={<WebBoardDetailPage />}></Route>
            <Route path="notice/:noticeId" element={<WebNoticeDetailPage />}></Route>
          </Route>
          <Route path="recommend" element={<WebRecommendPage />}></Route>
          <Route path="survey" element={<WebSurveyPage />}></Route>
          <Route path="*" element={<Web404Page />}></Route>
        </Routes>
      </div>
    </div>
  );
};
