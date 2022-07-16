package locker.configuration;

import locker.exception.AppException;
import locker.service.crypto.CryptoService;
import locker.service.crypto.impl.AeadCryptoServiceImpl;
import locker.service.crypto.impl.DefaultCryptoServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BeanConfiguration {
    @Bean
    @Primary
    public CryptoService regular() {
        CryptoService service;
        try {
            service = new DefaultCryptoServiceImpl();
        } catch (AppException e) {
            throw new RuntimeException(e);
        }

        return service;
    }

    @Bean
    @Qualifier("AEAD")
    public CryptoService aead() {
        CryptoService service;
        try {
            service = new AeadCryptoServiceImpl();
        } catch (AppException e) {
            throw new RuntimeException(e);
        }

        return service;
    }
}
