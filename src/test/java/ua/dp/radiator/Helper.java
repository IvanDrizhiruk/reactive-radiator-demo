package ua.dp.radiator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import reactor.core.publisher.Mono;

public class Helper {

	public static WebClient mockWebClient(final Mono<?> mono) {
		WebClient originalClient = WebClient.create();
		WebClient client = spy(originalClient);

		WebClient.UriSpec uriSpec = mock(WebClient.UriSpec.class);
		doReturn(uriSpec).when(client).get();

		WebClient.RequestHeadersSpec<?> headerSpec = mock(WebClient.RequestHeadersSpec.class);
		doReturn(headerSpec).when(uriSpec).uri(anyString());
		doReturn(headerSpec).when(headerSpec).header(any(), any());
		doReturn(headerSpec).when(headerSpec).accept(any());
		doReturn(headerSpec).when(headerSpec).acceptCharset(any());

		ResponseSpec responseSpec = mock(ResponseSpec.class);
		doReturn(responseSpec).when(headerSpec).retrieve();
		doReturn(mono).when(responseSpec).bodyToMono(any());

		ClientResponse clientResponse = mock(ClientResponse.class);
		doReturn(mono).when(clientResponse).bodyToMono(any());
		doReturn(Mono.just(clientResponse)).when(headerSpec).exchange();

		return client;
	}
}
