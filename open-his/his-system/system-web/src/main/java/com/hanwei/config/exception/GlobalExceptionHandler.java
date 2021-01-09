package com.hanwei.config.exception;

import com.hanwei.vo.AjaxResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

/**
 * @author hanwei
 * @ClassName GlobalExceptionHandler
 * @date 2020/9/26
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 当出现参数不合法时调用此方法
     * @param e MethodArgumentNotValidExceptions是传送json数据时，如果为空，会触发的报错
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public AjaxResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        return getAjaxResult(e.getBindingResult());
    }
    /**
     * 当系统出现BindException这个异常时，会调用下面的方法
     * @param e BindException非json数据传参，springMVC参数绑定数据验证错误
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public AjaxResult jsonErrorHandlerForParams(BindException e){
        return getAjaxResult(e.getBindingResult());
    }

    /**
     * 重新包装异常数据
     * @param bindingResult
     * @return
     */
    private AjaxResult getAjaxResult(BindingResult bindingResult) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError allError : allErrors) {
            Map<String, Object> map = new HashMap<>();
            map.put("defaultMessage", allError.getDefaultMessage());
            map.put("objectName", allError.getObjectName());
            //注意，这里面拿到具体的某一个属性
            FieldError fieldError = (FieldError) allError;
            map.put("field", fieldError.getField());
            list.add(map);
        }
        return AjaxResult.fail("后端数据校验异常", list);
    }
}