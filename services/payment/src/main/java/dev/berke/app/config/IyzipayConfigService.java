package dev.berke.app.config;

import com.iyzipay.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IyzipayConfigService {

    @Autowired
    private Options iyzipayOptions;
}