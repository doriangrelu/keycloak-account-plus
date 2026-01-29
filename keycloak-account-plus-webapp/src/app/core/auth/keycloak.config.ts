import { KeycloakOptions } from 'keycloak-angular';
import { environment } from '../../../environments/environment';

export const keycloakConfig: KeycloakOptions = {
  config: {
    url: environment.keycloak.url,
    realm: environment.keycloak.realm,
    clientId: environment.keycloak.clientId
  },
  initOptions: {
    onLoad: 'login-required',
    checkLoginIframe: false,
    pkceMethod: 'S256'
  },
  enableBearerInterceptor: true,
  bearerPrefix: 'Bearer',
  bearerExcludedUrls: ['/assets', '/public']
};
