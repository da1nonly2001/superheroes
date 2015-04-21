package superheroes.service;

import org.springframework.stereotype.Service;

/**
 * Created by catop on 4/7/15.
 */
@Service
public interface Authorization {

    public String getApiKey();

    public Long getTimestamp();

    public String getMd5Hash();
}
