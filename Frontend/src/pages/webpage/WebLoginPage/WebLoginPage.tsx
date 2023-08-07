import { FC } from 'react';
import { WebLoginPageProps } from '.';
import { WebHeader } from '@/components/web/WebHeader';

export const WebLoginPage: FC<WebLoginPageProps> = (props) => {
	return (
    <div {...props}>
			<div>로그인</div>
    </div>
  );
};
