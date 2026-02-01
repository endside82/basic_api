package com.endside.api.user.model;


import com.endside.api.user.constants.LoginType;
import com.endside.api.user.constants.Os;
import com.endside.api.user.constants.ProviderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAddInfo {
    private LoginType loginType;
    private ProviderType providerType;
    private Os os;
}
