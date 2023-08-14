import { FC } from 'react';
import { Web404PageProps } from '.';

export const Web404Page: FC<Web404PageProps> = (props) => {
	return <div {...props}>
		<h2 style={{textAlign:'center'}}>404 Not Found..</h2>
	</div>;
};
