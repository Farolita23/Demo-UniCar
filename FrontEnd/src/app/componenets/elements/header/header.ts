import { Component } from '@angular/core';
import { AppConfig } from '../../../services/app-config';
import { AuthService } from '../../../services/auth-service';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
	selector: 'app-header',
	imports: [
        RouterLink, 
        RouterLinkActive
    ],
	templateUrl: './header.html',
	styleUrl: './header.css',
})
export class Header {
	
	constructor(
		public config: AppConfig, 
		public auth: AuthService
	) {}

	showNav: boolean = false;
	alternateNav() {
		this.showNav = !this.showNav;
	}
}
