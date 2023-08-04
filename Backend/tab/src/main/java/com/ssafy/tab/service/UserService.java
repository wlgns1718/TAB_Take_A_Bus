package com.ssafy.tab.service;

import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.UserLoginDto;
import com.ssafy.tab.repository.UserRepository;
//import com.ssafy.tab.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final int SALT_SIZE = 16;
    @Value("${jwt.secret}")
    private String secretKey;

    private Long expiredMs = 1000 * 60 * 60l; // 1시간

    /*public int idCheck(String userId) throws Exception {
        return userRepository.idCheck(userId);
    }*/

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
    /*
    public String login(String userName, String password, String role) throws Exception {
        // 인증과정 생략
        return JwtUtil.createJwt(userName,role,secretKey,expiredMs);
    }
    */

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
