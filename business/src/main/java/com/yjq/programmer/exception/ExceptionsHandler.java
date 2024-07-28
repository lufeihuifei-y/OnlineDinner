package com.yjq.programmer.exception;


import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2020-11-16 22:23
 */

/**
 * 运行时触发异常捕获
 */
@ControllerAdvice
public class ExceptionsHandler {

    private  final Logger logger = LoggerFactory.getLogger(ExceptionsHandler.class);

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseDTO<Boolean> handle(RuntimeException e) {
        e.printStackTrace();
        if(!CommonUtil.isEmpty(e.getMessage())) {
            logger.info("异常信息={}", e.getMessage());
            if ("订单交易失败，请稍后重试！".equals(e.getMessage())) {
                return ResponseDTO.errorByMsg(CodeMsg.ORDER_ADD_ERROR);
            }
        }
        return ResponseDTO.errorByMsg(CodeMsg.SYSTEM_ERROR);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseDTO<Boolean> handle(MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return ResponseDTO.errorByMsg(CodeMsg.PHOTO_SURPASS_MAX_SIZE);
    }
}
