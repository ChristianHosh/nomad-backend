package com.nomad.socialspring.common.annotations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@ResponseBody
@ResponseStatus(HttpStatus.OK)
@Target(ElementType.METHOD)
public @interface ResponseOk {
}
