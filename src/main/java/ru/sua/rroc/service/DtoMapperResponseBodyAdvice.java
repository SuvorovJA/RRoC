package ru.sua.rroc.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;
import ru.sua.rroc.domain.Dto;

import java.util.Collection;

/**
 *  класс производит конвертацию типа полезной нагрузки для ResponseBody
 *  тех методов контроллера, которые помечены аннотацией @Dto.
 *
 *  класс является примером использования AOP
 */


@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class DtoMapperResponseBodyAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private ModelMapper modelMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType) && returnType.hasMethodAnnotation(Dto.class);
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer,
                                           MediaType contentType,
                                           MethodParameter returnType,
                                           ServerHttpRequest request,
                                           ServerHttpResponse response) {
        Dto annotation = returnType.getMethodAnnotation(Dto.class);
        Assert.state(annotation != null, "No Dto annotation");

        Class<?> dtoType = annotation.value();
        Object value = bodyContainer.getValue();
        Object returnValue;

        if (value instanceof Page) {
            returnValue = ((Page<?>) value).map(it -> modelMapper.map(it, dtoType));
        } else if (value instanceof Collection) {
            returnValue = ((Collection<?>) value).stream().map(it -> modelMapper.map(it, dtoType));
        } else {
            returnValue = modelMapper.map(value, dtoType);
            log.info("CLASS OF SINGLE MAPPED VALUE IS \'{}\'", returnValue.getClass());
        }

        bodyContainer.setValue(returnValue);
    }
}
