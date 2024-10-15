package com.bit.muiu.provider;

import java.util.Date;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    Date getBirth();
    String getGender();
    String getMobile();
}
