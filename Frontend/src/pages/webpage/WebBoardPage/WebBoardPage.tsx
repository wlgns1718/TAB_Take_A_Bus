import { FC } from 'react';
import { WebBoardPageProps } from '.';
import { WebHeader } from '@/components/web/WebHeader';

export const WebBoardPage: FC<WebBoardPageProps> = (props) => {
	return (
    <div {...props}>
      <div>게시판</div>
    </div>
  );
};
