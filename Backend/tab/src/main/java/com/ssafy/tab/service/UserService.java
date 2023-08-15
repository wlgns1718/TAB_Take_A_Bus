package com.ssafy.tab.service;

import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.UserUpdateDto;
import com.ssafy.tab.repository.UserRepository;
//import com.ssafy.tab.utils.JwtUtil;
import com.ssafy.tab.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private static final int SALT_SIZE = 16;
    @Value("${jwt.secret}")
    private String secretKey;

    private Long accessExpiredMs = 1000 * 60 * 60l; // 1시간 (현재 테스트를 위해 5분으로 설정)
    private Long refreshExpiredMs = 1000 * 60 * 600l; // 10시간

    /*public int idCheck(String userId) throws Exception {
        return userRepository.idCheck(userId);
    }*/

    @Transactional
    public boolean updateUser(String userId,UserUpdateDto userUpdateDto) throws Exception {
        User user = findByUserId(userId);
        if(user!=null){
            String dtoPw = userUpdateDto.getUserPw();
            String dtoName = userUpdateDto.getName();
            String dtoEmail = userUpdateDto.getEmail();

            System.out.println(dtoPw);
            System.out.println(dtoName);
            System.out.println(dtoEmail);

            if(dtoPw!=null){
                String findedPw = hashing(dtoPw, user.getSalt());
                if(!findedPw.equals(user.getUserPw())){
                    user.setUserPw(findedPw);
                }
            }
            if(dtoName!=null){
                user.setName(dtoName);
            }

            if(dtoEmail!=null){
                user.setEmail(dtoEmail);
            }

            return true;
        }

        return false;

    }

    @Transactional
    public Map<String,String> login(String userId, String password) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        // 입력받은 아이디와 비밀번호 검사
        List<User> users = userRepository.findByUserId(userId);
        if(users.isEmpty() || users.size()>2){ // 사용자가 없거나 2명 이상이면 잘못된 아이디
            log.info("사용자가 없거나 2명 이상인 아이디 입니다.");
            throw new Exception();
        }

        User user = users.get(0); // 사용자를 가져옴

        if(!hashing(password,user.getSalt()).equals(user.getUserPw())){ // 입력받은 비밀번호를 해싱해서 결과값이 같지 않으면 잘못된 비밀번호
            log.info("입력받은 password : "+password);
            log.info("salt 값 : " + user.getSalt());
            log.info("입력받은 password로 해싱한 값 : "+hashing(password,user.getSalt()));
            log.info("db에 저장된 userpw값 : "+ user.getUserPw());
            log.info("비밀번호가 일치하지 않습니다");
            throw new Exception();
        }

        String accessToken = JwtUtil.createToken(userId,secretKey,accessExpiredMs);
        String refreshToken = JwtUtil.createToken(userId,secretKey,refreshExpiredMs);

        user.setRefreshToken(refreshToken);
        resultMap.put("accessToken",accessToken);
        resultMap.put("refreshToken",refreshToken);
        resultMap.put("role",user.getRole().toString());
        return resultMap; // 위 과정을 성공적으로 거친 후 로그인 수행
    }

    public String requestToken(String token){
        Map<String, String> resultMap = new HashMap<>();

        if(JwtUtil.isExpired(token,secretKey)){
            return null;
        }

        String userId = JwtUtil.getUserId(token, secretKey);

        Optional<String> refreshToken = userRepository.findRefreshToken(userId);

        if(token.equals(refreshToken.get())){
            String accessToken = JwtUtil.createToken(userId,secretKey,accessExpiredMs);
            return accessToken;
        }

        return null;
    }

    @Transactional
    public Long joinUser(User user) throws Exception { // 회원가입

        validateDuplicateUser(user); // 중복검사

        String salt = makeSalt();
        user.setUserPw(hashing(user.getUserPw(),salt)); // 해싱 적용하여 비밀번호 설정
        user.setSalt(salt);
        userRepository.save(user);
        return user.getId();
    }
    private void validateDuplicateUser(User user) {
        List<User> findMembers = userRepository.findByUserId(user.getUserId());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public User findByUserId(String userId){
        List<User> users = userRepository.findByUserId(userId);
        if(users.isEmpty() || users.size() >1){
            return null;
        }
        return users.get(0);
    }

    @Transactional
    public Long joinUserKakao(User user) throws Exception { // 카카오 로그인
        userRepository.save(user);
        return user.getId();
    }

    public boolean checkId(String id){
        List<User> users = userRepository.findByUserId(id);
        if(users.size() > 0){
            return false;
        }
        return true;
    }

    @Transactional
    public void updatePw(String userId, String code) throws Exception {
        User user = findByUserId(userId);
        String salt = user.getSalt();
        String newPw = hashing(code,salt);
        System.out.println(newPw);
        user.setUserPw(newPw);
    }





    /*public UserDto getUser(String userId) throws Exception {
        return userRepository.getUser(userId);
    }

    public UserDto getUserByNo(int userNo) throws Exception {
        return UserMapper.getUserByNo(userNo);
    }
    @Transactional
    public void updateUser(UserDto UserDto) throws Exception {
        String salt = UserMapper.getSalt(UserDto.getUserId());
        UserDto.setPassword(hashing(UserDto.getPassword(),salt));
        UserDto.setSalt(salt);
        UserMapper.updateUser(UserDto);
    }*/


    /*@Transactional
    public void deleteUser(int userNo) throws Exception {
        UserMapper.deleteUser(userNo);
    }*/

    // 비밀번호 해싱
    private String hashing(String password, String salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");// SHA-256 해시함수를 이용
        for (int i = 0; i < 10000; i++) {// salt를 합쳐 새로운 해시비밀번호를 생성해 디코딩를 방지
            String temp = password + salt;// 패스워드와 Salt를 합쳐 새로운 문자열 생성
            md.update(temp.getBytes());// temp의 문자열을 해싱하여 md에 저장
            password = byteToString(md.digest());// md객체의 다이제스트를 얻어 password를 갱신
        }
        return password;
    }

    private String byteToString(byte[] temp) {// 바이트 값을 16진수로 변경
        StringBuilder sb = new StringBuilder();
        for (byte a : temp) {
            sb.append(String.format("%02x", a));
        }
        return sb.toString();
    }

    private String makeSalt() throws Exception {// salt 생성
        SecureRandom random = new SecureRandom();
        byte[] temp = new byte[SALT_SIZE];
        random.nextBytes(temp);

        return byteToString(temp);
    }

   /* @Transactional
    public void saveRefreshToken(int userNo, String refreshToken) throws Exception {
        UserMapper.saveRefreshToken(userNo,refreshToken);
    }


    @Transactional
    public void deleteRefreshToken(int userNo) throws Exception {
        UserMapper.deleteRefreshToken(userNo);
    }

    public String getRefreshToken(int userNo) throws Exception {
        return UserMapper.getRefreshToken(userNo);
    }

    public void findPw(UserDto userDto) throws Exception {
        String userId = userDto.getUserId();
        String salt = UserMapper.getSalt(userId);
        userDto.setPassword(hashing(userDto.getPassword(),salt));
        UserMapper.findPw(userDto);
    }

    public List<UserDto> searchUser(String userId) throws Exception {
        return UserMapper.searchUser(userId);
    }*/


}

