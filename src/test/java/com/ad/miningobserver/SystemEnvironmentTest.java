package com.ad.miningobserver;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class SystemEnvironmentTest {
    
//    @Test
//    public void testApplicationDirectory() {
//        SystemEnvironmentMockup mockup = new SystemEnvironmentMockup();
//        boolean presentProperty = mockup.isPropertySet(SystemEnvironment.getApplicationDirectory());
//        Assertions.assertThat(presentProperty)
//                .isTrue();
//    }
//    
//    @Test
//    public void testNoUserDirectoryPresent() {
//        System.clearProperty("user.dir");
//        Throwable exception = Assertions.catchThrowable(
//                () -> SystemEnvironment.getApplicationDirectory());
//        
//        Assertions.assertThat(exception).isInstanceOf(RuntimeException.class);
//        Assertions.assertThat(exception)
//                .hasMessageContaining("Unable to locate application location "
//                    + "by System properties.");
//    }
//    
//    private static final class SystemEnvironmentMockup {
//        public boolean isPropertySet(String value) {
//            return value != null;
//        }
//        
//        public boolean noProperty() {
//            return false;
//        }
//    }
}
