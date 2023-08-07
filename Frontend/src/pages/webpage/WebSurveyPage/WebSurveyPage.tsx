import { FC } from 'react';
import { WebSurveyPageProps } from '.';
import { WebHeader } from "@/components/web/WebHeader";


export const WebSurveyPage: FC<WebSurveyPageProps> = (props) => {
	return (
    <div {...props}>
			<div>수요조사</div>
    </div>
  );
};
