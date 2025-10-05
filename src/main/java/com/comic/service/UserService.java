package com.comic.service;

import com.comic.dto.UserLoginDTO;
import com.comic.dto.UserRegisterDTO;
import com.comic.vo.LoginVO;
import com.comic.vo.ResultVO;

public interface UserService {
    // 注册
    ResultVO register(UserRegisterDTO registerDTO);

    // 登录
    ResultVO<LoginVO> login(UserLoginDTO loginDTO);
}