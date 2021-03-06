package com.reading.website.biz.controller.reading;

import com.reading.website.api.base.BaseResult;
import com.reading.website.api.base.StatusCodeEnum;
import com.reading.website.api.domain.LoginInfoDTO;
import com.reading.website.api.domain.UserBaseInfoDO;
import com.reading.website.api.domain.UserNotesInfoDO;
import com.reading.website.api.domain.UserNotesInfoQuery;
import com.reading.website.api.service.UserNotesInfoService;
import com.reading.website.api.vo.UserNotesInfoVO;
import com.reading.website.biz.logic.NoteLogic;
import com.reading.website.biz.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户笔记接口
 *
 * @xyang010 2019/3/21
 */
@Api(value = "用户笔记接口", description = "UserNoteController", tags = {"用户笔记接口"})
@RestController
@Slf4j
@RequestMapping("/notes")
public class UserNoteController {

    private final UserNotesInfoService notesInfoService;
    private final NoteLogic noteLogic;

    @Autowired
    public UserNoteController(UserNotesInfoService notesInfoService, NoteLogic noteLogic) {
        this.notesInfoService = notesInfoService;
        this.noteLogic = noteLogic;
    }

    @ApiOperation(value="新增阅读笔记", notes="新增阅读笔记")
    @PostMapping(value = "/add")
    public BaseResult<Integer> register(@RequestBody UserNotesInfoDO notesInfoDO, HttpServletRequest request) {
        if (notesInfoDO.getUserId() == null) {
            LoginInfoDTO loginInfoDTO = UserUtil.getUserLoginInfo(request);
            if (loginInfoDTO != null) {
                notesInfoDO.setUserId(loginInfoDTO.getUserId());

            } else {
                return BaseResult.errorReturn(StatusCodeEnum.TOKEN_EXPIRE.getCode(), "TOKEN_EXPIRE");
            }
        }

        return notesInfoService.insert(notesInfoDO);
    }

    @ApiOperation(value="删除阅读笔记", notes="删除阅读笔记")
    @GetMapping(value = "/del")
    public BaseResult<Integer> register(@RequestParam("noteId") Integer noteId) {
        return notesInfoService.del(noteId);
    }

    @ApiOperation(value="查询用户阅读笔记", notes="查询用户阅读笔记")
    @PostMapping(value = "/queryByUserId")
    public BaseResult<List<UserNotesInfoVO>> queryByUserId(@RequestBody UserNotesInfoQuery query) {
        BaseResult<List<UserNotesInfoDO>> queryRes = notesInfoService.selectNoteByUserId(query);
        if (!queryRes.getSuccess()) {
            return BaseResult.errorReturn(StatusCodeEnum.SERVICE_ERROR.getCode(), "查询用户阅读笔记失败");
        }

        List<UserNotesInfoVO> voList = noteLogic.convertDOsToVOs(queryRes.getData());
        return BaseResult.rightReturn(voList, queryRes.getPage());
    }

    @ApiOperation(value="查询用户单本书阅读笔记", notes="查询用户单本书阅读笔记")
    @PostMapping(value = "/queryByUserIdAndBookId")
    public BaseResult<List<UserNotesInfoVO>> queryByUserIdAndBookId(@RequestBody UserNotesInfoQuery query) {
        BaseResult<List<UserNotesInfoDO>> queryRes = notesInfoService.selectNoteByUserIdAndBookId(query);
        if (!queryRes.getSuccess()) {
            return BaseResult.errorReturn(StatusCodeEnum.SERVICE_ERROR.getCode(), "查询用户单本书阅读笔记");
        }

        List<UserNotesInfoVO> voList = noteLogic.convertDOsToVOs(queryRes.getData());
        return BaseResult.rightReturn(voList, queryRes.getPage());
    }


}
