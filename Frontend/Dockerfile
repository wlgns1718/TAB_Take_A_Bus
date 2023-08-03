# Node.js 빌드 단계
FROM node:14 as build

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY . .
# RUN npm run build

# Nginx 실행 단계
FROM nginx:alpine

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]