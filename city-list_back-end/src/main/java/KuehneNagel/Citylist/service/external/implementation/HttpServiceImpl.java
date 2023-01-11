package KuehneNagel.Citylist.service.external.implementation;

import KuehneNagel.Citylist.service.external.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpServiceImpl implements HttpService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public byte[] getDataFromUrl(String link) {
        return call(link);
    }

    private byte[] call(String url) {
        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
            return responseEntity.getBody();

        } catch (RestClientException e) {
            return call(url);
        }
    }
}
