import { FC, useEffect, useState } from "react";
import { WebRecommendPageProps } from ".";
import axios from "axios";
import {
  AspectRatio,
  Button,
  Card,
  CardContent,
  CardOverflow,
  Container,
  Divider,
  Option,
  Select,
  Stack,
  Typography,
} from "@mui/joy";
import { busAPI, webAPI } from "@/store/api/api";
import "./WebRecommendPage.css";
import MapIcon from "@mui/icons-material/Map";

export const WebRecommendPage: FC<WebRecommendPageProps> = (props) => {
  // citycode 리스트
  // https://apis.data.go.kr/1613000/BusRouteInfoInqireService/getCtyCodeList?serviceKey=5ts%2Baf9Tv7mT28mcFD0Y8pzBg7sy1TYdLve4W7vJd5pt44kEEAkpi8AbNEVKnb%2Fk2z79M9WDxTozeVzNWlPkdA%3D%3D&_type=xml

  // 노선 리스트
  //  https://apis.data.go.kr/1613000/BusRouteInfoInqireService/getRouteNoList?serviceKey=5ts%2Baf9Tv7mT28mcFD0Y8pzBg7sy1TYdLve4W7vJd5pt44kEEAkpi8AbNEVKnb%2Fk2z79M9WDxTozeVzNWlPkdA%3D%3D&pageNo=1&numOfRows=100&_type=xml&cityCode=37050
  type TripPlace = {
    addr1: string;
    firstimage: string;
    firstimage2: string;
    mapx: number | null;
    mapy: number | null;
    tel: string;
    title: string;
  };

  type Route = {
    endnodenm: string;
    endvehicletime: number;
    routeid: string;
    routeno: string;
    routetp: string;
    startnodenm: string;
    startvehicletime: string;
  };

  const citis = [
    { cityname: "가평군", citycode: "31370" },
    { cityname: "강릉시", citycode: "32030" },
    { cityname: "강진군", citycode: "36390" },
    { cityname: "거제시", citycode: "38090" },
    { cityname: "거창군", citycode: "38390" },
    { cityname: "경산시", citycode: "37100" },
    { cityname: "경주시", citycode: "37020" },
    { cityname: "계룡시", citycode: "34070" },
    { cityname: "고령군", citycode: "37370" },
    { cityname: "고성군", citycode: "38340" },
    { cityname: "고양시", citycode: "31100" },
    { cityname: "고창군", citycode: "35370" },
    { cityname: "고흥군", citycode: "36350" },
    { cityname: "공주시", citycode: "34020" },
    { cityname: "과천시", citycode: "31110" },
    { cityname: "광명시", citycode: "31060" },
    { cityname: "광양시", citycode: "36060" },
    { cityname: "광주광역시", citycode: "24" },
    { cityname: "광주시", citycode: "31250" },
    { cityname: "괴산군", citycode: "33360" },
    { cityname: "구례군", citycode: "36330" },
    { cityname: "구리시", citycode: "31120" },
    { cityname: "구미시", citycode: "37050" },
    { cityname: "군산시", citycode: "35020" },
    { cityname: "군위군", citycode: "37310" },
    { cityname: "군포시", citycode: "31160" },
    { cityname: "금산군", citycode: "34310" },
    { cityname: "김제시", citycode: "35060" },
    { cityname: "김천시", citycode: "37030" },
    { cityname: "김포시", citycode: "31230" },
    { cityname: "김해시", citycode: "38070" },
    { cityname: "나주시", citycode: "36040" },
    { cityname: "남양주시", citycode: "31130" },
    { cityname: "남원시", citycode: "35050" },
    { cityname: "남해군", citycode: "38350" },
    { cityname: "논산시", citycode: "34060" },
    { cityname: "단양군", citycode: "33380" },
    { cityname: "담양군", citycode: "36310" },
    { cityname: "당진시", citycode: "34390" },
    { cityname: "대구광역시", citycode: "22" },
    { cityname: "대전광역시", citycode: "25" },
    { cityname: "동두천시", citycode: "31080" },
    { cityname: "동해시", citycode: "32040" },
    { cityname: "목포시", citycode: "36010" },
    { cityname: "무안군", citycode: "36420" },
    { cityname: "무주군", citycode: "35330" },
    { cityname: "문경시", citycode: "37090" },
    { cityname: "밀양시", citycode: "38080" },
    { cityname: "보령시", citycode: "34030" },
    { cityname: "보은군", citycode: "33320" },
    { cityname: "봉화군", citycode: "37410" },
    { cityname: "부산광역시", citycode: "21" },
    { cityname: "부안군", citycode: "35380" },
    { cityname: "부여군", citycode: "34330" },
    { cityname: "부천시", citycode: "31050" },
    { cityname: "사천시", citycode: "38060" },
    { cityname: "산청군", citycode: "38370" },
    { cityname: "삼척시", citycode: "32070" },
    { cityname: "상주시", citycode: "37080" },
    { cityname: "서산시", citycode: "34050" },
    { cityname: "서울특별시", citycode: "11" },
    { cityname: "서천군", citycode: "34340" },
    { cityname: "성남시", citycode: "31020" },
    { cityname: "성주군", citycode: "37380" },
    { cityname: "세종특별시", citycode: "12" },
    { cityname: "속초시", citycode: "32060" },
    { cityname: "수원시", citycode: "31010" },
    { cityname: "순창군", citycode: "35360" },
    { cityname: "순천시", citycode: "36030" },
    { cityname: "시흥시", citycode: "31150" },
    { cityname: "신안군", citycode: "36480" },
    { cityname: "아산시", citycode: "34040" },
    { cityname: "안동시", citycode: "37040" },
    { cityname: "안산시", citycode: "31090" },
    { cityname: "안성시", citycode: "31220" },
    { cityname: "안양시", citycode: "31040" },
    { cityname: "양구군", citycode: "32380" },
    { cityname: "양산시", citycode: "38100" },
    { cityname: "양양군", citycode: "32410" },
    { cityname: "양주시", citycode: "31260" },
    { cityname: "양평군", citycode: "31380" },
    { cityname: "여수시", citycode: "36020" },
    { cityname: "여주시", citycode: "31320" },
    { cityname: "연천군", citycode: "31350" },
    { cityname: "영광군", citycode: "36440" },
    { cityname: "영덕군", citycode: "37350" },
    { cityname: "영동군", citycode: "33340" },
    { cityname: "영암군", citycode: "36410" },
    { cityname: "영양군", citycode: "37340" },
    { cityname: "영월군", citycode: "32330" },
    { cityname: "영주시", citycode: "37060" },
    { cityname: "영천시", citycode: "37070" },
    { cityname: "예산군", citycode: "34370" },
    { cityname: "예천군", citycode: "37400" },
    { cityname: "오산시", citycode: "31140" },
    { cityname: "옥천군", citycode: "33330" },
    { cityname: "완도군", citycode: "36460" },
    { cityname: "완주군", citycode: "35310" },
    { cityname: "용인시", citycode: "31190" },
    { cityname: "울릉군", citycode: "37430" },
    { cityname: "울산광역시", citycode: "26" },
    { cityname: "울진군", citycode: "37420" },
    { cityname: "원주시", citycode: "32020" },
    { cityname: "음성군", citycode: "33370" },
    { cityname: "의령군", citycode: "38310" },
    { cityname: "의성군", citycode: "37320" },
    { cityname: "의왕시", citycode: "31170" },
    { cityname: "의정부시", citycode: "31030" },
    { cityname: "이천시", citycode: "31210" },
    { cityname: "익산시", citycode: "35030" },
    { cityname: "인제군", citycode: "32390" },
    { cityname: "인천광역시", citycode: "23" },
    { cityname: "임실군", citycode: "35350" },
    { cityname: "장성군", citycode: "36450" },
    { cityname: "장수군", citycode: "35340" },
    { cityname: "전주시", citycode: "35010" },
    { cityname: "정선군", citycode: "32350" },
    { cityname: "정읍시", citycode: "35040" },
    { cityname: "제주도", citycode: "39" },
    { cityname: "제천시", citycode: "33030" },
    { cityname: "증평군", citycode: "33390" },
    { cityname: "진도군", citycode: "36470" },
    { cityname: "진안군", citycode: "35320" },
    { cityname: "진주시", citycode: "38030" },
    { cityname: "진천군", citycode: "33350" },
    { cityname: "창녕군", citycode: "38330" },
    { cityname: "창원시", citycode: "38010" },
    { cityname: "천안시", citycode: "34010" },
    { cityname: "철원군", citycode: "32360" },
    { cityname: "청도군", citycode: "37360" },
    { cityname: "청송군", citycode: "37330" },
    { cityname: "청양군", citycode: "34350" },
    { cityname: "청주시", citycode: "33010" },
    { cityname: "춘천시", citycode: "32010" },
    { cityname: "충주시", citycode: "33020" },
    { cityname: "칠곡군", citycode: "37390" },
    { cityname: "태백시", citycode: "32050" },
    { cityname: "통영시", citycode: "38050" },
    { cityname: "파주시", citycode: "31200" },
    { cityname: "평창군", citycode: "32340" },
    { cityname: "평택시", citycode: "31070" },
    { cityname: "포천시", citycode: "31270" },
    { cityname: "포항시", citycode: "37010" },
    { cityname: "하남시", citycode: "31180" },
    { cityname: "하동군", citycode: "38360" },
    { cityname: "함안군", citycode: "38320" },
    { cityname: "함양군", citycode: "38380" },
    { cityname: "함평군", citycode: "36430" },
    { cityname: "합천군", citycode: "38400" },
    { cityname: "해남군", citycode: "36400" },
    { cityname: "홍성군", citycode: "34360" },
    { cityname: "홍천군", citycode: "32310" },
    { cityname: "화성시", citycode: "31240" },
    { cityname: "횡성군", citycode: "32320" },
  ];
  // 12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점
  const tripTypes = [
    {
      code: 12,
      text: "관광지",
    },
    {
      code: 14,
      text: "문화시설",
    },
    {
      code: 15,
      text: "축제공연행사",
    },
    {
      code: 25,
      text: "여행코스",
    },
    {
      code: 28,
      text: "레포츠",
    },
    {
      code: 32,
      text: "숙박",
    },
    {
      code: 38,
      text: "쇼핑",
    },
    {
      code: 39,
      text: "음식점",
    },
  ];
  const [routes, setRoutes] = useState([]);

  const [tripData, setTripData] = useState<object>({
    신천3치안센터앞: [
      {
        addr1: "대구광역시 동구 동부로26길 6",
        firstimage:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        firstimage2:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        mapx: 128.6276707967,
        mapy: 35.8753644169,
        tel: "",
        title: "대구 메리어트 호텔",
      },
      {
        addr1: "대구광역시 동구 동부로26길 6",
        firstimage:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        firstimage2:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        mapx: 128.627670796,
        mapy: 35.8753644169,
        tel: "",
        title: "대구 메리어트 호텔",
      },
      {
        addr1: "대구광역시 동구 동부로26길 6",
        firstimage: "",
        firstimage2:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        mapx: 128.6276707967,
        mapy: 35.8753644169,
        tel: "",
        title: "대구 메리어트 호텔",
      },
      {
        addr1: "대구광역시 동구 동부로26길 6",
        firstimage:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        firstimage2:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        mapx: 128.6276707967,
        mapy: 35.8753644169,
        tel: "",
        title: "대구 메리어트 호텔",
      },
    ],
    홈플러스동촌점건너: [
      {
        addr1: "대구광역시 동구 동부로26길 6",
        firstimage:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        firstimage2:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        mapx: null,
        mapy: null,
        tel: "",
        title: "대구 메리어트 호텔",
      },
      {
        addr1: "대구광역시 동구 동부로26길 6",
        firstimage:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        firstimage2:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        mapx: 128.6276707967,
        mapy: 35.8753644169,
        tel: "",
        title: "대구 메리어트 호텔",
      },
      {
        addr1: "대구광역시 동구 동부로26길 6",
        firstimage:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        firstimage2:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        mapx: 128.6276707967,
        mapy: 35.8753644169,
        tel: "",
        title: "대구 메리어트 호텔",
      },
      {
        addr1: "대구광역시 동구 동부로26길 6",
        firstimage:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        firstimage2:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        mapx: 128.6276707967,
        mapy: 35.8753644169,
        tel: "",
        title: "대구 메리어트 호텔",
      },
      {
        addr1: "대구광역시 동구 동부로26길 6",
        firstimage:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        firstimage2:
          "http://tong.visitkorea.or.kr/cms/resource/53/2721553_image2_1.jpg",
        mapx: 28.6276707967,
        mapy: 35.8753644169,
        tel: "",
        title: "대구 메리어트 호텔",
      },
    ],
  });

  const [isexistResult, setIsexistResult] = useState<boolean>(false);

  const [selectedTripType, setSelectedTripType] = useState<{
    code: number;
    text: string;
  }>();

  const [selectedCity, setSelectedCity] = useState({
    citycode: null,
    cityname: null,
  });

  const [isSelectCity, setIsSelectCity] = useState(false);
  const [selectedRouteId, setSelectedRouteId] = useState<Route>();

  useEffect(() => {
    if (routes != null) setIsSelectCity(true);
    else setIsSelectCity(false);
  }, [routes]);
  useEffect(() => {
    if (citis.length > 0) {
      axios
        .get(
          `https://apis.data.go.kr/1613000/BusRouteInfoInqireService/getRouteNoList?serviceKey=5ts%2Baf9Tv7mT28mcFD0Y8pzBg7sy1TYdLve4W7vJd5pt44kEEAkpi8AbNEVKnb%2Fk2z79M9WDxTozeVzNWlPkdA%3D%3D&pageNo=1&numOfRows=100&_type=json&cityCode=${selectedCity.citycode}`
        )
        .then((response) => {
          console.log(response.data);
          setRoutes(response.data.response.body.items.item);
        });
    }
  }, [selectedCity]);
  useEffect(() => {
    if (tripData) {
      setIsexistResult(true);
    } else {
      setIsexistResult(false);
    }
  }, [isexistResult]);

  const getRecommend = () => {
    if (!selectedCity) {
      alert("도시를 선택해주세요");
      return;
    }
    if (!selectedRouteId) {
      alert("노선을 선택해주세요");
      return;
    }
    if (!selectedTripType) {
      alert("관광지을 선택해주세요");
      return;
    }
    // axios
    busAPI
      .get(
        `/trip/${selectedCity?.citycode}/${selectedRouteId?.routeid}/${selectedTripType?.code}`,
        { timeout: 10000 }
      )
      .then((response) => {
        console.log(response.data);
        setTripData(response.data.data);
      });
  };

  const handleCityChange = (value) => {
    console.log(value);
    setSelectedCity(value);
  };
  const handleRouteChange = (value) => {
    console.log(value);
    setSelectedRouteId(value);
  };
  const handleTripTypeChange = (value) => {
    console.log(value);
    setSelectedTripType(value);
  };

  const RouteTripList = ({ key, value, index }) => {
    console.log(key, value);
    let maxIndex = 4;
    return (
      <Container sx={{ marginTop: 10 }} key={index}>
        <Container sx={{ fontSize: 30 }}>{key}에서 하차시</Container>
        <Container
          sx={{
            display: "flex",
            flexWrap: "wrap",
            gap: 2,
            justifyContent: "left",
          }}
        >
          {value?.slice(0, maxIndex).map((el: TripPlace, idx) => {
            return (
              <Card variant="outlined" sx={{ width: "45%" }} key={idx}>
                <CardOverflow>
                  <AspectRatio ratio="2">
                    <img
                      src={
                        el.firstimage
                          ? `${el.firstimage}`
                          : `/place_basic.jpg?url`
                      }
                      srcSet={`${el.firstimage}`}
                      loading="lazy"
                      alt=""
                    />
                  </AspectRatio>
                </CardOverflow>
                <CardContent>
                  <Stack direction={"row"} justifyContent={"space-between"}>
                    <div>
                      <Typography level="title-md">
                        <a
                          href={`https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=${el.title}`}
                        >
                          {el.title}
                        </a>
                      </Typography>
                      <Typography level="body-sm">{el.addr1}</Typography>
                    </div>
                    <Button
                      style={{ flexShrink: 1 }}
                      disabled={el.mapx ? false : true}
                      onClick={() => {
                        window.open(
                          `http://maps.naver.com/?menu=location&mapMode=0&lat=${el.mapx}&lng=${el.mapy}&dlevel=12&enc=b64mapMode`
                        );
                      }}
                    >
                      <MapIcon />
                    </Button>
                  </Stack>
                </CardContent>
              </Card>
            );
          })}
          {value.length > 4 ? (
            <Button
              onClick={() => {
                if (maxIndex == 4) {
                  maxIndex = 1000;
                } else {
                  maxIndex = 4;
                }
              }}
            >
              더보기
            </Button>
          ) : (
            ""
          )}
        </Container>
      </Container>
    );
  };

  return (
    <div {...props}>
      <h1 style={{ textAlign: "center" }}>버스 노선 장소 추천 서비스</h1>
      <Container maxWidth="md">
        <Select size="lg" placeholder="시/군 선택" className="select-city">
          {citis.map((op, index) => {
            return (
              <Option
                key={index}
                value={op.cityname}
                onClick={() => handleCityChange(op)}
                className="options-city"
              >
                {op.cityname}
              </Option>
            );
          })}
        </Select>
        <Select
          size="lg"
          placeholder="버스 노선 선택"
          className="select-bus-route"
          disabled={!isSelectCity}
        >
          {routes?.map((op, index) => {
            return (
              <Option
                key={index}
                value={op}
                onClick={() => handleRouteChange(op)}
                className="options-city"
              >
                {op.routeno}
              </Option>
            );
          })}
        </Select>
        <Select
          size="lg"
          placeholder="카테고리"
          className="select-bus-route"
          disabled={!isSelectCity}
        >
          {tripTypes?.map((op, index) => {
            return (
              <Option
                key={index}
                value={op.text}
                onClick={() => handleTripTypeChange(op)}
                className="options-city"
              >
                {op.text}
              </Option>
            );
          })}
        </Select>
        <Button
          onClick={() => {
            getRecommend();
          }}
        >
          추천 받기
        </Button>
      </Container>
      {/* <div>
        {isexistResult ? (
          Object.entries(tripData).map(([key, value], index) => {
            <RouteTripList key={key} value={value} index={index}/>
          })
        ) : (
          <div>검색된 데이터가 없습니다</div>
        )}
      </div> */}
    </div>
  );
};
