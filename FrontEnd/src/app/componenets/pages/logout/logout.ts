import { Component, } from '@angular/core';
import { AuthService } from '../../../services/auth-service';
import { Router } from '@angular/router';

@Component({
	selector: 'app-logout',
	imports: [],
    templateUrl: `./logout.html`,
	styleUrl: './logout.css',
})
export class Logout {

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
