import { FC } from 'react';
import { WebBoardPostPageProps } from '.';
import { PostBoardForm } from '@/components/web/PostBoardForm';

export const WebBoardPostPage: FC<WebBoardPostPageProps> = (props) => {
	return <div {...props}>
		<div>게시판 작성</div>
		<div>
			<PostBoardForm></PostBoardForm>
		</div>
	</div>;
};
