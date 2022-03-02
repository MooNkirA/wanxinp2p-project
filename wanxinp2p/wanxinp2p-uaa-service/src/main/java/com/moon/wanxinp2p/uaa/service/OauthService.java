package com.moon.wanxinp2p.uaa.service;


import com.moon.wanxinp2p.uaa.domain.OauthClientDetails;
import com.moon.wanxinp2p.uaa.domain.OauthClientDetailsDto;

import java.util.List;

public interface OauthService {

    OauthClientDetails loadOauthClientDetails(String clientId);

    List<OauthClientDetailsDto> loadAllOauthClientDetailsDtos();

    void archiveOauthClientDetails(String clientId);

    OauthClientDetailsDto loadOauthClientDetailsDto(String clientId);

    void registerClientDetails(OauthClientDetailsDto formDto);

}