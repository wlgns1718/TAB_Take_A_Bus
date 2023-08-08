import { FC } from 'react';
import { WebLoginPageProps } from '.';
import Input from "@mui/joy/Input";

export const WebLoginPage: FC<WebLoginPageProps> = (props) => {
	return (
    <div {...props}>
      <h1>로그인</h1>
      <div className='login-box'>
        <Input
          color="primary"
          disabled={false}
          placeholder="아이디"
          size="lg"
          variant="outlined"
        />
        <Input
          color="primary"
          disabled={false}
          placeholder="비밀번호"
          size="lg"
          variant="outlined"
        />
      </div>
    </div>
  );
};
