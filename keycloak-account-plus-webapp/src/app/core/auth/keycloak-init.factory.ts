import { KeycloakService } from 'keycloak-angular';
import { keycloakConfig } from './keycloak.config';

export function initializeKeycloak(keycloak: KeycloakService): () => Promise<boolean> {
  return () => keycloak.init(keycloakConfig);
}
