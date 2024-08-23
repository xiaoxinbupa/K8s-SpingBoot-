package com.tarena.passport.protocol.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class TokenResult {
    @ApiModelProperty(name="jwt",value="登录认证jwt加密字符串")
    private String jwt;
    @ApiModelProperty(name="headerName",value="需要携带在header里使用的header名称")
    private String headerName;
    @ApiModelProperty(name="type",value="headerValue的前缀,如果是空不需要加前缀,非空时,前缀+空格+jwt携带")
    private String type;
    @ApiModelProperty(name = "nickname",value="用户昵称")
    private String nickname;
}
