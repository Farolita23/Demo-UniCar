import { Component } from '@angular/core';
import { AppConfig } from   '../../../services/app-config';
import { AuthService } from '../../../services/auth-service';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
	selector: 'app-footer',
	imports: [
        RouterLink, 
        RouterLinkActive
    ],
	templateUrl: './footer.html',
	styleUrl: './footer.css',
})
export class Footer {

	constructor(
		public config: AppConfig,
		public auth: AuthService
	) {}
	
}
