import { UserManager, WebStorageStateStore } from 'oidc-client-ts';

export const userManager = new UserManager({
  authority: import.meta.env.VITE_OIDC_AUTHORITY,
  client_id: import.meta.env.VITE_OIDC_CLIENT_ID,
  redirect_uri: import.meta.env.VITE_OIDC_REDIRECT_URI,
  response_type: 'code',
  scope: 'openid profile email',
  post_logout_redirect_uri: window.location.origin,
  userStore: new WebStorageStateStore({ store: window.localStorage }),
  automaticSilentRenew: true,
  // Disable metadata loading to avoid CORS issues
  loadUserInfo: false,
  metadata: {
    issuer: import.meta.env.VITE_OIDC_AUTHORITY,
    authorization_endpoint: 'https://is.schema31.it/oauth2/authorize',
    // Use local proxy for token endpoint to avoid CORS issues
    token_endpoint: `${window.location.origin}/oauth2/token`,
    end_session_endpoint: 'https://is.schema31.it/oidc/logout',
    jwks_uri: `${window.location.origin}/oauth2/jwks`,
  },
});
