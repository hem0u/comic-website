package com.comic.service;

import com.comic.dto.UserRegisterDTO;
import com.comic.vo.ResultVO;

public interface UserService {
    // 用户注册
    ResultVO register(UserRegisterDTO registerDTO);
}