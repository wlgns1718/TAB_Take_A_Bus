import { FC } from 'react';
import { WebSignupPageProps } from '.';
import { WebHeader } from "@/components/web/WebHeader";


export const WebSignupPage: FC<WebSignupPageProps> = (props) => {
	return (
    <div {...props}>
			<div>회원가입</div>
    </div>
  );
};
