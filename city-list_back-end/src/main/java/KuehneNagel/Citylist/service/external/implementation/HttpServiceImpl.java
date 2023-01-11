package KuehneNagel.Citylist.service.external.implementation;

import KuehneNagel.Citylist.service.external.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class HttpServiceImpl implements HttpService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

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
            logger.error("Max attempt count reached and return: {}", url);
            return null;
        }
        URI uri;
        try {
            uri = getUrl(url);
        } catch (URISyntaxException e) {
            return null;
        }
        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(uri, byte[].class);
            return responseEntity.getBody();

        } catch (RestClientException e) {
            logger.error("File download error: {}", url);
            return call(url, ++attempt);
        }
    }

    private URI getUrl(String url) throws URISyntaxException {
        return new URI(url);
    }
}
