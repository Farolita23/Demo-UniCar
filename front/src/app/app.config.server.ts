import { mergeApplicationConfig, ApplicationConfig } from '@angular/core';
import { appConfig } from './app.config';

// SSR desactivado en angular.json (outputMode: static)
const serverConfig: ApplicationConfig = { providers: [] };

export const config = mergeApplicationConfig(appConfig, serverConfig);
