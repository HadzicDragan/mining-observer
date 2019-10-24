package com.ad.miningobserver;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class EnvironmentSettingsLookupTest {
    
    private static final String USER_DIR = "user.dir";
    
    @Autowired
    private Environment env;
    
    @Test
    public void userDir_isPresent() {
        final String userDir = env.getProperty(USER_DIR);
        Assertions.assertThat(userDir)
                .isNotEmpty();
    }
}
