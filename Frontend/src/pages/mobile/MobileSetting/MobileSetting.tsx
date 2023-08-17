import { FC, useState } from "react";
import { MobileSettingProps } from ".";
import { useNavigate } from "react-router-dom";
import { Button, Input, Stack } from "@mui/joy";

export const MobileSetting: FC<MobileSettingProps> = (props) => {
  const navigate = useNavigate();

  const [inputContent, setInputContent] = useState<string>("");

  return (
    <div {...props} className="mobile-body">
      <div className="content">
        <div className="content-header">
          <div style={{ display: "flex" }}>
            <h1 className="white">차량번호 입력</h1>
          </div>
        </div>
        <div className="content-body">
          <Stack
            direction={"row"}
            gap={3}
            sx={{ height: 50, marginY: "auto", marginLeft: 5 }}
          >
            <input
              style={{ width: 1000, height: 100, fontSize: 40 }}
              placeholder=" 00가0000 형식으로 입력해주세요"
              onChange={(value) => {
                setInputContent(value.target.value);
              }}
              onKeyDown={(e) => {
                if (e.key == "Enter") {
                  navigate(inputContent);
                }
              }}
            ></input>
            <Button
              sx={{ width: 160, height: 110, fontSize: 40 }}
              onClick={() => {
                navigate(inputContent);
              }}
            >
              등록
            </Button>
          </Stack>
        </div>
      </div>
    </div>
  );
};
