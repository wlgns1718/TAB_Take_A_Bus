import { FC } from 'react';
import { WebHeader } from "@/components/web/WebHeader";
import { WebRecommendPageProps } from ".";

export const WebRecommendPage: FC<WebRecommendPageProps> = (props) => {
	return (
    <div {...props}>
      <div>관광/맛집</div>
    </div>
  );
};
