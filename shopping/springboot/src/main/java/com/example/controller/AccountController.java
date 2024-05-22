package com.example.controller;

import com.example.common.config.CaptureConfig;
import com.example.common.config.JwtTokenUtils;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.*;
import com.example.service.*;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.*;

/**
 *  描述：系统用户操作相关接口
 */
@RestController
public class AccountController {

	@Resource
	private AdminService adminService;
	@Resource
	private UserService userService;

    /**
     * 描述：用户登录接口
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account,@RequestParam String key, HttpServletRequest request) {
        Integer role = account.getRole();
        Account loginUser = new Account();
        Map<String, Object> map = new HashMap<>();
		if (1 == role) {
			loginUser = adminService.login(account.getUsername(), account.getPassword());
			map.put("user", loginUser);
		}
		if (2 == role) {
			loginUser = userService.numLogin(account.getUsername(), account.getPassword());
			map.put("user", loginUser);
		}
		// 判断验证码对不对
		if (!account.getVerCode().toLowerCase().equals(CaptureConfig.CAPTURE_MAP.get(key))) {
			// 如果不相等，说明验证不通过
			CaptchaUtil.clear(request);
			return Result.error("验证码不正确");
		}

        // create token
        String token = JwtTokenUtils.genToken(loginUser.getId() + "-" + loginUser.getRole(), loginUser.getPassword());
        map.put("token", token);
        return Result.success(map);
    }

    /**
     * 描述：用户注册接口
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        Integer role = account.getRole();
        Account login = new Account();
		if (1 == role) {
			Admin info = new Admin();
			BeanUtils.copyProperties(account, info);
			login = adminService.add(info);
		}
		if (2 == role) {
			User info = new User();
			BeanUtils.copyProperties(account, info);
			login = userService.add(info);
		}

        return Result.success(login);
    }

    /**
     * 描述：获取当前登录用户信息
     */
    @GetMapping("/getUser")
    public Result getAccountInfo() {
        Account loginUser = JwtTokenUtils.getCurrentUser();
        if (loginUser == null) {
            return Result.success(new Object());
        }
        Integer role = loginUser.getRole();
		if (1 == role) {
			return Result.success(adminService.findById(loginUser.getId()));
		}
		if (2 == role) {
			return Result.success(userService.findById(loginUser.getId()));
		}

        return Result.success(new Object());
    }

    /**
    * 描述：更新密码接口
    */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account info) {
        Account account = JwtTokenUtils.getCurrentUser();
        if (account == null) {
            return Result.error(ResultCode.USER_NOT_EXIST_ERROR.code, ResultCode.USER_NOT_EXIST_ERROR.msg);
        }
        String oldPassword = info.getPassword();
        if (!oldPassword.equals(account.getPassword())) {
            return Result.error(ResultCode.PARAM_PASSWORD_ERROR.code, ResultCode.PARAM_PASSWORD_ERROR.msg);
        }
        Integer role = account.getRole();
		if (1 == role) {
			Admin admin = new Admin();
			BeanUtils.copyProperties(account, admin);
			admin.setPassword(info.getNmm());
			adminService.update(admin);
		}
		if (2 == role) {
			User user = new User();
			BeanUtils.copyProperties(account, user);
			user.setPassword(info.getNmm());
			userService.update(user);
		}

        return Result.success();
    }

    /**
     * 描述：重置密码接口
     */
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody Account account) {
        Integer role = account.getRole();
		if (1 == role) {
			Admin info = adminService.findByUsername(account.getUsername());
			if (info == null) {
				return Result.error(ResultCode.USER_NOT_EXIST_ERROR);
			}
			info.setPassword("123456");
			adminService.update(info);
		}
		if (2 == role) {
			User info = userService.findByUserName(account.getUsername());
			if (info == null) {
				return Result.error(ResultCode.USER_NOT_EXIST_ERROR);
			}
			info.setPassword("123456");
			userService.update(info);
		}

        return Result.success();
    }
}
