import type { GlobConfig } from '/#/config';

import { getAppEnvConfig } from '/@/utils/env';

export const useGlobSetting = (): Readonly<GlobConfig> => {
  const { VITE_GLOB_APP_TITLE, VITE_GLOB_API_URL, VITE_GLOB_API_URL_PREFIX, VITE_GLOB_UPLOAD_URL, VITE_GLOB_TENANT_ID,
    VITE_GLOB_CLIENT_ID,
    VITE_GLOB_CLIENT_SECRET
 } = getAppEnvConfig();

  // Take global configuration
  const glob: Readonly<GlobConfig> = {
    title: VITE_GLOB_APP_TITLE,
    apiUrl: VITE_GLOB_API_URL,
    shortName: VITE_GLOB_APP_TITLE.replace(/\s/g, '_'),
    urlPrefix: VITE_GLOB_API_URL_PREFIX,
    uploadUrl: VITE_GLOB_UPLOAD_URL,
    tenantId: VITE_GLOB_TENANT_ID,
    clientId: VITE_GLOB_CLIENT_ID,
    clientSecret: VITE_GLOB_CLIENT_SECRET,
  };
  return glob as Readonly<GlobConfig>;
};
