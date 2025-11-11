package com.example.javaeloadasbeadando.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import soapclient.MNBArfolyamServiceSoapImpl;

@Configuration
public class MnbConfig {

        @Bean
        public Jaxb2Marshaller marshaller() {
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setPackagesToScan("soapclient");
            return marshaller;
        }

        @Bean
        public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) {
            WebServiceTemplate template = new WebServiceTemplate();
            template.setMarshaller(marshaller);
            template.setUnmarshaller(marshaller);
            return template;
        }

        @Bean
        public MNBArfolyamServiceSoapImpl mnbService() {
            return new MNBArfolyamServiceSoapImpl();
        }
}
