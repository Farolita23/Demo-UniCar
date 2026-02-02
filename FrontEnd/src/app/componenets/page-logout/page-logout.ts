import { Component, } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';

@Component({
	selector: 'app-page-logout',
	imports: [],
    templateUrl: `./page-logout.html`,
	styleUrl: './page-logout.css',
})
export class PageLogout {

	constructor(
		private router: Router,
		private auth: AuthService
	) {}
	
	ngOnInit() {
        if(this.auth.isLoggedIn()) {
		    this.auth.removeToken();
        }
        this.router.navigate(["/"]);
	}

}
