package com.frame.kzou.business.controller.common;

import com.frame.kzou.business.base.BaseController;
import com.frame.kzou.common.ResultResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: HP-zouKun
 * Date: 2022/10/10
 * Time: 8:50
 * Description: 
 */
@RestController
@RequestMapping(value = "auth")
public class AuthController extends BaseController {

    @ApiOperation(value = "绑定操作用户")
    @GetMapping("bind/{userId}")
    public ResultResponse<Void> bindUser(@PathVariable String userId) {
        setUser(userId);
        return ResultResponse.success();
    }

    @ApiOperation(value = "解绑操作用户")
    @DeleteMapping("unbind/user")
    public ResultResponse<Void> unbindUser() {
        removeUser();
        return ResultResponse.success();
    }
}
