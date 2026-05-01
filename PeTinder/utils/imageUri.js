const trimQuotes = (value) => String(value ?? '').trim().replace(/^["']+|["']+$/g, '');

export const sanitizeBaseUrl = (value) => {
  const normalized = trimQuotes(value);
  return normalized.replace(/\/+$/g, '');
};

const isAbsoluteUri = (value) => /^[a-z][a-z0-9+.-]*:/.test(value);

export const getApiBaseUrl = () => {
  const env = process.env;
  const useBackend = String(env.EXPO_PUBLIC_UTILIZAR_BACKEND ?? '').toLowerCase() === 'true';
  const base = useBackend ? env.EXPO_PUBLIC_BACKEND_API_URL : env.EXPO_PUBLIC_JSON_SERVER_URL;
  return sanitizeBaseUrl(base);
};

export const getApiOrigin = () => {
  const baseUrl = getApiBaseUrl();
  if (!baseUrl) {
    return '';
  }

  try {
    return new URL(baseUrl).origin;
  } catch (error) {
    const match = String(baseUrl).match(/^(https?:\/\/[^/]+)/i);
    return match ? match[1] : baseUrl;
  }
};

export const resolveImageUri = (value, baseOrigin = getApiOrigin()) => {
  const raw = trimQuotes(value);

  if (!raw) {
    return '';
  }

  if (raw.startsWith('data:')) {
    return raw;
  }

  if (isAbsoluteUri(raw)) {
    return raw;
  }

  if (!baseOrigin) {
    return raw;
  }

  const normalizedBase = sanitizeBaseUrl(baseOrigin);

  if (raw.startsWith('/')) {
    return `${normalizedBase}${raw}`;
  }

  return `${normalizedBase}/${raw}`;
};

export default {
  sanitizeBaseUrl,
  getApiBaseUrl,
  getApiOrigin,
  resolveImageUri,
};
