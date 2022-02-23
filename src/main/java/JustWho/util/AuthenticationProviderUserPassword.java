package JustWho.util;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;

import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;
@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    @Inject
    EndpointSecurityConfigurationProperties endpointSecurityConfiguration;

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        final String user = endpointSecurityConfiguration.getUser();
        final String password = endpointSecurityConfiguration.getPassword();

        return Mono.<AuthenticationResponse>create(emitter -> {
            if (authenticationRequest.getIdentity().equals(user) && authenticationRequest.getSecret().equals(password)) {
                emitter.success(AuthenticationResponse.success("user"));
            } else {
                emitter.error(AuthenticationResponse.exception());
            }
        });
    }
}