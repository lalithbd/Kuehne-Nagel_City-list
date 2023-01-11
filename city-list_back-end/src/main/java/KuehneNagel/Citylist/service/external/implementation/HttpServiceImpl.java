package KuehneNagel.Citylist.service.external.implementation;

import KuehneNagel.Citylist.service.external.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpServiceImpl implements HttpService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${external.max.attempt.count}")
    private Integer maxAttemptCount;

    @Override
    public byte[] getDataFromUrl(String link) {
        return call(link, 1);
    }

    private byte[] call(String url, int attempt) {
        if(attempt >= maxAttemptCount){
            return null;
        }
        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
            return responseEntity.getBody();

        } catch (RestClientException e) {
            return call(url, ++attempt);
        }
    }
}
