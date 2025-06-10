# VaultBerry App

Client app for [VaultBerry](https://github.com/Alexian123/VaultBerry), a self-hosted, secure and minimalist password manager.


## Initial Configuration Steps
1. Transfer the *cert.pem* file from the server to your mobile device;
2. Launch the app;
3. Tap the cloud icon in the top left corner to navigate to the API Config page;
4. Set the IP address and port of your private **VaultBerry** server and press save;
5. Import the *cert.pem* file from Step 1;
6. Restart the App. 


## Notable Features
- Self-hosted private cloud for password storage, intented for individual households
- Double encryption for sensitive data, once in the client (AES 256 GCM + PBKDF2) and once in the server (Fernet)
- Secure login using [SCRAM](https://en.wikipedia.org/wiki/Salted_Challenge_Response_Authentication_Mechanism)
- Account recovery using a randomly generated recovery password and a code sent via email
- 2FA with [TOTP](https://en.wikipedia.org/wiki/Time-based_one-time_password)
- [Autofill Service](https://developer.android.com/reference/android/service/autofill/AutofillService) to fill requests in compatible apps
- Biometric authentication
- Secure random password generator with configurable parameters
- Failsafe measures to prevent encrypted data loss while changing the password and/or master key
