package ua.dp.radiator.client.auth;

import com.google.common.base.Preconditions;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

public class BasicAutorisationRestClient extends RestTemplate {

    public BasicAutorisationRestClient(AuthTokenGenerator tokenGenerator) {
        Preconditions.checkNotNull(tokenGenerator);

        setRequestFactory(new InterceptingClientHttpRequestFactory(getRequestFactory(),
                Arrays.asList(new BasicAuthorizationInterceptor(tokenGenerator))));

    }

    private static class BasicAuthorizationInterceptor implements ClientHttpRequestInterceptor {


        private AuthTokenGenerator tokenGenerator;

        public BasicAuthorizationInterceptor(AuthTokenGenerator tokenGenerator) {
            this.tokenGenerator = tokenGenerator;
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

            request.getHeaders().add("Authorization", "Basic " + tokenGenerator.generateToken());

            return execution.execute(request, body);
        }
    }
}
