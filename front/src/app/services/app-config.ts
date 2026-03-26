import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({ providedIn: 'root' })
export class AppConfig {
  private platformId = inject(PLATFORM_ID);
  appName = 'UniCar';

  refer(): string {
    if (isPlatformBrowser(this.platformId)) {
      return window.location.pathname;
    }
    return '/';
  }
}
