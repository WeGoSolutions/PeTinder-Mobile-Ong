const CEP_REGEX = /^\d{5}-\d{3}$/;
const EMAIL_REGEX = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;
const HTTPS_URL_REGEX = /^https:\/\/.+/i;

export const validateCEP = (value) => CEP_REGEX.test(String(value ?? '').trim());

export const validateEmail = (value) => EMAIL_REGEX.test(String(value ?? '').trim());

export const validateURL = (value) => HTTPS_URL_REGEX.test(String(value ?? '').trim());

export default {
  validateCEP,
  validateEmail,
  validateURL,
};
