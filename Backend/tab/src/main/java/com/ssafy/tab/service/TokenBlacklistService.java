package com.ssafy.tab.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service // 토큰 블랙리스트를 관리하기 위해 서비스
public class TokenBlacklistService {
    private Set<String> blacklistedTokens = new HashSet<>();

    public void addToBlacklist(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
