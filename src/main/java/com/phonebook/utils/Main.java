package com.phonebook.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.phonebook.rest.LoggingRequestInterceptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        PolicyRequest policyRequest = new PolicyRequest();
        policyRequest.setDate(ZonedDateTime.now());
        policyRequest.setControlFlags(1);
        policyRequest.setFromId("1");
        List<String> ids = Stream.of("1", "2").collect(Collectors.toList());
        policyRequest.setIds(ids);


        //System.out.println(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        ObjectMapper om =  new ObjectMapper();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JavaTimeModule jm = new JavaTimeModule();
        jm.addSerializer(ZonedDateTime.class, new LocalDateSerializer());
        jm.addDeserializer(ZonedDateTime.class, new LocalDateDeserializer());
        om.registerModule(jm);


        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(om);
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(converter);
        RestTemplate restTemplate = new RestTemplate(converters);

        restTemplate.getMessageConverters().add(converter);
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LoggingRequestInterceptor());
        restTemplate.setInterceptors(interceptors);

        HttpEntity<PolicyRequest> request = new HttpEntity<>(policyRequest, getHttpHeaders());
        ResponseEntity<Object> response = restTemplate.exchange("http://test-insurancesws.vtb.ge/api/Insurance/PolicyList", HttpMethod.POST, request, Object.class);
        LinkedHashMap<String, Object> body = (LinkedHashMap<String, Object>) response.getBody();

    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //httpHeaders.setContentLength(170);
        //httpHeaders.add("Authenticate", "Basic eTEST_TOKEN_FUc3Jo2HlkjwoeirHJLLksd35wJLiuhINVALI0");
        //httpHeaders.add("WWW-Authenticate", "Basic eTEST_TOKEN_FUc3Jo2HlkjwoeirHJLLksd35wJLiuhINVALID0");
        //httpHeaders.add("Host","217.147.236.136");
        return  httpHeaders;
    }
}
