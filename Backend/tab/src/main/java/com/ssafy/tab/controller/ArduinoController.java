package com.ssafy.tab.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.tab.domain.Bus;
import com.ssafy.tab.dto.BusDataDto;
import com.ssafy.tab.dto.BusDto;
import com.ssafy.tab.service.ArduinoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiOperation(value = "아두이노 통신", notes = "아두이노가 통신할 수 있습니다.",response = Map.class)
    @GetMapping("/{vehicleNo}") //버스 테이블에서 사용할 foriegn key
    public ResponseEntity<Map<String,Object>> getInfo(@PathVariable("vehicleNo") @ApiParam(value = "버스 번호판", required = true) String vehicleNo){

        //busNo로 정보 얻어오기 ex) 경북12가3456
        //만약 BUS_DATA안에 조회가 된다면 1 아니면 0 반환하기
        //vulerable은 bit(1) 0 or 1
        Map<String, Object> resultMap = new HashMap<>();

        try{
            Optional<Bus> info = arduinoService.getInfo(vehicleNo);
            System.out.println(info);
            if(!info.isPresent()){
                resultMap.put("code","401");
                resultMap.put("msg","버스 정보가 없습니다.");
             }else{
                //만약 해당 버스 정보가 있다면 조회 후 삭제 하기
                Bus busEntity = info.get();
                BusDto busDto = BusDto.toEntity(busEntity);
                System.out.println("daslkdjalskdlksa"+busDto);
                resultMap.put("code","200");
                resultMap.put("msg","버스 정보가 있습니다.");
                resultMap.put("data",busDto);
                //조회 후 삭제 기능 추가
                //Bus 데이터 베이스에 해당 데이터 삭제
                arduinoService.deleteInfo(busDto.getId());
             }
        }catch (Exception e){
            e.printStackTrace();
            String error = e.getMessage();
            resultMap.put("code", "500");
            resultMap.put("msg","정보 불러오기 실패!!");
            resultMap.put("error", error);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "버스 탑승 정보 등록", notes = "버스 탑승정보를 저장할 수 있습니다.",response = Map.class)
    @PostMapping("/regist")
    public ResponseEntity<Map<String, Object>> registInfo(@RequestBody @ApiParam(value = "버스 탑승정보를 위한 데이터", required = true)BusDataDto busDataDto){
        Map<String,Object> resultMap = new HashMap<>();
        try{
            if(busDataDto.getCount() >= 1){
                arduinoService.createBus(busDataDto);
                resultMap.put("code","200");
                resultMap.put("msg","버스 정보 등록 완료!");
            }
            else{
                resultMap.put("code","200");
                resultMap.put("msg","탑승 인원이 0명이라 등록되지 않습니다.");
            }


        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("code","500");
            resultMap.put("msg","버스 정보 등록 실패!");

        }

        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
    }

}
