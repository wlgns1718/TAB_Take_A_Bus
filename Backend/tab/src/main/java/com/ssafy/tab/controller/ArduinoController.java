package com.ssafy.tab.controller;

import com.ssafy.tab.domain.Bus;
import com.ssafy.tab.dto.BusDto;
import com.ssafy.tab.service.ArduinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tab/arduino")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ArduinoController {

    private final ArduinoService arduinoService;
    //아두이노에서 요청해서 넘겨줄 것
    @GetMapping("/{busNo}") //버스 테이블에서 사용할 foriegn key
    public ResponseEntity<Map<String,Object>> getInfo(@PathVariable("busNo") String busNo){

        //busNo로 정보 얻어오기 ex) 경북12가3456
        //만약 BUS_DATA안에 조회가 된다면 1 아니면 0 반환하기
        //vulerable은 bit(1) 0 or 1
        Map<String, Object> resultMap = new HashMap<>();

        try{
            Optional<Bus> info = arduinoService.getInfo(busNo);

            if(!info.isPresent()){
                resultMap.put("code","401");
                resultMap.put("msg","버스 정보가 없습니다.");
             }else{
                Bus busEntity = info.get();
                BusDto busDto = new BusDto(busEntity.getId(),busEntity.isVulnerable());
                resultMap.put("code","200");
                resultMap.put("msg","버스 정보가 있습니다.");
                resultMap.put("data",busDto);
             }
        }catch (Exception e){
            resultMap.put("code", "500");
            resultMap.put("msg","정보 불러오기 실패!!");
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    @PostMapping("/regist/{busNo}")
    public Map<String,Object> registInfo(){
        return new HashMap<>();
    }

}
